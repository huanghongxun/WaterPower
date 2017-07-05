/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.item

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.LoaderState
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.common.registry.GameRegistry.addShapedRecipe
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.OreDictionary
import waterpower.annotations.Init
import waterpower.annotations.NewInstance
import waterpower.client.i18n
import waterpower.client.render.IIconContainer
import waterpower.client.render.RecolorableTextures
import waterpower.common.init.WPBlocks
import waterpower.common.init.WPItems
import waterpower.common.item.MaterialForms.*
import waterpower.common.recipe.RecipeInputOreDictionary
import waterpower.common.recipe.Recipes
import waterpower.integration.EnderIOModule
import waterpower.integration.Mod
import waterpower.util.isStackEmpty
import waterpower.util.shrink

@Init
@NewInstance(LoaderState.ModState.PREINITIALIZED)
class ItemMaterial() : ItemColorable("material") {

    init {
        WPItems.material = this
        WPItems.items += this
        setHasSubtypes(true)

        for (form in MaterialForms.values())
            for (type in MaterialTypes.values())
                OreDictionary.registerOre(form.name + type.name, getItemStack(type, form))
    }

    fun getItemStack(type: MaterialTypes, form: MaterialForms, amount: Int = 1)
            = ItemStack(this, amount, type.index + form.ordinal)

    fun getTypeFromMeta(meta: Int)
            = MaterialTypes.values()[meta / MaterialTypes.SPACE]

    fun getFormFromMeta(meta: Int)
            = MaterialForms.values()[meta % MaterialTypes.SPACE]

    override fun stopScanning(stack: ItemStack) = false

    override fun getItemStackDisplayName(stack: ItemStack): String {
        val meta = stack.itemDamage
        return i18n("waterpower.material.format")
                .replace("{forms}", getFormFromMeta(meta).getLocalizedName())
                .replace("{material}", getTypeFromMeta(meta).getLocalizedName())
    }

    override fun getUnlocalizedName(stack: ItemStack): String {
        val meta = stack.itemDamage
        return "waterpower.${getTypeFromMeta(meta).name}.${getFormFromMeta(meta).name}"
    }

    override fun getTextureFolder() = "material"

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: NonNullList<ItemStack>) {
        for (form in MaterialForms.values())
            for (type in MaterialTypes.values())
                subItems += getItemStack(type, form)
    }

    @SideOnly(Side.CLIENT)
    override fun getIconContainer(stack: ItemStack): IIconContainer
            = getIconContainers()[getFormFromMeta(stack.itemDamage).ordinal]

    @SideOnly(Side.CLIENT)
    override fun getColorFromItemStack(stack: ItemStack, tintIndex: Int)
            = getTypeFromMeta(stack.itemDamage).color

    @SideOnly(Side.CLIENT)
    override fun getIconContainers() = RecolorableTextures.METAL

    override fun onItemUseFirst(player: EntityPlayer, world: World, pos: BlockPos, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, hand: EnumHand): EnumActionResult {
        var stack = player.getHeldItem(hand)
        if (!isStackEmpty(stack) && getFormFromMeta(stack.itemDamage) == MaterialForms.ingot && getTypeFromMeta(stack.itemDamage) == MaterialTypes.Magnetite) {
            player.inventory.addItemStackToInventory(ItemStack(net.minecraft.init.Items.IRON_INGOT))
            stack = shrink(stack)
        }
        return EnumActionResult.PASS
    }

    companion object {

        fun get(type: MaterialTypes, form: MaterialForms, amount: Int = 1) =
                WPItems.material.getItemStack(type, form, amount)

        @JvmStatic
        fun postInit() {
            if (Mod.EnderIO.isAvailable) {
                EnderIOModule.alloySmelter("Zinc Alloy Dust", get(MaterialTypes.VanadiumSteel, ingot, 3), RecipeInputOreDictionary("ingotVanadium"),
                        RecipeInputOreDictionary("ingotSteel"), RecipeInputOreDictionary("ingotSteel"))
                EnderIOModule.alloySmelter("Neodymium Magnet Dust", get(MaterialTypes.NeodymiumMagnet, ingot, 2), RecipeInputOreDictionary("ingotNeodymium"),
                        RecipeInputOreDictionary("ingotMagnetite"))
                EnderIOModule.alloySmelter("Vanadium Steel Dust", get(MaterialTypes.VanadiumSteel, ingot, 3), RecipeInputOreDictionary("ingotVanadium"),
                        RecipeInputOreDictionary("ingotSteel"), RecipeInputOreDictionary("ingotSteel"))
                EnderIOModule.alloySmelter("Manganese Steel Dust 3", get(MaterialTypes.ManganeseSteel, ingot, 3), RecipeInputOreDictionary("ingotManganese"),
                        RecipeInputOreDictionary("ingotSteel"), RecipeInputOreDictionary("ingotSteel"))
                EnderIOModule.alloySmelter("Manganese Steel Dust 4", get(MaterialTypes.ManganeseSteel, ingot, 4), RecipeInputOreDictionary("ingotManganese"),
                        RecipeInputOreDictionary("ingotSteel"), RecipeInputOreDictionary("ingotSteel"), RecipeInputOreDictionary("ingotCoal"))
            }
            Recipes.craftShapeless(get(MaterialTypes.ZincAlloy, dust, 5), "dustZinc", "dustZinc", "dustZinc", "dustZinc", "dustCopper")
            Recipes.craftShapeless(get(MaterialTypes.VanadiumSteel, dust, 3), "dustVanadium", "dustIron", "dustIron")
            Recipes.craftShapeless(get(MaterialTypes.NeodymiumMagnet, dust, 2), "dustNeodymium", "dustMagnetite")
            Recipes.craftShapeless(get(MaterialTypes.ManganeseSteel, dust, 4), "dustManganese", "dustIron", "dustIron", "dustCoal")
            Recipes.craftShapeless(get(MaterialTypes.ManganeseSteel, dust, 3), "dustManganese", "dustIron", "dustIron")
            val steelIngots = OreDictionary.getOres("ingotSteel")
            var flag = false
            for (stack in steelIngots)
                flag = flag or Recipes.blastFurnace(stack, get(MaterialTypes.IndustrialSteel, ingot), 1000)
            if (!flag) {
                GameRegistry.addSmelting(ItemStack(Items.IRON_INGOT), WPItems.material.getItemStack(MaterialTypes.Steel, MaterialForms.ingot), 0f)
            }

            for (type in MaterialTypes.values()) {
                Recipes.craftShapeless(get(type, dust), get(type, dustSmall), get(type, dustSmall), get(type, dustSmall), get(type, dustSmall))

                Recipes.craftShapeless(get(type, dustSmall, 4), get(type, dust))

                addShapedRecipe(ItemMaterial.get(type, dust), "AAA", "AAA", "AAA", 'A', ItemMaterial.get(type, dustTiny))

                addShapedRecipe(ItemMaterial.get(type, dustTiny, 9), "A", 'A', ItemMaterial.get(type, dust))

                addShapedRecipe(WPBlocks.material.getItemStack(type), "AAA", "AAA", "AAA", 'A', ItemMaterial.get(type, ingot))

                addShapedRecipe(ItemMaterial.get(type, ingot), "AAA", "AAA", "AAA", 'A', ItemMaterial.get(type, nugget))

                Recipes.craftShapeless(get(type, nugget, 9), get(type, ingot))

                addShapedRecipe(ItemMaterial.get(type, stick, 4), "A", "A", 'A', ItemMaterial.get(type, ingot))

                addShapedRecipe(ItemMaterial.get(type, gear), // 4 sticks & 4 plates -> 1 gear
                        "SPS", "P P", "SPS", 'S', ItemMaterial.get(type, stick), 'P', ItemMaterial.get(type, plate))

                addShapedRecipe(ItemMaterial.get(type, ring), // 4 sticks -> 1 ring
                        " S ", "S S", " S ", 'S', ItemMaterial.get(type, stick))

                Recipes.craftShapeless(get(type, ingot, 9), WPBlocks.material.getItemStack(type))

                Recipes.furnace(get(type, dust), get(type, ingot))
                Recipes.furnace(get(type, dustTiny), get(type, nugget))
                Recipes.furnace(get(type, screw), get(type, nugget))
                Recipes.furnace(WPBlocks.material.getItemStack(type), get(type, ingot, 9))

                Recipes.lathe(get(type, stick), get(type, screw, 4))

                if (type !== MaterialTypes.Steel) {
                    Recipes.bender(get(type, ingot), get(type, plate))
                    Recipes.bender(get(type, plate, 9), get(type, plateDense))
                    Recipes.crusher(get(type, screw), get(type, dustTiny)) // no tiny steel dust
                    Recipes.crusher(get(type, nugget), get(type, dustTiny)) // no tiny steel dust

                    Recipes.crusher(get(type, ingot), get(type, dust)) // steel -> iron dust
                    Recipes.crusher(get(type, plate), get(type, dust)) // steel -> iron dust
                    Recipes.crusher(WPBlocks.material.getItemStack(type), get(type, dust, 9)) // steel -> iron dust
                    Recipes.crusher(get(type, plateDense), get(type, dust, 9)) // steel -> iron dust
                    Recipes.crusher(get(type, gear), get(type, dust, 6)) // steel -> iron dust
                    Recipes.crusher(get(type, ring), get(type, dust, 2)) // steel -> iron dust
                }

                Recipes.craftShapeless(get(type, plateDense), ItemStack(WPItems.hammer), WPBlocks.material.getItemStack(type))
            }

            if (!waterpower.integration.Mod.IndustrialCraft2.isAvailable) {
                Recipes.bender(get(MaterialTypes.Steel, ingot), get(MaterialTypes.Steel, plate))
                Recipes.bender(get(MaterialTypes.Steel, plate, 9), get(MaterialTypes.Steel, plateDense))
                Recipes.crusher(get(MaterialTypes.Steel, ingot), WPItems.crafting.getItemStack(EnumCrafting.iron_dust))
                Recipes.crusher(get(MaterialTypes.Steel, plate), WPItems.crafting.getItemStack(EnumCrafting.iron_dust))
                Recipes.crusher(net.minecraft.item.ItemStack(WPBlocks.material, 1, MaterialTypes.Steel.ordinal), WPItems.crafting.getItemStack(EnumCrafting.iron_dust, 9))
                Recipes.crusher(get(MaterialTypes.Steel, plateDense), WPItems.crafting.getItemStack(EnumCrafting.iron_dust, 9))
                Recipes.crusher(get(MaterialTypes.Steel, gear), WPItems.crafting.getItemStack(EnumCrafting.iron_dust, 6))
                Recipes.crusher(get(MaterialTypes.Steel, ring), WPItems.crafting.getItemStack(EnumCrafting.iron_dust, 2))
            }
        }
    }
}