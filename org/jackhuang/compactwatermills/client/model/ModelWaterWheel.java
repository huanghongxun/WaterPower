package org.jackhuang.compactwatermills.client.model;

import org.jackhuang.compactwatermills.block.watermills.WaterType;
import org.jackhuang.compactwatermills.item.rotors.RotorType;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

@SideOnly(Side.CLIENT)
public class ModelWaterWheel extends ModelBase
{
  public ModelRenderer trunk;
  public ModelRenderer plank[];
  public static final int length = 10;

  public ModelWaterWheel()
  {
    this.trunk = new ModelRenderer(this, 0, 0);
    this.trunk.setRotationPoint(0.0F, 0.0F, 0.0F);

    this.trunk.addBox(0.0F, 0.0F, 0.0F, 1, 12, 1);
    this.trunk.addBox(-1.0F, 0.0F, 0.0F, 1, 12, 1);
    this.trunk.addBox(0.0F, 0.0F, -1.0F, 1, 12, 1);
    this.trunk.addBox(-1.0F, 0.0F, -1.0F, 1, 12, 1);

    this.plank = new ModelRenderer[WaterType.values().length];
    for(int i = 0; i < plank.length; i++) {
	    this.plank[i] = new ModelRenderer(this, 0, 0);
	    this.plank[i].addBox(-1.0F, 0.0F, 1.0F, 2, 10, WaterType.values()[i].length * 6);
	    this.plank[i].setRotationPoint(0.0F, 0.0F, 0.0F);
    }
  }

  public void render(float f)
  {
    this.trunk.render(f);
    this.plank[0].render(f);
  }

  public void renderPlank(float f, WaterType type)
  {
	  this.plank[type.ordinal()].render(f);
  }

  public void renderTrunk(float f)
  {
    this.trunk.render(f);
  }
}