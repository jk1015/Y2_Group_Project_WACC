package wacc;
import static java.lang.System.exit;

import java.io.*;

public class Compiler {
	
    public static void main(String[] args) {
    	Frontend frontend = new Frontend();
    	InputStream in;
        PrintStream out;
    	CompilerStatus status = CompilerStatus.SUCCESS;
    	try {
    		if (args.length == 0) {
    			in = System.in;
    		} else {
    			in = new FileInputStream(args[0]);
    		}
    		if (args.length <= 1) {
                out = System.out;
            } else {
                out = new PrintStream(args[1]);
            }
    		status = frontend.run(in, out);
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

