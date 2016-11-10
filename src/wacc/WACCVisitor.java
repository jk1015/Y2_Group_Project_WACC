package wacc;

import antlr.WACCLexer;
import antlr.WACCParser;
import antlr.WACCParser.ExprContext;
import antlr.WACCParserBaseVisitor;
import wacc.exceptions.IntegerSizeException;
import org.antlr.v4.runtime.tree.TerminalNode;
import wacc.exceptions.InvalidTypeException;
import wacc.exceptions.RedeclaredVariableException;
import wacc.exceptions.UndeclaredVariableException;
import wacc.types.*;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ad5115 on 08/11/16.
 */
public class WACCVisitor extends WACCParserBaseVisitor<Type> {
	
	private final ScopedSymbolTable symbolTable;
	
	public WACCVisitor() {
		symbolTable = new ScopedSymbolTable();
	}
	
	// James
	
	@Override
	public Type visitBaseType(WACCParser.BaseTypeContext ctx) {
		// Look contents and return type.
		return null;
	}
	
    @Override
    public Type visitArrayElem(WACCParser.ArrayElemContext ctx) {
    	// Make sure expression is int
        return super.visitArrayElem(ctx);
    }

    @Override
    public Type visitUnaryOper(WACCParser.UnaryOperContext ctx) {
    	// Return valid type
        return super.visitUnaryOper(ctx);
    }

    @Override
    public Type visitCallFunction(WACCParser.CallFunctionContext ctx) {
    	// Return function return type, and check children against symbol table
        return super.visitCallFunction(ctx);
    }

    @Override
    public Type visitSkipStat(WACCParser.SkipStatContext ctx) {
    	// End of tree
        return super.visitSkipStat(ctx);
    }

    @Override
    public Type visitFunction(WACCParser.FunctionContext ctx) {
    	// Add to symbol table, check validity of children under new scope
    	//Check that statement contains a return
        return super.visitFunction(ctx);
    }

    @Override
    public Type visitReturnStat(WACCParser.ReturnStatContext ctx) {
    	// Check whether we're in function, returns correct type
        return super.visitReturnStat(ctx);
    }

    @Override
    public Type visitExpr6(WACCParser.Expr6Context ctx) {
    	// Check LHS, RHS and operator
        return super.visitExpr6(ctx);
    }

    @Override
    public Type visitExpr5(WACCParser.Expr5Context ctx) {
    	// As above
        return super.visitExpr5(ctx);
    }
    
    @Override
    public Type visitExpr4(WACCParser.Expr4Context ctx) {
    	// As above
        return super.visitExpr4(ctx);
    }

    @Override
    public Type visitExpr3(WACCParser.Expr3Context ctx) {
    	// As above
        return super.visitExpr3(ctx);
    }

    @Override
    public Type visitExpr2(WACCParser.Expr2Context ctx) {
    	// As Above
        return super.visitExpr2(ctx);
    }

    @Override
    public Type visitExpr1(WACCParser.Expr1Context ctx) {
    	// Might hash these
    	// As above for expr1s
    	// or look at internals
        return super.visitExpr1(ctx);
    }

    // Max
    
    @Override
    public Type visitWhileStat(WACCParser.WhileStatContext ctx) {
    	// Check condition is boolean, check children are valid.
    	Type type = visit(ctx.expr());
    	if (type.checkType(PrimType.BOOL)) {
    		return visit(ctx.stat());
    	}
        throw new InvalidTypeException(ctx, PrimType.BOOL, type);
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
    public Type visitArrayType(WACCParser.ArrayTypeContext ctx) {
    	// Returns array version of child type
    	int arrayDepth = ctx.CLOSE_SQUARE().size();
    	Type type = visitChildren(ctx);
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
        if (type.checkType(rhs)) {
        	String ident = ctx.identifier().getText();
        	try {
            	symbolTable.add(ident, type);
        	} catch (RedeclaredVariableException e) {
        		throw new RedeclaredVariableException(ctx, e.getMessage());
        	}
        }
        throw new InvalidTypeException(ctx, type, rhs);
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
        Type exit = visitExpr(ctx.expr());
        if (exit != PrimType.INT){
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
    public Type visitAssignStat(WACCParser.AssignStatContext ctx) {
    	// Check LHS and RHS match
        Type lhs = visitAssignLHS(ctx.assignLHS());
        Type rhs = visitAssignRHS(ctx.assignRHS());
        if (lhs != rhs) {
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
    	// Return null pair
        return super.visitPairLiter(ctx);
    }
    
    @Override
    public Type visitArrayLiter(WACCParser.ArrayLiterContext ctx) {
    	// Check array is valid
    	List<ExprContext> expr = ctx.expr();
    	Iterator<ExprContext> iter = expr.iterator();
    	Type t = visitExpr(iter.next());
    	while (iter.hasNext()) {
    		Type type = visitExpr(iter.next());
    		if (t != type) {
    			throw new InvalidTypeException(ctx, t, type);
    		}
    	}
        return t;
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
        int convertedToken = Integer.parseInt(intToken);
        int integerLimit = (int)(Math.pow(2,32));

        boolean withinIntBounds = (convertedToken < integerLimit) && (convertedToken > -integerLimit);
        if (!withinIntBounds) {
            throw new IntegerSizeException("Integer " + convertedToken + " larger than WACCMAXINT");
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
