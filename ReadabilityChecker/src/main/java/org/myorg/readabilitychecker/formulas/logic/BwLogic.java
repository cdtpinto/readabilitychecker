package org.myorg.readabilitychecker.formulas.logic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.myorg.readabilitychecker.codeabstractionlevels.SourceCodeFile;
import org.myorg.readabilitychecker.formulas.objects.BW;

/**
 * Contains the logical methods for the Buse & Weimer model.
 *
 * @author <a href="mailto:1120301@isep.ipp.pt">Cláudio Pinto</a>
 */
public class BwLogic {

    public void analyzeFile(SourceCodeFile scf) throws IOException {
        double value = (double) 0; // CALCULAR!!!

        int blockCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(scf.getFile().getAbsoluteFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                blockCount++;
                if (blockCount <= 8) {
                    System.out.println(line);
                } else {
                    System.out.println(line);
                    blockCount = 1;
                }
            }
        }

        BW bw = new BW(value);
        scf.setBw(bw);
    }
}
