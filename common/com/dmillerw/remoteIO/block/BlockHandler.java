package com.dmillerw.remoteIO.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

import com.dmillerw.remoteIO.block.item.ItemBlockEnder;
import com.dmillerw.remoteIO.block.item.ItemBlockMachine;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;

public class BlockHandler {

	public static int blockMachineID;
	public static int blockEnderID;

	public static Block blockMachine;
	public static Block blockEnder;
	
	public static void handleConfig(Configuration config) {
		config.addCustomCategoryComment(Configuration.CATEGORY_BLOCK, "Set any ID to 0 to disable block");
		
		Property machine = config.getBlock("block_id.blockMachine", 500);
		machine.comment = "Generic machine blocks (heater/reservoir)";
		blockMachineID = machine.getInt(500);
		
		Property ender = config.getBlock("block_id.blockEnder", 501);
		ender.comment = "Ender blocks (remote/link/proxy)";
		blockEnderID = ender.getInt(501);
	}
	
	public static void initializeBlocks() {
		if (blockMachineID != 0) {
			blockMachine = new BlockMachine(blockMachineID).setUnlocalizedName("blockMachine");
			GameRegistry.registerBlock(blockMachine, ItemBlockMachine.class, "blockMachine");
			for (int i=0; i<BlockMachine.INTERNAL_NAMES.length; i++) {
				LanguageRegistry.addName(new ItemStack(blockMachine, 1, i), BlockMachine.NAMES[i]);
			}
		}
		
		if (blockEnderID != 0) {
			blockEnder = new BlockEnder(blockEnderID).setUnlocalizedName("blockEnder");
			GameRegistry.registerBlock(blockEnder, ItemBlockEnder.class, "blockEnder");
			for (int i=0; i<BlockEnder.INTERNAL_NAMES.length; i++) {
				LanguageRegistry.addName(new ItemStack(blockEnder, 1, i), BlockEnder.NAMES[i]);
			}
		}
	}
	
}
