package org.cdtpinto.readabilitychecker.logic;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Contains auxiliary methods for the application.
 *
 * @author <a href="mailto:1120301@isep.ipp.pt">Cláudio Pinto</a>
 */
public class Utilities {

    /**
     * Disables the current print stream.
     */
    public static void disablePrintStream() {
        PrintStream dummyStream = new PrintStream(new OutputStream() {
            public void write(int b) {
                // NO-OP
            }
        });
        System.setOut(dummyStream);
    }

    /**
     * Re-enables the output stream with <code>originalStream</code> value.
     *
     * @param originalStream the stream that was defined before the call to the
     * <code>disablePrintStream()</code> method.
     */
    public static void enablePrintStram(PrintStream originalStream) {
        System.setOut(originalStream);
    }

}
