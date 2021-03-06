
package wacc;

import antlr.WACCLexer;
import antlr.WACCParser;
import antlr.WACCParserBaseVisitor;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.TerminalNode;
import wacc.instructions.*;
import wacc.instructions.expressions.ExprInstruction;
import wacc.instructions.expressions.baseExpressions.*;
import wacc.instructions.expressions.binaryExpressions.BinaryExprInstruction;
import wacc.instructions.expressions.binaryExpressions.arithmeticExpressions.*;
import wacc.instructions.expressions.binaryExpressions.comparatorExpressions.*;
import wacc.instructions.expressions.binaryExpressions.logicalExpressions.ANDInstruction;
import wacc.instructions.expressions.binaryExpressions.logicalExpressions.ORInstruction;
import wacc.instructions.expressions.unaryExpressions.*;
import wacc.types.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class BackendVisitor extends WACCParserBaseVisitor<Instruction> {

    private final ScopedSymbolTable symbolTable;
    private final MemoryStack stack;
    private final HashMap<String, StructType> structs;

    private List<DataInstruction> data = new ArrayList<>();
    private List<LabelInstruction> labels = new ArrayList<>();

    private HashMap<String,String> dataMap = new HashMap<>();
    private HashMap<String,String> identifier = new HashMap<>();

    private int currentReg;


    public BackendVisitor(ScopedSymbolTable symbolTable) {
        this.symbolTable = symbolTable;
        this.stack = new MemoryStack();
        this.structs = new HashMap<>();
        this.currentReg = 4;
    }

    public void addDataAndLabels(ContainingDataOrLabelsInstruction ins){
        ArrayList<DataInstruction> dataInstructions = ins.getData();
        ArrayList<LabelInstruction> labelInstructions = ins.getLabel();

        if (dataInstructions != null){
            for (DataInstruction dataInstruction : dataInstructions){
                if (!data.contains(dataInstruction)){
                    data.add(dataInstruction);
                }
            }
        }

        if (labelInstructions != null){
            for (LabelInstruction labelIns : labelInstructions){
                if (!labels.contains(labelIns)){
                    labels.add(labelIns);
                }
            }
        }
    }


    @Override
    public Instruction visitProgram(@NotNull WACCParser.ProgramContext ctx) {
        // Visit functions then visit program.

        for(WACCParser.StructContext s: ctx.struct()) {
            visit(s);
        }

        List<FunctionInstruction> funcs = new LinkedList<>();
        for(WACCParser.FunctionContext f: ctx.function()) {
            funcs.add((FunctionInstruction) visit(f));
        }

        stack.newScope();
        Instruction ins = visit(ctx.stat());
        int scopeSize = stack.descope();
        ProgramInstruction program = new ProgramInstruction(ins, scopeSize);

        return new AssemblyInstruction(data, funcs, program, labels);
    }

    @Override
    public Instruction visitFunction(@NotNull WACCParser.FunctionContext ctx) {

        String functionLabel = LabelMaker.getFunctionLabel(ctx.identifier().getText());


        //map params to location on stack
        List<WACCParser.ParamContext> params = new LinkedList<>();

        if(ctx.paramList() != null) {
            params = ctx.paramList().param();
        }

        stack.newScope();

        for (WACCParser.ParamContext param: params) {
            String paramIdentifier = param.identifier().getText();
            WACCParser.TypeContext type = param.type();
            stack.add(paramIdentifier, parseType(type));
        }

        // error string that symbolises the branch link for the function
        stack.add("@!$%", new NullType());
        stack.newScope();
        Instruction statement = visit(ctx.stat());

        int scopeSize = stack.descope();

        stack.descope();
        return new FunctionInstruction(functionLabel, statement, scopeSize);
    }

    @Override
    public Instruction visitCallFunction(@NotNull WACCParser.CallFunctionContext ctx) {
        // arglist adds args to stack
        List<ExprInstruction> args = new LinkedList<>();
        List<WACCParser.ExprContext> exprs = new LinkedList<>();

        if(ctx.argList() != null) {
            exprs = ctx.argList().expr();
        }

        stack.newScope();

        currentReg++;
        for (WACCParser.ExprContext expr : exprs) {
            ExprInstruction exprIns = (ExprInstruction) visit(expr);
            args.add(exprIns);
            stack.add(expr.getText(), exprIns.getType());
        }
        currentReg--;

        stack.descope();

        // get corresponding function label of function

        if (ctx.derefLHS() != null) {
            DerefIdentLHSInstruction ins = (DerefIdentLHSInstruction) visit(ctx.derefLHS());
            return new CallFunctionInstruction(ins, args);
        } else {
            String functionLabel = LabelMaker.getFunctionLabel(ctx.identifier().getText());
            return new CallFunctionInstruction(functionLabel, args);
        }
    }

    @Override
    public Instruction visitReturnStat(@NotNull WACCParser.ReturnStatContext ctx) {
        return new ReturnInstruction((ExprInstruction) visit(ctx.expr()));
    }

    @Override
    public Instruction visitStruct(@NotNull WACCParser.StructContext ctx) {
        String id = ctx.identifier(0).getText();
        List<Type> typeList = new ArrayList<>();
        List<String> idList = new ArrayList<>();
        //The lists are passed by reference and filled after the fact
        structs.put(id, new StructType(id, typeList, idList));
        for(int i = 1; i < ctx.identifier().size(); i++) {
            idList.add(ctx.identifier(i).getText());
            typeList.add(parseFixedSizeType(ctx.fixedSizeType(i - 1)));
        }
        return null;
    }

    @Override
    public Instruction visitStructContentsExpr(@NotNull WACCParser.StructContentsExprContext ctx) {
        if(ctx.structContentsExpr() != null) {
            return visit(ctx.structContentsExpr());
        }
        return visitChildren(ctx);
    }

    @Override
    public Instruction visitStructContents(@NotNull WACCParser.StructContentsContext ctx) {
        WACCParser.StructContentsExprContext structExpr = ctx.structContentsExpr();

        ExprInstruction getStructIns = (ExprInstruction) visit(structExpr);

        List<String> fieldIds = new ArrayList<>();
        for(WACCParser.IdentifierContext id: ctx.identifier()) {
            fieldIds.add(id.getText());
        }

        if(ctx.getParent() instanceof WACCParser.AssignLHSContext) {
            return new StructContentsLHSInstruction(getStructIns, fieldIds);
        } else {
            return new StructContentsExprInstruction(getStructIns, fieldIds, currentReg);
        }
    }

    @Override
    public Instruction visitSeqStat(@NotNull WACCParser.SeqStatContext ctx) {
        Instruction stat1 = visit(ctx.stat(0));
        Instruction stat2 = visit(ctx.stat(1));
        return new SequenceInstruction(stat1, stat2);
    }

    @Override
    public Instruction visitInitAssignStat(@NotNull WACCParser.InitAssignStatContext ctx) {
        String var = ctx.identifier().getText();
        LocatableInstruction expr = (LocatableInstruction) visit(ctx.assignRHS());
        WACCParser.TypeContext type = ctx.type();

        Type varType = parseType(type);
        if (varType.checkType(PrimType.STRING)){
            StringLiterInstruction string = (StringLiterInstruction)expr;
            String stringValue = string.getStringLiter();
            identifier.put(var,stringValue);
        }
        stack.add(var, varType);
        if(expr.getType() instanceof StructType && !(expr instanceof StructListInstruction)) {
            return new InitAssignStructInstruction(expr, stack.getOffsetString(var));
        }
        return new InitAssignInstruction(expr, stack.getOffsetString(var));
    }

    private Type parseFixedSizeType(WACCParser.FixedSizeTypeContext type) {

        Type varType;

        if (type.ptrType() != null) {
            varType = parsePtrType(type.ptrType());
        } else if (type.baseType() != null) {
            varType = parseBaseType(type.baseType());
        } else {
            varType = parseStructType(type.structType());
        }

        return varType;
    }

    private Type parseType(WACCParser.TypeContext type) {

        Type varType;

        if(type.arrayType() != null) {
            varType = parseArrayType(type.arrayType());
        } else if(type.pairType() != null) {
            varType = parsePairType(type.pairType());
        } else if (type.ptrType() != null) {
            varType = parsePtrType(type.ptrType());
        } else if (type.baseType() != null) {
            varType = parseBaseType(type.baseType());
        } else if (type.funcPtrType() != null) {
            varType = parseFuncPtrType(type.funcPtrType());
        } else {
            varType = parseStructType(type.structType());
        }

        return varType;

    }

    private Type parseStructType(@NotNull WACCParser.StructTypeContext type) {
        String id = type.identifier().getText();
        return structs.get(id);
    }


    private Type parseBaseType(@NotNull WACCParser.BaseTypeContext type) {

        Type varType;

        int t = ((TerminalNode) type.getChild(0)).getSymbol().getType();

        if(t == WACCLexer.BOOL_TYPE) {
            varType = PrimType.BOOL;
        } else if (t == WACCLexer.INT_TYPE) {
            varType = PrimType.INT;
        } else if (t == WACCLexer.CHAR_TYPE) {
            varType = PrimType.CHAR;
        } else {
            varType = PrimType.STRING;
        }


        return varType;
    }

    private Type parseArrayType(@NotNull WACCParser.ArrayTypeContext type) {

        Type varType;
        int dimension = type.CLOSE_SQUARE().size();


        if(type.pairType() != null) {
            varType = parsePairType(type.pairType());
        } else {
            varType = parseBaseType(type.baseType());
        }

        for (int i = 0; i < dimension; i++) {
            varType = new ArrayType(varType);
        }

        return varType;
    }

    private Type parsePairType(@NotNull WACCParser.PairTypeContext type) {

        Type type1;
        Type type2;
        WACCParser.PairElemTypeContext t1 = type.pairElemType(0);
        WACCParser.PairElemTypeContext t2 = type.pairElemType(1);

        if(t1.baseType() != null) {
            type1 = parseBaseType(t1.baseType());
        } else if (t1.arrayType() != null) {
            type1 = parseArrayType(t1.arrayType());
        } else if (t1.ptrType() != null) {
            type1 = parsePtrType(t1.ptrType());
        } else {
            type1 = new NullType();
        }

        if(t2.baseType() != null) {
            type2 = parseBaseType(t2.baseType());
        } else if (t2.arrayType() != null) {
            type2 = parseArrayType(t2.arrayType());
        } else if (t2.ptrType() != null) {
            type2 = parsePtrType(t2.ptrType());
        } else {
            type2 = new NullType();
        }

        return new PairType(type1, type2);
    }

    private Type parsePtrType(@NotNull WACCParser.PtrTypeContext type) {
        WACCParser.PtrBaseTypeContext ctx2 = type.ptrBaseType();
        Type varType = parsePtrBaseType(ctx2);
        int ptrNum = type.MULTIPLY().size();

        for (int i = 0; i < ptrNum; i++) {
            varType = new PtrType(varType);
        }

        return varType;
    }

    private Type parsePtrBaseType(@NotNull WACCParser.PtrBaseTypeContext type) {
        Type varType;

        if(type.arrayType() != null) {
            varType = parseArrayType(type.arrayType());
        } else if(type.pairType() != null) {
            varType = parsePairType(type.pairType());
        } else if (type.baseType() != null) {
            varType = parseBaseType(type.baseType());
        } else {
            varType =parseStructType(type.structType());
        }

        return varType;
    }

    private Type parseFuncPtrType(@NotNull WACCParser.FuncPtrTypeContext ctx) {
        return new PtrType(new FunctionType());
    }

    @Override
    public Instruction visitAssignStat(@NotNull WACCParser.AssignStatContext ctx) {
        AssignLHSInstruction lhs = ((AssignLHSInstruction) visitAssignLHS(ctx.assignLHS()));

        if (lhs.usesRegister()) {
            currentReg++;
        }
        LocatableInstruction rhs = ((LocatableInstruction) visit(ctx.assignRHS()));
        if (lhs.usesRegister()) {
            currentReg--;
        }
        if(rhs.getType() instanceof StructType && !(rhs instanceof StructListInstruction)) {
            return new StructAssignInstruction(lhs, rhs, currentReg + 2);
        }

        return new AssignInstruction(lhs, rhs);
    }

    @Override
    public Instruction visitBlockStat(@NotNull WACCParser.BlockStatContext ctx) {
        stack.newScope();
        Instruction ins = visit(ctx.stat());
        int scopeSize = stack.descope();
        return new BlockInstruction(ins, scopeSize);
    }

    @Override
    public Instruction visitWhileStat(@NotNull WACCParser.WhileStatContext ctx) {
        ExprInstruction expr = (ExprInstruction) visit(ctx.expr());
        stack.newScope();
        Instruction stat = visit(ctx.stat());
        int scopeSize = stack.descope();
        return new WhileInstruction(expr, stat, scopeSize, getIdentOfStat(ctx));
    }

    private int getIdentOfStat(@NotNull WACCParser.StatContext ctx) {
        return ctx.hashCode();
    }

    private WACCParser.StatContext getParentStatContext(@NotNull WACCParser.StatContext ctx) {
        WACCParser.StatContext parent = (WACCParser.StatContext) ctx.getParent();
        while (!(parent instanceof WACCParser.WhileStatContext || parent instanceof WACCParser.ForStatContext)){
            parent = (WACCParser.StatContext) parent.getParent();
        }
        return parent;
    }

    @Override
    public Instruction visitBreakStat(@NotNull WACCParser.BreakStatContext ctx){
        WACCParser.StatContext parent = getParentStatContext(ctx);
        return new BreakInstruction(getIdentOfStat(parent));
    }

    @Override
    public Instruction visitContinueStat(@NotNull WACCParser.ContinueStatContext ctx){
        WACCParser.StatContext parent = getParentStatContext(ctx);
        return new ContinueInstruction(getIdentOfStat(parent));
    }

    // TODO: Add break and continue to for loops
    @Override
    public Instruction visitForStat(@NotNull WACCParser.ForStatContext ctx) {
        stack.newScope();
        String id = ctx.identifier().getText();

        ExprInstruction startExpr = (ExprInstruction) visit(ctx.expr(0));

        stack.add(id, PrimType.INT);
        String locationString = stack.getOffsetString(id);
        Type type = stack.getType(id);

        InitAssignInstruction counterInit = new InitAssignInstruction(startExpr, locationString);
        ExprInstruction idExpr = new IdentifierExprInstruction(locationString, type, currentReg);
        currentReg++;
        ExprInstruction endExpr =  (ExprInstruction) visit(ctx.expr(1));
        ExprInstruction increment = (ExprInstruction) visit(ctx.expr(2));
        currentReg--;

        BinaryExprInstruction addIncrement = new PlusInstruction(idExpr, increment, currentReg, dataMap);

        dataMap = addIncrement.setCheckError();
        ContainingDataOrLabelsInstruction dataAndLabels = addIncrement.getErrorPrint();
        addDataAndLabels(dataAndLabels);

        LocatableInstruction idLHS = (LocatableInstruction) visit(ctx.identifier());
        AssignInstruction incrementId = new AssignInstruction(idLHS, addIncrement);

        Instruction stat = visit(ctx.stat());
        
        int scopeSize = stack.descope();
        return new ForInstruction(counterInit, idExpr, endExpr, stat, incrementId, scopeSize, getIdentOfStat(ctx));
    }

    @Override
    public Instruction visitIfStat(@NotNull WACCParser.IfStatContext ctx) {
        ExprInstruction expr = (ExprInstruction) visit(ctx.expr());
        if (expr instanceof BoolLiterInstruction) {
            boolean exprValue = ((BoolLiterInstruction) expr).getValue();
            if (exprValue) {
                return visit(ctx.stat(0));
            } else {
                return visit(ctx.stat(1));
            }
        }
        stack.newScope();
        Instruction stat1 = visit(ctx.stat(0));
        int scopeSize1 = stack.descope();
        stack.newScope();
        Instruction stat2 = visit(ctx.stat(1));
        int scopeSize2 = stack.descope();
        IfInstruction ins = new IfInstruction(expr, stat1, stat2, scopeSize1, scopeSize2, getIdentOfStat(ctx));
        return ins;
    }

    @Override
    public Instruction visitReadStat(@NotNull WACCParser.ReadStatContext ctx) {
        AssignLHSInstruction assignLHSInstruction = (AssignLHSInstruction) visitAssignLHS(ctx.assignLHS());
        ReadInstruction readInstruction = new ReadInstruction(assignLHSInstruction,dataMap);
        dataMap = readInstruction.addDataAndLabels();
        addDataAndLabels(readInstruction.getDataAndLabels());
        return readInstruction;
    }

    @Override
    public Instruction visitRefLHS(@NotNull WACCParser.RefLHSContext ctx) {
        if (ctx.funcIdent() != null) {
            String ident = ctx.funcIdent().IDENTIFIER().getText();
            LocatableInstruction ins = new FuncRefIdentInstruction(ident, currentReg, new PtrType(new FunctionType()));
            AssignLHSInstruction ins2 = new AssignLHSInstruction(ins);
            return new RefIdentInstruction(currentReg, ins2);
        }

        AssignLHSInstruction ins = (AssignLHSInstruction) visit(ctx.assignLHS());
        Type type = ins.getType();

        return new RefIdentInstruction(currentReg, ins);
    }

    @Override
    public Instruction visitPrintStat(@NotNull WACCParser.PrintStatContext ctx) {
        ExprInstruction expr = (ExprInstruction) visitExpr(ctx.expr());
        PrintInstruction print = new PrintInstruction(expr,dataMap);
        dataMap = print.addDataAndLabels();
        addDataAndLabels(print.getDataAndLabels());
        return print;
    }

    @Override
    public Instruction visitPrintlnStat(@NotNull WACCParser.PrintlnStatContext ctx) {
        ExprInstruction expr = (ExprInstruction) visitExpr(ctx.expr());
        PrintlnInstruction print = new PrintlnInstruction(expr,dataMap);
        dataMap = print.addDataAndLabels();
        addDataAndLabels(print.getDataAndLabels());
        return print;
    }

    @Override
    public Instruction visitFreeStat(@NotNull WACCParser.FreeStatContext ctx) {
        ExprInstruction expr = (ExprInstruction) visit(ctx.expr());
        FreeInstruction freeInstruction= new FreeInstruction(expr,dataMap);
        dataMap = freeInstruction.setCheckError();
        ContainingDataOrLabelsInstruction dataAndLabels = freeInstruction.getErrorPrint();
        addDataAndLabels(dataAndLabels);
        return freeInstruction;
    }

    @Override
    public Instruction visitExitStat(@NotNull WACCParser.ExitStatContext ctx) {
        ExprInstruction exInst = (ExprInstruction) (visitExpr(ctx.expr()));
        return new ExitInstruction(exInst);
    }

    //TODO: Const eval
    @Override
    public Instruction visitUnaryExpr(@NotNull WACCParser.UnaryExprContext ctx) {
        int op = ((TerminalNode) ctx.unaryOper().getChild(0)).getSymbol().getType();
        ExprInstruction i = (ExprInstruction) visit(ctx.expr1());
        if(op == WACCLexer.NOT) {
            return new NotInstruction(i, currentReg);
        } else if (op == WACCLexer.MINUS) {

            NegInstruction negInstruction = new NegInstruction(i, currentReg,dataMap);
            dataMap = negInstruction.setCheckError();
            addDataAndLabels(negInstruction.getDataAndLabels());
            return negInstruction;
        } else if (op == WACCLexer.LEN){
            return new LenInstruction(i, currentReg);
        } else if (op == WACCLexer.ORD) {
            return new OrdInstruction(i, currentReg);
        } else {
            return new ChrInstruction(i, currentReg);
        }
    }

    @Override
    public Instruction visitBracketsExpr(@NotNull WACCParser.BracketsExprContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Instruction visitExpr6(@NotNull WACCParser.Expr6Context ctx) {
        WACCParser.Expr5Context e = ctx.expr5();
        if (e != null) {
            return visit(e);
        }
        boolean capped = true;
        ExprInstruction i1 = (ExprInstruction) visit(ctx.expr6(0));
        if (currentReg < 10) {
            currentReg++;
            capped = false;
        } else {
            stack.newScope();
            stack.add("", null);
        }
        ExprInstruction i2 = (ExprInstruction) visit(ctx.expr6(1));
        if (capped) {
            stack.descope();
        } else {
            currentReg--;
        }

        boolean lhsIsConst = isConst(i1);
        boolean rhsIsConst = isConst(i2);

        if (lhsIsConst && rhsIsConst) {
            boolean lhsValue = ((BoolLiterInstruction) i1).getValue();
            boolean rhsValue = ((BoolLiterInstruction) i2).getValue();
            return new BoolLiterInstruction(lhsValue || rhsValue, currentReg);
        } else if (lhsIsConst) {
            boolean lhsValue = ((BoolLiterInstruction) i1).getValue();
            if (lhsValue) {
                return new BoolLiterInstruction(true, currentReg);
            } else {
                return i2;
            }
        } else if (rhsIsConst) {
            boolean rhsValue = ((BoolLiterInstruction) i2).getValue();
            if (rhsValue) {
                return new BoolLiterInstruction(true, currentReg);
            } else {
                return i1;
            }
        } else {
            return new ORInstruction(i1, i2, currentReg);
        }

    }

    @Override
    public Instruction visitExpr5(@NotNull WACCParser.Expr5Context ctx) {
        WACCParser.Expr4Context e = ctx.expr4();
        if(e != null) {
            return visit(e);
        }
        boolean capped = true;
        ExprInstruction i1 = (ExprInstruction) visit(ctx.expr5(0));
        if(currentReg < 10) {
            currentReg++;
            capped = false;
        } else {
            stack.newScope();
            stack.add("", null);
        }
        ExprInstruction i2 = (ExprInstruction) visit(ctx.expr5(1));
        if(capped) {
            stack.descope();
        } else {
            currentReg--;
        }

        boolean lhsIsConst = isConst(i1);
        boolean rhsIsConst = isConst(i2);

        if (lhsIsConst && rhsIsConst) {
            boolean lhsValue = ((BoolLiterInstruction) i1).getValue();
            boolean rhsValue = ((BoolLiterInstruction) i2).getValue();
            return new BoolLiterInstruction(lhsValue && rhsValue, currentReg);
        } else if (lhsIsConst) {
            boolean lhsValue = ((BoolLiterInstruction) i1).getValue();
            if (lhsValue) {
                return i2;
            } else {
                return new BoolLiterInstruction(false, currentReg);
            }
        } else if (rhsIsConst) {
            boolean rhsValue = ((BoolLiterInstruction) i2).getValue();
            if (rhsValue) {
                return i2;
            } else {
                return new BoolLiterInstruction(false, currentReg);
            }
        } else {
            return new ANDInstruction(i1, i2, currentReg);
        }
    }

    @Override
    public Instruction visitExpr4(@NotNull WACCParser.Expr4Context ctx) {
        WACCParser.Expr3Context e = ctx.expr3();
        if(e != null) {
            return visit(e);
        }
        boolean capped = true;
        ExprInstruction i1 = (ExprInstruction) visit(ctx.expr4(0));
        if(currentReg < 10) {
            currentReg++;
            capped = false;
        } else {
            stack.newScope();
            stack.add("", null);
        }
        ExprInstruction i2 = (ExprInstruction) visit(ctx.expr4(1));
        if(capped) {
            stack.descope();
        } else {
            currentReg--;
        }

        boolean lhsIsConst = isConst(i1);
        boolean rhsIsConst = isConst(i2);

        int op = ((TerminalNode) ctx.binaryOper4().getChild(0)).getSymbol().getType();

        if (lhsIsConst && rhsIsConst) {
            if (i1 instanceof IntLiterInstruction) {
                int lhsValue = ((IntLiterInstruction) i1).getValue();
                int rhsValue = ((IntLiterInstruction) i2).getValue();
                if(op == WACCLexer.EQ) {
                    return new BoolLiterInstruction(lhsValue == rhsValue, currentReg);
                } else {
                    return new BoolLiterInstruction(lhsValue != rhsValue, currentReg);
                }
            } else if (i1 instanceof CharLiterInstruction) {
                char lhsValue = ((CharLiterInstruction) i1).getValue();
                char rhsValue = ((CharLiterInstruction) i2).getValue();
                if(op == WACCLexer.EQ) {
                    return new BoolLiterInstruction(lhsValue == rhsValue, currentReg);
                } else {
                    return new BoolLiterInstruction(lhsValue != rhsValue, currentReg);
                }
            } else {
                boolean lhsValue = ((BoolLiterInstruction) i1).getValue();
                boolean rhsValue = ((BoolLiterInstruction) i2).getValue();
                if(op == WACCLexer.EQ) {
                    return new BoolLiterInstruction(lhsValue == rhsValue, currentReg);
                } else {
                    return new BoolLiterInstruction(lhsValue != rhsValue, currentReg);
                }
            }
        } else {
            if (op == WACCLexer.EQ) {
                return new EQInstruction(i1, i2, currentReg);
            } else {
                return new NEQInstruction(i1, i2, currentReg);
            }
        }


    }

    @Override
    public Instruction visitExpr3(@NotNull WACCParser.Expr3Context ctx) {
        WACCParser.Expr2Context e = ctx.expr2();
        if(e != null) {
            return visit(e);
        }
        boolean capped = true;
        ExprInstruction i1 = (ExprInstruction) visit(ctx.expr3(0));
        if(currentReg < 10) {
            currentReg++;
            capped = false;
        } else {
            stack.newScope();
            stack.add("", null);
        }
        ExprInstruction i2 = (ExprInstruction) visit(ctx.expr3(1));
        if(capped) {
            stack.descope();
        } else {
            currentReg--;
        }

        boolean lhsIsConst = isConst(i1);
        boolean rhsIsConst = isConst(i2);

        int op = ((TerminalNode) ctx.binaryOper3().getChild(0)).getSymbol().getType();

        if (lhsIsConst && rhsIsConst) {
            if (i1 instanceof  IntLiterInstruction) {
                int lhsValue = ((IntLiterInstruction) i1).getValue();
                int rhsValue = ((IntLiterInstruction) i2).getValue();
                if (op == WACCLexer.GT) {
                    return new BoolLiterInstruction(lhsValue > rhsValue, currentReg);
                } else if (op == WACCLexer.LT) {
                    return new BoolLiterInstruction(lhsValue < rhsValue, currentReg);
                } else if (op == WACCLexer.LEQ) {
                    return new BoolLiterInstruction(lhsValue <= rhsValue, currentReg);
                } else {
                    return new BoolLiterInstruction(lhsValue >= rhsValue, currentReg);
                }
            } else {
                char lhsValue = ((CharLiterInstruction) i1).getValue();
                char rhsValue = ((CharLiterInstruction) i2).getValue();
                if (op == WACCLexer.GT) {
                    return new BoolLiterInstruction(lhsValue > rhsValue, currentReg);
                } else if (op == WACCLexer.LT) {
                    return new BoolLiterInstruction(lhsValue < rhsValue, currentReg);
                } else if (op == WACCLexer.LEQ) {
                    return new BoolLiterInstruction(lhsValue <= rhsValue, currentReg);
                } else {
                    return new BoolLiterInstruction(lhsValue >= rhsValue, currentReg);
                }
            }
        } else {
            if (op == WACCLexer.GT) {
                return new GTInstruction(i1, i2, currentReg);
            } else if (op == WACCLexer.LT) {
                return new LTInstruction(i1, i2, currentReg);
            } else if (op == WACCLexer.LEQ) {
                return new LEQInstruction(i1, i2, currentReg);
            } else {
                return new GEQInstruction(i1, i2, currentReg);
            }
        }

    }

    @Override
    public Instruction visitExpr2(@NotNull WACCParser.Expr2Context ctx) {
        WACCParser.Expr1Context e = ctx.expr1();
        if(e != null) {
            return visit(e);
        }

        boolean capped = true;
        ExprInstruction i1 = (ExprInstruction) visit(ctx.expr2(0));
        if (currentReg < 10) {
            currentReg++;
            capped = false;
        } else {
            stack.newScope();
            stack.add("", null);
        }

        ExprInstruction i2 = (ExprInstruction) visit(ctx.expr2(1));
        if (capped) {
            stack.descope();
        } else {
            currentReg--;
        }

        boolean lhsIsConst = isConst(i1);
        boolean rhsIsConst = isConst(i2);
        int op = ((TerminalNode) ctx.binaryOper2().getChild(0)).getSymbol().getType();

        if (lhsIsConst && rhsIsConst) {
            IntLiterInstruction intLiter;
            int lhsValue = ((IntLiterInstruction) i1).getValue();
            int rhsValue = ((IntLiterInstruction) i2).getValue();
            if (op == WACCLexer.PLUS) {
                intLiter = new IntLiterInstruction(lhsValue + rhsValue, currentReg);
            } else {
                intLiter = new IntLiterInstruction(lhsValue - rhsValue, currentReg);
            }
            return intLiter;
        } else {
            BinaryExprInstruction binOp;
            if (op == WACCLexer.PLUS) {
                binOp = new PlusInstruction(i1, i2, currentReg, dataMap);
            } else {
                binOp = new MinusInstruction(i1, i2, currentReg, dataMap);
            }
            dataMap = binOp.setCheckError();
            ContainingDataOrLabelsInstruction dataAndLabels = binOp.getErrorPrint();
            addDataAndLabels(dataAndLabels);
            return binOp;
        }
    }

    @Override
    public Instruction visitExpr1(@NotNull WACCParser.Expr1Context ctx) {

        if (ctx.getChildCount() == 1) {
            return visit(ctx.getChild(0));
        }

        boolean capped = true;

        ExprInstruction i1 = (ExprInstruction) visit(ctx.expr1(0));
        if (currentReg < 10) {
            currentReg++;
            capped = false;
        } else {
            stack.newScope();
            stack.add("", null);
        }

        ExprInstruction i2 = (ExprInstruction) visit(ctx.expr1(1));
        if (capped) {
            stack.descope();
        } else {
            currentReg--;
        }

        boolean lhsIsConst = isConst(i1);
        boolean rhsIsConst = isConst(i2);
        int op = ((TerminalNode) ctx.binaryOper1().getChild(0)).getSymbol().getType();

        if (lhsIsConst && rhsIsConst) {
            IntLiterInstruction intLiter;
            int lhsValue = ((IntLiterInstruction) i1).getValue();
            int rhsValue = ((IntLiterInstruction) i2).getValue();
            if (op == WACCLexer.MULTIPLY) {
                intLiter = new IntLiterInstruction(lhsValue * rhsValue, currentReg);
            } else if (op == WACCLexer.DIVIDE) {
                intLiter = new IntLiterInstruction(lhsValue / rhsValue, currentReg);
            } else {
                intLiter = new IntLiterInstruction(lhsValue % rhsValue, currentReg);
            }
            return intLiter;
        } else {
            BinaryExprInstruction binaryOp;
            if (op == WACCLexer.MULTIPLY) {
                binaryOp = new MultiplyInstruction(i1, i2, currentReg, currentReg + 1, dataMap);
            } else if (op == WACCLexer.DIVIDE) {
                binaryOp = new DivideInstruction(i1, i2, currentReg, dataMap);
            } else {
                binaryOp = new ModInstruction(i1, i2, currentReg, dataMap);
            }
            dataMap = binaryOp.setCheckError();
            ContainingDataOrLabelsInstruction dataAndLabels = binaryOp.getErrorPrint();
            addDataAndLabels(dataAndLabels);
            return binaryOp;
        }
    }

    private boolean isConst(ExprInstruction instruction) {
        return  instruction instanceof IntLiterInstruction ||
                instruction instanceof BoolLiterInstruction ||
                instruction instanceof CharLiterInstruction;
    }


    @Override
    public Instruction visitStructList(@NotNull WACCParser.StructListContext ctx) {

        List<LocatableInstruction> assignList = new ArrayList<>();
        currentReg++;
        for(WACCParser.AssignRHSContext rhs: ctx.assignRHS()) {
            assignList.add((LocatableInstruction) visit(rhs));
        }
        currentReg--;

        return new StructListInstruction(assignList, currentReg);
    }

    @Override
    public Instruction visitPairLiter(@NotNull WACCParser.PairLiterContext ctx) {
        return new PairLiterInstruction(currentReg);
    }

    @Override
    public Instruction visitArrayLiter(@NotNull WACCParser.ArrayLiterContext ctx) {
        List<WACCParser.ExprContext> exprs = ctx.expr();
        List<ExprInstruction> elems = new LinkedList<>();
        currentReg++;
        for (WACCParser.ExprContext expr : exprs) {
            ExprInstruction exprIns = (ExprInstruction) visit(expr);
            elems.add(exprIns);
        }
        currentReg--;
        return new ArrayLiterInstruction(elems, currentReg);
    }

    @Override
    public Instruction visitStringLiter(@NotNull WACCParser.StringLiterContext ctx) {
        String literal = ctx.STRING_LITERAL().getText();
        /*if (stringList.contains(literal)) {
            return new StringLiterInstruction(stringList.indexOf(literal), currentReg, literal);
        }
        stringList.add(literal);
        */
        StringLiterInstruction stringLiterInstruction =  new StringLiterInstruction(dataMap.size(), currentReg, literal);
        DataInstruction dataString = stringLiterInstruction.setData(literal);
        data.add(dataString);
        dataMap.put("msg_" + dataMap.size(), dataString.getAscii());
        return stringLiterInstruction;
    }

    @Override
    public Instruction visitIntLiter(@NotNull WACCParser.IntLiterContext ctx) {
        String value = ctx.getText();

        char numberBase = value.charAt(value.length()-1);
        int radix;

        if (Character.isAlphabetic(numberBase)) {
            value = value.substring(0, value.length()-1);
        }

        switch (numberBase) {
            case 'h' : radix = 16; break;
            case 'o' : radix = 8; break;
            case 'b' : radix = 2; break;
            default: radix = 10;
        }

        int decValue = Integer.parseInt(value, radix);

        return new IntLiterInstruction(decValue, currentReg);
    }

    @Override
    public Instruction visitFloatLiter(@NotNull WACCParser.FloatLiterContext ctx){
        String value = ctx.getText();
        float valueInFloat = Float.parseFloat(value);

        FloatLiterInstruction floatLiterInstruction =  new FloatLiterInstruction(dataMap.size(),
                currentReg, valueInFloat);
        DataInstruction dataString = floatLiterInstruction.setData("\"" + valueInFloat + "f\"");
        data.add(dataString);
        dataMap.put("msg_" + dataMap.size(), dataString.getAscii());
        return floatLiterInstruction;
    }

    @Override
    public Instruction visitCharLiter(@NotNull WACCParser.CharLiterContext ctx) {
        char value = ctx.CHAR_LITERAL().getText().charAt(1);
        String valStr;
        if (value == '\\') {
            value = ctx.CHAR_LITERAL().getText().charAt(2);
            valStr = "" + EscapedCharacters.getAscii(value);
        } else {
            valStr = "\'" + value + "\'";
        }
        return new CharLiterInstruction(valStr, currentReg);
    }

    @Override
    public Instruction visitBoolLiter(@NotNull WACCParser.BoolLiterContext ctx) {
        return new BoolLiterInstruction(ctx.BOOL_LITERAL().getText().equals("true"), currentReg);
    }

    @Override
    public Instruction visitBaseType(@NotNull WACCParser.BaseTypeContext ctx) {
        return super.visitBaseType(ctx);
    }

    @Override
    public Instruction visitArrayType(@NotNull WACCParser.ArrayTypeContext ctx) {
        return super.visitArrayType(ctx);
    }

    @Override
    public Instruction visitPairType(@NotNull WACCParser.PairTypeContext ctx) {
        return super.visitPairType(ctx);
    }

    @Override
    public Instruction visitPairNullType(@NotNull WACCParser.PairNullTypeContext ctx) {
        return super.visitPairNullType(ctx);
    }

    @Override
    public Instruction visitArrayElem(@NotNull WACCParser.ArrayElemContext ctx) {

        String id = ctx.identifier().getText();
        String locationString = "" + stack.get(id);
        Type type = stack.getType(id);
        for(TerminalNode c:ctx.CLOSE_SQUARE()) {
            type = ((ArrayType) type).getContentsType();
        }

        List<ExprInstruction> exprs = new LinkedList<>();
        currentReg += 2;
        for (WACCParser.ExprContext e : ctx.expr()) {
            exprs.add((ExprInstruction) visit(e));
        }
        currentReg -= 2;
        if (ctx.getParent() instanceof WACCParser.AssignLHSContext) {
            ArrayElemLHSInstruction array = new ArrayElemLHSInstruction(
                    locationString, type, currentReg, exprs, dataMap);
            dataMap = array.setErrorChecking();
            addDataAndLabels(array.getDataAndLabels());
            return array;
        } else {
            currentReg++;
            ArrayElemInstruction array = new ArrayElemInstruction(locationString, type, currentReg, exprs, dataMap);
            dataMap = array.setErrorChecking();
            addDataAndLabels(array.getDataAndLabels());
            currentReg--;
            return array;
        }
    }

    @Override
    public Instruction visitPairElemType(@NotNull WACCParser.PairElemTypeContext ctx) {
        return super.visitPairElemType(ctx);
    }

    @Override
    public Instruction visitPtrBaseType(@NotNull WACCParser.PtrBaseTypeContext ctx) {
        return super.visitPtrBaseType(ctx);
    }

    @Override
    public Instruction visitIdentifier(@NotNull WACCParser.IdentifierContext ctx) {
        String id = ctx.IDENTIFIER().getText();
        String locationString = stack.getOffsetString(id);
        Type type = stack.getType(id);

        if (type.checkType(PrimType.STRING)){
            String stringValue = identifier.get(id);
            return new IdentifierExprInstruction(locationString, type, currentReg,stringValue);
        }
        if (ctx.getParent() instanceof WACCParser.BaseExprContext || ctx.getParent() instanceof WACCParser.StructContentsExprContext) {
            return new IdentifierExprInstruction(locationString, type, currentReg);
        } else {
            return new IdentifierInstruction(locationString, type);
        }
    }

    @Override
    public Instruction visitNewPair(@NotNull WACCParser.NewPairContext ctx) {
        currentReg++;
        ExprInstruction exprA = (ExprInstruction) visit(ctx.expr(0));
        ExprInstruction exprB = (ExprInstruction) visit(ctx.expr(1));
        currentReg--;
        return new NewPairInstruction(currentReg, exprA, exprB);
    }

    @Override
    public Instruction visitNewArray(@NotNull WACCParser.NewArrayContext ctx) {
        currentReg++;
        ExprInstruction expr = (ExprInstruction) visit(ctx.expr());
        currentReg--;
        return new NewArrayInstruction(expr, currentReg, parseType(ctx.type()));
    }

    @Override
    public Instruction visitArgList(@NotNull WACCParser.ArgListContext ctx) {
        return super.visitArgList(ctx);
    }

    @Override
    public Instruction visitUnaryOper(@NotNull WACCParser.UnaryOperContext ctx) {
        return super.visitUnaryOper(ctx);
    }

    @Override
    public Instruction visitAssignRHS(@NotNull WACCParser.AssignRHSContext ctx) {
        return super.visitAssignRHS(ctx);
    }

    @Override
    public Instruction visitAssignLHS(@NotNull WACCParser.AssignLHSContext ctx) {

        return new AssignLHSInstruction((LocatableInstruction) visit(ctx.getChild(0)));
    }

    @Override
    public Instruction visitPairElem(@NotNull WACCParser.PairElemContext ctx) {
        TerminalNode pairOp = (TerminalNode) ctx.getChild(0);
        int pairOpToken = pairOp.getSymbol().getType();
        boolean isTokenFST = pairOpToken == WACCLexer.FST;
        if (ctx.getParent() instanceof WACCParser.AssignRHSContext) {
            ExprInstruction expr = (ExprInstruction) visit(ctx.expr());
            PairRHSInstruction pair = new PairRHSInstruction(isTokenFST, expr, dataMap);
            dataMap = pair.setErrorChecking();
            addDataAndLabels(pair.getDataAndLabels());
            return pair;
        } else {
            currentReg++;
            ExprInstruction expr = (ExprInstruction) visit(ctx.expr());
            PairLHSInstruction pair = new PairLHSInstruction(isTokenFST, expr, dataMap);
            dataMap = pair.setErrorChecking();
            addDataAndLabels(pair.getDataAndLabels());
            currentReg--;
            return pair;
        }
    }

    @Override
    public Instruction visitType(@NotNull WACCParser.TypeContext ctx) {
        return super.visitType(ctx);
    }

    @Override
    public Instruction visitSkipStat(@NotNull WACCParser.SkipStatContext ctx) {
        return new SkipInstruction();
    }

    @Override
    public Instruction visitDerefLHS(@NotNull WACCParser.DerefLHSContext ctx) {

        WACCParser.AssignLHSContext lhs = ctx.assignLHS();
        LocatableInstruction ins = (LocatableInstruction) visit(lhs);

        String location = ins.getLocationString();

        int derefNum = ctx.MULTIPLY().size();
        Type type = ins.getType();
        for (int i = 0; i < derefNum; i++) {
            type = ((PtrType) type).deref();
        }

        Instruction retIns;
        if (ctx.getParent() instanceof WACCParser.AssignLHSContext
                || ctx.getParent() instanceof WACCParser.CallFunctionContext) {
            retIns = new DerefIdentLHSInstruction(ins, location, type,currentReg, derefNum);
        } else {
            retIns = new DerefIdentInstruction(currentReg, location, type, derefNum, ins);
        }
        return retIns;
    }

    @Override
    public Instruction visitParamList(@NotNull WACCParser.ParamListContext ctx) {
        return super.visitParamList(ctx);
    }

    @Override
    public Instruction visitBaseExpr(@NotNull WACCParser.BaseExprContext ctx) {
        return super.visitBaseExpr(ctx);
    }

    @Override
    public Instruction visitBinaryOper1(@NotNull WACCParser.BinaryOper1Context ctx) {
        return super.visitBinaryOper1(ctx);
    }

    @Override
    public Instruction visitBinaryOper2(@NotNull WACCParser.BinaryOper2Context ctx) {
        return super.visitBinaryOper2(ctx);
    }

    @Override
    public Instruction visitBinaryOper3(@NotNull WACCParser.BinaryOper3Context ctx) {
        return super.visitBinaryOper3(ctx);
    }

    @Override
    public Instruction visitBinaryOper4(@NotNull WACCParser.BinaryOper4Context ctx) {
        return super.visitBinaryOper4(ctx);
    }

    @Override
    public Instruction visitPtrType(@NotNull WACCParser.PtrTypeContext ctx) {
        return super.visitPtrType(ctx);
    }

    @Override
    public Instruction visitBinaryOper5(@NotNull WACCParser.BinaryOper5Context ctx) {
        return super.visitBinaryOper5(ctx);
    }

    @Override
    public Instruction visitBinaryOper6(@NotNull WACCParser.BinaryOper6Context ctx) {
        return super.visitBinaryOper6(ctx);
    }


    @Override
    public Instruction visitParam(@NotNull WACCParser.ParamContext ctx) {
        return super.visitParam(ctx);
    }

    @Override
    public Instruction visitFuncIdent(@NotNull WACCParser.FuncIdentContext ctx) {
        return super.visitFuncIdent(ctx);
    }
}

