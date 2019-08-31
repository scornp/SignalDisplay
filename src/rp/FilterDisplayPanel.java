package rp;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ  IDEA.
 * User: scornp
 * Date: 05-Mar-2007
 * Time: 13:37:11
 * To change this template use File | Settings | File Templates.
 */
public class FilterDisplayPanel extends JPanel {

    //    XYSeriesCollection dataset;
    String title;
    String xAxis;
    String yAxis;
    //    int sizeOfData;
    double[] filterArray;
    private ChartPanel chartPanel;
    XYSeriesCollection dataset;
            XYSeries series;
    public FilterDisplayPanel(String title, String xAxis, String yAxis, double[] filterArray) {
        this.title = title;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.filterArray = filterArray;
        this.setBackground(Color.white);
        this.setForeground(Color.white);

         series = new XYSeries("");

 /*       for (int i = 0; i < filterArray.length; i++) {
            series.add((double) i, filterArray[i]);
        }
*/

        dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        JFreeChart chart = createChart(dataset);
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 150));
    }

    public ChartPanel getChartPanel() {

        return chartPanel;
    }

    public void setNewDataSeries(double[] filter) {
        if (series != null) dataset.removeSeries(series);
        series = new XYSeries("");
        double value;
        for (int i = 1; i < filter.length; i++) {
            series.add((double)i, filter[i]);
        }
        dataset.addSeries(series);
    }

    /**
     * Creates a chart.
     *
     * @param dataset the data for the chart.
     * @return a chart.
     */
    private JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYAreaChart(
                title, // chart title
                xAxis, // x axis label
                yAxis, // y axis label
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

        /*   ValueAxis axis = plot.getDomainAxis();
                axis.setLowerMargin(0);
                axis.setUpperMargin(0);
        */

        //   add(chartPanel);
        return chart;
    }
}