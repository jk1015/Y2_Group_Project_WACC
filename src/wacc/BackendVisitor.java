package wacc;

import antlr.WACCLexer;
import antlr.WACCParser;
import org.antlr.v4.runtime.misc.NotNull;
import antlr.WACCParserBaseVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;
import wacc.instructions.*;
import wacc.instructions.expressions.ExprInstruction;
import wacc.instructions.expressions.baseExpressions.*;
import wacc.instructions.expressions.binaryExpressions.arithmeticExpressions.*;
import wacc.instructions.expressions.binaryExpressions.comparatorExpressions.*;
import wacc.instructions.expressions.binaryExpressions.logicalExpressions.ANDInstruction;
import wacc.instructions.expressions.binaryExpressions.logicalExpressions.ORInstruction;
import wacc.instructions.expressions.unaryExpressions.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BackendVisitor extends WACCParserBaseVisitor<Instruction> {

    private final ScopedSymbolTable symbolTable;
    private final MemoryStack stack;

    private List<DataInstruction> data = new ArrayList<>();
    private List<LabelInstruction> labels = new ArrayList<>();
    private int numOfMsg = 0;

    private int currentReg;
    private List<String> stringList = new ArrayList<>();


    public BackendVisitor(ScopedSymbolTable symbolTable) {
        this.symbolTable = symbolTable;
        this.stack = new MemoryStack(2);
        this.currentReg = 4;
    }

    public void addDataAndLabels(ContainingDataOrLabelsInstruction ins){
        ArrayList<DataInstruction> dataInstructions = ins.getData();
        ArrayList<LabelInstruction> labelInstructions = ins.getLabel();

        if (dataInstructions != null){
             data.addAll(dataInstructions);
        }

        if (labelInstructions != null){
            labels.addAll(labelInstructions);
        }
    }


    @Override
    public Instruction visitProgram(@NotNull WACCParser.ProgramContext ctx) {
        // Visit functions then visit program.
        ProgramInstruction program = new ProgramInstruction(visit(ctx.stat()));



        return new AssemblyInstruction(data, program, labels);
    }

    @Override
    public Instruction visitFunction(@NotNull WACCParser.FunctionContext ctx) {
        String functionLabel = LabelMaker.getFunctionLabel(ctx.identifier().getText());
        Instruction statement = visit(ctx.stat());

        //map params to location on stack
        List<WACCParser.ParamContext> params = ctx.paramList().param();
        stack.scope(params.size());

        for (WACCParser.ParamContext param: params) {
            String paramIdentifier = param.getText();
            stack.add(paramIdentifier);
        }

        stack.descope(params.size());

        return new FunctionInstruction(functionLabel, statement);
    }

    @Override
    public Instruction visitCallFunction(@NotNull WACCParser.CallFunctionContext ctx) {
        // get corresponding function label of function
        String functionLabel = LabelMaker.getFunctionLabel(ctx.identifier().getText());

        // arglist adds args to stack
        List<ExprInstruction> args = new LinkedList<>();
        List<WACCParser.ExprContext> exprs = ctx.argList().expr();
        for (WACCParser.ExprContext expr : exprs) {
            args.add((ExprInstruction) visit(expr));
            stack.scope(1);
        }

        stack.descope(exprs.size());

        return new CallFunctionInstruction(functionLabel, args);
    }

    @Override
    public Instruction visitReturnStat(@NotNull WACCParser.ReturnStatContext ctx) {
        return super.visitReturnStat(ctx);
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
        stack.add(var);
        //TODO
        ExprInstruction expr = (ExprInstruction) visit(ctx.assignRHS());
        return new InitAssignInstruction(expr, stack.getLocationString(var));
    }

    @Override
    public Instruction visitAssignStat(@NotNull WACCParser.AssignStatContext ctx) {
        return super.visitAssignStat(ctx);
    }

    @Override
    public Instruction visitBlockStat(@NotNull WACCParser.BlockStatContext ctx) {
        return super.visitBlockStat(ctx);
    }

    @Override
    public Instruction visitWhileStat(@NotNull WACCParser.WhileStatContext ctx) {
        ExprInstruction expr = (ExprInstruction) visit(ctx.expr());
        Instruction stat = visit(ctx.stat());
        return new WhileInstruction(expr, stat);
    }

    @Override
    public Instruction visitIfStat(@NotNull WACCParser.IfStatContext ctx) {
        ExprInstruction expr = (ExprInstruction) visit(ctx.expr());
        Instruction stat1 = visit(ctx.stat(0));
        Instruction stat2 = visit(ctx.stat(1));
        IfInstruction ins = new IfInstruction(expr, stat1, stat2);
        return ins;
    }

    @Override
    public Instruction visitReadStat(@NotNull WACCParser.ReadStatContext ctx) {
        AssignLHSInstruction assignLHSInstruction = (AssignLHSInstruction) visitAssignLHS(ctx.assignLHS());
        ReadInstruction readInstruction = new ReadInstruction(assignLHSInstruction);
        numOfMsg = readInstruction.addDataAndLabels();
        addDataAndLabels(readInstruction);
        return readInstruction;
    }

    @Override
    public Instruction visitPrintStat(@NotNull WACCParser.PrintStatContext ctx) {
        ExprInstruction expr = (ExprInstruction) visitExpr(ctx.expr());
        PrintInstruction print = new PrintInstruction(expr,numOfMsg);
        numOfMsg = print.addDataAndLabels();
        addDataAndLabels(print);
        return print;
    }

    @Override
    public Instruction visitPrintlnStat(@NotNull WACCParser.PrintlnStatContext ctx) {
        ExprInstruction expr = (ExprInstruction) visitExpr(ctx.expr());
        PrintlnInstruction print = new PrintlnInstruction(expr,numOfMsg);
        print.addDataAndLabels();
        numOfMsg += 3;
        addDataAndLabels(print);
        return print;
    }

    @Override
    public Instruction visitFreeStat(@NotNull WACCParser.FreeStatContext ctx) {
        return super.visitFreeStat(ctx);
    }

    @Override
    public Instruction visitExitStat(@NotNull WACCParser.ExitStatContext ctx) {
        ExprInstruction exInst = (ExprInstruction) (visitExpr(ctx.expr()));
        return new ExitInstruction(exInst);
    }

    @Override
    public Instruction visitUnaryExpr(@NotNull WACCParser.UnaryExprContext ctx) {
        int op = ((TerminalNode) ctx.unaryOper().getChild(0)).getSymbol().getType();
        ExprInstruction i = (ExprInstruction) visit(ctx.expr1());
        if(op == WACCLexer.NOT) {
            return new NotInstruction(i, currentReg);
        } else if (op == WACCLexer.MINUS) {
            return new NegInstruction(i, currentReg);
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
        if(e != null) {
            return visit(e);
        }

        ExprInstruction i1 = (ExprInstruction) visit(ctx.expr6(0));
        currentReg++;
        ExprInstruction i2 = (ExprInstruction) visit(ctx.expr6(1));
        currentReg--;
        return new ORInstruction(i1, i2, currentReg);

    }

    @Override
    public Instruction visitExpr5(@NotNull WACCParser.Expr5Context ctx) {
        WACCParser.Expr4Context e = ctx.expr4();
        if(e != null) {
            return visit(e);
        }

        ExprInstruction i1 = (ExprInstruction) visit(ctx.expr5(0));
        currentReg++;
        ExprInstruction i2 = (ExprInstruction) visit(ctx.expr5(1));
        currentReg--;
        return new ANDInstruction(i1, i2, currentReg);
    }

    @Override
    public Instruction visitExpr4(@NotNull WACCParser.Expr4Context ctx) {
        WACCParser.Expr3Context e = ctx.expr3();
        if(e != null) {
            return visit(e);
        }

        ExprInstruction i1 = (ExprInstruction) visit(ctx.expr4(0));
        currentReg++;
        ExprInstruction i2 = (ExprInstruction) visit(ctx.expr4(1));
        currentReg--;
        int op = ((TerminalNode) ctx.binaryOper4().getChild(0)).getSymbol().getType();
        if(op == WACCLexer.EQ) {
            return new EQInstruction(i1, i2, currentReg);
        } else {
            return new NEQInstruction(i1, i2, currentReg);
        }

    }

    @Override
    public Instruction visitExpr3(@NotNull WACCParser.Expr3Context ctx) {
        WACCParser.Expr2Context e = ctx.expr2();
        if(e != null) {
            return visit(e);
        }

        ExprInstruction i1 = (ExprInstruction) visit(ctx.expr3(0));
        currentReg++;
        ExprInstruction i2 = (ExprInstruction) visit(ctx.expr3(1));
        currentReg--;
        int op = ((TerminalNode) ctx.binaryOper3().getChild(0)).getSymbol().getType();
        if(op == WACCLexer.GT) {
            return new GTInstruction(i1, i2, currentReg);
        } else if (op == WACCLexer.LT) {
            return new LTInstruction(i1, i2, currentReg);
        } else if (op == WACCLexer.LEQ) {
            return new LEQInstruction(i1, i2, currentReg);
        } else {
            return new GEQInstruction(i1, i2, currentReg);
        }

    }

    @Override
    public Instruction visitExpr2(@NotNull WACCParser.Expr2Context ctx) {
        WACCParser.Expr1Context e = ctx.expr1();
        if(e != null) {
            return visit(e);
        }

        ExprInstruction i1 = (ExprInstruction) visit(ctx.expr2(0));
        currentReg++;
        ExprInstruction i2 = (ExprInstruction) visit(ctx.expr2(1));
        currentReg--;
        int op = ((TerminalNode) ctx.binaryOper2().getChild(0)).getSymbol().getType();
        if(op == WACCLexer.PLUS) {
            return new PlusInstruction(i1, i2, currentReg);
        } else {
            return new MinusInstruction(i1, i2, currentReg);
        }
    }

    @Override
    public Instruction visitExpr1(@NotNull WACCParser.Expr1Context ctx) {

        if(ctx.getChildCount() == 1) {
            return visit(ctx.getChild(0));
        }

        ExprInstruction i1 = (ExprInstruction) visit(ctx.expr1(0));
        currentReg++;
        ExprInstruction i2 = (ExprInstruction) visit(ctx.expr1(1));
        currentReg--;
        int op = ((TerminalNode) ctx.binaryOper1().getChild(0)).getSymbol().getType();
        if(op == WACCLexer.MULTIPLY) {
            return new MultiplyInstruction(i1, i2, currentReg, currentReg + 1);
        } else if (op == WACCLexer.DIVIDE) {
            return new DivideInstruction(i1, i2, currentReg);
        }  else {
            return new ModInstruction(i1, i2, currentReg);
        }
    }

    @Override
    public Instruction visitPairLiter(@NotNull WACCParser.PairLiterContext ctx) {
        return new PairLiterInstruction(currentReg);
    }

    @Override
    public Instruction visitArrayLiter(@NotNull WACCParser.ArrayLiterContext ctx) {
        return super.visitArrayLiter(ctx);
    }

    @Override
    public Instruction visitStringLiter(@NotNull WACCParser.StringLiterContext ctx) {
        String literal = ctx.STRING_LITERAL().getText();
        if (stringList.contains(literal)) {
            return new StringLiterInstruction(stringList.indexOf(literal), currentReg, literal);
        }
        stringList.add(literal);
        return new StringLiterInstruction(stringList.size() - 1, currentReg, literal);
    }

    @Override
    public Instruction visitIntLiter(@NotNull WACCParser.IntLiterContext ctx) {
        int value = Integer.parseInt(ctx.INT().getText());
        return new IntLiterInstruction(value, currentReg);
    }

    @Override
    public Instruction visitCharLiter(@NotNull WACCParser.CharLiterContext ctx) {
        char value = ctx.CHAR_LITERAL().getText().charAt(0);
        return new CharLiterInstruction(value, currentReg);
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
        return super.visitArrayElem(ctx);
    }

    @Override
    public Instruction visitPairElemType(@NotNull WACCParser.PairElemTypeContext ctx) {
        return super.visitPairElemType(ctx);
    }

    @Override
    public Instruction visitIdentifier(@NotNull WACCParser.IdentifierContext ctx) {
        String id = ctx.IDENTIFIER().getText();
        return new IdentifierInstruction(stack.getLocationString(id), currentReg);
    }

    @Override
    public Instruction visitNewPair(@NotNull WACCParser.NewPairContext ctx) {
        return super.visitNewPair(ctx);
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

        return super.visitAssignLHS(ctx);
    }

    @Override
    public Instruction visitType(@NotNull WACCParser.TypeContext ctx) {
        return super.visitType(ctx);
    }

    @Override
    public Instruction visitSkipStat(@NotNull WACCParser.SkipStatContext ctx) {
        return super.visitSkipStat(ctx);
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
    public Instruction visitExpr(@NotNull WACCParser.ExprContext ctx) {
        return super.visitExpr(ctx);
    }

    @Override
    public Instruction visitPairElem(@NotNull WACCParser.PairElemContext ctx) {
        return super.visitPairElem(ctx);
    }
}
