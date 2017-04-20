package waterpower.common.block.machines;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import waterpower.common.block.BlockWaterPower;
import waterpower.common.block.GlobalBlocks;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockMachines extends BlockWaterPower {

	static final PropertyEnum<MachineType> MACHINE_TYPES = PropertyEnum.create("type", MachineType.class);

    public BlockMachines() {
        super("machine", Material.IRON, ItemMachines.class);

        setHardness(2f);

        GlobalBlocks.macerator = new ItemStack(this, 1, 0);
        GlobalBlocks.compressor = new ItemStack(this, 1, 1);
        GlobalBlocks.sawmill = new ItemStack(this, 1, 2);
        GlobalBlocks.advancedCompressor = new ItemStack(this, 1, 3);
        GlobalBlocks.centrifuge = new ItemStack(this, 1, 4);
        GlobalBlocks.lathe = new ItemStack(this, 1, 5);
        GlobalBlocks.cutter = new ItemStack(this, 1, 6);

        for (MachineType type : MachineType.values())
        	type.registerTileEntity();
        
        TileEntityMacerator.init();
        TileEntityCompressor.init();
        TileEntitySawmill.init();
        TileEntityAdvancedCompressor.init();
        TileEntityCentrifuge.init();
        TileEntityLathe.init();
        TileEntityCutter.init();

        setDefaultState(blockState.getBaseState().withProperty(MACHINE_TYPES, MachineType.MACERATOR).withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    protected BlockStateContainer createBlockState() {
    	return new BlockStateContainer(this, MACHINE_TYPES, FACING);
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
    	return getDefaultState().withProperty(MACHINE_TYPES, MachineType.values()[meta]);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
    	return state.getValue(MACHINE_TYPES).ordinal();
    }

    @Override
    public int maxMetaData() {
        return MachineType.values().length;
    }

    public int getComparatorInputOverride(World par1World, BlockPos pos, int par5) {
        TileEntity te = par1World.getTileEntity(pos);
        if (te != null) {

            if (te instanceof TileEntityStandardWaterMachine) {
                TileEntityStandardWaterMachine tem = (TileEntityStandardWaterMachine) te;
                return (int) Math.floor(tem.getProgress() * 15.0F);
            }
        }

        return 0;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
    	if (meta >= maxMetaData()) return null;
    	else return MachineType.values()[meta].newInstance();
    }
}
