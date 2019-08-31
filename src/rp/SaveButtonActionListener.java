package rp;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: R Philp
 * Date: 24-Apr-2006
 * Time: 15:16:10
 * A component of the MDAQ project to isolate data chunks
 * that contain meteor data
 */
public class SaveButtonActionListener extends JFrame implements ActionListener {

    SignalDisplaySettings signalDisplaySettings;
    AnalysisPanelComponents analysisPanelComponents;
    double[] dataArray;
    Date[] timeDatesArray;
    MyPlotListener myPlotListener;

    SaveButtonActionListener(SignalDisplaySettings signalDisplaySettings,
                             AnalysisPanelComponents analysisPanelComponents,
                             double[] dataArray, Date[] timeDatesArray,
                             MyPlotListener myPlotListener) {

        this.signalDisplaySettings = signalDisplaySettings;
        this.analysisPanelComponents = analysisPanelComponents;
        this.dataArray = dataArray;
        this.timeDatesArray = timeDatesArray;
        this.myPlotListener = myPlotListener;
    }


    public void actionPerformed(ActionEvent e) {
        File outputFileName = saveAFileName();

        int iStart = myPlotListener.getBeginIndex();
        int iEnd = myPlotListener.getEndIndex();

        System.out.println("saveButtonActionListner :\n"
                + "---------> iStart \t" + iStart + "\n"
                + "---------> iEnd \t" + iEnd + "\n"
                + "---------> data iStart \t" + iStart + "\n"
                + "---------> data iEnd \t" + iEnd + "\n");

        if (outputFileName != null) {
            try {
                PrintStream outputAsciiDataFile = new PrintStream(new FileOutputStream(outputFileName));

                for (int i = iStart; i < iEnd; i++) {
                    outputAsciiDataFile.println(dataArray[i]);
                }
                outputAsciiDataFile.close();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public File saveAFileName() {
        JFileChooser fileChooser = new JFileChooser();
        //    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //   fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setCurrentDirectory(new File(System.getenv("MDAQDataRoot")));
        fileChooser.setVisible(true);
        fileChooser.showSaveDialog(this);
        return fileChooser.getSelectedFile();
    }

}
