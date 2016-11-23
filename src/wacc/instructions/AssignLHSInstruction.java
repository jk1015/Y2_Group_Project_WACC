package wacc.instructions;

public class AssignLHSInstruction {
    private ExprInstruction expr;

    public AssignLHSInstruction(ExprInstruction expr) {
        this.expr = expr;
    }

    public String getLocationString() {
        return null;
    }

    public ExprInstruction grtExpr() {
        return expr;
    }
}
