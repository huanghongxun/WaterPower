/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.config

import net.minecraftforge.common.config.Configuration
import waterpower.annotations.Init
import waterpower.annotations.Parser
import java.lang.reflect.Field

/**
 * Parse @ConfigCategory and @ConfigValue, serving configuration initialization
 * @see waterpower.config.ConfigCategory
 * @see waterpower.config.ConfigValue
 */
@Init
@Parser
object ConfigHandler {

    val loadedWrappers = ArrayList<FieldWrapper>()
    lateinit var config: Configuration

    /**
     * Parse configurations and load fields in a class.
     *
     * Will be called by
     * @param clazz An "object" (optional: annotation @ConfigCategory)
     * @param obj an instance of the object
     * @see waterpower.annotations.Parser
     */
    @JvmStatic
    fun loadClass(clazz: Class<*>) {
        val cc = clazz.getAnnotation(ConfigCategory::class.java) ?: return
        val fields = clazz.declaredFields

        for (f in fields) {
            if (f.isAnnotationPresent(ConfigValue::class.java)) {
                f.isAccessible = true
                val annotation = f.getAnnotation(ConfigValue::class.java)
                loadedWrappers.add(wrappers[f.type]!!
                        .getConstructor(Any::class.java, Field::class.java, ConfigValue::class.java, ConfigCategory::class.java)
                        .newInstance(null, f, annotation, cc))
            }
        }
    }

    fun read() {
        for (w in loadedWrappers) {
            try {
                w.read(this)
            } catch(ignore: IllegalAccessException) {
            }
        }
    }

    fun load() {
        config.load()
    }

    fun save() {
        config.save()
    }

    @JvmStatic
    fun preInit() {
        load()
        read()
        save()
    }

    abstract class FieldWrapper(val obj: Any?, val field: Field, val value: ConfigValue, val category: ConfigCategory?) {
        fun category() = category?.category ?: value.category
        fun key() = if (value.key == "") field.name else value.key
        fun read(handler: ConfigHandler) {
            field.set(obj, parse(handler))
        }

        abstract fun parse(handler: ConfigHandler): Any?
    }

    class IntFieldWrapper(obj: Any?, field: Field, value: ConfigValue, category: ConfigCategory?)
        : FieldWrapper(obj, field, value, category) {
        override fun parse(handler: ConfigHandler): Any?
                = handler.config.get(category(), key(), field.getInt(obj), value.comment).int
    }

    class DoubleFieldWrapper(obj: Any?, field: Field, value: ConfigValue, category: ConfigCategory?)
        : FieldWrapper(obj, field, value, category) {
        override fun parse(handler: ConfigHandler): Any?
                = handler.config.get(category(), key(), field.getDouble(obj), value.comment).double
    }

    class BooleanFieldWrapper(obj: Any?, field: Field, value: ConfigValue, category: ConfigCategory?)
        : FieldWrapper(obj, field, value, category) {
        override fun parse(handler: ConfigHandler): Any?
                = handler.config.get(category(), key(), field.getBoolean(obj), value.comment).boolean
    }

    class StringFieldWrapper(obj: Any?, field: Field, value: ConfigValue, category: ConfigCategory?)
        : FieldWrapper(obj, field, value, category) {
        override fun parse(handler: ConfigHandler): Any?
                = handler.config.get(category(), key(), field.get(obj) as String, value.comment).string
    }

    class OreConfigFieldWrapper(obj: Any?, field: Field, value: ConfigValue, category: ConfigCategory?)
        : FieldWrapper(obj, field, value, category) {
        override fun parse(handler: ConfigHandler): Any? {
            val categoryStr = "${category()}.${key()}"
            val generate = handler.config.getBoolean(categoryStr, "generates", (field.get(obj) as OreConfig).generate,
                    "Whether ${key()} should be generated or not.")
            val amount = handler.config.get(categoryStr, "amountPerChunk", (field.get(obj) as OreConfig).amountPerChunk,
                    "Amount of ${key()} per chunk").int
            val size = handler.config.get(categoryStr, "maxSize", (field.get(obj) as OreConfig).maxSize,
                    "Amount of ${key()} per vein").int
            val min = handler.config.get(categoryStr, "minLevel", (field.get(obj) as OreConfig).minLevel,
                    "Min level to generate ${key()}").int
            val max = handler.config.get(categoryStr, "maxLevel", (field.get(obj) as OreConfig).maxLevel,
                    "Max level to generate ${key()}").int
            return OreConfig(amount, size, min, max, generate)
        }

    }

    val wrappers = mutableMapOf(
            Int::class.java to IntFieldWrapper::class.java,
            Double::class.java to DoubleFieldWrapper::class.java,
            Boolean::class.java to BooleanFieldWrapper::class.java,
            String::class.java to StringFieldWrapper::class.java,
            OreConfig::class.java to OreConfigFieldWrapper::class.java
    )
}