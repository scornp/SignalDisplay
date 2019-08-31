package rp;

import javax.swing.*;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: R Philp
 * Date: 20-Apr-2006
 * Time: 13:13:29
 * A component of the MDAQ project to isolate data chunks
 * that contain meteor data. The Meta data class analyses and extracts data from
 * an input file
 */
public class XMLDataReader extends JFrame {

    private String fileContents;
    String currentLocation;
    String currentLocationSubtring;


    /**
     * Parse the string for all data concerning the current run
     *
     * @param openFileContents
     */
    XMLDataReader(String openFileContents) {

        this.fileContents = getFileContents(openFileContents);
    }

    /**
     * Count the number of complete sub elements in the current substring
     *
     * @return the number of complete
     */
    public int getNumberOfElements(String elementName) {
        int iiend = 0;
        int count = 0;
        while (true) {
            iiend = currentLocationSubtring.indexOf(getEndSections(elementName), iiend);

            if (iiend < 0) break;
            iiend++;
            count++;
        }
        return count;
    }

    /**
     * Look for a tagged quantity in a string
     *
     * @param quantity is the tag
     * @return the tagged substring minus the tag
     */
    public String getDetails(String quantity) {
        String tmp;
        int istart = currentLocationSubtring.lastIndexOf(getBeginSections(quantity));
        int iend = currentLocationSubtring.lastIndexOf(getEndSections(quantity));

        // error checking
        // if no errors return the tagged string other wise return a blank
        if (istart < 0 || iend < 0) {
            tmp = "";
        } else {
            tmp = currentLocationSubtring.substring(istart + getBeginSections(quantity).length(), iend);
        }
        return tmp;
    }

    public void setLocation(String currentLocation) {
        this.currentLocation = currentLocation;

        //todo turn this into a parser that can locate subsections so that it is recursive
        //todo description.sublevel.sublevel1.sublevel2 etc
        int istart = fileContents.indexOf(getBeginSections(currentLocation));
        int iend = fileContents.indexOf(getEndSections(currentLocation));
        if (istart < 0) {
            JOptionPane.showMessageDialog(this,
                    "The begin tag " + getBeginSections(currentLocation) + " does not exist ",
                    "Invalid tag", JOptionPane.ERROR_MESSAGE);
            //todo need to put in some bomb out action
        } else if (iend < 0) {
            JOptionPane.showMessageDialog(this,
                    "The end tag " + getEndSections(currentLocation) + " does not exist ",
                    "Invalid tag will use end of file contents as end marker",
                    JOptionPane.ERROR_MESSAGE);
            iend = fileContents.length() - 1;
            currentLocationSubtring
                    = fileContents.substring(istart + getBeginSections(currentLocation).length(), iend);
        } else {
            // ok so we have located the substring
            currentLocationSubtring
                    = fileContents.substring(istart + getBeginSections(currentLocation).length(), iend);
        }
    }

    /**
     * Count the number of @quantity in @string
     *
     * @param string   to be searched
     * @param quantity tagged quantity
     * @return the number of @quantity in @string
     */
    public int countNumberOfElements(String string, String quantity) {
        int iiend = 0;
        int count = 0;
        while (true) {
            iiend = fileContents.indexOf(getEndSections(quantity), iiend);

            if (iiend < 0) break;
            iiend++;
            count++;
        }
        return count;
    }

    /**
     * Get all the the first @count number of fully formed @quantity elements in @string
     *
     * @param elementName tagged quantity
     * @param count       number of elements to return
     * @return list of fully formed elements
     */
    public String [] getElements(String elementName, int count) {
        int istart = 0;
        int iend = 0;
        String [] element;
        element = new String[count];
        for (int i = 0; i < count; i++) {
            istart = currentLocationSubtring.indexOf(getBeginSections(elementName), istart);
            iend = currentLocationSubtring.indexOf(getEndSections(elementName), iend);

            element[i] = currentLocationSubtring.substring(istart + getBeginSections(elementName).length(), iend);
            istart = iend;
            iend++;
        }
        return element;
    }


    /**
     * Get the file contents as a string
     *
     * @param filename is the metaData file name
     * @return the contents of the meta data file as a String
     */
    public String getFileContents(String filename) {
        String fileContents = "";
        String tmp;
        StringBuffer x = new  StringBuffer();
        try {
            FileReader insrc = new FileReader(filename);
            BufferedReader chnl = new BufferedReader(insrc);
            while ((tmp = chnl.readLine()) != null) {
                x.append(tmp);
            //    fileContents += tmp;
            }
                 fileContents = x.toString();
            insrc.close();
            chnl.close();
          //   System.out.println(fileContents);
        }
        catch (IOException ie) {
            JOptionPane.showMessageDialog(this, "Unable to open file " + filename,
                    "Invalid filename", JOptionPane.ERROR_MESSAGE);
        }
        return fileContents;
    }

    /**
     * This routine generates the begin and end Tags
     *
     * @param sections the sections to be parsed for
     * @return begin Tags
     */
    public String getEndSections(String sections) {
        return "</" + sections + ">";
    }

    /**
     * This routine generates the begin and end Tags
     *
     * @param sections the sections to be parsed for
     * @return begin Tags
     */
    public String getBeginSections(String sections) {
        return "<" + sections + ">";
    }



}
