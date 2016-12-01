package wacc;

public class Importer {


    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException();
        }

        ImporterWriter iw = new ImporterWriter(args[0]);
        iw.importDependencies();
    }
}
