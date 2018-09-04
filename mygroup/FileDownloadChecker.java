/*    */ package mygroup;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileDownloadChecker
/*    */   implements Runnable
/*    */ {
/*    */   private FileDownloadManager downloadManager;
/*    */   
/*    */   public FileDownloadChecker(FileDownloadManager manager)
/*    */   {
/* 21 */     this.downloadManager = manager;
/*    */   }
/*    */   
/*    */ 
/*    */   public void run()
/*    */   {
/* 27 */     synchronized (new Object())
/*    */     {
/* 29 */       while (!this.downloadManager.isDownloadComplete)
/*    */       {
/*    */         try
/*    */         {
/* 33 */           Thread.currentThread();Thread.sleep(2000L);
/*    */         }
/*    */         catch (InterruptedException ex) {}
/*    */         
/* 37 */         if (checkFileCompletion()) {
/* 38 */           this.downloadManager.isDownloadComplete = true;
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public synchronized boolean checkFileCompletion()
/*    */   {
/* 49 */     int totalsize = 0;
/*    */     
/* 51 */     for (FileSubContent subContent : this.downloadManager.getSUB_CONTENTS())
/*    */     {
/* 53 */       if ((subContent != null) && (subContent.isIsDownloaded()) && (subContent.getContent().length == this.downloadManager.getSUB_SIZE()))
/*    */       {
/* 55 */         totalsize++;
/*    */       }
/*    */     }
/*    */     
/* 59 */     if (totalsize == this.downloadManager.getSUB_CONTENTS().size())
/*    */     {
/* 61 */       System.out.println("FileDownloadChecker: got all of the subParts.");
/* 62 */       return true;
/*    */     }
/*    */     
/*    */ 
/* 66 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/groups/Downloads/MyGroupApp.jar!/mygroup/FileDownloadChecker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */