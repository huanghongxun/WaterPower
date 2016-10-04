package waterpower.integration;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import waterpower.common.block.GlobalBlocks;
import waterpower.common.block.ore.OreType;
import waterpower.common.item.crafting.ItemMaterial;
import waterpower.common.item.crafting.MaterialForms;


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

        addMeltingRecipe(block, fluid, blockLiquidValue);
        TinkerRegistry.registerBasinCasting(block, null, fluid, blockLiquidValue);

        addMeltingRecipe(new ItemStack(GlobalBlocks.ore, 1, ore.ordinal()), fluid, oreLiquidValue);

        tryAddMelting(MaterialForms.ingot, ore, block, fluid, temp, ingotLiquidValue);
        TinkerRegistry.registerTableCasting(ItemMaterial.get(ore.t, MaterialForms.ingot), TinkerSmeltery.castIngot, fluid, ingotLiquidValue);

        tryAddMelting(MaterialForms.nugget, ore, block, fluid, temp, nuggetLiquidValue);
        TinkerRegistry.registerTableCasting(ItemMaterial.get(ore.t, MaterialForms.nugget), TinkerSmeltery.castNugget, fluid, nuggetLiquidValue);

        tryAddMelting(MaterialForms.dust, ore, block, fluid, temp, ingotLiquidValue);
    }

    private void tryAddMelting(MaterialForms prefix, OreType ore, ItemStack block, Fluid fluid, int temp, int fluidAmount) {
        try {
            TinkerRegistry.registerMelting(ItemMaterial.get(ore.t, prefix), fluid, fluidAmount);
            TinkerRegistry.registerMelting(new ItemStack(Block.getBlockFromItem(block.getItem()), 1,  block.getItemDamage()), fluid, fluidAmount);
        } catch (NullPointerException e) {
        }
    }

    private void addMeltingRecipe(ItemStack input, Fluid output, int amount) {
        TinkerRegistry.registerMelting(input, output, amount);
    }

    private static class MoltenMetal extends Fluid {
        private final OreType ore;

        public MoltenMetal(OreType ore) {
            this(ore, ore.name().toLowerCase());
        }

        public MoltenMetal(OreType ore, String name) {
            super(name, new ResourceLocation("waterpower:fluid_still"), new ResourceLocation("waterpower:fluid_flow"));
            this.ore = ore;
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