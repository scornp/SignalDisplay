package rp;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

/**
 * DemitriDataReader
 * Author: Roger Philp
 * Date: 30-Apr-2006
 * Time: 23:20:49
 */
public class LoadFilterListener implements ActionListener {
    double[] filter;
    double[] dataArray;
    boolean fireOffDataProcessor;
    Date[] timeDatesArray;
    MyPlotListener myPlotListener;
    FilterDisplayPanel filterPanel;

    LoadFilterListener(double[] dataArray, Date[] timeDatesArray,
                       MyPlotListener myPlotListener, FilterDisplayPanel filterPanel) {
        this.dataArray = dataArray;
        this.timeDatesArray = timeDatesArray;
        this.myPlotListener = myPlotListener;
        this.filterPanel = filterPanel;
        fireOffDataProcessor = true;
        System.out.println("LoadFilterListener   is here!!!!!");
    }

    LoadFilterListener() {
        fireOffDataProcessor = false;
    }

    public void actionPerformed(ActionEvent e) {
        // fire off file chooser
        String[] extensions = {"fir", "dat"};
        File filterFile = getAFileName(extensions);

        getTextBasedDataEntries(filterFile);

        filterPanel.setNewDataSeries(filter);

/*
        if (fireOffDataProcessor) {
            DataProcessor dataProcessor = new DataProcessor(dataArray, filter);

            dataProcessor.pleaseStart();
        }
*/

    }

    public double[] getFilter() {
        return filter;
    }

    public void getTextBasedDataEntries(File inFileName) {
        //   signalDisplaySettings.setDataSourceFileExtension(MyFileFilter.getExtension(inFileName));
        System.out.println("Loading a .dat file " + inFileName.toString());
        //     String tmp = getFileContents(inFileName.toString());
        //   System.out.println("Contains " + tmp);
        String fileContents = "";
        String tmp;
        int entryCount = 0;

        try {
            System.out.println("here 1");
            FileReader insrc = new FileReader(inFileName);
            System.out.println("here 11");
            BufferedReader chnl = new BufferedReader(insrc);
            System.out.println("here 12");
            while ((tmp = chnl.readLine()) != null) {
                if (tmp.trim().equals("")) System.out.println("Huston we have a problem ");
                entryCount++;
                //         System.out.println("entry count = " + entryCount);
            }
            insrc.close();
            chnl.close();
            System.out.println("entry count = " + entryCount);

            filter = new double[entryCount];

            insrc = new FileReader(inFileName);
            chnl = new BufferedReader(insrc);
            for (int i = 0; i < entryCount; i++) {
                filter[i] = new Float(chnl.readLine());
            }
            insrc.close();
            chnl.close();
        }
        catch (IOException ie) {
            JOptionPane.showMessageDialog(new JFrame(), "Unable to open file " + inFileName,
                    "Invalid filename", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("first element = " + filter[0] + " last element = " + filter[entryCount - 1]);

        //      signalDisplaySettings.setDataArray(localDataArray);

        //      AnalysisFrame analysisFrame = new AnalysisFrame(signalDisplaySettings, localDataArray);
        // fire off the display window
    }


    public File getAFileName(String[] extensions) {
        JFrame aNewFrame = new JFrame();
        MyFileFilter myFileFilter = new MyFileFilter(extensions);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(myFileFilter);
        //    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //    fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setCurrentDirectory(new File(System.getenv("MDAQDataRoot")));
        fileChooser.setVisible(true);   // , "Attach"
        fileChooser.showOpenDialog(aNewFrame);
        return fileChooser.getSelectedFile();
    }
}