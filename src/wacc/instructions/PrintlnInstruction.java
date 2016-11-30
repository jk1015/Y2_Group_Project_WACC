package wacc.instructions;

import wacc.instructions.expressions.ExprInstruction;

import java.io.PrintStream;
import java.util.HashMap;


public class PrintlnInstruction extends PrintInstruction {
        public PrintlnInstruction(ExprInstruction expr, HashMap<String,String> dataMap) {
            super(expr,dataMap);
        }

        @Override
        public void toAssembly(PrintStream out) {
            super.toAssembly(out);
            out.println("BL p_print_ln");

        }
        @Override
        public HashMap<String,String>  addDataAndLabels() {
            dataMap = super.addDataAndLabels();
            String[] ascii = {"\"\\0\""};
            dataMap = dataAndLabels.addDataAndLabels("p_print_ln",ascii);
            return dataMap;
        }
}

