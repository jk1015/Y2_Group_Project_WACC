package wacc;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ImporterWriter {

    private final String outputFileLocation = "out.wacc";
    private int currentOutputFile;
    private File fin;
    private File fout;

    public ImporterWriter(String s) {
        this.fin = new File(s);
        currentOutputFile = 0;
        this.fout = new File(outputFileLocation);
    }

    private ImporterWriter(String s, int n) {
        this.fin = new File(s);
        currentOutputFile = n;
        this.fout = new File(currentOutputFile+outputFileLocation);
    }

    public File importDependencies() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fin));
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
                    ImporterWriter iw = new ImporterWriter(dependencyName, ++currentOutputFile);
                    File dependency = iw.importDependencies();

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

    private void copyContentsOfDependencyIntoOutputFile(BufferedReader bin, BufferedWriter bw) throws IOException {
        String transfer;
        while ((transfer = bin.readLine()) != null) {
            bw.write(transfer);
            bw.newLine();
        }
    }
}
