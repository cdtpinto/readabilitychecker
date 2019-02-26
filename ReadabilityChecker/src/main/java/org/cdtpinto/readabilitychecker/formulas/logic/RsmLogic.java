package org.cdtpinto.readabilitychecker.formulas.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.SystemUtils;
import org.cdtpinto.readabilitychecker.codeabstractionlevels.SourceCodeFile;
import org.cdtpinto.readabilitychecker.formulas.objects.RSM;

/**
 * Contains the logical methods for RSM model.
 *
 * @author <a href="mailto:1120301@isep.ipp.pt">Cláudio Pinto</a>
 */
public class RsmLogic {

    public void analyzeFile(SourceCodeFile scf) throws IOException {
        System.out.println("-> "+scf.getFile().getAbsolutePath());
        
        String rsmOutput = runRsmCommand(scf.getFile().getAbsolutePath());

        System.out.println("output -> " + rsmOutput);
        Double readability = 0.0;

        if (!rsmOutput.equals("")) {
            readability = getReadabilityFromRsmOutput(rsmOutput);
        }

        System.out.println("legibilidade -> " + readability);

        scf.setRsm(new RSM(readability));
    }

    public static String runRsmCommand(String filePath) throws IOException {
        String output = "";

        if (SystemUtils.IS_OS_WINDOWS) {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd lib\\readability && java -jar rsm.jar " + filePath);
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) {
                    break;
                }

                System.out.println(line);
                if (line.contains("Class mean readability") || line.contains("Snippet readability")) {
                    output = line.toLowerCase();
                }
            }
        } else if (SystemUtils.IS_OS_LINUX) {
            System.out.println("implement linux logic");
        } else if (SystemUtils.IS_OS_MAC) {
            System.out.println("implement mac logic");
        }

        return output;
    }

    public static Double getReadabilityFromRsmOutput(String output) {
        Pattern pattern = Pattern.compile("\\d{1}\\.\\d{1,3}");
        Matcher matcher = pattern.matcher(output);

        if (matcher.find()) {
            return Double.parseDouble(matcher.group());
        }

        return 0.0;
    }

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
                detailedResults.append(String.valueOf(new DecimalFormat("#0.00").format(file.getRsm().getValue())));
                detailedResults.append("<br />");
                detailedResults.append("<br />");
            }
        }

        detailedResults.setLength(detailedResults.length() - 12); // to remove the three last "<br />" added in the loop

        return detailedResults.toString();
    }

    public double getReadabilityOfProject(List<SourceCodeFile> sourceCodeFiles) {
        double sumReadabilityValues = 0;
        int ignoredFiles = 0;

        /* Sum the readability values of each file and ignore files whose readability value was not calculated */
        for (SourceCodeFile file : sourceCodeFiles) {
            if (file.getRsm() == null || file.getRsm().getValue() == 0) {
                ignoredFiles++;
            } else {
                sumReadabilityValues += file.getRsm().getValue();
            }
        }

        if (sumReadabilityValues == 0) {
            return 0;
        } else {
            return sumReadabilityValues / (double) (sourceCodeFiles.size() - ignoredFiles);
        }
    }
}
