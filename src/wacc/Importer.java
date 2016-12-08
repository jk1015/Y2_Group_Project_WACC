package wacc;

import antlr.WACCLexer;
import antlr.WACCParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import wacc.exceptions.DependencyNotFoundException;
import wacc.exceptions.WACCCompilerException;
import wacc.exceptions.WACCSemanticErrorException;
import wacc.exceptions.WACCSyntaxErrorException;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Importer {

    private final String sourceBasePath = new File("").getAbsolutePath()+"/";
    private String currentBasePath;

    private final String outputFolder = ".temp/";
    private final String outputFileLocation = "out.wacc";

    private int fileNum;

    private InputStream in;
    private File fout;

    private Set<String> importedFilePaths;

    public Importer(InputStream in, String sourcePath) {
        this.in = in;
        fileNum = 0;
        currentBasePath = sourcePath;
        importedFilePaths = new HashSet<>();
        this.fout = new File(sourceBasePath+outputFolder+outputFileLocation);
    }

    // used to recursively import dependencies of dependencies into temp files
    private Importer(InputStream in, int n, String dependencyPath, Set<String> importedFilePaths) {
        this.in = in;
        fileNum = n;
        this.currentBasePath = dependencyPath;
        this.importedFilePaths = importedFilePaths;

        // sets output file to temp file
        this.fout = new File(sourceBasePath+outputFolder+fileNum+outputFileLocation);
    }

    public File importDependencies() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter outputWriter = new BufferedWriter(new OutputStreamWriter(fos));
            String inputLine;

            // read line from current input file
            while ((inputLine = br.readLine()) != null) {

                // check for import statement using regex
                Pattern r = Pattern.compile("(?<=import)\\s+(\\S+)");
                Matcher m = r.matcher(inputLine);

                // if import found, try to import the given file into current output file
                // else, copy current line into current output file
                if (m.find()) {
                    String dependencyName = m.group(1);

                    // if import statement is a wildcard, import files in folders in given directory
                    // else import file normally
                    char lastChar = dependencyName.charAt(dependencyName.length() - 1);
                    if (lastChar == '*')  {
                        importFilesinFolder(dependencyName, outputWriter);
                    } else {
                        checkAndImportFile(dependencyName, outputWriter);
                    }
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

    private void importFilesinFolder(String dependencyName, BufferedWriter outputWriter) throws IOException {
        // get folder file
        File folder = new File(currentBasePath+dependencyName.substring(0, dependencyName.length()-1)+"/");

        // get files in folder
        File[] filesInFolder = folder.listFiles();

        // recursively import files in folder
        for (int i = 0; i < filesInFolder.length; i++) {
            File file = filesInFolder[i];
            if (file.isDirectory()) {
                importFilesinFolder(file.getPath(), outputWriter);
            } else if (file.isFile()) {
                checkAndImportFile(getRelativePath(file.getPath()), outputWriter);
            }
        }
    }

    private void checkAndImportFile(String dependencyName, BufferedWriter outputWriter) throws IOException {
        // get dependency paths
        String dependencyFilePath = currentBasePath + dependencyName;
        String dependencyParentPath = new File(dependencyFilePath).getParentFile().getAbsolutePath()+"/";
        if (!importedFilePaths.contains(dependencyFilePath)) {
            // import sub-dependencies recursively into temporary file
            FileInputStream in;
            try {
                in = new FileInputStream(dependencyFilePath);
            } catch (FileNotFoundException e) {
                DependencyNotFoundException dnfe =
                        new DependencyNotFoundException(getRelativePath(dependencyFilePath)+ " : File not found");
                System.out.println(dnfe.getMessage());
                throw dnfe;
            }
            Importer iw = new Importer(in, ++fileNum, dependencyParentPath, importedFilePaths);
            
            File dependency = iw.importDependencies();
            errorCheck(dependency, dependencyFilePath);
            // copy contents of temporary file into output file
            BufferedReader inputReader = new BufferedReader(new FileReader(dependency));
            copyContentsOfDependencyIntoOutputFile(inputReader, outputWriter);
            importedFilePaths.add(dependencyFilePath);
            // delete temporary file
            dependency.delete();
        }
    }

    private void errorCheck(File dependency, String dependencyFilePath) throws IOException {
        String dependencyRelativePath = getRelativePath(dependencyFilePath);

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
            printErrorInHeader(e , dependencyRelativePath);
            throw e;
        }

        System.out.println(dependencyRelativePath + " : Syntax and Semantic checking is successful.");
    }

    private String getRelativePath(String dependencyFilePath) {
        Path dependency = FileSystems.getDefault().getPath(dependencyFilePath);
        Path source = FileSystems.getDefault().getPath(currentBasePath);
        return source.relativize(dependency).toString();
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
