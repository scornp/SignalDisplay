package rp;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: scornp
 * Date: 17-Mar-2009
 * Time: 19:36:50
 * To change this template use File | Settings | File Templates.
 */
public class StartFiltering implements ActionListener {
    double[] filter;
    double[] dataArray;
    boolean fireOffDataProcessor;
    Date[] timeDatesArray;
    MyPlotListener myPlotListener;
    TimeSeriesDemo1InMilliSecondNew filterPanel;
    LoadFilterListener loadFilterListener;

    StartFiltering(double[] dataArray, Date[] timeDatesArray,
                       MyPlotListener myPlotListener, TimeSeriesDemo1InMilliSecondNew filteredSignalPanel,
                       LoadFilterListener loadFilterListener) {
        this.dataArray = dataArray;
        this.timeDatesArray = timeDatesArray;
        this.myPlotListener = myPlotListener;
        this.filterPanel = filteredSignalPanel;
        this.loadFilterListener = loadFilterListener;
        fireOffDataProcessor = true;
        System.out.println("StartFiltering   is here!!!!!");
    }

    public void actionPerformed(ActionEvent e) {
        if (fireOffDataProcessor) {
            DataProcessor dataProcessor
                    = new DataProcessor(dataArray, timeDatesArray,
                        myPlotListener, filterPanel, loadFilterListener);

            dataProcessor.pleaseStart();
        }

    }
}