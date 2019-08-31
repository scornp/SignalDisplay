package rp;

import java.io.File;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

/**
 * Created by IntelliJ IDEA.
 * User: R Philp
 * Date: 21-Apr-2006
 * Time: 20:32:03
 * The XML data writer for formatting system paramemeters into an XML-like format
 */
public class XMLDataWriter {
    private int indentCount = 0;
    private String outputString = "";
    PrintStream masterIndexFile;

    /**
     * Constructor to open a data file. If the file exists it will try to move it first and then
     * create a new file
     *
     * @param saveFilename name of the file to which the meta data will be stored
     */
    XMLDataWriter(String saveFilename) {

        try {
            // need to test if file exists
            File file = new File(saveFilename);

            // get rid of backup copy
            if (new File(saveFilename + ".old").exists()) new File(saveFilename + ".old").delete();

            // copy current to backup
            if (file.exists()) {
                // copy old file to a backup copy
                File backupFile = new File(saveFilename + ".old");

                if (!file.renameTo(backupFile)) {
                    System.out.println("Data writer : warnuing file "
                            + saveFilename + " not moved");
                }
            }

            // create new file
            masterIndexFile = new PrintStream(new FileOutputStream(saveFilename));
        } catch (FileNotFoundException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Start a major clause of the name @param string
     *
     * @param string name of element clause to create
     */
    public void startClause(String string) {
        indentCount++;
        for (int i = 0; i < indentCount; i++) outputString += "\t";
        outputString += getBeginSections(string) + "\n";
    }

    /**
     * End a major clause
     *
     * @param string name of element clause to close
     */
    public void endClause(String string) {
        for (int i = 0; i < indentCount; i++) outputString += "\t";
        outputString += getEndSections(string) + "\n";
        indentCount--;
        if (indentCount < 0)
            System.out.println("Warning: Data writer has more closures than opens ");
    }


    /**
     * Create the basic element.
     *
     * @param elementName
     * @param booleanValue input value to be converted to a string
     */
    public void setElement(String elementName, boolean booleanValue) {
        String value = Boolean.toString(booleanValue);
        for (int i = 0; i < indentCount + 1; i++) outputString += "\t";
        outputString += getBeginSections(elementName) + value + getEndSections(elementName) + "\n";
    }

    /**
     * Create the basic element.
     *
     * @param elementName
     * @param floatValue  input value to be converted to a string
     */
    public void setElement(String elementName, float floatValue) {
        String value = Float.toString(floatValue);
        for (int i = 0; i < indentCount + 1; i++) outputString += "\t";
        outputString += getBeginSections(elementName) + value + getEndSections(elementName) + "\n";
    }

    /**
     * Create the basic element.
     *
     * @param elementName
     * @param intValue    input value to be converted to a string
     */
    public void setElement(String elementName, int intValue) {
        String value = Integer.toString(intValue);
        for (int i = 0; i < indentCount + 1; i++) outputString += "\t";
        outputString += getBeginSections(elementName) + value + getEndSections(elementName) + "\n";
    }

    /**
     * Create the basic element. Currently this function is not overloaded but will
     * be in the future
     *
     * @param elementName element to be created
     * @param value       assinged to that element
     */
    public void setElement(String elementName, String value) {
        for (int i = 0; i < indentCount + 1; i++) outputString += "\t";
        outputString += getBeginSections(elementName) + value + getEndSections(elementName) + "\n";
    }

    /**
     * display the string to the screen
     */
    public void display() {
        System.out.println(outputString);
    }

    /**
     * flush the output string to disk and reset the output string
     */
    public void write() {
        masterIndexFile.println(outputString);
        outputString = "\n";
    }

    /**
     * close the print stream
     */
    public void close() {
        masterIndexFile.close();
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
