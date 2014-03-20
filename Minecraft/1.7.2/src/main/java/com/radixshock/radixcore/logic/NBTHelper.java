/*******************************************************************************
 * NBTHelper.java
 * Copyright (c) 2014 Radix-Shock Entertainment.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package com.radixshock.radixcore.logic;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Enables autosaving and loading of NBT data using reflection.
 */
public class NBTHelper 
{
	/**
	 * Automatically saves the provided entity's public fields to NBT. <p>
	 * <b>For public fields you <u>do not</u> want saved to NBT, apply the <code>transient</code> modifier.</b>
	 * 
	 * @param 	entity	The entity containing data to be written to NBT.
	 * @param 	nbt		The entity's NBTTagCompound provided by Minecraft.
	 */
	public static void autoWriteEntityToNBT(Entity entity, NBTTagCompound nbt)
	{
		autoWriteEntityToNBT(entity, nbt, entity.getClass());
	}
	
	/**
	 * Automatically saves the provided entity's public fields to NBT. <p>
	 * 
	 * <b>For public fields you <u>do not</u> want saved to NBT, apply the <code>transient</code> modifier.</b> <p>
	 * 
	 * Use this method if you have a base/abstract class for an entity and want to avoid
	 * overriding writeEntityToNBT() in all extending classes. Provide each possible extending
	 * class to this method and its public fields will be written to NBT automatically.
	 * 
	 * @param 	entity				The entity containing data to be written to NBT.
	 * @param 	nbt					The entity's NBTTagCompound provided by Miencraft.
	 * @param 	extendingClasses	A list of classes that extend from this entity's base class.
	 */
	public static void autoWriteEntityToNBT(Entity entity, NBTTagCompound nbt, Class... extendingClasses)
	{
		for (final Field field : entity.getClass().getFields())
		{
			if (isFieldDeclaredInAnExtendingClass(field, extendingClasses))
			{
				try
				{
					final String fieldName = field.getName();
					final String fieldType = field.getType().toString();
					
					if (!Modifier.isTransient(field.getModifiers()))
					{
						if (fieldType.contains("String"))
						{
							nbt.setString(fieldName, (String)field.get(entity));
						}

						else if (fieldType.contains("boolean"))
						{
							nbt.setBoolean(fieldName, Boolean.parseBoolean(field.get(entity).toString()));
						}

						else if (fieldType.contains("double"))
						{
							nbt.setDouble(fieldName, Double.parseDouble(field.get(entity).toString()));
						}

						else if (fieldType.contains("int"))
						{
							nbt.setInteger(fieldName, Integer.parseInt(field.get(entity).toString()));
						}

						else if (fieldType.contains("float"))
						{
							nbt.setFloat(fieldName, Float.parseFloat(field.get(entity).toString()));
						}
					}
				}
				
				catch (NullPointerException e)
				{
					continue;
				}

				catch (IllegalArgumentException e)
				{
					continue;
				}

				catch (IllegalAccessException e)
				{
					continue;
				}
			}
		}
	}
	
	/**
	 * Automatically reads the provided entity's public fields from NBT. <p>
	 * 
	 * <b>For public fields you <u>do not</u> want read from NBT, apply the <code>transient</code> modifier.</b>
	 * 
	 * @param 	entity	The entity containing fields to be read from NBT.
	 * @param 	nbt		The entity's NBTTagCompound provided by Minecraft.
	 */
	public static void autoReadEntityFromNBT(Entity entity, NBTTagCompound nbt)
	{
		autoReadEntityFromNBT(entity, nbt, entity.getClass());
	}
	
	/**
	 * Automatically reads the provided entity's public fields from NBT. <p>
	 * 
	 * <b>For public fields you <u>do not</u> want read from NBT, apply the <code>transient</code> modifier.</b> <p>
	 * 
	 * Use this method if you have a base/abstract class for an entity and want to avoid
	 * overriding readEntityFromNBT() in all extending classes. Provide each possible extending
	 * class to this method and its public fields will be read from NBT automatically.
	 * 
	 * @param 	entity				The entity containing fields to be read from NBT.
	 * @param 	nbt					The entity's NBTTagCompound provided by Miencraft.
	 * @param 	extendingClasses	A list of classes that extend from this entity's base class.
	 */
	public static void autoReadEntityFromNBT(Entity entity, NBTTagCompound nbt, Class... extendingClasses)
	{
		for (final Field field : entity.getClass().getFields())
		{
			final Class fieldDeclaringClass = field.getDeclaringClass();

			if (isFieldDeclaredInAnExtendingClass(field, extendingClasses))
			{
				try
				{
					final String fieldName = field.getName();
					final String fieldType = field.getType().toString();

					if (!Modifier.isTransient(field.getModifiers()))
					{
						if (fieldType.contains("String"))
						{
							field.set(entity, String.valueOf(nbt.getString(fieldName)));
						}

						else if (fieldType.contains("boolean"))
						{
							field.set(entity, Boolean.valueOf(nbt.getBoolean(fieldName)));
						}

						else if (fieldType.contains("double"))
						{
							field.set(entity, Double.valueOf(nbt.getDouble(fieldName)));
						}

						else if (fieldType.contains("int"))
						{
							field.set(entity, Integer.valueOf(nbt.getInteger(fieldName)));
						}

						else if (fieldType.contains("float"))
						{
							field.set(entity, Float.valueOf(nbt.getFloat(fieldName)));
						}
					}
				}

				catch (NullPointerException e)
				{
					continue;
				}

				catch (IllegalArgumentException e)
				{
					continue;
				}

				catch (IllegalAccessException e)
				{
					continue;
				}
			}
		}
	}
	
	private static boolean isFieldDeclaredInAnExtendingClass(Field field, Class... classes)
	{
		int assignableClasses = 0;
		
		for (Class clazz : classes)
		{
			if (clazz.isAssignableFrom(field.getDeclaringClass()))
			{
				assignableClasses++;
			}
		}
		
		return assignableClasses > 0;
	}
}
