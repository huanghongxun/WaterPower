package org.jackhuang.watercraft.client.render;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.common.block.watermills.TileEntityWatermill;
import org.jackhuang.watercraft.common.entity.EntityWaterWheel;
import org.jackhuang.watercraft.common.item.rotors.RotorType;
import org.jackhuang.watercraft.util.WPLog;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class EntityWaterWheelRenderer extends Render {
    public ModelWaterWheel model = new ModelWaterWheel();
    float ratio = 0.0625F;

    public void doRender(Entity entity, double x, double y, double z, float f,
            float f1) {
        EntityWaterWheel wheel = (EntityWaterWheel) entity;
        if (wheel.parent == null || wheel.wheelType == null) {
            WPLog.debugLog("rendering terminated...");
            return;
        }
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y, (float) z);

        TileEntityWatermill tile = wheel.parent;

        int facing = wheel.parentFacing;

        float[] rotationAngle = { 0.0F, 0.0F };
        float[] wheelAngle = { 0.0F, 0.0F, 0.0F };
        float wheelf = 0.5F;
        float wheell = 0.25F;
        float wheelk = 0.125F;
        float wheelRotate = 90f;
        float[] translateWheel = { wheelf, wheelf, wheelf };

        //System.out.println(facing);
        switch (facing) {
        case 5:
            translateWheel[0] += 0.25;
            rotationAngle[0] = -1.0F;
            wheelAngle[2] = 1.0F;
            break;
        case 4:
            translateWheel[0] -= 1.25f;
            translateWheel[2] += 1f;
            rotationAngle[0] = -1F;
            wheelAngle[2] = -1.5F;
            break;
        case 3:
            translateWheel[2] += 0.25;
            rotationAngle[0] = -2.0f;
            wheelAngle[0] = 1.0F;
            wheelRotate = -90f;
            break;
        case 2:
            translateWheel[2] -= 1.25;
            translateWheel[1] += 1;
            rotationAngle[0] = 1.0F;
            wheelAngle[0] = 1.0F;
            break;
        }

        GL11.glTranslatef(translateWheel[0], translateWheel[1],
                translateWheel[2]);
        GL11.glRotatef(wheelRotate, wheelAngle[0], wheelAngle[1], wheelAngle[2]);

        FMLClientHandler.instance().getClient().renderEngine
                .bindTexture(getTexture(wheel.wheelType));

        GL11.glRotatef(wheel.parent.getWheelAngle(), 0.0F, rotationAngle[0],
                rotationAngle[1]);
        
        this.model.trunk.render(this.ratio);
        
        this.model.initPlankWithLength(wheel.parent.getRange());

        GL11.glTranslatef(0.0F, 0.0F, 0.0F);
        for (int i = 0; i < 360; i += 45) {
            GL11.glRotatef(i, 0.0F, rotationAngle[0], rotationAngle[1]);
            this.model.renderPlank(this.ratio, wheel.parent.getType());
        }
        GL11.glPopMatrix();
    }

    public ResourceLocation getTexture(RotorType wheel) {
        return new ResourceLocation(Reference.ModID + ":" + "textures/models/" + wheel.name() + ".png");
    }

    protected ResourceLocation getEntityTexture(Entity entity) {
        return getTexture(((EntityWaterWheel) entity).wheelType);
    }
}