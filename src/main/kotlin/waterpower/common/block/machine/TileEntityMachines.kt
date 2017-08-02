/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.machine

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.world.World
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.oredict.OreDictionary
import waterpower.annotations.HasGui
import waterpower.annotations.Init
import waterpower.annotations.Register
import waterpower.common.block.inventory.InventorySlotProcessableGeneric
import waterpower.common.init.WPBlocks
import waterpower.common.recipe.IRecipeManager
import waterpower.common.recipe.MultiRecipeManager
import waterpower.common.recipe.RecipeManagers
import waterpower.common.recipe.Recipes
import waterpower.integration.Mod
import waterpower.integration.ic2.Ic2Wrapper
import waterpower.integration.ic2.IndustrialCraftModule
import waterpower.util.*

open class TileEntityMachine(consume: Int, period: Int, outputSlotCount: Int, val type: Machines, mgr: IRecipeManager, val _name: String)
    : TileEntityBaseMachine(consume, period, outputSlotCount) {
    init {
        this.inputSlot = InventorySlotProcessableGeneric(this, "input", 1, mgr)
    }

    override fun getBlockState() = super.getBlockState().withProperty(WPBlocks.machine.TYPES, type)!!

    override fun getName() = _name
}

@Register("waterpower.lathe")
@HasGui(guiClass = GuiLathe::class, containerClass = ContainerBaseMachine::class)
class TileEntityLathe : TileEntityMachine(80, 10 * 20, 2, Machines.lathe, RecipeManagers.lathe, "Water-Powered Lathe")

@Register("waterpower.sawmill")
@HasGui(guiClass = GuiSawmill::class, containerClass = ContainerBaseMachine::class)
class TileEntitySawmill : TileEntityMachine(80, 10 * 20, 2, Machines.sawmill, RecipeManagers.sawmill, "Water-Powered Sawmill")

@Register("waterpower.crusher")
@HasGui(guiClass = GuiCrusher::class, containerClass = ContainerBaseMachine::class)
class TileEntityCrusher : TileEntityMachine(80, 10 * 20, 2, Machines.crusher, RecipeManagers.crusher, "Water-Powered Crusher")

@Register("waterpower.cutter")
@HasGui(guiClass = GuiCutter::class, containerClass = ContainerBaseMachine::class)
class TileEntityCutter : TileEntityMachine(8000, 1 * 20, 2, Machines.cutter, RecipeManagers.cutter, "Water-Powered Cutter")

@Register("waterpower.advanced_compressor")
@HasGui(guiClass = GuiAdvCompressor::class, containerClass = ContainerBaseMachine::class)
class TileEntityAdvCompressor : TileEntityMachine(5000, 64 * 20, 2, Machines.advanced_compressor, RecipeManagers.advCompressor, "Water-Powered Advanced Compressor")

@Register("waterpower.compressor")
@HasGui(guiClass = GuiCompressor::class, containerClass = ContainerBaseMachine::class)
class TileEntityCompressor : TileEntityMachine(2000, 2 * 20, 2, Machines.compressor, RecipeManagers.compressor, "Water-Powered Compressor")

@Register("waterpower.centrifuge")
@HasGui(guiClass = GuiCentrifuge::class, containerClass = ContainerCentrifuge::class)
class TileEntityCentrifuge : TileEntityBaseMachine(80, 10 * 20, 4) {

    init {
        this.inputSlot = InventorySlotProcessableGeneric(this, "input", 2, RecipeManagers.centrifuge)
    }

    override fun getBlockState(): IBlockState =
            super.getBlockState().withProperty(WPBlocks.machine.TYPES, Machines.centrifuge)

    override fun getName() = "Water-Powered Centrifuge"
}

@Init(priority = EventPriority.NORMAL)
object TileEntityMachines {
    @JvmStatic
    fun init() {
        RecipeManagers.centrifuge = MultiRecipeManager()

        RecipeManagers.compressor = MultiRecipeManager()
        if (Mod.IndustrialCraft2.isAvailable)
            (RecipeManagers.compressor as MultiRecipeManager).addRecipeManager(Ic2Wrapper(IndustrialCraftModule.getCompressorMachineManager()!!))
        else
            Recipes.compressors += { input: ItemStack, output: ItemStack -> RecipeManagers.compressor.addRecipe(input, output) }

        RecipeManagers.cutter = MultiRecipeManager()
        if (Mod.IndustrialCraft2.isAvailable)
            (RecipeManagers.cutter as MultiRecipeManager).addRecipeManager(Ic2Wrapper(IndustrialCraftModule.getCutterMachineManager()!!))
        else
            Recipes.cutters += { input: ItemStack, output: ItemStack -> RecipeManagers.cutter.addRecipe(input, output) }

        RecipeManagers.lathe = MultiRecipeManager()
        RecipeManagers.advCompressor = MultiRecipeManager()
        RecipeManagers.crusher = MultiRecipeManager()
        if (Mod.IndustrialCraft2.isAvailable)
            (RecipeManagers.crusher as MultiRecipeManager).addRecipeManager(Ic2Wrapper(IndustrialCraftModule.getMaceratorMachineManager()!!))
        else
            Recipes.crushers += { input: ItemStack, output: ItemStack -> RecipeManagers.crusher.addRecipe(input, output) }

        RecipeManagers.sawmill = MultiRecipeManager()
        addAllLogs()
    }

    fun addAllLogs() {
        val tempContainer = object : Container() {
            override fun canInteractWith(player: EntityPlayer): Boolean {
                return false
            }
        }
        val tempCrafting = InventoryCrafting(tempContainer, 3, 3)

        for (i in 1..8)
            tempCrafting.setInventorySlotContents(i, emptyStack)

        val registeredOres = OreDictionary.getOres("logWood")
        for (i in registeredOres.indices) {
            val logEntry = registeredOres[i]

            if (logEntry.itemDamage == 32767) {
                for (j in 0..15) {
                    val log = ItemStack(logEntry.item, 1, j)
                    tempCrafting.setInventorySlotContents(0, log)
                    val resultEntry = findMatchingRecipe(tempCrafting, null)

                    if (resultEntry != null) {
                        val result = resultEntry.copy()
                        result.set(getCount(result) * 3 / 2)
                        RecipeManagers.sawmill.addRecipe(log, result)
                    }
                }
            } else {
                val log = logEntry.copyWithNewCount(1)
                tempCrafting.setInventorySlotContents(0, log)
                val resultEntry = findMatchingRecipe(tempCrafting, null)

                if (resultEntry != null) {
                    val result = resultEntry.copy()
                    result.set(getCount(result) * 3 / 2)
                    RecipeManagers.sawmill.addRecipe(log, result)
                }
            }
        }
    }

    fun findMatchingRecipe(inv: InventoryCrafting, world: World?): ItemStack? {
        val dmgItems = Array<ItemStack>(2, { emptyStack })
        for (i in 0..inv.sizeInventory - 1) {
            if (inv.getStackInSlot(i) != null) {
                if (isStackEmpty(dmgItems[0])) {
                    dmgItems[0] = inv.getStackInSlot(i)
                } else {
                    dmgItems[1] = inv.getStackInSlot(i)
                    break
                }
            }
        }

        if (isStackEmpty(dmgItems[0]))
            return null
        if (!isStackEmpty(dmgItems[1]) && dmgItems[0].item == dmgItems[1].item && getCount(dmgItems[0]) == 1 && getCount(dmgItems[1]) == 1
                && dmgItems[0].item.isRepairable) {
            val theItem = dmgItems[0].getItem()
            val var13 = theItem.maxDamage - dmgItems[0].itemDamage
            val var8 = theItem.maxDamage - dmgItems[1].itemDamage
            val var9 = var13 + var8 + theItem.maxDamage * 5 / 100
            val var10 = maxOf(0, theItem.maxDamage - var9)
            return ItemStack(theItem, 1, var10)
        }

        for (recipe in CraftingManager.REGISTRY.iterator()) {
            if (recipe.matches(inv, world)) {
                return recipe.getCraftingResult(inv)
            }
        }
        return null
    }
}