package org.cdtpinto.readabilitychecker.formulas.logic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import org.cdtpinto.readabilitychecker.codeabstractionlevels.SourceCodeFile;
import org.cdtpinto.readabilitychecker.formulas.objects.BW;

/**
 * Contains the logical methods for the B&amp;W metric.
 *
 * @author <a href="mailto:1120301@isep.ipp.pt">Cláudio Pinto</a>
 */
public class BwLogic {

    private static final int SNIPPET_SIZE = 8;    // the file will be split in blocks of code with this number of lines

    /**
     * Analyzes a given source code file.
     *
     * @param scf represents a Java file.
     * @throws IOException in case there was a problem reading the file.
     */
    public void analyzeFile(SourceCodeFile scf) throws IOException {
        StringBuilder codeBlock = new StringBuilder();
        int currentBlockLineCount = 0;
        int totalBlockCount = 0;
        double valueSum = (double) 0;

        try (BufferedReader br = new BufferedReader(new FileReader(scf.getFile().getAbsoluteFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                currentBlockLineCount++;
                if (currentBlockLineCount <= SNIPPET_SIZE) {
                    codeBlock.append(line).append('\n');
                } else {
                    valueSum += raykernel.apps.readability.eval.Main.getReadability(codeBlock.toString());

                    totalBlockCount++;
                    currentBlockLineCount = 1;
                    codeBlock.setLength(0);
                    codeBlock.append(line).append('\n');
                }
            }

            // Calculate the final part of the code
            if (codeBlock.length() != 0) {
                valueSum += raykernel.apps.readability.eval.Main.getReadability(codeBlock.toString());

                totalBlockCount++;
            }
        }

        scf.setBw(new BW(valueSum / totalBlockCount));
    }

    /**
     * Gets the B&amp;W readability value of a project.
     *
     * @param sourceCodeFiles represents every Java file in a project.
     * @return the B&amp;W readability value for the project.
     */
    public double getReadabilityOfProject(List<SourceCodeFile> sourceCodeFiles) {
        double sumReadabilityValues = 0;
        int ignoredFiles = 0;

        /* Sum the readability values of each file and ignore files whose readability value was not calculated */
        for (SourceCodeFile file : sourceCodeFiles) {
            if (file.getBw() == null || file.getBw().getValue() == 0) {
                ignoredFiles++;
            } else {
                sumReadabilityValues += file.getBw().getValue();
            }
        }

        if (sumReadabilityValues == 0) {
            return 0;
        } else {
            return sumReadabilityValues / (double) (sourceCodeFiles.size() - ignoredFiles);
        }
    }

    /**
     * Gets the detailed results for the B&amp;W readability metric.
     *
     * @param javaFiles the Java files of the project tested by B&amp;W.
     * @param project the name of the project.
     * @param projectReadability the readability value of the project.
     * @return a String with the detailed information for every method tested by
     * B&amp;W.
     */
    public static String getDetailedResults(List<SourceCodeFile> javaFiles, String project, double projectReadability) {
        StringBuilder detailedResults = new StringBuilder();

        detailedResults.append("<b>");
        detailedResults.append("Project: ");
        detailedResults.append(project);
        detailedResults.append("<br />");
        detailedResults.append("Readability value: ");
        detailedResults.append(String.valueOf(String.valueOf(new DecimalFormat("#0.00").format(projectReadability))));
        detailedResults.append("</b>");
        detailedResults.append("<br />");
        detailedResults.append("<br />");

        for (SourceCodeFile file : javaFiles) {
            if (file.getFile() != null) {
                detailedResults.append("File: ");
                detailedResults.append(file.getFile().getName());
                detailedResults.append("<br />");

                detailedResults.append("Readability value: ");
                detailedResults.append(String.valueOf(new DecimalFormat("#0.00").format(file.getBw().getValue())));
                detailedResults.append("<br />");
                detailedResults.append("<br />");
            }
        }

        detailedResults.setLength(detailedResults.length() - 12); // to remove the three last "<br />" added in the loop

        return detailedResults.toString();
    }

    /**
     * Gets the detailed results for the B&amp;W readability metric in the
     * correct format to be exported as a text file.
     *
     * @param javaFiles the Java files of the project tested by B&amp;W.
     * @param project the name of the project.
     * @param projectReadability the readability value of the project.
     * @return a String with the detailed information for every method tested by
     * B&amp;W.
     */
    public static String getDetailedResultsForTxtExport(List<SourceCodeFile> javaFiles, String project, double projectReadability) {
        StringBuilder detailedResults = new StringBuilder();

        detailedResults.append("Project: ");
        detailedResults.append(project);
        detailedResults.append(System.lineSeparator());
        detailedResults.append("Readability value: ");
        detailedResults.append(String.valueOf(String.valueOf(new DecimalFormat("#0.00").format(projectReadability))));
        detailedResults.append(System.lineSeparator());
        detailedResults.append(System.lineSeparator());

        for (SourceCodeFile file : javaFiles) {
            if (file.getFile() != null) {
                detailedResults.append("File: ");
                detailedResults.append(file.getFile().getName());
                detailedResults.append(System.lineSeparator());

                detailedResults.append("Readability value: ");
                detailedResults.append(String.valueOf(new DecimalFormat("#0.00").format(file.getBw().getValue())));
                detailedResults.append(System.lineSeparator());
                detailedResults.append(System.lineSeparator());
            }
        }

        detailedResults.setLength(detailedResults.length() - 3); // to remove the three last newline chars added in the loop

        return detailedResults.toString();
    }

}
