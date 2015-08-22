/*    */ package cofh.api.tileentity;
/*    */ 
/*    */ public abstract interface ISecureTile
/*    */ {
/*    */   public abstract boolean setAccess(AccessMode paramAccessMode);
/*    */ 
/*    */   public abstract AccessMode getAccess();
/*    */ 
/*    */   public abstract boolean setOwnerName(String paramString);
/*    */ 
/*    */   public abstract String getOwnerName();
/*    */ 
/*    */   public abstract boolean canPlayerAccess(String paramString);
/*    */ 
/*    */   public static enum AccessMode
/*    */   {
/*  6 */     PUBLIC, RESTRICTED, PRIVATE;
/*    */ 
/*    */     public boolean isPublic()
/*    */     {
/* 10 */       return this == PUBLIC;
/*    */     }
/*    */ 
/*    */     public boolean isRestricted()
/*    */     {
/* 15 */       return this == RESTRICTED;
/*    */     }
/*    */ 
/*    */     public boolean isPrivate()
/*    */     {
/* 20 */       return this == PRIVATE;
/*    */     }
/*    */ 
/*    */     public static AccessMode stepForward(AccessMode curAccess)
/*    */     {
/* 25 */       return curAccess == PRIVATE ? PUBLIC : curAccess == PUBLIC ? RESTRICTED : PRIVATE;
/*    */     }
/*    */ 
/*    */     public static AccessMode stepBackward(AccessMode curAccess)
/*    */     {
/* 30 */       return curAccess == PRIVATE ? RESTRICTED : curAccess == PUBLIC ? PRIVATE : PUBLIC;
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\Java\cofh\
 * Qualified Name:     cofh.api.tileentity.ISecureTile
 * JD-Core Version:    0.6.0
 */