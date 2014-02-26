package com.radixshock.radixcore.lang;

import java.util.Map;

public interface ILanguageLoaderHook 
{
	public boolean processEntrySet(Map.Entry<Object, Object> entrySet);
	
	public boolean shouldReceiveGetStringCalls();
	
	public String onGetString(String elementId, Object... arguments);
}
