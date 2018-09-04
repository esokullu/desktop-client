/*     */ package mygroup;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.net.Socket;
/*     */ import java.util.ArrayList;
/*     */ import java.util.StringTokenizer;
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
/*     */ class Connection
/*     */   extends Thread
/*     */ {
/*     */   protected Socket client;
/*     */   protected BufferedReader in;
/*     */   protected BufferedOutputStream out;
/*     */   String httpRootDir;
/*     */   String requestedFile;
/*     */   String groupName;
/*     */   protected boolean stopSendingFilePart;
/*     */   
/*     */   public Connection(Socket client_socket, String httpRoot)
/*     */   {
/*  42 */     this.httpRootDir = httpRoot;
/*  43 */     this.client = client_socket;
/*  44 */     this.stopSendingFilePart = false;
/*     */     
/*     */ 
/*     */     try
/*     */     {
/*  49 */       this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
/*  50 */       this.out = new BufferedOutputStream(this.client.getOutputStream());
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/*  54 */       System.err.println(e);
/*  55 */       try { this.client.close();
/*     */       } catch (IOException e2) {}
/*  57 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  62 */     start();
/*     */   }
/*     */   
/*     */ 
/*     */   public void run()
/*     */   {
/*  68 */     String line = null;
/*  69 */     String req = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/*  76 */       req = this.in.readLine();
/*     */       
/*     */ 
/*  79 */       line = req;
/*  80 */       while (line.length() > 0)
/*     */       {
/*  82 */         line = this.in.readLine();
/*     */       }
/*     */       
/*     */ 
/*  86 */       StringTokenizer st = new StringTokenizer(req);
/*     */       
/*  88 */       st.nextToken();
/*  89 */       this.requestedFile = st.nextToken();
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
/* 104 */       System.out.println("Connection: requested file is " + this.requestedFile + ".flv");
/*     */       
/* 106 */       if (this.requestedFile.equals("/crossdomain.xml"))
/*     */       {
/* 108 */         System.out.println("evet");
/* 109 */         sendResponseHeader("text/xml", 0L);
/* 110 */         String crossdomain = "<?xml version=\"1.0\"?>\n<!DOCTYPE cross-domain-policy SYSTEM \"http://www.macromedia.com/xml/dtds/cross-domain-policy.dtd\">\n<cross-domain-policy>\n\t<site-control permitted-cross-domain-policies=\"master-only\"/>\t<allow-access-from domain=\"*\" secure=\"false\" />\n</cross-domain-policy>";
/* 111 */         sendString(crossdomain);
/*     */ 
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/* 118 */         File f = new File(this.httpRootDir + this.requestedFile + ".flv");
/*     */         
/*     */ 
/*     */ 
/* 122 */         if ((!f.canRead()) || (f.isDirectory()))
/*     */         {
/*     */ 
/* 125 */           System.err.println("Connection: in can't read");
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 131 */           synchronized (new Object())
/*     */           {
/* 133 */             System.out.println("Connection: Start P2P Client");
/* 134 */             P2PClient p2pClient = new P2PClient(this.requestedFile, this);
/* 135 */             Thread t = new Thread(p2pClient);
/* 136 */             t.start();
/*     */             
/*     */             long lenght;
/*     */             do
/*     */             {
/* 141 */               lenght = p2pClient.getFileLength();
/*     */             }
/* 143 */             while (lenght < 0L);
/*     */             
/* 145 */             sendResponseHeader("video/x-flv", lenght);
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 155 */             int subPartsent = 0;
/*     */             for (;;)
/*     */             {
/*     */               try
/*     */               {
/* 160 */                 Thread.currentThread();Thread.sleep(500L);
/*     */               }
/*     */               catch (InterruptedException ex) {}
/*     */               
/*     */ 
/*     */ 
/* 166 */               int partBuff = 5;
/*     */               FileSubContent subContent;
/*     */               try
/*     */               {
/* 170 */                 double totalLenght = p2pClient.getFileDownloadManager().getSUB_CONTENTS().size();
/* 171 */                 if (subPartsent + partBuff < totalLenght - 1.0D)
/*     */                 {
/*     */                   try
/*     */                   {
/* 175 */                     FileSubContent tmpContent = (FileSubContent)p2pClient.getFileDownloadManager().getSUB_CONTENTS().get(subPartsent + partBuff);
/*     */                     
/* 177 */                     if ((tmpContent != null) && (tmpContent.getContent() != null) && (tmpContent.isIsDownloaded()) && (tmpContent.getContent().length != p2pClient.getFileDownloadManager().getSUB_SIZE())) {
/*     */                       continue;
/*     */                     }
/*     */                     
/* 181 */                     subContent = (FileSubContent)p2pClient.getFileDownloadManager().getSUB_CONTENTS().get(subPartsent);
/*     */                   }
/*     */                   catch (NullPointerException ex) {}
/* 184 */                   continue;
/*     */                 }
/*     */                 
/*     */ 
/* 188 */                 subContent = (FileSubContent)p2pClient.getFileDownloadManager().getSUB_CONTENTS().get(subPartsent);
/*     */               }
/*     */               catch (NullPointerException ex) {}
/*     */               
/*     */ 
/*     */ 
/* 194 */               continue;
/*     */               
/*     */ 
/*     */ 
/* 198 */               if ((subContent != null) && (subContent.getContent() != null) && (subContent.isIsDownloaded()))
/*     */               {
/*     */ 
/*     */ 
/* 202 */                 byte[] buffer = subContent.getContent();
/*     */                 
/*     */ 
/* 205 */                 if ((buffer != null) && (buffer.length == p2pClient.getFileDownloadManager().getSUB_SIZE()))
/*     */                 {
/*     */ 
/* 208 */                   if (sendBytes(buffer, this.out)) {
/* 209 */                     subPartsent++;
/*     */                   }
/*     */                 }
/*     */                 
/*     */ 
/* 214 */                 if ((subPartsent >= p2pClient.getFileDownloadManager().getSUB_CONTENTS().size()) || (this.stopSendingFilePart)) {
/*     */                   break;
/*     */                 }
/*     */               }
/*     */             }
/*     */             
/*     */ 
/* 221 */             System.err.println("Connection: SubContnets Length:" + p2pClient.getFileDownloadManager().getSUB_CONTENTS().size() + "\nSubPart sent:" + subPartsent);
/*     */             
/*     */ 
/*     */ 
/* 225 */             this.out.flush();
/* 226 */             this.out.close();
/*     */             
/*     */ 
/*     */ 
/* 230 */             if (!this.stopSendingFilePart)
/*     */             {
/* 232 */               FileOutputStream fos = new FileOutputStream(f);
/* 233 */               System.out.println("Connection: File Sending is completed.");
/* 234 */               for (FileSubContent subContent : p2pClient.getFileDownloadManager().getSUB_CONTENTS())
/*     */               {
/* 236 */                 byte[] buffer = subContent.getContent();
/* 237 */                 fos.write(buffer);
/* 238 */                 fos.flush();
/*     */               }
/* 240 */               fos.close();
/*     */               
/* 242 */               if (mygroup.getP2PServer().isUpnpSupported())
/*     */               {
/* 244 */                 System.out.println("Connection: Sending 'IgotTheFile' message to the server.");
/* 245 */                 HTTPRequest httpReq = new HTTPRequest();
/* 246 */                 httpReq.sendIgotTheFile(this.requestedFile, mygroup.getP2PServer());
/*     */               }
/* 248 */               System.out.println("Connection: All operation is finished successfully.");
/*     */             }
/*     */             else
/*     */             {
/* 252 */               System.out.println("Connection: File sending is stopped.");
/*     */             }
/*     */             
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 259 */           sendResponseHeader("video/x-flv", f.length());
/*     */           
/* 261 */           System.out.println("Connection: Start sending file:" + this.requestedFile + " from local.");
/* 262 */           BufferedInputStream fis = new BufferedInputStream(new FileInputStream(f.getPath()));
/*     */           
/*     */ 
/* 265 */           int bufferSize = 65536;
/*     */           
/* 267 */           byte[] buffer = new byte[bufferSize];
/*     */           int bytesRead;
/* 269 */           while ((bytesRead = fis.read(buffer)) != -1)
/*     */           {
/* 271 */             this.out.write(buffer, 0, bytesRead);
/*     */           }
/* 273 */           this.out.flush();
/* 274 */           this.out.close();
/* 275 */           fis.close();
/* 276 */           System.out.println("Connection: All operation is finished successfully.");
/*     */         }
/*     */       }
/*     */       return;
/*     */     } catch (IOException e) {
/* 281 */       System.out.println(e);
/*     */     }
/*     */     finally
/*     */     {
/*     */       try
/*     */       {
/* 287 */         this.out.close();
/* 288 */         this.client.close();
/*     */       }
/*     */       catch (IOException e) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private synchronized boolean sendBytes(byte[] buffer, BufferedOutputStream out)
/*     */   {
/* 302 */     if (buffer != null)
/*     */     {
/*     */       try
/*     */       {
/* 306 */         out.write(buffer);
/* 307 */         return true;
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/* 311 */         this.stopSendingFilePart = true;
/* 312 */         return false;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 317 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void sendResponseHeader(String type, long lenght)
/*     */   {
/* 326 */     String res = "HTTP/1.0 200 OK\nContent-type: " + type + "\n" + "Content-Lenght: " + lenght + "\n\n";
/* 327 */     InputStream resb = new ByteArrayInputStream(res.getBytes());
/* 328 */     byte[] buffer = new byte['က'];
/*     */     try {
/*     */       int bytesRead;
/* 331 */       while ((bytesRead = resb.read(buffer)) != -1)
/* 332 */         this.out.write(buffer, 0, bytesRead);
/*     */     } catch (IOException e) {
/* 334 */       System.out.println(e);
/*     */     }
/*     */   }
/*     */   
/*     */   void sendString(String str)
/*     */   {
/* 340 */     InputStream resb = new ByteArrayInputStream(str.getBytes());
/* 341 */     byte[] buffer = new byte['က'];
/*     */     try {
/*     */       int bytesRead;
/* 344 */       while ((bytesRead = resb.read(buffer)) != -1)
/* 345 */         this.out.write(buffer, 0, bytesRead);
/*     */     } catch (IOException e) {
/* 347 */       System.out.println(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized boolean stopSendingFilePart() {
/* 352 */     return this.stopSendingFilePart;
/*     */   }
/*     */ }


/* Location:              /Users/groups/Downloads/MyGroupApp.jar!/mygroup/Connection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */