package rp;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;


/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 01-Apr-2005
 * Time: 20:31:15
 * The purpose of this class is to read and load the data files
 * to create a display frame and display the data
 *
 * @author Roger Philp
 * @version 1.4.1
 */
class MenuListener extends JFrame implements ActionListener {
    String[] metaData;
    JPanel frameSliderPanel;
    boolean dataLoaded;
    int thisNnode = 0;
    int numberOfNodes = 1;

    // localDataArray is used to store ascii style data from single data files or from the RMS file
    float[] localDataArray = null;
    SignalDisplaySettings signalDisplaySettings;

    MenuListener(SignalDisplaySettings signalDisplaySettings) {
        this.signalDisplaySettings = signalDisplaySettings;

    }

    public void actionPerformed(ActionEvent e) {
        String lnfName = e.getActionCommand();

        if (lnfName.equals("Open")) {

            // the current acceptable extentions are
            String[] extensions = {"cat", "dat", "asc", "out", "txt", "bin"};
            //todo need to sort out which files have been loaded

            // first setup the file reader and get a name
            File inFileName = getAFileName(extensions);

            // The cat entry simply loads a list of selectable files
            // loading of the data files occurs when loadListedDataButton of the control frame
            // fires an event to LoadListedDataButtonListener which does the reading and then
            // fires off a an analysis frame

            if (MyFileFilter.getExtension(inFileName).equals("cat")) {
                getTheCatalogueEntries(inFileName);

                // at this point we load the local data array into signal display settings and
                // fire off an analysis window with out ontrols
                signalDisplaySettings.setDataArray(localDataArray);
                boolean showControls = false;

                // this need to be replace with a widonw with suitable axes
                // and selectability

                RMSPanel RMSPAnel =
                 new RMSPanel(signalDisplaySettings, localDataArray);

                JFrame jjClone = new JFrame();
                 jjClone.add(RMSPAnel);
                 jjClone.pack();
                 jjClone.setVisible(true);

            } else if (MyFileFilter.getExtension(inFileName).equals("dat") |
                    MyFileFilter.getExtension(inFileName).equals("asc") |
                    MyFileFilter.getExtension(inFileName).equals("txt") |
                    MyFileFilter.getExtension(inFileName).equals("out")) {


                getTextBasedDataEntries(inFileName);
                signalDisplaySettings.setDataArray(localDataArray);
                boolean showControls = true;
         //       new AnalysisFrame(signalDisplaySettings, localDataArray, showControls);
            } else if (MyFileFilter.getExtension(inFileName).equals("bin")) {

                getBinaryBasedDataEntries(inFileName);
                signalDisplaySettings.setDataArray(localDataArray);

                boolean showControls = true;
                //       AnalysisFrame analysisFrame = new AnalysisFrame(signalDisplaySettings, localDataArray);
         //       new AnalysisFrame(signalDisplaySettings, localDataArray, showControls);
            }

        } else if (lnfName.equals("Load Master index")) {
        }
    }


    public File getAFileName(String[] extensions) {
        MyFileFilter myFileFilter = new MyFileFilter(extensions);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(myFileFilter);
        //    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //    fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setCurrentDirectory(new File(System.getenv("MDAQDataRoot")));
        fileChooser.setVisible(true);   // , "Attach"
        fileChooser.showOpenDialog(this);
        return fileChooser.getSelectedFile();
    }

    public File saveAFileName() {
        JFileChooser fileChooser = new JFileChooser();
        //    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //    fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setCurrentDirectory(new File(System.getenv("MDAQDataRoot")));
        fileChooser.setVisible(true);
        fileChooser.showSaveDialog(this);
        return fileChooser.getSelectedFile();
    }


    public void getBinaryBasedDataEntries(File inFileName) {
        //    float [] localDataArray = null;
        try {
            RandomAccessFile inputBinaryDataFile = new RandomAccessFile(inFileName, "r");
            localDataArray = new float[(int) inputBinaryDataFile.length() / 4];

            for (int j = 0; j < localDataArray.length; j++) {
                localDataArray[j] = inputBinaryDataFile.readFloat();
                if(j == 0) System.out.println("Data reader firt value = " + localDataArray[j]);
            }
            inputBinaryDataFile.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }


    public void getTextBasedDataEntries(File inFileName) {
        signalDisplaySettings.setDataSourceFileExtension(MyFileFilter.getExtension(inFileName));
        System.out.println("Loading a .dat file " + inFileName.toString());
        //     String tmp = getFileContents(inFileName.toString());
        //   System.out.println("Contains " + tmp);
        String fileContents = "";
        String tmp;
        int entryCount = 0;

        try {
            FileReader insrc = new FileReader(inFileName);
            BufferedReader chnl = new BufferedReader(insrc);
            while ((tmp = chnl.readLine()) != null) {
                entryCount++;
            }
            insrc.close();
            chnl.close();
            System.out.println("entry count = " + entryCount);

            localDataArray = new float[entryCount];

            insrc = new FileReader(inFileName);
            chnl = new BufferedReader(insrc);
            for (int i = 0; i < entryCount; i++) {
                localDataArray[i] = new Float(chnl.readLine());
            }
            insrc.close();
            chnl.close();
        }
        catch (IOException ie) {
            JOptionPane.showMessageDialog(this, "Unable to open file " + inFileName,
                    "Invalid filename", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("first element = " + localDataArray[0] + " last element = " + localDataArray[entryCount - 1]);

        //      signalDisplaySettings.setDataArray(localDataArray);

        //      AnalysisFrame analysisFrame = new AnalysisFrame(signalDisplaySettings, localDataArray);
        // fire off the display window
    }

    /**
     * This reads the entries of the Catalogue .cat file
     * @param inFileName
     */
    public void getTheCatalogueEntries(File inFileName) {
        signalDisplaySettings.setCatalogueFilePath(inFileName.getParent());

        // get the input catalogueFileName
        signalDisplaySettings.setCatalogueFileName(inFileName.getAbsolutePath());

        // get the meta data
        XMLDataReader xmlDataReader = new XMLDataReader(signalDisplaySettings.getCatalogueFileName());

        xmlDataReader.setLocation("Description");

        signalDisplaySettings.setProgram(xmlDataReader.getDetails("Program"));
        signalDisplaySettings.setVersion(xmlDataReader.getDetails("Version"));
        signalDisplaySettings.setComments(xmlDataReader.getDetails("Comments"));
        signalDisplaySettings.setAbstract(xmlDataReader.getDetails("Abstract"));
        signalDisplaySettings.setAuthor(xmlDataReader.getDetails("Author"));
        signalDisplaySettings.setDate(xmlDataReader.getDetails("Date"));

        // Step 4 get the audio information
        xmlDataReader.setLocation("Audio");

        signalDisplaySettings.setSampleRate(xmlDataReader.getDetails("sampleRate"));
        signalDisplaySettings.setSampleSizeInBits(xmlDataReader.getDetails("sampleSizeInBits"));
        signalDisplaySettings.setChannels(xmlDataReader.getDetails("channels"));
        signalDisplaySettings.setSigned(xmlDataReader.getDetails("signed"));
        signalDisplaySettings.setBigEndian(xmlDataReader.getDetails("bigEndian"));

        xmlDataReader.setLocation("DataType");

        signalDisplaySettings.setFiletype(xmlDataReader.getDetails("fileType"));
        signalDisplaySettings.setDatatype(xmlDataReader.getDetails("numberType"));

        xmlDataReader.setLocation("DataFiles");

        int count = xmlDataReader.getNumberOfElements("element");

        signalDisplaySettings.setDataFileNames(xmlDataReader.getElements("fileName", count));
        System.out.println(signalDisplaySettings.getDataFileNames());
        signalDisplaySettings.setDataSourceFileExtension
                ((MyFileFilter.getExtension(signalDisplaySettings.getDataFileNames()[0])));


        // get an array of all the frame sizes. count is determined above, this is not really
        // necessary since all files will have the same size
        String[] tmpInt = xmlDataReader.getElements("fileSize", count);

        System.out.println("The count is " + count);
        int[] fileSize = new int[count];
        for (int i = 0; i < tmpInt.length; i++) fileSize[i] = new Integer(tmpInt[i]);
        signalDisplaySettings.setDataFileSizes(fileSize);
        signalDisplaySettings.print();

        // populate the data list panel and enable
        signalDisplaySettings.getDataFileJList().setListData(signalDisplaySettings.getDataFileNames());
        //   signalDisplaySettings.getDataFileJList().setListData(xx);
        signalDisplaySettings.getDataFileJList().setEnabled(true);

        // enable the load and display panel button
        signalDisplaySettings.getLoadListedDataButton().setEnabled(true);
        signalDisplaySettings.getBatchButton().setEnabled(true);

        // Read the RMS data file
        getTextBasedDataEntries(new File(signalDisplaySettings.getCatalogueFilePath() + "\\RMS.dat"));
    }

}




