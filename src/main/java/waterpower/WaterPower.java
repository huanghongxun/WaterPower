/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.classloading.FMLForgePlugin;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import waterpower.client.gui.CreativeTabWaterCraft;
import waterpower.common.CommonProxy;
import waterpower.common.block.GlobalBlocks;
import waterpower.common.block.machines.BlockMachines;
import waterpower.common.block.ore.BlockMaterial;
import waterpower.common.block.ore.BlockOre;
import waterpower.common.block.ore.ItemOreDust;
import waterpower.common.block.reservoir.BlockReservoir;
import waterpower.common.block.turbines.BlockTurbine;
import waterpower.common.block.watermills.BlockWatermill;
import waterpower.common.block.watermills.WaterType;
import waterpower.common.item.GlobalItems;
import waterpower.common.item.crafting.ItemCrafting;
import waterpower.common.item.crafting.ItemMaterial;
import waterpower.common.item.other.ItemOthers;
import waterpower.common.item.range.ItemPlugins;
import waterpower.common.item.range.ItemRange;
import waterpower.common.item.rotor.RotorType;
import waterpower.common.network.MessagePacketHandler;
import waterpower.common.recipe.EasyRecipeRegistrar;
import waterpower.common.recipe.IRecipeRegistrar;
import waterpower.common.recipe.NormalRecipeRegistrar;
import waterpower.integration.IntegrationType;

/**
 * WaterPower Main Class.
 * 
 * @author jackhuang
 */
@Mod(modid = Reference.ModID, name = Reference.ModName, version = Reference.Version, acceptedMinecraftVersions = "[1.10,1.11)", dependencies = "required-after:Forge; after:IC2; after:gregtech; after:Thaumcraft; after:BuildCraftAPI|power[1.1,); after:Forestry; after:craftguide; after:Waila; after:factorization; after:CoFHCore; after:Mekanism; after:ThermalFoundation; after:MineFactoryReloaded; after: tconstruct; ")
public class WaterPower implements IWorldGenerator {

    /**
     * Instance of the Mod Water Power.
     */
    @Mod.Instance(Reference.ModID)
    public static WaterPower instance;

    /**
     * Instance of the Sided Proxy, ClientProxy on Client Side, CommonProxy on
     * Server Side.
     */
    @SidedProxy(clientSide = "waterpower.client.ClientProxy", serverSide = "waterpower.common.CommonProxy")
    public static CommonProxy proxy;

    /**
     * Instance of the creative tab of Water Power.
     */
    public static final CreativeTabs creativeTabWaterPower = new CreativeTabWaterCraft("creativeTabWaterPower");

    /**
     * Loaded configuration
     */
    private Configuration config;

    /**
     * Recipe Handler
     */
    private IRecipeRegistrar recipe;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        Reference.initConfig(config);

        MessagePacketHandler.init();

        config.save();

        MinecraftForge.EVENT_BUS.register(this);

        init();
        proxy.preInit();

        GameRegistry.registerWorldGenerator(this, 0);

        for (IntegrationType type : IntegrationType.values()) {
            if (type.getModule() != null)
                type.getModule().init();
        }
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {

        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

        config.save();
        
        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        
        Property enableEasyRecipe = config.get("recipe", "enableEasyRecipe", false);
        if (enableEasyRecipe.getBoolean(false))
            recipe = new EasyRecipeRegistrar(config);
        Property enableNormalRecipe = config.get("recipe", "enableNormalRecipe", true);
        if (enableNormalRecipe.getBoolean(true))
            recipe = new NormalRecipeRegistrar(config);

        recipe.registerAllRecipes();

        for (IntegrationType type : IntegrationType.values()) {
            if (type.getModule() != null)
                type.getModule().postInit();
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (Reference.ModID.equals(event.getModID())) {
            Reference.initConfig(config);
        }
    }

    protected void init() {

        GlobalItems.updater = new ItemOthers();

        GlobalBlocks.reservoir = new BlockReservoir();
        GlobalBlocks.machine = new BlockMachines();
        GlobalBlocks.material = new BlockMaterial();

        GlobalItems.crafting = new ItemCrafting();
        GlobalItems.meterial = new ItemMaterial();
        GlobalItems.oreDust = new ItemOreDust();
        GlobalItems.range = new ItemRange();
        GlobalItems.plugins = new ItemPlugins();

        RotorType.initRotors();
        RotorType.registerRotor();
        WaterType.initTrousers();

        GlobalBlocks.waterMill = new BlockWatermill();
        GlobalBlocks.turbine = new BlockTurbine();
        new BlockOre();
    }

    public static boolean isServerSide() {
        return FMLCommonHandler.instance().getEffectiveSide().isServer();
    }

    public static boolean isClientSide() {
        return FMLCommonHandler.instance().getEffectiveSide().isClient();
    }

    private static WorldGenMinable getMinable(ItemStack is, int number) {
    	IBlockState state = Block.getBlockFromItem(is.getItem()).getStateFromMeta(is.getItemDamage());
        return new WorldGenMinable(state, number);
    }

    private static void generateOre(ItemStack ore, int number, int baseCount, World world, Random random, int chunkX, int chunkZ, int low, int high) {
        if (ore != null) {
            int count = (int) Math.round(random.nextGaussian() * Math.sqrt(baseCount) + baseCount);

            for (int n = 0; n < count; n++) {
                int x = chunkX * 16 + random.nextInt(16);
                int y = random.nextInt(high - low) + low;
                int z = chunkZ * 16 + random.nextInt(16);
                getMinable(ore, number).generate(world, random, new BlockPos(x, y, z));
            }
        }
    }

    @Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
        int baseCount = Math.round(Reference.WorldGen.oreDensityFactor);

        if (Reference.WorldGen.vanadiumOre)
            generateOre(GlobalBlocks.vanadiumOre, 8, baseCount, world, random, chunkX, chunkZ, 10, 13);
        if (Reference.WorldGen.manganeseOre)
            generateOre(GlobalBlocks.manganeseOre, 8, baseCount * 2, world, random, chunkX, chunkZ, 6, 20);
        if (Reference.WorldGen.monaziteOre)
            generateOre(GlobalBlocks.monaziteOre, 8, baseCount, world, random, chunkX, chunkZ, 6, 32);
        if (Reference.WorldGen.magnetOre)
            generateOre(GlobalBlocks.magnetOre, 8, baseCount * 2, world, random, chunkX, chunkZ, 6, 64);
        if (Reference.WorldGen.zincOre)
            generateOre(GlobalBlocks.zincOre, 8, baseCount * 2, world, random, chunkX, chunkZ, 6, 64);
    }
    
    @SideOnly(Side.CLIENT)
    public static World getWorld() {
        Minecraft mc = FMLClientHandler.instance().getClient();
        if (mc != null)
            return mc.theWorld;
        return null;
    }

    public static boolean isDeobf() {
        return FMLForgePlugin.RUNTIME_DEOBF;
    }
}
