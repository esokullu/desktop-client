/*     */ package mygroup;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.net.Socket;
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
/*     */ public class P2PConnection
/*     */   implements Runnable
/*     */ {
/*     */   protected Socket client;
/*     */   protected BufferedReader in;
/*     */   protected BufferedOutputStream out;
/*     */   String httpRootDir;
/*     */   String requestedFile;
/*  34 */   private int bufferSize = 65536;
/*     */   
/*     */ 
/*  37 */   public static int p2pConnectionValue = -1;
/*     */   
/*     */ 
/*     */   public P2PConnection(Socket clientSocket, String httpRootDir)
/*     */   {
/*  42 */     p2pConnectionValue += 1;
/*     */     
/*     */ 
/*  45 */     this.httpRootDir = httpRootDir;
/*  46 */     this.client = clientSocket;
/*     */     
/*     */ 
/*     */     try
/*     */     {
/*  51 */       this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
/*  52 */       this.out = new BufferedOutputStream(this.client.getOutputStream());
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/*  56 */       System.err.println(e);
/*  57 */       try { this.client.close();
/*     */       } catch (IOException e2) {}
/*  59 */       return;
/*     */     }
/*     */   }
/*     */   
/*     */   public void run()
/*     */   {
/*  65 */     String line = null;
/*  66 */     String req = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/*  73 */       req = this.in.readLine();
/*     */       
/*     */ 
/*  76 */       line = req;
/*  77 */       while (line.length() > 0)
/*     */       {
/*  79 */         line = this.in.readLine();
/*     */       }
/*     */       
/*     */ 
/*  83 */       StringTokenizer st = new StringTokenizer(req);
/*     */       
/*  85 */       st.nextToken();
/*     */       
/*  87 */       this.requestedFile = st.nextToken();
/*  88 */       String[] str = this.requestedFile.split(":");
/*  89 */       int seek = Integer.parseInt(str[1]);
/*  90 */       this.requestedFile = str[0];
/*     */       
/*  92 */       System.out.println("P2PServer: Requested file is: " + this.requestedFile);
/*     */       
/*     */ 
/*  95 */       File f = new File(this.httpRootDir + this.requestedFile);
/*     */       
/*  97 */       synchronized (f)
/*     */       {
/*     */ 
/* 100 */         BufferedInputStream fis = new BufferedInputStream(new FileInputStream(f.getPath()));
/*     */         
/*     */ 
/* 103 */         byte[] buffer = new byte[this.bufferSize];
/*     */         
/* 105 */         for (int i = 0; i < seek; i++)
/*     */         {
/* 107 */           fis.read(buffer);
/*     */         }
/*     */         
/* 110 */         fis.read(buffer);
/*     */         
/*     */ 
/* 113 */         this.out.write(buffer);
/*     */         
/* 115 */         fis.close();
/* 116 */         this.out.flush();
/* 117 */         this.out.close();
/*     */         
/*     */ 
/* 120 */         BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(seek + "_" + p2pConnectionValue + ".txt"));
/* 121 */         writer.write(buffer);
/* 122 */         writer.flush();
/* 123 */         writer.close();
/*     */       }
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/* 128 */       System.out.println("P2PServer: " + ex.getMessage());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/groups/Downloads/MyGroupApp.jar!/mygroup/P2PConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */