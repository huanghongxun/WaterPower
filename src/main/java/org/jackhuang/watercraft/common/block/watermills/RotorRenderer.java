package org.jackhuang.watercraft.common.block.watermills;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.client.render.ModelWaterWheel;
import org.jackhuang.watercraft.common.item.rotors.RotorType;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RotorRenderer extends TileEntitySpecialRenderer {

	private static final float ratio = 0.0625F;

	public void renderBlockRotor(TileEntityWatermill tileEntity, World world,
			int posX, int posY, int posZ) {
		if (!tileEntity.isActive())
			return;
		if (!tileEntity.hasRotor())
			return;

		GL11.glPushMatrix();
		//GL11.glTranslatef((float) posX, (float) posY, (float) posZ);

		int facing = tileEntity.getFacing();

		float[] rotationAngle = { 0.0F, 0.0F };
		float[] wheelAngle = { 0.0F, 0.0F, 0.0F };
		float wheelf = 0.5F;
		float wheell = 0.25F;
		float wheelk = 0.125F;
		float[] translateWheel = { wheelf, wheelf, wheelf };

		switch (facing) {
		case 5:
			translateWheel[0] += 1.15;
			rotationAngle[0] = -1.0F;
			wheelAngle[2] = 1.0F;
			break;
		case 4:
			translateWheel[0] -= 1.25;
			rotationAngle[0] = -1.0F;
			wheelAngle[2] = -1.0F;
			break;
		case 3:
			translateWheel[2] += 0.5;
			rotationAngle[0] = 1.0F;
			wheelAngle[0] = 1.0F;
			break;
		case 2:
			translateWheel[2] -= 1.15;
			//translateWheel[0] -= 1;
			rotationAngle[0] = 1.0F;
			wheelAngle[0] = 1.0F;
			break;
		}

		GL11.glTranslatef(translateWheel[0], translateWheel[1],
				translateWheel[2]);
		GL11.glRotatef(90.0F, wheelAngle[0], wheelAngle[1], wheelAngle[2]);

		FMLClientHandler.instance().getClient().renderEngine
				.bindTexture(getTexture(tileEntity.getRotor().type));
		tileEntity.model.trunk.render(this.ratio);

		GL11.glRotatef(tileEntity.getWheelAngle(), 0.0F, rotationAngle[0],
				rotationAngle[1]);

		GL11.glTranslatef(0.0F, 0.0F, 0.0F);
		for (int i = 0; i < 360; i += 45) {
			GL11.glRotatef(i, 0.0F, rotationAngle[0], rotationAngle[1]);
			tileEntity.model.renderPlank(this.ratio, tileEntity.getType());
		}
		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double posX,
			double posY, double posZ, float partialTickTime) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) posX, (float) posY, (float) posZ);
		TileEntityWatermill te = (TileEntityWatermill) tileEntity;
		renderBlockRotor(te, te.getWorldObj(), te.xCoord, te.yCoord, te.zCoord);
		GL11.glPopMatrix();
	}

	public ResourceLocation getTexture(RotorType wheel) {
		return new ResourceLocation(Reference.ModID + ":" + "textures/models/" + wheel.name() + ".png");
	}

}
