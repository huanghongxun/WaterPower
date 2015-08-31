package org.jackhuang.watercraft.common.block.machines;

import java.util.List;

import ic2.api.recipe.RecipeOutput;

import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.api.IUpgrade;
import org.jackhuang.watercraft.client.gui.IHasGui;
import org.jackhuang.watercraft.common.block.inventory.InventorySlotOutput;
import org.jackhuang.watercraft.common.block.inventory.InventorySlotProcessable;
import org.jackhuang.watercraft.common.block.inventory.InventorySlotUpgrade;
import org.jackhuang.watercraft.common.recipe.MyRecipeOutput;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileEntityStandardWaterMachine extends
        TileEntityWaterMachine implements IHasGui {
    protected short progress = 0;
    public int defaultEnergyConsume;
    public int defaultOperationLength;
    public int defaultEnergyStorage;
    public int energyConsume;
    public int operationLength;
    public int operationsPerTick;
    protected float guiProgress;
    private static final int EventStart = 0;
    private static final int EventInterrupt = 1;
    private static final int EventStop = 2;
    public InventorySlotProcessable inputSlot;
    public final InventorySlotOutput outputSlot;
    public final InventorySlotUpgrade upgradeSlot;
    protected boolean isLastFailed = false;
    protected ItemStack[] lastFailedContents;

    public TileEntityStandardWaterMachine(int waterPerTick, int length) {
        this(waterPerTick, length, 2);
    }

    public TileEntityStandardWaterMachine(int waterPerTick, int length,
            int outputSlots) {
        super(waterPerTick * length);

        this.defaultEnergyConsume = this.energyConsume = waterPerTick;
        this.defaultOperationLength = this.operationLength = length;
        this.defaultEnergyStorage = waterPerTick * length;

        operationsPerTick = 1;

        this.outputSlot = new InventorySlotOutput(this, "output", outputSlots);
        this.upgradeSlot = new InventorySlotUpgrade(this, "upgrade", 4);
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getShort("progress");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("progress", this.progress);
    }

    @Override
    public void onLoaded() {
        if (isServerSide())
            setOverclockRates();

        super.onLoaded();
    }

    @Override
    public void markDirty() {
        super.markDirty();

        if (isServerSide())
            setOverclockRates();
    }

    public float getProgress() {
        return this.guiProgress;
    }

    public void updateEntity() {
        super.updateEntity();

        if (!isServerSide())
            return;

        boolean needsInvUpdate = false;

        if (isLastFailed && inputSlot.isEquals(lastFailedContents))
            ;
        else {
            MyRecipeOutput output = getOutput();

            if ((output != null) && (getFluidAmount() >= this.energyConsume)) {

                if (isLastFailed) {
                    beginProcess(output);
                    isLastFailed = false;
                }

                this.progress = (short) (this.progress + 1);
                this.getFluidTank().drain(energyConsume, true);

                if (this.progress >= this.operationLength) {
                    operate(output);
                    needsInvUpdate = true;
                    this.progress = 0;
                }
            } else {
                if (output == null) {
                    this.progress = 0;
                    isLastFailed = true;
                    lastFailedContents = inputSlot.getCopiedContent();
                }

                // setActive(false);
            }

            float tmp = guiProgress;
            if (isServerSide())
                this.guiProgress = ((float) this.progress / (float) this.operationLength);
            if (Math.abs(tmp - guiProgress) > 0.001 && isServerSide())
                sendUpdateToClient();
        }

        if (needsInvUpdate)
            super.markDirty();
    }

    protected void beginProcess(MyRecipeOutput output) {
    }

    protected void failedProcess() {
    }

    public void operate(MyRecipeOutput output) {
        for (int i = 0; i < this.operationsPerTick; i++) {
            List processResult = output.items;

            operateOnce(output, processResult);

            output = getOutput();
            if (output == null)
                break;
        }
    }

    public void operateOnce(MyRecipeOutput output, List<ItemStack> processResult) {
        this.inputSlot.consume();

        this.outputSlot.add(processResult);
    }

    public void setOverclockRates() {
        int extraProcessTime = 0;
        double processTimeMultiplier = 1.0D;
        int extraEnergyDemand = 0;
        double energyDemandMultiplier = 1.0D;
        int extraEnergyStorage = 0;
        double energyStorageMultiplier = 1.0D;

        for (int i = 0; i < this.upgradeSlot.size(); i++) {
            ItemStack stack = this.upgradeSlot.get(i);

            if ((stack == null) || (!(stack.getItem() instanceof IUpgrade)))
                continue;
            IUpgrade upgrade = (IUpgrade) stack.getItem();

            processTimeMultiplier *= Math.pow(
                    upgrade.getSpeedAdditionalValue(stack), stack.stackSize);
            energyDemandMultiplier *= Math.pow(
                    upgrade.getEnergyDemandMultiplier(stack), stack.stackSize);
            extraEnergyStorage += upgrade.getStorageAdditionalValue(stack)
                    * stack.stackSize;
            energyStorageMultiplier *= Math.pow(1, stack.stackSize);
        }

        double previousProgress = this.progress / this.operationLength;

        double stackOpLen = (this.defaultOperationLength + extraProcessTime)
                * 64.0D * processTimeMultiplier;
        this.operationsPerTick = (int) Math.min(Math.ceil(64.0D / stackOpLen),
                2147483647.0D);
        this.operationLength = (int) Math.round(stackOpLen
                * this.operationsPerTick / 64.0D);

        this.energyConsume = applyModifier(this.defaultEnergyConsume,
                extraEnergyDemand, energyDemandMultiplier);
        this.setFluidTankCapacity(applyModifier(this.defaultEnergyStorage,
                extraEnergyStorage + this.operationLength * this.energyConsume,
                energyStorageMultiplier));

        if (this.operationLength < 1)
            this.operationLength = 1;

        this.progress = (short) (int) Math.floor(previousProgress
                * this.operationLength + 0.1D);
    }

    private int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);

        return ret > 2147483647.0D ? 2147483647 : (int) ret;
    }

    public MyRecipeOutput getOutput() {
        if (this.inputSlot.isEmpty())
            return null;

        MyRecipeOutput output = this.inputSlot.process();
        if (output == null)
            return null;

        if (this.outputSlot.canAdd(output.items)) {
            return output;
        }
        return null;
    }

    public int getEnergy() {
        return this.getFluidAmount();
    }

    public boolean useEnergy(double amount) {
        if (this.getFluidAmount() >= amount) {
            this.getFluidTank().drain((int) amount, true);

            return true;
        }
        return false;
    }

    public int getOutputSize() {
        return this.outputSlot.size();
    }

    public ItemStack getOutput(int index) {
        return this.outputSlot.get(index);
    }

    public void setOutput(int index, ItemStack stack) {
        this.outputSlot.put(index, stack);
    }

    float preGuiProgress = -999;
    short preProgress = -999;
    int preEnergyCusume = -999;

    @Override
    public void readPacketData(NBTTagCompound tag) {
        super.readPacketData(tag);

        if (tag.hasKey("guiProgress"))
            guiProgress = tag.getFloat("guiProgress");
        if (tag.hasKey("progress"))
            progress = tag.getShort("progress");
        if (tag.hasKey("energyConsume"))
            energyConsume = tag.getInteger("energyConsume");
    }

    @Override
    public void writePacketData(NBTTagCompound tag) {
        super.writePacketData(tag);

        if (guiProgress != preGuiProgress) {
            tag.setFloat("guiProgress", guiProgress);
            preGuiProgress = guiProgress;
        }
        if (progress != preProgress) {
            tag.setShort("progress", progress);
            preProgress = progress;
        }
        if (energyConsume != preEnergyCusume) {
            tag.setInteger("energyConsume", energyConsume);
            preEnergyCusume = energyConsume;
        }
    }
}