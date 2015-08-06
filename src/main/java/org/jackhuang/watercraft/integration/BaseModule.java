package org.jackhuang.watercraft.integration;

public class BaseModule {
    
    protected void testClassExistence(Class c) {
        c.isInstance(this);
    }
    
    public void init() {} 

}
