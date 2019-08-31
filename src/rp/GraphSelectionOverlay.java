package rp;

import org.jfree.chart.ChartRenderingInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

//todo this class needs to be refreshed when the underlying panel is refreshed

/**
 * Created by IntelliJ IDEA.
 * User: scornp
 * Date: 11-Mar-2007
 * Time: 22:23:45
 * A class to overlay graph data with a visual selector
 */
public class GraphSelectionOverlay extends JPanel implements Runnable {

    // The position of the start and end of the rectangle to be drawn
    int startX;
    int endX;
    int startY;
    int endY;
    // Status of the rectangle to be drawn
    boolean drawRectangle = false;

    /**
     * Constructor sets the initial panel to being transparent
     */
    public GraphSelectionOverlay() {
        //  this.setSize(200, 200);
        setOpaque(false);

        //  repaint();
    }

    /**
     * @param width  sets the width for the panel after resizing
     * @param height sets the height for the panel after resizing
     */
    public void setNewSize(int width, int height) {
        this.setSize(width, height);
    }

    int plotAreaMinX;
    int plotAreaMinY;
    int plotAreaMaxX;
    int plotAreaMaxY;

    /**
     * This sets the limits for  the chart graph area
     *
     * @param plotAreaMinX minimum X
     * @param plotAreaMinY minimum Y
     * @param plotAreaMaxX maximum X
     * @param plotAreaMaxY maximum Y
     */
    public void setPlotArea(int plotAreaMinX, int plotAreaMinY, int plotAreaMaxX, int plotAreaMaxY) {
        this.plotAreaMinX = plotAreaMinX;
        this.plotAreaMinY = plotAreaMinY;
        this.plotAreaMaxX = plotAreaMaxX;
        this.plotAreaMaxY = plotAreaMaxY;
    }

    ChartRenderingInfo info2;

    public void setChartInfo(ChartRenderingInfo info2) {
        this.info2 = info2;
        Rectangle2D dataArea2 = info2.getPlotInfo().getDataArea();
        plotAreaMinX = (int) dataArea2.getMinX();
        plotAreaMinY = (int) dataArea2.getMinY();
        plotAreaMaxX = (int) dataArea2.getMaxX();
        plotAreaMaxY = (int) dataArea2.getMaxY();
    }

    /**
     * Sets the position of the selection region to be drawn
     *
     * @param startX x start position
     * @param startY y start position
     * @param endX   x end position
     * @param endY   y end position
     */
    public void drawRectangle(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
        drawRectangle = true;
    }

    private boolean drawAudioScanLineSwitch = false;
    private int audioScanLinePosition = 0;
    boolean continueToDrawplayLine = false;

    /**
     * Start the thread for drawing the sound line
     */
    public void pleaseStart() {
        Rectangle2D dataArea2 = info2.getPlotInfo().getDataArea();

        plotAreaMinX = (int) dataArea2.getMinX();
        plotAreaMinY = (int) dataArea2.getMinY();
        plotAreaMaxX = (int) dataArea2.getMaxX();
        plotAreaMaxY = (int) dataArea2.getMaxY();
        continueToDrawplayLine = true;

        (new Thread(this)).start();
    }

    /**
     * Stop the sound draeing thread
     */
    public void pleaseStop() {
        continueToDrawplayLine = false;
    }

    int numberOfPlayBlocks;

    /**
     * The total number of blocks to draw
     *
     * @param numberOfPlayBlocks number of block the
     *                           sound segment has been broken down into
     */
    public void setNumberOfBlocks(int numberOfPlayBlocks) {
        this.numberOfPlayBlocks = numberOfPlayBlocks;
    }

    int playLineBlock;

    /**
     * The sound block currently plaing
     *
     * @param playLineBlock current sound block
     */
    public void setPlayLineForBlock(int playLineBlock) {
        this.playLineBlock = playLineBlock;
    }

    /**
     * The run method sets the play line
     */
    public void run() {
        int audioScanLinePositionOld = plotAreaMinX;
        int audioScanLinePositionNew;
        while (continueToDrawplayLine) {
            audioScanLinePositionNew
                    = (int) ((float) plotAreaMinX
                    + (((float) (plotAreaMaxX - plotAreaMinX) * (float) playLineBlock) / (float) (numberOfPlayBlocks - 1)));
            drawAudioScanLineSwitch = true;

            for (int j = audioScanLinePositionOld; j < audioScanLinePositionNew; j++) {
                audioScanLinePosition = j;
                System.out.println("audioScanLinePosition " + audioScanLinePosition);
                repaint();

            }
            audioScanLinePositionOld = audioScanLinePositionNew;
            try {
                Thread.sleep(4);
            } catch (InterruptedException e) {
            }
        }
    }


    /**
     * Draw the selection and play line
     *
     * @param g graphics context
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;
        if (drawRectangle) {
            GradientPaint gp = new GradientPaint(50.0f, 50.0f, Color.blue,
                    50.0f, 250.0f, Color.green);
            graphics2D.setPaint(gp);
            AlphaComposite ac =
                    AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
            graphics2D.setComposite(ac);
            graphics2D.fillRect(startX, startY, endX - startX, endY - startY);
        }

        if (drawAudioScanLineSwitch) {
            graphics2D.setColor(Color.black);
            graphics2D.drawLine(audioScanLinePosition, plotAreaMinY - 4, audioScanLinePosition, plotAreaMaxY - 5);
        }
    }
}
