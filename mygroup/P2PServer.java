/*     */ package mygroup;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.BindException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import net.sbbi.upnp.impls.InternetGatewayDevice;
/*     */ import net.sbbi.upnp.messages.UPNPResponseException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class P2PServer
/*     */   implements Runnable
/*     */ {
/*  35 */   private int MAX_THREAD_SIZE = 100;
/*     */   
/*     */ 
/*     */ 
/*  39 */   private int serverPort = 16312;
/*     */   
/*  41 */   private String httpRootDir = null;
/*     */   
/*     */ 
/*  44 */   private boolean isPortBind = false;
/*  45 */   private boolean isNatSupported = false;
/*  46 */   private boolean isMapped = false;
/*     */   
/*  48 */   private InternetGatewayDevice firstIGD = null;
/*     */   private ServerSocket listenSocket;
/*     */   private ExecutorService THREAD_POOL;
/*     */   
/*     */   public P2PServer() throws IOException {
/*  53 */     do { int discoveryTimeout = 5000;
/*     */       try
/*     */       {
/*     */         try
/*     */         {
/*  58 */           Thread.currentThread();Thread.sleep(5000L);
/*     */         }
/*     */         catch (InterruptedException ex) {}
/*     */         
/*  62 */         InternetGatewayDevice[] IGDs = InternetGatewayDevice.getDevices(discoveryTimeout);
/*  63 */         if (IGDs != null)
/*     */         {
/*     */ 
/*  66 */           this.isNatSupported = true;
/*     */           
/*  68 */           this.firstIGD = IGDs[0];
/*     */           
/*  70 */           String localHostIP = InetAddress.getLocalHost().getHostAddress();
/*     */           
/*  72 */           this.isMapped = this.firstIGD.addPortMapping("Some mapping description", null, this.serverPort, this.serverPort, localHostIP, 0, "TCP");
/*  73 */           if (this.isMapped)
/*     */           {
/*  75 */             System.out.println("P2PServer: Port" + this.serverPort + "mapped to " + localHostIP);
/*     */           }
/*     */           else
/*     */           {
/*  79 */             this.serverPort += 1;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/*  84 */           this.isNatSupported = false;
/*  85 */           this.isMapped = false;
/*     */         }
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/*  90 */         System.out.println("P2P Server: " + ex.getMessage());
/*  91 */         if (this.isMapped)
/*     */         {
/*     */           try
/*     */           {
/*  95 */             this.firstIGD.deletePortMapping(null, this.serverPort, "TCP");
/*     */           }
/*     */           catch (UPNPResponseException ex2) {}
/*     */         }
/*  99 */         this.isMapped = false;
/* 100 */         this.serverPort += 1;
/*     */       }
/*     */       catch (UPNPResponseException ex)
/*     */       {
/* 104 */         System.out.println("P2P Server: " + ex.getMessage());
/* 105 */         if (this.isMapped)
/*     */         {
/*     */           try
/*     */           {
/* 109 */             this.firstIGD.deletePortMapping(null, this.serverPort, "TCP");
/*     */           }
/*     */           catch (UPNPResponseException ex2) {}
/*     */         }
/* 113 */         this.isMapped = false;
/* 114 */         this.serverPort += 1;
/*     */       }
/*     */       
/*     */ 
/* 118 */       if ((this.isMapped) || (!this.isNatSupported))
/*     */       {
/*     */         try
/*     */         {
/* 122 */           this.listenSocket = new ServerSocket(this.serverPort);
/* 123 */           this.isPortBind = true;
/*     */         }
/*     */         catch (BindException ex)
/*     */         {
/* 127 */           System.out.println("P2P Server: " + ex.getMessage());
/* 128 */           if (this.isMapped)
/*     */           {
/*     */             try
/*     */             {
/* 132 */               this.firstIGD.deletePortMapping(null, this.serverPort, "TCP");
/*     */             }
/*     */             catch (UPNPResponseException ex2) {}
/*     */           }
/* 136 */           this.isMapped = false;
/* 137 */           this.isPortBind = false;
/* 138 */           this.serverPort += 1;
/*     */         }
/*     */         catch (IOException ex)
/*     */         {
/* 142 */           System.out.println("P2P Server: " + ex.getMessage());
/* 143 */           if (this.isMapped)
/*     */           {
/*     */             try
/*     */             {
/* 147 */               this.firstIGD.deletePortMapping(null, this.serverPort, "TCP");
/*     */             }
/*     */             catch (UPNPResponseException ex2) {}
/*     */           }
/* 151 */           this.isMapped = false;
/* 152 */           this.isPortBind = false;
/* 153 */           this.serverPort += 1;
/*     */         }
/*     */       }
/* 156 */     } while (((!this.isMapped) && (!this.isPortBind)) || ((this.isNatSupported) && (!this.isPortBind)));
/*     */     
/* 158 */     System.out.println("P2PServer: Port " + this.serverPort + " is binded successfully Map Info:" + this.isMapped + " SupportInfo: " + this.isNatSupported);
/*     */     
/*     */ 
/* 161 */     this.THREAD_POOL = Executors.newFixedThreadPool(this.MAX_THREAD_SIZE);
/*     */   }
/*     */   
/*     */   public P2PServer(String httpRootDir) throws IOException
/*     */   {
/* 166 */     this();
/* 167 */     this.httpRootDir = httpRootDir;
/*     */   }
/*     */   
/*     */   public void DoUnmapping()
/*     */   {
/* 172 */     if (this.isMapped)
/*     */     {
/* 174 */       if ((this.isMapped) && (this.firstIGD != null))
/*     */       {
/*     */         try
/*     */         {
/* 178 */           this.firstIGD.deletePortMapping(null, this.serverPort, "TCP");
/*     */         }
/*     */         catch (UPNPResponseException ex) {}catch (IOException ex) {}
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 185 */     System.out.println("P2P Server: Unmapping succesfully");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void run()
/*     */   {
/*     */     try
/*     */     {
/*     */       for (;;)
/*     */       {
/* 198 */         System.out.println("P2P Server:Listening on port: " + getServerPort());
/* 199 */         Socket clientSocket = this.listenSocket.accept();
/* 200 */         System.out.println("P2PServer: Connection request received.");
/* 201 */         P2PConnection p2pConnection = new P2PConnection(clientSocket, this.httpRootDir);
/* 202 */         Thread t = new Thread(p2pConnection);
/* 203 */         this.THREAD_POOL.submit(t);
/*     */       }
/*     */     }
/*     */     catch (IOException ex) {
/* 207 */       System.out.println("P2PServer: " + ex.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 213 */   public int getServerPort() { return this.serverPort; }
/* 214 */   public void setServerPort(int serverPort) { this.serverPort = serverPort; }
/* 215 */   public boolean isUpnpSupported() { return this.isNatSupported; }
/*     */ }


/* Location:              /Users/groups/Downloads/MyGroupApp.jar!/mygroup/P2PServer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */