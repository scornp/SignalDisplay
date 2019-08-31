package rp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by IntelliJ IDEA.
 * User: scornp
 * Date: 28-Aug-2006
 * Time: 05:55:58
 * The signalDisplay and analysis package as part
 * of the MDAQ package
 */
public class BatchFrame extends JFrame {
    Container container;
    GridBagLayout gbLayout;
    GridBagConstraints gbConstraints;
    MenuListener menuListener;
    SignalDisplaySettings signalDisplaySettings;
    String filePath;
    String fileNames[];
    JList fileList;
    DisplayPanel displayPanel;
    int[] fileSize;
    String filetype;

    BatchFrame(SignalDisplaySettings signalDisplaySettings) {
        super("Batch generator frame");
        this.signalDisplaySettings = signalDisplaySettings;
        filePath = signalDisplaySettings.getCatalogueFilePath();
        fileNames = signalDisplaySettings.getDataFileNames();
        displayPanel = signalDisplaySettings.getDisplayPanel();
        fileSize = signalDisplaySettings.getDataFileSizes();
        filetype = signalDisplaySettings.getFiletype();
        fileList = signalDisplaySettings.getDataFileJList();
        this.setSize(40, 60);
        this.addWindowListener(new WindowCloser());
        container = getContentPane();
        gbLayout = new GridBagLayout();
        container.setLayout(gbLayout);

        gbConstraints = new GridBagConstraints();
        gbConstraints.fill = GridBagConstraints.NONE;

        System.out.println("Batch frame ");


        // setup batch panel
        JPanel batchPanel = new JPanel(new GridLayout(10, 1));


        //     batchPanel.add(testPanel);

        //     signalDisplaySettings.setTestPanel(testPanel);


        // add the batch panel to the signalDisplaySettings
        signalDisplaySettings.setBatchPanel(batchPanel);
        //   batchPanel.setVisible(false);
        batchPanel.setBorder(BorderFactory.createTitledBorder("Batch Panel"));
        //   batchPanel.setPreferredSize(new Dimension(300, 200));

        //todo need to add code for output dir selection
        //todo need to add filer name
        //todo need to add executable name

        int minIndex = fileList.getMinSelectionIndex();
        int maxIndex = fileList.getMaxSelectionIndex();
        System.out.println("LoadListedDataButtonListener " + filePath + " " + signalDisplaySettings.getCatalogueFilePath());
        System.out.println("LoadListedDataButtonListener min index = " + minIndex + " max index = " + maxIndex);

// file selection
        JPanel startPanel = new JPanel();

        JLabel startFile = new JLabel("Start file");
        JTextField startFileName = new JTextField(fileNames[minIndex]);
        startFileName.setEnabled(false);

        startPanel.add(startFile);
        startPanel.add(startFileName);

        JPanel endPanel = new JPanel();

        JLabel endFile = new JLabel("End file");
        JTextField endFileName = new JTextField(fileNames[maxIndex]);
        endFileName.setEnabled(false);
        endPanel.add(endFile);
        endPanel.add(endFileName);

        batchPanel.add(startPanel);
        batchPanel.add(endPanel);

// processing options
        JPanel checkBoxPanel = new JPanel();
        JLabel batchSelectionType = new JLabel("Select all");
        JCheckBox selectAllCheckBox = new JCheckBox("", false);

        checkBoxPanel.add(batchSelectionType);
        checkBoxPanel.add(selectAllCheckBox);
        batchPanel.add(checkBoxPanel);

        signalDisplaySettings.setSelectAllCheckBox(selectAllCheckBox);
        String[] extensions = {"fir", "dat"};
// the output dir panel
        JRNPSelector outputDirSelector = new JRNPSelector("Output dir", 70, 100, System.getenv("MDAQDataRoot"), true, extensions);
        batchPanel.add(outputDirSelector);
        signalDisplaySettings.setOutputDirSelector(outputDirSelector);

        // the filter panel

        extensions[0] = "fcf";
        extensions[1] = "dat";
        JRNPSelector filterSelector = new JRNPSelector("Filter", 70, 100, System.getenv("MDAQDataRoot"), false, extensions);
        batchPanel.add(filterSelector);
        signalDisplaySettings.setFilterSelector(filterSelector);

        // setup executable selection panel
        extensions[0] = "exe";
        JRNPSelector executableSelector = new JRNPSelector("Executable", 70, 100, System.getenv("MDAQDataRoot"), false, extensions);
        batchPanel.add(executableSelector);
        signalDisplaySettings.setExecutableSelector(executableSelector);

        JPanel platformChooserPanel = new JPanel();
        final JLabel label = new JLabel("Platform");
        platformChooserPanel.add(label, BorderLayout.NORTH);
        JRadioButton standAlone = new JRadioButton("Standalone", true);
        JRadioButton condor = new JRadioButton("Condor", false);

        signalDisplaySettings.setStandAlone(standAlone);
        signalDisplaySettings.setCondor(condor);

        platformChooserPanel.add(standAlone, BorderLayout.WEST);
        platformChooserPanel.add(condor, BorderLayout.EAST);

        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(standAlone);
        radioGroup.add(condor);
        batchPanel.add(platformChooserPanel);

        JPanel byteSwapPanel = new JPanel();
        JLabel byteSwapLabel = new JLabel("PC byte format");
        byteSwapPanel.add(byteSwapLabel, BorderLayout.NORTH);

        JCheckBox byteSwapCheckBox = new JCheckBox("", true);
        byteSwapPanel.add(byteSwapCheckBox);
        batchPanel.add(byteSwapPanel);

        JButton genInputFile = new JButton("Generate Input files");

        GenInputFileListener genInputFileListener
                = new GenInputFileListener(signalDisplaySettings);
        genInputFile.addActionListener(genInputFileListener);

        batchPanel.add(genInputFile);

        StartFilteringListener startFilteringListener = new StartFilteringListener(signalDisplaySettings);
        JButton startFilterButton = new JButton("Start Filtering");
        startFilterButton.addActionListener(startFilteringListener);
        batchPanel.add(startFilterButton);

        addComponent(batchPanel, 0, 0, 1, 1);
        setVisible(true);
        pack();
    }

    class WindowCloser extends WindowAdapter {
        public void windowClosing(WindowEvent event) {
            System.out.println("the event was    " + event);
            System.exit(0);
        }
    }

    private void addComponent(Component c, int row, int column, int width, int height) {
        gbConstraints.gridx = column;
        gbConstraints.gridy = row;
        gbConstraints.gridwidth = width;
        gbConstraints.gridheight = height;
        gbConstraints.weightx = 0;
        gbConstraints.weighty = 0;
        gbLayout.setConstraints(c, gbConstraints);
        container.add(c);
    }
}
