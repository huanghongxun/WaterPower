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
