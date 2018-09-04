/*     */ package mygroup;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.ArrayList;
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
/*     */ public class P2PClient
/*     */   implements Runnable
/*     */ {
/*     */   private String fileName;
/*     */   private int fileLength;
/*     */   private ArrayList<GroupUser> peerList;
/*     */   private FileDownloadManager downloadManager;
/*     */   private Connection connection;
/*     */   
/*     */   public P2PClient(String fileName, Connection connection)
/*     */   {
/*  28 */     if ((fileName.startsWith("/")) || (fileName.startsWith("\\"))) {
/*  29 */       this.fileName = fileName.substring(1);
/*     */     } else {
/*  31 */       this.fileName = fileName;
/*     */     }
/*  33 */     this.fileLength = -1;
/*  34 */     this.peerList = new ArrayList();
/*  35 */     this.connection = connection;
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized String getFileName()
/*     */   {
/*  41 */     return this.fileName;
/*     */   }
/*     */   
/*     */   public synchronized void setFileName(String fileName) {
/*  45 */     this.fileName = fileName;
/*     */   }
/*     */   
/*     */   public synchronized FileDownloadManager getFileDownloadManager() {
/*  49 */     return this.downloadManager;
/*     */   }
/*     */   
/*     */   public synchronized void setFileDownloadManager(FileDownloadManager manager) {
/*  53 */     this.downloadManager = manager;
/*     */   }
/*     */   
/*     */   public synchronized int getFileLength() {
/*  57 */     return this.fileLength;
/*     */   }
/*     */   
/*     */   public synchronized void setFileLength(int fileLength) {
/*  61 */     this.fileLength = fileLength;
/*     */   }
/*     */   
/*  64 */   public synchronized Connection getConnection() { return this.connection; }
/*     */   
/*     */   public synchronized ArrayList<GroupUser> getPeerList() {
/*  67 */     return this.peerList;
/*     */   }
/*     */   
/*     */   public synchronized void setPeerList(ArrayList<GroupUser> peerList) {
/*  71 */     this.peerList = peerList;
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
/*     */   public void run()
/*     */   {
/*     */     try
/*     */     {
/*  88 */       HTTPRequest req = new HTTPRequest();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  95 */       System.out.println("P2PClient: Started the request for " + this.fileName + " user list info.");
/*  96 */       FileListInfo fileListInfo = req.getFileListInfo(getFileName());
/*  97 */       if ((fileListInfo.getUserList() == null) || (fileListInfo.getUserList().isEmpty()) || (fileListInfo.getFileLength() == null) || (fileListInfo.getFileLength().equals("0")))
/*     */       {
/*     */ 
/* 100 */         System.out.println("P2P Client: got an invalid fileListInfo !!!");
/*     */       }
/*     */       else
/*     */       {
/* 104 */         System.out.println("P2PClient: got the " + fileListInfo.getFileName() + " user list info.");
/*     */       }
/* 106 */       setFileLength(Integer.parseInt(fileListInfo.getFileLength()));
/* 107 */       setPeerList(fileListInfo.getUserList());
/*     */       
/*     */ 
/* 110 */       System.out.println("P2PClient: Started the FileDownload Manager.");
/* 111 */       this.downloadManager = new FileDownloadManager(this.fileName, this.fileLength, this.peerList, this);
/* 112 */       Thread t = new Thread(this.downloadManager);
/* 113 */       t.start();
/*     */     }
/*     */     catch (UnknownHostException ex)
/*     */     {
/* 117 */       System.out.println("P2PClient: " + ex.getMessage());
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/* 121 */       System.out.println("P2PClient: " + ex.getMessage());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/groups/Downloads/MyGroupApp.jar!/mygroup/P2PClient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */