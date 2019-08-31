package rp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: scornp
 * Date: 11-Nov-2006
 * Time: 07:19:34
 * To change this template use File | Settings | File Templates.
 */
public class DataProcessedIndicator {
    String filePath;
    String outputFilePath;
    DisplayPanel displayPanel;
    int[] fileSize;
    String filetype;
    JFrame indicatorFrame;
    JPanel indicatorPanel;
    SignalDisplaySettings signalDisplaySettings;
    String fileNames[];
    JList fileList;

    DataProcessedIndicator(SignalDisplaySettings signalDisplaySettings) {

        this.signalDisplaySettings = signalDisplaySettings;


        filePath = signalDisplaySettings.getCatalogueFilePath();
        fileNames = signalDisplaySettings.getDataFileNames();
        fileList = signalDisplaySettings.getDataFileJList();
        displayPanel = signalDisplaySettings.getDisplayPanel();
        fileSize = signalDisplaySettings.getDataFileSizes();
        filetype = signalDisplaySettings.getFiletype();

        outputFilePath = signalDisplaySettings.getOutputDirSelector().getTextContents();

        String[] nameListFiles;
        nameListFiles = new String[fileNames.length];
        // first generate an input file given set quantities

        //     signalDisplaySettings.getTestPanel().getTextContents();
        // first get the indexes of the file list to be displayed

        int minIndex = fileList.getMinSelectionIndex();
        int maxIndex = fileList.getMaxSelectionIndex();
        if (signalDisplaySettings.getSelectAllCheckBox().isSelected()) {
            minIndex = 0;
            maxIndex = fileNames.length - 1;
        }
        int size = (int) Math.sqrt(maxIndex - minIndex + 1);

        size = size + 1;

        indicatorFrame = new JFrame("Indicator frame");
        //    indicatorFrame.setPreferredSize(new Dimension(100, 100));

        indicatorFrame.setLayout(new FlowLayout());
        indicatorFrame.setVisible(true);

        indicatorPanel = new JPanel();
        indicatorPanel.setPreferredSize(new Dimension(size * 10 + 30, size * 10 + 30));
        indicatorPanel.setLayout(new GridLayout(size, size));
    //    indicatorPanel.setLayout(new ScrollPaneLayout());
        indicatorPanel.setVisible(true);
        JButton[] processIndicator = new JButton[maxIndex - minIndex + 1];
        for (int i = minIndex; i <= maxIndex; i++) {
            processIndicator[i - minIndex] = new JButton();
            processIndicator[i - minIndex].setPreferredSize(new Dimension(10, 10));
            processIndicator[i - minIndex].setBackground(Color.WHITE);
            processIndicator[i - minIndex].addActionListener(new ProcessIndicatorListener(signalDisplaySettings, i));
            indicatorPanel.add(processIndicator[i - minIndex]);
        }


        indicatorFrame.add(indicatorPanel);
        indicatorFrame.pack();

        CompletedList completedList = new CompletedList(signalDisplaySettings, processIndicator);

        completedList.start();
    }

}
