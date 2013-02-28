package com.snapwork.components;

import java.util.Vector;

/**
 * 
 * <p>This class is used for callback of http calls.
 * <p>Http call started after loding the screen. 
 *
 */

public interface ThreadedComponents
{
	public Vector getComponentData();
	public void componentsPrepared(byte componentID,Object component);
	public void componentsDataPrepared(byte componentID,Object data);
}
