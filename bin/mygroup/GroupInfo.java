/*    */ package mygroup;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GroupInfo
/*    */ {
/*    */   private String groupName;
/*    */   
/*    */ 
/*    */ 
/*    */   private String groupIcon;
/*    */   
/*    */ 
/*    */   private String groupIconLength;
/*    */   
/*    */ 
/*    */   private String videoServerPort;
/*    */   
/*    */ 
/*    */   private static GroupInfo singleton;
/*    */   
/*    */ 
/*    */ 
/*    */   private GroupInfo()
/*    */   {
/* 27 */     this.groupName = null;
/* 28 */     this.groupIcon = null;
/* 29 */     this.groupIconLength = null;
/* 30 */     this.videoServerPort = null;
/*    */   }
/*    */   
/*    */ 
/*    */   public static GroupInfo getInstance()
/*    */   {
/* 36 */     synchronized (new Object()) {
/* 37 */       if (singleton == null) {
/* 38 */         GroupInfo grp = new GroupInfo();
/* 39 */         singleton = grp;
/*    */       }
/* 41 */       return singleton;
/*    */     }
/*    */   }
/*    */   
/*    */   public String getGroupName() {
/* 46 */     return this.groupName;
/*    */   }
/*    */   
/*    */   public synchronized void setGroupName(String groupName) {
/* 50 */     this.groupName = groupName;
/*    */   }
/*    */   
/*    */   public String getGroupIcon() {
/* 54 */     return this.groupIcon;
/*    */   }
/*    */   
/*    */   public synchronized void setGroupIcon(String groupIcon) {
/* 58 */     this.groupIcon = groupIcon;
/*    */   }
/*    */   
/*    */   public String getGroupIconLength() {
/* 62 */     return this.groupIconLength;
/*    */   }
/*    */   
/*    */   public synchronized void setGroupIconLength(String groupIconLength) {
/* 66 */     this.groupIconLength = groupIconLength;
/*    */   }
/*    */   
/*    */   public String getVideoServerPort() {
/* 70 */     return this.videoServerPort;
/*    */   }
/*    */   
/*    */   public synchronized void setVideoServerPort(String videoServerPort) {
/* 74 */     this.videoServerPort = videoServerPort;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 84 */     return "groupname:" + getGroupName() + System.getProperty("line.separator") + "groupicon:" + getGroupIcon() + System.getProperty("line.separator") + "groupiconlength:" + getGroupIconLength() + System.getProperty("line.separator") + "videoserverport:" + getVideoServerPort();
/*    */   }
/*    */ }


/* Location:              /Users/groups/Downloads/MyGroupApp.jar!/mygroup/GroupInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */