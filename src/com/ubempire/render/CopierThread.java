package com.ubempire.render;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: K900
 * Date: 12.06.11
 * Time: 22:27
 * To change this template use File | Settings | File Templates.
 */
public class CopierThread extends Thread {
    private File copyTo;
    private File copyFrom;

    // A snippet I Googled. Laaa-zyy... :P
    public void copy(File sourceLocation , File targetLocation) throws IOException {
        if (!sourceLocation.exists())
        {
             System.out.println("Source files not found! Please check if you have everything set up correctly.");
        }
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copy(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {

            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }

    CopierThread(File copyFrom, File copyTo) {
        super();
        this.copyFrom = copyFrom;
        this.copyTo = copyTo;
    }

    public void run() {
        try {
            System.out.println("Done copying files.");
            copy(copyFrom, copyTo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
