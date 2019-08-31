package rp;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: R Philp
 * Date: 24-Apr-2006
 * Time: 13:24:29
 * A component of the MDAQ project to isolate data chunks
 * that contain meteor data
 */
public class SelectButtonActionListener implements ActionListener {
    SignalDisplaySettings signalDisplaySettings;
    AnalysisPanelComponents analysisPanelComponents;
    double[] dataArray;
    Date[] timeDatesArray;
    int iStart, iEnd;
    MyPlotListener myPlotListener;

    SelectButtonActionListener(SignalDisplaySettings signalDisplaySettings,
                               AnalysisPanelComponents analysisPanelComponents,
                               double[] dataArray, Date[] timeDatesArray, MyPlotListener myPlotListener) {
        this.signalDisplaySettings = signalDisplaySettings;
        this.analysisPanelComponents = analysisPanelComponents;
        this.dataArray = dataArray;
        this.timeDatesArray = timeDatesArray;
        this.myPlotListener = myPlotListener;
    }

    public void actionPerformed(ActionEvent e) {
        int iStart, iEnd;
        
        double sRate = new Double(signalDisplaySettings.getSampleRate());

        iStart = myPlotListener.getBeginIndex();
          iEnd =  myPlotListener.getEndIndex();

        System.out.println("SelectButtonActionListner :\n"
                + "---------> iStart \t" + iStart + "\n"
                + "---------> iEnd \t" + iEnd + "\n"
                + "---------> data iStart \t" + iStart + "\n"
                + "---------> data iEnd \t" + iEnd + "\n");

        double[] tmpArray;
        Date[] tmpTimeDateArray;
        tmpTimeDateArray = new Date[iEnd - iStart + 1];
        tmpArray = new double[iEnd - iStart + 1];

        int counter = 0;
        for (int i = iStart; i <= iEnd; i++) {
            tmpArray[counter] = dataArray[i];
            tmpTimeDateArray[counter] = timeDatesArray[i];
            counter++;
        }
        System.out.println("SelectButtonActionListner :"
                + "data length \t" + tmpArray.length + "\n"
                + "time length \t" + tmpTimeDateArray.length);
        boolean showControls = true;

        new AnalysisFrameNew(signalDisplaySettings, tmpArray, tmpTimeDateArray);

    }

/*    public void setIStartAndEnd(int iStart, int iEnd) {
        this.iStart = iStart;
        this.iEnd = iEnd;
    }*/
}
