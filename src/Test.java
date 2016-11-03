import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import antlr.*;

public class Test {
    public static void main(String[] args) throws Exception{
        ANTLRInputStream input = new ANTLRInputStream(System.in);
        WACCLexer lexer = new WACCLexer(input);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        WACCParser parser = new WACCParser(tokenStream);
        ParseTree tree = parser.prog();
        System.out.println(tree.toStringTree(parser));
    }
}