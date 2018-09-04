/*     */ package mygroup;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.InetAddress;
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
/*     */ public class FileDownloader
/*     */   implements Runnable
/*     */ {
/*     */   private GroupUser groupUser;
/*     */   private FileDownloadManager downloadManager;
/*     */   private int seek;
/*     */   private FileSubContent content;
/*     */   
/*     */   public FileDownloader(GroupUser peer, FileDownloadManager manager, int seek)
/*     */   {
/*  27 */     this.groupUser = peer;
/*  28 */     this.downloadManager = manager;
/*     */     
/*  30 */     this.content = new FileSubContent();
/*  31 */     this.seek = seek;
/*     */     
/*     */ 
/*  34 */     synchronized (this.downloadManager.getSUB_CONTENTS())
/*     */     {
/*  36 */       this.downloadManager.getSUB_CONTENTS().set(seek, new FileSubContent());
/*     */     }
/*     */   }
/*     */   
/*     */   public void run()
/*     */   {
/*  42 */     synchronized (this.content)
/*     */     {
/*     */       try
/*     */       {
/*  46 */         HTTPRequest rq = new HTTPRequest();
/*  47 */         rq.setHost(this.groupUser.getUserIP().getHostName());
/*  48 */         rq.setPort(this.groupUser.getUserPort());
/*     */         
/*  50 */         if (this.seek != -1)
/*     */         {
/*     */ 
/*  53 */           if ((this.groupUser.getUserIP().getHostName().equals("grou.ps")) && (this.groupUser.getUserPort() == 80))
/*     */           {
/*     */ 
/*  56 */             this.content.setContent(rq.getFileSubContentFromMainServer(this.downloadManager.getFILE_NAME(), this.seek, this.downloadManager.getSUB_SIZE()));
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/*  61 */             this.content.setContent(rq.getFileSubContent(this.downloadManager.getFILE_NAME(), this.seek, this.downloadManager.getSUB_SIZE()));
/*     */           }
/*     */           
/*  64 */           System.out.println("FileDownloader: got the file part:" + this.seek);
/*     */           
/*  66 */           if (this.content.getContent().length == (int)this.downloadManager.getSUB_SIZE())
/*     */           {
/*  68 */             this.content.setIsDownloaded(true);
/*  69 */             synchronized (this.downloadManager.getSUB_CONTENTS())
/*     */             {
/*  71 */               this.downloadManager.getSUB_CONTENTS().set(this.seek, this.content);
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/*  76 */             this.content = null;
/*  77 */             this.groupUser.setIsAvailable(false);
/*  78 */             synchronized (this.downloadManager.getSUB_CONTENTS())
/*     */             {
/*  80 */               this.downloadManager.getSUB_CONTENTS().set(this.seek, null);
/*     */             }
/*     */             
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (UnknownHostException ex)
/*     */       {
/*  88 */         System.out.println("FileDownloader:" + ex.getMessage() + "->GroupUser " + this.groupUser.getUserIP() + " is not available");
/*  89 */         this.content = null;
/*  90 */         this.groupUser.setIsAvailable(false);
/*  91 */         synchronized (this.downloadManager.getSUB_CONTENTS())
/*     */         {
/*  93 */           this.downloadManager.getSUB_CONTENTS().set(this.seek, null);
/*     */         }
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/*  98 */         System.out.println("FileDownloader:" + ex.getMessage() + "->GroupUser " + this.groupUser.getUserIP() + " is not available");
/*  99 */         this.content = null;
/* 100 */         this.groupUser.setIsAvailable(false);
/* 101 */         synchronized (this.downloadManager.getSUB_CONTENTS())
/*     */         {
/* 103 */           this.downloadManager.getSUB_CONTENTS().set(this.seek, null);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/groups/Downloads/MyGroupApp.jar!/mygroup/FileDownloader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */