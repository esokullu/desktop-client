/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mygroup;

/**
 *
 * @author esokullu
 */
public class p2pchecker extends Thread {
    p2p download;
    public p2pchecker(p2p p2pdownload) {
        download = p2pdownload;
        start();
    }
    public void run() {
        while(!download.dm.isComplete()) {
            try {
                Thread.currentThread().sleep(1000);
            } catch(Exception e) {}
        }
        download.videodownloaded = true;
    }

}