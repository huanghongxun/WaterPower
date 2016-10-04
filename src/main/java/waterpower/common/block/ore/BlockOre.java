package waterpower.common.block.ore;

import java.util.Map;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import waterpower.common.block.BlockWaterPower;
import waterpower.common.block.GlobalBlocks;
import waterpower.common.recipe.IRecipeRegistrar;

public class BlockOre extends BlockWaterPower {
	private static final PropertyEnum<OreType> ORE_TYPES = PropertyEnum.create("type", OreType.class);

    public BlockOre() {
        super("ore", Material.ROCK, ItemOre.class);

        GlobalBlocks.ore = this;

        GlobalBlocks.monaziteOre = new ItemStack(this, 1, 0);
        GlobalBlocks.vanadiumOre = new ItemStack(this, 1, 1);
        GlobalBlocks.manganeseOre = new ItemStack(this, 1, 2);
        GlobalBlocks.magnetOre = new ItemStack(this, 1, 3);
        GlobalBlocks.zincOre = new ItemStack(this, 1, 4);

        OreType.registerRecipes();
        registerOreDict();

        setDefaultState(blockState.getBaseState().withProperty(ORE_TYPES, OreType.Monazite));
    }
    
    @Override
    public void registerModels() {
    	Item item = Item.getItemFromBlock(this);
    	Map<IBlockState, ModelResourceLocation> loc = new DefaultStateMapper().putStateModelLocations(this);
    	for (OreType type : OreType.values()) {
    		IBlockState state = getStateFromMeta(type.ordinal());
    		ModelResourceLocation r = loc.get(state);
    		ModelLoader.setCustomModelResourceLocation(item, type.ordinal(), r);
    	}
    }

    @Override
    protected BlockStateContainer createBlockState() {
    	return new BlockStateContainer(this, ORE_TYPES);
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
    	return getDefaultState().withProperty(ORE_TYPES, OreType.values()[meta]);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
    	return state.getValue(ORE_TYPES).ordinal();
    }

    @Override
    public int maxMetaData() {
        return OreType.values().length;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int var2) {
        return null;
    }

    public void registerOreDict() {
        for (OreType value : OreType.values()) {
            IRecipeRegistrar.registerOreDict("ore" + value.name(), new ItemStack(this, 1, value.ordinal()));
        }
    }
}
