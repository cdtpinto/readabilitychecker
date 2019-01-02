package org.cdtpinto.readabilitychecker.formulas.objects;

/**
 * Buse B&amp;W Weimer (B&amp;W) metric.
 *
 * @author <a href="mailto:1120301@isep.ipp.pt">Cláudio Pinto</a>
 */
public class BW {

    private double value;                               // Readability value

    public BW() {

    }

    public BW(double value) {
        setValue(value);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

}
