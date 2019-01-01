package org.cdtpinto.readabilitychecker.logic;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Insets;
import java.time.LocalDateTime;

/**
 * Contains some logical methods needed by <code>ReadadilityFrame.java</code>.
 *
 * @author <a href="mailto:1120301@isep.ipp.pt">Cláudio Pinto</a>
 */
public class ReadabilityFrameLogic {

    /**
     * Greyes out all the elements of a container.
     *
     * @param container all the swing elements of a specific readability formula
     * presented in the main application window.
     * @param disable checkbox value in the main application window.
     */
    public static void disableComponents(Container container, boolean disable) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            component.setEnabled(disable);
            if (component instanceof Container) {
                disableComponents((Container) component, disable);
            }
        }
    }

    /**
     * Generates the filename for the detailed results text file.
     *
     * @return a String with the name that the saved file will have.
     */
    public static String getResultsFileName() {
        StringBuilder filename = new StringBuilder();
        String openedProjectName = SourceCodeFileLogic.getOpenedProjectName().toLowerCase();

        filename.append("readabilitychecker_");
        filename.append(openedProjectName.replace(' ', '_'));
        filename.append(".txt");

        return filename.toString();
    }

}
