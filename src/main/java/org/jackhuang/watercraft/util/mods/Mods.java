package org.jackhuang.watercraft.util.mods;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModAPIManager;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;

public class Mods {

	public static class IDs {
		public static final String BuildCraftPower = "BuildCraftAPI|power";
		public static final String Factorization = "factorization";
		public static final String IndustrialCraft2 = "IC2";
		public static final String Thaumcraft = "Thaumcraft";
		public static final String ThermalExpansion = "ThermalExpansion";
		public static final String RotaryCraft = "RotaryCraft";
	}

	public static final SimpleMod Factorization = new SimpleMod(IDs.Factorization);
	public static final SimpleMod ThermalExpansion = new SimpleMod(IDs.ThermalExpansion);
	public static final SimpleMod IndustrialCraft2 = new SimpleMod(IDs.IndustrialCraft2);
	public static final SimpleMod BuildCraftPower = new SimpleMod(IDs.BuildCraftPower);
	public static final SimpleMod Thaumcraft = new SimpleMod(IDs.Thaumcraft, false);

	public static class SimpleMod {
		protected boolean isModLoaded;
		public String id;
		public boolean isAvailable;
		public boolean providesPower;

		public SimpleMod(String id) {
			this(id, true);
		}

		public SimpleMod(String id, boolean providesPower) {
			this.id = id;
			this.providesPower = providesPower;
			
			ArtifactVersion version = VersionParser.parseVersionReference(id);
			if (Loader.isModLoaded(version.getLabel()))
				isModLoaded = version.containsVersion(Loader.instance()
						.getIndexedModList().get(version.getLabel())
						.getProcessedVersion());
			else
				isModLoaded = ModAPIManager.INSTANCE.hasAPI(version.getLabel());
			
			isAvailable = isModLoaded && providesPower;
		}
	}

}
