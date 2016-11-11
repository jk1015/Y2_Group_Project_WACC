package wacc;

import antlr.WACCLexer;
import antlr.WACCParser;
import antlr.WACCParser.ExprContext;
import antlr.WACCParserBaseVisitor;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import wacc.exceptions.*;
import org.antlr.v4.runtime.tree.TerminalNode;
import wacc.types.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ad5115 on 08/11/16.
 */
public class WACCVisitor extends WACCParserBaseVisitor<Type> {
	
	private final ScopedSymbolTable symbolTable;
	private String currentFunction; 
	
	
	public WACCVisitor() {
		symbolTable = new ScopedSymbolTable();
		currentFunction = "";
	}
	
	// James
	
	@Override
	public Type visitBaseType(WACCParser.BaseTypeContext ctx) {
		int type = ((TerminalNode) ctx.getChild(0)).getSymbol().getType();
		switch (type) { 
			case WACCLexer.BOOL_TYPE: return PrimType.BOOL;
			case WACCLexer.INT_TYPE: return PrimType.INT;
			case WACCLexer.CHAR_TYPE: return PrimType.CHAR;
			case WACCLexer.STRING_TYPE: return PrimType.STRING;
		}
		return null;
	}
	
    @Override
    public Type visitArrayElem(WACCParser.ArrayElemContext ctx) {
    	// Make sure expression is int
    	for(int i = 2; i <= ctx.getChildCount(); i += 3) {
    		if(visit(ctx.getChild(1)) != PrimType.INT) {
                System.out.println("thrown from visitArrayElem 1");
    			throw new InvalidTypeException("");
    		}
    	}
    	if (!(symbolTable.get(ctx.getChild(0).getText()) instanceof ArrayType)) {
            System.out.println("thrown from visitArrayElem 2");
    		throw new InvalidTypeException("");
    	}
        return super.visitArrayElem(ctx);
    }

    @Override
    public Type visitUnaryOper(WACCParser.UnaryOperContext ctx) {
    	// Return valid type
    	// Should not be called
        return super.visitUnaryOper(ctx);
    }

    @Override
    public Type visitCallFunction(WACCParser.CallFunctionContext ctx) {
    	FunctionType fType = (FunctionType) symbolTable.get(ctx.getChild(1).getText());
    	Type retType = fType.getReturnType();
    	
    	List<Type> types = new ArrayList<>();
    	if (ctx.getChildCount() == 5) {
    		ParseTree argList = ctx.getChild(4);
    		for(int i = 0; i < argList.getChildCount(); i += 2) {
    			types.add(visit(argList.getChild(i)));
    		}
    	}
    	
    	types.add(0, retType);

        Type calledFunctionType = new FunctionType((Type[]) types.toArray());
    	
    	if (!fType.checkType(calledFunctionType)) {
            System.out.println("thrown from visitCallFunction");
    		throw new InvalidTypeException(ctx, fType, calledFunctionType);
    	}
    	
    	return retType;
    }

    @Override
    public Type visitSkipStat(WACCParser.SkipStatContext ctx) {
    	// End of tree
        return super.visitSkipStat(ctx);
    }

    
    //ADD RETURN CHECKING
    @Override
    public Type visitFunction(WACCParser.FunctionContext ctx) {
    	// Add to symbol table, check validity of children under new scope
    	//Check that statement contains a return
        String fName = ctx.getChild(1).getText();
        Type returnType = visit(ctx.getChild(0));
        List<String> idents = new ArrayList<String>();
        List<Type> types = new ArrayList<Type>();
        
        if (ctx.getChildCount() == 8) {
        	ParseTree paramList = ctx.getChild(4);
        	for(int i = 0; i < paramList.getChildCount(); i += 2) {
        		types.add(visit(paramList.getChild(i).getChild(0)));
        		idents.add(paramList.getChild(i).getChild(1).getText());
        	}
        }
        
        types.add(0, returnType);
        FunctionType fType = new FunctionType((Type[]) types.toArray());
        types.remove(0);
        
        symbolTable.add(fName, fType);
        
        symbolTable.enterNewScope();
        for(int i = 0; i < types.size(); i++) {
        	symbolTable.add(idents.get(i), types.get(i));
        }
        String prevFunction = this.currentFunction;
        currentFunction = fName;
        visit(ctx.getChild(ctx.getChildCount() - 2));
        currentFunction = prevFunction;
        symbolTable.exitScope();
        
        return fType;
        
    }

    @Override
    public Type visitReturnStat(WACCParser.ReturnStatContext ctx) {
    	if (currentFunction == "") {
            System.out.println("thrown from visitReturn 1");
    		throw new InvalidReturnException(ctx, "Return called outside of function");
    	}
    	Type retType = ((FunctionType) symbolTable.get(currentFunction)).getReturnType();
    	if(visit(ctx.expr()) !=  retType) {
            System.out.println("thrown from visitReturn 2");
    		throw new InvalidTypeException("");
    	}
    	return retType;
    }
    
    @Override
    public Type visitExpr6(WACCParser.Expr6Context ctx) {
    	//OR (||)
    	if(ctx.getChildCount() == 1) {
    		return visitChildren(ctx);
    	}
    	Type rhsType = visit(ctx.getChild(0));
    	Type lhsType = visit(ctx.getChild(2));
    	if(rhsType != PrimType.BOOL || lhsType != PrimType.BOOL) {
            System.out.println("thrown from visitEXpr6");
    		throw new InvalidTypeException("");
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
    	if(rhsType != PrimType.BOOL || lhsType != PrimType.BOOL) {
            System.out.println("thrown from visitExpr5");
    		throw new InvalidTypeException("");
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
        //System.out.println(rhsType);
        //System.out.println(lhsType);
    	if(!rhsType.checkType(lhsType)) {
            System.out.println("thrown from visitExpr4");
    		throw new InvalidTypeException("");
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
    	if(!rhsType.checkType(lhsType)) {
            System.out.println("thrown from visitExpr3 1");
    		throw new InvalidTypeException("");
    	} else if (rhsType == PrimType.CHAR) {
    		return PrimType.BOOL;
    	} else if (rhsType == PrimType.INT) {
    		return PrimType.BOOL;
    	}
        System.out.println("thrown from visitExpr3 2");
    	throw new InvalidTypeException("");
    }

    @Override
    public Type visitExpr2(WACCParser.Expr2Context ctx) {
    	// PLUS (+) MINUS (-)
    	if(ctx.getChildCount() == 1) {
    		return visitChildren(ctx);
    	}
    	Type rhsType = visit(ctx.getChild(0));
    	Type lhsType = visit(ctx.getChild(2));
    	if(rhsType != PrimType.INT || lhsType != PrimType.INT) {
            System.out.println("thrown from visitExpr2");
    		throw new InvalidTypeException("");
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
    	if(!rhsType.checkType(PrimType.INT) || !lhsType.checkType(PrimType.INT)) {
            System.out.println("thrown from visitExpr1");
    		throw new InvalidTypeException("");
    	}
    	return PrimType.INT;
    }

    // Max


    @Override
    public Type visitBlockStat(@NotNull WACCParser.BlockStatContext ctx) {
        symbolTable.enterNewScope();
        super.visitBlockStat(ctx);
        symbolTable.exitScope();
        return null;
    }

    @Override
    public Type visitWhileStat(WACCParser.WhileStatContext ctx) {
    	// Check condition is boolean, check children are valid.
    	Type type = visit(ctx.expr());
    	if (type.checkType(PrimType.BOOL)) {
    		return visit(ctx.stat());
    	}
        System.out.println("thrown from visitWhileStat");
        throw new InvalidTypeException(ctx, PrimType.BOOL, type);
    }

    @Override
    public Type visitIdentifier(WACCParser.IdentifierContext ctx) {
    	// Returns type from symbol table.
    	try {
        	return symbolTable.get(ctx.getText());
    	} catch (UndeclaredVariableException e) {
            System.out.println("thrown from visitIdentifier");
    		throw new UndeclaredVariableException(ctx, e.getMessage());
    	}
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
    public Type visitInitAssignStat(WACCParser.InitAssignStatContext ctx) {
    	// Check type against rhs, add to symbol table
        Type type = visit(ctx.type());
        Type rhs = visit(ctx.assignRHS());
        //System.out.println(type);
        //System.out.println(rhs);
        if (type.checkType(rhs)) {
        	String ident = ctx.identifier().getText();
        	try {
            	symbolTable.add(ident, type);
        	} catch (RedeclaredVariableException e) {
                System.out.println("thrown from visitInitAssign 1");
        		throw new RedeclaredVariableException(ctx, e.getMessage());
        	}
        	return null;
        }
        //System.out.println("hello");
        System.out.println("thrown from visitInitAssign 2");
        throw new InvalidTypeException(ctx, type, rhs);
    }

    @Override
    public Type visitFreeStat(WACCParser.FreeStatContext ctx) {
    	// Check type is pair or array
        Type type = visit(ctx.expr());
        if ((type instanceof ArrayType) || (type instanceof PairType)) {
        	return type;
        }
        System.out.println("thrown from visitFreeStat");
        throw new InvalidTypeException(ctx, "Expected pair or array, got " + type);
    }

    @Override
    public Type visitParamList(WACCParser.ParamListContext ctx) {
    	// This probably is never called
        return super.visitParamList(ctx);
    }

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
        case WACCLexer.LEN: funType = PrimType.STRING;
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
        
        if (!type.checkType(funType)) {
            System.out.println("thrown from visitUaryExpr");
            throw new InvalidTypeException(ctx, funType, type);
        }
        
        return retType;
    }

    // Anant
    
    @Override
    public Type visitPairElem(WACCParser.PairElemContext ctx) {
        // Check child is pair, go to correct child
        Type pairType = visit(ctx.expr());
        if (!(pairType instanceof PairType)) {
            System.out.println("thrown from visitPairElem");
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
    public Type visitIfStat(WACCParser.IfStatContext ctx) {
    	// Check condition is boolean, check statements are valid.
    	Type type = visitExpr(ctx.expr());
        if (type != PrimType.BOOL){
            System.out.println("thrown from visitIfStat");
            throw new InvalidTypeException(ctx, PrimType.BOOL, type);
        }

        List<WACCParser.StatContext> stat = ctx.stat();
        Iterator<WACCParser.StatContext> iter = stat.iterator();
        while (iter.hasNext()) {
            symbolTable.enterNewScope();
            visit(iter.next());
            symbolTable.exitScope();
        }

        return null;
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
    public Type visitExitStat(WACCParser.ExitStatContext ctx) {
    	// Check child is int
        System.out.println("got in exit");
        Type exit = visitExpr(ctx.expr());
        System.out.println(exit);
        if (exit != PrimType.INT){
            System.out.println("thrown from visitExitStat");
            throw new InvalidTypeException(ctx, PrimType.INT, exit);
        }
        return exit;
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

    @Override
    public Type visitAssignStat(WACCParser.AssignStatContext ctx) {
    	// Check LHS and RHS match
        Type lhs = visitAssignLHS(ctx.assignLHS());
        Type rhs = visitAssignRHS(ctx.assignRHS());
        if (!lhs.checkType(rhs)) {
            System.out.println("thrown from visitAssignStat");
            throw new InvalidTypeException(ctx, rhs, lhs);
        }
        return null;
    }

    @Override
    public Type visitParam(WACCParser.ParamContext ctx) {
    	// Not called
        // just in case we need it
        Type param = visitType(ctx.type());
        return param;
    }
    
    // Jas
    
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
    	Type t = visitExpr(iter.next());
    	while (iter.hasNext()) {
    		Type type = visitExpr(iter.next());
    		if (!t.checkType(type)) {
                System.out.println("thrown from visitArrayLiter");
    			throw new InvalidTypeException(ctx, t, type);
    		}
    	}
        return new ArrayType(t);
    }
    
    @Override
    public Type visitStringLiter(WACCParser.StringLiterContext ctx) {
    	// Return string type
        return PrimType.STRING;
    }

    @Override
    public Type visitIntLiter(WACCParser.IntLiterContext ctx) {
    	// Return int, check
        String intToken = ctx.getText();
        int convertedToken;
        try {
            convertedToken = Integer.parseInt(intToken);
        } catch (NumberFormatException e) {
            System.out.println("thrown from visitIntLiter 1");
            throw new IntegerSizeException(ctx,"Integer " + intToken + " larger than WACCMAXINT");
        }

        int integerLimit = (int)(Math.pow(2,32));

        boolean withinIntBounds = (convertedToken < integerLimit) && (convertedToken > -integerLimit);
        if (!withinIntBounds) {
            System.out.println("thrown from visitIntLiter 2");
            throw new IntegerSizeException("Integer " + intToken + " larger than WACCMAXINT");

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
    
    
    @Override
    public Type visitArgList(WACCParser.ArgListContext ctx) {
    	// Not looked at?
        return super.visitArgList(ctx);
    }
    
    //These shouldn't be called
    @Override

    public Type visitBinaryOper1(WACCParser.BinaryOper1Context ctx) {
        return super.visitBinaryOper1(ctx);
    }

    @Override
    public Type visitBinaryOper2(WACCParser.BinaryOper2Context ctx) {
        return super.visitBinaryOper2(ctx);
    }
    
    @Override
    public Type visitBinaryOper3(WACCParser.BinaryOper3Context ctx) {
        return super.visitBinaryOper3(ctx);
    }

    @Override
    public Type visitBinaryOper4(WACCParser.BinaryOper4Context ctx) {
        return super.visitBinaryOper4(ctx);
    }

    @Override
    public Type visitBinaryOper5(WACCParser.BinaryOper5Context ctx) {
        return super.visitBinaryOper5(ctx);
    }

    @Override
    public Type visitBinaryOper6(WACCParser.BinaryOper6Context ctx) {
        return super.visitBinaryOper6(ctx);
    }

}
