package org.myorg.readabilitychecker.formulas.objects;

/**
 * Buse & Weimer (B&W) model.
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
