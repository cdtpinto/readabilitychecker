package org.myorg.readabilitychecker.formulas.objects;

/**
 * PHD formula.
 *
 * @author <a href="mailto:1120301@isep.ipp.pt">Cláudio Pinto</a>
 */
public class PHD {

    private double volume;
    private int lines;                                  // Contains lines with comments
    private double entropy;
    private double value;                               // Readability value

    public PHD() {
        this(0.0, 0, 0.0, 0.0);
    }

    public PHD(double volume, int lines, double entropy, double value) {
        setVolume(volume);
        setLines(lines);
        setEntropy(entropy);
        setValue(value);
    }

    public double getVolume() {
        return volume;
    }

    public int getLines() {
        return lines;
    }

    public double getEntropy() {
        return entropy;
    }

    public double getValue() {
        return value;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }

    public void setEntropy(double entropy) {
        this.entropy = entropy;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
