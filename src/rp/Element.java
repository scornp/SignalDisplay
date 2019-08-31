package rp;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: scornp
 * Date: 11-Nov-2006
 * Time: 09:49:36
 * To change this template use File | Settings | File Templates.
 */
public class Element {
  String fileNames;
    JButton processIndicator;

    Element nextElement;
    Element previousElement;

    Element(String fileNames, JButton processIndicator){
    this.fileNames = fileNames;
    this.processIndicator  = processIndicator;
}

    public String getFileNames() {
        return fileNames;
    }

    public JButton getProcessIndicator() {
        return processIndicator;
    }

    public  void setNextElement(Element tmp) {
        nextElement = tmp;
    }

    public  void setPreviousElement(Element tmp){
        previousElement = tmp;
    }

    public Element getNextElement(){
        return (nextElement);
    }

    public Element getPreviousElement(){
        return (previousElement);
    }

}
