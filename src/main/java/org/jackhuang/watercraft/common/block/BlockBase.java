package org.jackhuang.watercraft.common.block;

import java.util.Random;

import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.InternalName;
import org.jackhuang.watercraft.common.tileentity.TileEntityBase;
import org.jackhuang.watercraft.common.tileentity.TileEntityInventory;
import org.jackhuang.watercraft.util.WPLog;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

public abstract class BlockBase extends BlockContainer {

    protected final InternalName internalName;
    public static int id;

	// 0-bottom 1-top 2-left 3-front 4-right 5-back
    private static final int[][] facingAndSideToSpriteOffset = {
	{3, 5, 1, 0, 4, 2}, {5, 3, 1, 0, 2, 4}, {0, 1, 3, 5, 4, 2},
	{0, 1, 5, 3, 2, 4}, {0, 1, 2, 4, 3, 5}, {0, 1, 4, 2, 5, 3}};

    @SideOnly(Side.CLIENT)
    protected IIcon[][] textures;

    public BlockBase(InternalName internalName, Material material) {
	this(internalName, material, ItemBlock.class);
    }

    public BlockBase(InternalName internalName, Material material,
	    Class<? extends ItemBlock> itemClass) {
	super(material);

	setBlockName(internalName.name());
	setCreativeTab(WaterPower.creativeTabWaterPower);
	setHardness(3.0f);

	this.internalName = internalName;

	GameRegistry.registerBlock(this, itemClass, internalName.name());
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
	int facing = getFacing(meta);
	int index = getTextureIndex(meta);
	int subIndex = getTextureSubIndex(facing, side);

	if (index >= this.textures.length) {
	    return null;
	}
	try {
	    return this.textures[index][subIndex];
	} catch (Exception e) {
	    WPLog.warn("Side: " + side + "\n" + "Block: " + this + "\n"
		    + "Meta: " + meta + "\n" + "Facing: " + facing + "\n"
		    + "Index: " + index + "\n" + "SubIndex: " + subIndex);
	}

	return null;
    }

    public String getUnlocalizedName() {
	return super.getUnlocalizedName();
    }

    protected int getFacing(int meta) {
	return 3;
    }

    public int getFacing(IBlockAccess iBlockAccess, int x, int y, int z) {
	int meta = iBlockAccess.getBlockMetadata(x, y, z);

	return getFacing(meta);
    }

    protected String getTextureFolder(int index) {
	return null;
    }

    protected String getTextureName(int index) {
	Item item = Item.getItemFromBlock(this);

	if (!item.getHasSubtypes()) {
	    if (index == 0) {
		return getUnlocalizedName();
	    }
	    return null;
	}

	ItemStack itemStack = new ItemStack(this, 1, index);
	String ret = item.getUnlocalizedName(itemStack);

	if (ret == null) {
	    return null;
	}
	return ret.replace("item", "block");
    }

    @Override
    public int quantityDropped(Random random) {
	return 1;
    }

    @Override
    public int damageDropped(int meta) {
	return meta;
    }

    protected int getTextureIndex(IBlockAccess world, int x, int y, int z,
	    int meta) {
	return meta;
    }

    protected int getTextureIndex(int meta) {
	return meta;
    }

    public final int getTextureSubIndex(int facing, int side) {
	return facingAndSideToSpriteOffset[facing][side];
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block id, int meta) {
	dropItems(world, x, y, z);
	super.breakBlock(world, x, y, z, id, meta);
    }

    private static void dropItems(World world, int x, int y, int z) {
	Random rand = new Random();

	TileEntity te = world.getTileEntity(x, y, z);

	if (te == null && !(te instanceof TileEntityInventory)) {
	    return;
	}

	TileEntityInventory t = (TileEntityInventory) te;

	for (int i = 0; i < t.getSizeInventory(); i++) {
	    ItemStack item = t.getStackInSlot(i);

	    if ((item != null) && (item.stackSize > 0)) {
		float rx = rand.nextFloat() * 0.8F + 0.1F;
		float ry = rand.nextFloat() * 0.8F + 0.1F;
		float rz = rand.nextFloat() * 0.8F + 0.1F;

		EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z
			+ rz, new ItemStack(item.getItem(), item.stackSize,
				item.getItemDamage()));
		if (item.hasTagCompound()) {
		    entityItem.getEntityItem().setTagCompound(
			    (NBTTagCompound) item.getTagCompound().copy());
		}

		float factor = 0.05F;
		entityItem.motionX = (rand.nextGaussian() * factor);
		entityItem.motionY = (rand.nextGaussian() * factor + 0.2);
		entityItem.motionZ = (rand.nextGaussian() * factor);
		world.spawnEntityInWorld(entityItem);
		item.stackSize = 0;
		t.setInventorySlotContents(i, null);
	    }
	}
    }

    @Override
    public void onNeighborChange(IBlockAccess paramIBlockAccess, int paramInt1,
	    int paramInt2, int paramInt3, int paramInt4, int paramInt5,
	    int paramInt6) {
	TileEntity localTileEntity = paramIBlockAccess.getTileEntity(paramInt1,
		paramInt2, paramInt3);

	if ((localTileEntity instanceof TileEntityBase)) {
	    ((TileEntityBase) localTileEntity).onNeighborTileChange(paramInt4,
		    paramInt5, paramInt6);
	}
    }

}
