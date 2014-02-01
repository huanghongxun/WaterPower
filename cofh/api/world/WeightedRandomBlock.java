/*    */ package cofh.api.world;
/*    */ 
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.WeightedRandomItem;
/*    */ 
/*    */ public final class WeightedRandomBlock extends WeightedRandomItem
/*    */ {
/*    */   public final int blockId;
/*    */   public final int metadata;
/*    */ 
/*    */   public WeightedRandomBlock(ItemStack ore)
/*    */   {
/* 19 */     super(100);
/* 20 */     this.blockId = ore.itemID;
/* 21 */     this.metadata = ore.getItemDamage();
/*    */   }
/*    */ 
/*    */   public WeightedRandomBlock(ItemStack ore, int weight)
/*    */   {
/* 26 */     super(weight);
/* 27 */     this.blockId = ore.itemID;
/* 28 */     this.metadata = ore.getItemDamage();
/*    */   }
/*    */ }

/* Location:           D:\Java\cofh\
 * Qualified Name:     cofh.api.world.WeightedRandomBlock
 * JD-Core Version:    0.6.0
 */