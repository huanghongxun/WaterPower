/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.item

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.LoaderState
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.OreDictionary
import waterpower.annotations.Init
import waterpower.annotations.NewInstance
import waterpower.client.i18n
import waterpower.client.render.IIconContainer
import waterpower.client.render.RecolorableTextures
import waterpower.common.init.WPItems
import waterpower.common.item.EnumComponent.*
import waterpower.common.item.EnumLevel.*
import waterpower.common.recipe.Recipes
import waterpower.common.recipe.Recipes.craft
import waterpower.common.recipe.Recipes.craftShapeless
import waterpower.integration.IDs
import waterpower.integration.Mod
import waterpower.integration.ic2.ICItemFinder
import waterpower.util.emptyStack
import waterpower.util.generalize
import waterpower.util.getItemStack
import java.awt.Color

@Init
@NewInstance(LoaderState.ModState.PREINITIALIZED)
class ItemComponent : ItemColorable("component") {
    init {
        WPItems.component = this
        WPItems.items += this
        setHasSubtypes(true)
    }

    override fun getTextureFolder() = "component"

    fun getItemStack(type: EnumLevel, form: EnumComponent, amount: Int = 1)
            = ItemStack(this, amount, type.index + form.ordinal)

    fun getLevelFromMeta(meta: Int)
            = EnumLevel.values()[meta / EnumLevel.SPACE]

    fun getFormFromMeta(meta: Int)
            = EnumComponent.values()[meta % EnumLevel.SPACE]

    override fun stopScanning(stack: ItemStack) = false

    override fun getItemStackDisplayName(stack: ItemStack): String {
        val meta = stack.itemDamage
        return i18n("waterpower.component.format")
                .replace("{component}", getFormFromMeta(meta).getLocalizedName())
                .replace("{level}", getLevelFromMeta(meta).getLocalizedName())
    }

    override fun getUnlocalizedName(stack: ItemStack): String {
        val meta = stack.itemDamage
        return "waterpower.${getLevelFromMeta(meta).name}.${getFormFromMeta(meta).name}"
    }

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: MutableList<ItemStack>) {
        for (form in EnumComponent.values())
            for (type in EnumLevel.values())
                subItems += getItemStack(type, form)
    }

    @SideOnly(Side.CLIENT)
    override fun getIconContainer(stack: ItemStack): IIconContainer
            = getIconContainers()[getFormFromMeta(stack.itemDamage).ordinal]

    @SideOnly(Side.CLIENT)
    override fun getColorFromItemStack(stack: ItemStack, tintIndex: Int)
            = getLevelFromMeta(stack.itemDamage).color

    @SideOnly(Side.CLIENT)
    override fun getIconContainers() = RecolorableTextures.CRAFTING

    companion object {
        fun get(type: EnumComponent, form: EnumLevel, amount: Int = 1) =
                WPItems.component.getItemStack(form, type, amount)

        @JvmStatic
        fun postInit() {
            OreDictionary.registerOre("circuitBasic", get(circuit, MK1))
            OreDictionary.registerOre("circuitAdvanced", get(circuit, MK3))
            OreDictionary.registerOre("circuitElite", get(circuit, MK5))
            OreDictionary.registerOre("circuitUltimate", get(circuit, MK7))

            val copperCable = getItemStack(IDs.Mekanism, "PartTransmitter", 2) ?: emptyStack
            val goldCable = getItemStack(IDs.Mekanism, "PartTransmitter", 3) ?: emptyStack

            craft(get(stator, MK1), "M", "M", 'M', "dustMagnetite")
            craft(get(casing, MK1), "WSW", "WSW", "WSW", 'W', ItemCrafting.get(EnumCrafting.stone_structure), 'S', Blocks.STONEBRICK)
            addPaddleBaseRecipe(MK1)
            craft(get(drainagePlate, MK1), "WW ", "WWS", "WW ", 'W', "plankWood", 'S', WPItems.hammer)
            craft(get(fixedFrame, MK1), "WWW", "WSW", "WWW", 'W', "logWood", 'S', "plankWood")
            craft(get(fixedTool, MK1), "WW", "AW", 'W', "logWood", 'A', Items.STRING)
            var flag = 0
            if (Mod.IndustrialCraft2.isAvailable && !Mod.GregTech.isAvailable) {
                flag = 1
                craft(get(outputInterface, MK1), "GW", " G", "GW", 'G', ICItemFinder.getItem("cable", "type:gold,insulation:0"),
                        'W', "plankWood")
                craftShapeless(get(circuit, MK1), ItemCrafting.get(EnumCrafting.water_resistant_rubber), "circuitBasic")
            }
            if (Mod.Mekanism.isAvailable) {
                flag = 1
                craft(get(outputInterface, MK1), "GW", " G", "GW", 'G', goldCable, 'W', "plankWood")
                craftShapeless(get(circuit, MK1), ItemCrafting.get(EnumCrafting.water_resistant_rubber),
                        getItemStack(IDs.Mekanism, "ControlCircuit", 0))
            }

            if (flag == 0) {
                craft(get(outputInterface, MK1), " W", "G ", " W", 'G', "ingotGold", 'W', "plankWood")
                craft(get(circuit, MK1), "NNN", "RPR", "NNN", 'N', "stickNeodymium", 'R', "dustRedstone", 'P', "plateZinc")
            }
            craftShapeless(ItemCrafting.get(EnumCrafting.water_resistant_rubber), "itemRubber", "itemRubber", "itemRubber", "itemRubber")
            craftShapeless(ItemCrafting.get(EnumCrafting.water_resistant_rubber), Items.SLIME_BALL, Items.SLIME_BALL, Items.SLIME_BALL, Items.SLIME_BALL)
            craft(get(rotationAxle, MK1), "SBS", "SHS", "SBS", 'S', Items.STICK, 'B', "plankWood", 'H', WPItems.hammer)
            Recipes.compressor(ItemCrafting.get(EnumCrafting.water_resistant_rubber), ItemCrafting.get(EnumCrafting.water_resistant_rubber_plate))

            OreDictionary.registerOre("plateRubber", ItemCrafting.get(EnumCrafting.water_resistant_rubber_plate))
            OreDictionary.registerOre("plateDenseRubber", ItemCrafting.get(EnumCrafting.water_resistant_rubber_dense_plate))
            Recipes.compressor(ItemCrafting.get(EnumCrafting.water_resistant_rubber_plate, 9), ItemCrafting.get(EnumCrafting.water_resistant_rubber_dense_plate))
            flag = 0
            if (Mod.IndustrialCraft2.isAvailable && !Mod.GregTech.isAvailable) {
                flag = 1
                craft(get(rotor, MK1), "CCC", "CAC", "CCC", 'C', ICItemFinder.getItem("cable", "type:copper,insulation:0"), 'A',
                        "dustMagnetite")
            }
            if (Mod.Mekanism.isAvailable) {
                flag = 1
                craft(get(rotor, MK1), "CCC", "CAC", "CCC", 'C', copperCable, 'A', "dustMagnetite")
            }
            if (flag == 0) {
                craft(get(rotor, MK1), " C ", "CAC", " C ", 'C', "ingotIron", 'A', "dustMagnetite")
            }

            // MK3

            addCasingRecipe(MK3, "stickZincAlloy", "plateZincAlloy", "blockZinc")
            addPaddleBaseRecipe(MK3)
            if (Mod.IndustrialCraft2.isAvailable) {
                craft(get(drainagePlate, MK3), "WW", "AW", "KW", 'W', "plateZincAlloy", 'A',
                        ICItemFinder.getItem("scaffold", "iron"), 'K', "screwZinc")
            } else {
                craft(get(drainagePlate, MK3), "WW", "AW", "KW", 'W', "plateZincAlloy", 'A', "stickZincAlloy", 'K',
                        "screwZinc")
            }
            craft(get(fixedFrame, MK3), "PSP", "S S", "PSP", 'P',
                    ItemMaterial.get(MaterialTypes.Zinc, MaterialForms.plate), 'S', ItemMaterial.get(MaterialTypes.Zinc, MaterialForms.screw))
            craft(get(fixedTool, MK3), "PSP", "   ", "PSP", 'P',
                    ItemMaterial.get(MaterialTypes.Zinc, MaterialForms.ingot), 'S', ItemMaterial.get(MaterialTypes.Zinc, MaterialForms.screw))
            craft(get(rotationAxle, MK3), "GPG", "ICI", "GPG", 'G', "gearZincAlloy", 'P', "plateZincAlloy", 'I',
                    "plateIron", 'C', get(casing, MK3))
            craft(get(rotationAxle, MK3), "GPG", "ICI", "GPG", 'G', "gearZincAlloy", 'P', "plateZincAlloy", 'I',
                    "plateSteel", 'C', get(casing, MK3))
            craft(get(circuit, MK3), "PPP", "CDC", "PPP", 'P', ItemCrafting.get(EnumCrafting.water_resistant_rubber_dense_plate), 'C',
                    get(circuit, MK1), 'P', ItemCrafting.get(EnumCrafting.water_resistant_rubber_plate), 'D', get(casing, MK3))
            craft(get(stator, MK3), "PIS", "PI ", "PIS", 'P', "plateZincAlloy", 'I', "dustMagnetite", 'S',
                    "stickZincAlloy")
            flag = 0

            if (Mod.IndustrialCraft2.isAvailable && !Mod.GregTech.isAvailable) {
                flag = 1
                craft(get(rotor, MK3), "CIC", "GIG", "G G", 'G', ICItemFinder.getItem("cable", "type:gold,insulation:0"), 'C',
                        ItemCrafting.get(EnumCrafting.dense_copper_coil), 'I', "ingotIron")
            }

            if (Mod.Mekanism.isAvailable) {
                flag = 1
                craft(get(rotor, MK3), "CIC", "GIG", "G G", 'G', goldCable, 'C', ItemCrafting.get(EnumCrafting.dense_copper_coil), 'I',
                        "ingotIron")
            }

            if (flag == 0) {
                craft(get(rotor, MK3), "CIC", " I ", " G ", 'G', "ingotGold", 'C', ItemCrafting.get(EnumCrafting.dense_copper_coil), 'I',
                        "ingotIron")
            }

            if (Mod.IndustrialCraft2.isAvailable) {
                craft(get(outputInterface, MK3), "PRB", "RAI", "PRB", 'P', "plateZincAlloy", 'R', "plateRubber",
                        'I', "ingotZincAlloy", 'A', ICItemFinder.getItem("te", "lv_transformer"), 'B', ICItemFinder.getItem("re_battery"))
            } else {
                craft(get(outputInterface, MK3), "PRB", "RAI", "PRB", 'P', "plateZincAlloy", 'R', "plateRubber",
                        'I', "ingotZincAlloy", 'A', ItemCrafting.get(EnumCrafting.dense_copper_coil), 'B', "dustRedstone")
            }

            // MK4

            addCasingRecipe(MK4, "stickIndustrialSteel", "plateIndustrialSteel", "blockIndustrialSteel")
            addPaddleBaseRecipe(MK4)
            craft(get(drainagePlate, MK4), "WW", "KW", "KW", 'W', "plateIndustrialSteel", 'K',
                    "blockIndustrialSteel")
            craft(get(fixedFrame, MK4), "P  ", "DP ", "SDP", 'P', "plateIndustrialSteel", 'D',
                    "plateDenseIndustrialSteel", 'S', "screwIndustrialSteel")
            craftShapeless(get(fixedTool, MK4), "plateDenseIndustrialSteel",
                    "ingotIndustrialSteel")
            if (Mod.IndustrialCraft2.isAvailable) {
                craft(get(outputInterface, MK4), "SSP", "PTB", "SSP", 'S', "blockSilver", 'P',
                        "plateIndustrialSteel", 'T', ICItemFinder.getItem("te", "mv_transformer"), 'B', ICItemFinder.getItem("single_use_battery"))
                craft(get(outputInterface, MK4), "SSP", "PTB", "SSP", 'S', "blockNeodymium", 'P',
                        "plateIndustrialSteel", 'T', ICItemFinder.getItem("te", "mv_transformer"), 'B', ICItemFinder.getItem("single_use_battery"))
            } else {
                craft(get(outputInterface, MK4), "SSP", "PTB", "SSP", 'S', "blockSilver", 'P',
                        "plateIndustrialSteel", 'T', "blockIron", 'B', "dustRedstone")
                craft(get(outputInterface, MK4), "SSP", "PTB", "SSP", 'S', "blockNeodymium", 'P',
                        "plateIndustrialSteel", 'T', "blockIron", 'B', "dustRedstone")
            }
            craft(get(rotationAxle, MK4), "GPG", "ICI", "GPG", 'G', "gearIndustrialSteel", 'P',
                    "plateIndustrialSteel", 'I', "plateIron", 'C', get(casing, MK4))
            craft(get(rotationAxle, MK4), "GPG", "ICI", "GPG", 'G', "gearIndustrialSteel", 'P',
                    "plateIndustrialSteel", 'I', "plateSteel", 'C', get(casing, MK4))
            craft(get(circuit, MK4), "PPP", "CDC", "BPB", 'P', ItemCrafting.get(EnumCrafting.dense_redstone_plate),
                    'C', "circuitAdvanced", 'P', ItemCrafting.get(EnumCrafting.water_resistant_rubber_plate), 'D', ItemCrafting.get(EnumCrafting.data_ball), 'B', "platePlatinum")
            craft(get(circuit, MK4), "PPP", "CDC", "BPB", 'P', ItemCrafting.get(EnumCrafting.dense_redstone_plate),
                    'C', "circuitAdvanced", 'P', ItemCrafting.get(EnumCrafting.water_resistant_rubber_plate), 'D', ItemCrafting.get(EnumCrafting.data_ball), 'B', "plateVanadiumSteel")
            craft(get(stator, MK4), "PP", "PB", "PP", 'P', "plateIndustrialSteel", 'B',
                    "blockNeodymiumMagnet")
            craft(get(rotor, MK4), "SPS", "PBP", "SPS", 'S', ItemCrafting.get(EnumCrafting.dense_silver_coil), 'P',
                    "plateIndustrialSteel", 'B', Blocks.DIAMOND_BLOCK)

            // MK5
            addCasingRecipe(MK5, "plateManganeseSteel", "plateManganeseSteel", "blockManganeseSteel")
            addPaddleBaseRecipe(MK5)
            craft(get(drainagePlate, MK5), "WW", "KW", "KW", 'W', "plateManganeseSteel", 'K',
                    "blockManganeseSteel")
            if (Mod.IndustrialCraft2.isAvailable) {
                craft(get(circuit, MK5), "PDP", "DCD", "PDP", 'P',
                        ItemCrafting.get(EnumCrafting.water_resistant_rubber_dense_plate), 'C', ICItemFinder.getItem("energy_crystal")?.generalize(), 'D',
                        ItemCrafting.get(EnumCrafting.data_ball))
            } else {
                craft(get(circuit, MK5), "PDP", "DCD", "PDP", 'P',
                        ItemCrafting.get(EnumCrafting.water_resistant_rubber_dense_plate), 'C', "gemDiamond", 'D', ItemCrafting.get(EnumCrafting.data_ball))
            }
            craft(get(fixedFrame, MK5), "P  ", "DP ", "SDP", 'P', "plateManganeseSteel", 'D',
                    "plateDenseManganeseSteel", 'S', "screwManganeseSteel")

            craftShapeless(get(fixedTool, MK5), "plateDenseManganeseSteel", "ingotManganeseSteel")

            craft(get(stator, MK5), "PP", "PB", "PP", 'P', "plateManganeseSteel", 'B',
                    "blockNeodymiumMagnet")

            if (Mod.IndustrialCraft2.isAvailable) {
                craft(get(outputInterface, MK5), "SSP", "PTB", "SSP", 'S', "blockNeodymiumMagnet",
                        'P', "plateManganeseSteel", 'T', ICItemFinder.getItem("te", "hv_transformer"), 'B', ICItemFinder.getItem("single_use_battery"))
            } else {
                craft(get(outputInterface, MK5), "SSP", "PTB", "SSP", 'S', "blockNeodymiumMagnet",
                        'P', "plateManganeseSteel", 'T', "blockManganese", 'B', "dustRedstone")
            }
            craft(get(rotationAxle, MK5), "GPG", "ICI", "GPG", 'G', "gearManganeseSteel", 'P',
                    "plateManganeseSteel", 'I', "plateIron", 'C', get(casing, MK5))
            craft(get(rotationAxle, MK5), "GPG", "ICI", "GPG", 'G', "gearManganeseSteel", 'P',
                    "plateManganeseSteel", 'I', "plateSteel", 'C', get(casing, MK5))
            craft(get(rotor, MK5), "SPS", "PBP", "SPS", 'S', ItemCrafting.get(EnumCrafting.dense_silver_coil), 'P',
                    "plateManganeseSteel", 'B', Blocks.DIAMOND_BLOCK)

            // MK7
            addCasingRecipe(MK7, "plateVanadiumSteel", "plateVanadiumSteel", "blockVanadiumSteel")
            addPaddleBaseRecipe(MK7)
            craft(get(drainagePlate, MK7), "WW", "KW", "KW", 'W', "plateVanadiumSteel", 'K',
                    "blockVanadiumSteel")
            if (Mod.IndustrialCraft2.isAvailable) {
                craft(get(circuit, MK7), "PDP", "DCD", "PDP", 'P',
                        ItemCrafting.get(EnumCrafting.water_resistant_rubber_dense_plate), 'C', ICItemFinder.getItem("lapotron_crystal")?.generalize(), 'D',
                        ItemCrafting.get(EnumCrafting.data_ball))
            } else {
                craft(get(circuit, MK7), "PDP", "DCD", "PDP", 'P',
                        ItemCrafting.get(EnumCrafting.water_resistant_rubber_dense_plate), 'C', "blockDiamond", 'D', ItemCrafting.get(EnumCrafting.data_ball))
            }
            craft(get(fixedFrame, MK7), "P  ", "DP ", "SDP", 'P', "plateVanadiumSteel", 'D',
                    "plateDenseVanadiumSteel", 'S', "screwVanadiumSteel")
            craftShapeless(get(fixedTool, MK7), "plateDenseVanadiumSteel", "ingotVanadiumSteel")

            craft(get(stator, MK7), "PP", "PB", "PP", 'P', "plateVanadiumSteel", 'B',
                    "blockNeodymiumMagnet")

            craft(get(outputInterface, MK7), "SSP", "PTB", "SSP", 'S', "blockNeodymiumMagnet", 'P',
                    "plateVanadiumSteel", 'T', "blockVanadium", 'B', "dustRedstone")
            craft(get(rotationAxle, MK7), "GPG", "ICI", "GPG", 'G', "gearVanadiumSteel", 'P',
                    "plateVanadiumSteel", 'I', "plateIron", 'C', get(casing, MK7))
            craft(get(rotationAxle, MK7), "GPG", "ICI", "GPG", 'G', "gearVanadiumSteel", 'P',
                    "plateVanadiumSteel", 'I', "plateSteel", 'C', get(casing, MK7))
            craft(get(rotor, MK7), "SPS", "PBP", "SPS", 'S', ItemCrafting.get(EnumCrafting.dense_silver_coil), 'P',
                    "plateVanadiumSteel", 'B', Blocks.DIAMOND_BLOCK)

        }

        private fun addPaddleBaseRecipe(level: EnumLevel) {
            craft(get(paddleBase, level), "W W", "SAS", 'W',
                    get(drainagePlate, level), 'S', get(fixedTool, level), 'A',
                    get(fixedFrame, level))
        }

        private fun addCasingRecipe(level: EnumLevel, stick: Any, plate: Any, casing: Any) {
            craft(get(EnumComponent.casing, level), "WSW", "WAW", "WSW", 'W', stick, 'S', plate, 'A', casing)
        }
    }
}

enum class EnumComponent {
    paddleBase,
    drainagePlate,
    fixedFrame,
    fixedTool,
    rotationAxle,
    outputInterface,
    rotor,
    stator,
    casing,
    circuit;

    fun getLocalizedName() = i18n("waterpower.component." + name)

}

enum class EnumLevel(val R: Int, val G: Int, val B: Int, val A: Int) {

    /** Stone & Wood */
    MK1(255, 255, 255, 0),
    /** Brass & Zinc */
    MK3(255, 255, 255, 0),
    /** Steel & Industrial Steel */
    MK4(255, 255, 255, 0),
    /** Maganese Steel */
    MK5(255, 255, 255, 0),
    /** Vanadium Steel */
    MK7(255, 255, 255, 0);

    val color: Int by lazy { Color(R, G, B).rgb and 0xffffff }

    fun getLocalizedName() = i18n("waterpower.level." + name)

    val index: Int get() = ordinal * SPACE

    companion object {
        const val SPACE = 100
    }
}