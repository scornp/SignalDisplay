package rp;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: R Philp
 * Date: 23-Apr-2006
 * Time: 14:12:10
 * A component of the MDAQ project to isolate data chunks
 * that contain meteor data
 * This class contains all the information about the current data source and selection
 */
public class SignalDisplaySettings {

    // Catalogue details
    private String catalogueFilePath;
    private String catalogueFileName;

    // general details
    private String Version;
    private String Comments;
    private String Program;
    private String Abstract;
    private String Author;
    private String Date;

    // audio details
    private String sampleRate;
    private String sampleSizeInBits;
    private String channels;
    private String signed;
    private String bigEndian;

    // data storage details
    private String filetype;
    private String datatype;

    // data files and sizes
    private String[] DataFileNames;
    private int[] DataFileSizes;

    private float[] dataArray;

    // filelist to be displayed
    private JList dataFileJList;

    private DisplayPanel displayPanel;


    private JButton loadListedDataButton;

    private String dataSourceFileExtension;

    String dataOutputRootDirectory;
    JLabel dataOutputLocationTextLabel;

    JRadioButton standAlone;
    JRadioButton condor;

    JPanel batchPanel;
    JButton batchButton;

    JRNPSelector outputDirSelector;
    JRNPSelector filterSelector;

    JButton playButton;
    JButton stopButton;

    JSlider frameSlider;
    VolumeSliderListener volumeSliderListener;


    public VolumeSliderListener getFrameSliderListener() {
        return volumeSliderListener;
    }

    public void setFrameSliderListener(VolumeSliderListener volumeSliderListener) {
        this.volumeSliderListener = volumeSliderListener;
    }

    public JSlider getFrameSlider() {
        return frameSlider;
    }

    public void setFrameSlider(JSlider frameSlider) {
        this.frameSlider = frameSlider;
    }

    public JButton getPlayButton() {
        return playButton;
    }

    public void setPlayButton(JButton playButton) {
        this.playButton = playButton;
    }

    public JButton getStopButton() {
        return stopButton;
    }

    public void setStopButton(JButton stopButton) {
        this.stopButton = stopButton;
    }

     public JCheckBox getSelectAllCheckBox() {
        return selectAllCheckBox;
    }

    public void setSelectAllCheckBox(JCheckBox selectAllCheckBox) {
        this.selectAllCheckBox = selectAllCheckBox;
    }

    JCheckBox selectAllCheckBox;

    public JRNPSelector getOutputDirSelector() {
        return outputDirSelector;
    }

    public void setOutputDirSelector(JRNPSelector outputDirSelector) {
        this.outputDirSelector = outputDirSelector;
    }

    public JRNPSelector getFilterSelector() {
        return filterSelector;
    }

    public void setFilterSelector(JRNPSelector filterSelector) {
        this.filterSelector = filterSelector;
    }

    public JRNPSelector getExecutableSelector() {
        return executableSelector;
    }

    public void setExecutableSelector(JRNPSelector executableSelector) {
        this.executableSelector = executableSelector;
    }

    JRNPSelector executableSelector;


    public JRNPSelector getTestPanel() {
        return testPanel;
    }

    public void setTestPanel(JRNPSelector testPanel) {
        this.testPanel = testPanel;
    }

    JRNPSelector testPanel;

    public JButton getBatchButton() {
        return batchButton;
    }

    public void setBatchButton(JButton batchButton) {
        this.batchButton = batchButton;
    }

    public JPanel getBatchPanel() {
        return batchPanel;
    }

    public void setBatchPanel(JPanel batchPanel) {
        this.batchPanel = batchPanel;
    }

    public JLabel getExecutableNameLabel() {
        return executableNameLabel;
    }

    public void setExecutableNameLabel(JLabel executableNameLabel) {
        this.executableNameLabel = executableNameLabel;
    }

    JLabel executableNameLabel;

    public JRadioButton getStandAlone() {
        return standAlone;
    }

    public void setStandAlone(JRadioButton standAlone) {
        this.standAlone = standAlone;
    }

    public JRadioButton getCondor() {
        return condor;
    }

    public void setCondor(JRadioButton condor) {
        this.condor = condor;
    }

    public JLabel getFilterNameLabel() {
        return filterNameLabel;
    }

    public void setFilterNameLabel(JLabel filterNameLabel) {
        this.filterNameLabel = filterNameLabel;
    }

    JLabel filterNameLabel;

    public JLabel getDataOutputLocationTextLabel() {
        return dataOutputLocationTextLabel;
    }

    public void setDataOutputLocationTextLabel(JLabel dataOutputLocationTextLabel) {
        this.dataOutputLocationTextLabel = dataOutputLocationTextLabel;
    }

    public String getDataOutputRootDirectory() {
        return dataOutputRootDirectory;
    }

    public void setDataOutputRootDirectory(String dataOutputRootDirectory) {
        this.dataOutputRootDirectory = dataOutputRootDirectory;
    }


    public String getDataSourceFileExtension() {
        return dataSourceFileExtension;
    }

    public void setDataSourceFileExtension(String dataSourceFileExtension) {
        this.dataSourceFileExtension = dataSourceFileExtension;
    }

    public JButton getLoadListedDataButton() {
        return loadListedDataButton;
    }

    public void setLoadListedDataButton(JButton loadListedDataButton) {
        this.loadListedDataButton = loadListedDataButton;
    }

    public float[] getDataArray() {
        return dataArray;
    }

    public void setDataArray(float[] dataArray) {
        this.dataArray = dataArray;
    }

    public DisplayPanel getDisplayPanel() {
        return displayPanel;
    }

    public void setDisplayPanel(DisplayPanel displayPanel) {
        this.displayPanel = displayPanel;
    }

    public JList getDataFileJList() {
        return dataFileJList;
    }

    public void setDataFileJList(JList dataFileJList) {
        this.dataFileJList = dataFileJList;
    }

    public String getCatalogueFilePath() {
        return catalogueFilePath;
    }

    public void setCatalogueFilePath(String catalogueFilePath) {
        this.catalogueFilePath = catalogueFilePath;
    }

    public String getCatalogueFileName() {
        return catalogueFileName;
    }

    public void setCatalogueFileName(String catalogueFileName) {
        this.catalogueFileName = catalogueFileName;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public String getProgram() {
        return Program;
    }

    public void setProgram(String program) {
        Program = program;
    }

    public String getAbstract() {
        return Abstract;
    }

    public void setAbstract(String anAbstract) {
        Abstract = anAbstract;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(String sampleRate) {
        System.out.println("The sample rate is " + sampleRate);
        this.sampleRate = sampleRate;
    }

    public String getSampleSizeInBits() {
        return sampleSizeInBits;
    }

    public void setSampleSizeInBits(String sampleSizeInBits) {
        this.sampleSizeInBits = sampleSizeInBits;
    }

    public String getChannels() {
        return channels;
    }

    public void setChannels(String channels) {
        this.channels = channels;
    }

    public String getSigned() {
        return signed;
    }

    public void setSigned(String signed) {
        this.signed = signed;
    }

    public String getBigEndian() {
        return bigEndian;
    }

    public void setBigEndian(String bigEndian) {
        this.bigEndian = bigEndian;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String[] getDataFileNames() {
        return DataFileNames;
    }

    public void setDataFileNames(String[] dataFileNames) {
        this.DataFileNames = dataFileNames;
    }

    public int[] getDataFileSizes() {
        return DataFileSizes;
    }

    public void setDataFileSizes(int[] dataFileSizes) {
        this.DataFileSizes = dataFileSizes;
    }


    /**
     * print the aquired elements
     */
    public void print() {
        // general details
        System.out.println("Program " + Program);
        System.out.println("Version " + Version);
        System.out.println("Comments " + Comments);
        System.out.println("Abstract " + Abstract);
        System.out.println("Author " + Author);
        System.out.println("Date " + Date);

        // audio details
        System.out.println("sampleRate " + sampleRate);
        System.out.println("sampleSizeInBits " + sampleSizeInBits);
        System.out.println("channels " + channels);
        System.out.println("signed " + signed);
        System.out.println("bigEndian " + bigEndian);

        // data storage details
        System.out.println("filetype " + filetype);
        System.out.println("datatype " + datatype);

        // file and sizes
        for (int i = 0; i < DataFileNames.length; i++)
            System.out.println("fileName " + DataFileNames[i] + " " + "fileSize " + DataFileSizes[i]);
    }
}
