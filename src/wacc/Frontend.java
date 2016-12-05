package wacc;

import static java.lang.System.exit;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import antlr.WACCLexer;
import antlr.WACCParser;
import wacc.exceptions.WACCCompilerException;
import wacc.exceptions.WACCSemanticErrorException;
import wacc.exceptions.WACCSyntaxErrorException;

public class Frontend {
	
	public CompilerStatus run(InputStream in, PrintStream out) throws IOException {
        ANTLRInputStream input;
		input = new ANTLRInputStream(in);

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

        FrontendVisitor semanticAnalysis = new FrontendVisitor();

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

        if (compilerStatus != CompilerStatus.SUCCESS) {
            return compilerStatus;
        }

        System.out.println();
        System.out.println("Syntax and Semantic checking of main program is successful");

        BackendVisitor back = new BackendVisitor(null);
        back.visit(tree).toAssembly(out);

        return compilerStatus;
	}
	
}
