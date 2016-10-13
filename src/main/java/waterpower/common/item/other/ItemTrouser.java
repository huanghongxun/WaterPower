package waterpower.common.item.other;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import waterpower.WaterPower;
import waterpower.client.Local;
import waterpower.client.render.IIconRegister;
import waterpower.client.render.item.IItemIconProvider;
import waterpower.common.block.watermills.WaterType;
import waterpower.common.item.GlobalItems;
import waterpower.util.Utils;

public class ItemTrouser extends ItemArmor implements ISpecialArmor, IIconRegister, IItemIconProvider {

    WaterType type;

    int saved = 0;

    public ItemTrouser(WaterType type) {
        super(ItemArmor.ArmorMaterial.DIAMOND, -1, EntityEquipmentSlot.LEGS);

        setUnlocalizedName("trouser");
        setCreativeTab(WaterPower.creativeTabWaterPower);
        setHasSubtypes(true);

        GameRegistry.registerItem(this, type.getTrousersUnlocalizedName());
        GlobalItems.items.add(this);

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

    private void tryToCharge(ItemStack is) {
        if (is != null && is.getItem() instanceof IElectricItem) {
            IElectricItem electricItem = (IElectricItem) is.getItem();
            saved -= ElectricItem.manager.charge(is, saved, 2147483647, true, false);
        }
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (world.isRemote)
            return;

        double percent = 0;

        double[] d = Utils.getBiomeRaining(player.worldObj, player.getPosition());
        double weather = d[0];
        double biomeGet = d[2];

        percent += biomeGet * weather / 10.0;

        if (player.isInWater())
            percent += 1;

        int energy = (int) (type.output * percent);
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

    @SideOnly(Side.CLIENT)
    TextureAtlasSprite icon;
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(TextureMap textureMap) {
    	icon = textureMap.registerSprite(new ResourceLocation("waterpower:items/armor/itemArmorLegs"));
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIcon(ItemStack stack, int pass) {
    	return icon;
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemstack) {
        return type.getShowedName() + Local.get("cptwtmrl.watermill.TROUSERS");
    }
    
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
    	return "waterpower:textures/armor/cptwtrml.png";
    }
}