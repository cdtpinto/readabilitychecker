package org.cdtpinto.readabilitychecker.formulas.objects;

/**
 * RSM model.
 *
 * @author <a href="mailto:1120301@isep.ipp.pt">Cláudio Pinto</a>
 */
public class RSM {

    private double value;                               // Readability value    

    public RSM() {

    }

    public RSM(double value) {
        setValue(value);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
