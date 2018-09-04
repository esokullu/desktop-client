/*     */ package mygroup;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.URISyntaxException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ class HTTPRequest
/*     */ {
/*     */   private InetAddress HOST;
/*     */   private int PORT;
/*     */   private Socket client;
/*     */   private int timeout;
/*     */   private PrintWriter out;
/*     */   private BufferedReader in;
/*     */   
/*     */   public HTTPRequest()
/*     */     throws UnknownHostException
/*     */   {
/*  42 */     this.HOST = InetAddress.getByName("grou.ps");
/*  43 */     this.PORT = 80;
/*  44 */     this.client = null;
/*  45 */     this.timeout = 5;
/*     */   }
/*     */   
/*     */ 
/*  49 */   public synchronized InetAddress getHost() { return this.HOST; }
/*  50 */   public synchronized void setHost(String host) throws UnknownHostException { this.HOST = InetAddress.getByName(host); }
/*     */   
/*  52 */   public synchronized int getPort() { return this.PORT; }
/*  53 */   public synchronized void setPort(int port) { this.PORT = port; }
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
/*     */   public synchronized boolean updateIcon(GroupInfo group)
/*     */     throws IOException, FileNotFoundException, URISyntaxException
/*     */   {
/*  68 */     String header = "";
/*  69 */     String date = "";
/*  70 */     String server = "";
/*  71 */     String contentLength = "";
/*  72 */     String contentType = "";
/*     */     
/*     */ 
/*  75 */     String link = "wysiwyg_files/GroupLogos/" + group.getGroupName() + "/80.png";
/*  76 */     String request = "HEAD /" + link + " HTTP/1.0\n";
/*  77 */     request = request + "Host: " + this.HOST.getHostName();
/*     */     
/*     */ 
/*  80 */     this.client = new Socket(this.HOST, this.PORT);
/*     */     
/*     */ 
/*     */ 
/*  84 */     this.out = new PrintWriter(this.client.getOutputStream());
/*  85 */     this.out.write(request);
/*  86 */     this.out.write("\r\n\r\n");
/*  87 */     this.out.flush();
/*     */     
/*     */ 
/*     */ 
/*  91 */     String str = null;
/*  92 */     this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream(), "ISO-8859-1"));
/*  93 */     boolean moreData = true;
/*     */     
/*     */ 
/*     */ 
/*  97 */     while (moreData)
/*     */     {
/*     */       try
/*     */       {
/* 101 */         str = this.in.readLine();
/* 102 */         if (str != null)
/*     */         {
/* 104 */           if (str.startsWith("HTTP/1.1")) {
/* 105 */             header = str;
/* 106 */           } else if (str.startsWith("Date")) {
/* 107 */             date = str;
/* 108 */           } else if (str.startsWith("Server")) {
/* 109 */             server = str;
/* 110 */           } else if (str.startsWith("Content-Length")) {
/* 111 */             contentLength = str.split(" ")[1];
/* 112 */           } else if (str.startsWith("Content-Type: ")) {
/* 113 */             contentType = str;
/*     */           }
/*     */         } else {
/* 116 */           moreData = false;
/*     */         }
/*     */       }
/*     */       catch (InterruptedIOException ex) {
/* 120 */         this.in.close();
/* 121 */         this.out.close();
/* 122 */         this.client.close();
/*     */         
/* 124 */         throw ex;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 130 */     if (contentLength.equals(""))
/*     */     {
/* 132 */       group = GroupInfoHandler.readGroupInfo();
/* 133 */       group.setGroupIcon("groups_logo.gif");
/* 134 */       group.setGroupIconLength("1807");
/* 135 */       GroupInfoHandler.writeGroupInfo(group);
/* 136 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 140 */     if ((!contentLength.equals(group.getGroupIconLength())) && (Integer.parseInt(contentLength) > 0))
/*     */     {
/*     */ 
/* 143 */       request = "GET /" + link + " HTTP/1.1\n";
/* 144 */       request = request + "Host: " + this.HOST.getHostName();
/*     */       
/*     */ 
/* 147 */       this.client = new Socket(this.HOST, this.PORT);
/*     */       
/*     */ 
/* 150 */       this.out = new PrintWriter(this.client.getOutputStream());
/* 151 */       this.out.write(request);
/* 152 */       this.out.write("\r\n\r\n");
/* 153 */       this.out.flush();
/*     */       
/*     */ 
/* 156 */       this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream(), "ISO-8859-1"));
/*     */       
/* 158 */       moreData = true;
/*     */       
/* 160 */       while (moreData)
/*     */       {
/*     */         try
/*     */         {
/* 164 */           str = this.in.readLine();
/* 165 */           if (str != null)
/*     */           {
/* 167 */             if (str.equals(""))
/*     */             {
/* 169 */               int byteReceived = 0;
/*     */               
/* 171 */               int size = Integer.parseInt(contentLength);
/* 172 */               byte[] content = new byte[size];
/* 173 */               if (size > byteReceived) {
/*     */                 int i;
/* 175 */                 while ((i = this.in.read()) != -1)
/*     */                 {
/* 177 */                   content[byteReceived] = ((byte)i);
/*     */                   
/* 179 */                   if (size == ++byteReceived) {
/*     */                     break;
/*     */                   }
/*     */                 }
/* 183 */                 writeIntoFile(content);
/* 184 */                 break;
/*     */               }
/*     */               
/* 187 */               break;
/*     */             }
/*     */           }
/*     */           else {
/* 191 */             moreData = false;
/*     */           }
/*     */         }
/*     */         catch (InterruptedIOException ex) {
/* 195 */           this.in.close();
/* 196 */           this.out.close();
/* 197 */           this.client.close();
/*     */           
/* 199 */           throw ex;
/*     */         }
/*     */       }
/*     */       
/* 203 */       this.in.close();
/* 204 */       this.out.close();
/* 205 */       this.client.close();
/*     */       
/* 207 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 211 */     this.in.close();
/* 212 */     this.out.close();
/* 213 */     this.client.close();
/* 214 */     return false;
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
/*     */   public synchronized byte[] getFileSubContent(String fileName, int seek, double size)
/*     */     throws IOException
/*     */   {
/* 228 */     byte[] content = new byte[(int)size];
/*     */     
/*     */ 
/* 231 */     String link = fileName + ".flv" + ":" + seek;
/* 232 */     String request = "GET /" + link + " HTTP/1.0\n";
/* 233 */     request = request + "Host: " + this.HOST.getHostName();
/*     */     
/* 235 */     BufferedInputStream inStream = null;
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 240 */       this.client = new Socket(this.HOST, this.PORT);
/*     */       
/*     */ 
/*     */ 
/* 244 */       this.out = new PrintWriter(this.client.getOutputStream());
/* 245 */       this.out.write(request);
/* 246 */       this.out.write("\r\n\r\n");
/* 247 */       this.out.flush();
/*     */       
/*     */ 
/* 250 */       inStream = new BufferedInputStream(this.client.getInputStream());
/*     */       
/*     */ 
/* 253 */       inStream.read(content);
/*     */       
/* 255 */       inStream.close();
/* 256 */       this.out.close();
/* 257 */       this.client.close();
/* 258 */       return content;
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/* 262 */       if (inStream != null)
/* 263 */         inStream.close();
/* 264 */       if (this.out != null)
/* 265 */         this.out.close();
/* 266 */       if ((this.client != null) && (!this.client.isClosed())) {
/* 267 */         this.client.close();
/*     */       }
/* 269 */       throw ex;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized byte[] getFileSubContentFromMainServer(String fileName, int seek, double size)
/*     */     throws IOException
/*     */   {
/* 282 */     byte[] content = new byte[(int)size];
/*     */     
/*     */ 
/* 285 */     String link = "includes/emre_tmp/seek.php?id=" + fileName + "&seek=" + seek;
/* 286 */     String request = "GET /" + link + " HTTP/1.0\n";
/* 287 */     request = request + "Host: " + this.HOST.getHostName();
/*     */     
/* 289 */     System.err.println("HTTPREQUEST:REQUEST" + request);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 296 */       this.client = new Socket(this.HOST, this.PORT);
/*     */       
/*     */ 
/*     */ 
/* 300 */       this.out = new PrintWriter(this.client.getOutputStream());
/* 301 */       this.out.write(request);
/* 302 */       this.out.write("\r\n\r\n");
/* 303 */       this.out.flush();
/*     */       
/*     */ 
/*     */ 
/* 307 */       this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream(), "ISO-8859-1"));
/*     */       
/*     */ 
/* 310 */       boolean moreData = true;
/*     */       
/* 312 */       while (moreData) {
/*     */         String str;
/* 314 */         if ((str = this.in.readLine()) != null)
/*     */         {
/* 316 */           if (str.equals(""))
/*     */           {
/* 318 */             int byteReceived = 0;
/*     */             
/* 320 */             int contentSize = content.length;
/* 321 */             if (contentSize <= byteReceived) break;
/*     */             int i;
/* 323 */             while ((i = this.in.read()) != -1)
/*     */             {
/* 325 */               content[byteReceived] = ((byte)i);
/*     */               
/* 327 */               if (contentSize == ++byteReceived)
/*     */               {
/*     */                 break;
/*     */ 
/*     */               }
/*     */               
/*     */             }
/*     */             
/*     */           }
/*     */           
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 341 */           moreData = false;
/*     */         }
/*     */       }
/*     */       
/* 345 */       this.in.close();
/*     */       
/* 347 */       this.out.close();
/* 348 */       this.client.close();
/* 349 */       return content;
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/* 353 */       this.in.close();
/*     */       
/* 355 */       this.out.close();
/* 356 */       this.client.close();
/* 357 */       throw ex;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized FileListInfo getFileListInfo(String fileName)
/*     */     throws IOException
/*     */   {
/* 370 */     FileListInfo fileListInfo = new FileListInfo(fileName + ".flv");
/*     */     
/*     */ 
/*     */ 
/* 374 */     String header = "";
/* 375 */     String date = "";
/* 376 */     String server = "";
/* 377 */     String contentLength = "";
/* 378 */     String contentType = "";
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
/* 391 */     String link = "includes/emre_tmp/filelistinfo.php?id=" + fileName;
/* 392 */     String request = "GET /" + link + " HTTP/1.0\n";
/* 393 */     request = request + "Host: " + this.HOST.getHostName();
/*     */     
/*     */ 
/* 396 */     this.client = new Socket(this.HOST, this.PORT);
/*     */     
/*     */ 
/*     */ 
/* 400 */     this.out = new PrintWriter(this.client.getOutputStream());
/* 401 */     this.out.write(request);
/* 402 */     this.out.write("\r\n\r\n");
/* 403 */     this.out.flush();
/*     */     
/* 405 */     String str = null;
/* 406 */     this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream(), "ISO-8859-1"));
/*     */     
/* 408 */     boolean moreData = true;
/*     */     
/*     */ 
/* 411 */     while (moreData)
/*     */     {
/*     */       try
/*     */       {
/* 415 */         str = this.in.readLine();
/* 416 */         if (str != null)
/*     */         {
/* 418 */           if (str.equals(""))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/* 423 */             while ((str = this.in.readLine()) != null)
/*     */             {
/*     */ 
/* 426 */               if (!str.startsWith("filename="))
/*     */               {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 434 */                 if (str.startsWith("filelength="))
/*     */                 {
/* 436 */                   String[] arrTemp = str.split("=");
/* 437 */                   if (arrTemp.length >= 2)
/*     */                   {
/* 439 */                     if ((arrTemp[1] != null) && (!arrTemp[1].equals("")) && (!arrTemp[1].equals("null")))
/*     */                     {
/* 441 */                       fileListInfo.setFileLength(arrTemp[1]);
/*     */                     }
/*     */                   }
/*     */                 }
/* 445 */                 else if (str.startsWith("users:"))
/*     */                 {
/*     */                   GroupUser gu;
/*     */                   
/*     */ 
/* 450 */                   while ((str = this.in.readLine()) != null)
/*     */                   {
/*     */ 
/*     */ 
/* 454 */                     String[] arrTemp = str.split(":");
/* 455 */                     if (arrTemp.length >= 2)
/*     */                     {
/* 457 */                       if ((arrTemp[0] != null) && (!arrTemp[0].equals("")) && (!arrTemp[0].equals("null")))
/*     */                       {
/* 459 */                         if ((arrTemp[1] != null) && (!arrTemp[1].equals("")) && (!arrTemp[1].equals("null")))
/*     */                         {
/* 461 */                           gu = new GroupUser(arrTemp[0], Integer.parseInt(arrTemp[1]));
/*     */ 
/*     */                         }
/*     */                         
/*     */ 
/*     */                       }
/*     */                       
/*     */ 
/*     */                     }
/*     */                     
/*     */                   }
/*     */                   
/*     */                 }
/*     */                 
/*     */               }
/*     */               
/*     */             }
/*     */             
/*     */           }
/*     */           
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 485 */           moreData = false;
/*     */         }
/*     */       }
/*     */       catch (InterruptedIOException ex) {
/* 489 */         this.in.close();
/* 490 */         this.out.close();
/* 491 */         this.client.close();
/*     */         
/* 493 */         throw ex;
/*     */       }
/*     */     }
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
/* 514 */     fileListInfo.getUserList().add(new GroupUser("98.210.137.151", 16313));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 521 */     fileListInfo.setFileLength("12385032");
/*     */     
/*     */ 
/*     */ 
/* 525 */     this.in.close();
/* 526 */     this.out.close();
/* 527 */     this.client.close();
/*     */     
/* 529 */     return fileListInfo;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void sendIgotTheFile(String fileName, P2PServer p2pServer)
/*     */     throws IOException
/*     */   {
/* 539 */     if ((fileName.startsWith("/")) || (fileName.startsWith("\\"))) {
/* 540 */       fileName = fileName.substring(1);
/*     */     }
/*     */     
/* 543 */     String link = "includes/emre_tmp/igotthefile.php?id=" + fileName + "&port=" + p2pServer.getServerPort();
/* 544 */     String request = "GET /" + link + " HTTP/1.0\n";
/* 545 */     request = request + "Host: " + this.HOST.getHostName();
/*     */     
/*     */ 
/* 548 */     this.client = new Socket(this.HOST, this.PORT);
/*     */     
/*     */ 
/*     */ 
/* 552 */     this.out = new PrintWriter(this.client.getOutputStream());
/* 553 */     this.out.write(request);
/* 554 */     this.out.write("\r\n\r\n");
/* 555 */     this.out.flush();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void sendPortsInfo(int videoServerPort, int p2pServerPort)
/*     */     throws IOException
/*     */   {
/* 568 */     String link = "includes/emre_tmp/ports.php?videoserver=" + videoServerPort + "&p2pserver=" + p2pServerPort;
/* 569 */     String request = "GET /" + link + " HTTP/1.0\n";
/* 570 */     request = request + "Host: " + this.HOST.getHostName();
/*     */     
/*     */ 
/* 573 */     this.client = new Socket(this.HOST, this.PORT);
/*     */     
/*     */ 
/*     */ 
/* 577 */     this.out = new PrintWriter(this.client.getOutputStream());
/* 578 */     this.out.write(request);
/* 579 */     this.out.write("\r\n\r\n");
/* 580 */     this.out.flush();
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
/*     */   public synchronized void writeIntoFile(byte[] content)
/*     */     throws FileNotFoundException, IOException, URISyntaxException
/*     */   {
/* 594 */     if (content != null)
/*     */     {
/* 596 */       GroupInfo grp = GroupInfo.getInstance();
/*     */       
/*     */ 
/* 599 */       String fileName = "./resources";
/*     */       
/*     */ 
/* 602 */       FileOutputStream out = new FileOutputStream(fileName + "/80.png", false);
/*     */       
/* 604 */       for (int i = 0; i < content.length; i++)
/*     */       {
/* 606 */         out.write(content[i]);
/*     */       }
/* 608 */       out.flush();
/* 609 */       out.close();
/*     */       
/* 611 */       grp.setGroupIconLength(Integer.valueOf(content.length).toString());
/* 612 */       if ((grp.getGroupIcon() != null) || (!grp.getGroupIcon().equals("")) || (!grp.getGroupIcon().equals("null")))
/* 613 */         grp.setGroupIcon("80.png");
/* 614 */       GroupInfoHandler.writeGroupInfo(grp);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/groups/Downloads/MyGroupApp.jar!/mygroup/HTTPRequest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */