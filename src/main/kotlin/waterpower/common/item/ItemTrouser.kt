/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.item

import ic2.api.item.ElectricItem
import ic2.api.item.IElectricItem
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemArmor
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraftforge.common.ISpecialArmor
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import waterpower.WaterPower
import waterpower.annotations.Init
import waterpower.client.i18n
import waterpower.client.render.IIconRegister
import waterpower.client.render.item.IItemIconProvider
import waterpower.common.block.watermill.EnumWatermill
import waterpower.common.init.WPBlocks
import waterpower.common.init.WPItems
import waterpower.common.recipe.Recipes
import waterpower.util.getWaterIncomeAndExpenseByBiome

@Init
class ItemTrouser(val type: EnumWatermill) : ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, -1, EntityEquipmentSlot.LEGS), ISpecialArmor, IIconRegister, IItemIconProvider {
    var saved = 0.0

    init {
        unlocalizedName = "waterpower.trouser.${type.getName()}"
        creativeTab = WaterPower.creativeTab
        hasSubtypes = true

        setRegistryName("trouser_${type.getName()}")
        WPItems.registryNames += registryName!!
        ForgeRegistries.ITEMS.register(this)
        WPItems.items += this
        WPItems.trousers[type] = this
    }

    override fun damageArmor(entity: EntityLivingBase?, stack: ItemStack, source: DamageSource?, damage: Int, slot: Int) {
    }

    override fun getProperties(player: EntityLivingBase?, armor: ItemStack, source: DamageSource?, damage: Double, slot: Int)
            = ISpecialArmor.ArmorProperties(0, 0.0, 0)

    override fun getArmorDisplay(player: EntityPlayer?, armor: ItemStack, slot: Int) = 0

    @SideOnly(Side.CLIENT)
    lateinit var icon: TextureAtlasSprite

    override fun registerIcons(textureMap: TextureMap) {
        icon = textureMap.registerSprite(ResourceLocation("${WaterPower.MOD_ID}:items/armor/armor_legs"))
    }

    override fun getIcon(stack: ItemStack, layer: Int) = icon

    override fun getArmorTexture(stack: ItemStack, entity: Entity?, slot: EntityEquipmentSlot, type: String?) =
            "${WaterPower.MOD_ID}:textures/armor/trouser.png"

    override fun getItemStackDisplayName(stack: ItemStack) =
            i18n("waterpower.watermill.trouser", type.getLocalizedName())

    override fun addInformation(stack: ItemStack, worldIn: World?, tooltip: MutableList<String>, flagIn: ITooltipFlag) {
        tooltip += i18n("waterpower.watermill.max_output", type.getOutput())
        tooltip += i18n("waterpower.watermill.in_water")
    }

    private fun tryToCharge(stack: ItemStack?) {
        if (stack != null && stack.item is IElectricItem) {
            val electricItem = stack.item as IElectricItem
            saved -= ElectricItem.manager.charge(stack, saved, 2147483647, true, false)
        }
    }

    override fun onArmorTick(world: World, player: EntityPlayer, itemStack: ItemStack) {
        if (world.isRemote)
            return
        var percent = 0.0
        val biomeId = Biome.REGISTRY.getNameForObject(world.getBiomeForCoordsBody(player.position))?.resourcePath?.toLowerCase() ?: ""
        val (weather, acquirement) = getWaterIncomeAndExpenseByBiome(player.world, biomeId)
        percent += acquirement * weather / 10.0

        if (player.isInWater)
            percent += 1

        saved += type.getOutput() * percent
        for (stack in player.inventory.armorInventory) {
            if (stack == itemStack)
                continue
            if (saved <= 0)
                break
            tryToCharge(stack)
        }
        for (stack in player.inventory.mainInventory) {
            if (stack == itemStack)
                continue
            if (saved <= 0)
                break
            tryToCharge(stack)
        }
    }

    companion object {

        @JvmStatic
        fun preInit() {
            for (type in EnumWatermill.values())
                ItemTrouser(type)
        }

        @JvmStatic
        fun postInit() {
            for (i in EnumWatermill.values()) {
                Recipes.craftShapeless(ItemStack(WPItems.trousers[i], 1, 0), WPBlocks.watermill.getItemStack(i),
                        Items.IRON_LEGGINGS)
            }
        }
    }

}