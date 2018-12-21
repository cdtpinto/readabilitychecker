package org.myorg.readabilitychecker.formulas.logic;

import edu.cs.umu.sres.ParserInterface;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.myorg.readabilitychecker.codeabstractionlevels.SourceCodeFile;
import static org.myorg.readabilitychecker.formulas.logic.SresLogic.createSresObject;
import static org.myorg.readabilitychecker.formulas.logic.SresLogic.resetSresVars;
import org.myorg.readabilitychecker.formulas.objects.BW;
import org.myorg.readabilitychecker.formulas.objects.CommentsRatio;
import org.myorg.readabilitychecker.formulas.objects.SRES;
import org.myorg.readabilitychecker.logic.Utilities;
import org.openide.util.Exceptions;

/**
 * Contains the logical methods for the Comments Ratio formula.
 *
 * @author <a href="mailto:1120301@isep.ipp.pt">Cláudio Pinto</a>
 */
public class BwLogic {

    public void analyzeFile(SourceCodeFile scf) throws IOException {
        double value = (double) 0; // CALCULAR!!!
        try (Stream<String> stream = Files.lines(Paths.get(scf.getFile().getAbsolutePath()))) {
            stream.forEach(System.out::println);
        }

        BW bw = new BW(value);
        scf.setBw(bw);
    }
}
