package org.jackhuang.compactwatermills.common.block;

import java.util.Random;

import org.jackhuang.compactwatermills.InternalName;
import org.jackhuang.compactwatermills.common.block.watermills.WaterType;
import org.jackhuang.compactwatermills.common.inventory.InventorySlot;
import org.jackhuang.compactwatermills.common.tileentity.TileEntityBase;
import org.jackhuang.compactwatermills.common.tileentity.TileEntityInventory;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

public abstract class BlockMeta extends BlockMultiID {

	public BlockMeta(InternalName name,
			Material material, Class<? extends ItemBlock> c) {
		super(name, material, c);
	}

	@Override
	protected int maxMetaData() {
		return 1;
	}

}
