/*     */ package mygroup;
/*     */ 
/*     */ import edu.stanford.ejalbert.BrowserLauncher;
/*     */ import java.awt.AWTException;
/*     */ import java.awt.CheckboxMenuItem;
/*     */ import java.awt.Image;
/*     */ import java.awt.Menu;
/*     */ import java.awt.MenuItem;
/*     */ import java.awt.PopupMenu;
/*     */ import java.awt.SystemTray;
/*     */ import java.awt.TrayIcon;
/*     */ import java.awt.TrayIcon.MessageType;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URISyntaxException;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JOptionPane;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class mygroup
/*     */ {
/*     */   private static TeenyWeb videoServer;
/*     */   private static P2PServer p2pServer;
/*     */   final PopupMenu popup;
/*     */   GroupInfo group;
/*     */   final TrayIcon trayIcon;
/*     */   final SystemTray tray;
/*     */   MenuItem aboutItem;
/*     */   CheckboxMenuItem cb2;
/*     */   Menu displayMenu;
/*     */   MenuItem errorItem;
/*     */   MenuItem warningItem;
/*     */   MenuItem infoItem;
/*     */   MenuItem noneItem;
/*     */   MenuItem exitItem;
/*     */   
/*     */   public mygroup()
/*     */   {
/*  48 */     if (!SystemTray.isSupported())
/*     */     {
/*  50 */       System.out.println("Main:SystemTray is not supported");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  55 */     String s = CreateDir();
/*  56 */     if (s != null) {
/*  57 */       System.out.println("Main: Folder check is successful: " + s);
/*     */     } else {
/*  59 */       System.out.println("Main: Folder check is unsuccessful!!!");
/*     */     }
/*     */     
/*  62 */     this.popup = new PopupMenu();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/*  69 */       System.out.println("Main: Starting Video Server.");
/*  70 */       videoServer = new TeenyWeb(CreateDir());
/*  71 */       Thread t1 = new Thread(videoServer);
/*  72 */       t1.start();
/*     */       
/*  74 */       System.out.println("Main: Starting P2P Server.");
/*     */       
/*  76 */       p2pServer = new P2PServer(CreateDir());
/*  77 */       Thread t2 = new Thread(p2pServer);
/*  78 */       t2.start();
/*     */       
/*     */ 
/*  81 */       System.out.println("MyGroup: Sending 'PortInfo' message to the server. VideoServer:" + videoServer.getVideoServerPort() + "P2PServer:" + p2pServer.getServerPort());
/*  82 */       HTTPRequest httpReq = new HTTPRequest();
/*  83 */       httpReq.sendPortsInfo(videoServer.getVideoServerPort(), p2pServer.getServerPort());
/*     */       
/*     */ 
/*  86 */       this.group = GroupInfo.getInstance();
/*  87 */       this.group = GroupInfoHandler.readGroupInfo();
/*  88 */       this.group.setVideoServerPort(Integer.valueOf(videoServer.getVideoServerPort()).toString());
/*  89 */       GroupInfoHandler.writeGroupInfo(this.group);
/*     */     }
/*     */     catch (URISyntaxException ex)
/*     */     {
/*  93 */       System.out.println(ex.getMessage());
/*     */     }
/*     */     catch (FileNotFoundException ex)
/*     */     {
/*  97 */       System.out.println(ex.getMessage());
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/* 101 */       System.out.println(ex.getMessage());
/*     */     }
/*     */     
/*     */ 
/* 105 */     if ((!this.group.getGroupIcon().equals("null")) && (!this.group.getGroupIcon().equals("")) && (this.group.getGroupIcon() != null))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 110 */       this.trayIcon = new TrayIcon(createImage("./resources/" + this.group.getGroupIcon(), this.group.getGroupName()));
/*     */ 
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/*     */ 
/* 118 */       this.trayIcon = new TrayIcon(createImage("./resources/groups_logo.gif", "grou.ps"));
/*     */     }
/*     */     
/*     */ 
/* 122 */     this.tray = SystemTray.getSystemTray();
/*     */     
/* 124 */     this.trayIcon.setImageAutoSize(true);
/*     */     
/*     */ 
/*     */ 
/* 128 */     this.aboutItem = new MenuItem("About");
/* 129 */     this.cb2 = new CheckboxMenuItem("Sound On/Off");
/* 130 */     this.displayMenu = new Menu("Options");
/* 131 */     this.errorItem = new MenuItem("Error");
/* 132 */     this.warningItem = new MenuItem("Serve");
/* 133 */     this.infoItem = new MenuItem("Download");
/* 134 */     this.noneItem = new MenuItem("Chat");
/* 135 */     this.exitItem = new MenuItem("Exit");
/*     */     
/*     */ 
/* 138 */     this.popup.add(this.aboutItem);
/* 139 */     this.popup.addSeparator();
/* 140 */     this.popup.add(this.cb2);
/*     */     
/*     */ 
/* 143 */     this.popup.add(this.displayMenu);
/* 144 */     this.displayMenu.add(this.errorItem);
/* 145 */     this.displayMenu.add(this.warningItem);
/* 146 */     this.displayMenu.add(this.infoItem);
/* 147 */     this.popup.addSeparator();
/* 148 */     this.popup.add(this.noneItem);
/* 149 */     this.popup.addSeparator();
/* 150 */     this.popup.add(this.exitItem);
/*     */     
/* 152 */     this.trayIcon.setPopupMenu(this.popup);
/*     */     
/*     */     try
/*     */     {
/* 156 */       this.tray.add(this.trayIcon);
/*     */     }
/*     */     catch (AWTException e)
/*     */     {
/* 160 */       System.out.println("TrayIcon could not be added.");
/* 161 */       return;
/*     */     }
/*     */     
/*     */ 
/* 165 */     this.trayIcon.addActionListener(new ActionListener()
/*     */     {
/*     */       public void actionPerformed(ActionEvent e) {
/* 168 */         JOptionPane.showMessageDialog(null, "This dialog box is run from System Tray");
/*     */       }
/*     */       
/*     */ 
/* 172 */     });
/* 173 */     this.aboutItem.addActionListener(new ActionListener()
/*     */     {
/*     */       public void actionPerformed(ActionEvent e) {
/* 176 */         JOptionPane.showMessageDialog(null, "This dialog box is run from the About menu item");
/*     */       }
/*     */       
/*     */ 
/* 180 */     });
/* 181 */     this.cb2.addItemListener(new ItemListener()
/*     */     {
/*     */       public void itemStateChanged(ItemEvent e) {
/* 184 */         int cb2Id = e.getStateChange();
/* 185 */         if (cb2Id == 1) {
/* 186 */           mygroup.this.trayIcon.setToolTip("Sun TrayIcon");
/*     */         } else {
/* 188 */           mygroup.this.trayIcon.setToolTip(null);
/*     */         }
/*     */         
/*     */       }
/* 192 */     });
/* 193 */     ActionListener listener = new ActionListener()
/*     */     {
/*     */       public void actionPerformed(ActionEvent e) {
/* 196 */         MenuItem item = (MenuItem)e.getSource();
/*     */         
/* 198 */         System.out.println(item.getLabel());
/*     */         
/* 200 */         if ("Error".equals(item.getLabel()))
/*     */         {
/* 202 */           mygroup.this.trayIcon.displayMessage("Sun TrayIcon Demo", "This is an error message", TrayIcon.MessageType.ERROR);
/*     */         }
/* 204 */         else if ("Warning".equals(item.getLabel()))
/*     */         {
/* 206 */           mygroup.this.trayIcon.displayMessage("Sun TrayIcon Demo", "This is a warning message", TrayIcon.MessageType.WARNING);
/*     */         }
/* 208 */         else if (!"Serve".equals(item.getLabel()))
/*     */         {
/* 210 */           if (!"Download".equals(item.getLabel()))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 216 */             if ("Browser".equals(item.getLabel()))
/*     */             {
/*     */               try
/*     */               {
/*     */ 
/* 221 */                 BrowserLauncher nb = new BrowserLauncher();
/* 222 */                 nb.openURLinBrowser("http://grou.ps/chat.php?gname=stanford99");
/*     */               } catch (Exception eb) {
/* 224 */                 System.err.println("can't find a browser");
/*     */               } }
/*     */           }
/*     */         }
/*     */       }
/* 229 */     };
/* 230 */     this.errorItem.addActionListener(listener);
/* 231 */     this.warningItem.addActionListener(listener);
/* 232 */     this.infoItem.addActionListener(listener);
/* 233 */     this.noneItem.addActionListener(listener);
/*     */     
/* 235 */     this.exitItem.addActionListener(new ActionListener()
/*     */     {
/*     */       public void actionPerformed(ActionEvent e) {
/* 238 */         mygroup.this.tray.remove(mygroup.this.trayIcon);
/*     */         
/* 240 */         mygroup.p2pServer.DoUnmapping();
/* 241 */         System.exit(0);
/*     */       }
/*     */       
/*     */ 
/* 245 */     });
/* 246 */     Thread t = new Thread(new IconChecker(this));
/* 247 */     t.start();
/*     */   }
/*     */   
/*     */ 
/*     */   public TrayIcon getTrayIcon()
/*     */   {
/* 253 */     return this.trayIcon;
/*     */   }
/*     */   
/*     */ 
/* 257 */   public static TeenyWeb getTeenyWebServer() { return videoServer; }
/* 258 */   public static P2PServer getP2PServer() { return p2pServer; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String CreateDir()
/*     */   {
/* 268 */     String parentFolder = ".Groups";
/* 269 */     String childFolder = "Downloads";
/*     */     
/*     */ 
/* 272 */     String userProfilePath = System.getProperty("user.home");
/* 273 */     File f = new File(userProfilePath);
/* 274 */     File fParent = null;
/* 275 */     File fChild = null;
/*     */     
/*     */ 
/* 278 */     if (f.isDirectory())
/*     */     {
/* 280 */       fParent = new File(System.getProperty("user.home") + "/.Groups");
/* 281 */       fChild = new File(System.getProperty("user.home") + "/.Groups/Downloads");
/*     */       
/* 283 */       if (!fParent.isDirectory())
/*     */       {
/* 285 */         fParent.mkdir();
/*     */         
/* 287 */         if (!fChild.isDirectory()) {
/* 288 */           fChild.mkdir();
/*     */         }
/*     */         
/*     */       }
/* 292 */       else if (!fChild.isDirectory()) {
/* 293 */         fChild.mkdir();
/*     */       }
/*     */     }
/*     */     
/* 297 */     if ((fChild != null) && (fChild.isDirectory())) {
/* 298 */       return fChild.getPath() + "/";
/*     */     }
/* 300 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Image createImage(String path, String description)
/*     */   {
/* 319 */     if (path == null)
/*     */     {
/* 321 */       System.err.println("Resource not found: " + path);
/* 322 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 326 */     return new ImageIcon(path, description).getImage();
/*     */   }
/*     */   
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 332 */     new mygroup();
/*     */   }
/*     */ }


/* Location:              /Users/groups/Downloads/MyGroupApp.jar!/mygroup/mygroup.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */