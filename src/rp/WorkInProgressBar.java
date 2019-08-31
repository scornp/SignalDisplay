package rp;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: scornp
 * Date: 14-Nov-2006
 * Time: 10:03:01
 * This class creates a continuous activity progress bar
 */
public class WorkInProgressBar {
    ProgressPanel progressPanel;
    JWindow w;
    JLabel l;

    WorkInProgressBar(String title) {
        w = new JWindow();
        w.setSize(250, 100);

        JPanel windowPanel = new JPanel(new GridLayout(2, 1));
        JPanel p = new JPanel();

        progressPanel = new ProgressPanel(200, 10);

        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle frameDim = progressPanel.getBounds();
        progressPanel.setLocation((screenDim.width - frameDim.width) / 2,
                (screenDim.height - frameDim.height) / 2);
        progressPanel.setVisible(true);
        p.setBorder(BorderFactory.createTitledBorder(title));
        p.add(progressPanel);
        l = new JLabel();
        //     p.add(l);
        windowPanel.add(p);
        windowPanel.add(l);

        w.add(windowPanel);
        //    w.add(l);
        w.setVisible(false);
        w.setLocation((screenDim.width - frameDim.width) / 2,
                (screenDim.height - frameDim.height) / 2);

        w.invalidate();
        w.validate();
                            Container container;
        container = w.getContentPane();
        container.invalidate();
        p.setVisible(true);

        //   progressPanel.notifyAll();
        progressPanel.revalidate();
     //   progressPanel.pleaseStart();
        container.validate();
    }

    public void pleaseStart() {
        w.setVisible(true);
        progressPanel.pleaseStart();
    }

    public void pleaseStop() {
        progressPanel.pleaseStop();
        l.setText("Done");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
               w.setVisible(false);
    }

  //  public void dispose() {

  //  }
}
