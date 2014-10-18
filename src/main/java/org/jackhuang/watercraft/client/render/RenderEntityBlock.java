package org.jackhuang.watercraft.client.render;

import java.util.Arrays;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RenderEntityBlock
{
  private static RenderBlocks renderBlocks = new RenderBlocks();

  public static void renderBlock(RenderInfo info, IBlockAccess blockAccess, double x, double y, double z, boolean doLight, boolean doTessellating) {
    renderBlock(info, blockAccess, x, y, z, (int)x, (int)y, (int)z, doLight, doTessellating);
  }

  public static void renderBlock(RenderInfo info, IBlockAccess blockAccess, double x, double y, double z, int lightX, int lightY, int lightZ, boolean doLight, boolean doTessellating) {
    float lightBottom = 0.5F;
    float lightTop = 1.0F;
    float lightEastWest = 0.8F;
    float lightNorthSouth = 0.6F;

    Tessellator tessellator = Tessellator.instance;

    if (blockAccess == null) {
      doLight = false;
    }
    if (doTessellating) {
      tessellator.startDrawingQuads();
    }
    float light = 0.0F;
    if (doLight) {
      if (info.light < 0.0F)
      {
        light = 1.0F;
      }
      else light = info.light;
      int brightness = 0;
      if (info.brightness < 0)
        brightness = info.template.getMixedBrightnessForBlock(blockAccess, lightX, lightY, lightZ);
      else
        brightness = info.brightness;
      tessellator.setBrightness(brightness);
      tessellator.setColorOpaque_F(lightBottom * light, lightBottom * light, lightBottom * light);
    }
    else if (info.brightness >= 0) {
      tessellator.setBrightness(info.brightness);
    }

    renderBlocks.setRenderBounds(info.minX, info.minY, info.minZ, info.maxX, info.maxY, info.maxZ);

    if (info.renderSide[0] != false) {
      renderBlocks.renderFaceYNeg(info.template, x, y, z, info.getBlockTextureFromSide(0));
    }
    if (doLight) {
      tessellator.setColorOpaque_F(lightTop * light, lightTop * light, lightTop * light);
    }
    if (info.renderSide[1] != false) {
      renderBlocks.renderFaceYPos(info.template, x, y, z, info.getBlockTextureFromSide(1));
    }
    if (doLight) {
      tessellator.setColorOpaque_F(lightEastWest * light, lightEastWest * light, lightEastWest * light);
    }
    if (info.renderSide[2] != false) {
      renderBlocks.renderFaceZNeg(info.template, x, y, z, info.getBlockTextureFromSide(2));
    }
    if (doLight) {
      tessellator.setColorOpaque_F(lightEastWest * light, lightEastWest * light, lightEastWest * light);
    }
    if (info.renderSide[3] != false) {
      renderBlocks.renderFaceZPos(info.template, x, y, z, info.getBlockTextureFromSide(3));
    }
    if (doLight) {
      tessellator.setColorOpaque_F(lightNorthSouth * light, lightNorthSouth * light, lightNorthSouth * light);
    }
    if (info.renderSide[4] != false) {
      renderBlocks.renderFaceXNeg(info.template, x, y, z, info.getBlockTextureFromSide(4));
    }
    if (doLight) {
      tessellator.setColorOpaque_F(lightNorthSouth * light, lightNorthSouth * light, lightNorthSouth * light);
    }
    if (info.renderSide[5] != false) {
      renderBlocks.renderFaceXPos(info.template, x, y, z, info.getBlockTextureFromSide(5));
    }
    if (doTessellating)
      tessellator.draw();
  }

  public static void renderBlockOnInventory(RenderBlocks renderblocks, RenderInfo info, float light) {
    renderBlockOnInventory(renderblocks, info, light, -1);
  }

  public static void renderBlockOnInventory(RenderBlocks renderer, RenderInfo info, float light, int side) {
    if (side >= 0)
      info.setRenderSingleSide(side);
    Block block = info.template;
    Tessellator tessellator = Tessellator.instance;
    if (renderer.useInventoryTint) {
      int j = block.getRenderColor(9);
      float red = (j >> 16 & 0xFF) / 255.0F;
      float green = (j >> 8 & 0xFF) / 255.0F;
      float blue = (j & 0xFF) / 255.0F;
      GL11.glColor4f(red * light, green * light, blue * light, 1.0F);
    }
    renderer.setRenderBounds(info.minX, info.minY, info.minZ, info.maxX, info.maxY, info.maxZ);
    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
    int s = 0;
    if (info.renderSide[s] != false) {
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, -1.0F, 0.0F);
      IIcon icon = info.getBlockTextureFromSide(s);
      if (icon != null)
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, icon);
      tessellator.draw();
    }
    s = 1;
    if (info.renderSide[s] != false) {
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 1.0F, 0.0F);
      IIcon icon = info.getBlockTextureFromSide(s);
      if (icon != null)
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, icon);
      tessellator.draw();
    }
    s = 2;
    if (info.renderSide[s] != false) {
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 0.0F, -1.0F);
      IIcon icon = info.getBlockTextureFromSide(s);
      if (icon != null)
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, icon);
      tessellator.draw();
    }
    s = 3;
    if (info.renderSide[s] != false) {
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 0.0F, 1.0F);
      IIcon icon = info.getBlockTextureFromSide(s);
      if (icon != null)
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, icon);
      tessellator.draw();
    }
    s = 4;
    if (info.renderSide[s] != false) {
      tessellator.startDrawingQuads();
      tessellator.setNormal(-1.0F, 0.0F, 0.0F);
      IIcon icon = info.getBlockTextureFromSide(s);
      if (icon != null)
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, icon);
      tessellator.draw();
    }
    s = 5;
    if (info.renderSide[s] != false) {
      tessellator.startDrawingQuads();
      tessellator.setNormal(1.0F, 0.0F, 0.0F);
      IIcon icon = info.getBlockTextureFromSide(s);
      if (icon != null)
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, icon);
      tessellator.draw();
    }

    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    if (side >= 0)
      info.setRenderAllSides();
  }

  public static class RenderInfo
  {
    public Block template = Blocks.stone;
    public IIcon[] texture = null;
    public IIcon override = null;
    public float minX = 0.0F;
    public float minY = 0.0F;
    public float minZ = 0.0F;
    public float maxX = 1.0F;
    public float maxY = 1.0F;
    public float maxZ = 1.0F;
    public boolean[] renderSide = new boolean[6];
    public float light = -1.0F;
    public int brightness = -1;

    public RenderInfo() {
      setRenderAllSides();
    }

    public RenderInfo(Block template, IIcon[] texture) {
      this();
      this.template = template;
      this.texture = texture;
    }

    public RenderInfo(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
      this();
      setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public final void setBlockBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
      this.minX = minX;
      this.minY = minY;
      this.minZ = minZ;
      this.maxX = maxX;
      this.maxY = maxY;
      this.maxZ = maxZ;
    }

    public final void setRenderSingleSide(int side) {
      Arrays.fill(this.renderSide, false);
      this.renderSide[side] = true;
    }

    public final void setRenderAllSides() {
      Arrays.fill(this.renderSide, true);
    }

    public void rotate() {
      float temp = this.minX;
      this.minX = this.minZ;
      this.minZ = temp;

      temp = this.maxX;
      this.maxX = this.maxZ;
      this.maxZ = temp;
    }

    public void reverseX() {
      float temp = this.minX;
      this.minX = (1.0F - this.maxX);
      this.maxX = (1.0F - temp);
    }

    public void reverseZ() {
      float temp = this.minZ;
      this.minZ = (1.0F - this.maxZ);
      this.maxZ = (1.0F - temp);
    }

    public IIcon getBlockTextureFromSide(int i) {
      if (this.override != null)
        return this.override;
      if ((this.texture == null) || (this.texture.length == 0)) {
        return this.template.getBlockTextureFromSide(i);
      }
      if (i >= this.texture.length)
        i = 0;
      return this.texture[i];
    }
  }
}
