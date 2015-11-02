package org.jackhuang.watercraft.common.block.reservoir;

import java.util.ArrayList;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.common.block.BlockRotor;
import org.jackhuang.watercraft.common.item.others.ItemType;
import org.jackhuang.watercraft.common.recipe.IRecipeRegistrar;
import org.jackhuang.watercraft.integration.ic2.ICItemFinder;
import org.jackhuang.watercraft.util.Mods;
import org.jackhuang.watercraft.util.StackUtil;

import cpw.mods.fml.common.registry.GameRegistry;

public class BlockReservoir extends BlockRotor {

    public BlockReservoir() {
        super("cptBlockReservoir", Material.iron, ItemReservoir.class);

        registerReservoir();

        GameRegistry.registerTileEntity(TileEntityReservoir.class, "cptwtrml.reservoir");
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        textures = new IIcon[maxMetaData()][6];
        for (int i = 0; i < maxMetaData(); i++) {
            textures[i][0] = textures[i][1] = textures[i][2] = textures[i][3] = textures[i][4] = textures[i][5] = iconRegister.registerIcon(Reference.ModID
                    + ":reservoir/" + ReservoirType.values()[i].name());
        }
    }

    @Override
    protected int getTextureIndex(IBlockAccess iBlockAccess, int x, int y, int z, int meta) {
        TileEntity tTileEntity = iBlockAccess.getTileEntity(x, y, z);
        if (tTileEntity instanceof TileEntityReservoir) {
            TileEntityReservoir te = (TileEntityReservoir) tTileEntity;
            if (te.type != null)
                return te.type.ordinal();
        }
        return meta;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int var2) {
        return ReservoirType.makeTileEntity(0);
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return ReservoirType.makeTileEntity(metadata);
    }

    @Override
    protected int maxMetaData() {
        return ReservoirType.values().length;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int s, float f1, float f2, float f3) {
        ItemStack current = entityplayer.inventory.getCurrentItem();
        if (current != null) {
            TileEntity tile = world.getTileEntity(x, y, z);

            if ((tile instanceof TileEntityReservoir)) {
                TileEntityReservoir reservoir = (TileEntityReservoir) tile;

                if (current.getItem() instanceof ItemReservoir) {
                    if (reservoir.type != null && current.getItemDamage() == reservoir.type.ordinal()) {
                        return false;
                    }
                }

                if (FluidContainerRegistry.isContainer(current)) {
                    FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(current);

                    if (liquid != null) {
                        int qty = reservoir.fill(ForgeDirection.UNKNOWN, liquid, true);

                        if ((qty != 0) && (!entityplayer.capabilities.isCreativeMode)) {
                            if (current.stackSize > 1) {
                                if (!entityplayer.inventory.addItemStackToInventory(FluidContainerRegistry.drainFluidContainer(current))) {
                                    entityplayer.dropPlayerItemWithRandomChoice(FluidContainerRegistry.drainFluidContainer(current), false);
                                }

                                entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, StackUtil.consumeItem(current));
                            } else {
                                entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem,
                                        FluidContainerRegistry.drainFluidContainer(current));
                            }
                        }

                        return true;
                    }

                    FluidStack available = reservoir.getFluidStackfromTank();

                    if (available != null) {
                        ItemStack filled = FluidContainerRegistry.fillFluidContainer(available, current);

                        liquid = FluidContainerRegistry.getFluidForFilledItem(filled);

                        if (liquid != null) {
                            if (!entityplayer.capabilities.isCreativeMode) {
                                if (current.stackSize > 1) {
                                    if (!entityplayer.inventory.addItemStackToInventory(filled)) {
                                        return false;
                                    }
                                    entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, StackUtil.consumeItem(current));
                                } else {
                                    entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, StackUtil.consumeItem(current));
                                    entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, filled);
                                }
                            }

                            reservoir.drain(ForgeDirection.UNKNOWN, liquid.amount, true);

                            return true;
                        }
                    }
                } else if ((current.getItem() instanceof IFluidContainerItem)) {
                    if (current.stackSize != 1) {
                        return false;
                    }

                    if (!world.isRemote) {
                        IFluidContainerItem container = (IFluidContainerItem) current.getItem();
                        FluidStack liquid = container.getFluid(current);
                        FluidStack tankLiquid = reservoir.getFluidStackfromTank();
                        boolean mustDrain = (liquid == null) || (liquid.amount == 0);
                        boolean mustFill = (tankLiquid == null) || (tankLiquid.amount == 0);
                        if ((!mustDrain) || (!mustFill)) {
                            if ((mustDrain) || (!entityplayer.isSneaking())) {
                                liquid = reservoir.drain(ForgeDirection.UNKNOWN, 1000, false);
                                int qtyToFill = container.fill(current, liquid, true);
                                reservoir.drain(ForgeDirection.UNKNOWN, qtyToFill, true);
                            } else if (((mustFill) || (entityplayer.isSneaking())) && (liquid.amount > 0)) {
                                int qty = reservoir.fill(ForgeDirection.UNKNOWN, liquid, false);
                                reservoir.fill(ForgeDirection.UNKNOWN, container.drain(current, qty, true), true);
                            }
                        }
                    }

                    return true;
                }

            }

        }

        return super.onBlockActivated(world, x, y, z, entityplayer, s, f1, f2, f3);
    }

    public ArrayList<String> getDebugInfo(EntityPlayer aPlayer, int aX, int aY, int aZ, int aLogLevel) {
        ArrayList<String> al = new ArrayList<String>();
        TileEntity tileEntity = aPlayer.worldObj.getTileEntity(aX, aY, aZ);
        if (tileEntity instanceof TileEntityReservoir) {
            TileEntityReservoir te = (TileEntityReservoir) tileEntity;
            if (te.type == null)
                al.add("Type: null");
            else
                al.add("Type: " + te.type.name());
            al.add("Water: " + te.getFluidAmount());
        } else {
            al.add("Not a reservoir tile entity.");
        }
        return al;
    }

    public void registerReservoir() {

        // Reservoir recipes registering
        addReservoirRecipe(new ItemStack(this, 8, 0), "logWood");
        addReservoirRecipe(new ItemStack(this, 8, 1), Blocks.stone);
        addReservoirRecipe(new ItemStack(this, 8, 2), Blocks.lapis_block);
        addReservoirRecipe(new ItemStack(this, 8, 3), "blockTin");
        addReservoirRecipe(new ItemStack(this, 8, 4), "blockCopper");
        addReservoirRecipe(new ItemStack(this, 8, 5), "blockLead");
        addReservoirRecipe(new ItemStack(this, 8, 6), Blocks.quartz_block);
        addReservoirRecipe(new ItemStack(this, 8, 7), "blockBronze");
        addReservoirRecipe(new ItemStack(this, 8, 8), Blocks.iron_block);
        addReservoirRecipe(new ItemStack(this, 8, 9), Blocks.nether_brick);
        addReservoirRecipe(new ItemStack(this, 8, 10), Blocks.obsidian);
        addReservoirRecipe(new ItemStack(this, 8, 11), "blockSilver");
        addReservoirRecipe(new ItemStack(this, 8, 12), Blocks.gold_block);
        addReservoirRecipe(new ItemStack(this, 8, 13), ICItemFinder.getIC2Item("carbonPlate"));
        addReservoirAdvancedRecipe(new ItemStack(this, 8, 14), ICItemFinder.getIC2Item("advancedAlloy"));
        addReservoirAdvancedRecipe(new ItemStack(this, 8, 15), Blocks.emerald_block);
        addReservoirAdvancedRecipe(new ItemStack(this, 8, 16), Blocks.diamond_block);
        addReservoirAdvancedRecipe(new ItemStack(this, 8, 17), ICItemFinder.getIC2Item("iridiumOre"));
        addReservoirAdvancedRecipe(new ItemStack(this, 8, 18), ICItemFinder.getIC2Item("iridiumPlate"));
        addReservoirRecipe(new ItemStack(this, 8, 19), "blockZinc");
        addReservoirRecipe(new ItemStack(this, 8, 20), "blockBrass");
        addReservoirRecipe(new ItemStack(this, 8, 20), "blockZincAlloy");
        addReservoirRecipe(new ItemStack(this, 8, 21), "blockAluminum");
        addReservoirAdvancedRecipe(new ItemStack(this, 8, 22), "blockSteel");
        addReservoirRecipe(new ItemStack(this, 8, 23), "blockInvar");
        addReservoirRecipe(new ItemStack(this, 8, 24), "blockElectrum");
        addReservoirRecipe(new ItemStack(this, 8, 25), "blockNickel");
        addReservoirAdvancedRecipe(new ItemStack(this, 8, 26), "blockOsmium");
        addReservoirAdvancedRecipe(new ItemStack(this, 8, 27), "blockTitanium");
        addReservoirAdvancedRecipe(new ItemStack(this, 8, 27), "blockManganese");
        addReservoirAdvancedRecipe(new ItemStack(this, 8, 28), "blockPlatinum");
        addReservoirAdvancedRecipe(new ItemStack(this, 8, 29), "blockTungsten");
        addReservoirAdvancedRecipe(new ItemStack(this, 8, 29), "blockVanadium");
        addReservoirAdvancedRecipe(new ItemStack(this, 8, 30), "blockChrome");
        addReservoirAdvancedRecipe(new ItemStack(this, 8, 30), "blockManganeseSteel");
        addReservoirAdvancedRecipe(new ItemStack(this, 8, 31), "blockTungstenSteel");
        addReservoirAdvancedRecipe(new ItemStack(this, 8, 31), "blockVanadiumSteel");
        if (Mods.Thaumcraft.isAvailable) {
            addReservoirAdvancedRecipe(new ItemStack(this, 8, 32), new ItemStack(GameRegistry.findBlock(Mods.IDs.Thaumcraft, "blockCosmeticSolid"), 1, 4));
        }
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