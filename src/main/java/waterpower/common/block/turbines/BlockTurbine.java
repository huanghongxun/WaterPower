package waterpower.common.block.turbines;

import java.util.ArrayList;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import waterpower.common.block.BlockRotor;

public class BlockTurbine extends BlockRotor {
	static final PropertyEnum<TurbineType> TURBINE_TYPES = PropertyEnum.create("type", TurbineType.class);

    public BlockTurbine() {
        super("turbine", Material.IRON, ItemTurbine.class);

        GameRegistry.registerTileEntity(TileEntityTurbine.class, "cptwtrml.turbine");

        setDefaultState(blockState.getBaseState().withProperty(TURBINE_TYPES, TurbineType.MK1).withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    protected BlockStateContainer createBlockState() {
    	return new BlockStateContainer(this, TURBINE_TYPES, FACING);
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
    	return getDefaultState().withProperty(TURBINE_TYPES, TurbineType.values()[meta]);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
    	return state.getValue(TURBINE_TYPES).ordinal();
    }

    @Override
    public TileEntity createNewTileEntity(World world, int var2) {
        return new TileEntityTurbine(TurbineType.values()[var2]);
    }

    @Override
    public int maxMetaData() {
        return TurbineType.values().length;
    }

    public ArrayList<String> getDebugInfo(EntityPlayer aPlayer, BlockPos aPos, int aLogLevel) {
        ArrayList<String> al = new ArrayList<String>();
        TileEntity tileEntity = aPlayer.worldObj.getTileEntity(aPos);
        if (tileEntity instanceof TileEntityTurbine) {
            TileEntityTurbine te = (TileEntityTurbine) tileEntity;
            if (te.getType() == null)
                al.add("Type: null");
            else
                al.add("Type: " + te.getType().name());
            al.add("Output: " + te.getOfferedEnergy());
            al.add("Water: " + te.getWater());
        } else {
            al.add("Not a turbine tile entity.");
        }
        return al;
    }
}