package org.jackhuang.watercraft.common.block.machines;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;

import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.client.gui.DefaultGuiIds;
import org.jackhuang.watercraft.common.inventory.InventorySlotProcessableGeneric;
import org.jackhuang.watercraft.common.recipe.MultiRecipeManager;
import org.jackhuang.watercraft.common.recipe.MyRecipes;
import org.jackhuang.watercraft.common.tileentity.TileEntityStandardWaterMachine;
import org.jackhuang.watercraft.util.Mods;
import org.jackhuang.watercraft.util.StackUtil;

public class TileEntityAdvancedCompressor extends TileEntityStandardWaterMachine {

	public TileEntityAdvancedCompressor() {
		super(5000, 64*20);

		/*if(Mods.GregTech.isAvailable) {
			this.inputSlot = new InventorySlotProcessableGreg(this, "input",
					2, MyRecipes.implosion_gt, GT_Recipe_Map.sImplosionRecipes);
		}
		else*/
			this.inputSlot = new InventorySlotProcessableGeneric(this, "input",
					1, MyRecipes.implosion);
	}
	
	public static void init() {
		MyRecipes.implosion = new MultiRecipeManager();
	}

	@Override
	public String getInventoryName() {
		return "AdvancedCompressor";
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
		
		/*if(Mods.GregTech.isAvailable) {
			InventorySlotProcessableGreg greg = ((InventorySlotProcessableGreg)inputSlot);
			if(greg.get(1) == null || greg.get(1).stackSize < 64)
				greg.put(1, StackUtil.copyWithSize(IC2Items.getItem("industrialTnt"), 64));
		}*/
	}

}