package org.jackhuang.compactwatermills.common.item.crafting;

import java.util.Arrays;

import org.jackhuang.compactwatermills.client.renderer.IIconContainer;
import org.jackhuang.compactwatermills.client.renderer.Textures;

import net.minecraft.util.StatCollector;

public enum MaterialTypes {
	Zinc(Textures.METAL, 182, 201, 206, 0, false),
	ZincAlloy(Textures.METAL, 226,226,226,0,false),
	Neodymium(Textures.METAL, 210,221,221,0,false),
	NeodymiumMagnet(Textures.METAL, 162,170,171,0,true),
	IndustrialSteel(Textures.METAL, 221,243,249,0,true),
	Magnet(Textures.METAL, 80,83,91,0,false),
	Vanadium(Textures.METAL, 189,197,202,0,true),
	VanadiumSteel(Textures.METAL, 166,176,183,0,true),
	Manganese(Textures.METAL, 137,156,167,0,false),
	ManganeseSteel(Textures.METAL, 174,181,194,0,true),
	Steel(Textures.METAL, 75,83,94,0,false);
	
	public static int space = 100;
	
	public short R, G, B, A;
	public boolean blastFurnaceRequired;
	
	public IIconContainer[] iconContainer;
	
	private MaterialTypes(IIconContainer[] iconContainer, int R, int G, int B, int A, boolean blastFurnaceRequired) {
		this.R = (short)R;
		this.G = (short)G;
		this.B = (short)B;
		this.A = (short)A;
		this.blastFurnaceRequired = blastFurnaceRequired;
		this.iconContainer = (IIconContainer[]) Arrays.copyOf(iconContainer, 64);
	}
	
	public int ind() {
		return ordinal() * space;
	}
	
	public String getShowedName() {
		String format = "cptwtrml.material." + name();
		String s = StatCollector.translateToLocal(format);
		return s;
	}
}
