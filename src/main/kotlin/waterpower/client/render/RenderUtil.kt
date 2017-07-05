/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.client.render

import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.renderer.vertex.VertexFormat
import net.minecraft.client.renderer.vertex.VertexFormatElement
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*

@SideOnly(Side.CLIENT)
private val BLOCK_TEXTURE = TextureMap.LOCATION_BLOCKS_TEXTURE

@SideOnly(Side.CLIENT)
fun renderBlock(state: IBlockState, pos: BlockPos) {

    val tessellator = Tessellator.getInstance()

    val vertexbuffer = tessellator.buffer

    vertexbuffer.begin(GL_QUADS, DefaultVertexFormats.BLOCK)

    val minecraft = Minecraft.getMinecraft()

    val dispatcher = minecraft.blockRendererDispatcher

    dispatcher.renderBlock(state, pos, minecraft.world, vertexbuffer)

    tessellator.draw()

}

@SideOnly(Side.CLIENT)
fun getRenderPartialTicks(): Float {
    return Minecraft.getMinecraft().renderPartialTicks
}

@SideOnly(Side.CLIENT)
fun calculateRenderOffset(now: Float, last: Float, partialTicks: Float): Float {
    return last + (now - last) * partialTicks
}

@SideOnly(Side.CLIENT)
fun translateToZero() {
    val player = Minecraft.getMinecraft().player
    val partialTicks = getRenderPartialTicks()
    glTranslatef(
            -calculateRenderOffset(player.posX.toFloat(), player.lastTickPosX.toFloat(), partialTicks),
            -calculateRenderOffset(player.posY.toFloat(), player.lastTickPosY.toFloat(), partialTicks),
            -calculateRenderOffset(player.posZ.toFloat(), player.lastTickPosZ.toFloat(), partialTicks)
    )
}

@SideOnly(Side.CLIENT)
fun scaleAndCorrectThePosition(x: Float, y: Float, z: Float, dx: Float, dy: Float, dz: Float) {

    glTranslatef(dx * (1f - x), dy * (1f - y), dz * (1f - z))

    glScalef(x, y, z)

}

@SideOnly(Side.CLIENT)
fun sprite2Container(sprite: TextureAtlasSprite): IIconContainer {
    return object : IIconContainer {
        @SideOnly(Side.CLIENT)
        override fun getIcon(): TextureAtlasSprite? {
            return sprite
        }

        @SideOnly(Side.CLIENT)
        override fun getOverlayIcon(): TextureAtlasSprite? {
            return sprite
        }
    }
}

@SideOnly(Side.CLIENT)
fun getTexture(location: String): TextureAtlasSprite {
    return Minecraft.getMinecraft().textureMapBlocks.getAtlasSprite(location)
}

@SideOnly(Side.CLIENT)
fun getTexture(location: ResourceLocation): TextureAtlasSprite {
    return Minecraft.getMinecraft().textureMapBlocks.getAtlasSprite(location.toString())
}

@SideOnly(Side.CLIENT)
fun renderSide(vertexFormat: VertexFormat, sprite: TextureAtlasSprite, side: EnumFacing, tint: Int, offset: Float, color: Int, hideSiding: Boolean): BakedQuad {
    when (side) {
        EnumFacing.NORTH -> return buildQuad(vertexFormat, if (hideSiding) null else EnumFacing.NORTH, sprite, tint,
                0f, 0f, -offset, sprite.maxU, sprite.maxV,
                0f, 1f, -offset, sprite.maxU, sprite.minV,
                1f, 1f, -offset, sprite.minU, sprite.minV,
                1f, 0f, -offset, sprite.minU, sprite.maxV,
                color)
        EnumFacing.SOUTH -> return buildQuad(vertexFormat, if (hideSiding) null else EnumFacing.SOUTH, sprite, tint,
                1f, 0f, 1 + offset, sprite.minU, sprite.maxV,
                1f, 1f, 1 + offset, sprite.minU, sprite.minV,
                0f, 1f, 1 + offset, sprite.maxU, sprite.minV,
                0f, 0f, 1 + offset, sprite.maxU, sprite.maxV,
                color)
        EnumFacing.WEST -> return buildQuad(vertexFormat, if (hideSiding) null else EnumFacing.WEST, sprite, tint,
                -offset, 0f, 1f, sprite.maxU, sprite.maxV,
                -offset, 1f, 1f, sprite.maxU, sprite.minV,
                -offset, 1f, 0f, sprite.minU, sprite.minV,
                -offset, 0f, 0f, sprite.minU, sprite.maxV,
                color)
        EnumFacing.EAST -> return buildQuad(vertexFormat, if (hideSiding) null else EnumFacing.EAST, sprite, tint,
                1 + offset, 0f, 0f, sprite.maxU, sprite.maxV,
                1 + offset, 1f, 0f, sprite.maxU, sprite.minV,
                1 + offset, 1f, 1f, sprite.minU, sprite.minV,
                1 + offset, 0f, 1f, sprite.minU, sprite.maxV,
                color)
        EnumFacing.DOWN -> return buildQuad(vertexFormat, if (hideSiding) null else EnumFacing.DOWN, sprite, tint,
                0f, -offset, 0f, sprite.minU, sprite.minV,
                1f, -offset, 0f, sprite.maxU, sprite.minV,
                1f, -offset, 1f, sprite.maxU, sprite.maxV,
                0f, -offset, 1f, sprite.minU, sprite.maxV,
                color)
        EnumFacing.UP -> return buildQuad(vertexFormat, if (hideSiding) null else EnumFacing.UP, sprite, tint,
                0f, 1 + offset, 1f, sprite.minU, sprite.maxV,
                1f, 1 + offset, 1f, sprite.maxU, sprite.maxV,
                1f, 1 + offset, 0f, sprite.maxU, sprite.minV,
                0f, 1 + offset, 0f, sprite.minU, sprite.minV,
                color)

        else -> {
            println("Can't render side " + side)
            throw IllegalArgumentException()
        }
    }
}

@SideOnly(Side.CLIENT)
fun renderQuadCustom(x: Float, y: Float, z: Float, width: Float, height: Float, vertexFormat: VertexFormat, sprite: TextureAtlasSprite, side: EnumFacing, tint: Int, color: Int, hide: Boolean): BakedQuad? {
    when (side) {
        EnumFacing.NORTH -> return buildQuad(vertexFormat, EnumFacing.NORTH, sprite, tint,
                x, y, z, sprite.maxU, sprite.maxV,
                x, y + height, z, sprite.maxU, sprite.minV,
                x + width, y + height, z, sprite.minU, sprite.minV,
                x + width, y, z, sprite.minU, sprite.maxV,
                color)
        EnumFacing.SOUTH -> return buildQuad(vertexFormat, EnumFacing.SOUTH, sprite, tint,
                x + width, y, z, sprite.minU, sprite.maxV,
                x + width, y + height, z, sprite.minU, sprite.minV,
                x, y + height, z, sprite.maxU, sprite.minV,
                x, y, z, sprite.maxU, sprite.maxV,
                color)
        EnumFacing.WEST -> return buildQuad(vertexFormat, EnumFacing.WEST, sprite, tint,
                x, y, z + height, sprite.maxU, sprite.maxV,
                x, y + width, z + height, sprite.maxU, sprite.minV,
                x, y + width, z, sprite.minU, sprite.minV,
                x, y, z, sprite.minU, sprite.maxV,
                color)
        EnumFacing.EAST -> return buildQuad(vertexFormat, EnumFacing.EAST, sprite, tint,
                x, y, z, sprite.maxU, sprite.maxV,
                x, y + width, z, sprite.maxU, sprite.minV,
                x, y + width, z + height, sprite.minU, sprite.minV,
                x, y, z + height, sprite.minU, sprite.maxV,
                color)
        EnumFacing.DOWN -> return buildQuad(vertexFormat, EnumFacing.DOWN, sprite, tint,
                x, y, z, sprite.minU, sprite.minV,
                x + width, y, z, sprite.maxU, sprite.minV,
                x + width, y, z + height, sprite.maxU, sprite.maxV,
                x, y, z + height, sprite.minU, sprite.maxV,
                color)
        EnumFacing.UP -> return buildQuad(vertexFormat, if (hide) null else EnumFacing.UP, sprite, tint,
                x, y, z + height, sprite.minU, sprite.maxV,
                x + width, y, z + height, sprite.maxU, sprite.maxV,
                x + width, y, z, sprite.maxU, sprite.minV,
                x, y, z, sprite.minU, sprite.minV,
                color)

        else -> {
            println("Can't render side " + side)
            return null
        }
    }
}

@SideOnly(Side.CLIENT)
private fun buildQuad(
        format: VertexFormat, side: EnumFacing?, sprite: TextureAtlasSprite, tint: Int,
        x0: Float, y0: Float, z0: Float, u0: Float, v0: Float,
        x1: Float, y1: Float, z1: Float, u1: Float, v1: Float,
        x2: Float, y2: Float, z2: Float, u2: Float, v2: Float,
        x3: Float, y3: Float, z3: Float, u3: Float, v3: Float, color: Int): BakedQuad {
    val builder = UnpackedBakedQuad.Builder(format)
    builder.setQuadTint(tint)
    builder.setQuadOrientation(side)
    builder.setTexture(sprite)

    putVertex(builder, format, side, x0, y0, z0, u0, v0, color)
    putVertex(builder, format, side, x1, y1, z1, u1, v1, color)
    putVertex(builder, format, side, x2, y2, z2, u2, v2, color)
    putVertex(builder, format, side, x3, y3, z3, u3, v3, color)
    return builder.build()
}

@SideOnly(Side.CLIENT)
private fun putVertex(builder: UnpackedBakedQuad.Builder, format: VertexFormat, side: EnumFacing?, x: Float, y: Float, z: Float, u: Float, v: Float, color: Int) {
    for (e in 0..format.getElementCount() - 1) {
        when (format.getElement(e).usage) {
            VertexFormatElement.EnumUsage.POSITION -> builder.put(e, x, y, z, 1.0f)
            VertexFormatElement.EnumUsage.COLOR -> builder.put(e,
                    (color shr 16 and 0xFF) / 255.0f,
                    (color shr 8 and 0xFF) / 255.0f,
                    (color and 0xFF) / 255.0f, 1f)
            VertexFormatElement.EnumUsage.UV -> {
                if (format.getElement(e).index == 0)
                    builder.put(e, u, v, 0f, 1f)
                else
                    builder.put(e, 0.0f, 0.0f, 0.0f, 1.0f)
            }
            VertexFormatElement.EnumUsage.NORMAL -> builder.put(e, 0.0f, 0.0f, 0.0f, 1.0f)
            else -> builder.put(e)
        }
    }
}

@SideOnly(Side.CLIENT)
fun getSafeIcon(icon: TextureAtlasSprite?): TextureAtlasSprite {
    if (icon == null)
        return getMissingIcon()
    return icon
}

@SideOnly(Side.CLIENT)
fun getMissingIcon(): TextureAtlasSprite {
    return Minecraft.getMinecraft().textureMapBlocks.missingSprite
}

@SideOnly(Side.CLIENT)
fun getFluidTexture(stack: FluidStack?, flowing: Boolean): TextureAtlasSprite {
    if (stack == null || stack.fluid == null) return getMissingIcon()
    return getTexture(if (flowing) stack.fluid.getFlowing(stack).toString() else stack.fluid.getStill(stack).toString())

}

@SideOnly(Side.CLIENT)
fun getFluidSheet(fluid: Fluid?): ResourceLocation {
    return BLOCK_TEXTURE
}

@SideOnly(Side.CLIENT)
fun setColor(color: Int) {
    val red = (color shr 16 and 0xFF) / 255.0f
    val green = (color shr 8 and 0xFF) / 255.0f
    val blue = (color and 0xFF) / 255.0f
    GL11.glColor4f(red, green, blue, 1.0f)
}