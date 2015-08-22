/*     */ package cofh.api.energy;
/*     */ 
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ 
/*     */ public class ItemEnergyContainer extends Item
/*     */   implements IEnergyContainerItem
/*     */ {
/*     */   protected int capacity;
/*     */   protected int maxReceive;
/*     */   protected int maxExtract;
/*     */ 
/*     */   public ItemEnergyContainer(int itemID)
/*     */   {
/*  21 */     super(itemID);
/*     */   }
/*     */ 
/*     */   public ItemEnergyContainer(int itemID, int capacity)
/*     */   {
/*  26 */     this(itemID, capacity, capacity, capacity);
/*     */   }
/*     */ 
/*     */   public ItemEnergyContainer(int itemID, int capacity, int maxTransfer)
/*     */   {
/*  31 */     this(itemID, capacity, maxTransfer, maxTransfer);
/*     */   }
/*     */ 
/*     */   public ItemEnergyContainer(int itemID, int capacity, int maxReceive, int maxExtract)
/*     */   {
/*  36 */     super(itemID);
/*  37 */     this.capacity = capacity;
/*  38 */     this.maxReceive = maxReceive;
/*  39 */     this.maxExtract = maxExtract;
/*     */   }
/*     */ 
/*     */   public ItemEnergyContainer setCapacity(int capacity)
/*     */   {
/*  44 */     this.capacity = capacity;
/*  45 */     return this;
/*     */   }
/*     */ 
/*     */   public void setMaxTransfer(int maxTransfer)
/*     */   {
/*  50 */     setMaxReceive(maxTransfer);
/*  51 */     setMaxExtract(maxTransfer);
/*     */   }
/*     */ 
/*     */   public void setMaxReceive(int maxReceive)
/*     */   {
/*  56 */     this.maxReceive = maxReceive;
/*     */   }
/*     */ 
/*     */   public void setMaxExtract(int maxExtract)
/*     */   {
/*  61 */     this.maxExtract = maxExtract;
/*     */   }
/*     */ 
/*     */   public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate)
/*     */   {
/*  68 */     if (container.stackTagCompound == null) {
/*  69 */       container.stackTagCompound = new NBTTagCompound();
/*     */     }
/*  71 */     int energy = container.stackTagCompound.getInteger("Energy");
/*  72 */     int energyReceived = Math.min(this.capacity - energy, Math.min(this.maxReceive, maxReceive));
/*     */ 
/*  74 */     if (!simulate) {
/*  75 */       energy += energyReceived;
/*  76 */       container.stackTagCompound.setInteger("Energy", energy);
/*     */     }
/*  78 */     return energyReceived;
/*     */   }
/*     */ 
/*     */   public int extractEnergy(ItemStack container, int maxExtract, boolean simulate)
/*     */   {
/*  84 */     if ((container.stackTagCompound == null) || (!container.stackTagCompound.hasKey("Energy"))) {
/*  85 */       return 0;
/*     */     }
/*  87 */     int energy = container.stackTagCompound.getInteger("Energy");
/*  88 */     int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
/*     */ 
/*  90 */     if (!simulate) {
/*  91 */       energy -= energyExtracted;
/*  92 */       container.stackTagCompound.setInteger("Energy", energy);
/*     */     }
/*  94 */     return energyExtracted;
/*     */   }
/*     */ 
/*     */   public int getEnergyStored(ItemStack container)
/*     */   {
/* 100 */     if ((container.stackTagCompound == null) || (!container.stackTagCompound.hasKey("Energy"))) {
/* 101 */       return 0;
/*     */     }
/* 103 */     return container.stackTagCompound.getInteger("Energy");
/*     */   }
/*     */ 
/*     */   public int getMaxEnergyStored(ItemStack container)
/*     */   {
/* 109 */     return this.capacity;
/*     */   }
/*     */ }

/* Location:           D:\Java\cofh\
 * Qualified Name:     cofh.api.energy.ItemEnergyContainer
 * JD-Core Version:    0.6.0
 */