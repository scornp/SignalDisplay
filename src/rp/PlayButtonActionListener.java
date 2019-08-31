package rp;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * DemitriDataReader
 * Author: Roger Philp
 * Date: 17-Apr-2006
 * Time: 11:27:17
 */
public class PlayButtonActionListener implements ActionListener {
    float [] dataArray;
    int sampleSizeInBits = 16;
    int numberOfBitsPerByte = 8;
    SignalDisplaySettings signalDisplaySettings;
    AnalysisPanelComponents analysisPanelComponents;

    PlayButtonActionListener(SignalDisplaySettings signalDisplaySettings, AnalysisPanelComponents analysisPanelComponents) {
        this.signalDisplaySettings = signalDisplaySettings;
        this.analysisPanelComponents = analysisPanelComponents;
    }

    boolean firstTime = true;
    public void actionPerformed(ActionEvent e) {

        System.out.println("Playbutton listner has been activated");
              if (e.getSource() == signalDisplaySettings.getPlayButton()){
                  System.out.println("Attempting to play");
                  analysisPanelComponents.getAudioPlayer().pleaseStart();
              } else if (e.getSource() == signalDisplaySettings.getStopButton()){
                  System.out.println("Attempting to stop");
                  analysisPanelComponents.getAudioPlayer().pleaseStop();
              }
    }
}
