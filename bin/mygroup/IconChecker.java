/*    */ package mygroup;
/*    */ 
/*    */ import java.awt.TrayIcon;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.net.URISyntaxException;
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
/*    */ public class IconChecker
/*    */   implements Runnable
/*    */ {
/*    */   private mygroup controller;
/*    */   private GroupInfo group;
/*    */   
/*    */   public IconChecker(mygroup controller)
/*    */   {
/* 28 */     this.controller = controller;
/* 29 */     this.group = GroupInfo.getInstance();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void run()
/*    */   {
/*    */     for (;;)
/*    */     {
/*    */       try
/*    */       {
/* 41 */         Thread.currentThread();Thread.sleep(3000L);
/*    */         
/* 43 */         if (isIconChanged())
/*    */         {
/* 45 */           this.group = GroupInfoHandler.readGroupInfo();
/* 46 */           String pat = this.group.getGroupIcon();
/*    */           
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 52 */           this.controller.getTrayIcon().setImage(mygroup.createImage("./resources/" + pat, this.group.getGroupName()));
/*    */           
/*    */ 
/* 55 */           System.out.println("Icon has been updated");
/*    */         }
/*    */       }
/*    */       catch (URISyntaxException ex)
/*    */       {
/* 60 */         System.out.println("Icon Checker:" + ex.getMessage());
/*    */       }
/*    */       catch (InterruptedException ex)
/*    */       {
/* 64 */         System.out.println("Icon Checker:" + ex.getMessage());
/*    */       }
/*    */       catch (FileNotFoundException ex)
/*    */       {
/* 68 */         System.out.println("Icon Checker:" + ex.getMessage());
/*    */       }
/*    */       catch (IOException ex)
/*    */       {
/* 72 */         System.out.println("Icon Checker:" + ex.getMessage());
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public synchronized boolean isIconChanged()
/*    */     throws FileNotFoundException, IOException, URISyntaxException
/*    */   {
/* 80 */     GroupInfo current = GroupInfo.getInstance();
/*    */     
/*    */ 
/* 83 */     HTTPRequest rq = new HTTPRequest();
/* 84 */     if (rq.updateIcon(current)) {
/* 85 */       return true;
/*    */     }
/* 87 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/groups/Downloads/MyGroupApp.jar!/mygroup/IconChecker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */