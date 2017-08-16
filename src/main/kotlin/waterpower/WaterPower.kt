/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.FMLLog
import net.minecraftforge.fml.common.LoaderState
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.*
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.apache.logging.log4j.Logger
import waterpower.annotations.AnnotationSystem
import waterpower.client.CreativeTabWaterPower
import waterpower.common.CommonProxy
import waterpower.common.init.InitParser
import waterpower.common.init.NewInstanceParser
import waterpower.config.ConfigHandler
import waterpower.integration.IDs
import java.util.*

@Mod(modid = WaterPower.MOD_ID, name = WaterPower.MOD_NAME, version = "@MODVERSION@", modLanguage = "kotlin",
        modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter",
        dependencies = "required-after: forgelin; after:${IDs.IndustrialCraft2}; after:${IDs.GregTech}; after:${IDs.Thaumcraft}; after:${IDs.Waila}; after:jei; after:${IDs.BuildCraftCore}; after:${IDs.Forestry}; after:${IDs.RedstoneFlux}; after:${IDs.Mekanism}; after: ${IDs.TinkersConstruct}; ")
object WaterPower {

    lateinit var logger: Logger

    @SidedProxy(
            clientSide = "waterpower.client.ClientProxy",
            serverSide = "waterpower.common.ServerProxy"
    )
    lateinit var proxy: CommonProxy

    val creativeTab = CreativeTabWaterPower("creativeTabWaterPower")

    val random = Random()

    @Mod.EventHandler
    fun onConstruction(e: FMLConstructionEvent) {
        MinecraftForge.EVENT_BUS.register(this)
        AnnotationSystem.initialize()
        InitParser.init(LoaderState.ModState.CONSTRUCTED)
    }

    @Mod.EventHandler
    fun onPreInit(e: FMLPreInitializationEvent) {
        ConfigHandler.config = Configuration(e.suggestedConfigurationFile)
        logger = e.modLog
        InitParser.init(LoaderState.ModState.PREINITIALIZED)
        NewInstanceParser.init(LoaderState.ModState.PREINITIALIZED)
        proxy.onPreInit()
    }

    @Mod.EventHandler
    fun onInit(e: FMLInitializationEvent) {
        InitParser.init(LoaderState.ModState.INITIALIZED)
        NewInstanceParser.init(LoaderState.ModState.INITIALIZED)
        proxy.onInit()
    }

    @Mod.EventHandler
    fun onPostInit(e: FMLPostInitializationEvent) {
        InitParser.init(LoaderState.ModState.POSTINITIALIZED);
        NewInstanceParser.init(LoaderState.ModState.POSTINITIALIZED);
        proxy.onPostInit()
    }

    @Mod.EventHandler
    fun onLoadComplete(e: FMLLoadCompleteEvent) {
        InitParser.init(LoaderState.ModState.AVAILABLE)
        NewInstanceParser.init(LoaderState.ModState.AVAILABLE)
    }

    @SubscribeEvent
    fun onConfigChanged(e: ConfigChangedEvent.OnConfigChangedEvent) {
        if (MOD_ID == e.modID)
            ConfigHandler.preInit()
    }

    const val MOD_ID = "waterpower";
    const val MOD_NAME = "WaterPower";
}