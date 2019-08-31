package rp;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: R Philp
 * Date: 01-May-2006
 * Time: 22:24:59
 * A component of the MDAQ project to isolate data chunks
 * that contain meteor data.
 * This class sets up a progress window and then launches the appropriate
 * process either standalone or condor
 */
public class StartFilteringListener implements ActionListener {
    SignalDisplaySettings signalDisplaySettings;

    StartFilteringListener(SignalDisplaySettings signalDisplaySettings) {
        this.signalDisplaySettings = signalDisplaySettings;

    }

    public void actionPerformed(ActionEvent e) {
        String outputFilePath = signalDisplaySettings.getOutputDirSelector().getTextContents();

        System.out.println("Data will be output to " + signalDisplaySettings.getDataOutputRootDirectory());
        System.out.println("Source files are of type: " + signalDisplaySettings.getDatatype());

        DataProcessedIndicator dataProcessedIndicator = new DataProcessedIndicator(signalDisplaySettings);
        GoodWindowsExec goodWindowsExec;
                    if (signalDisplaySettings.getCondor().isSelected()) {
                        System.out.println("Firing off condor ");
                   //     goodWindowsExec = new GoodWindowsExec( "cd D:\\MDAQDataRoot\\20061109152500\\out ");
                        //+
                          //      "condor_submit " + outputFilePath + "\\condor.sub");
                   //     goodWindowsExec = new GoodWindowsExec("condor_submit " + outputFilePath + "\\condor.sub");
                        goodWindowsExec = new GoodWindowsExec(outputFilePath + "\\condor.bat");
                    } else{
                         goodWindowsExec = new GoodWindowsExec(outputFilePath + "\\batch.bat");

                    }

        goodWindowsExec.start();

        String outDir = signalDisplaySettings.getOutputDirSelector().getTextContents();
        String catalogFileName = outDir + "\\" + "Catalogue.cat";

        XMLDataWriter xmlDataWriter = new XMLDataWriter(catalogFileName);

         // program description
        xmlDataWriter.startClause("Description");
        xmlDataWriter.setElement("Program", "MDAQ");
        xmlDataWriter.setElement("Version", "1.0");
        xmlDataWriter.setElement("Comments", "comments");
        xmlDataWriter.setElement("Abstract", "some description");
        xmlDataWriter.setElement("Author", "Roger Philp");
        xmlDataWriter.setElement("Date", "03/04/2006");
        xmlDataWriter.endClause("Description");

        CaptureDefaults captureDefaults = new CaptureDefaults();

        // audio settings
        xmlDataWriter.startClause("Audio");
        xmlDataWriter.setElement("sampleRate", captureDefaults.getSampleRate());
        xmlDataWriter.setElement("sampleSizeInBits", captureDefaults.getSampleSizeInBits());
        xmlDataWriter.setElement("channels", captureDefaults.getChannels());
        xmlDataWriter.setElement("signed", captureDefaults.isSigned());
        xmlDataWriter.setElement("bigEndian", captureDefaults.isBigEndian());
        xmlDataWriter.endClause("Audio");

        // data format
        xmlDataWriter.startClause("DataType");
        xmlDataWriter.setElement("fileType", "binary");
        xmlDataWriter.setElement("numberType", "float");
        xmlDataWriter.endClause("DataType");

        xmlDataWriter.startClause("DataFiles");
        xmlDataWriter.setElement("rmsDataFile", "RMS.dat");

  //      String rmsFileName = outDir + "\\" + "RMS.dat";
  //      PrintStream  rmsOutputFileStream;
  //      try {
  //          rmsOutputFileStream = new PrintStream(new FileOutputStream(rmsFileName));
  //      } catch (FileNotFoundException ee) {
  //          ee.printStackTrace();
  //      }

        JList fileList = signalDisplaySettings.getDataFileJList();

                int minIndex = fileList.getMinSelectionIndex();
        int maxIndex = fileList.getMaxSelectionIndex();
            String fileNames[];
                fileNames = signalDisplaySettings.getDataFileNames();
        if (signalDisplaySettings.getSelectAllCheckBox().isSelected()) {
            minIndex = 0;
            maxIndex = fileNames.length - 1;
        }
    // add entry to catalog
    // outputCatalogFile.println(filename);
    for(int i = minIndex; i <= maxIndex; i++){
    xmlDataWriter.startClause("element");
    int iiend = fileNames[i].indexOf(".");
    xmlDataWriter.setElement("fileName",fileNames[i].substring(0, iiend) + ".bin");
    xmlDataWriter.setElement("fileSize", Integer.toString(signalDisplaySettings.getDataFileSizes()[i]));
    xmlDataWriter.endClause("element");
    }
    xmlDataWriter.endClause("DataFiles");
    xmlDataWriter.write();
    xmlDataWriter.close();
}
}
