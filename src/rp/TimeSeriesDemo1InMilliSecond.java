package rp;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.time.*;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Date;

//       public class TimeSeriesDemo1InMilliSecond extends JPanel implements MouseListener, MouseMotionListener {
public class TimeSeriesDemo1InMilliSecond extends JPanel {

    private ChartPanel chartPanel;
    private GraphSelectionOverlay graphSelectionOverlay;
    private JFreeChart chart;
    public XYPlot plot;
    private double[] dataArrayNew;
    private Date[] timeDateSeries;
    private int numberOfPoints;
    private int numberOfPointsToDisplay = 4000;
    TimeSeries series;
    SelectButtonActionListener selectButtonActionListener;
    MyPlotListener myPlotListener;
    String title;
    int indexArray[];
        XYDataset dataset;

    /**
     * This class loads in data from the start date and the end date in mills plus the
     * associated data array
     *
     * @param dataArrayNew the data array
     */
    public TimeSeriesDemo1InMilliSecond(String title, double[] dataArrayNew, Date[] timeDateSeries) {
        this.dataArrayNew = dataArrayNew;
        this.timeDateSeries = timeDateSeries;
        numberOfPoints = dataArrayNew.length;
        this.setBackground(Color.white);
        this.setForeground(Color.white);
        this.title = title;
        timeSeriesDemo1();
    }


    public void setSelectButtonActionListener(SelectButtonActionListener selectButtonActionListener) {
        this.selectButtonActionListener = selectButtonActionListener;
        myPlotListener.setDelectButtonActionListener(selectButtonActionListener);
    }


    public GraphSelectionOverlay getGraphSelectionOverlay() {
        return graphSelectionOverlay;
    }


    public void timeSeriesDemo1() {
    //    XYDataset dataset;

/* setup the sampling of the data because we can't fit all the stuff on the screen
   also there does not appear to be a Microseecond class.
   A sample rate of 44100Hz mmeans that there is a 26us gap between sample
   points so to ensure no two points occupy the same millisecond there has to be
   a minimum of 40 points between displayed data.

   not all of the data actually needs to be displayed
   each buffer will contain at least 441000 points
   but the window it is stored in is on ~ 400 or 0.1%
   and there are further complications with multiple
   entries in the time series with effectively the same
   millisecond entries. So the data thats actually displayed
   needs to be decimated. Lets try 1%.
 */
        int stride;
        if (numberOfPoints >= numberOfPointsToDisplay * 10) {
            stride = (int) ((float) (numberOfPoints - 1) / (float) (numberOfPointsToDisplay - 1));
        } else {
            stride = 1;
        }

        int maxIndex = (int) ((float) numberOfPointsToDisplay / (float) stride) * stride;

        System.out.println("After Timeseries before exception \n"
                + "numberOfPoints + " + numberOfPoints + "\n "
                + "numberOfPointsToDisplay + " + numberOfPointsToDisplay + "\n "
                + "stride " + stride + "\n"
                + "maxIndex " + maxIndex + "\n"
                + "Max sample point " + maxIndex * stride);

        int index;

        int indexArray[] = new int[numberOfPointsToDisplay];

        // create the new time series to be displayed
        series = new TimeSeries("Signal Amplitude", Millisecond.class);

        for (int i = 0; i < numberOfPointsToDisplay; i++) {
            index = i * stride;
            indexArray[i] = index;
            try {
                series.add(new Millisecond(timeDateSeries[index]), dataArrayNew[index]);
            } catch (Exception e) {
                System.out.println("Probably tried adding a microsecond entry again");
                System.out.println("TimeSeriesIn " + index + " " + stride);

         //       e.printStackTrace();
            }
        }
        System.out.println("After Timeseries ");
        dataset = new TimeSeriesCollection(series);

        chart = ChartFactory.createTimeSeriesChart(
                title,                   // title
                "Time",                     // x-axis label
                "Amplitude (Arb units)",    // y-axis label
                dataset,                    // data
                false,                      // create legend?
                true,                       // generate tooltips?
                false                       // generate URLs?
        );


        chart.setBackgroundPaint(Color.white);

        plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);


        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setLowerMargin(0);
        axis.setUpperMargin(0);
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 150));
        //     chartPanel.setMouseZoomable(true);
        chartPanel.setRangeZoomable(false);
        chartPanel.setDomainZoomable(true);
        chartPanel.setFillZoomRectangle(true);
        //    chartPanel.setForeground(Color.white);

        //     chartPanel.addMouseListener(this);
        //     chartPanel.addMouseMotionListener(this);

        graphSelectionOverlay = new GraphSelectionOverlay();

        ChartRenderingInfo info2 = chartPanel.getChartRenderingInfo();
        graphSelectionOverlay.setChartInfo(info2);
        graphSelectionOverlay.setVisible(true);

        graphSelectionOverlay.setPreferredSize(new Dimension(500, 150));
        graphSelectionOverlay.setLocation(0, 0);
        chartPanel.setLocation(0, 0);

        chartPanel.add(graphSelectionOverlay);
        add(chartPanel);
        //    this.addChangeListener(this);
        myPlotListener
                = new MyPlotListener(plot, chartPanel, series, dataArrayNew, timeDateSeries);
        plot.addChangeListener(myPlotListener);

    }

    public MyPlotListener getMyplotListner(){
        return myPlotListener;
    }

    public int getStartX() {
        return startX;
    }

    public int getEndX() {
        return endX;
    }

    public int getDataStartX() {
        int offX = (int) chartPanel.getChartRenderingInfo().getPlotInfo().getDataArea().getMinX();
        return startX - offX;
    }

    public int getDataEndX() {
        int offX = (int) chartPanel.getChartRenderingInfo().getPlotInfo().getDataArea().getMinX();
        return endX - offX;
    }

    public int getMaxDataX() {
        int offX = (int) chartPanel.getChartRenderingInfo().getPlotInfo().getDataArea().getMaxX();
        return offX;
    }


    public ChartPanel getChartPanel() {
        return chartPanel;
    }

    // this stuff is from display panel
    int startX;
    int endX;
    boolean mentToBeDrawingRectangle = false;

    public void mouseClicked(MouseEvent e) {
        /*
        ** check for double click
        */
        if (e.getClickCount() == 2) {
            System.out.println("Double click!");
        } else {
            if (e.getButton() == 3) {
                System.out.println("Cancelling selection");
                mentToBeDrawingRectangle = false;
                repaint();
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        startX = e.getX();
        System.out.println("mousePressed " + " startX " + startX);
        e.consume();
    }

    int selectionStartIndex;
    int selectionEndIndex;

    public void mouseDragged(MouseEvent e) {
        System.out.println("Mouse dragged event " + startX);
//        Point2D p = chartPanel.translateScreenToJava2D(new Point(getX(), getY()));

// setting the overlay panel starts here
        ChartRenderingInfo info2 = chartPanel.getChartRenderingInfo();
        Rectangle2D dataArea2 = info2.getPlotInfo().getDataArea();

        graphSelectionOverlay.setPlotArea((int) dataArea2.getMinX(), (int) dataArea2.getMinY(),
                (int) dataArea2.getMaxX(), (int) dataArea2.getMaxY());

        //    System.out.println("Mouse drag event detected..." + e.getButton());
        if (e.isShiftDown()) {
            System.out.println("The shift key is pressed ");
        } else if (e.isControlDown()) {
            System.out.println("The cntrl key is pressed ");
        } else {
            endX = e.getX();
            System.out.println("Selecting region from " + "startX " + startX + " endX " + endX);
            if (startX >= (int) dataArea2.getMinX() && endX <= (int) dataArea2.getMaxX()) {
                graphSelectionOverlay.drawRectangle(startX, (int) dataArea2.getMinY() - 5, endX, (int) dataArea2.getMaxY() - 4);
                graphSelectionOverlay.repaint();
            }
// the current selection starts here

        }
        e.consume();
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }


    /*   public static void main(String[] args) {

// setup dummy data for testing
String startTime = "20070110123000";
String endTime = "20070112163000";
int numberOfPoints = 400000;
double[] dataArray = new double[numberOfPoints];

dataArray[0] = 100.0;
for (int i = 1; i < numberOfPoints; i++) {
    dataArray[i] = dataArray[i - 1] * (1 + ((Math.random() - 0.499) / 100.0));
}
JFrame vv = new JFrame();
TimeSeriesDemo1InMilliSecond demo
        = new TimeSeriesDemo1InMilliSecond(TimeConverter.getCalendarXInMills(startTime),
        TimeConverter.getCalendarXInMills(endTime), dataArray);
vv.add(demo);
vv.pack();
RefineryUtilities.centerFrameOnScreen(vv);
vv.setVisible(true);
}        */


}
