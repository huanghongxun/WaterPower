/**
 * Copyright (c) Huang Yuhui, 2017
 * <p>
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package waterpower;

import java.lang.invoke.MethodHandle;

public class JavaAdapter {
    public static void invokeMethodHandle(MethodHandle handle) throws Throwable {
        handle.invoke();
    }

    public static void invokeMethodHandle(MethodHandle handle, Object arg0) throws Throwable {
        handle.invoke(arg0);
    }
}
