package org.jackhuang.watercraft.common.block;

import java.util.ArrayList;
import java.util.Random;

import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.common.tileentity.TileEntityBase;
import org.jackhuang.watercraft.common.tileentity.TileEntityBlock;
import org.jackhuang.watercraft.common.tileentity.TileEntityInventory;
import org.jackhuang.watercraft.util.Utils;
import org.jackhuang.watercraft.util.WPLog;

import scala.actors.threadpool.Arrays;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
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
import net.minecraftforge.common.util.ForgeDirection;

public abstract class BlockBase extends BlockContainer {

    private static final ThreadLocal<TileEntity> TEMPORARYTILEENTITY_LOCAL = new ThreadLocal<TileEntity>();

    public static int id;

    // 0-bottom 1-top 2-left 3-front 4-right 5-back
    private static final int[][] facingAndSideToSpriteOffset = {
	{3, 5, 1, 0, 4, 2}, {5, 3, 1, 0, 2, 4}, {0, 1, 3, 5, 4, 2},
	{0, 1, 5, 3, 2, 4}, {0, 1, 2, 4, 3, 5}, {0, 1, 4, 2, 5, 3}};

    @SideOnly(Side.CLIENT)
    protected IIcon[][] textures;

    public BlockBase(String id, Material material) {
	this(id, material, ItemBlock.class);
    }

    public BlockBase(String id, Material material,
	    Class<? extends ItemBlock> itemClass) {
	super(material);

	setBlockName(id);
	setCreativeTab(WaterPower.creativeTabWaterPower);
	setHardness(3.0f);

	GameRegistry.registerBlock(this, itemClass, id);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
	int facing = getDirection(meta);
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

    protected int getDirection(int meta) {
	return 3;
    }

    public int getDirection(IBlockAccess iBlockAccess, int x, int y, int z) {
	int meta = iBlockAccess.getBlockMetadata(x, y, z);

	return getDirection(meta);
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
	Random rand = new Random();

	TileEntity te = world.getTileEntity(x, y, z);

	if (te != null) {
	    TEMPORARYTILEENTITY_LOCAL.set(te);
	    if (te instanceof TileEntityInventory) {
		TileEntityInventory t = (TileEntityInventory) te;

		for (int i = 0; i < t.getSizeInventory(); i++) {
		    ItemStack item = t.getStackInSlot(i);

		    if ((item != null) && (item.stackSize > 0)) {
			float rx = rand.nextFloat() * 0.8F + 0.1F;
			float ry = rand.nextFloat() * 0.8F + 0.1F;
			float rz = rand.nextFloat() * 0.8F + 0.1F;

			EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z
				+ rz, item.copy());

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
	}
	super.breakBlock(world, x, y, z, id, meta);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
	    int metadata, int fortune) {
	ArrayList<ItemStack> al = new ArrayList<ItemStack>();
	TileEntity te = world.getTileEntity(x, y, z);
	if (te == null) {
	    te = TEMPORARYTILEENTITY_LOCAL.get();
	}
	if (te instanceof IDroppable) {
	    al.add(((IDroppable) te).getDroppedItemStack());
	} else {
	    al.addAll(super.getDrops(world, x, y, z, metadata, fortune));
	}
	if (te instanceof IInventory) {
	    IInventory inv = (IInventory) te;
	    for (int i = 0; i < inv.getSizeInventory(); i++) {
		ItemStack is = inv.getStackInSlot(i);
		if (is != null) {
		    al.add(is.copy());
		    is.stackSize = 0;
		    inv.setInventorySlotContents(i, null);
		}
	    }
	}
	return al;
    }

    @Override
    public void onNeighborChange(IBlockAccess world, int x, int y, int z,
	    int tileX, int tileY, int tileZ) {
	TileEntity localTileEntity = world.getTileEntity(x, y, z);

	if ((localTileEntity instanceof TileEntityBase)) {
	    ((TileEntityBase) localTileEntity).onNeighborTileChange(tileX,
		    tileY, tileZ);
	}
    }

    @Override
    public boolean rotateBlock(World worldObj, int x, int y, int z,
	    ForgeDirection axis) {
	TileEntity te = worldObj.getTileEntity(x, y, z);
	if (te != null && te instanceof TileEntityBlock) {
	    TileEntityBlock t = (TileEntityBlock) te;
	    return t.setDirection(axis.ordinal());
	}
	return false;
    }
}
