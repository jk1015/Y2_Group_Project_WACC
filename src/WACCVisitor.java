import java.util.*;
import antlr.*;

public class WACCVisitor extends WACCParserBaseVisitor<Void>{
    public Void visitProgram(WACCParser.ProgramContext ctx) {
        System.out.println("found Program");
        return visitChildren(ctx);
    }

    public Void visitFunc(WACCParser.FuncContext ctx) {
        System.out.println("found Function");
        return visitChildren(ctx);
    }

    public Void visitParam_list(WACCParser.Param_listContext ctx) {
        System.out.println("found Param list");
        return visitChildren(ctx);
    }

    public Void visitParam(WACCParser.ParamContext ctx) {
        System.out.println("found param");
        return visitChildren(ctx);
    }

    public Void visitStat(WACCParser.StatContext ctx) {
        System.out.println("found stat");
        return visitChildren(ctx);
    }

    public Void visitAssign_lhs(WACCParser.Assign_lhsContext ctx) {
        System.out.println("found lhs");
        return visitChildren(ctx);
    }

    public Void visitAssign_rhs(WACCParser.Assign_rhsContext ctx) {
        System.out.println("found rsh");
        return visitChildren(ctx);
    }

    public Void visitArg_list(WACCParser.Arg_listContext ctx) {
        System.out.println("found Arg list");
        return visitChildren(ctx);
    }

    public Void visitPair_elem(WACCParser.Pair_elemContext ctx) {
        System.out.println("found Pair elem");
        return visitChildren(ctx);
    }

    public Void visitType(WACCParser.TypeContext ctx) {
        System.out.println("found Type");
        return visitChildren(ctx);
    }

    public Void visitBase_type(WACCParser.Base_typeContext ctx) {
        System.out.println("found Base type");
        return visitChildren(ctx);
    }

    public Void visitArray_type(WACCParser.Array_typeContext ctx) {
        System.out.println("found Array");
        return visitChildren(ctx);
    }

    public Void visitPair_type(WACCParser.Pair_typeContext ctx) {
        System.out.println("found Pair type");
        return visitChildren(ctx);
    }

    public Void visitPair_elem_type(WACCParser.Pair_elem_typeContext ctx) {
        System.out.println("found pair elem");
        return visitChildren(ctx);
    }

    public Void visitExpr(WACCParser.ExprContext ctx) {
        System.out.println("found expr");
        return visitChildren(ctx);
    }

    public Void visitUnary_oper(WACCParser.Unary_operContext ctx) {
        System.out.println("found unary operator");
        return visitChildren(ctx);
    }

    public Void visitBinary_oper(WACCParser.Binary_operContext ctx) {
        System.out.println("found binary operator");
        return visitChildren(ctx);
    }

    public Void visitArray_elem(WACCParser.Array_elemContext ctx) {
        System.out.println("found array elem");
        return visitChildren(ctx);
    }

    public Void visitArray_liter(WACCParser.Array_literContext ctx) {
        System.out.println("found array liter");
        return visitChildren(ctx);
    }

}
