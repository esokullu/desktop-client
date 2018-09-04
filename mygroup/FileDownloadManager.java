/*     */ package mygroup;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileDownloadManager
/*     */   implements Runnable
/*     */ {
/*     */   private String FILE_NAME;
/*     */   private double TOTAL_SIZE;
/*  22 */   private double SUB_SIZE = 65536.0D;
/*  23 */   private final int DEFAULT_PORT = 16312;
/*     */   private ArrayList<GroupUser> PEER_LIST;
/*     */   private ArrayList<FileSubContent> SUB_CONTENTS;
/*     */   private byte[] FILE_CONTENT;
/*     */   private P2PClient p2pClient;
/*     */   protected boolean isDownloadComplete;
/*     */   private ExecutorService THREAD_POOL;
/*  30 */   private int MAX_THREAD_SIZE = 100;
/*     */   
/*     */   public FileDownloadManager(String fileName, int totalSize, ArrayList<GroupUser> peerList, P2PClient p2pClient)
/*     */   {
/*  34 */     this.FILE_NAME = fileName;
/*  35 */     this.TOTAL_SIZE = totalSize;
/*  36 */     this.PEER_LIST = peerList;
/*  37 */     this.isDownloadComplete = false;
/*  38 */     this.FILE_CONTENT = new byte[(int)this.TOTAL_SIZE];
/*  39 */     double subContentCount = Math.ceil(this.TOTAL_SIZE / this.SUB_SIZE);
/*  40 */     this.p2pClient = p2pClient;
/*     */     
/*     */ 
/*  43 */     this.SUB_CONTENTS = new ArrayList();
/*  44 */     for (int i = 0; i < subContentCount; i++)
/*     */     {
/*  46 */       this.SUB_CONTENTS.add(null);
/*     */     }
/*     */     
/*  49 */     this.THREAD_POOL = Executors.newFixedThreadPool(this.MAX_THREAD_SIZE);
/*     */   }
/*     */   
/*     */ 
/*     */   public void run()
/*     */   {
/*  55 */     System.out.println("File Download Manager: Started the FileDownloadChecker");
/*  56 */     Thread t1 = new Thread(new FileDownloadChecker(this));
/*  57 */     t1.start();
/*     */     
/*     */     Iterator i$;
/*     */     GroupUser peer;
/*  61 */     while ((!this.isDownloadComplete) && (!this.p2pClient.getConnection().stopSendingFilePart()))
/*     */     {
/*     */       try
/*     */       {
/*  65 */         Thread.currentThread();Thread.sleep(1000L);
/*     */       }
/*     */       catch (InterruptedException ex) {}
/*     */       
/*  69 */       for (i$ = getPEER_LIST().iterator(); i$.hasNext();) { peer = (GroupUser)i$.next();
/*     */         
/*     */ 
/*  72 */         if (peer.isAvailable())
/*     */         {
/*  74 */           for (FileSubContent subPart : getSUB_CONTENTS())
/*     */           {
/*     */ 
/*  77 */             if (subPart == null)
/*     */             {
/*  79 */               int seek = getSUB_CONTENTS().indexOf(subPart);
/*  80 */               System.out.println("File Download Manager: Started the File Downloader for: " + peer.getUserIP() + ":" + peer.getUserPort());
/*     */               
/*  82 */               Thread t2 = new Thread(new FileDownloader(peer, this, seek));
/*  83 */               this.THREAD_POOL.submit(t2);
/*  84 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  93 */     this.isDownloadComplete = false;
/*  94 */     this.THREAD_POOL.shutdown();
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized String getFILE_NAME()
/*     */   {
/* 100 */     return this.FILE_NAME;
/*     */   }
/*     */   
/*     */   public synchronized void setFILE_NAME(String FILE_NAME) {
/* 104 */     this.FILE_NAME = FILE_NAME;
/*     */   }
/*     */   
/*     */   public synchronized double getTOTAL_SIZE() {
/* 108 */     return this.TOTAL_SIZE;
/*     */   }
/*     */   
/*     */   public synchronized void setTOTAL_SIZE(int TOTAL_SIZE) {
/* 112 */     this.TOTAL_SIZE = TOTAL_SIZE;
/*     */   }
/*     */   
/*     */   public synchronized double getSUB_SIZE() {
/* 116 */     return this.SUB_SIZE;
/*     */   }
/*     */   
/*     */   public synchronized void setSUB_SIZE(int SUB_SIZE) {
/* 120 */     this.SUB_SIZE = SUB_SIZE;
/*     */   }
/*     */   
/*     */   public synchronized int getDEFAULT_PORT() {
/* 124 */     getClass();return 16312;
/*     */   }
/*     */   
/*     */   public synchronized ArrayList<GroupUser> getPEER_LIST() {
/* 128 */     return this.PEER_LIST;
/*     */   }
/*     */   
/*     */   public synchronized void setPEER_LIST(ArrayList<GroupUser> PEER_LIST) {
/* 132 */     this.PEER_LIST = PEER_LIST;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public synchronized ArrayList<FileSubContent> getSUB_CONTENTS()
/*     */   {
/* 139 */     return this.SUB_CONTENTS;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public synchronized void setSUB_CONTENTS(ArrayList<FileSubContent> aSUB_CONTENTS)
/*     */   {
/* 146 */     this.SUB_CONTENTS = aSUB_CONTENTS;
/*     */   }
/*     */   
/*     */   public synchronized byte[] getFILE_CONTENT() {
/* 150 */     return this.FILE_CONTENT;
/*     */   }
/*     */   
/*     */   public synchronized void setFILE_CONTENT(byte[] aFILE_CONTENT) {
/* 154 */     this.FILE_CONTENT = aFILE_CONTENT;
/*     */   }
/*     */ }


/* Location:              /Users/groups/Downloads/MyGroupApp.jar!/mygroup/FileDownloadManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */