package org.jackhuang.watercraft.common.block.ore;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.client.ClientProxy;
import org.jackhuang.watercraft.common.block.BlockWaterPower;
import org.jackhuang.watercraft.common.item.crafting.MaterialTypes;
import org.jackhuang.watercraft.common.recipe.IRecipeRegistrar;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMaterial extends BlockWaterPower {
    public IIcon icon = null;

    public BlockMaterial() {
        super("cptBlockMaterial", Material.rock, ItemBlockMaterial.class);

        registerOreDict();
    }

    @Override
    protected int getTextureIndex(IBlockAccess world, int x, int y, int z, int meta) {
        return meta;
    }

    @Override
    protected int maxMetaData() {
        return MaterialTypes.values().length;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int var2) {
        return null;
    }

    @Override
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        return getRenderColor(world.getBlockMetadata(x, y, z));
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        ClientProxy cp = (ClientProxy) WaterPower.proxy;
        if (cp.blockIconRegister == null) {
            cp.blockIconRegister = iconRegister;
            cp.loadBlockIcons();
        }

        icon = iconRegister.registerIcon(Reference.ModID + ":BLOCK");
    }

    public short[] getRGBA(int meta) {
        MaterialTypes type = MaterialTypes.values()[meta];
        return new short[] { type.R, type.G, type.B, type.A };
    }

    @Override
    public int getRenderColor(int meta) {
        short[] rgba = getRGBA(meta);
        return ((int) rgba[0] << 16) + ((int) rgba[1] << 8) + (int) rgba[2];
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return icon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess iBlockAccess, int x, int y, int z, int side) {
        int meta = iBlockAccess.getBlockMetadata(x, y, z);
        return getIcon(side, meta);
    }

    public void registerOreDict() {
        for (MaterialTypes value : MaterialTypes.values()) {
            IRecipeRegistrar.registerOreDict("block" + value.getName(), new ItemStack(this, 1, value.ordinal()));
        }
    }
}
