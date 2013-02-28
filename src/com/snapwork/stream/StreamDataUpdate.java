package com.snapwork.stream;

import java.util.Hashtable;

import com.lightstreamer.javameclient.midp.SimpleItemUpdate;

public interface StreamDataUpdate {
	public void streamDataUpdate(int item, SimpleItemUpdate update);
	public void streamDataUpdate(Hashtable hashtable);
}
