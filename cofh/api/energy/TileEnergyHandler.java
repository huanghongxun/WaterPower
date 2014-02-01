/*    */ package cofh.api.energy;
/*    */ 
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.tileentity.TileEntity;
/*    */ import net.minecraftforge.common.ForgeDirection;
/*    */ 
/*    */ public class TileEnergyHandler extends TileEntity
/*    */   implements IEnergyHandler
/*    */ {
/* 15 */   protected EnergyStorage storage = new EnergyStorage(32000);
/*    */ 
/*    */   public void readFromNBT(NBTTagCompound nbt)
/*    */   {
/* 20 */     super.readFromNBT(nbt);
/* 21 */     this.storage.readFromNBT(nbt);
/*    */   }
/*    */ 
/*    */   public void writeToNBT(NBTTagCompound nbt)
/*    */   {
/* 27 */     super.writeToNBT(nbt);
/* 28 */     this.storage.writeToNBT(nbt);
/*    */   }
/*    */ 
/*    */   public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
/*    */   {
/* 35 */     return this.storage.receiveEnergy(maxReceive, simulate);
/*    */   }
/*    */ 
/*    */   public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
/*    */   {
/* 41 */     return this.storage.extractEnergy(maxExtract, simulate);
/*    */   }
/*    */ 
/*    */   public boolean canInterface(ForgeDirection from)
/*    */   {
/* 47 */     return true;
/*    */   }
/*    */ 
/*    */   public int getEnergyStored(ForgeDirection from)
/*    */   {
/* 53 */     return this.storage.getEnergyStored();
/*    */   }
/*    */ 
/*    */   public int getMaxEnergyStored(ForgeDirection from)
/*    */   {
/* 59 */     return this.storage.getMaxEnergyStored();
/*    */   }
/*    */ }

/* Location:           D:\Java\cofh\
 * Qualified Name:     cofh.api.energy.TileEnergyHandler
 * JD-Core Version:    0.6.0
 */