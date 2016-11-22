package wacc;

import antlr.WACCParser;
import org.antlr.v4.runtime.misc.NotNull;
import antlr.WACCParserBaseVisitor;
import wacc.instructions.ExitInstruction;
import wacc.instructions.ExprInstruction;
import wacc.instructions.Instruction;
import wacc.instructions.IntLiterInstruction;

import java.util.ArrayList;
import java.util.List;

public class BackendVisitor extends WACCParserBaseVisitor<List<Instruction>> {

    private final ScopedSymbolTable symbolTable;

    public BackendVisitor(ScopedSymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    @Override
    public List<Instruction> visitArgList(@NotNull WACCParser.ArgListContext ctx) {
        return super.visitArgList(ctx);
    }

    @Override
    public List<Instruction> visitBlockStat(@NotNull WACCParser.BlockStatContext ctx) {
        return super.visitBlockStat(ctx);
    }

    @Override
    public List<Instruction> visitArrayLiter(@NotNull WACCParser.ArrayLiterContext ctx) {
        return super.visitArrayLiter(ctx);
    }

    @Override
    public List<Instruction> visitArrayElem(@NotNull WACCParser.ArrayElemContext ctx) {
        return super.visitArrayElem(ctx);
    }

    @Override
    public List<Instruction> visitAssignRHS(@NotNull WACCParser.AssignRHSContext ctx) {
        return super.visitAssignRHS(ctx);
    }

    @Override
    public List<Instruction> visitAssignLHS(@NotNull WACCParser.AssignLHSContext ctx) {
        return super.visitAssignLHS(ctx);
    }

    @Override
    public List<Instruction> visitUnaryOper(@NotNull WACCParser.UnaryOperContext ctx) {
        return super.visitUnaryOper(ctx);
    }

    @Override
    public List<Instruction> visitCallFunction(@NotNull WACCParser.CallFunctionContext ctx) {
        return super.visitCallFunction(ctx);
    }

    @Override
    public List<Instruction> visitPrintlnStat(@NotNull WACCParser.PrintlnStatContext ctx) {
        return super.visitPrintlnStat(ctx);
    }

    @Override
    public List<Instruction> visitProgram(@NotNull WACCParser.ProgramContext ctx) {
        // Visit functions then visit program.
        List<Instruction> program = visit(ctx.stat());
        for (Instruction inst : program) {
            inst.toAssembly(System.out);
        }
        return program;
    }

    @Override
    public List<Instruction> visitType(@NotNull WACCParser.TypeContext ctx) {
        return super.visitType(ctx);
    }

    @Override
    public List<Instruction> visitSkipStat(@NotNull WACCParser.SkipStatContext ctx) {
        return super.visitSkipStat(ctx);
    }

    @Override
    public List<Instruction> visitFunction(@NotNull WACCParser.FunctionContext ctx) {
        return super.visitFunction(ctx);
    }
    
    @Override
    public List<Instruction> visitReturnStat(@NotNull WACCParser.ReturnStatContext ctx) {
        return super.visitReturnStat(ctx);
    }

    @Override
    public List<Instruction> visitStringLiter(@NotNull WACCParser.StringLiterContext ctx) {
        return super.visitStringLiter(ctx);
    }

    @Override
    public List<Instruction> visitExpr1(@NotNull WACCParser.Expr1Context ctx) {
        return super.visitExpr1(ctx);
    }

    @Override
    public List<Instruction> visitExpr2(@NotNull WACCParser.Expr2Context ctx) {
        return super.visitExpr2(ctx);
    }

    @Override
    public List<Instruction> visitExpr3(@NotNull WACCParser.Expr3Context ctx) {
        return super.visitExpr3(ctx);
    }

    @Override
    public List<Instruction> visitExpr4(@NotNull WACCParser.Expr4Context ctx) {
        return super.visitExpr4(ctx);
    }

    @Override
    public List<Instruction> visitExpr5(@NotNull WACCParser.Expr5Context ctx) {
        return super.visitExpr5(ctx);
    }

    @Override
    public List<Instruction> visitExpr6(@NotNull WACCParser.Expr6Context ctx) {
        return super.visitExpr6(ctx);
    }

    @Override
    public List<Instruction> visitBracketsExpr(@NotNull WACCParser.BracketsExprContext ctx) {
        return super.visitBracketsExpr(ctx);
    }

    @Override
    public List<Instruction> visitWhileStat(@NotNull WACCParser.WhileStatContext ctx) {
        return super.visitWhileStat(ctx);
    }

    @Override
    public List<Instruction> visitIdentifier(@NotNull WACCParser.IdentifierContext ctx) {
        return super.visitIdentifier(ctx);
    }

    @Override
    public List<Instruction> visitArrayType(@NotNull WACCParser.ArrayTypeContext ctx) {
        return super.visitArrayType(ctx);
    }

    @Override
    public List<Instruction> visitInitAssignStat(@NotNull WACCParser.InitAssignStatContext ctx) {
        return super.visitInitAssignStat(ctx);
    }

    @Override
    public List<Instruction> visitFreeStat(@NotNull WACCParser.FreeStatContext ctx) {
        return super.visitFreeStat(ctx);
    }

    @Override
    public List<Instruction> visitPairElemType(@NotNull WACCParser.PairElemTypeContext ctx) {
        return super.visitPairElemType(ctx);
    }

    @Override
    public List<Instruction> visitParamList(@NotNull WACCParser.ParamListContext ctx) {
        return super.visitParamList(ctx);
    }

    @Override
    public List<Instruction> visitBaseExpr(@NotNull WACCParser.BaseExprContext ctx) {
        return super.visitBaseExpr(ctx);
    }

    @Override
    public List<Instruction> visitReadStat(@NotNull WACCParser.ReadStatContext ctx) {
        return super.visitReadStat(ctx);
    }

    @Override
    public List<Instruction> visitPrintStat(@NotNull WACCParser.PrintStatContext ctx) {
        return super.visitPrintStat(ctx);
    }

    @Override
    public List<Instruction> visitIntLiter(@NotNull WACCParser.IntLiterContext ctx) {
        int value = Integer.parseInt(ctx.INT().getText());
        ArrayList<Instruction> ret = new ArrayList<>();
        ret.add(new IntLiterInstruction(value, 4));
        return ret;
    }

    @Override
    public List<Instruction> visitBaseType(@NotNull WACCParser.BaseTypeContext ctx) {
        return super.visitBaseType(ctx);
    }

    @Override
    public List<Instruction> visitBinaryOper1(@NotNull WACCParser.BinaryOper1Context ctx) {
        return super.visitBinaryOper1(ctx);
    }

    @Override
    public List<Instruction> visitBinaryOper2(@NotNull WACCParser.BinaryOper2Context ctx) {
        return super.visitBinaryOper2(ctx);
    }

    @Override
    public List<Instruction> visitBinaryOper3(@NotNull WACCParser.BinaryOper3Context ctx) {
        return super.visitBinaryOper3(ctx);
    }

    @Override
    public List<Instruction> visitBinaryOper4(@NotNull WACCParser.BinaryOper4Context ctx) {
        return super.visitBinaryOper4(ctx);
    }

    @Override
    public List<Instruction> visitBinaryOper5(@NotNull WACCParser.BinaryOper5Context ctx) {
        return super.visitBinaryOper5(ctx);
    }

    @Override
    public List<Instruction> visitBinaryOper6(@NotNull WACCParser.BinaryOper6Context ctx) {
        return super.visitBinaryOper6(ctx);
    }

    @Override
    public List<Instruction> visitUnaryExpr(@NotNull WACCParser.UnaryExprContext ctx) {
        return super.visitUnaryExpr(ctx);
    }

    @Override
    public List<Instruction> visitPairLiter(@NotNull WACCParser.PairLiterContext ctx) {
        return super.visitPairLiter(ctx);
    }

    @Override
    public List<Instruction> visitParam(@NotNull WACCParser.ParamContext ctx) {
        return super.visitParam(ctx);
    }

    @Override
    public List<Instruction> visitCharLiter(@NotNull WACCParser.CharLiterContext ctx) {
        return super.visitCharLiter(ctx);
    }

    @Override
    public List<Instruction> visitExpr(@NotNull WACCParser.ExprContext ctx) {
        return super.visitExpr(ctx);
    }

    @Override
    public List<Instruction> visitPairElem(@NotNull WACCParser.PairElemContext ctx) {
        return super.visitPairElem(ctx);
    }

    @Override
    public List<Instruction> visitIfStat(@NotNull WACCParser.IfStatContext ctx) {
        return super.visitIfStat(ctx);
    }

    @Override
    public List<Instruction> visitNewPair(@NotNull WACCParser.NewPairContext ctx) {
        return super.visitNewPair(ctx);
    }

    @Override
    public List<Instruction> visitPairNullType(@NotNull WACCParser.PairNullTypeContext ctx) {
        return super.visitPairNullType(ctx);
    }

    @Override
    public List<Instruction> visitExitStat(@NotNull WACCParser.ExitStatContext ctx) {
        ExprInstruction exInst = (ExprInstruction) (visitExpr(ctx.expr()).get(0));
        Instruction inst = new ExitInstruction(exInst);
        ArrayList<Instruction> ret = new ArrayList<>();
        ret.add(inst);
        return ret;
    }

    @Override
    public List<Instruction> visitBoolLiter(@NotNull WACCParser.BoolLiterContext ctx) {
        return super.visitBoolLiter(ctx);
    }

    @Override
    public List<Instruction> visitPairType(@NotNull WACCParser.PairTypeContext ctx) {
        return super.visitPairType(ctx);
    }

    @Override
    public List<Instruction> visitSeqStat(@NotNull WACCParser.SeqStatContext ctx) {
        return super.visitSeqStat(ctx);
    }

    @Override
    public List<Instruction> visitAssignStat(@NotNull WACCParser.AssignStatContext ctx) {
        return super.visitAssignStat(ctx);
    }
}
