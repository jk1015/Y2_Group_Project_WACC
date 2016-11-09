package wacc;

import antlr.WACCParser;
import antlr.WACCParserBaseVisitor;
import wacc.types.*;

/**
 * Created by ad5115 on 08/11/16.
 */
public class WACCVisitor extends WACCParserBaseVisitor<Type> {
	
	private final ScopedSymbolTable symbolTable;
	
	public WACCVisitor() {
		symbolTable = new ScopedSymbolTable();
	}
	
	@Override
	public Type visitBaseType(WACCParser.BaseTypeContext ctx) {
		// Look contents and return type.
		return null;
	}
	
    @Override
    public Type visitArrayLiter(WACCParser.ArrayLiterContext ctx) {
    	// Check array is valid
        return super.visitArrayLiter(ctx);
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

    @Override
    public Type visitStringLiter(WACCParser.StringLiterContext ctx) {
    	// Return string type
        return PrimType.STRING;
    }

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
    public Type visitIntLiter(WACCParser.IntLiterContext ctx) {
    	// Return int
        return PrimType.INT;
    }
    
    @Override
    public Type visitUnaryExpr(WACCParser.UnaryExprContext ctx) {
    	// Examine expression and operator for validity
        return super.visitUnaryExpr(ctx);
    }

    @Override
    public Type visitPairLiter(WACCParser.PairLiterContext ctx) {
    	// Return null pair
        return super.visitPairLiter(ctx);
    }

    @Override
    public Type visitCharLiter(WACCParser.CharLiterContext ctx) {
    	// Return char
        return PrimType.CHAR;
    }

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
    public Type visitBoolLiter(WACCParser.BoolLiterContext ctx) {
    	// Return bool
        return PrimType.BOOL;
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
    public Type visitArgList(WACCParser.ArgListContext ctx) {
    	// Not looked at?
        return super.visitArgList(ctx);
    }

    @Override
    public Type visitParam(WACCParser.ParamContext ctx) {
    	// Not called
        return super.visitParam(ctx);
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
