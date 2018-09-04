/*     */ package mygroup;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.net.URISyntaxException;
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
/*     */ public class GroupInfoHandler
/*     */ {
/*     */   private static BufferedReader in;
/*     */   private static BufferedWriter out;
/*  28 */   private static String FILE_NAME = "./resources/group.info";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static synchronized int writeGroupInfo(GroupInfo groupInfo)
/*     */     throws IOException, URISyntaxException
/*     */   {
/*  38 */     if (groupInfo != null)
/*     */     {
/*     */ 
/*     */ 
/*  42 */       String filePath = FILE_NAME;
/*  43 */       out = new BufferedWriter(new FileWriter(filePath));
/*  44 */       out.write(groupInfo.toString());
/*  45 */       out.flush();
/*  46 */       out.close();
/*  47 */       return 1;
/*     */     }
/*     */     
/*     */ 
/*  51 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static synchronized GroupInfo readGroupInfo()
/*     */     throws FileNotFoundException, IOException, URISyntaxException
/*     */   {
/*  64 */     GroupInfo grp = GroupInfo.getInstance();
/*     */     
/*     */ 
/*  67 */     String filePath = FILE_NAME;
/*  68 */     in = new BufferedReader(new FileReader(filePath));
/*  69 */     boolean stillRead = true;
/*  70 */     String str = "";
/*     */     
/*  72 */     while (stillRead)
/*     */     {
/*  74 */       if ((str = in.readLine()) != null)
/*     */       {
/*  76 */         if (str.startsWith("groupname:"))
/*     */         {
/*  78 */           if (str.split(":").length > 1) {
/*  79 */             grp.setGroupName(str.split(":")[1]);
/*     */           } else {
/*  81 */             grp.setGroupName("");
/*     */           }
/*  83 */         } else if (str.startsWith("groupicon:"))
/*     */         {
/*  85 */           if (str.split(":").length > 1) {
/*  86 */             grp.setGroupIcon(str.split(":")[1]);
/*     */           } else {
/*  88 */             grp.setGroupIcon("");
/*     */           }
/*  90 */         } else if (str.startsWith("groupiconlength:"))
/*     */         {
/*  92 */           if (str.split(":").length > 1) {
/*  93 */             grp.setGroupIconLength(str.split(":")[1]);
/*     */           } else {
/*  95 */             grp.setGroupIconLength("");
/*     */           }
/*  97 */         } else if (str.startsWith("videoserverport:"))
/*     */         {
/*  99 */           if (str.split(":").length > 1) {
/* 100 */             grp.setVideoServerPort(str.split(":")[1]);
/*     */           } else {
/* 102 */             grp.setVideoServerPort("");
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/* 107 */         stillRead = false;
/*     */       }
/*     */     }
/*     */     
/* 111 */     in.close();
/* 112 */     return grp;
/*     */   }
/*     */ }


/* Location:              /Users/groups/Downloads/MyGroupApp.jar!/mygroup/GroupInfoHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */