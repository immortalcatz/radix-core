/*******************************************************************************
 * AbstractGui.java
 * Copyright (c) 2014 Radix-Shock Entertainment.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package com.radixshock.radixcore.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Base pausing GUI class that allows exiting by pressing escape.
 */
@SideOnly(Side.CLIENT)
public abstract class AbstractGui extends GuiScreen
{
	/** The last GUI screen shown. */
	protected GuiScreen		parentGui;

	/** An instance of the player that opened this GUI. */
	protected EntityPlayer	player;

	/**
	 * Constructor
	 * 
	 * @param player
	 *            The player who caused this GUI to open.
	 */
	public AbstractGui(EntityPlayer player)
	{
		this.player = player;
	}

	/**
	 * Displays the current gui's parent GUI.
	 */
	public void back()
	{
		if (parentGui != null)
		{
			Minecraft.getMinecraft().displayGuiScreen(parentGui);
		}

		else
		{
			Minecraft.getMinecraft().displayGuiScreen(null);
		}
	}

	/**
	 * Closes the GUI screen.
	 */
	public void close()
	{
		Minecraft.getMinecraft().displayGuiScreen(null);
	}

	@Override
	protected void keyTyped(char eventCharacter, int eventKey)
	{
		if (eventKey == Keyboard.KEY_ESCAPE)
		{
			close();
		}
	}
}
