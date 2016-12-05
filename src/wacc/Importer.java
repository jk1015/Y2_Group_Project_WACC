package wacc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Importer {


    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException();
        }

        FileInputStream fin = null;

        try {
            fin = new FileInputStream(args[0]);
        }
        catch (FileNotFoundException e) {
        }

        ImporterWriter iw = new ImporterWriter(fin);
        iw.importDependencies();
    }
}
