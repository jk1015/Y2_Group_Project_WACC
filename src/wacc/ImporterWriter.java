package wacc;

import antlr.WACCLexer;
import antlr.WACCParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import wacc.exceptions.WACCCompilerException;
import wacc.exceptions.WACCSemanticErrorException;
import wacc.exceptions.WACCSyntaxErrorException;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ImporterWriter {

    private final String outputFolder = ".temp/";
    private final String outputFileLocation = "out.wacc";
    private int currentOutputFile;
    private InputStream in;
    private File fout;

    public ImporterWriter(InputStream in) {
        this.in = in;
        currentOutputFile = 0;
        this.fout = new File(outputFolder+outputFileLocation);
    }

    private ImporterWriter(InputStream in, int n) {
        this.in = in;
        currentOutputFile = n;
        this.fout = new File(outputFolder+currentOutputFile+outputFileLocation);
    }

    public File importDependencies() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter outputWriter = new BufferedWriter(new OutputStreamWriter(fos));
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                Pattern r = Pattern.compile("(?<=import)\\s+(\\S+)");
                Matcher m = r.matcher(inputLine);
                if (m.find()) {
                    // get dependency name
                    String dependencyName = m.group(1);

                    // import sub-dependencies recursively into temporary file
                    ImporterWriter iw = new ImporterWriter(new FileInputStream(dependencyName), ++currentOutputFile);
                    File dependency = iw.importDependencies();

                    errorCheck(dependency, dependencyName);

                    // copy contents of temporary file into output file
                    BufferedReader inputReader = new BufferedReader(new FileReader(dependency));
                    copyContentsOfDependencyIntoOutputFile(inputReader, outputWriter);

                    // delete temporary file
                    dependency.delete();
                } else {
                    outputWriter.write(inputLine);
                    outputWriter.newLine();
                }
            }
            outputWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist");
        } catch (IOException e) {
            System.out.println("File invalid");
        }
        return fout;
    }

    private void errorCheck(File dependency, String dependencyName) throws IOException {
        ANTLRInputStream input;
        input = new ANTLRInputStream(new FileInputStream(dependency));

        WACCLexer lexer = new WACCLexer(input);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);

        WACCParser parser = new WACCParser(tokenStream);
        parser.removeErrorListeners();
        parser.addErrorListener(WACCErrorListener.INSTANCE);

        try {
            ParseTree tree = parser.header();
            FrontendVisitor semanticAnalysis = new FrontendVisitor();
            semanticAnalysis.visit(tree);
        } catch (WACCSemanticErrorException | WACCSyntaxErrorException e) {
            printErrorInHeader(e , dependencyName);
            throw e;
        }

        System.out.println("Syntax and Semantic checking of " + dependencyName + " successful.");
    }

    private void copyContentsOfDependencyIntoOutputFile(BufferedReader bin, BufferedWriter bw) throws IOException {
        String transfer = bin.readLine();

        if (transfer == null) {
            bw.newLine();
            return;
        }

        while (true) {
            bw.write(transfer);
            transfer = bin.readLine();
            if (transfer != null) {
                bw.write(' ');
            } else {
                break;
            }
        }

        bw.newLine();
    }

    private void printErrorInHeader(WACCCompilerException e, String dependencyName) {
        System.out.println(dependencyName + " : " + e);
    }
}
