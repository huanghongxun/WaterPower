package org.jackhuang.watercraft.common.block.machines;

import ic2.api.item.IC2Items;
import ic2.api.recipe.RecipeOutput;
import ic2.api.recipe.Recipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;

import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.api.BasicMachineRecipeManager;
import org.jackhuang.watercraft.api.MyRecipes;
import org.jackhuang.watercraft.client.gui.DefaultGuiIds;
import org.jackhuang.watercraft.common.inventory.InventorySlotProcessableGeneric;
import org.jackhuang.watercraft.common.tileentity.TileEntityStandardWaterMachine;
import org.jackhuang.watercraft.util.StackUtil;

public class TileEntityAdvancedCompressor extends TileEntityStandardWaterMachine {

	public TileEntityAdvancedCompressor() {
		super(5000, 64*20);

		if(WaterPower.isGregTechLoaded) {
			//this.inputSlot = new InventorySlotProcessableGreg(this, "input",
			//		2, MyRecipes.implosion_gt, GT_Recipe.sImplosionRecipes);
		}
		else
			this.inputSlot = new InventorySlotProcessableGeneric(this, "input",
					1, MyRecipes.implosion_gt);
	}
	
	public static void init() {
		MyRecipes.implosion_gt = new BasicMachineRecipeManager();
	}

	@Override
	public String getInventoryName() {
		return "AdvancedCompressor";
	}

	public float getWrenchDropRate() {
		return 1;
	}

	@Override
	public int getGuiId() {
		return DefaultGuiIds.get("tileEntityAdvancedCompressor");
	}
	
	@Override
	public void markDirty() {
		if(!inputSlot.isEmpty()) {
			InventorySlotProcessableGeneric generic = (InventorySlotProcessableGeneric)inputSlot;
			/*if(generic instanceof InventorySlotProcessableGreg) {
				GT_Recipe recipe = ((InventorySlotProcessableGreg)generic).getGTRecipeOutput();
				if(recipe == null) operationLength = 1;
				else defaultOperationLength = recipe.mInputs[1].stackSize * 20;
			}*/
		}
		
		super.markDirty();
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		if(WaterPower.isGregTechLoaded) {
		/*	InventorySlotProcessableGreg greg = ((InventorySlotProcessableGreg)inputSlot);
			if(greg.get(1) == null || greg.get(1).stackSize < 64)
				greg.put(1, StackUtil.copyWithSize(IC2Items.getItem("industrialTnt"), 64));*/
		}
	}

}