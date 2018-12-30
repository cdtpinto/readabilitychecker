package org.cdtpinto.readabilitychecker.formulas.objects;

/**
 * SRES formula.
 *
 * @author <a href="mailto:1120301@isep.ipp.pt">Cláudio Pinto</a>
 */
public class SRES {

    private double asl;                                 // Average Sentence Length
    private double awl;                                 // Average Word Length
    private double value;                               // Readability value

    public SRES() {
        this(0, 0, 0);
    }

    public SRES(double asl, double awl, double value) {
        setAsl(asl);
        setAwl(awl);
        setValue(value);
    }

    public double getAsl() {
        return asl;
    }

    public double getAwl() {
        return awl;
    }

    public double getValue() {
        return value;
    }

    public void setAsl(double asl) {
        this.asl = asl;
    }

    public void setAwl(double awl) {
        this.awl = awl;
    }

    public void setValue(double value) {
        this.value = value;
    }

}
