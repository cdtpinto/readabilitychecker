package org.myorg.readabilitychecker.formulas.logic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import org.myorg.readabilitychecker.codeabstractionlevels.SourceCodeFile;
import org.myorg.readabilitychecker.formulas.objects.BW;

/**
 * Contains the logical methods for the Buse & Weimer model.
 *
 * @author <a href="mailto:1120301@isep.ipp.pt">Cláudio Pinto</a>
 */
public class BwLogic {

    public void analyzeFile(SourceCodeFile scf) throws IOException {
        StringBuilder codeBlock = new StringBuilder();
        int currentBlockLineCount = 0;
        int totalBlockCount = 0;
        double valueSum = (double) 0;

        try (BufferedReader br = new BufferedReader(new FileReader(scf.getFile().getAbsoluteFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                currentBlockLineCount++;
                if (currentBlockLineCount <= 8) {
                    codeBlock.append(line).append('\n');
                } else {
                    //System.out.println(codeBlock);
                    valueSum += raykernel.apps.readability.eval.Main.getReadability(codeBlock.toString());

                    totalBlockCount++;
                    currentBlockLineCount = 1;
                    codeBlock.setLength(0);
                }
            }

            // Calculate the final part of the code
            if (codeBlock.length() != 0) {
                valueSum += raykernel.apps.readability.eval.Main.getReadability(codeBlock.toString());
                totalBlockCount++;
            }
        }

        double value = valueSum / totalBlockCount;
        System.out.println("value -> " + value);

        BW bw = new BW(value);
        scf.setBw(bw);
    }

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
}
