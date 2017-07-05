/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.machine

import net.minecraft.block.ITileEntityProvider
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import waterpower.client.i18n
import waterpower.common.INameable

enum class Machines(val info: String, val cls: Class<out TileEntityBaseMachine>) : INameable, ITileEntityProvider {
    crusher("80mb/t, 60s, tot: 96000mb, storage: 96000mb", TileEntityCrusher::class.java),
    compressor("2000mb/t, 2s, tot: 80000mb, storage: 80000mb", TileEntityCompressor::class.java),
    sawmill("1000mb/t, 1s, tot: 20000mb, storage: 20000mb", TileEntitySawmill::class.java),
    advanced_compressor("5000mb/t, (itnt)s, tot: ?, storage: 1000000mb", TileEntityAdvCompressor::class.java),
    centrifuge("500*(gt)mb/t, 0.2*(gt)s, tot: ?, storage: 1000000mb", TileEntityCentrifuge::class.java),
    lathe("8000mb/t, 1s, tot: 160000mb, storage: 160000mb", TileEntityLathe::class.java),
    cutter("8000mb/t, 1s, tot: 160000mb, storage: 160000mb", TileEntityCutter::class.java);

    override fun getName() = name
    override fun getUnlocalizedName() = "waterpower.machine.$name"

    override fun addInformation(tooltip: MutableList<String>) {
        tooltip += i18n("waterpower.machine.info")
        tooltip += info
    }

    override fun createNewTileEntity(worldIn: World?, meta: Int): TileEntity? {
        return cls.newInstance()
    }
}