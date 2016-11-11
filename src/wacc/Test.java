package wacc;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.*;
import antlr.*;
import wacc.exceptions.WACCCompilerException;
import wacc.exceptions.WACCSemanticErrorException;
import wacc.exceptions.WACCSyntaxErrorException;

import static java.lang.System.exit;

public class Test {
    public static void main(String[] args) throws Exception{
        ANTLRInputStream input = new ANTLRInputStream(System.in);

        WACCLexer lexer = new WACCLexer(input);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);

        WACCParser parser = new WACCParser(tokenStream);
        parser.removeErrorListeners();
        parser.addErrorListener(WACCErrorListener.INSTANCE);

        ParseTree tree = new ParserRuleContext();

        try {
            tree = parser.program();
        } catch (WACCCompilerException e) {
            System.out.println(e);
            exit(100);
        }


        WACCVisitor semanticAnalysis = new WACCVisitor();

        CompilerStatus compilerStatus = CompilerStatus.SUCCESS;

        try {
            semanticAnalysis.visit(tree);
        } catch (Exception e)  {
            System.out.println(e);
            if (e instanceof WACCSyntaxErrorException) {
                compilerStatus = CompilerStatus.SYNTAX_ERROR;
            }
            if (e instanceof WACCSemanticErrorException) {
                compilerStatus = CompilerStatus.SEMANTIC_ERROR;
            }
        }

        switch (compilerStatus) {
            case SUCCESS:           exit(0); break;
            case SYNTAX_ERROR:      exit(100); break;
            case SEMANTIC_ERROR:    exit(200); break;
        }
    }
}

