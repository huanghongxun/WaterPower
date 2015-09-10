package org.jackhuang.watercraft.common.block.machines;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import org.jackhuang.watercraft.client.gui.DefaultGuiIds;
import org.jackhuang.watercraft.common.block.inventory.InventorySlotProcessableGeneric;
import org.jackhuang.watercraft.common.recipe.HashMapRecipeManager;
import org.jackhuang.watercraft.common.recipe.MultiRecipeManager;
import org.jackhuang.watercraft.common.recipe.MyRecipeManager;
import org.jackhuang.watercraft.common.recipe.MyRecipes;
import org.jackhuang.watercraft.integration.MekanismModule;
import org.jackhuang.watercraft.integration.ic2.IndustrialCraftModule;
import org.jackhuang.watercraft.integration.ic2.IndustrialCraftRecipeManager;
import org.jackhuang.watercraft.util.Mods;

public class TileEntityCompressor extends TileEntityStandardWaterMachine {

    public TileEntityCompressor() {
        super(2000, 2 * 20);

        this.inputSlot = new InventorySlotProcessableGeneric(this, "input", 1, MyRecipes.compressor);
    }

    public static void init() {
        MyRecipes.compressor = new MultiRecipeManager();
        if (Mods.IndustrialCraft2.isAvailable)
            ((MultiRecipeManager) MyRecipes.compressor).addRecipeManager(new IndustrialCraftRecipeManager(IndustrialCraftModule.compressor));
    }

    @Override
    public String getInventoryName() {
        return "Compressor";
    }

    @Override
    public int getGuiId() {
        return DefaultGuiIds.get("tileEntityCompressor");
    }

}