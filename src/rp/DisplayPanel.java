package rp;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by IntelliJ IDEA.
 * User: Roger philp
 * Date: 26-Aug-2005
 * Time: 09:50:03
 * To change this template use File | Settings | File Templates.
 */

class DisplayPanel extends JPanel implements Runnable, MouseListener, MouseMotionListener {
    //  public class rp.DisplayPanel extends JPanel implements Runnable, MouseMotionListener {
    private BufferedImage onScreenBufferedImage;
    private Color background = Color.BLACK;
    private int height;
    private int width;
    private int bufferedImagesize;
    private SignalDisplaySettings signalDisplaySettings;
    float dataArray[];

    /**
     * Constructor to the DisplaPanel class
     *
     * @param width                 width of the display panel
     * @param height                height of the display panel
     * @param signalDisplaySettings of the environment
     */
    public DisplayPanel(int width, int height, SignalDisplaySettings signalDisplaySettings, float[] dataArray) {
        this.width = width;
        this.height = height;
        this.dataArray = dataArray;
        Container cc;
        //   this.setSize(width, height);
        this.signalDisplaySettings = signalDisplaySettings;
        //     this.setMinimumSize(new Dimension(width, height));
        bufferedImagesize = width;
        //    intArray = new int[bufferedImagesize];
        //   for (int i = 0; i < width; i++) intArray[i] = height / 2;
        setVisible(true);
        setBackground(background);
        onScreenBufferedImage = new BufferedImage(bufferedImagesize, height, BufferedImage.TYPE_INT_RGB);
        addMouseListener(this);
        addMouseMotionListener(this);
        runa();
    }

    public DisplayPanel(int width, int height, float[] dataArray) {
        this.width = width;
        this.height = height;
        this.dataArray = dataArray;
        Container cc;
        //   this.setSize(width, height);
        this.signalDisplaySettings = signalDisplaySettings;
        //     this.setMinimumSize(new Dimension(width, height));
        bufferedImagesize = width;
        //    intArray = new int[bufferedImagesize];
        //   for (int i = 0; i < width; i++) intArray[i] = height / 2;
        setVisible(true);
        setBackground(background);
        onScreenBufferedImage = new BufferedImage(bufferedImagesize, height, BufferedImage.TYPE_INT_RGB);
        addMouseListener(this);
        addMouseMotionListener(this);
        runa();
    }

    public void setData(float[] dataArray) {
        this.dataArray = dataArray;
    }

    public void pleaseStartIntensity() {
        //    continueLoop = true;

        (new Thread(this)).start();
        runa();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            System.out.println("Can't sleep");
        }
    }

    public void pleaseStart() {
        //    continueLoop = true;

        (new Thread(this)).start();
        //  runa();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            System.out.println("Can't sleep");
        }
    }

    public void run() {
        //  runa();
        repaint();
        try {
            Thread.sleep(50);
        } catch (Exception te) {
        }
    }

    /**
     * The run method to display data to the screen. This may be turned off in the
     * future to increase speed
     */
    public void runa() {
        int j = 0;
        int localSignal = 0;
        float xmin = 0;
        float xmax = 0;
        float Xmin = 0;
        float Xmax = width - 1;
        float ymin = 0;
        float ymax = 0;
        float Ymin = height - 1;
        float Ymax = 0;
        System.out.println("Display panel run method");
        Graphics g = onScreenBufferedImage.getGraphics();
        // scale the data
        // real cooredinates
        float x[];
        x = new float[dataArray.length];
        float y[];
        y = new float[dataArray.length];

        // internal coordinates
        float X[];
        X = new float[dataArray.length];
        float Y[];
        Y = new float[dataArray.length];

        // initialise real values
        for (int i = 0; i < dataArray.length; i++) {
            x[i] = (float) i;
            y[i] = dataArray[i];
        }

        xmin = getMin(x);
        xmax = getMax(x);
        ymin = getMin(y);
        ymax = getMax(y);

        // tranformation x' = m x + b
        float mx, bx;
        mx = (Xmax - Xmin) / (xmax - xmin);
        bx = Xmax - mx * xmax;

        // tranformation y' = m y + b
        float my, by;
        my = (Ymax - Ymin) / (ymax - ymin);
        by = Ymax - my * ymax;

        for (int i = 0; i < dataArray.length; i++) {
            X[i] = mx * x[i] + bx;
            Y[i] = my * y[i] + by;
        }

        g.clearRect(0, 0, width, height);

        //      g.setStroke(stroke);
        for (j = 0; j < dataArray.length - 1; j++) {
            g.drawLine((int) X[j], (int) Y[j], (int) X[j + 1], (int) Y[j + 1]);
            this.repaint();
        }
    }

    public float getMin(float[] array) {
        float tmp = array[0];
        for (int i = 0; i < array.length; i++) if (tmp > array[i]) tmp = array[i];
        return tmp;
    }

    public float getMax(float[] array) {
        float tmp = array[0];
        for (int i = 0; i < array.length; i++) if (tmp < array[i]) tmp = array[i];
        return tmp;
    }

    boolean mentToBeDrawingRectangle = false;

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.drawImage(onScreenBufferedImage, 0, 0, this);

        if (mentToBeDrawingRectangle) {
            graphics2D.setColor(Color.YELLOW);
            graphics2D.drawRect((int) startX, (int) 0, (int) (endX - startX), (int) height - 1);
        }

        if (drawAudioScanLineSwitch) {
            graphics2D.setColor(Color.RED);
            //  System.out.println("repaint method");
            graphics2D.drawLine(audioScanLinePosition, 0, audioScanLinePosition, height - 1);
        }      //        System.out.println("repaint method");

    }

    int audioScanLinePosition = 50;
    boolean drawAudioScanLineSwitch = false;


    public void drawAudioScanLine(int i) {
        audioScanLinePosition = (int) (width * ((float) i / dataArray.length));
        //     System.out.println("displayPanel audioScanLinePosition = " + audioScanLinePosition );
        drawAudioScanLineSwitch = true;

        //   for(int ii = 0; ii < 400; ii++){
        //       audioScanLinePosition = ii;
        repaint();

        //   }
        //  this.validate();

        //   drawAudioScanLineSwitch = false;

    }

    int startX;
    int endX;

    /**
     * What should be happening here is a drag creates a rectangle
     * A right click cancels it
     * An one of the buttons should select the region of interest
     *
     * @param e
     */

    public void mouseClicked(MouseEvent e) {
        /*
        ** check for double click
        */
        if (e.getClickCount() == 2) {
            System.out.println("  and it's a double click! and time " +
                    "to sort out the appropriate blocks");
            System.out.println("The start and end points are " + startX + " " + endX);
        } else {
            if (e.getButton() == 3) {
                System.out.println("Cancelling selection");
                //     componentsAndSettings.setSelectBlockBegin(-1);
                //     componentsAndSettings.setSelectBlockEnd(-1);
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
        //    System.out.println("Mouse drag event detected..." + e.getButton());
        if (e.isShiftDown()) {
            System.out.println("The shift key is pressed ");
        } else if (e.isControlDown()) {
            System.out.println("The cntrl key is pressed ");
        } else {
            endX = e.getX();
            System.out.println("Selecting region from " + "startX " + startX + " endX " + endX);

            System.out.println("Array length = " + dataArray.length
                    + " relative indexs are from "
                    + (int) ((float) dataArray.length / width * startX) + " to "
                    + (int) ((float) dataArray.length / width * endX));
            selectionStartIndex = (int) ((float) dataArray.length / width * startX);
            if (selectionStartIndex < 0) selectionStartIndex = 0;
            if (selectionStartIndex >= dataArray.length) selectionStartIndex = dataArray.length - 2;
            selectionEndIndex = (int) ((float) dataArray.length / width * endX);
            if (selectionEndIndex < 0) selectionEndIndex = 1;
            if (selectionEndIndex >= dataArray.length) selectionEndIndex = dataArray.length - 1;
            mentToBeDrawingRectangle = true;

            //todo ******* DANGER DANGER Will Robinson ************************
            //todo note this is a temporary fix only for the selection of files
            //todo need to have a scheme which discriminates between file and data selection
            int[] selectionIndoces;
            selectionIndoces = new int[selectionEndIndex - selectionStartIndex + 1];
            for (int i = 0; i < selectionEndIndex - selectionStartIndex + 1; i++)
                selectionIndoces[i] = selectionStartIndex + i;
            signalDisplaySettings.getDataFileJList().setSelectedIndices(selectionIndoces);
            repaint();
        }
        e.consume();
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        System.out.println("mouseReleased " + e.getButton());
        //  componentsAndSettings.getSelectionButton().setEnabled(true);
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

}


