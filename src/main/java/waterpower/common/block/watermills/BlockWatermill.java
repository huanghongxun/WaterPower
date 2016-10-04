/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package waterpower.common.block.watermills;

import java.util.ArrayList;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import waterpower.common.block.BlockRotor;

public class BlockWatermill extends BlockRotor {
	static final PropertyEnum<WaterType> WATERMILL_TYPES = PropertyEnum.create("type", WaterType.class);

    public BlockWatermill() {
        super("watermill", Material.IRON, ItemWatermill.class);

        GameRegistry.registerTileEntity(TileEntityWatermill.class, "cptwtrml.watermill");

        setDefaultState(blockState.getBaseState().withProperty(WATERMILL_TYPES, WaterType.MK1));
    }

    @Override
    protected BlockStateContainer createBlockState() {
    	return new BlockStateContainer(this, WATERMILL_TYPES);
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
    	return getDefaultState().withProperty(WATERMILL_TYPES, WaterType.values()[meta]);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
    	return state.getValue(WATERMILL_TYPES).ordinal();
    }

    @Override
    public TileEntity createNewTileEntity(World world, int var2) {
        return new TileEntityWatermill(WaterType.values()[var2]);
    }

    @Override
    public int maxMetaData() {
        return WaterType.values().length;
    }

    public ArrayList<String> getDebugInfo(EntityPlayer aPlayer, BlockPos pos, int aLogLevel) {
        ArrayList<String> al = new ArrayList<String>();
        TileEntity tileEntity = aPlayer.worldObj.getTileEntity(pos);
        if (tileEntity instanceof TileEntityWatermill) {
            TileEntityWatermill te = (TileEntityWatermill) tileEntity;
            if (te.getType() == null)
                al.add("Type: null");
            else
                al.add("Type: " + te.getType().name());
            al.add("Output: " + te.getOfferedEnergy());
            al.add("Range: " + te.getRange());
        } else {
            al.add("Not a watermill tile entity.");
        }
        return al;
    }

}
