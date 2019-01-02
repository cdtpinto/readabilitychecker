package org.cdtpinto.readabilitychecker.formulas.objects;

/**
 * Comments Ratio formula.
 *
 * @author <a href="mailto:1120301@isep.ipp.pt">Cláudio Pinto</a>
 */
public class CommentsRatio {

    private int loc;    // Lines Of Code
    private int lom;    // Lines With Comments
    private double value;

    public CommentsRatio() {
        this(0, 0, 0.0);
    }

    public CommentsRatio(int loc, int lom, double value) {
        setLoc(loc);
        setLom(lom);
        setValue(value);
    }

    public int getLoc() {
        return loc;
    }

    public int getLom() {
        return lom;
    }

    public double getValue() {
        return value;
    }

    public void setLoc(int loc) {
        this.loc = loc;
    }

    public void setLom(int lom) {
        this.lom = lom;
    }

    public void setValue(double value) {
        this.value = value;
    }

}
