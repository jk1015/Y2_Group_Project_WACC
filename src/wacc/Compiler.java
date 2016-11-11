package wacc;
import static java.lang.System.exit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Compiler {
	
    public static void main(String[] args) {
    	Backend backend = new Backend();
    	InputStream in;
    	CompilerStatus status = CompilerStatus.SUCCESS;
    	try {
    		if (args.length == 0) {
    			in = System.in;
    		} else {
    			in = new FileInputStream(args[0]);
    		}
    		status = backend.run(in);
    		System.out.println("Syntax and Semantic checking successful.");
    	} catch (FileNotFoundException e) {
    		System.err.println("File \"" + args[0] + "\" not found.");
    		exit(-1);
    	} catch (IOException e) {
    		System.err.println("Bad input");
    		System.err.println(e);
    		exit(-1);
    	}
    	exit(status.code());
    }
    
}

