package rp;

import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: scornp
 * Date: 11-Nov-2006
 * Time: 09:26:45
 * This class creates a link list of all jobs to be processed
 */
public class CompletedList extends Thread {
    SignalDisplaySettings signalDisplaySettings;
    JButton[] processIndicator;
    /**
     * initialise the tail to null for new instance
     */
    public Element tail = null;
    /**
     * initialise the head to null for new instance
     */
    public Element head = null;
    /**
     * intialise the element count to 0 for new instance - don't remember why its static!
     */
    public static int counter = 0;

    String fileNames[];
    JList fileList;
    int minIndex;
    int maxIndex;

    CompletedList(SignalDisplaySettings signalDisplaySettings, JButton[] processIndicator) {
        this.signalDisplaySettings = signalDisplaySettings;
        this.processIndicator = processIndicator;
        fileNames = signalDisplaySettings.getDataFileNames();
        fileList = signalDisplaySettings.getDataFileJList();
        minIndex = fileList.getMinSelectionIndex();
        maxIndex = fileList.getMaxSelectionIndex();

        /**
         * pushes the new buffer onto the link
         * list and increments the link list internal counter
         */
        for (int i = 0; i < maxIndex - minIndex + 1; i++) {
            if (tail == null) {
                tail = new Element(fileNames[i + minIndex], processIndicator[i]);
                tail.setPreviousElement(null);
                tail.setNextElement(null);
                head = tail;
                counter++;
            } else {
                Element newElement;
                newElement = new Element(fileNames[i + minIndex], processIndicator[i]);
                head.setNextElement(newElement);
                newElement.setPreviousElement(head);
                head = newElement;
                head.setNextElement(null);
                counter++;
            }
        }

        head.setNextElement(tail);
        tail.setPreviousElement(head);
    }

    public void popElement(Element element) {
        element.getProcessIndicator().setBackground(Color.RED);
        element.getNextElement().setPreviousElement(element.getPreviousElement());
        element.getPreviousElement().setNextElement(element.getNextElement());
        counter--;
    }

    public void pleaseStart() {
        run();

    }

    public void run() {
        Element tmp = tail;
        Element nextElement = null;
        String fileName;
        String outputFilePath = signalDisplaySettings.getOutputDirSelector().getTextContents();

        while (counter > 0) {
            int iend = tmp.getFileNames().indexOf(".");
            fileName = outputFilePath + "\\" + tmp.getFileNames().substring(0, iend) + ".rms";
           //        System.out.println("Checking for file : " + fileName + "\n");

            boolean exists = (new File(fileName)).exists();
            if (tmp.getNextElement() != null) nextElement = tmp.getNextElement();
            if (exists) {
                //          System.out.println("File exists : " + fileName + "\n");
                popElement(tmp);
            }
            tmp = nextElement;

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Float[] rmsDataElement;
        rmsDataElement = new Float[maxIndex - minIndex + 1];
        System.out.println(" maxIndex - minIndex + 1 " + (maxIndex - minIndex + 1));
        for (int i = minIndex; i <= maxIndex; i++) {
            int iend = fileNames[i].indexOf(".");
            fileName = outputFilePath + "\\" + fileNames[i].substring(0, iend) + ".rms";

            try {
                FileReader insrc = new FileReader(new File(fileName));
                BufferedReader chnl = new BufferedReader(insrc);
                rmsDataElement[i - minIndex] = new Float(chnl.readLine());
                     System.out.println("Lately reading " + fileName + " " + rmsDataElement[i - minIndex]);
                insrc.close();
                chnl.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        /**
         * Now try and write the data out to the RMS.dat file
         */

        String rmsFileName = outputFilePath + "\\" + "RMS.dat";
        PrintStream rmsOutputFileStream = null;
        try {
            rmsOutputFileStream = new PrintStream(new FileOutputStream(rmsFileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < rmsDataElement.length; i++) {
            System.out.println("Writing rms file " + rmsDataElement.length + " " + rmsDataElement[i]);
            rmsOutputFileStream.println(rmsDataElement[i]);
        }       
        rmsOutputFileStream.close();
    }

}

