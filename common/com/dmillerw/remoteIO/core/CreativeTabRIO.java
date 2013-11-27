package com.dmillerw.remoteIO.core;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import com.dmillerw.remoteIO.RemoteIO;
import com.dmillerw.remoteIO.item.ItemUpgrade.Upgrade;
import com.dmillerw.remoteIO.lib.ModInfo;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class CreativeTabRIO extends CreativeTabs {

	public static CreativeTabs tab;
	
	public int itemID = 1;
	public int itemMeta = 0;

	static {
		tab = new CreativeTabRIO(ModInfo.NAME).setIcon(Block.stone.blockID, 0);
	}

	public CreativeTabRIO(String label) {
		super(label.toLowerCase().replace(" ", "_"));
		LanguageRegistry.instance().addStringLocalization("itemGroup." + label.toLowerCase().replace(" ", "_"), label);
	}

	public CreativeTabRIO setIcon(int id, int meta) {
		this.itemID = id;
		this.itemMeta = meta;
		return this;
	}

	public CreativeTabRIO setIcon(ItemStack stack) {
		this.itemID = stack.itemID;
		this.itemMeta = stack.getItemDamage();
		return this;
	}

	@Override
	public ItemStack getIconItemStack() {
		return new ItemStack(itemID, 1, itemMeta);
	}

}
