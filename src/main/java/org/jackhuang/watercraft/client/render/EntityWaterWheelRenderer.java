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
import org.jackhuang.watercraft.util.WCLog;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class EntityWaterWheelRenderer extends Render {
	public ModelWaterWheel model = new ModelWaterWheel();
	float ratio = 0.0625F;

	public void doRender(Entity entity, double x, double y, double z, float f,
			float f1) {
		EntityWaterWheel wheel = (EntityWaterWheel) entity;
		if (wheel.parent == null || wheel.wheelType == null) {
			WCLog.debugLog("rendering terminated...");
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
		float[] translateWheel = { wheelf, wheelf, wheelf };

		switch (facing) {
		case 5:
			translateWheel[0] -= wheelf;
			rotationAngle[0] = -1.0F;
			wheelAngle[2] = 1.0F;
			break;
		case 4:
			translateWheel[0] += wheelf;
			rotationAngle[0] = -1.0F;
			wheelAngle[2] = 1.0F;
			break;
		case 3:
			translateWheel[2] += wheell;
			rotationAngle[0] = 1.0F;
			wheelAngle[0] = 1.0F;
			break;
		case 2:
			translateWheel[2] -= wheell;
			rotationAngle[0] = 1.0F;
			wheelAngle[0] = 1.0F;
			break;
		}

		GL11.glTranslatef(translateWheel[0], translateWheel[1],
				translateWheel[2]);
		GL11.glRotatef(90.0F, wheelAngle[0], wheelAngle[1], wheelAngle[2]);

		FMLClientHandler.instance().getClient().renderEngine
				.bindTexture(getTexture(wheel.wheelType));
		this.model.trunk.render(this.ratio);

		GL11.glRotatef(wheel.wheelAngle, 0.0F, rotationAngle[0],
				rotationAngle[1]);

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