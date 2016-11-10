package wacc;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import antlr.*;

import static java.lang.System.exit;

public class Test {
    public static void main(String[] args) throws Exception{
        ANTLRInputStream input = new ANTLRInputStream(System.in);
        WACCLexer lexer = new WACCLexer(input);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        WACCParser parser = new WACCParser(tokenStream);
        ParseTree tree = parser.program();
        try {
            WACCVisitor semanticAnalysis = new WACCVisitor();
            semanticAnalysis.visit(tree);
        } catch (Exception e) {
            System.out.println(e);
        }

        exit(0);
    }
}

