package org.cdtpinto.readabilitychecker.formulas.logic;

import edu.cs.umu.sres.ParserInterface;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.List;
import org.openide.util.Exceptions;
import org.cdtpinto.readabilitychecker.codeabstractionlevels.SourceCodeFile;
import org.cdtpinto.readabilitychecker.formulas.objects.SRES;
import org.cdtpinto.readabilitychecker.logic.Utilities;

/**
 * Contains the logical methods for the SRES formula.
 *
 * @author <a href="mailto:1120301@isep.ipp.pt">Cláudio Pinto</a>
 */
public class SresLogic {

    private static final double AWL_COEF = -0.1;

    /**
     * Resets SRES variables values, by setting all of them to 0.
     */
    public static void resetSresVars() {
        edu.cs.umu.sres.SRES.setWordsCount(0);
        edu.cs.umu.sres.SRES.setWordsLength(0);
        edu.cs.umu.sres.SRES.setSentencesCount(0);
    }

    /**
     * Creates SRES object.
     *
     * @param wordsCount the total number of words in file.
     * @param wordsLength the total length of the words in file.
     * @param sentencesCount the total number of sentences in file.
     * @return a SRES object.
     */
    public static SRES createSresObject(double wordsCount, double wordsLength, double sentencesCount) {
        SresLogic sl = new SresLogic();
        SRES sres = new SRES();

        if (wordsCount > 0.0 && wordsLength > 0.0 && sentencesCount > 0.0) {
            sres.setAsl(wordsCount / sentencesCount);
            sres.setAwl(wordsLength / wordsCount);
        } else {
            sres.setAsl(0.0);
            sres.setAwl(0.0);
        }

        sres.setValue(sl.calculateSres(sres.getAsl(), sres.getAwl()));

        return sres;
    }

    /**
     * Analyzes a given source code file.
     *
     * @param scf represents a Java file.
     */
    public void analyzeFile(SourceCodeFile scf) {
        resetSresVars();

        try {
            // Disable PrintStream so countMetrics(...) won't flood the console with prints
            PrintStream originalStream = System.out;
            Utilities.disablePrintStream();

            ParserInterface.countMetrics(scf.getFile().getPath()); // Let POGJE handle the parsing of the variables for this formula

            // Re-enable PrintStream with the original stream
            Utilities.enablePrintStram(originalStream);

            double wordsCount = (double) edu.cs.umu.sres.SRES.getWordsCount();
            double wordsLength = (double) edu.cs.umu.sres.SRES.getWordsLength();
            double sentencesCount = (double) edu.cs.umu.sres.SRES.getSentencesCount();

            SRES sres = createSresObject(wordsCount, wordsLength, sentencesCount);
            scf.setSres(sres);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * Applies the SRES formula.
     *
     * @param asl the average sentence length.
     * @param awl the average word length.
     * @return the SRES readability value.
     */
    public double calculateSres(double asl, double awl) {
        if (asl == 0 || awl == 0) {
            return 0;
        } else {
            return asl + (AWL_COEF * awl);
        }
    }

    /**
     * Gets the SRES readability value of a project.
     *
     * @param sourceCodeFiles represents every Java file in a project.
     * @return the SRES readability value for the project.
     */
    public double getReadabilityOfProject(List<SourceCodeFile> sourceCodeFiles) throws NullPointerException {
        double sumReadabilityValues = 0;
        int ignoredFiles = 0;   // files with ASL = 0 or AWL = 0 are ignored

        try {
            /* Sum the readability values of each file and ignore files whose readability value was not calculated */
            for (SourceCodeFile file : sourceCodeFiles) {
                if (file.getSres() == null || file.getSres().getValue() == 0 || Double.isInfinite(file.getSres().getValue())) {
                    ignoredFiles++;
                } else {
                    sumReadabilityValues += file.getSres().getValue();
                }
            }
        } catch (NullPointerException ex) {
            System.out.println(ex);
            return 0;
        }

        if (sumReadabilityValues == 0) {
            return 0;
        } else {
            return sumReadabilityValues / (double) (sourceCodeFiles.size() - ignoredFiles);
        }
    }

    /**
     * Gets the detailed results for the SRES readability formula.
     *
     * @param javaFiles the Java files of the project tested by SRES.
     * @return a String with the detailed information for every method tested by
     * SRES.
     */
    public static String getDetailedResults(List<SourceCodeFile> javaFiles, String project, double projectReadability) {
        StringBuilder detailedResults = new StringBuilder();

        detailedResults.append("Project: ");
        detailedResults.append(project);
        detailedResults.append(System.lineSeparator());
        detailedResults.append("Readability value: ");
        detailedResults.append(String.valueOf(String.valueOf(new DecimalFormat("#0.00").format(projectReadability))));
        detailedResults.append(System.lineSeparator());
        detailedResults.append(System.lineSeparator());

        try {
            for (SourceCodeFile file : javaFiles) {
                if (file.getFile() != null) {
                    detailedResults.append("File: ");
                    detailedResults.append(file.getFile().getName());
                    detailedResults.append(System.lineSeparator());
                    detailedResults.append("ASL: ");
                    detailedResults.append(String.valueOf(new DecimalFormat("#0.00").format(file.getSres().getAsl())));
                    detailedResults.append(System.lineSeparator());
                    detailedResults.append("AWL: ");
                    detailedResults.append(String.valueOf(new DecimalFormat("#0.00").format(file.getSres().getAwl())));
                    detailedResults.append(System.lineSeparator());

                    if (file.getSres().getAsl() == 0.0 && file.getSres().getAwl() == 0.0) {
                        detailedResults.append("Readability value not calculated. Make sure this file doesn't have any feature introduced after Java SE 5.");
                    } else if (file.getSres().getAsl() == 0.0) {
                        detailedResults.append("Readability value not calculated. File has no Java sentences.");
                    } else if (file.getSres().getAwl() == 0.0) {
                        detailedResults.append("Readability value not calculated. File has no Java words.");
                    } else {
                        detailedResults.append("Readability value: ");
                        detailedResults.append(String.valueOf(new DecimalFormat("#0.00").format(file.getSres().getValue())));
                    }
                    detailedResults.append(System.lineSeparator());
                    detailedResults.append(System.lineSeparator());
                }
            }
        } catch (NullPointerException ex) {
            System.out.println(ex);
        }

        return detailedResults.toString();
    }

}
