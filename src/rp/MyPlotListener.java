package rp;

import org.jfree.chart.event.PlotChangeListener;
import org.jfree.chart.event.ChartProgressListener;
import org.jfree.chart.event.ChartProgressEvent;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;
import org.jfree.data.Range;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.Millisecond;
import org.jfree.data.xy.XYDataset;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: scornp
 * Date: 08-Nov-2008
 * Time: 19:34:19
 * This class obtains the new begining index and the endindex
 * of a selected region of the signal dramn by the TimeSeriesDemoInMilliSecond
 * a reference is then passed to the select button actionlistener.
 */
public class MyPlotListener implements ChangeListener,
        ChartProgressListener, PlotChangeListener {

    public XYPlot plot;
    public ChartPanel chartPanel;
    //   public TimeSeries series;
    private double[] dataArrayNew;
    private Date[] timeDateSeries;
    SelectButtonActionListener selectButtonActionListener;
    int beginIndex = 0;
    int endIndex = 0;

    /**
     * @param plot           is a reference to the signal chart graph
     * @param chartPanel     is a reference to the actual chart
     * @param series         time series to be removed
     * @param dataArrayNew   signal data array
     * @param timeDateSeries signal date stamps
     */
    public MyPlotListener(XYPlot plot, ChartPanel chartPanel, TimeSeries series,
                          double[] dataArrayNew, Date[] timeDateSeries) {

        this.plot = plot;
        this.chartPanel = chartPanel;
        //       this.series = series;
        this.dataArrayNew = dataArrayNew;
        this.timeDateSeries = timeDateSeries;
    }

    public void setDelectButtonActionListener(SelectButtonActionListener selectButtonActionListener) {
        this.selectButtonActionListener = selectButtonActionListener;

    }

    public void stateChanged(ChangeEvent e) {
        System.out.println("MyPlotListener stateChanged");
    }

    public void plotChanged(PlotChangeEvent event) {

        System.out.println("MyPlotListener plotChanged Chart state chaned");
        ValueAxis domainAxis = plot.getDomainAxis();
        Range range = domainAxis.getRange();

        Date beginDate = new Date((long) range.getLowerBound());
        Date endDate = new Date((long) range.getUpperBound());

        int ii;
        for (ii = 0; ii < timeDateSeries.length; ii++) {
            if (beginDate.equals(timeDateSeries[ii])) {
                System.out.println("beginIndex found it ");
                beginIndex = ii;
                break;
            }
        }

        for (ii = 0; ii < timeDateSeries.length; ii++) {
            if (endDate.equals(timeDateSeries[ii])) {
                System.out.println("endDate found it ");
                endIndex = ii;
                break;
            }
        }

        /*       double c = domainAxis.getLowerBound()
                + (20 / 100.0) * range.getLength();
        plot.setDomainCrosshairValue(c);*/
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public int getEndIndex() {

        if (endIndex == 0) endIndex = dataArrayNew.length; 
        return endIndex;
    }

    public void chartProgress(ChartProgressEvent event) {
        System.out.println("chartProgress");
        if (event.getType() != ChartProgressEvent.DRAWING_FINISHED) {
            return;
        }
/*        if (this.chartPanel != null) {
            JFreeChart c = this.chartPanel.getChart();
            if (c != null) {
                XYPlot plot = c.getXYPlot();
                XYDataset dataset = plot.getDataset();
                Comparable seriesKey = dataset.getSeriesKey(0);
                double xx = plot.getDomainCrosshairValue();

                long millis = (long) xx;
                int itemIndex = this.series.getIndex(new Minute(
                        new Date(millis)));
                if (itemIndex >= 0) {
                    TimeSeriesDataItem item = this.series.getDataItem(
                            Math.min(199, Math.max(0, itemIndex)));
                    TimeSeriesDataItem prevItem = this.series.getDataItem(
                            Math.max(0, itemIndex - 1));
                    TimeSeriesDataItem nextItem = this.series.getDataItem(
                            Math.min(199, itemIndex + 1));
                    long x = item.getPeriod().getMiddleMillisecond();
                    double y = item.getValue().doubleValue();
                     long prevX = prevItem.getPeriod().getMiddleMillisecond();
                    double prevY = prevItem.getValue().doubleValue();
                      long nextX = nextItem.getPeriod().getMiddleMillisecond();
                    double nextY = nextItem.getValue().doubleValue();

                }

            }
        }   */
    }

}
