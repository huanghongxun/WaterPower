package org.jackhuang.watercraft.integration;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.client.ClientProxy;
import org.jackhuang.watercraft.common.block.GlobalBlocks;
import org.jackhuang.watercraft.common.block.ore.OreType;
import org.jackhuang.watercraft.common.item.crafting.ItemMaterial;
import org.jackhuang.watercraft.common.item.crafting.MaterialForms;

import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.Smeltery;
import tconstruct.smeltery.TinkerSmeltery;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TinkersConstructModule extends BaseModule {

    @Override
    public void postInit() {
        for (OreType o : OreType.values())
            initOre(o);
    }

    public void initOre(OreType ore) {
        String fluidName = ore.name().toLowerCase();
        Fluid fluid;
        if ((fluid = FluidRegistry.getFluid(fluidName)) == null) {
            fluid = new MoltenMetal(ore);
            FluidRegistry.registerFluid(fluid);
        }

        int temp = 600;

        int blockLiquidValue = 1296;
        int oreLiquidValue = 288;
        int ingotLiquidValue = 144;
        int nuggetLiquidValue = 16;

        ItemStack block = new ItemStack(GlobalBlocks.material, 1, ore.t.ordinal());

        addMeltingRecipe(block, temp, new FluidStack(fluid, blockLiquidValue));
        TConstructRegistry.getBasinCasting().addCastingRecipe(block, new FluidStack(fluid, blockLiquidValue), 50);

        addMeltingRecipe(new ItemStack(GlobalBlocks.ore, 1, ore.ordinal()), temp, new FluidStack(fluid, oreLiquidValue));

        tryAddMelting(MaterialForms.ingot, ore, block, fluid, temp, ingotLiquidValue);
        TConstructRegistry.getTableCasting().addCastingRecipe(ItemMaterial.get(ore.t, MaterialForms.ingot), new FluidStack(fluid, ingotLiquidValue),
                new ItemStack(TinkerSmeltery.metalPattern), 50);

        tryAddMelting(MaterialForms.nugget, ore, block, fluid, temp, nuggetLiquidValue);
        tryAddCasting(MaterialForms.nugget, ore, new FluidStack(fluid, nuggetLiquidValue), 27);

        tryAddMelting(MaterialForms.dust, ore, block, fluid, temp, ingotLiquidValue);
    }

    private void tryAddMelting(MaterialForms prefix, OreType ore, ItemStack block, Fluid fluid, int temp, int fluidAmount) {
        try {
            Smeltery.addMelting(ItemMaterial.get(ore.t, prefix), Block.getBlockFromItem(block.getItem()), block.getItemDamage(), temp, new FluidStack(fluid,
                    fluidAmount));
        } catch (NullPointerException e) {
        }
    }

    private void tryAddCasting(MaterialForms prefix, OreType ore, FluidStack fluid, int patternMeta) {
        try {
            TConstructRegistry.getTableCasting().addCastingRecipe(ItemMaterial.get(ore.t, prefix), fluid,
                    new ItemStack(TinkerSmeltery.metalPattern, 1, patternMeta), 50);
        } catch (NullPointerException e) {
        }
    }

    private void addMeltingRecipe(ItemStack input, int temp, FluidStack output) {
        if ((input.getItem() instanceof ItemBlock))
            Smeltery.addMelting(input, temp, output);
        else
            Smeltery.addMelting(input, Blocks.iron_block, 0, temp, output);
    }

    @SideOnly(Side.CLIENT)
    private static IIcon still, flow;

    @SideOnly(Side.CLIENT)
    public static void registerIcons() {
        ClientProxy proxy = (ClientProxy) WaterPower.proxy;
        still = proxy.blockIconRegister.registerIcon("waterpower:fluid_still");
        flow = proxy.blockIconRegister.registerIcon("waterpower:fluid_flow");
    }

    private static class MoltenMetal extends Fluid {
        private final OreType ore;

        public MoltenMetal(OreType ore) {
            this(ore, ore.name().toLowerCase());
        }

        public MoltenMetal(OreType ore, String name) {
            super(name);
            this.ore = ore;
        }

        @Override
        public IIcon getStillIcon() {
            return still;
        }

        @Override
        public IIcon getFlowingIcon() {
            return flow;
        }

        @Override
        public int getColor() {
            return this.ore.getColor();
        }

        @Override
        public String getUnlocalizedName() {
            return "fluid.waterpower.molten." + this.ore.name();
        }
    }
}
