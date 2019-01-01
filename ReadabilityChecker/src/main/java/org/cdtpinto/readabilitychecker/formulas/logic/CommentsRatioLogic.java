package org.cdtpinto.readabilitychecker.formulas.logic;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.comments.Comment;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.cdtpinto.readabilitychecker.codeabstractionlevels.Method;
import org.cdtpinto.readabilitychecker.codeabstractionlevels.SourceCodeFile;
import org.cdtpinto.readabilitychecker.formulas.objects.CommentsRatio;

/**
 * Contains the logical methods for the Comments Ratio formula.
 *
 * @author <a href="mailto:1120301@isep.ipp.pt">Cláudio Pinto</a>
 */
public class CommentsRatioLogic {

    /**
     * Gets the number of lines of code in a file.
     *
     * @param cu represents a Java file parsed by JavaParser.
     * @return an integer representing the number of lines of code in
     * <code>cu</code>.
     */
    public int getLinesOfCodeFromFile(CompilationUnit cu) {
        return cu.getEnd().get().line;
    }

    /**
     * Gets the number of lines of code in a method.
     *
     * @param m represents a method in a Java file.
     * @return an integer representing the number of lines of code in
     * <code>m</code>.
     */
    public int getLinesOfCodeFromMethod(Method m) {
        Optional<Comment> associatedComment = m.getMethodDeclaration().getComment();
        int loc = 0;

        /* Handle the comment associated to the method (typpically a Javadoc comment) */
        if (associatedComment.isPresent()) {
            loc += associatedComment.get().getEnd().get().line - associatedComment.get().getBegin().get().line + 1;
        }

        /* Handle number of lines of the method body */
        loc += m.getMethodDeclaration().getEnd().get().line - m.getMethodDeclaration().getBegin().get().line + 1;

        return loc;
    }

    /**
     * Gets the number of lines with comments in a file.
     *
     * @param cu represents a Java file parsed by JavaParser.
     * @return an integer representing the number of lines with comments in
     * <code>cu</code>.
     */
    public int getLinesWithCommentsFromFile(CompilationUnit cu) {
        List<Comment> comments = cu.getAllContainedComments();
        int lom = 0;    // lines with comments

        for (Comment c : comments) {
            lom += c.getRange().get().end.line - c.getRange().get().begin.line + 1;
        }

        return lom;
    }

    /**
     * Gets the number of lines with comments in a method.
     *
     * @param m represents a method in a Java file.
     * @return an integer representing the number of lines with comments in
     * <code>m</code>.
     */
    public int getLinesWithCommentsFromMethod(Method m) {
        Optional<Comment> associatedComment = m.getMethodDeclaration().getComment();
        List<Comment> comments = m.getMethodDeclaration().getAllContainedComments();
        int lomc = 0;   // lines with comments

        /* Handle the comment associated to the method (typpically a Javadoc comment) */
        if (associatedComment.isPresent()) {
            lomc += associatedComment.get().getEnd().get().line - associatedComment.get().getBegin().get().line + 1;
        }

        /* Handle comments inside method body */
        for (Comment c : comments) {
            lomc += c.getRange().get().end.line - c.getRange().get().begin.line + 1;
        }

        return lomc;
    }

    /**
     * Analyzes a file and and gets all the values needed to create the
     * respective <code>CommentsRatio</code> object.
     *
     * @param cu represents a Java file.
     * @return a <code>CommentsRatio</code> object with the Comments Ratio
     * values <code>cu</code>.
     */
    public CommentsRatio analyzeFile(CompilationUnit cu) {
        CommentsRatioLogic crl = new CommentsRatioLogic();
        CommentsRatio cr = new CommentsRatio();

        if (cu != null) {
            cr.setLinesOfCode(crl.getLinesOfCodeFromFile(cu));
            cr.setLinesWithComments(crl.getLinesWithCommentsFromFile(cu));
            cr.setValue(crl.calculateCommentsRatio(cr.getLinesOfCode(), cr.getLinesWithComments()));
        }

        return cr;
    }

    /**
     * Analyzes all the methods and gets all the values needed to create the
     * respective <code>CommentsRatio</code> object.
     *
     * @param methods list of methods to be analyzed.
     */
    public void analyzeMethods(ArrayList<Method> methods) {
        CommentsRatioLogic crl = new CommentsRatioLogic();

        if (methods != null) {
            for (Method m : methods) {
                CommentsRatio cr = new CommentsRatio();

                cr.setLinesOfCode(crl.getLinesOfCodeFromMethod(m));
                cr.setLinesWithComments(crl.getLinesWithCommentsFromMethod(m));
                cr.setValue(crl.calculateCommentsRatio(cr.getLinesOfCode(), cr.getLinesWithComments()));

                m.setCommentsRatio(cr);
            }
        }
    }

    /**
     * Applies the Comments Ratio formula.
     *
     * @param loc number of lines of code.
     * @param lom number of lines with comments.
     * @return the Comments Ratio readability value.
     */
    public double calculateCommentsRatio(int loc, int lom) {
        if (loc == 0 || lom == 0 || lom > loc) {
            return 0;
        } else {
            return (double) loc / (double) lom;
        }
    }

    /**
     * Gets the Comments Ratio readability value of a project.
     *
     * @param sourceCodeFiles represents every Java file in a project.
     * @return the Comments Ratio readability value for the project.
     */
    public double getReadabilityOfProject(List<SourceCodeFile> sourceCodeFiles) throws NullPointerException {
        double sumReadabilityValues = 0;
        int filesIgnored = 0;

        try {
            /* Sum the readability values of each file and ignore files whose readability value was not calculated */
            for (SourceCodeFile file : sourceCodeFiles) {
                if (file.getCommentsRatio() == null || file.getCommentsRatio().getValue() == 0 || Double.isInfinite(file.getCommentsRatio().getValue())) {
                    filesIgnored++;
                } else {
                    sumReadabilityValues += file.getCommentsRatio().getValue();
                }
            }
        } catch (NullPointerException ex) {
            System.out.println(ex);
            return 0;
        }

        if (sumReadabilityValues == 0) {
            return 0;
        } else {
            return sumReadabilityValues / ((double) sourceCodeFiles.size() - filesIgnored);
        }
    }

    /**
     * Gets the detailed results for the Comments Ratio readability formula.
     *
     * @param javaFiles the Java files of the project tested by Comments Ratio.
     * @return a String with the detailed information for every method tested by
     * Comments Ratio.
     */
    public static String getDetailedResults(List<SourceCodeFile> javaFiles, String project, double projectReadability) throws NullPointerException {
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

        try {
            for (SourceCodeFile file : javaFiles) {
                if (file.getFile() != null) {
                    /* Handle files */
                    detailedResults.append("File: ");
                    detailedResults.append(file.getFile().getName());
                    detailedResults.append("<br />");
                    detailedResults.append("LOC: ");
                    detailedResults.append(file.getCommentsRatio().getLinesOfCode());
                    detailedResults.append("<br />");
                    detailedResults.append("LOM: ");
                    detailedResults.append(file.getCommentsRatio().getLinesWithComments());
                    detailedResults.append("<br />");

                    if (file.getCommentsRatio().getLinesOfCode() == 0) {
                        detailedResults.append("Readability value not calculated. File is empty.");
                    } else if (file.getCommentsRatio().getLinesWithComments() == 0) {
                        detailedResults.append("Readability value not calculated. File has no comments.");
                    } else {
                        detailedResults.append("Readability value: ");
                        detailedResults.append(String.valueOf(new DecimalFormat("#0.00").format(file.getCommentsRatio().getValue())));
                    }
                    detailedResults.append("<br />");
                    detailedResults.append("<br />");
                }

                /* Handle the methods in file */
                for (Method m : file.getMethods()) {
                    detailedResults.append("\t");
                    detailedResults.append(m.getMethodDeclaration().getDeclarationAsString(true, false, true));
                    detailedResults.append("<br />");
                    if (m.getCommentsRatio() != null) {
                        detailedResults.append("\tLOC: ");
                        detailedResults.append(String.valueOf(m.getCommentsRatio().getLinesOfCode()));
                        detailedResults.append("<br />");
                        detailedResults.append("\tLOM: ");
                        detailedResults.append(String.valueOf(m.getCommentsRatio().getLinesWithComments()));
                        detailedResults.append("<br />");

                        if (m.getCommentsRatio().getLinesWithComments() == 0) {
                            detailedResults.append("\tReadability value not calculated. Method has no comments.");
                        } else {
                            detailedResults.append("\tReadability value: ");
                            detailedResults.append(String.valueOf(new DecimalFormat("#0.00").format(m.getCommentsRatio().getValue())));
                        }

                    } else {
                        detailedResults.append("\tCannot apply Comments Ratio formula to this method.");
                    }
                    detailedResults.append("<br />");
                    detailedResults.append("<br />");
                }
            }
        } catch (NullPointerException ex) {
            System.out.println(ex);
        }

        detailedResults.setLength(detailedResults.length() - 12); // to remove the three last "<br />" added in the loop

        return detailedResults.toString();
    }

    /**
     * Gets the detailed results for the Comments Ratio readability formula in
     * the correct format to be exported as a text file.
     *
     * @param javaFiles the Java files of the project tested by Comments Ratio.
     * @return a String with the detailed information for every method tested by
     * Comments Ratio.
     */
    public static String getDetailedResultsForTxtExport(List<SourceCodeFile> javaFiles, String project, double projectReadability) throws NullPointerException {
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
                    /* Handle files */
                    detailedResults.append("File: ");
                    detailedResults.append(file.getFile().getName());
                    detailedResults.append(System.lineSeparator());
                    detailedResults.append("LOC: ");
                    detailedResults.append(file.getCommentsRatio().getLinesOfCode());
                    detailedResults.append(System.lineSeparator());
                    detailedResults.append("LOM: ");
                    detailedResults.append(file.getCommentsRatio().getLinesWithComments());
                    detailedResults.append(System.lineSeparator());

                    if (file.getCommentsRatio().getLinesOfCode() == 0) {
                        detailedResults.append("Readability value not calculated. File is empty.");
                    } else if (file.getCommentsRatio().getLinesWithComments() == 0) {
                        detailedResults.append("Readability value not calculated. File has no comments.");
                    } else {
                        detailedResults.append("Readability value: ");
                        detailedResults.append(String.valueOf(new DecimalFormat("#0.00").format(file.getCommentsRatio().getValue())));
                    }
                    detailedResults.append(System.lineSeparator());
                    detailedResults.append(System.lineSeparator());
                }

                /* Handle the methods in file */
                for (Method m : file.getMethods()) {
                    detailedResults.append("\t");
                    detailedResults.append(m.getMethodDeclaration().getDeclarationAsString(true, false, true));
                    detailedResults.append(System.lineSeparator());
                    if (m.getCommentsRatio() != null) {
                        detailedResults.append("\tLOC: ");
                        detailedResults.append(String.valueOf(m.getCommentsRatio().getLinesOfCode()));
                        detailedResults.append(System.lineSeparator());
                        detailedResults.append("\tLOM: ");
                        detailedResults.append(String.valueOf(m.getCommentsRatio().getLinesWithComments()));
                        detailedResults.append(System.lineSeparator());

                        if (m.getCommentsRatio().getLinesWithComments() == 0) {
                            detailedResults.append("\tReadability value not calculated. Method has no comments.");
                        } else {
                            detailedResults.append("\tReadability value: ");
                            detailedResults.append(String.valueOf(new DecimalFormat("#0.00").format(m.getCommentsRatio().getValue())));
                        }

                    } else {
                        detailedResults.append("\tCannot apply Comments Ratio formula to this method.");
                    }
                    detailedResults.append(System.lineSeparator());
                    detailedResults.append(System.lineSeparator());
                }
            }
        } catch (NullPointerException ex) {
            System.out.println(ex);
        }

        detailedResults.setLength(detailedResults.length() - 3); // to remove the three last newline chars added in the loop

        return detailedResults.toString();
    }

}
