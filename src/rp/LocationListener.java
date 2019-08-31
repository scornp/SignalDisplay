package rp;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: scornp
 * Date: 28-Aug-2006
 * Time: 06:52:56
 * The signalDisplay and analysis package as part
 * of the MDAQ package
 */
public class LocationListener extends JFrame implements ActionListener {
    //   SignalDisplaySettings signalDisplaySettings;
    JTextField text;
    JRNPSelector rnpSelector;
    String [] extensions;
    boolean directoriesOnly;
    String startDir;

    LocationListener(JTextField text, JRNPSelector rnpSelector, String startDir, boolean directoriesOnly, String [] extensions) {
        this.text = text;
        this.rnpSelector = rnpSelector;
        this.extensions = extensions;
        this.directoriesOnly = directoriesOnly;
        this.startDir = startDir;
    }

    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();

        if (extensions != null) {
            MyFileFilter myFileFilter = new MyFileFilter(extensions);
            fileChooser = new JFileChooser();
            fileChooser.setFileFilter(myFileFilter);
        } else {
            fileChooser = new JFileChooser();
        }

        if (directoriesOnly) fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (startDir != null) {
            fileChooser.setCurrentDirectory(new File(System.getenv("MDAQDataRoot")));
            fileChooser.setCurrentDirectory(new File(startDir));
        } else {
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        }
        fileChooser.setVisible(true);
        fileChooser.showOpenDialog(this);
        String filterFile = fileChooser.getSelectedFile().toString();
        //    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //    fileChooser.setCurrentDirectory(new File(System.getenv("MDAQDataRoot")));
        fileChooser.setVisible(true);
        fileChooser.showOpenDialog(this);

        String file = fileChooser.getSelectedFile().getAbsolutePath();
        text.setText(file);
        rnpSelector.setTextContents(file);
    }
}

