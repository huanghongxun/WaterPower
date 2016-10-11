package waterpower.common.block.ore;

import java.awt.Color;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import waterpower.Reference;
import waterpower.common.block.BlockWaterPower;
import waterpower.common.item.crafting.MaterialTypes;
import waterpower.common.recipe.IRecipeRegistrar;

public class BlockMaterial extends BlockWaterPower {
	private static final PropertyEnum<MaterialTypes> MATERIAL_TYPES = PropertyEnum.create("type", MaterialTypes.class);

    public BlockMaterial() {
        super("materialBlock", Material.ROCK, ItemBlockMaterial.class);

        registerOreDict();

        setDefaultState(blockState.getBaseState().withProperty(MATERIAL_TYPES, MaterialTypes.Zinc));
    }

    @Override
    protected BlockStateContainer createBlockState() {
    	return new BlockStateContainer(this, MATERIAL_TYPES);
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
    	return getDefaultState().withProperty(MATERIAL_TYPES, MaterialTypes.values()[meta]);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
    	return state.getValue(MATERIAL_TYPES).ordinal();
    }

    @Override
    public int maxMetaData() {
        return MaterialTypes.values().length;
    }
    
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
    	return null;
    }
    
    @Override
    public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
    	int meta = getMetaFromState(state);
        MaterialTypes type = MaterialTypes.values()[meta];
    	return new Color(type.R, type.G, type.B, type.A).getRGB();
    }
    
    TextureAtlasSprite icon;

    public void registerIcons(TextureMap textureMap) {
    	icon = textureMap.registerSprite(new ResourceLocation(Reference.ModID + ":blocks/BLOCK"));
    }

    public void registerOreDict() {
        for (MaterialTypes value : MaterialTypes.values()) {
            IRecipeRegistrar.registerOreDict("block" + value.name(), new ItemStack(this, 1, value.ordinal()));
        }
    }
}
