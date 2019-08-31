package rp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: spxrnp
 * Date: 29-Nov-2006
 * Time: 14:30:14
 * To change  this template use File | Settings | File Templates.
 */

public class ProcessIndicatorListener 
implements ActionListener {
    SignalDisplaySettings signalDisplaySettings;
    int processID;
        String fileNames[];
        String filePath;
    ProcessIndicatorListener(SignalDisplaySettings signalDisplaySettings, int processID){
        this.signalDisplaySettings = signalDisplaySettings;
        this.processID = processID;

        filePath = signalDisplaySettings.getCatalogueFilePath();
        fileNames = signalDisplaySettings.getDataFileNames();
    }

    public void actionPerformed(ActionEvent e) {

        System.out.println("trying to recreate run " + fileNames[processID]);
                    CondorScriptBuilder CondorScriptBuilder = new CondorScriptBuilder(signalDisplaySettings);
                      CondorScriptBuilder.condorSubfileBuild(processID);

    }
}
