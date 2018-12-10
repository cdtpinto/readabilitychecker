package org.myorg.readabilitychecker.formulas.logic;

import com.github.javaparser.JavaToken;
import edu.cs.umu.sres.Halstead;
import edu.cs.umu.sres.output.JavaLexer;
import edu.cs.umu.sres.output.JavaTreeParser;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.myorg.readabilitychecker.codeabstractionlevels.Method;
import org.myorg.readabilitychecker.codeabstractionlevels.SourceCodeFile;
import org.myorg.readabilitychecker.formulas.objects.PHD;
import org.myorg.readabilitychecker.logic.Utilities;

/**
 * Contains the logical methods for the PHD formula.
 *
 * @author <a href="mailto:1120301@isep.ipp.pt">Cláudio Pinto</a>
 */
public class PhdLogic {

    private static final double Y_INTERCEPT = 8.87;
    private static final double VOLUME_COEF = -0.033;
    private static final double LINES_COEF = 0.40;
    private static final double ENTROPY_COEF = -1.5;

    /**
     * Resets Halstead variables values, by setting all of them to 0.
     */
    public static void resetHalsteadVars() {
        Halstead.setDistinctOperands(0);
        Halstead.setDistinctOperators(0);
        Halstead.setTotalOperands(0);
        Halstead.setTotalOperators(0);
        Halstead.setProgramLength(0);
        Halstead.setProgramVocabulary(0);
    }

    /**
     * Gets the number of lines in a method.
     *
     * @param m represents a method in a Java file.
     * @return an integer representing the number of lines in <code>m</code>.
     */
    public int getLinesFromMethod(Method m) {
        return m.getMethodDeclaration().getEnd().get().line - m.getMethodDeclaration().getBegin().get().line + 1;
    }

    /**
     * Calculates the metrics of the code using POGJE.
     *
     * This implementation is based in the <code>countMetrics</code> method in
     * POGJE JAR file. The method was tweaked, so instead of receiving a path to
     * a file, it receives the code and counts its metrics.
     *
     * @param code Java code snippet.
     * @throws Exception for ANTLRInputStream
     */
    public static void countMetrics(String code) throws Exception {
        InputStream stream = new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8));

        ANTLRInputStream input = new ANTLRInputStream(stream);

        JavaLexer lexer = new JavaLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        edu.cs.umu.sres.output.JavaParser parser = new edu.cs.umu.sres.output.JavaParser(tokens);

        edu.cs.umu.sres.output.JavaParser.javaSource_return r = parser.javaSource();

        CommonTree t = (CommonTree) r.getTree();

        CommonTreeNodeStream nodes = new CommonTreeNodeStream(t);

        nodes.setTokenStream(tokens);

        JavaTreeParser walker = new JavaTreeParser(nodes);

        walker.javaSource();
    }

    /**
     * Gets the volume of a method.
     *
     * @param m represents a method in a Java file.
     * @return a double representing the Halstead's volume value of
     * <code>m</code>.
     * @throws Exception in case there was a problem parsing the method in the
     * method <code>countMetrics(...)</code>.
     */
    public double getVolumeFromMethod(Method m) throws Exception {
        resetHalsteadVars();

        /* 
         * In "methodCode" it's added "class {" at the beggining of the String and "}" at the end
         * so "countMetrics()" is able to parse it. This method doesn't parse only the method 
         * declaration, but parses a class declaration, even without a name.
         * This is a workaround. Another solution should be thought.
         */
        String methodCode = "class {" + m.getMethodDeclaration().toString() + "}";

        // Disable PrintStream, so countMetrics(...) won't flood the console with prints
        PrintStream originalStream = System.out;
        Utilities.disablePrintStream();
        countMetrics(methodCode);

        // Re-enable PrintStream with the original stream
        Utilities.enablePrintStram(originalStream);

        /* 
         * Subtract by 2 total operators, because the class definition starts and ends with "{}".
         * This is considered an operator. "class" is also considered an operator. Both have to be discarded.
         * Subtract by 1 distinct operators (this removes "class" as an operator).
         * Subtract by 1 total and distinct operands (Pogje adds "<missing IDENT>" as an operand, 
         * because no class name is defined).
         */
        Halstead.setDistinctOperators(Halstead.getDistinctOperators() - 1);
        Halstead.setDistinctOperands(Halstead.getDistinctOperands() - 1);
        Halstead.setTotalOperators(Halstead.getTotalOperators() - 2);
        Halstead.setTotalOperands(Halstead.getTotalOperands() - 1);

        // Re-calculate program vocabulary and program length with the new values
        Halstead.setProgramLength(Halstead.getTotalOperators() + Halstead.getTotalOperands());              // N
        Halstead.setProgramVocabulary(Halstead.getDistinctOperators() + Halstead.getDistinctOperands());    // n

        //System.out.println("Operadores únicos: " + Halstead.getDistinctOperators());
        //System.out.println("Operandos únicos: " + Halstead.getDistinctOperands());
        //System.out.println("Operadores totais: " + Halstead.getTotalOperators());
        //System.out.println("Operandos totais: " + Halstead.getTotalOperands());
        // Apply Halstead's volume formula and return its value
        return (double) Halstead.getProgramLength() * Halstead.log2((double) Halstead.getProgramVocabulary());
    }

    /**
     * Gets all the tokens from a method.
     *
     * @param m represents a method in a Java file.
     * @return an ArrayList of type JavaToken with all the tokens in the
     * <code>m</code>.
     */
    public ArrayList<JavaToken> getTokensFromMethod(Method m) {
        ArrayList<JavaToken> tokens = new ArrayList<>();

        if (m != null && m.getMethodDeclaration() != null) {
            m.getMethodDeclaration().getTokenRange().get().forEach(t -> tokens.add(t));
            tokens.removeIf(obj -> obj.getText().matches("[\\n\\r\\t ]+"));               // ignore \n, \r, \t and spaces
        }

        return tokens;
    }

    /**
     * Gets all the total bytes in a method.
     *
     * All the characters, newlines, carriage returns, tabs and spaces are
     * considered as bytes.
     *
     * @param m represents a method in a Java file.
     * @return an ArrayList of type Character containing all the bytes in
     * <code>m</code>.
     */
    public ArrayList<Character> getBytesFromMethod(Method m) {
        ArrayList<Character> bytes = new ArrayList<>();

        if (m != null && m.getMethodDeclaration() != null) {
            for (char c : m.getMethodDeclaration().toString().toCharArray()) {
                bytes.add(c);
            }
        }

        return bytes;
    }

    /**
     * Applies Shannon's entropy formula to a list of Strings.
     *
     * This algorithm was taken from
     * https://whaticode.wordpress.com/2010/05/24/a-java-implementation-for-shannon-entropy/
     *
     * @param values in this case, represents the list of terms (either tokens
     * or bytes) that a method contains.
     * @return a Double representing the entropy value.
     */
    public Double calculateShannonEntropy(List<String> values) {
        Map<String, Integer> map = new HashMap<>();

        /* Count the occurrences of each value */
        for (String sequence : values) {
            if (!map.containsKey(sequence)) {
                map.put(sequence, 0);
            }
            map.put(sequence, map.get(sequence) + 1);
        }

        /* Calculate the entropy */
        Double result = 0.0;
        for (String sequence : map.keySet()) {
            Double frequency = (double) map.get(sequence) / values.size();
            result -= frequency * (Math.log(frequency) / Math.log(2));
        }

        return result;
    }

    /**
     * Gets the entropy of a method.
     *
     * @param m represents a method in a Java file.
     * @return a Double representing the entropy value of <code>m</code>.
     */
    public double getEntropyFromMethod(Method m) {
        List<String> terms = new ArrayList<>();
        List<JavaToken> tokens = getTokensFromMethod(m);
        List<Character> bytes = getBytesFromMethod(m);

        for (JavaToken t : tokens) {
            terms.add(t.getText());
        }

        for (Character c : bytes) {
            terms.add(c.toString());
        }

        return calculateShannonEntropy(terms);
    }

    /**
     * Analyzes all methods and gets PHD values.
     *
     * @param methods the list of methods to be analyzed.
     * @param max the maximum number of lines that a method can have to be
     * analyzed.
     * @param methodsAnalyzed the number of methods that could be analyzed using
     * PHD formula.
     * @return the number of methods analyzed by PHD updated.
     * @throws Exception in case there was a problem parsing the method in the
     * volume calculation.
     */
    public int analyzeMethods(ArrayList<Method> methods, int max, int methodsAnalyzed) throws Exception {
        if (methods != null) {
            for (Method m : methods) {
                PHD phd = new PHD();
                phd.setLines(getLinesFromMethod(m));                                                     // Lines
                if (phd.getLines() <= max) {
                    phd.setVolume(getVolumeFromMethod(m));                                               // Volume
                    phd.setEntropy(getEntropyFromMethod(m));                                             // Entropy
                    phd.setValue(calculatePhd(phd.getVolume(), phd.getLines(), phd.getEntropy()));       // Calculate readability

                    m.setPhd(phd);

                    methodsAnalyzed++;
                }
            }
        }

        return methodsAnalyzed;
    }

    /**
     * Calculates the regression variable.
     *
     * @param volume the Halstead's volume of the code.
     * @param lines the number of lines of the code.
     * @param entropy the Shannon's entropy value of the code.
     * @return the regression variable value.
     */
    public double calculateRegressionVariable(double volume, int lines, double entropy) {
        return Y_INTERCEPT + (VOLUME_COEF * volume) + (LINES_COEF * lines) + (ENTROPY_COEF * entropy);
    }

    /**
     * Applies the logistic function using the given regression variable.
     *
     * @param regressionVar the regression variable.
     * @return the logistic value.
     */
    public double applyLogisticFunction(double regressionVar) {
        return 1 / (1 + Math.exp(-regressionVar));
    }

    /**
     * Applies the PHD formula.
     *
     * @param volume the Halstead's volume of the code.
     * @param lines the number of lines of the code.
     * @param entropy the Shannon's entropy value of the code.
     * @return the PHD readability value.
     */
    public double calculatePhd(double volume, int lines, double entropy) {
        if (volume == 0 || lines == 0 || entropy == 0) {
            return 0;
        } else {
            double regressionVar = calculateRegressionVariable(volume, lines, entropy);

            return applyLogisticFunction(regressionVar);
        }
    }

    /**
     * Gets the detailed results for the PHD readability formula.
     *
     * @param currentlyOpenedFile is the Java file currently opened and tested
     * by PHD.
     * @return a String with the detailed information for every method tested by
     * PHD.
     */
    public static String getDetailedResults(SourceCodeFile currentlyOpenedFile) {
        StringBuilder detailedResults = new StringBuilder();

        detailedResults.append("File: " + currentlyOpenedFile.getFile().getName());
        detailedResults.append(System.lineSeparator());
        detailedResults.append(System.lineSeparator());

        for (Method m : currentlyOpenedFile.getMethods()) {
            if (m.getPhd() != null) {
                detailedResults.append("\t" + m.getMethodDeclaration().getDeclarationAsString(true, false, true));
                detailedResults.append(System.lineSeparator());

                if (!Double.isNaN(m.getPhd().getVolume()) && !Double.isInfinite(m.getPhd().getVolume()) && Double.isFinite(m.getPhd().getVolume())) {
                    detailedResults.append("\tVolume: " + String.valueOf(new DecimalFormat("#0.00").format(m.getPhd().getVolume())));
                    detailedResults.append(System.lineSeparator());
                    detailedResults.append("\tLines: " + m.getPhd().getLines());
                    detailedResults.append(System.lineSeparator());
                    detailedResults.append("\tEntropy: " + String.valueOf(new DecimalFormat("#0.00").format(m.getPhd().getEntropy())));
                    detailedResults.append(System.lineSeparator());
                    detailedResults.append("\tReadability value: " + String.valueOf(new DecimalFormat("#0.00").format(m.getPhd().getValue())));
                } else {                    // Pogje couldn't analyze the code due to an ANTLR parsing error.
                    detailedResults.append("\tError parsing the code! Make sure this method doesn't have any feature introduced after Java SE 5.");
                }

                detailedResults.append(System.lineSeparator());
                detailedResults.append(System.lineSeparator());
            }
        }

        return detailedResults.toString();
    }

}
