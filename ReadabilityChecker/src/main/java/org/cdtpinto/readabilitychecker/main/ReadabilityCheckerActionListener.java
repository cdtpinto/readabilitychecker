package org.cdtpinto.readabilitychecker.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "File",
        id = "org.cdtpinto.readabilitychecker.ReadabilityCheckerActionListener"
)
@ActionRegistration(
        iconBase = "org/cdtpinto/readabilitychecker/readabilitycheckericon.png",
        displayName = "#CTL_ReadabilityCheckerActionListener"
)
@ActionReference(path = "Toolbars/File", position = 0)
@Messages("CTL_ReadabilityCheckerActionListener=Readability Checker")
public final class ReadabilityCheckerActionListener implements ActionListener {

    private ReadabilityFrame readabilityFrame;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (readabilityFrame != null) {
            readabilityFrame.dispose();
        }
        readabilityFrame = new ReadabilityFrame();
    }
}
