package rp;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: scornp
 * Date: 12-Nov-2006
 * Time: 11:36:08
 * To change this template use File | Settings | File Templates.
 */

public class VolumeSliderListener implements ChangeListener {
    SignalDisplaySettings signalDisplaySettings;

    public VolumeSliderListener(SignalDisplaySettings signalDisplaySettings) {

        this.signalDisplaySettings = signalDisplaySettings;
    }

    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();

        if (!source.getValueIsAdjusting()) {
         //   controlPanelComponents.getDisplayPanel().setStartPoint(source.getValue());
            //      button[0].setText("Play");
            System.out.println("SliderListner setting " + source.getValue());
       //     signalDisplaySettings.getDisplayPanel().setsliderMode(source.getValue());
        //    controlPanelComponents.getDisplayPanel().setStartPoint(source.getValue());
         //   controlPanelComponents.getDisplayPanel().drawSingleFrame();
         //   controlPanelComponents.getDisplayPanel().run();
        }
    }
}