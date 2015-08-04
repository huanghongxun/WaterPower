package org.jackhuang.watercraft.integration;

public abstract class BaseModule {
    
    protected void testClassExistence(Class c) {
        c.isInstance(this);
    }
    
    public void init() {} 
    public void loadComplete() {} 

}
