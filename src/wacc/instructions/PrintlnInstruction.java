package wacc.instructions;

import wacc.instructions.expressions.ExprInstruction;

import java.io.PrintStream;


    public class PrintlnInstruction extends PrintInstruction {

        public PrintlnInstruction(ExprInstruction expr, int numOfMsg) {
            super(expr,numOfMsg);
        }

        @Override
        public void toAssembly(PrintStream out) {
            super.toAssembly(out);
            out.println("BL p_print_ln");

        }
        @Override
        public int addDataAndLabels() {
            numOfMsg = super.addDataAndLabels();
            String nameOfMsg1 = setData("\0");
            String[] namesOfMsg = {nameOfMsg1};
            setLabel("p_print_ln",namesOfMsg);
            return numOfMsg;
        }
    }

