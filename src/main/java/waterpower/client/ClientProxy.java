/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import waterpower.client.gui.DefaultGuiIds;
import waterpower.client.render.BlockColor;
import waterpower.client.render.IconRegisterService;
import waterpower.client.render.RecolorableTextures;
import waterpower.client.render.RenderReservoir;
import waterpower.client.render.item.ItemColor;
import waterpower.common.CommonProxy;
import waterpower.common.block.BlockWaterPower;
import waterpower.common.block.GlobalBlocks;
import waterpower.common.block.machines.GuiCentrifuge;
import waterpower.common.block.machines.GuiCompressor;
import waterpower.common.block.machines.GuiCutter;
import waterpower.common.block.machines.GuiLathe;
import waterpower.common.block.machines.GuiMacerator;
import waterpower.common.block.machines.GuiSawmill;
import waterpower.common.block.machines.TileEntityAdvancedCompressor;
import waterpower.common.block.machines.TileEntityCentrifuge;
import waterpower.common.block.machines.TileEntityCompressor;
import waterpower.common.block.machines.TileEntityCutter;
import waterpower.common.block.machines.TileEntityLathe;
import waterpower.common.block.machines.TileEntityMacerator;
import waterpower.common.block.machines.TileEntitySawmill;
import waterpower.common.block.ore.BlockMaterial;
import waterpower.common.block.reservoir.GuiReservoir;
import waterpower.common.block.reservoir.TileEntityReservoir;
import waterpower.common.block.turbines.GuiTurbine;
import waterpower.common.block.turbines.TileEntityTurbine;
import waterpower.common.block.watermills.GuiWatermill;
import waterpower.common.block.watermills.TileEntityWatermill;
import waterpower.common.item.GlobalItems;
import waterpower.common.item.ItemRecolorable;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    public void loadBlockIcons() {
        //if (Mods.TinkersConstruct.isAvailable)
        //    TinkersConstructModule.registerIcons();
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer thePlayer, World world, int X, int Y, int Z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(X, Y, Z));
        if (tileEntity == null)
            return null;
        if (ID == DefaultGuiIds.get("tileEntityTurbine")) {
            TileEntityTurbine tileEntityT = (TileEntityTurbine) tileEntity;
            return new GuiTurbine(thePlayer, tileEntityT);
        } else if (ID == DefaultGuiIds.get("tileEntityWatermill")) {
            TileEntityWatermill tileEntityCW = (TileEntityWatermill) tileEntity;
            return new GuiWatermill(thePlayer, tileEntityCW);
        } else if (ID == DefaultGuiIds.get("tileEntityReservoir")) {
            TileEntityReservoir tileEntityR = (TileEntityReservoir) tileEntity;
            return new GuiReservoir(thePlayer, tileEntityR);
        } else if (ID == DefaultGuiIds.get("tileEntityMacerator")) {
            TileEntityMacerator tileEntityR = (TileEntityMacerator) tileEntity;
            return new GuiMacerator(thePlayer, tileEntityR);
        } else if (ID == DefaultGuiIds.get("tileEntityCompressor")) {
            TileEntityCompressor tileEntityR = (TileEntityCompressor) tileEntity;
            return new GuiCompressor(thePlayer, tileEntityR);
        } else if (ID == DefaultGuiIds.get("tileEntitySawmill")) {
            TileEntitySawmill tileEntityR = (TileEntitySawmill) tileEntity;
            return new GuiSawmill(thePlayer, tileEntityR);
        } else if (ID == DefaultGuiIds.get("tileEntityLathe")) {
            TileEntityLathe tileEntityR = (TileEntityLathe) tileEntity;
            return new GuiLathe(thePlayer, tileEntityR);
        } else if (ID == DefaultGuiIds.get("tileEntityCutter")) {
            TileEntityCutter tileEntityR = (TileEntityCutter) tileEntity;
            return new GuiCutter(thePlayer, tileEntityR);
        } else if (ID == DefaultGuiIds.get("tileEntityAdvancedCompressor")) {
            TileEntityAdvancedCompressor tileEntityR = (TileEntityAdvancedCompressor) tileEntity;
            return new GuiCompressor(thePlayer, tileEntityR);
        } else if (ID == DefaultGuiIds.get("tileEntityCentrifuge")) {
            TileEntityCentrifuge tileEntityR = (TileEntityCentrifuge) tileEntity;
            return new GuiCentrifuge(thePlayer, tileEntityR);
        }
        return null;
    }

    @Override
    public void preInit() {
        super.preInit();
        
        for (BlockWaterPower block : GlobalBlocks.blocks) {
        	block.registerModels();
        }

    	MinecraftForge.EVENT_BUS.register(IconRegisterService.INSTANCE);
    	RecolorableTextures.registerToService();
    }
    
    @Override
    public void init() {
    	super.init();
    	
        IconRegisterService.setupItemModels();
        
        BlockMaterial material = (BlockMaterial) GlobalBlocks.material;
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(BlockColor.INSTANCE, material);

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityReservoir.class, new RenderReservoir());
        
		for (Item item : GlobalItems.items) {
			if (item instanceof ItemRecolorable) {
				Minecraft.getMinecraft().getItemColors().registerItemColorHandler(ItemColor.INSTANCE, item);
			}
		}
    }

}
