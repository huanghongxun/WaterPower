package org.jackhuang.watercraft.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

import org.jackhuang.watercraft.common.block.watermills.WaterType;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelWaterWheel extends ModelBase {
    public ModelRenderer trunk;
    public ModelRenderer plank;
    private int plankPreviousLength = -1;

    public ModelWaterWheel() {
        this.trunk = new ModelRenderer(this, 0, 0);
        this.trunk.setRotationPoint(0.0F, 0.0F, 0.0F);

        this.trunk.addBox(0.0F, 0.0F, 0.0F, 1, 12, 1);
        this.trunk.addBox(-1.0F, 0.0F, 0.0F, 1, 12, 1);
        this.trunk.addBox(0.0F, 0.0F, -1.0F, 1, 12, 1);
        this.trunk.addBox(-1.0F, 0.0F, -1.0F, 1, 12, 1);

        /*
         * this.plank = new ModelRenderer[WaterType.values().length]; for (int i
         * = 0; i < plank.length; i++) { }
         */
    }

    public void initPlankWithLength(int length) {
        if (length == plankPreviousLength)
            return;
        this.plank = new ModelRenderer(this, 0, 0);
        this.plank.addBox(-1.0F, 0.0F, 1.0F, 2, 10, length * 6);
        this.plank.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    public void render(float f) {
        this.trunk.render(f);
        this.plank.render(f);
    }

    public void renderPlank(float f, WaterType type) {
        this.plank.render(f);
    }

    public void renderTrunk(float f) {
        this.trunk.render(f);
    }
}