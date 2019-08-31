package rp;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: scornp
 * Date: 29-Aug-2006
 * Time: 04:39:55
 * The signalDisplay and analysis package as part
 * of the MDAQ package
 */
public class ProgressPanel extends JPanel implements Runnable {
    Graphics2D g2 = null;
    BufferedImage imageBuffer;
    Graphics2D bufferedGraphicsContext;

    int width;
    int height;
    int lbound;
    int ubound;
    boolean continueLoop;

    /**
     * First we need to know how big to make it
     * The its bounds
     */
    ProgressPanel(int width, int height) {//, int lbound, int ubound) {


        this.width = width;
        this.height = height;
        //   this.lbound = lbound;
        //   this.ubound = ubound;
        //    this.setPreferredSize(new Dimension(width, height));
        //       new GridLayout()
        this.setLayout(new FlowLayout());
        imageBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                imageBuffer.setRGB(i, j, Color.WHITE.getRGB());
            }
        }
           //  = imageBuffer.createGraphics();
    //    g2  = imageBuffer.createGraphics();
        this.revalidate();
    }


    /**
     * Please start methosd sets the internal continueLoop (to be renamed)
     * it also take the current displayPanel and puts it on a thread
     * and executes the run() method until the loop is ended or the
     * continueLoop is reset by the other components.
     */
    public void pleaseStart() {
        continueLoop = true;
        System.out.println("Starting load thread");
        this.invalidate();
        //    (new Thread(this)).start();
        Thread t = new Thread(this);
        t.setPriority(Thread.MAX_PRIORITY);
        t.setDaemon(true);
        t.start();
    }

    /**
     * The Please stop method resets the internal continueLoop (formall known as
     * stopgracefully - it may well go back to that!) to false and
     * should be set by all components wishing to interupt the system
     */
    public void pleaseStop() {
        continueLoop = false;
    }


    protected void paintComponent(Graphics g) {
   //     protected void pc() {

        super.paintComponent(g);
        g2 = (Graphics2D) g;
        g2.drawImage(imageBuffer, 0, 0, this);
        System.out.println("Redraw  ");
    //    g2.fill3DRect(0, 0, 10, 10, true);
    }

    public void run() {
        Color color = Color.WHITE;

        while (continueLoop) {
            System.out.println("Running progress panel");
            if (color == Color.WHITE) {
                color = Color.RED;
            } else if (color == Color.RED) {
                color = Color.WHITE;
            }
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    imageBuffer.setRGB(i, j, color.getRGB());
                }

            //    pc();
       //         if (i%20 == 0){
                  paintImmediately(0, 10, 200, 10);
       //         repaint();
         //       this.revalidate();
           //     super.revalidate();}
                //   this.notify();
                try {
                    Thread.sleep(2);
                } catch (Exception te) {
                }
            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                imageBuffer.setRGB(i, j, Color.GREEN.getRGB());
            }
        }
      //  repaint();
     //   paintImmediately(0, 10, 200, 10);
    //    this.revalidate();
    //    super.revalidate();
    }
}
