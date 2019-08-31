package rp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Date;

/**
 * Project Signal Display a subproject of Animator
 * Author: Roger Philp
 * Date: 05-Mar-2006
 * Time: 18:17:24
 * This class sets up the main control panel
 */
public class ControlFrame extends JFrame {
    Container container;
    GridBagLayout gbLayout;
    GridBagConstraints gbConstraints;
    MenuListener menuListener;
    SignalDisplaySettings signalDisplaySettings;

    public ControlFrame() {
        super("Signal analysis");
        this.setSize(40, 60);
        addWindowListener(new WindowCloser());
        container = getContentPane();
        gbLayout = new GridBagLayout();
        container.setLayout(gbLayout);

        gbConstraints = new GridBagConstraints();
        gbConstraints.fill = GridBagConstraints.NONE;
        // create components
        //
        System.out.println("ControlFrame 1");
        Calendar calendar = Calendar.getInstance();

        Calendar c = (Calendar) calendar.clone();
        c.set(2006, 02, 17, 15, 35, 03);
        Date date = c.getTime();

        System.out.println("ControlFrame date is " + date + " " + c.getTimeInMillis());

        long time = date.getTime();

        long timeDelta = 3600000;
        time += timeDelta;
        date.setTime(time);
        System.out.println("ControlFrame date is " + date);


        SignalDisplaySettings signalDisplaySettings = new SignalDisplaySettings();

        menuListener = new MenuListener(signalDisplaySettings);
        setupMenuBar();

        //todo posibly add a file size column or reintsate the table stuff

        // set up the data file list panel
        JPanel listPanel = new JPanel();
        listPanel.setBorder(BorderFactory.createTitledBorder("Data files"));
        listPanel.setPreferredSize(new Dimension(300, 200));
        // set up the list
        JList dataFileJList = new JList();
        //     dataFileJList.setEnabled(false);
        //     dataFileJList.setPreferredSize(new Dimension(250, 450));
        //    dataFileJList.setVisibleRowCount(20);
        dataFileJList.setAutoscrolls(true);
        dataFileJList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        JScrollPane dataFileListScrollPane = new JScrollPane(dataFileJList);
        //     dataFileJList.setPreferredSize(new Dimension(250, 250));
        listPanel.add(dataFileListScrollPane);
        listPanel.setForeground(Color.black);
        listPanel.setBackground(Color.white);

        // setup the list panel load button
        JButton loadListedDataButton = new JButton();
        loadListedDataButton.setText("Load");
        loadListedDataButton.setEnabled(false);
        loadListedDataButton.setPreferredSize(new Dimension(60, 20));


        LoadListedDataButtonListener loadListedDataButtonListener
                = new LoadListedDataButtonListener(signalDisplaySettings);

        loadListedDataButton.addActionListener(loadListedDataButtonListener);
        listPanel.add(loadListedDataButton);

        // setup the newJload button
        JButton newJChartLoadListedDataButton = new JButton();
        newJChartLoadListedDataButton.setText("JLoad");
        newJChartLoadListedDataButton.setEnabled(false);
        newJChartLoadListedDataButton.setPreferredSize(new Dimension(60, 20));

        listPanel.add(newJChartLoadListedDataButton);        

        signalDisplaySettings.setDataFileJList(dataFileJList);
        signalDisplaySettings.setLoadListedDataButton(loadListedDataButton);

        // setup the list panel load batchbutton
        // possibly this reall needs to go on the RMS window

        JButton batchButton = new JButton();
        batchButton.setText("Batch mode");
        batchButton.setEnabled(false);


        batchButton.setPreferredSize(new Dimension(60, 20));
        BatchButtonListener batchButtonListener
                = new BatchButtonListener(signalDisplaySettings);

        batchButton.addActionListener(batchButtonListener);
        listPanel.add(batchButton);

        signalDisplaySettings.setBatchButton(batchButton);

        //     JPanel dataLocationPanel = new JPanel(new FlowLayout());
        JPanel dataLocationPanel = new JPanel(new BorderLayout());
        dataLocationPanel.setPreferredSize(new Dimension(300, 50));
        dataLocationPanel.setBorder(BorderFactory.createTitledBorder("Root Data Storage location :"));
        JLabel dataLocationTextLabel = new JLabel(System.getenv("MDAQDataRoot"));

        signalDisplaySettings.setDataOutputLocationTextLabel(dataLocationTextLabel);
        if (System.getenv("MDAQDataRoot") == null) JOptionPane.showMessageDialog(this,
                "Root Data Storage location is not set with environment variable MDAQDataRoot",
                "Invalid tag will use end of file contents as end marker",
                JOptionPane.ERROR_MESSAGE);
        dataLocationPanel.add(dataLocationTextLabel, BorderLayout.WEST);

   //     JPanel progress = new JPanel();
     //       progress.setPreferredSize(new Dimension(300, 30));
       //   ProgressPanel progressBar = new ProgressPanel(200, 10);

         //  progress.add(progressBar);
        addComponent(dataLocationPanel, 0, 0, 1, 1);
        addComponent(listPanel, 1, 0, 1, 1);
     //  addComponent(progress, 2, 0, 1, 1);



        this.pack();

                        /* Center the frame */
        Dimension screenDim =
                Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle frameDim = this.getBounds();
        setLocation((screenDim.width - frameDim.width) / 2,
                (screenDim.height - frameDim.height) / 2);
        this.setVisible(true);


  //      progressBar.pleaseStart();
        repaint();
    }

    // set up the menu bar
    private void setupMenuBar() {
        JMenuBar mbar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menu.addSeparator();
        JMenuItem loadMasterIndexMenuItem = new JMenuItem("Load Master index");
        loadMasterIndexMenuItem.addActionListener(menuListener);
        menu.add(loadMasterIndexMenuItem);
        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(menuListener);
        menu.add(openMenuItem);
        JMenuItem printMenuItem = new JMenuItem("Print to file");
        printMenuItem.addActionListener(menuListener);
        menu.add(printMenuItem);
        menu.addSeparator();
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        menu.add(exitMenuItem);
        exitMenuItem.addActionListener(menuListener);
        mbar.add(menu);
        setJMenuBar(mbar);
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

    class WindowCloser extends WindowAdapter {
        public void windowClosing(WindowEvent event) {
            System.out.println("the event was    " + event);
            System.exit(0);
        }
    }
}
