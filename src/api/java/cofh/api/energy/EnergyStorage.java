/*     */ package cofh.api.energy;
/*     */ 
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ 
/*     */ public class EnergyStorage
/*     */   implements IEnergyStorage
/*     */ {
/*     */   protected int energy;
/*     */   protected int capacity;
/*     */   protected int maxReceive;
/*     */   protected int maxExtract;
/*     */ 
/*     */   public EnergyStorage(int capacity)
/*     */   {
/*  20 */     this(capacity, capacity, capacity);
/*     */   }
/*     */ 
/*     */   public EnergyStorage(int capacity, int maxTransfer)
/*     */   {
/*  25 */     this(capacity, maxTransfer, maxTransfer);
/*     */   }
/*     */ 
/*     */   public EnergyStorage(int capacity, int maxReceive, int maxExtract)
/*     */   {
/*  30 */     this.capacity = capacity;
/*  31 */     this.maxReceive = maxReceive;
/*  32 */     this.maxExtract = maxExtract;
/*     */   }
/*     */ 
/*     */   public EnergyStorage readFromNBT(NBTTagCompound nbt)
/*     */   {
/*  37 */     this.energy = nbt.getInteger("Energy");
/*     */ 
/*  39 */     if (this.energy > this.capacity) {
/*  40 */       this.energy = this.capacity;
/*     */     }
/*  42 */     return this;
/*     */   }
/*     */ 
/*     */   public NBTTagCompound writeToNBT(NBTTagCompound nbt)
/*     */   {
/*  47 */     if (this.energy < 0) {
/*  48 */       this.energy = 0;
/*     */     }
/*  50 */     nbt.setInteger("Energy", this.energy);
/*  51 */     return nbt;
/*     */   }
/*     */ 
/*     */   public void setCapacity(int capacity)
/*     */   {
/*  56 */     this.capacity = capacity;
/*     */ 
/*  58 */     if (this.energy > capacity)
/*  59 */       this.energy = capacity;
/*     */   }
/*     */ 
/*     */   public void setMaxTransfer(int maxTransfer)
/*     */   {
/*  65 */     setMaxReceive(maxTransfer);
/*  66 */     setMaxExtract(maxTransfer);
/*     */   }
/*     */ 
/*     */   public void setMaxReceive(int maxReceive)
/*     */   {
/*  71 */     this.maxReceive = maxReceive;
/*     */   }
/*     */ 
/*     */   public void setMaxExtract(int maxExtract)
/*     */   {
/*  76 */     this.maxExtract = maxExtract;
/*     */   }
/*     */ 
/*     */   public int getMaxReceive()
/*     */   {
/*  81 */     return this.maxReceive;
/*     */   }
/*     */ 
/*     */   public int getMaxExtract()
/*     */   {
/*  86 */     return this.maxExtract;
/*     */   }
/*     */ 
/*     */   public void setEnergyStored(int energy)
/*     */   {
/*  97 */     this.energy = energy;
/*     */ 
/*  99 */     if (this.energy > this.capacity)
/* 100 */       this.energy = this.capacity;
/* 101 */     else if (this.energy < 0)
/* 102 */       this.energy = 0;
/*     */   }
/*     */ 
/*     */   public void modifyEnergyStored(int energy)
/*     */   {
/* 114 */     this.energy += energy;
/*     */ 
/* 116 */     if (this.energy > this.capacity)
/* 117 */       this.energy = this.capacity;
/* 118 */     else if (this.energy < 0)
/* 119 */       this.energy = 0;
/*     */   }
/*     */ 
/*     */   public int receiveEnergy(int maxReceive, boolean simulate)
/*     */   {
/* 127 */     int energyReceived = Math.min(this.capacity - this.energy, Math.min(this.maxReceive, maxReceive));
/*     */ 
/* 129 */     if (!simulate) {
/* 130 */       this.energy += energyReceived;
/*     */     }
/* 132 */     return energyReceived;
/*     */   }
/*     */ 
/*     */   public int extractEnergy(int maxExtract, boolean simulate)
/*     */   {
/* 138 */     int energyExtracted = Math.min(this.energy, Math.min(this.maxExtract, maxExtract));
/*     */ 
/* 140 */     if (!simulate) {
/* 141 */       this.energy -= energyExtracted;
/*     */     }
/* 143 */     return energyExtracted;
/*     */   }
/*     */ 
/*     */   public int getEnergyStored()
/*     */   {
/* 149 */     return this.energy;
/*     */   }
/*     */ 
/*     */   public int getMaxEnergyStored()
/*     */   {
/* 155 */     return this.capacity;
/*     */   }
/*     */ }

/* Location:           D:\Java\cofh\
 * Qualified Name:     cofh.api.energy.EnergyStorage
 * JD-Core Version:    0.6.0
 */