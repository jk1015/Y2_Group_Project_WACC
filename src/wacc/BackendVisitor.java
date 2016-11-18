package wacc;

import antlr.WACCParser;
import org.antlr.v4.runtime.misc.NotNull;
import antlr.WACCParserBaseVisitor;
import wacc.instructions.*;

import java.util.ArrayList;
import java.util.List;

public class BackendVisitor extends WACCParserBaseVisitor<Instruction> {

    private final ScopedSymbolTable symbolTable;

    public BackendVisitor(ScopedSymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    @Override
    public Instruction visitProgram(@NotNull WACCParser.ProgramContext ctx) {
        // Visit functions then visit program.
        Instruction program = new ProgramInstruction(visit(ctx.stat()));
        program.toAssembly(System.out);
        return program;
    }

    @Override
    public Instruction visitFunction(@NotNull WACCParser.FunctionContext ctx) {
        return super.visitFunction(ctx);
    }

    @Override
    public Instruction visitCallFunction(@NotNull WACCParser.CallFunctionContext ctx) {
        return super.visitCallFunction(ctx);
    }

    @Override
    public Instruction visitReturnStat(@NotNull WACCParser.ReturnStatContext ctx) {
        return super.visitReturnStat(ctx);
    }

    @Override
    public Instruction visitSeqStat(@NotNull WACCParser.SeqStatContext ctx) {
        return super.visitSeqStat(ctx);
    }

    @Override
    public Instruction visitInitAssignStat(@NotNull WACCParser.InitAssignStatContext ctx) {
        return super.visitInitAssignStat(ctx);
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
        return super.visitWhileStat(ctx);
    }

    @Override
    public Instruction visitIfStat(@NotNull WACCParser.IfStatContext ctx) {
        return super.visitIfStat(ctx);
    }

    @Override
    public Instruction visitReadStat(@NotNull WACCParser.ReadStatContext ctx) {
        return super.visitReadStat(ctx);
    }

    @Override
    public Instruction visitPrintStat(@NotNull WACCParser.PrintStatContext ctx) {
        return super.visitPrintStat(ctx);
    }

    @Override
    public Instruction visitPrintlnStat(@NotNull WACCParser.PrintlnStatContext ctx) {
        return super.visitPrintlnStat(ctx);
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
        return super.visitUnaryExpr(ctx);
    }

    @Override
    public Instruction visitBracketsExpr(@NotNull WACCParser.BracketsExprContext ctx) {
        return super.visitBracketsExpr(ctx);
    }

    @Override
    public Instruction visitExpr6(@NotNull WACCParser.Expr6Context ctx) {
        return super.visitExpr6(ctx);
    }

    @Override
    public Instruction visitExpr5(@NotNull WACCParser.Expr5Context ctx) {
        return super.visitExpr5(ctx);
    }

    @Override
    public Instruction visitExpr4(@NotNull WACCParser.Expr4Context ctx) {
        return super.visitExpr4(ctx);
    }

    @Override
    public Instruction visitExpr3(@NotNull WACCParser.Expr3Context ctx) {
        return super.visitExpr3(ctx);
    }

    @Override
    public Instruction visitExpr2(@NotNull WACCParser.Expr2Context ctx) {
        return super.visitExpr2(ctx);
    }

    @Override
    public Instruction visitExpr1(@NotNull WACCParser.Expr1Context ctx) {
        return super.visitExpr1(ctx);
    }

    @Override
    public Instruction visitPairLiter(@NotNull WACCParser.PairLiterContext ctx) {
        return super.visitPairLiter(ctx);
    }

    @Override
    public Instruction visitArrayLiter(@NotNull WACCParser.ArrayLiterContext ctx) {
        return super.visitArrayLiter(ctx);
    }

    @Override
    public Instruction visitStringLiter(@NotNull WACCParser.StringLiterContext ctx) {
        return super.visitStringLiter(ctx);
    }

    @Override
    public Instruction visitIntLiter(@NotNull WACCParser.IntLiterContext ctx) {
        int value = Integer.parseInt(ctx.INT().getText());
        return new IntLiterInstruction(value, 4);
    }

    @Override
    public Instruction visitCharLiter(@NotNull WACCParser.CharLiterContext ctx) {
        return super.visitCharLiter(ctx);
    }

    @Override
    public Instruction visitBoolLiter(@NotNull WACCParser.BoolLiterContext ctx) {
        return super.visitBoolLiter(ctx);
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
        return super.visitIdentifier(ctx);
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
