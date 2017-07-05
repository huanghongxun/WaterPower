/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.client.render

import com.google.common.base.Optional
import com.google.common.collect.ImmutableList
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.*
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.renderer.vertex.VertexFormat
import net.minecraft.client.renderer.vertex.VertexFormatElement
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.client.model.IModel
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.model.IModelState
import net.minecraftforge.common.model.TRSRTransformation
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import waterpower.WaterPower
import waterpower.annotations.Init
import waterpower.client.render.item.IItemIconProvider
import waterpower.common.init.WPItems
import java.util.*
import javax.vecmath.Vector4f

@Init
@SideOnly(Side.CLIENT)
object IconRegisterService : IBakedModel, IModel {

    val items = LinkedList<IIconRegister>()

    @SubscribeEvent
    fun onPreTextureMapStitch(event: TextureStitchEvent.Pre) {
        val map = event.map
        for (runnable in items)
            runnable.registerIcons(map)
        for (item in WPItems.items)
            if (item is IIconRegister)
                item.registerIcons(map)
    }

    @SubscribeEvent
    fun onModelsBake(bakeEvent: ModelBakeEvent) {
        bakeEvent.modelRegistry.putObject(RESOURCE_LOCATION, this)
    }

    @JvmStatic
    @SideOnly(Side.CLIENT)
    fun preInit() {
        MinecraftForge.EVENT_BUS.register(this)
    }

    private var itemStack: ItemStack? = null
    private val vertexFormat = DefaultVertexFormats.ITEM

    override fun getQuads(state: IBlockState?, side: EnumFacing?, rand: Long): List<BakedQuad> {
        val stack = itemStack
        if (side == null && stack != null) {
            val resultQuads = ArrayList<BakedQuad>()
            val iconProvider = stack.item as IItemIconProvider
            val layer = iconProvider.getRenderLayer(stack)
            for (i in 0..layer) {
                val atlasSprite = iconProvider.getIcon(stack, i)
                if (atlasSprite != null) {
                    resultQuads.addAll(getQuadsForSprite(i, atlasSprite, vertexFormat, Optional.absent<TRSRTransformation>()))
                }
            }
            return resultQuads
        }
        return emptyList()
    }

    override fun isAmbientOcclusion() = false

    override fun isGui3d() = false

    override fun isBuiltInRenderer() = false

    override fun getParticleTexture(): TextureAtlasSprite? {
        if (itemStack != null) {
            val iconProvider = itemStack!!.item as IItemIconProvider
            return iconProvider.getIcon(itemStack!!, 0)
        }
        return null
    }

    override fun getItemCameraTransforms(): ItemCameraTransforms {
        val stack = itemStack
        if (stack != null) {
            val iconProvider = stack.item as IItemIconProvider
            return if (iconProvider.isHandHeld(stack)) Models.HANDHELD_TRANSFORMS else Models.DEFAULT_TRANSFORMS
        }
        return ItemCameraTransforms.DEFAULT
    }

    override fun getOverrides(): ItemOverrideList {
        return object : ItemOverrideList(emptyList<ItemOverride>()) {
            override fun handleItemState(originalModel: IBakedModel, stack: ItemStack, world: World?, entity: EntityLivingBase?): IBakedModel? {
                if (stack.item is ItemBlock)
                    return null

                this@IconRegisterService.itemStack = stack
                return this@IconRegisterService
            }
        }
    }

    //-----------------------------------------
    // IBakedModel
    //-----------------------------------------

    private val RESOURCE_LOCATION = ModelResourceLocation(WaterPower.MOD_ID, "IItemIconProvider")

    @JvmStatic
    @SideOnly(Side.CLIENT)
    fun init() {
        for (item in WPItems.items)
            if (item is IItemIconProvider)
                for (i in 0 until Short.MAX_VALUE)
                    Minecraft.getMinecraft().renderItem.itemModelMesher.register(item, i, RESOURCE_LOCATION)
    }

    fun getQuadsForSprite(tint: Int, sprite: TextureAtlasSprite, format: VertexFormat, transform: Optional<TRSRTransformation>): ImmutableList<BakedQuad> {
        val builder = ImmutableList.builder<BakedQuad>()

        val uMax = sprite.iconWidth
        val vMax = sprite.iconHeight

        val faces = BitSet((uMax + 1) * (vMax + 1) * 4)
        for (f in 0..sprite.frameCount - 1) {
            val pixels = sprite.getFrameTextureData(f)[0]
            var ptu: Boolean
            val ptv = BooleanArray(uMax, { true })
            for (v in 0..vMax - 1) {
                ptu = true
                for (u in 0..uMax - 1) {
                    val t = isTransparent(pixels, uMax, vMax, u, v)
                    // left - transparent, right - opaque
                    if (ptu && !t)
                        addSideQuad(builder, faces, format, transform, EnumFacing.WEST, tint, sprite, uMax, vMax, u, v)
                    // left - opaque, right - transparent
                    if (!ptu && t)
                        addSideQuad(builder, faces, format, transform, EnumFacing.EAST, tint, sprite, uMax, vMax, u, v)
                    // up - transparent, down - opaque
                    if (ptv[u] && !t)
                        addSideQuad(builder, faces, format, transform, EnumFacing.UP, tint, sprite, uMax, vMax, u, v)
                    // up - opaque, down - transparent
                    if (!ptv[u] && t)
                        addSideQuad(builder, faces, format, transform, EnumFacing.DOWN, tint, sprite, uMax, vMax, u, v)
                    ptu = t
                    ptv[u] = t
                }
                // last - opaque
                if (!ptu)
                    addSideQuad(builder, faces, format, transform, EnumFacing.EAST, tint, sprite, uMax, vMax, uMax, v)
            }
            // last line
            for (u in 0..uMax - 1)
                if (!ptv[u])
                    addSideQuad(builder, faces, format, transform, EnumFacing.DOWN, tint, sprite, uMax, vMax, u, vMax)
        }
        // front
        builder.add(buildQuad(format, transform, EnumFacing.NORTH, sprite, tint,
                0f, 0f, 7.5f / 16f, sprite.minU, sprite.maxV,
                0f, 1f, 7.5f / 16f, sprite.minU, sprite.minV,
                1f, 1f, 7.5f / 16f, sprite.maxU, sprite.minV,
                1f, 0f, 7.5f / 16f, sprite.maxU, sprite.maxV
        ))
        // back
        builder.add(buildQuad(format, transform, EnumFacing.SOUTH, sprite, tint,
                0f, 0f, 8.5f / 16f, sprite.minU, sprite.maxV,
                1f, 0f, 8.5f / 16f, sprite.maxU, sprite.maxV,
                1f, 1f, 8.5f / 16f, sprite.maxU, sprite.minV,
                0f, 1f, 8.5f / 16f, sprite.minU, sprite.minV
        ))
        return builder.build()
    }

    private fun isTransparent(pixels: IntArray, uMax: Int, vMax: Int, u: Int, v: Int): Boolean {
        return pixels[u + (vMax - 1 - v) * uMax] shr 24 and 0xFF == 0
    }

    private fun addSideQuad(builder: ImmutableList.Builder<BakedQuad>, faces: BitSet, format: VertexFormat, transform: Optional<TRSRTransformation>, side: EnumFacing, tint: Int, sprite: TextureAtlasSprite, uMax: Int, vMax: Int, u: Int, v: Int) {
        var si = side.ordinal
        if (si > 4) si -= 2
        val index = (vMax + 1) * ((uMax + 1) * si + u) + v
        if (!faces.get(index)) {
            faces.set(index)
            builder.add(buildSideQuad(format, transform, side, tint, sprite, u, v))
        }
    }

    private fun buildSideQuad(format: VertexFormat, transform: Optional<TRSRTransformation>, side: EnumFacing, tint: Int, sprite: TextureAtlasSprite, u: Int, v: Int): BakedQuad {
        val eps0 = 30e-5f
        val eps1 = 45e-5f
        val eps2 = .5f
        val eps3 = .5f
        var x0 = u.toFloat() / sprite.iconWidth
        var y0 = v.toFloat() / sprite.iconHeight
        var x1 = x0
        var y1 = y0
        var z1 = 7.5f / 16f - eps1
        var z2 = 8.5f / 16f + eps1
        when (side) {
            EnumFacing.WEST -> {
                z1 = 8.5f / 16f + eps1
                z2 = 7.5f / 16f - eps1
                y1 = (v + 1f) / sprite.iconHeight
            }
            EnumFacing.EAST -> y1 = (v + 1f) / sprite.iconHeight
            EnumFacing.DOWN -> {
                z1 = 8.5f / 16f + eps1
                z2 = 7.5f / 16f - eps1
                x1 = (u + 1f) / sprite.iconWidth
            }
            EnumFacing.UP -> x1 = (u + 1f) / sprite.iconWidth
            else -> throw IllegalArgumentException("can't handle z-oriented side")
        }
        var u0 = 16f * (x0 - side.directionVec.x * eps3 / sprite.iconWidth)
        var u1 = 16f * (x1 - side.directionVec.x * eps3 / sprite.iconWidth)
        var v0 = 16f * (1f - y0 - side.directionVec.y * eps3 / sprite.iconHeight)
        var v1 = 16f * (1f - y1 - side.directionVec.y * eps3 / sprite.iconHeight)
        when (side) {
            EnumFacing.WEST, EnumFacing.EAST -> {
                y0 -= eps1
                y1 += eps1
                v0 -= eps2 / sprite.iconHeight
                v1 += eps2 / sprite.iconHeight
            }
            EnumFacing.DOWN, EnumFacing.UP -> {
                x0 -= eps1
                x1 += eps1
                u0 += eps2 / sprite.iconWidth
                u1 -= eps2 / sprite.iconWidth
            }
            else -> throw IllegalArgumentException("can't handle z-oriented side")
        }
        when (side) {
            EnumFacing.WEST -> {
                x0 += eps0
                x1 += eps0
            }
            EnumFacing.EAST -> {
                x0 -= eps0
                x1 -= eps0
            }
            EnumFacing.DOWN -> {
                y0 -= eps0
                y1 -= eps0
            }
            EnumFacing.UP -> {
                y0 += eps0
                y1 += eps0
            }
            else -> throw IllegalArgumentException("can't handle z-oriented side")
        }
        return buildQuad(
                format, transform, side.opposite, sprite, tint, // getOpposite is related either to the swapping of V direction, or something else
                x0, y0, z1, sprite.getInterpolatedU(u0.toDouble()), sprite.getInterpolatedV(v0.toDouble()),
                x1, y1, z1, sprite.getInterpolatedU(u1.toDouble()), sprite.getInterpolatedV(v1.toDouble()),
                x1, y1, z2, sprite.getInterpolatedU(u1.toDouble()), sprite.getInterpolatedV(v1.toDouble()),
                x0, y0, z2, sprite.getInterpolatedU(u0.toDouble()), sprite.getInterpolatedV(v0.toDouble())
        )
    }

    private fun buildQuad(
            format: VertexFormat, transform: Optional<TRSRTransformation>, side: EnumFacing, sprite: TextureAtlasSprite, tint: Int,
            x0: Float, y0: Float, z0: Float, u0: Float, v0: Float,
            x1: Float, y1: Float, z1: Float, u1: Float, v1: Float,
            x2: Float, y2: Float, z2: Float, u2: Float, v2: Float,
            x3: Float, y3: Float, z3: Float, u3: Float, v3: Float): BakedQuad {
        val builder = UnpackedBakedQuad.Builder(format)
        builder.setQuadTint(tint)
        builder.setQuadOrientation(side)
        builder.setTexture(sprite)
        putVertex(builder, format, transform, side, x0, y0, z0, u0, v0)
        putVertex(builder, format, transform, side, x1, y1, z1, u1, v1)
        putVertex(builder, format, transform, side, x2, y2, z2, u2, v2)
        putVertex(builder, format, transform, side, x3, y3, z3, u3, v3)
        return builder.build()
    }

    private fun putVertex(builder: UnpackedBakedQuad.Builder, format: VertexFormat, transform: Optional<TRSRTransformation>, side: EnumFacing, x: Float, y: Float, z: Float, u: Float, v: Float) {
        val vec = Vector4f()
        for (e in 0..format.elementCount - 1) {
            when (format.getElement(e).usage) {
                VertexFormatElement.EnumUsage.POSITION -> if (transform.isPresent) {
                    vec.x = x
                    vec.y = y
                    vec.z = z
                    vec.w = 1f
                    transform.get().matrix.transform(vec)
                    builder.put(e, vec.x, vec.y, vec.z, vec.w)
                } else {
                    builder.put(e, x, y, z, 1.0f)
                }
                VertexFormatElement.EnumUsage.COLOR -> builder.put(e, 1f, 1f, 1f, 1f)
                VertexFormatElement.EnumUsage.UV -> {
                    if (format.getElement(e).index == 0)
                        builder.put(e, u, v, 0f, 1f)
                    else
                        builder.put(e, side.frontOffsetX.toFloat(), side.frontOffsetY.toFloat(), side.frontOffsetZ.toFloat(), 0f)
                }
                VertexFormatElement.EnumUsage.NORMAL -> builder.put(e, side.frontOffsetX.toFloat(), side.frontOffsetY.toFloat(), side.frontOffsetZ.toFloat(), 0f)
                else -> builder.put(e)
            }
        }
    }

    override fun bake(state: IModelState?, format: VertexFormat?, bakedTextureGetter: java.util.function.Function<ResourceLocation, TextureAtlasSprite>?) = this

    override fun getTextures(): MutableCollection<ResourceLocation> = mutableListOf()

    override fun getDefaultState(): IModelState = TRSRTransformation.identity()

    override fun getDependencies(): MutableCollection<ResourceLocation> = mutableListOf()
}