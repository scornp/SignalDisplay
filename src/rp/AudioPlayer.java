package rp;

import javax.sound.sampled.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: R Philp
 * Date: 24-Apr-2006
 * Time: 13:38:35
 * A component of the MDAQ project to isolate data chunks
 * that contain meteor data
 */
public class AudioPlayer extends Thread {
    double[] dataArray;           
    int sampleSizeInBits = 16;
    int numberOfBitsPerByte = 8;
    AnalysisPanelComponents analysisPanelComponents;
    int istart;
    private boolean continuePlayoing = false;

    AudioPlayer(double[] dataArray, AnalysisPanelComponents analysisPanelComponents) {
        this.dataArray = dataArray;
        this.analysisPanelComponents = analysisPanelComponents;
    }

    public void pleaseStart() {
        //   continueLoop = true;
        continuePlayoing = true;
        (new Thread(this)).start();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            System.out.println("Can't sleep");
        }
    }

    public void pleaseStop() {
        continuePlayoing = false;
    }

    public void run() {
        //  (new Thread(this)).start();
        play();
    }

    public void play() {

        System.out.println("Now were going to try to play the signal");

        // first we need to convert the signal back into a byte array
        byte[] byteArray;

        byteArray = convertRealtoByte(dataArray);

        SourceDataLine sourceDataLine;

        AudioFormat audioFormat = getAudioFormat();

        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
//      break things down into quarter second blocks

        // todo audio segmentation needs some serious revamping

        int quarterSecondLength = 44100 / 10;
        int numberOfWords = byteArray.length / 2;
        int numberOfQuarterSeconds = numberOfWords / quarterSecondLength;
        int numberOfBlocks = numberOfQuarterSeconds;
        int blockSize = 2 * (numberOfWords / numberOfBlocks);

        double numberOfCylesPerBlock = quarterSecondLength;
        double blockDurationInMills = 1000 / numberOfCylesPerBlock;
        //      int numberOfBlocks = 10;
        //     int blockSize = byteArray.length / numberOfBlocks;

        byte[][] tmpByteArray;
        tmpByteArray = new byte[numberOfBlocks][];
        int count = 0;
        for (int i = 0; i < numberOfBlocks; i++) {
            tmpByteArray[i] = new byte[blockSize];
            for (int j = 0; j < blockSize; j++) {
                tmpByteArray[i][j] = byteArray[count];
                count++;
            }
        }
        int cnt = 0;

        // set up the visual play line
        long lengthOfPlayInMillis = 1000 * numberOfWords / 44100;
        System.out.println("Audio player : length of play is " + lengthOfPlayInMillis);

        GraphSelectionOverlay graphSelectionOverlay = analysisPanelComponents.getTimeSeriesDemo1InMilliSecond().getGraphSelectionOverlay();
  //      graphSelectionOverlay.setLengthOfPlay(lengthOfPlayInMillis - 1);
        graphSelectionOverlay.setNumberOfBlocks(numberOfBlocks);

        try {
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();
            //    sourceDataLine.write(byteArray, 0, byteArray.length);


            graphSelectionOverlay.pleaseStart();
// end visual play

            //todo add please stop

            // put the audio play line here

            for (int i = 0; i < numberOfBlocks && continuePlayoing; i++) {
                //      for(int j = 0; j < tmpByteArray[i].length; j++) tmpByteArray[i][j] *= 3;
                istart = i;
                //   int offSet = i*blockSize;
                graphSelectionOverlay.setPlayLineForBlock(i);

                cnt = sourceDataLine.write(tmpByteArray[i], 0, tmpByteArray[i].length);
            }

            sourceDataLine.drain();
            sourceDataLine.flush();
            sourceDataLine.close();
        } catch (LineUnavailableException e1) {
            e1.printStackTrace();

        }

        graphSelectionOverlay.pleaseStop();
        System.out.println("End of Audio player");
    }

    AudioFormat audioFormat;
    TargetDataLine targetDataLine;

    private void audioInit() {
        // intialize the data capture
        try {
            audioFormat = getAudioFormat();
            System.out.println(" The sample rate is " + audioFormat.getSampleRate());
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();
            System.out.println(targetDataLine.toString());
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    /**
     * getAudioFormat method creates and returns an AudioFormat object for a given set
     * of format parameters. These are set above by default bu may be changed in testing
     *
     * @return AudioFormat
     */
    private AudioFormat getAudioFormat() {
        CaptureDefaults captureDefaults = new CaptureDefaults();

        return new AudioFormat(
                captureDefaults.getSampleRate(),
                captureDefaults.getSampleSizeInBits(),
                captureDefaults.getChannels(),
                captureDefaults.isSigned(),
                captureDefaults.isBigEndian());
    }


    private byte[] convertRealtoByte(double[] tempFloatBuffer) {
        byte[] tempByteBuffer;
        int sizeOfFloatBuffer;
        sizeOfFloatBuffer = tempFloatBuffer.length;
        int[] tempIntBuffer;
        tempIntBuffer = new int[sizeOfFloatBuffer];

        // convert floats to ints
        for (int i = 0; i < sizeOfFloatBuffer; i++)
            tempIntBuffer[i] = (int) (tempFloatBuffer[i] * 32768.0F);

        // allocate bye array
        int sizeOfByteBuffer = tempIntBuffer.length * (sampleSizeInBits / numberOfBitsPerByte);
        tempByteBuffer = new byte[sizeOfByteBuffer];

        // put the floats(int array) into the byte array
        for (int i = 0; i < sizeOfFloatBuffer; i++) {
            tempByteBuffer[2 * i + 0] = (byte) (tempIntBuffer[i] & 0xFF);
            tempByteBuffer[2 * i + 1] = (byte) ((tempIntBuffer[i] >> 8) & 0xFF);
        }

        return tempByteBuffer;
    }


    /**
     * Convert the byte buffer into floats
     *
     * @param tempBuffer
     * @return float []
     */
    private float[] convertByteToReal(byte[] tempBuffer) {
        float sample = 0;
        int j = 0;
        int sizeOfIntArray = tempBuffer.length / 2;
        int intArray[];
        intArray = new int[sizeOfIntArray];
        float floatArray[];
        floatArray = new float[sizeOfIntArray];

        System.out.println("PlayButtonActionListener : convertByteToReal WARNING \n" +
                "WARNING \n" +
                "DO NOT USE THIS ROUTINE: convertByteToReal");
        System.out.println("Size of buffers " + tempBuffer.length + " " +
                sizeOfIntArray);

        for (int i = 0; i < sizeOfIntArray; i++) {
            intArray[i] = 0;

            if (sampleSizeInBits == 16) {
                intArray[i] = (tempBuffer[j + 0] & 0xFF)
                        | (tempBuffer[j + 1] << 8);

                //  printBits(intArray[i], 0, 32);
                sample = intArray[i] / 32768.0F;
                j = j + 2;
                //     if (i % 1000 == 0) signal.put(sample);
            } else if (sampleSizeInBits == 24) {
                intArray[i] = (tempBuffer[j + 0] & 0xFF)
                        | ((tempBuffer[j + 1] & 0xFF) << 8)
                        | (tempBuffer[j + 2] << 16);
                sample = intArray[i] / 8388606.0F;
                j = j + 3;
            }

            floatArray[i] = sample;
        }
        return floatArray;
    }
}
