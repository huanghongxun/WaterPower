/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package org.jackhuang.watercraft.integration.minetweaker;

import minetweaker.IUndoableAction;

public abstract class OneWayAction implements IUndoableAction {

    @Override
    public boolean canUndo() {
        return false;
    }

    @Override
    public String describeUndo() {
        return "Impossible";
    }

    @Override
    public Object getOverrideKey() {
        return null;
    }

    @Override
    public void undo() {
    }

}
