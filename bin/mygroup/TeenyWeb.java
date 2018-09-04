/*    */ package mygroup;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.net.BindException;
/*    */ import java.net.ServerSocket;
/*    */ import java.net.Socket;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TeenyWeb
/*    */   implements Runnable
/*    */ {
/*    */   private ServerSocket listenSocket;
/*    */   private String httpRootDir;
/* 20 */   private int videoServerPort = 16365;
/*    */   
/*    */ 
/*    */ 
/*    */   private TeenyWebPortChecker webPortChecker;
/*    */   
/*    */ 
/*    */ 
/*    */   public TeenyWeb()
/*    */   {
/*    */     try
/*    */     {
/* 32 */       this.listenSocket = new ServerSocket(this.videoServerPort);
/*    */     }
/*    */     catch (BindException ex)
/*    */     {
/* 36 */       this.webPortChecker = new TeenyWebPortChecker(this.listenSocket, this.videoServerPort);
/* 37 */       Thread t = new Thread(this.webPortChecker);
/* 38 */       t.start();
/*    */     }
/*    */     catch (IOException ex) {}
/*    */   }
/*    */   
/*    */   public TeenyWeb(String httpRoot)
/*    */   {
/* 45 */     this();
/* 46 */     this.httpRootDir = httpRoot;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void run()
/*    */   {
/*    */     try
/*    */     {
/*    */       Connection c;
/*    */       
/*    */ 
/*    */       for (;;)
/*    */       {
/* 61 */         if ((this.webPortChecker == null) || (this.webPortChecker.isPortAvailable()))
/*    */         {
/*    */ 
/* 64 */           System.out.println("Teeny Web: Listening on port: " + this.videoServerPort);
/* 65 */           if (getListenSocket() == null) {
/* 66 */             setListenSocket(this.webPortChecker.getServerSocket());
/*    */           }
/* 68 */           Socket client_socket = getListenSocket().accept();
/* 69 */           System.out.println("TeenyWeb: Connection request received.");
/* 70 */           c = new Connection(client_socket, getHttpRootDir());
/*    */         }
/*    */       }
/* 73 */     } catch (IOException e) { System.err.println(e);
/*    */     }
/*    */   }
/*    */   
/*    */   public ServerSocket getListenSocket() {
/* 78 */     return this.listenSocket;
/*    */   }
/*    */   
/*    */   public synchronized void setListenSocket(ServerSocket listenSocket) {
/* 82 */     this.listenSocket = listenSocket;
/*    */   }
/*    */   
/*    */   public String getHttpRootDir() {
/* 86 */     return this.httpRootDir;
/*    */   }
/*    */   
/*    */   public synchronized void setHttpRootDir(String httpRootDir) {
/* 90 */     this.httpRootDir = httpRootDir;
/*    */   }
/*    */   
/*    */   public int getVideoServerPort() {
/* 94 */     return this.videoServerPort;
/*    */   }
/*    */   
/*    */   public synchronized void setVideoServerPort(int videoServerPort) {
/* 98 */     this.videoServerPort = videoServerPort;
/*    */   }
/*    */ }


/* Location:              /Users/groups/Downloads/MyGroupApp.jar!/mygroup/TeenyWeb.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */