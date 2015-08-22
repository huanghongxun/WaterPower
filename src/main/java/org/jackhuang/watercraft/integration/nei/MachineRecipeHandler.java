package org.jackhuang.watercraft.integration.nei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

import org.jackhuang.watercraft.common.recipe.IMyRecipeInput;
import org.jackhuang.watercraft.common.recipe.IRecipeManager;
import org.jackhuang.watercraft.common.recipe.MyRecipeOutput;
import org.jackhuang.watercraft.util.StackUtil;
import org.lwjgl.opengl.GL11;

import codechicken.core.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.API;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;

public abstract class MachineRecipeHandler extends TemplateRecipeHandler {

    protected int ticks;

    public abstract String getRecipeName();

    public abstract String getRecipeId();

    public abstract String getGuiTexture();

    public abstract String getOverlayIdentifier();

    public abstract IRecipeManager getRecipeList();

    @Override
    public void drawBackground(int i) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 5, 11, 140, 65);
    }

    @Override
    public void drawExtras(int i) {
        float f = this.ticks >= 20 ? (this.ticks - 20) % 20 / 20.0F : 0.0F;
        drawProgressBar(44, 23, 176, 14, 25, 16, f, 0);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        this.ticks += 1;
    }

    @Override
    public void loadTransferRects() {
        this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(
                new Rectangle(43, 23, 25, 16), getRecipeId()));
        //this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(
        //        new Rectangle(0, 0, 25, 16), getRecipeId()));
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getRecipeId())) {
            for (Map.Entry<IMyRecipeInput, MyRecipeOutput> entry : getRecipeList()
                    .getAllRecipes().entrySet())
                this.arecipes.add(new CachedIORecipe(entry.getKey(), entry
                        .getValue()));
        } else
            super.loadCraftingRecipes(outputId, results);
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (Map.Entry<IMyRecipeInput, MyRecipeOutput> entry : getRecipeList()
                .getAllRecipes().entrySet()) {
            for (ItemStack output : entry.getValue().items)
                if (NEIServerUtils.areStacksSameTypeCrafting(output, result)) {
                    this.arecipes.add(new CachedIORecipe(entry.getKey(), entry
                            .getValue()));
                    break;
                }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (Map.Entry<IMyRecipeInput, MyRecipeOutput> entry : getRecipeList()
                .getAllRecipes().entrySet())
            if (entry.getKey().matches(ingredient))
                this.arecipes.add(new CachedIORecipe(entry.getKey(), entry
                        .getValue()));
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack,
            List<String> currenttip, int recipe) {
        return currenttip;
    }

    protected int getInputPosX() {
        return 21;
    }

    protected int getInputPosY() {
        return 23;
    }

    protected int getOutputPosX() {
        return 80;
    }

    protected int getOutputPosY() {
        return 24;
    }

    protected boolean isOutputsVertical() {
        return true;
    }

    public class CachedIORecipe extends TemplateRecipeHandler.CachedRecipe {
        private final List<PositionedStack> ingredients = new ArrayList();
        private final PositionedStack output;
        private final List<PositionedStack> otherStacks = new ArrayList();

        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(
                    MachineRecipeHandler.this.cycleticks / 20, this.ingredients);
        }

        @Override
        public PositionedStack getResult() {
            return this.output;
        }

        @Override
        public List<PositionedStack> getOtherStacks() {
            return this.otherStacks;
        }

        public CachedIORecipe(ItemStack input, ItemStack output1) {
            super();
            if (input == null)
                throw new NullPointerException(
                        "Input must not be null (recipe " + input + " -> "
                                + output1 + ").");
            if (output1 == null)
                throw new NullPointerException(
                        "Output must not be null (recipe " + input + " -> "
                                + output1 + ").");

            this.ingredients.add(new PositionedStack(input,
                    MachineRecipeHandler.this.getInputPosX(),
                    MachineRecipeHandler.this.getInputPosY()));
            this.output = new PositionedStack(output1,
                    MachineRecipeHandler.this.getOutputPosX(),
                    MachineRecipeHandler.this.getOutputPosY());
        }

        public CachedIORecipe(IMyRecipeInput input, MyRecipeOutput output1) {
            super();
            if (input == null)
                throw new NullPointerException(
                        "Input must not be null (recipe " + input + " -> "
                                + output1 + ").");
            if (output1 == null)
                throw new NullPointerException(
                        "Output must not be null (recipe " + input + " -> "
                                + output1 + ").");
            if (output1.items.isEmpty())
                throw new IllegalArgumentException(
                        "Output must not be empty (recipe " + input + " -> "
                                + output1 + ").");
            if (output1.items.contains(null))
                throw new IllegalArgumentException(
                        "Output must not contain null (recipe " + input
                                + " -> " + output1 + ").");

            List items = new ArrayList();

            for (ItemStack item : input.getInputs()) {
                items.add(StackUtil.copyWithSize(item, input.getInputAmount()));
            }

            this.ingredients.add(new PositionedStack(items,
                    MachineRecipeHandler.this.getInputPosX(),
                    MachineRecipeHandler.this.getInputPosY()));
            this.output = new PositionedStack(output1.items.get(0),
                    MachineRecipeHandler.this.getOutputPosX(),
                    MachineRecipeHandler.this.getOutputPosY());

            for (int i = 1; i < output1.items.size(); i++)
                if (MachineRecipeHandler.this.isOutputsVertical())
                    this.otherStacks
                            .add(new PositionedStack(output1.items.get(i),
                                    MachineRecipeHandler.this.getOutputPosX(),
                                    MachineRecipeHandler.this.getOutputPosY()
                                            + i * 18));
                else
                    this.otherStacks
                            .add(new PositionedStack(output1.items.get(i),
                                    MachineRecipeHandler.this.getOutputPosX()
                                            + i * 18, MachineRecipeHandler.this
                                            .getOutputPosY()));
        }
    }

    public void registerSelf() {
        API.registerRecipeHandler(this);
        API.registerUsageHandler(this);
    }
}
