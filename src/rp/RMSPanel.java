package rp;

import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.chart.*;
import org.jfree.chart.plot.XYPlot;

import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Date;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: scornp
 * Date: 07-Mar-2007
 * Time: 15:27:58
 * The new file selector
 */
public class RMSPanel extends JPanel implements MouseListener, MouseMotionListener {

    private SignalDisplaySettings signalDisplaySettings;
    private float[] localDataArray;
    private JFreeChart chart;
    private ChartPanel chartPanel;
    private XYPlot plot;
    private int numberOfDataItems;
    private GraphSelectionOverlay jj;
    private XYDataset dataset;
    private TimeSeries s1;

    public RMSPanel(SignalDisplaySettings signalDisplaySettings, float[] localDataArray) {

        this.signalDisplaySettings = signalDisplaySettings;
        this.localDataArray = localDataArray;
        numberOfDataItems = localDataArray.length;

        s1 = new TimeSeries("Signal RMS Amplitude", Millisecond.class);

        Date[] allDates = new Date[localDataArray.length];

        for (int i = 0; i < localDataArray.length; i++)
            allDates[i] = new Date(TimeConverter.getCalendarX(signalDisplaySettings.getDataFileNames()[i]).getTimeInMillis());

        for (int i = 0; i < localDataArray.length; i++)
            s1.add(new Millisecond(allDates[i]), localDataArray[i]);

        dataset = new TimeSeriesCollection(s1);
        chart = ChartFactory.createTimeSeriesChart(
                "Ten second RMS values XX",    // title
                "Time",                     // x-axis label
                "RMS (Arb units)",          // y-axis label
                dataset,                    // data
                false,                      // create legend?
                true,                       // generate tooltips?
                false                       // generate URLs?
        );
        chartPanel = new ChartPanel(chart);
        chartPanel.setMouseZoomable(false);
        chartPanel.addMouseListener(this);
        chartPanel.addMouseMotionListener(this);
        chartPanel.setPreferredSize(new Dimension(500, 270));

        jj = new GraphSelectionOverlay();
        chartPanel.add(jj);
        add(chartPanel);
    }

    boolean firstTime = false;
    int clickNumber = 0;
    int markerPos = 0;

    int startX;
    int endX;
    boolean mentToBeDrawingRectangle = false;

    public void mouseClicked(MouseEvent e) {
        /*
        ** check for double click
        */
        if (e.getClickCount() == 2) {
            System.out.println("  and it's a double click!");
            System.out.println("The start and end points are " + startX + " " + endX);
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

        //  Point2D p = chartPanel.translateScreenToJava2D(new Point(getX(), getY()));
        Collection entities = chartPanel.getChartRenderingInfo().getEntityCollection().getEntities();
        System.out.println("RMS PAnel enitities " + entities.size());
// setting the overlay panel starts here
        ChartRenderingInfo info2 = chartPanel.getChartRenderingInfo();
        Rectangle2D dataArea2 = info2.getPlotInfo().getDataArea();
        //    System.out.println("Entity count is " + info2.getEntityCollection().getEntityCount());
        //    System.out.println("Entity is " + info2.getEntityCollection().getEntity(1).getArea());
        Rectangle2D xx = info2.getEntityCollection().getEntity(1).getArea().getBounds2D();
        //   System.out.println("Entity 1 is " + xx.getCenterX());
        Rectangle2D xx1 = info2.getEntityCollection().getEntity(entities.size() - 1).getArea().getBounds2D();
        //    System.out.println("Entity 7 is " + xx1.getCenterX());

        double xmin = xx.getCenterX();

        double xmax = xx1.getCenterX();

        jj.setNewSize((int) dataArea2.getWidth(), (int) dataArea2.getHeight());
        jj.setLocation((int) dataArea2.getMinX(), (int) dataArea2.getMinY());
        jj.setPreferredSize(new Dimension((int) dataArea2.getWidth(), (int) dataArea2.getHeight()));

        if (e.isShiftDown()) {
            System.out.println("The shift key is pressed ");
        } else if (e.isControlDown()) {
            System.out.println("The cntrl key is pressed ");
        } else {
            endX = e.getX();
            System.out.println("Selecting region from " + "startX " + startX + " endX " + endX);
            //         jj.drawRectangle(startX - (int) dataArea2.getMinX(), endX - (int) dataArea2.getMinX());
            jj.drawRectangle(startX - (int) dataArea2.getMinX(), 0, endX - (int) dataArea2.getMinX(), (int) dataArea2.getMaxY());
            jj.repaint();

//     now relate the marked region of the screen to the displayed data

            // if  startX - (int) dataArea2.getMinX() > 0 and  endX - (int) dataArea2.getMinX() < 0
            // we are in the data area
            // number of points is numberOfDataItems
            // the distance between points is:
            //
            double alpha = (numberOfDataItems - 1) / (xmax - xmin);
            double beta = 1 - alpha * xmin;

            double startDataPoint = alpha * startX + beta;
            double endDataPoint = alpha * endX + beta;
            ;

            System.out.println("Selecting items indices"
                    + (int) startDataPoint + " "
                    + ((int) endDataPoint - 1) + " "
                    + dataArea2.getMinX() + " "
                    + dataArea2.getMaxX() + " "
                    + numberOfDataItems);


            selectionStartIndex = ((int) startDataPoint);
            selectionEndIndex = ((int) endDataPoint - 1);
/*            String startBlock = null;
            String endBlock = null;
            boolean foundPreviousStartX = false;
            Collection entities = chartPanel.getChartRenderingInfo().getEntityCollection().getEntities();
            for (Iterator iter = entities.iterator(); iter.hasNext();) {
                ChartEntity element = (ChartEntity) iter.next();
                Rectangle rect = element.getArea().getBounds();

                if (rect.getCenterX() > startX)
                    if (!foundPreviousStartX) {
                        if (element.getToolTipText() != null) {
                            startBlock = element.getToolTipText().split(",")[1].split("\\)")[0].trim();
                            foundPreviousStartX = true;
                        }
                    }
                if (rect.getCenterX() < endX)
                    if (element.getToolTipText() != null) {
                        endBlock = element.getToolTipText().split(",")[1].split("\\)")[0].trim();
                    }
            }

            System.out.println("The selected start block is " + startBlock
                    + " the selected end block is " + endBlock);

//todo at some point we need to make sure that all is valid  in case something is null

// so the strings startBlock and  endBlock contain the name of the start and end blocks
// now we need to find their indices and marke the JList

            for (int i = 0; i < localDataArray.length; i++) {
                String str = (signalDisplaySettings.getDataFileNames()[i]).trim();

                if (startBlock.equals(str)) selectionStartIndex = i;
                if (endBlock.equals(str)) selectionEndIndex = i;
                System.out.println("The selected start block is" + startBlock
                        + "the selected end block is" + endBlock
                        + "compare to" + str
                        + "index " + i
                        + " selectionStartIndex " + selectionStartIndex
                        + " selectionEndIndex " + selectionEndIndex);
            }
// the end selection starts here
            System.out.println("The selected indicies are " + selectionStartIndex + " " + selectionEndIndex);
            //todo now need to sort of the objects selected
       */
            //todo ******* DANGER DANGER Will Robinson ************************
            //todo note this is a temporary fix only for the selection of files
            //todo need to have a scheme which discriminates between file and data selection
            int[] selectionIndices;
            selectionIndices = new int[selectionEndIndex - selectionStartIndex + 1];
            for (int i = 0; i < selectionEndIndex - selectionStartIndex + 1; i++)
                selectionIndices[i] = selectionStartIndex + i;
            signalDisplaySettings.getDataFileJList().setSelectedIndices(selectionIndices);
            repaint();
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

}
