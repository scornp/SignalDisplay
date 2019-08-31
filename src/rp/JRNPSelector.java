package rp;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: scornp
 * Date: 28-Aug-2006
 * Time: 06:48:13
 * The signalDisplay and analysis package as part
 * of the MDAQ package
 */
public class JRNPSelector extends JPanel {

    String textContents;
    JLabel titleLabel;
    JTextField textField;

    String[] extensions;
    boolean directoriesOnly;
    String startDir;

    JRNPSelector(String name, int size, int textSize, String startDir, boolean directoriesOnly, String[] extensions) {
        this.startDir = startDir;
        this.directoriesOnly = directoriesOnly;
        this.extensions = extensions;

        titleLabel = new JLabel(name);
        titleLabel.setPreferredSize(new Dimension(size, 20));

        textField = new JTextField();
        textField.setPreferredSize(new Dimension(textSize, 20));
        textField.setEditable(false);
        textField.setAutoscrolls(true);
        textField.setScrollOffset(0);

        JButton chooser = new JButton("..");
        chooser.setPreferredSize(new Dimension(15, 15));

        LocationListener locationListener
                = new LocationListener(textField, this, startDir, directoriesOnly, extensions);
        chooser.addActionListener(locationListener);
        add(titleLabel);
        add(textField);
        add(chooser);
    }


    public void setTextContents(String textContents) {
        this.textContents = textContents;
    }

    public String getTextContents() {

        if (textContents == null)
            JOptionPane.showMessageDialog(new JFrame(), "Location not set for  " + titleLabel.getText(),
                    "Invalid filename", JOptionPane.ERROR_MESSAGE);
        return textContents;
    }
}
