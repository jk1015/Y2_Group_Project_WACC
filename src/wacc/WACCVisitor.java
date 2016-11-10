package wacc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import antlr.WACCLexer;
import antlr.WACCParser;
import antlr.WACCParser.ExprContext;
import antlr.WACCParserBaseVisitor;
import wacc.exceptions.InvalidTypeException;
import wacc.types.*;

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
    			throw new InvalidTypeException("");
    		}
    	}
    	if (!(symbolTable.get(ctx.getChild(0).getText()) instanceof ArrayType)) {
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
    	
    	List<Type> types = new ArrayList<Type>();
    	if (ctx.getChildCount() == 5) {
    		ParseTree argList = ctx.getChild(4);
    		for(int i = 0; i < argList.getChildCount(); i += 2) {
    			types.add(visit(argList.getChild(i)));
    		}
    	}
    	
    	types.add(0, retType);
    	
    	if (!fType.checkType(new FunctionType((Type[]) types.toArray()))) { 
    		//THROW ERROR
    		return null;
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
    		//THROW ERROR
    		return null;
    	}
    	Type retType = ((FunctionType) symbolTable.get(currentFunction)).getReturnType();
    	if(visit(ctx.expr()) !=  retType) {
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
    	if(rhsType != lhsType) {
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
    	if(rhsType != lhsType) {
    		throw new InvalidTypeException("");
    	} else if (rhsType == PrimType.CHAR) {
    		return PrimType.CHAR;
    	} else if (rhsType == PrimType.INT) {
    		return PrimType.INT;
    	}
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
    	if(rhsType != PrimType.INT || lhsType != PrimType.INT) {
    		throw new InvalidTypeException("");
    	}
    	return PrimType.INT;
    }

    // Max
    
    @Override
    public Type visitWhileStat(WACCParser.WhileStatContext ctx) {
    	// Check condition is boolean, check children are valid.
        return super.visitWhileStat(ctx);
    }

    @Override
    public Type visitIdentifier(WACCParser.IdentifierContext ctx) {
    	// This should never be called
        return super.visitIdentifier(ctx);
    }

    @Override
    public Type visitArrayType(WACCParser.ArrayTypeContext ctx) {
    	// Returns array version of child type
        return super.visitArrayType(ctx);
    }

    @Override
    public Type visitInitAssignStat(WACCParser.InitAssignStatContext ctx) {
    	// Check type against rhs, add to symbol table
        return super.visitInitAssignStat(ctx);
    }

    @Override
    public Type visitFreeStat(WACCParser.FreeStatContext ctx) {
    	// Check type is pair or array
        return super.visitFreeStat(ctx);
    }

    @Override
    public Type visitParamList(WACCParser.ParamListContext ctx) {
    	// This probably is never called
        return super.visitParamList(ctx);
    }

    @Override
    public Type visitReadStat(WACCParser.ReadStatContext ctx) {
    	// Check LHS is variable, array or pair, and can take input
        return super.visitReadStat(ctx);
    }

    @Override
    public Type visitUnaryExpr(WACCParser.UnaryExprContext ctx) {
    	// Examine expression and operator for validity
        return super.visitUnaryExpr(ctx);
    }

    // Anant
    
    @Override
    public Type visitPairElem(WACCParser.PairElemContext ctx) {
        // Check child is pair, go to correct child
    	return super.visitPairElem(ctx);
    }

    @Override
    public Type visitIfStat(WACCParser.IfStatContext ctx) {
    	// Check condition is boolean, check statements are valid.
        return super.visitIfStat(ctx);
    }

    @Override
    public Type visitNewPair(WACCParser.NewPairContext ctx) {
    	// Return the pair type of the two children.
        return super.visitNewPair(ctx);
    }

    @Override
    public Type visitExitStat(WACCParser.ExitStatContext ctx) {
    	// Check child is int
        return super.visitExitStat(ctx);
    }

    @Override
    public Type visitPairType(WACCParser.PairTypeContext ctx) {
    	// Return the pair type
        return super.visitPairType(ctx);
    }

    @Override
    public Type visitAssignStat(WACCParser.AssignStatContext ctx) {
    	// Check LHS and RHS match
        return super.visitAssignStat(ctx);
    }

    @Override
    public Type visitParam(WACCParser.ParamContext ctx) {
    	// Not called
        return super.visitParam(ctx);
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
    		if (t != visitExpr(iter.next())) {
    			throw new InvalidTypeException(ctx.getStart().getLine() + "");
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
    	// Return int, check size
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
