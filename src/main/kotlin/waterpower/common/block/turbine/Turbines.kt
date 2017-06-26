/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.turbine

import net.minecraft.block.ITileEntityProvider
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import waterpower.client.i18n
import waterpower.common.INameable

enum class Turbines(private val _tier: Int, private val teClass: Class<out TileEntityTurbine>) : INameable, ITileEntityProvider {
    MK1(3, TileEntityTurbineMK1::class.java),
    MK2(4, TileEntityTurbineMK2::class.java),
    MK3(5, TileEntityTurbineMK3::class.java),
    MK4(6, TileEntityTurbineMK4::class.java),
    MK5(7, TileEntityTurbineMK5::class.java),
    MK6(8, TileEntityTurbineMK6::class.java),
    MK7(9, TileEntityTurbineMK7::class.java),
    MK8(10, TileEntityTurbineMK8::class.java),
    MK9(11, TileEntityTurbineMK9::class.java),
    MK10(12, TileEntityTurbineMK10::class.java),
    MK11(13, TileEntityTurbineMK11::class.java),
    MK12(14, TileEntityTurbineMK12::class.java);

    fun getTier() = if (_tier < 0) 0 else _tier

    fun getOutput() = if (_tier == 14) 2147483647 else if (_tier < 0) 8 shr ((-_tier) * 2) else 8 shl (_tier * 2)

    override fun getName() = name.toLowerCase()

    override fun getUnlocalizedName() = "waterpower.turbine.${getName()}"

    override fun getLocalizedName() = i18n("waterpower.watermill.turbine") + " " + name

    override fun createNewTileEntity(worldIn: World?, meta: Int): TileEntity = teClass.newInstance()
}