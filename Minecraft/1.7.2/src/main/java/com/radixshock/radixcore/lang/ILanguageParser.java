package com.radixshock.radixcore.lang;

/**
 * Defines a class as a language parser.
 */
public interface ILanguageParser 
{
	/**
	 * Parses variables contained in text.
	 * 
	 * @param 	text		The translated text that still contains parsable variables.
	 * @param 	arguments	List of objects you will use to parse the variables correctly.
	 * 
	 * @return	The parsed string.
	 */
	public String parseString(String text, Object... arguments);
}
