package rp;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: R Philp
 * Date: 24-Apr-2006
 * Time: 13:27:51
 * A component of the MDAQ project to isolate data chunks
 * that contain meteor data
 */
public class AnalysisPanelComponents {

    private JButton playButton;
    private JButton stopButton;
    private JButton selectButton;
    private DisplayPanel displayPanel;
    private AudioPlayer audioPlayer;
    private Thread thread;
    private TimeSeriesDemo1InMilliSecond timeSeriesDemo1InMilliSecond;

    public TimeSeriesDemo1InMilliSecond getTimeSeriesDemo1InMilliSecond() {
        return timeSeriesDemo1InMilliSecond;
    }

    public void setTimeSeriesDemo1InMilliSecond(TimeSeriesDemo1InMilliSecond timeSeriesDemo1InMilliSecond) {
        this.timeSeriesDemo1InMilliSecond = timeSeriesDemo1InMilliSecond;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public Thread getThread() {
        return thread;
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public void setAudioPlayer(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    public JButton getPlayButton() {
        return playButton;
    }

    public void setPlayButton(JButton playButton) {
        this.playButton = playButton;
    }

    public JButton getStopButton() {
        return stopButton;
    }

    public void setStopButton(JButton stopButton) {
        this.stopButton = stopButton;
    }

    public JButton getSelectButton() {
        return selectButton;
    }

    public void setSelectButton(JButton selectButton) {
        this.selectButton = selectButton;
    }

    public DisplayPanel getDisplayPanel() {
        return displayPanel;
    }

    public void setDisplayPanel(DisplayPanel displayPanel) {
        this.displayPanel = displayPanel;
    }

}
