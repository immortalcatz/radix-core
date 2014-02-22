package com.radixshock.lang;

import java.util.Map;

public interface ILanguageLoaderHook 
{
	public boolean processEntrySet(Map.Entry<Object, Object> entrySet);
	
	public boolean shouldReceiveGetStringCalls();
	
	public String onGetString(String elementId, Object... arguments);
}
