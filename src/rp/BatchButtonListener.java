package rp;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: scornp
 * Date: 28-Aug-2006
 * Time: 05:14:10
 * The signalDisplay and analysis package as part
 * of the MDAQ package
 */
public class BatchButtonListener implements ActionListener {

    String filePath;
    String fileNames[];
    JList fileList;
    DisplayPanel displayPanel;
    int [] fileSize;
    String filetype;
    SignalDisplaySettings signalDisplaySettings;

    BatchButtonListener(SignalDisplaySettings signalDisplaySettings) {
        this.signalDisplaySettings = signalDisplaySettings;

    }


    public void actionPerformed(ActionEvent e) {

        fileList = signalDisplaySettings.getDataFileJList();


        // first get the indexes of the file list to be displayed
        int minIndex = fileList.getMinSelectionIndex();
        int maxIndex = fileList.getMaxSelectionIndex();
        System.out.println("BatchButtonListener " + filePath + " " + signalDisplaySettings.getCatalogueFilePath());
        System.out.println("BatchButtonListener min index = " + minIndex + " max index = " + maxIndex);

        if (minIndex != -1 && maxIndex != -1){
        new BatchFrame(signalDisplaySettings);
        }
}
}
