package org.jackhuang.watercraft.common.item.others;

import java.util.List;

import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.common.block.watermills.WaterType;
import org.jackhuang.watercraft.util.Utils;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IMetalArmor;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.common.ISpecialArmor;

public class ItemTrouser extends ItemArmor implements ISpecialArmor {

    WaterType type;

    int saved = 0;

    public ItemTrouser(WaterType type) {
        super(EnumHelper.addArmorMaterial("CPTWTRMLPANT", 1, new int[] { 1, 1, 1, 1 }, 1), 0, 2);

        setUnlocalizedName(type.getTrousersUnlocalizedName());
        setCreativeTab(WaterPower.creativeTabWaterPower);
        setHasSubtypes(true);

        GameRegistry.registerItem(this, type.getTrousersUnlocalizedName());

        this.type = type;
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        return new ArmorProperties(0, 0, 0);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return 0;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(Reference.ModID + ":armor/itemArmorLegs");
    }

    private void tryToCharge(ItemStack is) {
        if (is != null) {
            if (is.getItem() instanceof IElectricItem) {
                IElectricItem electricItem = (IElectricItem) is.getItem();
                saved -= ElectricItem.manager.charge(is, saved, 2147483647, true, false);
            }

        }
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (world.isRemote)
            return;

        double percent = 0;

        double[] d = Utils.getBiomeRaining(player.worldObj, (int) player.posX, (int) player.posZ);
        double weather = d[0];
        double biomeGet = d[2];

        percent += biomeGet * weather / 10.0;

        if (player.isInWater())
            percent += 1;

        int energy = (int) ((double) type.output * percent);
        saved += energy;
        for (ItemStack is : player.inventory.armorInventory) {
            if (is == itemStack)
                continue;
            if (saved <= 0)
                break;
            tryToCharge(is);
        }
        for (ItemStack is : player.inventory.mainInventory) {
            if (is == itemStack)
                continue;
            if (saved <= 0)
                break;
            tryToCharge(is);
        }
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return Reference.ModID + ":textures/armor/cptwtrml.png";
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemstack) {
        return type.getShowedName() + StatCollector.translateToLocal("cptwtmrl.watermill.TROUSERS");
    }

    @SideOnly(Side.CLIENT)
    public ResourceLocation getRenderTexture() {
        return new ResourceLocation(Reference.ModID + ":textures/items/armor/" + "itemArmorLegs");
    }
}