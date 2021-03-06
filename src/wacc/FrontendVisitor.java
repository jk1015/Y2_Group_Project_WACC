

package wacc;

import antlr.WACCLexer;
import antlr.WACCParser;
import antlr.WACCParser.ExprContext;
import antlr.WACCParser.ReadStatContext;
import antlr.WACCParserBaseVisitor;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import wacc.exceptions.*;
import org.antlr.v4.runtime.tree.TerminalNode;
import wacc.types.*;

import java.util.*;


public class FrontendVisitor extends WACCParserBaseVisitor<Type> {
	
	private ScopedSymbolTable symbolTable;
	private String currentFunction; 
	private boolean hasReturn;
    private List<String> calledFuncNames;
    private List<FunctionType> calledFuncTypes;
    private List<WACCParser.CallFunctionContext> calledFuncCtxs;
    private HashMap<String, StructType> structs;
    private Type lhsRequiredType;
    private boolean inALoop;


    public FrontendVisitor() {
		symbolTable = new ScopedSymbolTable();
		currentFunction = "";
		hasReturn = false;
        calledFuncNames = new LinkedList<>();
        calledFuncTypes = new LinkedList<>();
        calledFuncCtxs = new LinkedList<>();
        structs = new HashMap<>();
    }

    @Override
    public Type visitProgram(@NotNull WACCParser.ProgramContext ctx) {
        super.visitProgram(ctx);

        checkThatFunctionsHaveBeenDefinedCorrectly();
        return null;
    }

    @Override
    public Type visitHeader(@NotNull WACCParser.HeaderContext ctx) {
        super.visitChildren(ctx);

        checkThatFunctionsHaveBeenDefinedCorrectly();
        return null;
    }

    private void checkThatFunctionsHaveBeenDefinedCorrectly() {
        Iterator<FunctionType> iter = calledFuncTypes.iterator();
        Iterator<WACCParser.CallFunctionContext> ctxIter = calledFuncCtxs.iterator();
        for (String name : calledFuncNames) {
            WACCParser.CallFunctionContext ctx = ctxIter.next();
            try {
                FunctionType type = iter.next();
                FunctionType actualType = symbolTable.getFunction(name);
                if (!actualType.checkType(type)) {
                    throw new InvalidTypeException(ctx, "Function " + name + " is undefined for type " + type);
                }
            } catch (UndeclaredFunctionException e) {
                throw new UndeclaredFunctionException(ctx, e.getMessage());
            }
        }
    }

    @Override
    public Type visitDerefLHS(@NotNull WACCParser.DerefLHSContext ctx) {
        Type type = visit(ctx.assignLHS());
        int count = ctx.MULTIPLY().size();
        for (int i = 0; i < count; i++) {
            if (!(type instanceof PtrType)) {
                throw new InvalidTypeException(ctx, "Can't dereference a non-pointer");
            }
            type = ((PtrType) type).deref();
        }
        return type;
    }

    @Override
    public Type visitStruct(@NotNull WACCParser.StructContext ctx) {
        String id = ctx.identifier(0).getText();
        List<Type> typeList = new ArrayList<>();
        List<String> idList = new ArrayList<>();
        StructType tempStruct = new StructType(id, typeList, idList);
        structs.put(id, tempStruct);
        for(int i = 1; i < ctx.identifier().size(); i++) {
            String nextId = ctx.identifier(i).getText();
            if(idList.contains(nextId)) {
                throw new RedeclaredVariableException(ctx);
            }
            idList.add(nextId);

            Type t = visit(ctx.fixedSizeType(i - 1));
            if(t.checkType(tempStruct)) {
                throw new WACCSemanticErrorException(ctx, "Structs may not contain themselves");
            }
            typeList.add(t);
        }
        return null;
    }

    @Override
    public Type visitFunction(WACCParser.FunctionContext ctx) {
        // Add to symbol table, check validity of children under new movePointer
        //Check that statement contains a return

        String fName = ctx.getChild(1).getText();
        Type returnType = visit(ctx.getChild(0));
        List<String> idents = new ArrayList<String>();
        List<Type> types = new ArrayList<Type>();

        ParseTree paramList = ctx.paramList();

        if (paramList != null) {
            for(int i = 0; i < paramList.getChildCount(); i += 2) {
                types.add(visit(paramList.getChild(i).getChild(0)));
                idents.add(paramList.getChild(i).getChild(1).getText());
            }
        }

        Type[] typesArray = new Type[types.size() + 1];
        typesArray[0] = returnType;
        for(int i = 1; i < typesArray.length; i++) {
            typesArray[i] = types.get(i - 1);
        }
        FunctionType fType = new FunctionType(typesArray);

        try {
            symbolTable.addFunction(fName, fType);
        } catch (RedeclaredFunctionException e) {
            throw new RedeclaredFunctionException(ctx, e.getMessage());
        }

        symbolTable.enterNewScope();
        for(int i = 0; i < types.size(); i++) {
            symbolTable.add(idents.get(i), types.get(i));
        }
        String prevFunction = this.currentFunction;
        currentFunction = fName;
        visit(ctx.getChild(ctx.getChildCount() - 2));
        currentFunction = prevFunction;
        symbolTable.exitScope();

        if(!hasReturn) {
            throw new InvalidReturnException(ctx, "Function does not return along all execution branches");
        }

        hasReturn = false;

        return fType;

    }

    @Override
    public Type visitCallFunction(WACCParser.CallFunctionContext ctx)  {

        List<Type> types = new ArrayList<>();

        types.add(lhsRequiredType);

        WACCParser.ArgListContext argList = ctx.argList();

        if (argList != null) {
            for(int i = 0; i < argList.getChildCount(); i += 2) {
                types.add(visit(argList.getChild(i)));
            }
        }

        FunctionType calledFunctionType = new FunctionType(types);
        if (ctx.identifier() != null) {
            calledFuncNames.add(ctx.identifier().getText());
            calledFuncTypes.add(calledFunctionType);
            calledFuncCtxs.add(ctx);
        } else {
            Type derefType = visitDerefLHS(ctx.derefLHS());
            if (!calledFunctionType.checkType(derefType)) {
                throw new InvalidTypeException(ctx, calledFunctionType, derefType);
            }
        }

        return lhsRequiredType;
    }

    // STATEMENTS

    @Override
    public Type visitReturnStat(WACCParser.ReturnStatContext ctx) {
        if (currentFunction == "") {
            throw new MainProgramReturnException(ctx, "Return called outside of function");
        }
        Type retType = (symbolTable.getFunction(currentFunction)).getReturnType();
        if(!visit(ctx.expr()).checkType(retType)) {
            throw new InvalidTypeException("");
        }
        hasReturn = true;
        return retType;
    }

    @Override
    public Type visitSeqStat(@NotNull WACCParser.SeqStatContext ctx) {
        for(ParseTree stat: ctx.stat()) {
            if(hasReturn) {
                throw new InvalidReturnException(ctx, "Dead code following return");
            }
            visit(stat);
        }
        return null;
    }

    @Override
    public Type visitInitAssignStat(WACCParser.InitAssignStatContext ctx) {
        // Check type against rhs, add to symbol table
        Type type = visit(ctx.type());
        lhsRequiredType = type;
        Type rhs = visit(ctx.assignRHS());
        if (type.checkType(rhs)) {
            String ident = ctx.identifier().getText();
            try {
                symbolTable.add(ident, type);
            } catch (RedeclaredVariableException e) {
                throw new RedeclaredVariableException(ctx, e.getMessage());
            }
            return null;
        }
        throw new InvalidTypeException(ctx, type, rhs);
    }

    @Override
    public Type visitAssignStat(WACCParser.AssignStatContext ctx) {
        // Check LHS and RHS match
        Type lhs = visitAssignLHS(ctx.assignLHS());
        lhsRequiredType = lhs;
        Type rhs = visitAssignRHS(ctx.assignRHS());
        if (!rhs.checkType(lhs)) {
            throw new InvalidTypeException(ctx, rhs, lhs);
        }
        return null;
    }

    @Override
    public Type visitBlockStat(@NotNull WACCParser.BlockStatContext ctx) {
        symbolTable.enterNewScope();
        super.visitBlockStat(ctx);
        symbolTable.exitScope();
        return null;
    }
    @Override
    public Type visitBreakStat(@NotNull WACCParser.BreakStatContext ctx){
        breakOrIfError(ctx, "break");
        return null;
    }

    @Override
    public Type visitContinueStat(@NotNull WACCParser.ContinueStatContext ctx){
        breakOrIfError(ctx, "continue");
        return null;
    }

    private void breakOrIfError(@NotNull WACCParser.StatContext ctx, String name) {
        if (!inALoop){
            throw new InvalidBreakOrContinueException(ctx, name + " statement must be in a loop or if statement");
        }
    }

    @Override
    public Type visitWhileStat(WACCParser.WhileStatContext ctx) {
        // Check condition is boolean, check children are valid.
        Type type = visit(ctx.expr());
        if (type.checkType(PrimType.BOOL)) {
            enterLoopOrIfStat(ctx.stat(),ctx);
            return null;
        }
        throw new InvalidTypeException(ctx, PrimType.BOOL, type);
    }

    private void enterLoopOrIfStat(WACCParser.StatContext ctx, WACCParser.StatContext ctxP) {
        symbolTable.enterNewScope();
        if (ctxP instanceof WACCParser.WhileStatContext) {
            inALoop = true;
        }
        if (ctxP instanceof WACCParser.ForStatContext) {
            inALoop = true;
            symbolTable.add(((WACCParser.ForStatContext) ctxP).identifier().getText(), PrimType.INT);
        }
        visit(ctx);
        inALoop = false;
        symbolTable.exitScope();
    }

    @Override
    public Type visitForStat(@NotNull WACCParser.ForStatContext ctx) {
        for (ExprContext expr : ctx.expr()) {
            Type exprType = visit(expr);
            if (!exprType.checkType(PrimType.INT)) {
                throw new InvalidTypeException(ctx, PrimType.INT, exprType);
            }
        }
        enterLoopOrIfStat(ctx.stat(), ctx);
        return null;
    }

    @Override
    public Type visitIfStat(WACCParser.IfStatContext ctx) {
        // Check condition is boolean, check statements are valid.

        Type type = visitExpr(ctx.expr());
        if (!PrimType.BOOL.checkType(type)){
            throw new InvalidTypeException(ctx, PrimType.BOOL, type);
        }
        enterLoopOrIfStat(ctx.stat(0),ctx);
        boolean tempReturn = hasReturn;
        hasReturn = false;
        enterLoopOrIfStat(ctx.stat(1),ctx);
        hasReturn = tempReturn && hasReturn;
        return null;
    }

    @Override
    public Type visitReadStat(ReadStatContext ctx) {
        Type type = visit(ctx.assignLHS());
        if (type.checkType(PrimType.BOOL) || type instanceof PairType) {
            throw new InvalidTypeException(ctx, "Can't read into booleans or pairs.");
        }
        return null;
    }

    @Override
    public Type visitRefLHS(@NotNull WACCParser.RefLHSContext ctx) {
        return new PtrType(visit(ctx.getChild(1)));
    }

    @Override
    public Type visitPrintStat(@NotNull WACCParser.PrintStatContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Type visitPrintlnStat(@NotNull WACCParser.PrintlnStatContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Type visitFreeStat(WACCParser.FreeStatContext ctx) {
        // Check type is pair or array
        Type type = visit(ctx.expr());
        if ((type instanceof ArrayType) || (type instanceof PairType)) {
            return type;
        }
        throw new InvalidTypeException(ctx, "Expected pair or array, got " + type);
    }

    @Override
    public Type visitExitStat(WACCParser.ExitStatContext ctx) {
        // Check child is int
        Type exit = visitExpr(ctx.expr());
        if (!exit.checkType(PrimType.INT)){
            throw new InvalidTypeException(ctx, PrimType.INT, exit);
        }
        if(currentFunction != "") {
            hasReturn = true;
        }
        return exit;
    }

    // EXPRESSIONS

    @Override
    public Type visitUnaryExpr(WACCParser.UnaryExprContext ctx) {
        // Examine expression and operator for validity
        Type funType;
        Type retType;
        TerminalNode op = (TerminalNode)ctx.unaryOper().getChild(0);
        Type type = visit(ctx.expr1());

        switch (op.getSymbol().getType()) {
            case WACCLexer.NOT: funType = PrimType.BOOL;
                retType = PrimType.BOOL; break;
            case WACCLexer.LEN: funType = new ArrayType(new NullType());
                retType = PrimType.INT; break;
            case WACCLexer.ORD: funType = PrimType.CHAR;
                retType = PrimType.INT; break;
            case WACCLexer.CHR: funType = PrimType.INT;
                retType = PrimType.CHAR; break;
            case WACCLexer.MINUS:
                funType = PrimType.INT;
                retType = PrimType.INT; break;
            default: throw new IllegalArgumentException(
                    "visitUnaryExpr somehow found non-existent function");
        }

        if (!funType.checkType(type)) {
            throw new InvalidTypeException(ctx, funType, type);
        }

        return retType;
    }

    @Override
    public Type visitBracketsExpr(@NotNull WACCParser.BracketsExprContext ctx) {
        return visit(ctx.getChild(1));
    }

    @Override
    public Type visitExpr6(WACCParser.Expr6Context ctx) {
        //OR (||)
        if(ctx.getChildCount() == 1) {
            return visitChildren(ctx);
        }
        Type rhsType = visit(ctx.getChild(0));
        Type lhsType = visit(ctx.getChild(2));
        if(!rhsType.checkType(PrimType.BOOL)) {
            throw new InvalidTypeException(ctx, PrimType.BOOL, rhsType);
        } else if (!lhsType.checkType(PrimType.BOOL)) {
            throw new InvalidTypeException(ctx, PrimType.BOOL, lhsType);
        }
        return PrimType.BOOL;
    }

    @Override
    public Type visitExpr5(WACCParser.Expr5Context ctx) {
        //AND (&&)
        if(ctx.getChildCount() == 1) {
            return visitChildren(ctx);
        }
        Type rhsType = visit(ctx.getChild(0));
        Type lhsType = visit(ctx.getChild(2));
        if(!rhsType.checkType(PrimType.BOOL)) {
            throw new InvalidTypeException(ctx, PrimType.BOOL, rhsType);
        } else if (!lhsType.checkType(PrimType.BOOL)) {
            throw new InvalidTypeException(ctx, PrimType.BOOL, lhsType);
        }
        return PrimType.BOOL;
    }

    @Override
    public Type visitExpr4(WACCParser.Expr4Context ctx) {
        //EQ (==) NEQ(!=)
        if(ctx.getChildCount() == 1) {
            return visitChildren(ctx);
        }
        Type rhsType = visit(ctx.getChild(0));
        Type lhsType = visit(ctx.getChild(2));
        if(!rhsType.checkType(lhsType)) {
            throw new InvalidTypeException(ctx, rhsType, lhsType);
        }
        return PrimType.BOOL;
    }

    @Override
    public Type visitExpr3(WACCParser.Expr3Context ctx) {
        // GT (>) LT (<) GEQ (>=) LEQ (<=)
        if(ctx.getChildCount() == 1) {
            return visitChildren(ctx);
        }
        Type rhsType = visit(ctx.getChild(0));
        Type lhsType = visit(ctx.getChild(2));

        if (!rhsType.checkType(PrimType.INT) && !rhsType.checkType(PrimType.CHAR)) {
            throw new InvalidTypeException(ctx, "Expected int or char, got " + rhsType);
        }
        if (!lhsType.checkType(PrimType.INT) && !lhsType.checkType(PrimType.CHAR)) {
            throw new InvalidTypeException(ctx, "Expected int or char, got " + lhsType);
        }
        if(rhsType != lhsType) {
            throw new InvalidTypeException(ctx, "Expected both int or both char, got " + lhsType + " and " + rhsType);
        }

        return PrimType.BOOL;
    }

    @Override
    public Type visitExpr2(WACCParser.Expr2Context ctx) {
        // PLUS (+) MINUS (-)
        if(ctx.getChildCount() == 1) {
            return visitChildren(ctx);
        }
        Type rhsType = visit(ctx.getChild(0));
        Type lhsType = visit(ctx.getChild(2));
        if(!(rhsType.checkType(PrimType.INT)
        || rhsType.checkType(PrimType.FLOAT)
        || rhsType instanceof PtrType)) {
            throw new InvalidTypeException(ctx, PrimType.INT, rhsType);
        } else if (!(lhsType.checkType(PrimType.INT)
        || lhsType.checkType(PrimType.FLOAT)
        || lhsType instanceof PtrType)) {
            throw new InvalidTypeException(ctx, PrimType.INT, lhsType);
        }
        if ((rhsType instanceof PtrType) && (lhsType instanceof PtrType)) {
            if (rhsType.checkType(lhsType)) {
                return rhsType;
            } else {
                throw new InvalidTypeException(ctx, rhsType, lhsType);
            }
        }
        if (rhsType instanceof PtrType) {
            return rhsType;
        }
        if (lhsType instanceof PtrType) {
            return lhsType;
        }
        return PrimType.INT;
    }

    @Override
    public Type visitExpr1(WACCParser.Expr1Context ctx) {
        // MULTIPLY (*) DIVIDE (/) MOD (%) also other expr types
        if(ctx.getChildCount() == 1) {
            return visitChildren(ctx);
        }
        Type rhsType = visit(ctx.getChild(0));
        Type lhsType = visit(ctx.getChild(2));
        if(!(rhsType.checkType(PrimType.INT)
                || rhsType.checkType(PrimType.FLOAT))) {
            throw new InvalidTypeException(ctx, PrimType.INT, rhsType);
        } else if (!(lhsType.checkType(PrimType.INT)
                || lhsType.checkType(PrimType.FLOAT))) {
            throw new InvalidTypeException(ctx, PrimType.INT, lhsType);
        }
        return PrimType.INT;
    }


    // LITERALS

    @Override
    public Type visitPairLiter(WACCParser.PairLiterContext ctx) {
        // Return null
        return new NullType();
    }

    @Override
    public Type visitArrayLiter(WACCParser.ArrayLiterContext ctx) {
        // Check array is valid
        List<ExprContext> expr = ctx.expr();
        Iterator<ExprContext> iter = expr.iterator();
        if (expr.size() == 0) {
            return new NullType();
        }

        Type t = visitExpr(iter.next());
        while (iter.hasNext()) {
            Type type = visitExpr(iter.next());
            if (!t.checkType(type)) {
                throw new InvalidTypeException(ctx, t, type);
            }
        }
        return new ArrayType(t);
    }

    @Override
    public Type visitStringLiter(WACCParser.StringLiterContext ctx) {
        // Return string type
        return new ArrayType(PrimType.CHAR);
    }

    @Override
    public Type visitIntLiter(WACCParser.IntLiterContext ctx) {
        // Return int, check
        String intToken = ctx.getText();

        char numberBase = intToken.charAt(intToken.length()-1);
        int radix;

        if (Character.isAlphabetic(numberBase)) {
            intToken = intToken.substring(0, intToken.length()-1);
        }

        switch (numberBase) {
            case 'h' : radix = 16; break;
            case 'o' : radix = 8; break;
            case 'b' : radix = 2; break;
            default: radix = 10;
        }

        try {
            Integer.parseInt(intToken, radix);
        } catch (NumberFormatException e) {
            throw new IntegerSizeException(ctx, "Integer " + intToken + " larger than WACCMAXINT");
        }
        return PrimType.INT;
    }

    @Override
    public Type visitCharLiter(WACCParser.CharLiterContext ctx) {
        // Return char
        return PrimType.CHAR;
    }

    @Override
    public Type visitBoolLiter(WACCParser.BoolLiterContext ctx) {
        // Return bool
        return PrimType.BOOL;
    }

    // TYPES

    @Override
    public Type visitStructType(@NotNull WACCParser.StructTypeContext ctx) {
        StructType retType = structs.get(ctx.identifier().getText());
        if(retType == null) {
            throw new UndeclaredVariableException(ctx);
        }
        return retType;
    }

    @Override
    public Type visitBaseType(WACCParser.BaseTypeContext ctx) {
        int type = ((TerminalNode) ctx.getChild(0)).getSymbol().getType();
        switch (type) {
            case WACCLexer.BOOL_TYPE: return PrimType.BOOL;
            case WACCLexer.INT_TYPE: return PrimType.INT;
            case WACCLexer.CHAR_TYPE: return PrimType.CHAR;
            case WACCLexer.STRING_TYPE: return new ArrayType(PrimType.CHAR);
        }
        return null;
    }

    @Override
    public Type visitPtrType(@NotNull WACCParser.PtrTypeContext ctx) {
        Type type = visit(ctx.ptrBaseType());
        for (int i = 0; i < ctx.MULTIPLY().size(); i++) {
            type = new PtrType(type);
        }
        return type;
    }

    @Override
    public Type visitArrayType(WACCParser.ArrayTypeContext ctx) {
        // Returns array version of child type
        int arrayDepth = ctx.CLOSE_SQUARE().size();
        Type type = visit(ctx.getChild(0));
        for (int i = 0; i < arrayDepth; i++) {
            type = new ArrayType(type);
        }
        return type;
    }

    @Override
    public Type visitPairType(WACCParser.PairTypeContext ctx) {
        // Return the pair type
        Type fst = visitPairElemType(ctx.pairElemType(0));
        Type snd = visitPairElemType(ctx.pairElemType(1));

        return new PairType(fst,snd);
    }

    @Override
    public Type visitPairNullType(@NotNull WACCParser.PairNullTypeContext ctx) {
        return new NullType();
    }

    // OTHER

    @Override
    public Type visitStructList(@NotNull WACCParser.StructListContext ctx) {
        List<Type> typeList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        for(WACCParser.AssignRHSContext rhs: ctx.assignRHS()) {
            typeList.add(visit(rhs));
            nameList.add("");
        }
        return new StructType(StructType.structListName,typeList, nameList);
    }

    @Override
    public Type visitStructContentsExpr(@NotNull WACCParser.StructContentsExprContext ctx) {
        if(ctx.structContentsExpr() != null) {
            return visit(ctx.structContentsExpr());
        }
        return visitChildren(ctx);
    }

    @Override
    public Type visitStructContents(@NotNull WACCParser.StructContentsContext ctx) {
        StructType struct = (StructType) visit(ctx.structContentsExpr());

        if(struct == null) {
            throw new UndeclaredVariableException(ctx);
        }

        Type conType = struct.getType(ctx.identifier(0).getText());
        for(int i = 1; i < ctx.identifier().size(); i++) {

            try {
                struct = (StructType) conType;
            } catch (ClassCastException e) {
                throw new InvalidTypeException(ctx);
            }
            conType = struct.getType(ctx.identifier(i).getText());
        }

        return conType;
    }

    @Override
    public Type visitArrayElem(WACCParser.ArrayElemContext ctx) {
        // Make sure expression is int

        /*
    	for(int i = 2; i <= ctx.getChildCount(); i += 3) {
    		if(!visit(ctx.getChild(i)).checkType(PrimType.INT)) {
    			throw new InvalidTypeException("");
    		}
    	}*/

        List<ExprContext> expr = ctx.expr();
        Iterator<ExprContext> iterator = expr.iterator();

        while (iterator.hasNext()) {
            Type childType = visit(iterator.next());
            if(!PrimType.INT.checkType(childType)) {
                throw new InvalidTypeException();
            }
        }

        if (!(symbolTable.get(ctx.getChild(0).getText()) instanceof ArrayType)) {
            throw new InvalidTypeException("");
        }

        ArrayType arrayType = (ArrayType) symbolTable.get(ctx.getChild(0).getText());
        return arrayType.getContentsType();
    }

    @Override
    public Type visitPairElem(WACCParser.PairElemContext ctx) {
        // Check child is pair, go to correct child
        Type pairType = visit(ctx.expr());
        if (!(pairType instanceof PairType)) {
            throw new InvalidTypeException(ctx, "Expected pair, got type " + pairType);
        }

        TerminalNode pairElement = (TerminalNode) ctx.getChild(0);

        if (pairElement.getSymbol().getType() == WACCLexer.FST) {
            return ((PairType) pairType).getType1();
        } else {
            return ((PairType) pairType).getType2();
        }
    }

    @Override
    public Type visitIdentifier(WACCParser.IdentifierContext ctx) {
        // Returns type from symbol table.
        try {
            return symbolTable.get(ctx.getText());

        } catch (UndeclaredVariableException e) {
            throw new UndeclaredVariableException(ctx, e.getMessage());
        }
    }

    @Override
    public Type visitNewPair(WACCParser.NewPairContext ctx) {
        // Return the pair type of the two children.
        Type fst = visitExpr(ctx.expr(0));
        Type snd = visitExpr(ctx.expr(1));
        Type pair = new PairType(fst,snd);

        return pair;
    }

    @Override
    public Type visitNewArray(@NotNull WACCParser.NewArrayContext ctx) {
        Type exprType = visit(ctx.expr());
        if (!(exprType.checkType(PrimType.INT))) {
            throw new InvalidTypeException(ctx, PrimType.INT, exprType);
        }

        Type contentsType = visit(ctx.type());

        return new ArrayType(contentsType);
    }

    @Override
    public Type visitFuncPtrType(@NotNull WACCParser.FuncPtrTypeContext ctx) {
        List<Type> args = new LinkedList<Type>();
        for (WACCParser.TypeContext typeCtx : ctx.type()) {
            args.add(visit(typeCtx));
        }
        return new PtrType(new FunctionType(args));
    }

    @Override
    public Type visitFuncIdent(@NotNull WACCParser.FuncIdentContext ctx) {
        try {
            return symbolTable.getFunction(ctx.IDENTIFIER().getText());
        } catch (UndeclaredVariableException e) {
            throw new UndeclaredVariableException(ctx, e.getMessage());
        }
    }

    @Override
    public Type visitFloatLiter(@NotNull WACCParser.FloatLiterContext ctx) {
        return PrimType.FLOAT;
    }
}

