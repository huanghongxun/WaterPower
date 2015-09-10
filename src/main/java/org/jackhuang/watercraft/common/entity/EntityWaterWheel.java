package org.jackhuang.watercraft.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import org.jackhuang.watercraft.common.block.watermills.TileEntityWatermill;
import org.jackhuang.watercraft.common.item.rotors.RotorType;
import org.jackhuang.watercraft.util.Utils;

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
        this.parentFacing = parent.getDirection();
        this.noClip = false;

        this.xParent = parent.xCoord;
        this.yParent = parent.yCoord;
        this.zParent = parent.zCoord;

        int[] actualCoords = Utils.moveForward(world, parent.xCoord, parent.yCoord, parent.zCoord, parent.getDirection(), 1);
        setPosition(actualCoords[0], actualCoords[1], actualCoords[2]);

        // this.boundingBox.setBB(getBoundingBox());
    }

    /*
     * @Override public AxisAlignedBB getBoundingBox() { double halfS =
     * (this.parent.getType().length - 1) / 2; double def = 1.0D; double[] exp =
     * { 0.0D, 0.0D }; double[] offset = new double[]{ 0, 0, 0 };
     * 
     * if (this.parent == null) { return null; }
     * 
     * //System.out.println(this.parent.getDirection()); switch
     * (this.parent.getDirection()) { case 5: offset[0] += 1; offset[2] -= 4;
     * exp[1] += halfS; break; case 2: exp[0] += halfS; offset[2] -= 1;
     * offset[0] += 4; break; case 3: offset[2] += 1; offset[0] += 10; exp[0] +=
     * halfS; break; case 4: offset[0] -= 1; offset[2] += 4; exp[1] += halfS;
     * break; }
     * 
     * double x = parent.xCoord + offset[0], y = parent.yCoord + offset[1], z =
     * parent.zCoord + offset[2];
     * 
     * AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(x, y, z, x + def, y +
     * def, z + def) .expand(exp[0], halfS, exp[1]); return
     * aabb;//AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX, posY,
     * posZ).expand(0, 0, 0); }
     */

    protected void entityInit() {
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        this.parent = ((TileEntityWatermill) this.worldObj.getTileEntity(this.xParent, this.yParent, this.zParent));

        if (this.parent == null)
            destroy();
        else {
            parentFacing = parent.getDirection();
            int[] actualCoords = Utils.moveForward(worldObj, parent.xCoord, parent.yCoord, parent.zCoord, parent.getDirection(), 1);
            setPosition(actualCoords[0], actualCoords[1], actualCoords[2]);
        }
        updateParameters();
    }

    /*
     * public float getWheelAngle() { float state = this.wheelAngle;
     * 
     * int i = MathHelper.floor_double(this.boundingBox.minX); int j =
     * MathHelper.floor_double(this.boundingBox.maxX); int k =
     * MathHelper.floor_double(this.boundingBox.minY); int l =
     * MathHelper.floor_double(this.boundingBox.maxY); int i1 =
     * MathHelper.floor_double(this.boundingBox.minZ); int j1 =
     * MathHelper.floor_double(this.boundingBox.maxZ);
     * 
     * Vec3 vec3 = this.worldObj.getWorldVec3Pool().getVecFromPool(0.0D, 0.0D,
     * 0.0D);
     * 
     * boolean sufficientVector = false;
     * 
     * if (vec3.func_72433_c() > 0.0D) { vec3 = vec3.func_72432_b();
     * 
     * float multi = getWheelSpeed();
     * 
     * switch
     * (1.$SwitchMap$net$minecraftforge$common$ForgeDirection[this.parent.
     * getDirection().ordinal()]) { case 1: case 2: if (vec3.xCoord != 0.0D) {
     * state = (float)(state + vec3.xCoord * multi); sufficientVector = true; }
     * else { sufficientVector = false; }break; case 3: case 4: if (vec3.zCoord
     * != 0.0D) { state = (float)(state - vec3.zCoord * multi); sufficientVector
     * = true; } else { sufficientVector = false; }break; default:
     * sufficientVector = false; } } else { sufficientVector = false;
     * }this.canEngineProduce = sufficientVector; return state; }
     */

    public void updateParameters() {
        if ((this.parent == null) || (!this.parent.hasRotor())) {
            return;
        }
        this.wheelType = this.parent.getRotor().type;
        // this.boundingBox.setBB(getBoundingBox());
    }

    public void destroy() {
        setDead();
        this.worldObj.removeEntity(this);
        /* remove rotor */
        /*
         * PacketDispatcher.sendPacketToAllAround(this.posX, this.posY,
         * this.posZ, 128.0D, this.worldObj.provider.dimensionId,
         * BCTPacketHandler.PacketType.fillPacket(new PacketEntityUpdate(
         * this.field_70157_k, PacketEntityUpdate.UpdateType.DESTROY)));
         */
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
    }
}