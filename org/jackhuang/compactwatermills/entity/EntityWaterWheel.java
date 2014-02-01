package org.jackhuang.compactwatermills.entity;

import org.jackhuang.compactwatermills.block.watermills.TileEntityWatermill;
import org.jackhuang.compactwatermills.item.rotors.RotorType;
import org.jackhuang.compactwatermills.util.Utilities;

import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3Pool;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.ForgeDirection;

public class EntityWaterWheel extends Entity {
	public RotorType wheelType;
	public TileEntityWatermill parent;
	private int xParent;
	private int yParent;
	private int zParent;
	public int parentFacing;
	public float wheelAngle = 0.0F;
	public boolean canEngineProduce = false;

	public EntityWaterWheel(World world) {
		super(world);
		this.noClip = false;
	}

	public EntityWaterWheel(World world, TileEntityWatermill parent) {
		super(world);

		this.parent = parent;
		this.parentFacing = parent.getFacing();
		this.noClip = false;

		this.xParent = parent.xCoord;
		this.yParent = parent.yCoord;
		this.zParent = parent.zCoord;

		int[] actualCoords = Utilities.moveForward(world, parent.xCoord,
				parent.yCoord, parent.zCoord, parent
						.getFacing(), 1);
		setPosition(actualCoords[0], actualCoords[1], actualCoords[2]);

		this.boundingBox.setBB(getBoundingBox());
	}

	protected void entityInit() {
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		this.parent = ((TileEntityWatermill) this.worldObj.getBlockTileEntity(
				this.xParent, this.yParent, this.zParent));
		if ((this.parent == null)
				|| (!this.parent.hasRotor())
				|| (this.parent.getFacing() != this.parentFacing))
			destroy();
		updateParameters();
	}

	@Override
	public AxisAlignedBB getBoundingBox() {
		double halfS = (this.parent.getType().length - 1) / 2;
		double def = 1.0D;
		double[] exp = { 0.0D, 0.0D };

		if (this.parent == null) {
			return null;
		}
		int facing = this.parent.getFacing();

		switch (facing) {
		case 3:
		case 2:
			exp[0] += halfS;
			break;
		case 5:
		case 4:
			exp[1] += halfS;
			break;
		}

		AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(this.posX, this.posY,
				this.posZ, this.posX + def, this.posY + def, this.posZ + def)
				.expand(exp[0], halfS, exp[1]);
		return aabb;
	}

	public void updateParameters() {
		if ((this.parent == null) || (!this.parent.hasRotor())) {
			return;
		}
		this.wheelType = this.parent.getRotor().type;
		this.boundingBox.setBB(getBoundingBox());
	}

	public void destroy() {
		setDead();
		this.worldObj.removeEntity(this);
		/* remove rotor */
		/*PacketDispatcher.sendPacketToAllAround(this.posX, this.posY, this.posZ,
				128.0D, this.worldObj.provider.dimensionId,
				BCTPacketHandler.PacketType.fillPacket(new PacketEntityUpdate(
						this.field_70157_k,
						PacketEntityUpdate.UpdateType.DESTROY)));*/
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
	}
}