package rp;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.RectangleInsets;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: R Philp
 * Date: 23-Apr-2006
 * Time: 19:52:36
 * The new data viewer and browser
 */
public class AnalysisFrameNew {
    boolean showControls;
    JPanel buttonPanel;

    JButton playButton;
    JButton stopButton;

    PlayButtonActionListener playButtonActionListener;
    JPanel volumeSliderPanel;
    JSlider volumeSlider;
    JPanel actionPanel;

    private double[] dataArrayNew;
    private Date[] timeDates;
    private int numberOfPoints;
    private SignalDisplaySettings signalDisplaySettings;

    AnalysisFrameNew(SignalDisplaySettings signalDisplaySettings,
                     double[] dataArrayNew, Date[] timeDates) {

        this.signalDisplaySettings = signalDisplaySettings;
        this.dataArrayNew = dataArrayNew;
        this.timeDates = timeDates;
        numberOfPoints = dataArrayNew.length;

        //todo temporary fix on dataArrayNew need to change this at some point
        //todo reall all of this should be simple float []

        //    float[] tmpFloatDataArray = new float[dataArrayNew.length];

        //    for (int i = 0; i < dataArrayNew.length; i++) tmpFloatDataArray[i] = (float) dataArrayNew[i];

        // ok we need to have a frame to put everything into
        JFrame analysisFrame;
        analysisFrame = new JFrame("Analysis Frame New");
        analysisFrame.setLayout(new FlowLayout());
        System.out.println("AnalysisFrameNew : \n"
                + "dataArrayNewLength " + dataArrayNew.length + "\n"
                + "timeDateswLength " + timeDates.length + "\n");
        analysisFrame.setBackground(Color.white);
        analysisFrame.setForeground(Color.white);
        //      JPanel analysisPanel = new JPanel(new FlowLayout());
        JPanel analysisPanel = new JPanel(new GridLayout(3, 2));
        analysisPanel.setBorder(BorderFactory.createEmptyBorder(50, 10, 10, 10));
        //    analysisPanel.setSize(50, 50);
        analysisPanel.setBackground(Color.white);
        analysisPanel.setForeground(Color.white);

        analysisFrame.add(analysisPanel);

//      so first off lets create a new time series window for the signal
        TimeSeriesDemo1InMilliSecond signalSeriesMilliSecond
                = new TimeSeriesDemo1InMilliSecond("Signal", dataArrayNew, timeDates);

//      and add it to the current frame

        analysisPanel.add(signalSeriesMilliSecond);

        double[] intensityDataArrayNew;

        intensityDataArrayNew = new double[dataArrayNew.length];
        int i;
        for (i = 0; i < dataArrayNew.length; i++) intensityDataArrayNew[i] = dataArrayNew[i] * dataArrayNew[i];
//      intensity window to analysisPanel
        TimeSeriesDemo1InMilliSecond intensitySeriesMilliSecond
                = new TimeSeriesDemo1InMilliSecond("Intensity", intensityDataArrayNew, timeDates);

        analysisPanel.add(intensitySeriesMilliSecond);


/*----------------  New FFT start
               Fourier bit starts here
 ---------------------------------------------*/

        // We will do the fft in 8096 block;
        int FFTBlockSize = 8192;

        int fftOffSet = 0;
        int numFFTBlocks = dataArrayNew.length / FFTBlockSize;
        Complex[] signalBlock = new Complex[FFTBlockSize];
        Complex[] FFTSignal;
        Complex[] FFTSignalTmp;
        float[] powerSignal = new float[FFTBlockSize];

        FFTSignalTmp = new Complex[FFTBlockSize];


        fftOffSet = 0;
        // reset the power signal for this time segment
        for (int k = 0; k < FFTBlockSize; k++) powerSignal[k] = 0;
        for (int k = 0; k < FFTBlockSize; k++) FFTSignalTmp[k] = new Complex(0, 0);
        System.out.println("Analysis numFFTBlocks " + numFFTBlocks);

        for (int jj = 0; jj < numFFTBlocks; jj++) {
            i = 0;
            for (int j = fftOffSet; j < fftOffSet + FFTBlockSize; j++) {
                if (j < dataArrayNew.length) {
                    signalBlock[i] = new Complex(dataArrayNew[j], 0);
                } else {
                    signalBlock[i] = new Complex(0, 0);
                }
                i++;
            }
            FFTSignal = FFT.fft(signalBlock);
            for (int k = 0; k < FFTBlockSize; k++) {
                FFTSignalTmp[k] = FFTSignalTmp[k].plus(FFTSignal[k]);
            }
            fftOffSet += FFTBlockSize;
        }

        float[] xx, yy;
        float wmin2 = 0, wmax2 = 44100 / 2, wdelta2;

        wdelta2 = (wmax2 - wmin2) / (FFTBlockSize / 2 - 1);

        xx = new float[FFTBlockSize / 2];
        yy = new float[FFTBlockSize / 2];

        for (i = 0; i < FFTBlockSize / 2; i++) {
            xx[i] = wmin2 + i * wdelta2;
            yy[i] = (float) FFTSignalTmp[i].abs();
        }
        System.out.println("Analysis yy[1] " + yy[1]);
        XYSeries series2 = new XYSeries("");

        for (i = 0; i < xx.length; i++) {
            series2.add(xx[i], yy[i]);
        }

        XYSeriesCollection dataset2;
        dataset2 = new XYSeriesCollection();
        dataset2.addSeries(series2);
        JFreeChart chart2 = createChart(dataset2);
        ChartPanel chartPanel2 = new ChartPanel(chart2);
        chartPanel2.setPreferredSize(new java.awt.Dimension(500, 150));
        analysisPanel.add(chartPanel2);

//----------------- New FFT end

        FilterDisplayPanel filterPanel
                = new FilterDisplayPanel("Filter", "frequency", "Poswer", dataArrayNew);

        TimeSeriesDemo1InMilliSecondNew filteredSignalPanel
                = new TimeSeriesDemo1InMilliSecondNew("Filtered", null, null);
                   filteredSignalPanel.setPlotListener(signalSeriesMilliSecond.getMyplotListner());
        analysisPanel.add(filterPanel.getChartPanel());
        analysisPanel.add(filteredSignalPanel.getChartPanel());
        AnalysisPanelComponents analysisPanelComponents = new AnalysisPanelComponents();

        analysisPanelComponents.setTimeSeriesDemo1InMilliSecond(signalSeriesMilliSecond);

        // create a new audio player and set a reference to it in analysisPanelComponents
        AudioPlayer audioPlayer = new AudioPlayer(dataArrayNew, analysisPanelComponents);
        analysisPanelComponents.setAudioPlayer(audioPlayer);

        JPanel controlPanel = new JPanel(new GridLayout(4, 1));
        JPanel playPanel = new JPanel(new FlowLayout());
        controlPanel.setBackground(Color.white);
        controlPanel.setForeground(Color.white);
        playPanel.setBackground(Color.white);
        playPanel.setForeground(Color.white);
        //    volumeSliderPanel = new JPanel(new FlowLayout());
        //    setupSliderPanel();


        playButtonActionListener
                = new PlayButtonActionListener(signalDisplaySettings, analysisPanelComponents);

        buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.white);
        buttonPanel.setForeground(Color.white);
        setUpButtonPanel();

        playPanel.add(buttonPanel);
        //  playPanel.add(volumeSliderPanel);

        controlPanel.add(playPanel);


// ************************************************
//
// everything seems to work ok up to this point
//
// ************************************************

        Dimension buttonSize = new Dimension(70, 30);
        JButton selectButton = new JButton("Select");
        selectButton.setPreferredSize(buttonSize);

        JPanel actionPanel = new JPanel(new FlowLayout());
        actionPanel.setBackground(Color.white);
        actionPanel.setForeground(Color.white);
        SelectButtonActionListener selectButtonActionListener
                = new SelectButtonActionListener(signalDisplaySettings,
                analysisPanelComponents,
                dataArrayNew, timeDates, signalSeriesMilliSecond.getMyplotListner());

        selectButton.addActionListener(selectButtonActionListener);
        signalSeriesMilliSecond.setSelectButtonActionListener(selectButtonActionListener);
        actionPanel.add(selectButton);
        controlPanel.add(actionPanel);
        playPanel.add(selectButton);

        //todo  tidy up panels when functionality is sorted
        //the save button is to save the current section of data
        SaveButtonActionListener saveButtonActionListener
                = new SaveButtonActionListener(signalDisplaySettings,
                analysisPanelComponents, dataArrayNew, timeDates,
                signalSeriesMilliSecond.getMyplotListner());

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(saveButtonActionListener);
        playPanel.add(saveButton);

        LoadFilterListener loadFilterListener = new LoadFilterListener(dataArrayNew, timeDates,
                signalSeriesMilliSecond.getMyplotListner(), filterPanel);
        JButton loadFilterButton = new JButton("load Filter");

        loadFilterButton.addActionListener(loadFilterListener);
        playPanel.add(loadFilterButton);

        StartFiltering startFilteringOnAnFrameListener = new StartFiltering(dataArrayNew, timeDates,
                signalSeriesMilliSecond.getMyplotListner(), filteredSignalPanel, loadFilterListener);
        JButton startFilteringButton = new JButton("Filter");
        startFilteringButton.addActionListener(startFilteringOnAnFrameListener);
        playPanel.add(startFilteringButton);

        controlPanel.add(playPanel);

        analysisPanel.add(controlPanel);

/*        int width = 400;
int height = 400;
DisplayPanel displayPanel = new DisplayPanel(width, height, signalDisplaySettings, dataArrayNew);
displayPanel.setPreferredSize(new Dimension(width, height));
displayPanel.setVisible(true);
analysisFrame.add(displayPanel, BorderLayout.NORTH);*/

/*
if (showControls) {
    // put the new controls in here
    playButtonActionListener
            = new PlayButtonActionListener(signalDisplaySettings, analysisPanelComponents);

    JPanel controlPanel = new JPanel(new GridLayout(2, 1));

    JPanel playPanel = new JPanel(new FlowLayout());


    volumeSliderPanel = new JPanel(new FlowLayout());
    setupSliderPanel();

    buttonPanel = new JPanel(new FlowLayout());
    setUpButtonPanel();

    playPanel.add(buttonPanel);
    playPanel.add(volumeSliderPanel);

    controlPanel.add(playPanel);


    AudioPlayer audioPlayer = new AudioPlayer(dataArrayNew, analysisPanelComponents);



    LoadFilterListener loadFilterListener = new LoadFilterListener(dataArrayNew);
    JButton loadFilterButton = new JButton("load Filter");

    loadFilterButton.addActionListener(loadFilterListener);
    //  loadFilterButton.setEnabled(false);

    IntensityButtonListener intensityButtonListener = new IntensityButtonListener(dataArrayNew);

    JButton intensityButton = new JButton("Intensity");
    intensityButton.addActionListener(intensityButtonListener);
    JButton rmsButton = new JButton("RMS");
    RMSButtonActionListener rmsButtonActionListener
            = new RMSButtonActionListener(signalDisplaySettings, analysisPanelComponents, dataArrayNew);
    rmsButton.addActionListener(rmsButtonActionListener);
    //     loadFilterButton.setEnabled(false);

    SaveButtonActionListener saveButtonActionListener
            = new SaveButtonActionListener(signalDisplaySettings, analysisPanelComponents, dataArrayNew);

    JButton saveButton = new JButton("Save");
    saveButton.addActionListener(saveButtonActionListener);

    actionPanel.add(loadFilterButton);
    actionPanel.add(intensityButton);
    actionPanel.add(rmsButton);
    actionPanel.add(saveButton);

    analysisPanelComponents.setDisplayPanel(displayPanel);
    analysisPanelComponents.setAudioPlayer(audioPlayer);
    analysisPanelComponents.setSelectButton(selectButtonActionListener);
    controlPanel.add(actionPanel);
    analysisFrame.add(controlPanel, BorderLayout.SOUTH);
}*/

        //       displayPanel.pleaseStart();
        //       analysisFrame.setVisible(true);
        //       analysisFrame.pack();

        // tidy up, pack the frame and make visible
        analysisFrame.pack();
        analysisFrame.setVisible(true);
        //   signalSeriesMilliSecond.getGraphSelectionOverlay().repaint();
    }

    private void setupSliderPanel() {
        volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 30, 10);
        volumeSlider.setVisible(false);
        signalDisplaySettings.setFrameSlider(volumeSlider);
        VolumeSliderListener volumeSliderListener = new VolumeSliderListener(signalDisplaySettings);
        signalDisplaySettings.setFrameSliderListener(volumeSliderListener);
        volumeSlider.addChangeListener(volumeSliderListener);
        volumeSliderPanel.add(volumeSlider);

        ClassLoader cldr = this.getClass().getClassLoader();
        java.net.URL imageURL = cldr.getResource("icons/Volume24.gif");
        ImageIcon stepBckIncon = new ImageIcon(imageURL);
        JLabel j = new JLabel(stepBckIncon);
        volumeSliderPanel.add(j);

        //@todo add in the volume icon at some point
        //  JPanel volumeIconPanel =  new JPanel(stepBckIncon);

        volumeSlider.setMinimum(0);
        volumeSlider.setMaximum(10);
        volumeSlider.setMinorTickSpacing(1);
        volumeSlider.setValue(0);
        volumeSlider.setVisible(true);
        volumeSlider.setPaintTrack(true);
    }

    // set up the play control panel
    private void setUpButtonPanel() {
        //@todo change the icons to what the should really be!
        ClassLoader cldr = this.getClass().getClassLoader();

        java.net.URL imageURL = cldr.getResource("icons/StepBack24.gif");
        ImageIcon stepBckIncon = new ImageIcon(imageURL);

        imageURL = cldr.getResource("icons/Play24.gif");
        ImageIcon playIncon = new ImageIcon(imageURL);

        imageURL = cldr.getResource("icons/Stop24.gif");
        ImageIcon stopIncon = new ImageIcon(imageURL);
        Dimension buttonSize = new Dimension(30, 30);
        playButton = new JButton(playIncon);
        playButton.setPreferredSize(buttonSize);

        //     playButton.setBackground(Color.white);
        //     playButton.setForeground(Color.white);
        stopButton = new JButton(stopIncon);
        stopButton.setPreferredSize(buttonSize);

        signalDisplaySettings.setPlayButton(playButton);

        signalDisplaySettings.setStopButton(stopButton);

        // set the button so that they can't be fiddled with before data is loaded
        //@todo move disabling of the buttons to the controlPanelComponents
        playButton.setEnabled(true);
        stopButton.setEnabled(true);

        playButton.addActionListener(playButtonActionListener);

        stopButton.addActionListener(playButtonActionListener);

        buttonPanel.add(stopButton);
        buttonPanel.add(playButton);

    }

    /**
     * Creates a chart.
     *
     * @param dataset the data for the chart.
     * @return a chart.
     */
    private JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYAreaChart(
                "Fourier transform", // chart title
                "frequency", // x axis label
                "Poswer", // y axis label
                dataset, // data
                PlotOrientation.VERTICAL,
                false, // include legend
                true, // tooltips
                false // urls
        );

        chart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        return chart;
    }
}
