/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.annotations

import com.google.common.reflect.ClassPath
import net.minecraftforge.fml.common.FMLLog
import java.lang.invoke.MethodHandles
import java.lang.reflect.Executable
import java.lang.reflect.Method
import java.net.URL
import java.net.URLClassLoader
import java.net.URLDecoder
import java.util.*

/**
 * @param obj null for constructors or static/object methods/fields.
 */
fun <T> call(clazz: Class<T>, name: String, obj: T? = null, vararg args: Any?): T? {
    return callImpl(clazz, name, obj, *args) as T?
}

/**
 * @param obj null for constructors or static/object methods/fields.
 */
fun callImpl(clazz: Class<*>, name: String, obj: Any? = null, vararg args: Any?): Any? {
    try {
        if (args.isEmpty())
            try {
                return clazz.getDeclaredField(name).get(obj)
            } catch(ignored: NoSuchFieldException) {
            }
        if (name == "new")
            clazz.declaredConstructors.forEach {
                if (checkParameter(it, *args)) return it.newInstance(*args)
            }
        else
            return forMethod(clazz, name, *args)!!.invoke(obj, *args)
        throw RuntimeException()
    } catch(e: Exception) {
        throw IllegalArgumentException("Cannot find `${name}` in Class `${clazz.name}`, please check your code.", e)
    }
}

fun forMethod(clazz: Class<*>, name: String, vararg args: Any?): Method? =
        clazz.declaredMethods.filter { it.name == name }.filter { checkParameter(it, *args) }.firstOrNull()

fun checkParameter(exec: Executable, vararg args: Any?): Boolean {
    val cArgs = exec.parameterTypes
    if (args.size == cArgs.size) {
        for (i in 0 until args.size) {
            val arg = args[i]
            // primitive variable cannot be null
            if (if (arg != null) !isInstance(cArgs[i], arg) else cArgs[i].isPrimitive)
                return false
        }
        exec.isAccessible = true
        return true
    } else
        return false
}

fun isInstance(superClass: Class<*>, obj: Any): Boolean {
    if (superClass.isInstance(obj)) return true
    else if (PRIMITIVES[superClass.name] == obj.javaClass) return true
    return false
}

fun isInstance(superClass: Class<*>, clazz: Class<*>): Boolean {
    for (i in clazz.interfaces)
        if (isInstance(superClass, i) || PRIMITIVES[superClass.name] == clazz)
            return true
    return isSubClass(superClass, clazz)
}

fun isSubClass(superClass: Class<*>, clazz: Class<*>): Boolean {
    var clz = clazz
    do {
        if (superClass == clz) return true
        clz = clz.superclass
    } while (clz != null)
    return false
}

fun <T> instance(clazz: Class<T>): T {
    val c = clazz.getDeclaredConstructor()
    c.isAccessible = true
    return c.newInstance()
}

fun <T> objectInstance(clz: Class<T>)
        = call(clz, "INSTANCE")

fun <T> enumElement(clz: Class<T>, ord: Int): T
        = (call(clz, "values") as Array<T>)[ord]

val PRIMITIVES = mapOf(
        "byte" to java.lang.Byte::class.java,
        "short" to java.lang.Short::class.java,
        "int" to java.lang.Integer::class.java,
        "long" to java.lang.Long::class.java,
        "char" to java.lang.Character::class.java,
        "float" to java.lang.Float::class.java,
        "double" to java.lang.Double::class.java,
        "boolean" to java.lang.Boolean::class.java
)

object ClassEngine {

    fun findClassesInURL(url: URL): LinkedList<String> {
        val pattern = Regex.fromLiteral(".*\\$[0-9]+")
        val res = LinkedList<String>()
        val loader = URLClassLoader(arrayOf(url), null)
        val path = ClassPath.from(loader)
        for (info in path.allClasses)
            if (!pattern.matches(info.name) && !info.name.contains("$$"))
                res.add(info.name)
        return res
    }

    val lookup: MethodHandles.Lookup by lazy {
        call(MethodHandles.Lookup::class.java, "new", null, Object::class.java, -1)!!
    }


}