package waterpower.common.block.reservoir;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.common.registry.GameRegistry;
import waterpower.common.Tanks;
import waterpower.common.block.BlockWaterPower;
import waterpower.common.item.other.ItemType;
import waterpower.common.recipe.IRecipeRegistrar;
import waterpower.util.Mods;

public class BlockReservoir extends BlockWaterPower {
	private static final PropertyEnum<ReservoirType> RESERVOIR_TYPES = PropertyEnum.create("type", ReservoirType.class);

    public BlockReservoir() {
        super("reservoir", Material.IRON, ItemReservoir.class);

        registerReservoir();
        GameRegistry.registerTileEntity(TileEntityReservoir.class, "cptwtrml.reservoir");

        setDefaultState(blockState.getBaseState().withProperty(RESERVOIR_TYPES, ReservoirType.STONE));
    }
    
    @Override
    public void registerModels() {
    	Item item = Item.getItemFromBlock(this);
    	Map<IBlockState, ModelResourceLocation> loc = new DefaultStateMapper().putStateModelLocations(this);
    	for (ReservoirType type : ReservoirType.values()) {
    		IBlockState state = getStateFromMeta(type.ordinal());
    		ModelResourceLocation r = loc.get(state);
    		ModelLoader.setCustomModelResourceLocation(item, type.ordinal(), new ModelResourceLocation(getRegistryName(), "type=" + type.getName()));
    	}
    }

    @Override
    protected BlockStateContainer createBlockState() {
    	return new BlockStateContainer(this, RESERVOIR_TYPES);
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
    	return getDefaultState().withProperty(RESERVOIR_TYPES, ReservoirType.values()[meta]);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
    	return state.getValue(RESERVOIR_TYPES).ordinal();
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityReservoir(ReservoirType.values()[meta]);
    }

    @Override
    public int maxMetaData() {
        return ReservoirType.values().length;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityplayer, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack current = entityplayer.inventory.getCurrentItem();
        if (current != null) {
            TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof TileEntityReservoir) {
                TileEntityReservoir reservoir = (TileEntityReservoir) tile;

                if (current.getItem() instanceof ItemReservoir) {
                    if (reservoir.getType() != null && current.getItemDamage() == reservoir.getType().ordinal()) {
                        return false;
                    }
                }

                if (!world.isRemote) {
                	if (Tanks.handleRightClick(reservoir, side, entityplayer, true, true))
                		return true;
                } else {
                	if (FluidContainerRegistry.isContainer(current))
                		return true;
                }

            }

        }

        return super.onBlockActivated(world, pos, state, entityplayer, hand, heldItem, side, hitX, hitY, hitZ);
    }

    @Override
    @Method(modid = Mods.IDs.IndustrialCraft2)
    public boolean wrenchCanRemove(World world, BlockPos pos, EntityPlayer player) {
        return true;
    }

    public ArrayList<String> getDebugInfo(EntityPlayer aPlayer, BlockPos pos, int aLogLevel) {
        ArrayList<String> al = new ArrayList<String>();
        TileEntity tileEntity = aPlayer.worldObj.getTileEntity(pos);
        if (tileEntity instanceof TileEntityReservoir) {
            TileEntityReservoir te = (TileEntityReservoir) tileEntity;
            if (te.getType() == null)
                al.add("Type: null");
            else
                al.add("Type: " + te.getType().name());
            al.add("Water: " + te.getFluidAmount());
        } else {
            al.add("Not a reservoir tile entity.");
        }
        return al;
    }

    public void registerReservoir() {

        // Reservoir recipes registering
        addReservoirRecipe(new ItemStack(this, 8, 0), "logWood");
        addReservoirRecipe(new ItemStack(this, 8, 1), Blocks.STONE);
        addReservoirRecipe(new ItemStack(this, 8, 2), Blocks.LAPIS_BLOCK);
        addReservoirRecipe(new ItemStack(this, 8, 3), "blockTin");
        addReservoirRecipe(new ItemStack(this, 8, 4), "blockCopper");
        addReservoirRecipe(new ItemStack(this, 8, 5), Blocks.QUARTZ_BLOCK);
        addReservoirRecipe(new ItemStack(this, 8, 6), "blockZinc");
        addReservoirRecipe(new ItemStack(this, 8, 7), "blockBronze");
        addReservoirRecipe(new ItemStack(this, 8, 8), Blocks.NETHER_BRICK);
        addReservoirRecipe(new ItemStack(this, 8, 9), Blocks.IRON_BLOCK);
        addReservoirRecipe(new ItemStack(this, 8, 10), Blocks.OBSIDIAN);
        addReservoirRecipe(new ItemStack(this, 8, 11), "blockSteel");
        addReservoirRecipe(new ItemStack(this, 8, 12), Blocks.GOLD_BLOCK);
        addReservoirAdvancedRecipe(new ItemStack(this, 8, 13), "blockManganeseSteel");
        addReservoirAdvancedRecipe(new ItemStack(this, 8, 14), Blocks.DIAMOND_BLOCK);
        addReservoirAdvancedRecipe(new ItemStack(this, 8, 15), "blockVanadiumSteel");
    }

    void addReservoirRecipe(ItemStack output, Object S) {
        if (S == null)
            return;
        IRecipeRegistrar.addRecipeByOreDictionary(output, "SSS", "SIS", "SSS", 'S', S, 'I', ItemType.ReservoirCore.item());
    }

    void addReservoirAdvancedRecipe(ItemStack output, Object S) {
        if (S == null)
            return;
        IRecipeRegistrar.addRecipeByOreDictionary(output, "SSS", "SIS", "SSS", 'S', S, 'I', ItemType.ReservoirCoreAdvanced.item());
    }

}