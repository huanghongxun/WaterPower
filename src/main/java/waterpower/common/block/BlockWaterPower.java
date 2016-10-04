package waterpower.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import ic2.api.tile.IWrenchable;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.common.registry.GameRegistry;
import waterpower.WaterPower;
import waterpower.client.gui.IHasGui;
import waterpower.client.render.IBlockModelProvider;
import waterpower.common.block.tileentity.TileEntityBase;
import waterpower.common.block.tileentity.TileEntityBlock;
import waterpower.common.block.tileentity.TileEntityInventory;
import waterpower.integration.BuildCraftModule;
import waterpower.util.Mods;
import waterpower.util.Utils;

public abstract class BlockWaterPower extends Block implements IWrenchable, ITileEntityProvider, IBlockModelProvider {
    public static final PropertyUnlistedDirection FACING = new PropertyUnlistedDirection("facing", Lists.newArrayList(EnumFacing.Plane.HORIZONTAL.facings()));
    private static final ThreadLocal<TileEntity> TEMPORARYTILEENTITY_LOCAL = new ThreadLocal<TileEntity>();

    public BlockWaterPower(String id, Material material) {
        this(id, material, ItemBlock.class);
    }

    public BlockWaterPower(String id, Material material, Class<? extends ItemBlock> itemClass) {
        super(material);

        setUnlocalizedName(id);
        setCreativeTab(WaterPower.creativeTabWaterPower);
        setHardness(3.0f);
        GlobalBlocks.blocks.add(this);

        GameRegistry.registerBlock(this, itemClass, id);
    }

    @Override
    public void registerModels() {
    	Item item = Item.getItemFromBlock(this);
    	Map<IBlockState, ModelResourceLocation> loc = new DefaultStateMapper().putStateModelLocations(this);
    	for (int i = 0; i < maxMetaData(); ++i) {
    		IBlockState state = getStateFromMeta(i);
    		ModelResourceLocation r = loc.get(state);
    		ModelLoader.setCustomModelResourceLocation(item, i, r);
    	}
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
    	TileEntity tileEntity = world.getTileEntity(pos);
    	if (tileEntity instanceof TileEntityBlock) {
    		TileEntityBlock block = (TileEntityBlock) tileEntity;
    		return block.getBlockState();
    	}
    	return state;
    }

    protected EnumFacing getDirection(IBlockState state) {
        return getDirection(getMetaFromState(state));
    }

    protected EnumFacing getDirection(int meta) {
        return EnumFacing.NORTH;
    }
    
    protected boolean setDirection(IBlockState sta, EnumFacing side) {
    	return false;
    }

    public EnumFacing getDirection(IBlockAccess iBlockAccess, BlockPos pos) {
        TileEntity te = iBlockAccess.getTileEntity(pos);

        if (te instanceof TileEntityBlock)
            return ((TileEntityBlock) te).getFacing();
        else
        	return getDirection(iBlockAccess.getBlockState(pos));
    }
    
    public boolean setDirection(IBlockAccess iBlockAccess, BlockPos pos, EnumFacing side) {
        TileEntity te = iBlockAccess.getTileEntity(pos);

        if (te instanceof TileEntityBlock)
            return ((TileEntityBlock) te).setFacing(side);
        else
        	return setDirection(iBlockAccess.getBlockState(pos), side);
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }
    
    @Override
    public int damageDropped(IBlockState state) {
    	return getMetaFromState(state);
    }
    
    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
    		int meta, EntityLivingBase placer) {
    	IBlockState state = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
    	if (state.getPropertyNames().contains(FACING))
    		return state.withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    	else
    		return state;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (WaterPower.isClientSide())
            return;

        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TileEntityBlock) {
            TileEntityBlock te = (TileEntityBlock) tileEntity;
            if (placer == null) {
                te.setFacing(EnumFacing.NORTH);
            } else {
                te.setFacing(placer.getHorizontalFacing().getOpposite());
            }
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState meta) {
        TEMPORARYTILEENTITY_LOCAL.remove();
        
        TileEntity te = world.getTileEntity(pos);

        if (te != null) {
            if (!alreadyGet)
                TEMPORARYTILEENTITY_LOCAL.set(te);
            if (te instanceof TileEntityInventory) {
                TileEntityInventory t = (TileEntityInventory) te;

                for (int i = 0; i < t.getSizeInventory(); i++) {
                    ItemStack item = t.getStackInSlot(i);

                    if (item != null && item.stackSize > 0) {
                    	System.out.println("breakBlock: drop item");
                        float rx = Utils.rand.nextFloat() * 0.8F + 0.1F;
                        float ry = Utils.rand.nextFloat() * 0.8F + 0.1F;
                        float rz = Utils.rand.nextFloat() * 0.8F + 0.1F;

                        EntityItem entityItem = new EntityItem(world, pos.getX() + rx, pos.getY() + ry, pos.getZ() + rz, item.copy());

                        float factor = 0.05F;
                        entityItem.motionX = (Utils.rand.nextGaussian() * factor);
                        entityItem.motionY = (Utils.rand.nextGaussian() * factor + 0.2);
                        entityItem.motionZ = (Utils.rand.nextGaussian() * factor);
                        world.spawnEntityInWorld(entityItem);
                        item.stackSize = 0;
                        t.setInventorySlotContents(i, null);
                    }
                }
            }
        }
        super.breakBlock(world, pos, meta);
        
        alreadyGet = false;
    }
    
    static boolean alreadyGet = false;
    
    
	protected List<ItemStack> getDropsImpl(IBlockAccess world, BlockPos pos, IBlockState state, TileEntity te, int fortune) {
        ArrayList<ItemStack> al = new ArrayList<ItemStack>();
        if (te instanceof IDroppable)
            al.add(((IDroppable) te).getDroppedItemStack());
        else
            al.addAll(super.getDrops(world, pos, state, fortune));
        return al;
	}

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState sta, int fortune) {
        alreadyGet = false;
        TileEntity te = world.getTileEntity(pos);
        if (te == null) {
            te = TEMPORARYTILEENTITY_LOCAL.get();
            alreadyGet = true;
        }
        TEMPORARYTILEENTITY_LOCAL.remove();
        return getDropsImpl(world, pos, sta, te, fortune);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        TileEntity localTileEntity = world.getTileEntity(pos);

        if (localTileEntity instanceof TileEntityBase) {
            ((TileEntityBase) localTileEntity).onNeighborTileChange(neighbor);
        }
    }

    @Override
    public boolean rotateBlock(World worldObj, BlockPos pos, EnumFacing axis) {
        TileEntity te = worldObj.getTileEntity(pos);
        if (te != null && te instanceof TileEntityBlock) {
            TileEntityBlock t = (TileEntityBlock) te;
            return t.setFacing(axis);
        }
        return false;
    }

    @Override
    public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityPlayer, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (entityPlayer != null) {
            ItemStack is = entityPlayer.inventory.getCurrentItem();
            if (BuildCraftModule.isWrench(entityPlayer, is, pos) && entityPlayer.isSneaking()) {
                TileEntity tileEntity = world.getTileEntity(pos);
                if (tileEntity != null && tileEntity instanceof IDroppable) {
                    List<ItemStack> drops = state.getBlock().getDrops(world, pos, state, 0);
                    if (state.getBlock().removedByPlayer(state, world, pos, entityPlayer, false)) {
                        Utils.dropItems(world, pos, drops);
                    }
                    return false;
                }
            }
        }

        if (entityPlayer.isSneaking())
            return false;

        TileEntity te = world.getTileEntity(pos);

        if (te instanceof IHasGui) {
            entityPlayer.openGui(WaterPower.instance, ((IHasGui) te).getGuiId(), world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }

        return false;
    }

    public abstract int maxMetaData();

    @Override
    public void getSubBlocks(Item item, CreativeTabs p_149666_2_, List itemList) {
        if (!item.getHasSubtypes())
            itemList.add(new ItemStack(this));
        else {
            for (int i = 0; i < maxMetaData(); i++) {
                ItemStack is = new ItemStack(this, 1, i);

                if (item.getUnlocalizedName(is) == null)
                    break;
                itemList.add(is);
            }
        }
    }
    
    

    /*
     * ---------------------------------------
     * 
     * WRENCH(INDUSTRIAL CRAFT 2) INTEGRATION BEGINS
     * 
     * ---------------------------------------
     */

    @Override
    @Method(modid = Mods.IDs.IndustrialCraft2API)
    public EnumFacing getFacing(World world, BlockPos pos) {
        return getDirection(world, pos);
    }

    @Override
    @Method(modid = Mods.IDs.IndustrialCraft2API)
    public boolean setFacing(World world, BlockPos pos, EnumFacing facing, EntityPlayer player) {
    	return setDirection(world, pos, facing);
    }

    @Override
    @Method(modid = Mods.IDs.IndustrialCraft2API)
    public boolean wrenchCanRemove(World world, BlockPos pos, EntityPlayer player) {
        return true;
    }

    @Override
    @Method(modid = Mods.IDs.IndustrialCraft2API)
	public List<ItemStack> getWrenchDrops(World world, BlockPos pos, IBlockState state, TileEntity te, EntityPlayer player, int fortune) {
        return getDropsImpl(world, pos, state, te, fortune);
    }
}