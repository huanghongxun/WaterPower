package org.jackhuang.compactwatermills.item;

import org.jackhuang.compactwatermills.CompactWatermills;
import org.jackhuang.compactwatermills.DefaultIds;
import org.jackhuang.compactwatermills.InternalName;
import org.jackhuang.compactwatermills.Reference;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Configuration;

public abstract class ItemBase extends Item {

	protected final InternalName internalName;
	public static int id;
	  protected Icon[] textures;
	
	public ItemBase(Configuration config, InternalName internalName) {
		super(id = CompactWatermills.getItemIdFor(config, internalName,
				DefaultIds.get(internalName)) + Reference.ItemIDDifference);

		setCreativeTab(CompactWatermills.creativeTabCompactWatermills);
		setNoRepair();
		
		this.internalName = internalName;
	}
	
	  public String getTextureName(int index)
	  {
	    if (this.hasSubtypes)
	      return getUnlocalizedName(new ItemStack(this.itemID, 1, index));
	    if (index == 0) {
	      return getUnlocalizedName();
	    }
	    return null;
	  }


	  public String getTextureFolder() {
	    return null;
	  }
	  
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		int indexCount = 0;

	    while (getTextureName(indexCount) != null) indexCount++;

	    this.textures = new Icon[indexCount];
	    String textureFolder = getTextureFolder() + "/";

	    for (int index = 0; index < indexCount; index++)
	      this.textures[index] = iconRegister.registerIcon("ic2:" + textureFolder + getTextureName(index));
	}
	
	  @SideOnly(Side.CLIENT)
	  public Icon getIconFromDamage(int meta)
	  {
	    if (meta < this.textures.length) {
	      return this.textures[meta];
	    }
	    return this.textures.length < 1 ? null : this.textures[0];
	  }
	
	@SideOnly(Side.CLIENT)
	public ResourceLocation getRenderTexture() {
		return new ResourceLocation(Reference.ModID + ":textures/items/"
			+ this.getUnlocalizedName() + ".png");
	}
}
