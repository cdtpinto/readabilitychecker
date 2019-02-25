package org.cdtpinto.readabilitychecker.formulas.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.SystemUtils;

/**
 * Contains the logical methods for RSM model.
 *
 * @author <a href="mailto:1120301@isep.ipp.pt">Cláudio Pinto</a>
 */
public class RsmLogic {

    public static String runRsmCommand(String filePath) throws IOException {
        String output = "";

        if (SystemUtils.IS_OS_WINDOWS) {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd lib && java -jar " + filePath);
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) {
                    break;
                }

                //System.out.println(line);
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
}
