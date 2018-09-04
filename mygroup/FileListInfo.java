/*    */ package mygroup;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileListInfo
/*    */ {
/*    */   private String fileName;
/*    */   private String fileLength;
/*    */   private ArrayList<GroupUser> userList;
/*    */   
/*    */   public FileListInfo(String fileName)
/*    */   {
/* 25 */     this.fileName = fileName;
/* 26 */     this.fileLength = "0";
/* 27 */     this.userList = new ArrayList();
/*    */   }
/*    */   
/*    */ 
/*    */   public String getFileName()
/*    */   {
/* 33 */     return this.fileName;
/*    */   }
/*    */   
/*    */   public void setFileName(String fileName) {
/* 37 */     this.fileName = fileName;
/*    */   }
/*    */   
/*    */   public String getFileLength() {
/* 41 */     return this.fileLength;
/*    */   }
/*    */   
/*    */   public void setFileLength(String fileLength) {
/* 45 */     this.fileLength = fileLength;
/*    */   }
/*    */   
/*    */   public ArrayList<GroupUser> getUserList() {
/* 49 */     return this.userList;
/*    */   }
/*    */   
/*    */   public void setUserList(ArrayList<GroupUser> userList) {
/* 53 */     this.userList = userList;
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 59 */     StringBuilder str = new StringBuilder();
/*    */     
/* 61 */     str.append("FileListInfo:" + getFileName() + "/" + getFileLength());
/* 62 */     str.append("\nUsers:");
/* 63 */     for (GroupUser user : getUserList())
/*    */     {
/* 65 */       str.append("\n" + user.getUserIP() + ":" + user.getUserPort());
/*    */     }
/* 67 */     return str.toString();
/*    */   }
/*    */ }


/* Location:              /Users/groups/Downloads/MyGroupApp.jar!/mygroup/FileListInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */