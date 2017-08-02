/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.annotations

import net.minecraft.inventory.Container
import net.minecraftforge.fml.common.LoaderState
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.relauncher.Side
import waterpower.client.GuiBase
import java.lang.annotation.Inherited
import kotlin.reflect.KClass

/**
 * @see waterpower.client.GuiHandler
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
annotation class HasGui(val guiClass: KClass<out GuiBase>, val containerClass: KClass<out Container>)

/**
 * the annotated class should declare func init() or just use default init code.
 * init() must be annotated @JvmStatic
 *
 * PreInit: block, item registry
 * Init: preparation for recipes registry, ore dictionary
 * PostInit: recipes registry
 * @param modState when will the initialize procedure run
 * @param side client = 0, server = 1 or both = 2
 * @param priority -1: most first, 0: normal, 1: most last
 * @see waterpower.common.init.InitParser
 * @see kotlin.jvm.JvmStatic
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Init(val priority: EventPriority = EventPriority.NORMAL)

/**
 * Must extend IModule
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Integration(val modID: String)

/**
 * the annotated class(extends IMessage and IMessageHandler<Class, IMessage> will be
 * initialized automatically
 * @param handlerSide the side of handler, not sender!
 * @see waterpower.common.network.NetworkHandler
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Message(val handlerSide: Side)

/**
 * @param modState when will the initialize procedure run
 * @param side client = 0, server = 1 or both = 2
 * @param priority If less than 0, the class will be invoked at first.
 * @see waterpower.common.init.NewInstanceParser
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class NewInstance(val modState: LoaderState.ModState, val priority: Int = 0, val side: Int = 2)

/**
 * If @Parser has been added to an "object", the object should have function "loadClass(Class<*>)" to
 * loadClass must be annotated @JvmStatic
 * @see waterpower.annotations.AnnotationSystem
 * @see kotlin.jvm.JvmStatic
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Parser()

/**
 * For TileEntities registry
 * @see waterpower.common.init.RegisterParser
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Register(val id: String)

/**
 * The <b>nonnull</b> annotated field should be declared in TileEntity classes
 * the type of field should be primitive data types or implement INBTSerializable
 *
 * @param name if == "", will use the field name instead
 * @see waterpower.util.INBTSerializable<T>
 * @see waterpower.common.block.tile.TileEntityBase
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Sync(val name: String = "")

/**
 * The <b>nonnull</b> annotated <b>boolean</b> field should be declared in TileEntity classes.
 *
 * @param name if == "", will use the field name instead
 * @param sendOnce if annotated field is boolean, when be set to true, we send a notice to client and set it to false meanwhile.
 * @see waterpower.common.block.tile.TileEntityBase
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Flag(val name: String = "")

/**
 * The <b>nonnull</b> annotated field should be declared in TileEntity classes
 * the type of field should be primitive data types or implement INBTSerializable
 *
 * @param name if == "", will use the field name instead
 * @see waterpower.util.INBTSerializable<T>
 * @see waterpower.common.block.tile.TileEntityBase
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class SaveNBT(val name: String = "")