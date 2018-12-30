package org.cdtpinto.readabilitychecker.formulas.objects;

/**
 * Comments Ratio formula.
 *
 * @author <a href="mailto:1120301@isep.ipp.pt">Cláudio Pinto</a>
 */
public class CommentsRatio {

    private int linesOfCode;
    private int linesWithComments;
    private double value;

    public CommentsRatio() {
        this(0, 0, 0.0);
    }

    public CommentsRatio(int linesOfCode, int linesWithComments, double value) {
        setLinesOfCode(linesOfCode);
        setLinesWithComments(linesWithComments);
        setValue(value);
    }

    public int getLinesOfCode() {
        return linesOfCode;
    }

    public int getLinesWithComments() {
        return linesWithComments;
    }

    public double getValue() {
        return value;
    }

    public void setLinesOfCode(int linesOfCode) {
        this.linesOfCode = linesOfCode;
    }

    public void setLinesWithComments(int linesWithComments) {
        this.linesWithComments = linesWithComments;
    }

    public void setValue(double value) {
        this.value = value;
    }

}
