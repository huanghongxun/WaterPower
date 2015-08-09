package org.jackhuang.watercraft.integration.minetweaker;

import net.minecraft.item.ItemStack;

import org.jackhuang.watercraft.common.recipe.IRecipeManager;
import org.jackhuang.watercraft.common.recipe.MyRecipeOutput;
import org.jackhuang.watercraft.common.recipe.MyRecipes;
import org.jackhuang.watercraft.util.WPLog;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.waterpower.Machines")
public class Machines {

    @ZenMethod
    public static void addCrusherRecipe(@NotNull IItemStack input,
            @NotNull IItemStack output) {
        MineTweakerAPI.apply(new AddRecipeAction("WaterPower Crusher",
                MyRecipes.macerator, MineTweakerMC.getItemStack(input),
                MineTweakerMC.getItemStack(output)));
    }

    @ZenMethod
    public static void addLatheRecipe(@NotNull IItemStack input,
            @NotNull IItemStack output) {
        MineTweakerAPI.apply(new AddRecipeAction("WaterPower Lathe",
                MyRecipes.lathe, MineTweakerMC.getItemStack(input),
                MineTweakerMC.getItemStack(output)));
    }

    @ZenMethod
    public static void addCutterRecipe(@NotNull IItemStack input,
            @NotNull IItemStack output) {
        MineTweakerAPI.apply(new AddRecipeAction("WaterPower Cutter",
                MyRecipes.cutter, MineTweakerMC.getItemStack(input),
                MineTweakerMC.getItemStack(output)));
    }

    @ZenMethod
    public static void addCompressorRecipe(@NotNull IItemStack input,
            @NotNull IItemStack output) {
        MineTweakerAPI.apply(new AddRecipeAction("WaterPower Compressor",
                MyRecipes.compressor, MineTweakerMC.getItemStack(input),
                MineTweakerMC.getItemStack(output)));
    }

    @ZenMethod
    public static void addCentrifugeRecipe(@NotNull IItemStack input,
            @NotNull IItemStack output) {
        MineTweakerAPI.apply(new AddRecipeAction("WaterPower Centrifuge",
                MyRecipes.centrifuge, MineTweakerMC.getItemStack(input),
                MineTweakerMC.getItemStack(output)));
    }

    @ZenMethod
    public static void addAdvancedCompressorRecipe(@NotNull IItemStack input,
            @NotNull IItemStack output) {
        MineTweakerAPI.apply(new AddRecipeAction("WaterPower Advanced Compressor",
                MyRecipes.implosion, MineTweakerMC.getItemStack(input),
                MineTweakerMC.getItemStack(output)));
    }

    @ZenMethod
    public static void addSawmillRecipe(@NotNull IItemStack input,
            @NotNull IItemStack output) {
        MineTweakerAPI.apply(new AddRecipeAction("WaterPower Sawmill",
                MyRecipes.sawmill, MineTweakerMC.getItemStack(input),
                MineTweakerMC.getItemStack(output)));
    }

    @ZenMethod
    public static void removeCrusherRecipe(@NotNull IItemStack input) {
        MineTweakerAPI.apply(new RemoveRecipeAction("WaterPower Crusher",
                MyRecipes.macerator, MineTweakerMC.getItemStack(input)));
    }

    @ZenMethod
    public static void removeLatheRecipe(@NotNull IItemStack input) {
        MineTweakerAPI.apply(new RemoveRecipeAction("WaterPower Lathe",
                MyRecipes.lathe, MineTweakerMC.getItemStack(input)));
    }

    @ZenMethod
    public static void removeCutterRecipe(@NotNull IItemStack input) {
        MineTweakerAPI.apply(new RemoveRecipeAction("WaterPower Cutter",
                MyRecipes.cutter, MineTweakerMC.getItemStack(input)));
    }

    @ZenMethod
    public static void removeCompressorRecipe(@NotNull IItemStack input) {
        MineTweakerAPI.apply(new RemoveRecipeAction("WaterPower Compressor",
                MyRecipes.compressor, MineTweakerMC.getItemStack(input)));
    }

    @ZenMethod
    public static void removeCentrifugeRecipe(@NotNull IItemStack input) {
        MineTweakerAPI.apply(new RemoveRecipeAction("WaterPower Centrifuge",
                MyRecipes.centrifuge, MineTweakerMC.getItemStack(input)));
    }

    @ZenMethod
    public static void removeAdvancedCompressorRecipe(@NotNull IItemStack input) {
        MineTweakerAPI.apply(new RemoveRecipeAction("WaterPower Advanced Compressor",
                MyRecipes.implosion, MineTweakerMC.getItemStack(input)));
    }

    @ZenMethod
    public static void removeSawmillRecipe(@NotNull IItemStack input,
            @NotNull IItemStack output) {
        MineTweakerAPI.apply(new RemoveRecipeAction("WaterPower Sawmill",
                MyRecipes.sawmill, MineTweakerMC.getItemStack(input)));
    }
    
    protected static IItemStack getMachineOutput(IItemStack input, IRecipeManager rm) {
        MyRecipeOutput output = rm.getOutput(
                MineTweakerMC.getItemStack(input), false);
        if ((output == null) || (output.items.isEmpty()))
            return null;
        return MineTweakerMC.getIItemStack((ItemStack) output.items.get(0));
    }

    @ZenMethod
    public static IItemStack getCrusherOutput(IItemStack input) {
        return getMachineOutput(input, MyRecipes.macerator);
    }

    @ZenMethod
    public static IItemStack getAdvancedCompressorOutput(IItemStack input) {
        return getMachineOutput(input, MyRecipes.implosion);
    }

    @ZenMethod
    public static IItemStack getCentrifugeOutput(IItemStack input) {
        return getMachineOutput(input, MyRecipes.centrifuge);
    }

    @ZenMethod
    public static IItemStack getCompressorOutput(IItemStack input) {
        return getMachineOutput(input, MyRecipes.compressor);
    }

    @ZenMethod
    public static IItemStack getCutterOutput(IItemStack input) {
        return getMachineOutput(input, MyRecipes.cutter);
    }

    @ZenMethod
    public static IItemStack getLatheOutput(IItemStack input) {
        return getMachineOutput(input, MyRecipes.lathe);
    }

    @ZenMethod
    public static IItemStack getSawmillOutput(IItemStack input) {
        return getMachineOutput(input, MyRecipes.sawmill);
    }

}
