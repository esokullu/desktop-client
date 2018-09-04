/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mygroup;

//import java.awt.SystemTray;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;
import edu.stanford.ejalbert.BrowserLauncher;


/**
 *
 * @author esokullu
 */
public class mygroup {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        //Check the SystemTray is supported
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }

        TeenyWeb videoserver = new TeenyWeb(8080,System.getProperty("java.io.tmpdir"));
        videoserver.start();

        final PopupMenu popup = new PopupMenu();

        final TrayIcon trayIcon =
                new TrayIcon(createImage("resources/stanfordlogo.gif", "tray icon"));
        final SystemTray tray = SystemTray.getSystemTray();

        trayIcon.setImageAutoSize(true);

        // Create a popup menu components
        MenuItem aboutItem = new MenuItem("About");
        CheckboxMenuItem cb2 = new CheckboxMenuItem("Sound On/Off");
        Menu displayMenu = new Menu("Options");
        MenuItem errorItem = new MenuItem("Error");
        MenuItem warningItem = new MenuItem("Serve");
        MenuItem infoItem = new MenuItem("Download");
        MenuItem noneItem = new MenuItem("Chat");
        MenuItem exitItem = new MenuItem("Exit");

        //Add components to popup menu
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(cb2);
        //popup.addSeparator();
        popup.add(displayMenu);
        displayMenu.add(errorItem);
        displayMenu.add(warningItem);
        displayMenu.add(infoItem);
        popup.addSeparator();
        popup.add(noneItem);
        popup.addSeparator();
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
            return;
        }

          trayIcon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "This dialog box is run from System Tray");
            }
        });

        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "This dialog box is run from the About menu item");
            }
        });


        cb2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                int cb2Id = e.getStateChange();
                if (cb2Id == ItemEvent.SELECTED){
                    trayIcon.setToolTip("Sun TrayIcon");
                } else {
                    trayIcon.setToolTip(null);
                }
            }
        });

        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MenuItem item = (MenuItem)e.getSource();
                //TrayIcon.MessageType type = null;
                System.out.println(item.getLabel());
                if ("Error".equals(item.getLabel())) {
                    //type = TrayIcon.MessageType.ERROR;
                    trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is an error message", TrayIcon.MessageType.ERROR);

                } else if ("Warning".equals(item.getLabel())) {
                    //type = TrayIcon.MessageType.WARNING;
                    trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is a warning message", TrayIcon.MessageType.WARNING);

                } else if ("Serve".equals(item.getLabel())) {
                    //type = TrayIcon.MessageType.WARNING;
                    

                } else if ("Download".equals(item.getLabel())) {
                    //type = TrayIcon.MessageType.INFO;
                    /*trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is an info message", TrayIcon.MessageType.INFO);*/
                    //new Tracker();
                    //new p2p("/home/esokullu/Downloads/funvideo.torrent");

                } else if ("Browser".equals(item.getLabel())) {
                    //type = TrayIcon.MessageType.NONE;
                    /*trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is an ordinary message", TrayIcon.MessageType.NONE);*/
                    try {
                    BrowserLauncher nb = new BrowserLauncher();
                     nb.openURLinBrowser("http://grou.ps/chat.php?gname=stanford99");
                    } catch(Exception eb) {
                        System.err.println("can't find a browser");
                    }
                }
            }
        };

        errorItem.addActionListener(listener);
        warningItem.addActionListener(listener);
        infoItem.addActionListener(listener);
        noneItem.addActionListener(listener);

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                System.exit(0);
            }
        });


    }

    //Obtain the image URL
    protected static Image createImage(String path, String description) {
        URL imageURL = mygroup.class.getResource(path);

        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }


    }



}
