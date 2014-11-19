package org.jackhuang.watercraft.integration.thermalexpansion;

import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TERecipeAdder {
    public static void addFurnaceRecipe(int energy, ItemStack input, ItemStack output)
    {
      NBTTagCompound toSend = new NBTTagCompound();
      toSend.setInteger("energy", energy);
      toSend.setTag("input", new NBTTagCompound());
      toSend.setTag("output", new NBTTagCompound());
      input.writeToNBT(toSend.getCompoundTag("input"));
      output.writeToNBT(toSend.getCompoundTag("output"));
      FMLInterModComms.sendMessage("ThermalExpansion", "FurnaceRecipe", toSend);
    }

    public static void addPulverizerRecipe(int energy, ItemStack input, ItemStack primaryOutput) {
      addPulverizerRecipe(energy, input, primaryOutput, null, 0);
    }

    public static void addPulverizerRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput) {
      addPulverizerRecipe(energy, input, primaryOutput, secondaryOutput, 100);
    }

    public static void addPulverizerRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int secondaryChance) {
      if ((input == null) || (primaryOutput == null)) return;
      NBTTagCompound toSend = new NBTTagCompound();
      toSend.setInteger("energy", energy);
      toSend.setTag("input", new NBTTagCompound());
      toSend.setTag("primaryOutput", new NBTTagCompound());
      toSend.setTag("secondaryOutput", new NBTTagCompound());
      input.writeToNBT(toSend.getCompoundTag("input"));
      primaryOutput.writeToNBT(toSend.getCompoundTag("primaryOutput"));
      if (secondaryOutput != null) secondaryOutput.writeToNBT(toSend.getCompoundTag("secondaryOutput"));
      toSend.setInteger("secondaryChance", secondaryChance);
      FMLInterModComms.sendMessage("ThermalExpansion", "PulverizerRecipe", toSend);
    }

    public static void addSawmillRecipe(int energy, ItemStack input, ItemStack primaryOutput) {
      addSawmillRecipe(energy, input, primaryOutput, null, 0);
    }

    public static void addSawmillRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput) {
      addSawmillRecipe(energy, input, primaryOutput, secondaryOutput, 100);
    }

    public static void addSawmillRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int secondaryChance) {
      if ((input == null) || (primaryOutput == null)) return;
      NBTTagCompound toSend = new NBTTagCompound();
      toSend.setInteger("energy", energy);
      toSend.setTag("input", new NBTTagCompound());
      toSend.setTag("primaryOutput", new NBTTagCompound());
      toSend.setTag("secondaryOutput", new NBTTagCompound());
      input.writeToNBT(toSend.getCompoundTag("input"));
      primaryOutput.writeToNBT(toSend.getCompoundTag("primaryOutput"));
      if (secondaryOutput != null) secondaryOutput.writeToNBT(toSend.getCompoundTag("secondaryOutput"));
      toSend.setInteger("secondaryChance", secondaryChance);
      FMLInterModComms.sendMessage("ThermalExpansion", "SawmillRecipe", toSend);
    }

    public static void addSmelterRecipe(int energy, ItemStack primaryInput, ItemStack secondaryInput, ItemStack primaryOutput) {
      addSmelterRecipe(energy, primaryInput, secondaryInput, primaryOutput, null, 0);
    }

    public static void addSmelterRecipe(int energy, ItemStack primaryInput, ItemStack secondaryInput, ItemStack primaryOutput, ItemStack secondaryOutput) {
      addSmelterRecipe(energy, primaryInput, secondaryInput, primaryOutput, secondaryOutput, 100);
    }

    public static void addSmelterRecipe(int energy, ItemStack primaryInput, ItemStack secondaryInput, ItemStack primaryOutput, ItemStack secondaryOutput, int secondaryChance) {
      if ((primaryInput == null) || (secondaryInput == null) || (primaryOutput == null)) return;
      NBTTagCompound toSend = new NBTTagCompound();
      toSend.setInteger("energy", energy);
      toSend.setTag("primaryInput", new NBTTagCompound());
      toSend.setTag("secondaryInput", new NBTTagCompound());
      toSend.setTag("primaryOutput", new NBTTagCompound());
      toSend.setTag("secondaryOutput", new NBTTagCompound());
      primaryInput.writeToNBT(toSend.getCompoundTag("primaryInput"));
      secondaryInput.writeToNBT(toSend.getCompoundTag("secondaryInput"));
      primaryOutput.writeToNBT(toSend.getCompoundTag("primaryOutput"));
      if (secondaryOutput != null) secondaryOutput.writeToNBT(toSend.getCompoundTag("secondaryOutput"));
      toSend.setInteger("secondaryChance", secondaryChance);
      FMLInterModComms.sendMessage("ThermalExpansion", "SmelterRecipe", toSend);
    }
}
