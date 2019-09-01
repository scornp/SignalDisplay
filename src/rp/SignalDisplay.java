package rp;

import javax.swing.*;
import java.awt.*;

//import rp.ControlFrame;

/**
 * Project rp.SignalDisplay a subproject of Animator
 * Author: Roger Philp
 * Date: 05-Mar-2006
 * Time: 18:02:44
 * The purpose of this project is to load and display RMS values with audio
 * of the original signal
 */
public class SignalDisplay {
    public static void main(String args[]) {

        System.out.println("SignalDisplay hello");


  /*      JFrame xx = new JFrame();


      GraphSelectionOverlay yy = new GraphSelectionOverlay();
        yy.setPreferredSize(new Dimension(250, 250));
            xx.add(yy);
            xx.pack();
        xx.setVisible(true);*/
        
     //   st.
      //    String myString = new String("Hello\\World\\xx");
  //       String [] filer = myString.split("\\");
        
  //          for (int graphSelectionOverlay = 0; graphSelectionOverlay < filer.length; graphSelectionOverlay++) {
  //          System.out.println(" ------------>     " + filer[graphSelectionOverlay]);
   //         }


        ControlFrame controlFrame = new ControlFrame();

   /*         float xmin = 10;
       float xmax = 150;
       int npoints = 20;
        String title = "hello";
       WorkDoneProgressBar workDoneProgressBar = new WorkDoneProgressBar(xmin, xmax, title);

       float alpha;
       float delta;

       delta = (xmax - xmin) / (npoints - 1);

       for (int i = 0; i <= npoints; i++) {
           alpha = xmin + (i - 1) * delta;
           workDoneProgressBar.setStatus(alpha);
           try {
               Thread.sleep(100);
           } catch (InterruptedException ie) {
               ie.printStackTrace();
           }
       }

      WorkInProgressBar w = new WorkInProgressBar("fred");
        w.pleaseStart();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        w.pleaseStop();
        w.dispose();      */
    }

}
