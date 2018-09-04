/*    */ package mygroup;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileSubContent
/*    */ {
/*    */   private byte[] content;
/*    */   
/*    */ 
/*    */ 
/*    */   private boolean isDownloaded;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public FileSubContent()
/*    */   {
/* 20 */     this.content = null;
/* 21 */     this.isDownloaded = false;
/*    */   }
/*    */   
/*    */   public FileSubContent(int size)
/*    */   {
/* 26 */     this();
/* 27 */     this.content = new byte[size];
/*    */   }
/*    */   
/*    */   public byte[] getContent()
/*    */   {
/* 32 */     return this.content;
/*    */   }
/*    */   
/*    */   void setContent(byte[] content) {
/* 36 */     this.content = content;
/*    */   }
/*    */   
/*    */   public boolean isIsDownloaded() {
/* 40 */     return this.isDownloaded;
/*    */   }
/*    */   
/*    */   public void setIsDownloaded(boolean isDownloaded) {
/* 44 */     this.isDownloaded = isDownloaded;
/*    */   }
/*    */ }


/* Location:              /Users/groups/Downloads/MyGroupApp.jar!/mygroup/FileSubContent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */