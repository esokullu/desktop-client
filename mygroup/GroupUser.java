/*    */ package mygroup;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.net.InetAddress;
/*    */ import java.net.UnknownHostException;
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
/*    */ public class GroupUser
/*    */ {
/*    */   private InetAddress userIP;
/*    */   private Integer userPort;
/*    */   private boolean isOnline;
/*    */   private boolean isAvailable;
/*    */   
/*    */   public GroupUser()
/*    */   {
/* 26 */     this.userIP = null;
/* 27 */     this.userPort = null;
/* 28 */     this.isOnline = false;
/* 29 */     this.isAvailable = true;
/*    */   }
/*    */   
/*    */   public GroupUser(String userIP, int userPort)
/*    */   {
/* 34 */     this();
/* 35 */     this.userPort = Integer.valueOf(userPort);
/*    */     try
/*    */     {
/* 38 */       this.userIP = InetAddress.getByName(userIP);
/*    */     }
/*    */     catch (UnknownHostException ex)
/*    */     {
/* 42 */       System.out.println("Can get an valid IP for user:" + this.userIP + "\nError:\n" + ex.getMessage());
/*    */       
/* 44 */       this.isAvailable = false;
/*    */     }
/*    */   }
/*    */   
/*    */   public GroupUser(String userIP, int userPort, boolean isOnline)
/*    */   {
/* 50 */     this(userIP, userPort);
/* 51 */     this.isOnline = isOnline;
/*    */   }
/*    */   
/*    */ 
/*    */   public InetAddress getUserIP()
/*    */   {
/* 57 */     return this.userIP;
/*    */   }
/*    */   
/*    */   public void setUserIP(InetAddress userIP) {
/* 61 */     this.userIP = userIP;
/*    */   }
/*    */   
/* 64 */   public int getUserPort() { return this.userPort.intValue(); }
/* 65 */   public void setUserPort(int userPort) { this.userPort = Integer.valueOf(userPort); }
/*    */   
/*    */   public boolean isIsOnline() {
/* 68 */     return this.isOnline;
/*    */   }
/*    */   
/*    */   public void setIsOnline(boolean isOnline) {
/* 72 */     this.isOnline = isOnline;
/*    */   }
/*    */   
/*    */   public boolean isAvailable() {
/* 76 */     return this.isAvailable;
/*    */   }
/*    */   
/*    */   public void setIsAvailable(boolean isAvailable) {
/* 80 */     this.isAvailable = isAvailable;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 90 */     return " userip:" + getUserIP().getHostAddress() + " userPort:" + getUserPort() + " isonline:" + isIsOnline() + " isavailable:" + isAvailable();
/*    */   }
/*    */ }


/* Location:              /Users/groups/Downloads/MyGroupApp.jar!/mygroup/GroupUser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */