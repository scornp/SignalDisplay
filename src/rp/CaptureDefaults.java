package rp;
/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 26-Dec-2005
 * Time: 23:24:37
 * To change this template use File | Settings | File Templates.
 */
public class CaptureDefaults {
    //8000,11025,16000,22050,44100
    // float sampleRate = 44100.0f;
    private float sampleRate;
    //8,16
    private int sampleSizeInBits;
    //1,2
    private int channels;
    //true,false
    private boolean signed;
    //true,false
    private boolean bigEndian;

   public CaptureDefaults() {
        sampleRate = 44100.0f;
        this.sampleSizeInBits = 16;
        channels = 1;
        signed = true;
        bigEndian = false;
    }


    public float getSampleRate() {
        return sampleRate;
    }



    public int getSampleSizeInBits() {
        return sampleSizeInBits;
    }



    public int getChannels() {
        return channels;
    }



    public boolean isSigned() {
        return signed;
    }



    public boolean isBigEndian() {
        return bigEndian;
    }




}
