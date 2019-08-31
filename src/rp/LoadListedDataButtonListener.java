package rp;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

/**
 * This class listens for an event from the file selector to load inthe apppropriate
 * binary or ascii data files. Current format binary format is little endian, pc format
 */
public class LoadListedDataButtonListener implements ActionListener {

    String filePath;
    String fileNames[];
    JList fileList;
    DisplayPanel displayPanel;
    int[] fileSize;
    String filetype;
    SignalDisplaySettings signalDisplaySettings;
    WorkInProgressBar w;

    LoadListedDataButtonListener(SignalDisplaySettings signalDisplaySettings) {
        this.signalDisplaySettings = signalDisplaySettings;
        System.out.println("Load listener");
        //  w = new WorkInProgressBar("fred");
        //  w.pleaseStart();
    }


    public void actionPerformed(ActionEvent e) {
        filePath = signalDisplaySettings.getCatalogueFilePath();
        fileNames = signalDisplaySettings.getDataFileNames();
        fileList = signalDisplaySettings.getDataFileJList();
        displayPanel = signalDisplaySettings.getDisplayPanel();
        fileSize = signalDisplaySettings.getDataFileSizes();
        filetype = signalDisplaySettings.getFiletype();
        System.out.println("Loadlistener   &&&&&&&&&&&&&&&&&&&& ");
        System.out.println("Loadlistener   &&&&&&&&&&&&&&&&&&&& ");
        System.out.println("Loadlistener   &&&&&&&&&&&&&&&&&&&& ");
        System.out.println("Loadlistener   &&&&&&&&&&&&&&&&&&&& ");
        // first get the indexes of the file list to be displayed
        int minIndex = fileList.getMinSelectionIndex();
        int maxIndex = fileList.getMaxSelectionIndex();
        System.out.println("LoadListedDataButtonListener " + filePath + " " + signalDisplaySettings.getCatalogueFilePath());
        System.out.println("LoadListedDataButtonListener min index = " + minIndex + " max index = " + maxIndex);
        float[] dataArray;
        // next
        // load data the following is temporary only
        int allDataSize = 0;

        //todo sort out progress bar

        ImageIcon myIcon = new ImageIcon("meteor.jpg");
        ProgressPanelNew screen = new ProgressPanelNew(myIcon);

        screen.setScreenVisible(true);
        screen.setProgressMax(maxIndex);
        screen.setProgressMin(minIndex);

        for (int i = minIndex; i <= maxIndex; i++) allDataSize += fileSize[i];
        dataArray = new float[allDataSize];

        // now get the data. Just binary for the moment
        if (filetype.equals("binary")) {
            int arrayIndex = 0;

            // scan all selected files
            System.out.println("Signal display starting to read dtata file");
            for (int i = minIndex; i <= maxIndex; i++) {
                try {

                    //open the individual files
                    RandomAccessFile inputBinaryDataFile = new RandomAccessFile(filePath + "\\" + fileNames[i], "r");
                    byte[] myTempByteArray = new byte[fileSize[i] * 4];

                    screen.setProgress("Loading data file " + fileNames[i] + " " + i + " of " + maxIndex,i);
                    // read the byte array
                    inputBinaryDataFile.read(myTempByteArray);

                    // convert the byte array to floats
                    int intbits;
                    int byteOffset = 0;
                    for (int ix = 0; ix < fileSize[i]; ix++) {
                        intbits = ((myTempByteArray[byteOffset + 0] & 0xff) << 0)
                                | ((myTempByteArray[byteOffset + 1] & 0xff) << 8)
                                | ((myTempByteArray[byteOffset + 2] & 0xff) << 16)
                                | ((myTempByteArray[byteOffset + 3] & 0xff) << 24);
                        byteOffset += 4;
                        dataArray[arrayIndex] = Float.intBitsToFloat(intbits);
                        if (ix == 0) System.out.println("The aquired value is " + dataArray[arrayIndex]);
                        arrayIndex++;
                    }

                    inputBinaryDataFile.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

               screen.dispose();

            System.out.println("Signal display Finished reading files");
        } else if (filetype.equals("ascii")) {
            //todo put in the ascii reader !!!!
            int arrayIndex = 0;
            for (int i = minIndex; i <= maxIndex; i++) {
                try {
                    FileReader insrc = new FileReader(filePath + "\\" + fileNames[i]);
                    BufferedReader chnl = new BufferedReader(insrc);
                    for (int j = 0; j < fileSize[i]; j++) {
                        dataArray[arrayIndex] = new Float(chnl.readLine());
                        arrayIndex++;
                    }
                    insrc.close();
                    chnl.close();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
            }
        }
        //      w.pleaseStop();

        // load data into the global storage area signalDisplaySettings
        signalDisplaySettings.setDataArray(dataArray);
        // allow the controls to be shown
        boolean showControls = true;
        // fire off the new data analysis window
 //       new AnalysisFrame(signalDisplaySettings, dataArray, showControls);


//*********** new stuff starts here
        // the are the file name of the starting blooc so the end block needs
        // to be adjusted to include its length
        System.out.println("Loadlistener loaded data  &&&&&&&&&&&&&&&&&&&& ");

        String startDateString = fileNames[minIndex];

        int numberOfSmaplePoints = dataArray.length;
        double sRate = new Double(signalDisplaySettings.getSampleRate());

        double deltaT = 1.0/(double)sRate;

        long startTimeInMilliSecs = TimeConverter.getCalendarXInMills(startDateString);
        long endTimeInMilliSec = (long)(startTimeInMilliSecs + (numberOfSmaplePoints - 1)*deltaT*1000.0);

        Date endTimeDate = TimeConverter.getDateFromMills(endTimeInMilliSec);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

        String endDateString = formatter.format(endTimeDate);

        System.out.println("Loadlistener: firing off TimeSeries windoow");
        System.out.println("Start date " + startDateString + "\n"
                         + "End date   " + endDateString);
        double[] dDataArray = new double[dataArray.length];
        for (int i = 0; i < dataArray.length; i++) dDataArray[i] = (double) dataArray[i];

//      create a time sequence array of milliseconds
        long timeInMilliSecs;
                Date[] allTimeDates = new Date[dataArray.length];
        for(int i = 0; i < dataArray.length; i++){
                   timeInMilliSecs =    (long)(startTimeInMilliSecs + i*deltaT*1000.0);
            allTimeDates[i] = new Date(timeInMilliSecs);
        }

        System.out.println("Data array length " + dDataArray.length + " Time series length " + allTimeDates.length);

/*        TimeSeriesDemo1InMilliSecond nxnx
                = new TimeSeriesDemo1InMilliSecond(startTimeInMilliSecs, endTimeInMilliSec, dDataArray);
        JFrame jjxx = new JFrame();
        jjxx.add(nxnx);
        jjxx.pack();
        jjxx.setVisible(true);*/

        new AnalysisFrameNew(signalDisplaySettings, dDataArray, allTimeDates);
    }

    /**
     * A routine to generate a string representation of the current date and time
     *
     * @return String
     */
    private String getDateString() {
        String dateStamp;
        Date currentDate;            // Used to get date to display
        SimpleDateFormat formatter;  // Formats the date displayed
        formatter = new SimpleDateFormat("EEE MMM dd hh:mm:ss yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        Calendar c = (Calendar) calendar.clone();
        currentDate = c.getTime();
        //       currentDate = new Date();
        formatter.applyPattern("yyyy MM dd HH mm ss");

        dateStamp = formatter.format(currentDate).toString();
        return dateStamp;
    }

}


