package org.jackhuang.watercraft.common.block.machines;

import java.util.ArrayList;

import org.jackhuang.watercraft.client.gui.DefaultGuiIds;
import org.jackhuang.watercraft.common.block.inventory.InventorySlotProcessableGeneric;
import org.jackhuang.watercraft.common.recipe.MyRecipeInputItemStack;
import org.jackhuang.watercraft.common.recipe.MyRecipeManager;
import org.jackhuang.watercraft.common.recipe.MyRecipes;
import org.jackhuang.watercraft.util.StackUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntitySawmill extends TileEntityStandardWaterMachine {

    public TileEntitySawmill() {
        super(80, 10 * 20);

        this.inputSlot = new InventorySlotProcessableGeneric(this, "input", 1, MyRecipes.sawmill);
    }

    public static void init() {
        MyRecipes.sawmill = new MyRecipeManager();

        addAllLogs();
    }

    public static void addAllLogs() {
        Container tempContainer = new Container() {
            @Override
            public boolean canInteractWith(EntityPlayer entityplayer) {
                return false;
            }
        };
        InventoryCrafting tempCrafting = new InventoryCrafting(tempContainer, 3, 3);
        ArrayList recipeList = (ArrayList) CraftingManager.getInstance().getRecipeList();

        for (int i = 1; i < 9; i++) {
            tempCrafting.setInventorySlotContents(i, null);
        }

        ArrayList registeredOres = OreDictionary.getOres("logWood");
        for (int i = 0; i < registeredOres.size(); i++) {
            ItemStack logEntry = (ItemStack) registeredOres.get(i);

            if (logEntry.getItemDamage() == 32767) {
                for (int j = 0; j < 16; j++) {
                    ItemStack log = new ItemStack(logEntry.getItem(), 1, j);
                    tempCrafting.setInventorySlotContents(0, log);
                    ItemStack resultEntry = findMatchingRecipe(tempCrafting, null);

                    if (resultEntry != null) {
                        ItemStack result = resultEntry.copy();
                        ItemStack tmp144_142 = result;
                        tmp144_142.stackSize = (int) (tmp144_142.stackSize * 1.5F);
                        MyRecipes.sawmill.addRecipe(log, result);
                    }
                }
            } else {
                ItemStack log = StackUtil.copyWithSize(logEntry, 1);
                tempCrafting.setInventorySlotContents(0, log);
                ItemStack resultEntry = findMatchingRecipe(tempCrafting, null);

                if (resultEntry != null) {
                    ItemStack result = resultEntry.copy();
                    ItemStack tmp216_214 = result;
                    tmp216_214.stackSize = (int) (tmp216_214.stackSize * 1.5F);
                    MyRecipes.sawmill.addRecipe(log, result);
                }
            }
        }
    }

    public static ItemStack findMatchingRecipe(InventoryCrafting inv, World world) {
        ItemStack[] dmgItems = new ItemStack[2];
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            if (inv.getStackInSlot(i) != null) {
                if (dmgItems[0] == null) {
                    dmgItems[0] = inv.getStackInSlot(i);
                } else {
                    dmgItems[1] = inv.getStackInSlot(i);
                    break;
                }
            }
        }

        if (dmgItems[0] == null)
            return null;
        if ((dmgItems[1] != null) && (dmgItems[0].getItem().equals(dmgItems[1].getItem())) && (dmgItems[0].stackSize == 1) && (dmgItems[1].stackSize == 1) && (dmgItems[0].getItem().isRepairable())) {
            Item theItem = dmgItems[0].getItem();
            int var13 = theItem.getMaxDamage() - dmgItems[0].getItemDamageForDisplay();
            int var8 = theItem.getMaxDamage() - dmgItems[1].getItemDamageForDisplay();
            int var9 = var13 + var8 + theItem.getMaxDamage() * 5 / 100;
            int var10 = Math.max(0, theItem.getMaxDamage() - var9);
            return new ItemStack(theItem, 1, var10);
        }

        for (int i = 0; i < CraftingManager.getInstance().getRecipeList().size(); i++) {
            IRecipe recipe = (IRecipe) CraftingManager.getInstance().getRecipeList().get(i);

            if (recipe.matches(inv, world)) {
                return recipe.getCraftingResult(inv);
            }
        }
        return null;
    }

    @Override
    public String getInventoryName() {
        return "Sawmill";
    }

    @Override
    public int getGuiId() {
        return DefaultGuiIds.get("tileEntitySawmill");
    }
}
