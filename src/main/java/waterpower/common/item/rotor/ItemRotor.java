package waterpower.common.item.rotor;

import java.util.List;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import waterpower.Reference;
import waterpower.WaterPower;
import waterpower.client.Local;
import waterpower.client.render.IIconRegister;
import waterpower.client.render.item.IItemIconProvider;
import waterpower.common.block.tileentity.TileEntityGenerator;
import waterpower.common.item.GlobalItems;

/**
 * 
 * @author jackhuang1998
 * 
 */
public class ItemRotor extends Item implements IItemIconProvider, IIconRegister {

    public RotorType type;
    protected TextureAtlasSprite[] textures;

    public ItemRotor(RotorType type) {
        super();
        this.type = type;
        setMaxDamage(type.maxDamage);
        setUnlocalizedName(type.unlocalizedName);
        setCreativeTab(WaterPower.creativeTabWaterPower);
        setMaxStackSize(1);
        setNoRepair();

        GlobalItems.items.add(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(TextureMap register) {
        int indexCount = 0;

        while (getTextureName(indexCount) != null)
            indexCount++;

        this.textures = new TextureAtlasSprite[indexCount];

        for (int index = 0; index < indexCount; index++)
            this.textures[index] = register.registerSprite(new ResourceLocation(Reference.ModID + ":items/rotors/" + getTextureName(index)));
    }

	@SideOnly(Side.CLIENT)
    public String getTextureName(int index) {
        if (this.hasSubtypes)
            return getUnlocalizedName(new ItemStack(this, 1, index));
        if (index == 0) {
            return getUnlocalizedName();
        }
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIcon(ItemStack stack, int pass) {
        if (stack.getItemDamage() < this.textures.length) {
            return this.textures[stack.getItemDamage()];
        }
        return this.textures.length < 1 ? null : this.textures[0];
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        if (!type.isInfinite()) {
            int leftOverTicks = par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage();
            par3List.add(Local.get("cptwtrml.rotor.REMAINTIME") + leftOverTicks + "tick");

            String str = "(";
            str = str + (leftOverTicks / 72000) + " " + Local.get("cptwtrml.rotor.HOUR");
            str = str + ((leftOverTicks % 72000) / 1200) + " " + Local.get("cptwtrml.rotor.MINUTE");
            str = str + ((leftOverTicks % 1200) / 20) + " " + Local.get("cptwtrml.rotor.SECOND");
            str = str + ").";
            par3List.add(str);
        } else {
            par3List.add(Local.get("cptwtrml.rotor.INFINITE"));
        }
        par3List.add(Local.get("cptwtrml.rotor.GOT_EFFICIENCY") + " "
                + (int) (((ItemRotor) par1ItemStack.getItem()).type.getEfficiency() * 100) + "%");
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemstack) {
        return type.getShowedName();
    }

    @SideOnly(Side.CLIENT)
    public ResourceLocation getRenderTexture() {
        return new ResourceLocation(Reference.ModID + ":textures/items/rotors/" + this.getUnlocalizedName() + ".png");
    }

    public void tickRotor(ItemStack rotor, TileEntityGenerator tileEntity, World worldObj) {
        return;
    }

}