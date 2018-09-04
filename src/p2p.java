/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mygroup;

import jBittorrentAPI.*;

/**
 *
 * @author esokullu
 */
public class p2p extends Thread {

    String fname;
    protected boolean videodownloaded = false;
    public DownloadManager dm;
    //Thread wakeme;
    
    public p2p(String f/*, Thread wakemethread*/) {
            //set instance variables from constructor args
            //int servPort = Integer.parseInt(port);
            fname = f;
            //wakeme = wakemethread;
            //this.start();
    }


    /*
     *     private String downloadFile(String fileurl) {

        file tempfile = File.createTempFile("",".flv",System.getProperty("java."))

        java.io.BufferedInputStream in = new java.io.BufferedInputStream(new

        java.net.URL("http://bdonline.sqe.com/documents/testplans.pdf").openStream());
        java.io.FileOutputStream fos = new java.io.FileOutputStream("testplans.pdf");
        java.io.BufferedOutputStream bout = new BufferedOutputStream(fos,1024);
        byte data[] = new byte[1024];
        while(in.read(data,0,1024)>=0)
        {
        bout.write(data);
        }
        bout.close();
        in.close();

        return x;

    }
     */

    public void run(){
        try {
            //synchronized(this) {
                TorrentProcessor tp = new TorrentProcessor();
                TorrentFile t = tp.getTorrentFile(tp.parseTorrent(fname));
                //if(args.length > 1)
                //    Constants.SAVEPATH = args[1];
                if (t != null) {
                    Constants.SAVEPATH = System.getProperty("java.io.tmpdir"); //"/tmp/";
                    dm = new DownloadManager(t, jBittorrentAPI.Utils.generateID());
                    new p2pchecker(this);
                    dm.startListening(6881, 6889); System.err.println("startListening");
                    dm.startTrackerUpdate(); System.err.println("startTrackerUpdate");
                    dm.blockUntilCompletion(); System.err.println("blockUntilCompletion");
                    dm.stopTrackerUpdate(); System.err.println("stopTrackerUpdate");
                    dm.closeTempFiles(); System.err.println("closeTempFiles");
                    
                } else {
                    System.err.println(
                            "Provided file is not a valid torrent file");
                    System.err.flush();
                    System.exit(1);
                }
            //}
        } catch (Exception e) {

            System.out.println("Error while processing torrent file. Please restart the client");
            //e.printStackTrace();
            System.exit(1);
        }

    }

    public boolean isvideodownloaded() {
        return videodownloaded;
    }

}

