package wacc;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import antlr.*;
import wacc.exceptions.WACCCompilerException;
import wacc.exceptions.WACCSemanticErrorException;
import wacc.exceptions.WACCSyntaxErrorException;

import static java.lang.System.exit;

import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        ANTLRInputStream input = null;
		try {
			input = new ANTLRInputStream(System.in);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Bad input.");
			exit(-1);
		}

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
            exit(CompilerStatus.SYNTAX_ERROR.code());
        }

        WACCVisitor semanticAnalysis = new WACCVisitor();

        CompilerStatus compilerStatus = CompilerStatus.SUCCESS;

        try {
        	semanticAnalysis.visit(tree);
        } catch (WACCSyntaxErrorException e) {
        	System.err.println(e);
        	compilerStatus = CompilerStatus.SYNTAX_ERROR;
        } catch (WACCSemanticErrorException e) {
        	System.err.println(e);
        	compilerStatus = CompilerStatus.SEMANTIC_ERROR;
        }
        exit(compilerStatus.code());
    }
}

