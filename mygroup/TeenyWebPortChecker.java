/*    */ package mygroup;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.BindException;
/*    */ import java.net.ServerSocket;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TeenyWebPortChecker
/*    */   implements Runnable
/*    */ {
/*    */   private int webPort;
/*    */   private ServerSocket listenSocket;
/*    */   private boolean isPortAvailable;
/*    */   
/*    */   public TeenyWebPortChecker()
/*    */   {
/* 24 */     this.webPort = -1;
/* 25 */     this.listenSocket = null;
/* 26 */     this.isPortAvailable = false;
/*    */   }
/*    */   
/*    */   public TeenyWebPortChecker(ServerSocket listenSocket, int webPort)
/*    */   {
/* 31 */     this();
/* 32 */     this.listenSocket = listenSocket;
/* 33 */     this.webPort = webPort;
/*    */   }
/*    */   
/*    */   public void run()
/*    */   {
/* 38 */     while (!this.isPortAvailable)
/*    */     {
/*    */       try
/*    */       {
/* 42 */         this.listenSocket = new ServerSocket(this.webPort);
/* 43 */         this.isPortAvailable = true;
/*    */       }
/*    */       catch (BindException ex)
/*    */       {
/* 47 */         this.isPortAvailable = false;
/*    */       }
/*    */       catch (IOException ex)
/*    */       {
/* 51 */         this.isPortAvailable = false;
/*    */       }
/*    */       
/*    */ 
/*    */       try
/*    */       {
/* 57 */         Thread.currentThread();Thread.sleep(3000L);
/*    */       }
/*    */       catch (InterruptedException ex) {}
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public synchronized boolean isPortAvailable()
/*    */   {
/* 66 */     return this.isPortAvailable;
/*    */   }
/*    */   
/*    */   public synchronized ServerSocket getServerSocket() {
/* 70 */     return this.listenSocket;
/*    */   }
/*    */ }


/* Location:              /Users/groups/Downloads/MyGroupApp.jar!/mygroup/TeenyWebPortChecker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */