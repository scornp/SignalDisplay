package rp;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: R Philp
 * Date: 01-May-2006
 * Time: 12:36:30
 * A component of the MDAQ project to isolate data chunks
 * that contain meteor data
 */
public class DataProcessor extends Thread {
    double[] filter;
    double[] dataArray;
    boolean fireOffDataProcessor;
    Date[] timeDatesArray;
    MyPlotListener myPlotListener;
    TimeSeriesDemo1InMilliSecondNew filteredSignalPanel;
    LoadFilterListener loadFilterListener;
    double [] filteredData;
    Date[] tmpTimeDatesArray;

    DataProcessor(double[] dataArray, Date[] timeDatesArray,
                  MyPlotListener myPlotListener, TimeSeriesDemo1InMilliSecondNew filteredSignalPanel,
                  LoadFilterListener loadFilterListener) {
        this.dataArray = dataArray;

        this.dataArray = dataArray;
        this.timeDatesArray = timeDatesArray;
        this.myPlotListener = myPlotListener;
        this.filteredSignalPanel = filteredSignalPanel;
        this.loadFilterListener = loadFilterListener;
        this.filter = loadFilterListener.getFilter();


        filteredData = new double[myPlotListener.endIndex - myPlotListener.beginIndex - filter.length + 1];
        tmpTimeDatesArray = new Date[myPlotListener.endIndex - myPlotListener.beginIndex - filter.length + 1];

        System.out.println("Generating filteredData length " + filteredData.length);
        System.out.println("Generating filter.length " + filter.length);
        System.out.println("Generating localDataArray.length " + dataArray.length);

    }

    public void pleaseStart() {
        //   continueLoop = true;

        (new Thread(this)).start();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            System.out.println("Can't sleep");
        }
    }


    public void run() {

        System.out.println("Generating filteredData length " + filteredData.length);
        System.out.println("Generating filter.length " + filter.length);
        System.out.println("Generating localDataArray.length " + dataArray.length);
        
        int k = myPlotListener.beginIndex;
        for (int i = 0; i < filteredData.length; i++) {
            //     for(int i = 0; i < 10; i++) {
            //     System.out.println("Generating filtered signal " + (float)i/filteredData.length);

            for (int j = 0; j < filter.length; j++) {
                filteredData[i] += dataArray[k + j] * filter[j];
                tmpTimeDatesArray[i] = timeDatesArray[k + j];

            }
                            k++;
        }
        filteredSignalPanel.setData("filtered data, ", filteredData, tmpTimeDatesArray);
        //   new AnalysisFrame(new SignalDisplaySettings(),  filteredData, true) ;

    }
}
