/*******************************************************************************
 * ILanguageLoaderHook.java
 * Copyright (c) 2014 WildBamaBoy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MCA Minecraft Mod license.
 ******************************************************************************/

package com.radixshock.radixcore.lang;

import java.util.Map;

/**
 * Defines a class as a language loader hook. It will received various callbacks from the language loader.
 */
public interface ILanguageLoaderHook
{
	/**
	 * Allows you to handle what data is loaded by the language loader. For example, MCA uses this to populate two different lists with male and female names so that one can be randomly generated properly.
	 * 
	 * @param entrySet A key-value pair. Key = phrase ID, Value = translated phrase.
	 * @return Return true only if you have processed the entry set yourself. Return false otherwise.
	 */
	public boolean processEntrySet(Map.Entry<Object, Object> entrySet);

	/**
	 * @return True if you want to handle calls to getString() yourself.
	 */
	public boolean shouldReceiveGetStringCalls();

	/**
	 * Called when the language loader gets a request for a string and you return <b>true</b> on <code>shouldReceiveGetStringCalls();</code>
	 * <p>
	 * For example, MCA uses this to properly implement its random dialogue system. Smaller mods with only items and GUI buttons to translate will most likely have no benefit to processing a getString request themselves.
	 * 
	 * @param elementId The ID of the requested phrase.
	 * @param arguments Arguments that were provided to the language loader.
	 * @return Return the phrase after it is <b>parsed by your language parser</b>.
	 */
	public String onGetString(String elementId, Object... arguments);
}
