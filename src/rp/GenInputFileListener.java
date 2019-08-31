package rp;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: scornp
 * Date: 26-Aug-2006
 * Time: 06:39:01
 * The signalDisplay and analysis package as part
 * of the MDAQ package
 * This class creates the batch file and input namelist
 */
public class GenInputFileListener implements ActionListener {
    SignalDisplaySettings signalDisplaySettings;

    GenInputFileListener(SignalDisplaySettings signalDisplaySettings) {
        this.signalDisplaySettings = signalDisplaySettings;
    }

    private String outputString = "";
    String filePath;
    String outputFilePath;
    String fileNames[];
    JList fileList;
    DisplayPanel displayPanel;
    int[] fileSize;
    String filetype;
 //   String filterPath;
//    String filterName;

    public void actionPerformed(ActionEvent e) {
      //  this.signalDisplaySettings = signalDisplaySettings;
        filePath = signalDisplaySettings.getCatalogueFilePath();
        fileNames = signalDisplaySettings.getDataFileNames();
        displayPanel = signalDisplaySettings.getDisplayPanel();
        fileSize = signalDisplaySettings.getDataFileSizes();
        filetype = signalDisplaySettings.getFiletype();
        fileList = signalDisplaySettings.getDataFileJList();
        outputFilePath = signalDisplaySettings.getOutputDirSelector().getTextContents();

         String[] nameListFiles;
        nameListFiles = new String[fileNames.length];

        int minIndex = fileList.getMinSelectionIndex();
        int maxIndex = fileList.getMaxSelectionIndex();
        if (signalDisplaySettings.getSelectAllCheckBox().isSelected()) {
            minIndex = 0;
            maxIndex = fileNames.length - 1;
        }
        String tmp;
        String firstFile = null;
        String secondFile = null;
        String outputFile = null;
        String inputFileName;
        String filterNameAndPath = null;
        String tagFile = null;
        String byteSwap;
        String condorErrFile = null;
        String condorOutFile = null;

        for (int i = minIndex; i <= maxIndex; i++) {
            // first lets generate the namelist file name
            System.out.println(fileNames[i]);

            int iend = fileNames[i].indexOf(".");
            nameListFiles[i] = fileNames[i].substring(0, iend) + ".nml";
            condorErrFile = fileNames[i].substring(0, iend) + ".err";
            condorOutFile = fileNames[i].substring(0, iend) + ".out";
            outputFile = fileNames[i].substring(0, iend) + ".bin";
            tagFile = fileNames[i].substring(0, iend) + ".rms";
            System.out.println("Name list filename will be " + nameListFiles[i]);

            if (i == 0) {
                firstFile = "";
            } else {
                firstFile = fileNames[i - 1];
            }
            secondFile = fileNames[i];
            filterNameAndPath = signalDisplaySettings.getFilterSelector().getTextContents();

            StringTokenizer st = new StringTokenizer(filterNameAndPath, "\\");
        String filterName = null;
     while (st.hasMoreTokens()) {
         filterName = st.nextToken();
         System.out.println(filterName);
     }
         System.out.println(filterName);

            System.out.println("Namelist file : " + nameListFiles[i]);
            System.out.println(outputString);
            String outDir = signalDisplaySettings.getOutputDirSelector().getTextContents();
            System.out.println("out put file " + outDir + "\\" + nameListFiles[i]);

            outputString = "  &inputNML\n";
            if (i == 0) {
                outputString += "     dataFile1 = " + "\"\"" + '\n';
            } else {
                if (signalDisplaySettings.getCondor().isSelected()) {
                    outputString += "     dataFile1 = " + firstFile + '\n';
                } else {
                    outputString += "     dataFile1 = " + "\"" + filePath + "\\" + firstFile + "\"" + '\n';

                }
            }

            if (signalDisplaySettings.getCondor().isSelected()) {
                outputString += "     dataFile2 = " + secondFile + '\n';
                outputString += "     outputFile = " + outputFile + '\n';
                outputString += "     filterName = " + filterName + '\n';
                outputString += "     tagFile = "  + tagFile +  '\n';
                outputString += "     fileSize = " + fileSize[i] + '\n';
                outputString += "  /" + '\n';
            } else {
                outputString += "     dataFile2 = " + "\"" + filePath + "\\" + secondFile + "\"" + '\n';
                outputString += "     outputFile = " + "\"" + outputFilePath + "\\" + outputFile + "\"" + '\n';
                outputString += "     filterName = " + "\"" + filterNameAndPath + "\"" + '\n';
                outputString += "     tagFile = " + "\"" + outputFilePath + "\\" + tagFile + "\"" + '\n';
                outputString += "     fileSize = " + fileSize[i] + '\n';
                outputString += "  /" + '\n';
            }

            // create new file
            try {
                PrintStream nmlFile = new PrintStream(new FileOutputStream(outDir + "\\" + nameListFiles[i]));
                nmlFile.print(outputString);
                nmlFile.close();

            } catch (FileNotFoundException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        // generate batch file
        String outDir = signalDisplaySettings.getOutputDirSelector().getTextContents();
        String exe = signalDisplaySettings.getExecutableSelector().getTextContents();

        if (signalDisplaySettings.getStandAlone().isSelected()) {
            System.out.println("Selected standalone option");
            try {
                PrintStream batchFile = new PrintStream(new FileOutputStream(outDir + "\\" + "batch.bat"));

                for (int i = minIndex; i <= maxIndex; i++) {
                    batchFile.println("echo processing file " + nameListFiles[i]);
                    batchFile.println("cmd /C  \"" + exe + " < "
                            + outDir + "\\" + nameListFiles[i] + " &" + "\"");
                    System.out.println("-----> " + exe + " < " + nameListFiles[i] + " &");
                }
                batchFile.close();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        } else {
            System.out.println("Selected condor option");

            CondorScriptBuilder CondorScriptBuilder = new CondorScriptBuilder(signalDisplaySettings);
                      CondorScriptBuilder.condorSubfileBuild(minIndex, maxIndex);

      /**
             try {
                PrintStream batchFile = new PrintStream(new FileOutputStream(outDir + "\\" + "condor.bat"));
                 batchFile.println("cd " + outDir);
                 batchFile.println("cmd /C  \"" + "c:\\condor\\bin\\condor_submit " + outDir + "\\" + "condor.sub" + "\"");

                batchFile.close();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }



            StringBuffer condorString = new StringBuffer();

            condorString.append("# Condor submit script generated by signal display \n");
            condorString.append("# Created by Roger Philp  \n \n \n");
            condorString.append("universe   = vanilla  \n \n");
            condorString.append("#Transfer all files generated by job\n");
            condorString.append("transfer_files = ALWAYS \n \n");
            condorString.append("#Say we Never want to receive email about this job...\n");
            condorString.append("notification = Never \n \n");
            condorString.append("executable = " + exe);
            condorString.append("\n \n");
            condorString.append("environment = path=");
            condorString.append(outputFilePath);
            condorString.append("\n \n");
            condorString.append("#-------------------------------------------- \n  \n");

            for (int i = minIndex; i <= maxIndex; i++) {
                int iend = fileNames[i].indexOf(".");
                nameListFiles[i] = fileNames[i].substring(0, iend) + ".nml";
                outputFile = fileNames[i].substring(0, iend) + ".dat";
                tagFile = fileNames[i].substring(0, iend) + ".tag";
                System.out.println("Name list filename will be " + nameListFiles[i]);

                if (i == 0) {
                    firstFile = "";
                } else {
                    firstFile = fileNames[i - 1];
                }
                secondFile = fileNames[i];
                filterNameAndPath = signalDisplaySettings.getFilterSelector().getTextContents();


                condorString.append("\n");
                condorString.append("Transfer_input_files = ");
                condorString.append(outputFilePath + "\\" + nameListFiles[i]);
                condorString.append(", ");
                if (!firstFile.equals("")) {
                condorString.append(filePath + "\\" + firstFile);
                condorString.append(", ");
                }

                condorString.append(filePath + "\\" +secondFile);
                condorString.append(", ");
                condorString.append(filterNameAndPath);
                condorString.append("\n");
                condorString.append("Input = ");
                condorString.append(nameListFiles[i]);
                condorString.append("\n");
                condorString.append("Output = ");
                condorString.append(condorOutFile);
                condorString.append("\n");
                condorString.append("Error = ");
                condorString.append(condorErrFile);
                condorString.append("\n");
                condorString.append("queue \n  \n");
                condorString.append("#-------------------------------------------- \n  \n");

                try {
                    PrintStream condorFile = new PrintStream(new FileOutputStream(outDir + "\\" + "condor.sub"));
                    condorFile.println(condorString);
                    condorFile.close();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            }  **/


        }
    }
}
