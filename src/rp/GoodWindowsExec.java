package rp;

import java.util.*;
import java.io.*;

/**
 * Used for handling IO streams
 */

class StreamGobbler extends Thread {
    InputStream is;
    String type;

    StreamGobbler(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null)
                System.out.println(type + ">" + line);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}

public class GoodWindowsExec extends Thread {

    String args;


    GoodWindowsExec(String args) {
        this.args = args;
    }

    //    if (args.length < 1) {
    //      System.out.println("USAGE: java GoodWindowsExec <cmd>");
    //    System.exit(1);
    //   }
    public void run() {


        try {
            String osName = System.getProperty("os.name");
            System.out.println(osName);
            String[] cmd = new String[3];

            if (osName.equals("Windows XP")) {
                cmd[0] = "cmd.exe";
                cmd[1] = "/C";
                cmd[2] = args;
            } else if (osName.equals("Windows 95")) {
                cmd[0] = "command.com";
                cmd[1] = "/C";
                cmd[2] = args;
            }

                         
            Runtime rt = Runtime.getRuntime();

            System.out.println("Execing " + cmd[0] + " " + cmd[1]
                    + " " + cmd[2]);
           
               Process proc;
                   proc = rt.exec(cmd);

            // any error message?
            StreamGobbler errorGobbler = new
                    StreamGobbler(proc.getErrorStream(), "ERROR");

            // any output?
            StreamGobbler outputGobbler = new
                    StreamGobbler(proc.getInputStream(), "OUTPUT");

            // kick them off
            errorGobbler.start();
            outputGobbler.start();

            // any error???
            int exitVal = proc.waitFor();
            System.out.println("ExitValue: " + exitVal);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}

