package wacc;

import static java.lang.System.exit;

import java.io.*;
import java.nio.file.Path;

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
        ImporterWriter iw = new ImporterWriter(in);

        CompilerStatus compilerStatus = CompilerStatus.SUCCESS;

        File fileAfterImports = null;

        try {
            fileAfterImports = iw.importDependencies();
        } catch (WACCSyntaxErrorException e) {
            compilerStatus = CompilerStatus.SYNTAX_ERROR;
        } catch (WACCSemanticErrorException e) {
            compilerStatus = CompilerStatus.SEMANTIC_ERROR;
        }

        if (compilerStatus != CompilerStatus.SUCCESS) {
            return compilerStatus;
        }

        ANTLRInputStream input;
		input = new ANTLRInputStream(new FileInputStream(fileAfterImports));

        WACCLexer lexer = new WACCLexer(input);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);

        WACCParser parser = new WACCParser(tokenStream);
        parser.removeErrorListeners();
        parser.addErrorListener(WACCErrorListener.INSTANCE);

        ParseTree tree = new ParserRuleContext();

        try {
            tree = parser.program();
        } catch (WACCCompilerException e) {
            printErrorInMainProgram(e);
            exit(CompilerStatus.SYNTAX_ERROR.code());
        }

        FrontendVisitor semanticAnalysis = new FrontendVisitor();

        try {
        	semanticAnalysis.visit(tree);
        } catch (WACCSyntaxErrorException e) {
        	printErrorInMainProgram(e);
        	compilerStatus = CompilerStatus.SYNTAX_ERROR;
        } catch (WACCSemanticErrorException e) {
            printErrorInMainProgram(e);
        	compilerStatus = CompilerStatus.SEMANTIC_ERROR;
        }

        if (compilerStatus != CompilerStatus.SUCCESS) {
            return compilerStatus;
        }

        System.out.println("Main Program : Syntax and Semantic checking is successful");

        BackendVisitor back = new BackendVisitor(null);
        back.visit(tree).toAssembly(out);

        return compilerStatus;
	}

    private void printErrorInMainProgram(WACCCompilerException e) {
        System.out.println("Main Program : " + e);
    }

}
