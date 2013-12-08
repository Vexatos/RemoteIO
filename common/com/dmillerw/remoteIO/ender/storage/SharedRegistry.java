package com.dmillerw.remoteIO.ender.storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;

import com.dmillerw.remoteIO.core.IOLogger;
import com.dmillerw.remoteIO.ender.IEnderLink;
import com.dmillerw.remoteIO.ender.storage.SharedStorage.StorageType;

import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class SharedRegistry {

	public static EnumMap<StorageType, Class<? extends SharedStorage>> typeToClassMapping = new EnumMap<StorageType, Class<? extends SharedStorage>>(StorageType.class);
	
	static {
		typeToClassMapping.put(StorageType.ITEM, SharedInventory.class);
		typeToClassMapping.put(StorageType.FLUID, SharedTank.class);
		typeToClassMapping.put(StorageType.ENERGY_RF, SharedEnergyRF.class);
		typeToClassMapping.put(StorageType.ENERGY_MJ, SharedEnergyMJ.class);
		typeToClassMapping.put(StorageType.AE_GRID, SharedAEGrid.class);
	}
	
	private static SharedRegistry INSTANCE = new SharedRegistry();

	public static SharedRegistry instance() {
		return INSTANCE;
	}
	
	public static File getSaveDir() {
		return new File(DimensionManager.getCurrentSaveRootDirectory(), "RemoteIO");
	}
	
	public Map<String, SharedStorage> storageMap = new HashMap<String, SharedStorage>();
	public Map<String, SharedStorage> dirtyStorage = new HashMap<String, SharedStorage>();
	
	private NBTTagCompound savedTag;
	
	public void dirty(IEnderLink link, SharedStorage storage) {
		dirtyStorage.put(link.getFrequency() + "|" + storage.getType().toString(), storage);
	}
	
	public SharedStorage getStorage(StorageType type, IEnderLink link) {
		return getStorageForFrequency(type, link.getFrequency(), false);
	}
	
	public SharedStorage getStorageForFrequency(StorageType type, int freq, boolean force) {
		try {
			String key = freq + "|" + type.toString();
			
			if (storageMap.containsKey(key)) { // If exists and loaded
				SharedStorage storage = storageMap.get(key);
				dirtyStorage.put(key, storage);
				return storage;
			} else if (savedTag.hasKey("key")) { // Else if in NBT tag
				SharedStorage storage = typeToClassMapping.get(type).newInstance().generateEmpty();
				storage.readFromNBT(savedTag.getCompoundTag("key"));
				storageMap.put(key, storage);
				dirtyStorage.put(key, storage);
				return storage;
			} else if (force){ // Generate new
				SharedStorage storage = typeToClassMapping.get(type).newInstance().generateEmpty();
				storageMap.put(key, storage);
				dirtyStorage.put(key, storage);
				return storage;
			} else {
				return null;
			}
		} catch(Exception ex) {
			IOLogger.logger.severe("Failed to create/get storage for type " + type.toString() + " for frequency " + freq);
			IOLogger.logger.severe("Reason: " + ex.getLocalizedMessage());
			return null;
		}
	}
	
	public SharedStorage[] getAllStorageOnFrequency(IEnderLink link) {
		return getAllStorageOnFrequency(link.getFrequency());
	}
	
	public SharedStorage[] getAllStorageOnFrequency(int freq) {
		List<SharedStorage> validStorage = new ArrayList<SharedStorage>();
		for (StorageType type : StorageType.values()) {
			SharedStorage storage = getStorageForFrequency(type, freq, false);
			if (storage != null) {
				validStorage.add(storage);
			}
		}
		return validStorage.toArray(new SharedStorage[validStorage.size()]);			
	}
	
	private void load() {
		File file = new File(getSaveDir(), "sharedStorage.dat");
		
		if (getSaveDir().exists()) {
			getSaveDir().mkdirs();
		}
		
		try {
			if (file.exists() && file.length() > 0L) {
				DataInputStream din = new DataInputStream(new FileInputStream(file));
				this.savedTag = CompressedStreamTools.readCompressed(din);
				din.close();
			} else {
				this.savedTag = new NBTTagCompound();
			}
		} catch(Exception ex) {
			IOLogger.logger.severe("Failed to load saved storage file!");
			throw new RuntimeException(ex);
		}
	}
	
	private void save() {
		File file = new File(getSaveDir(), "sharedStorage.dat");
		
		if (getSaveDir().exists()) {
			getSaveDir().mkdirs();
		}
		
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			
			// Save all dirty storages to NBT
			for (Entry<String, SharedStorage> dirtyEntry : dirtyStorage.entrySet()) {
				NBTTagCompound tag = new NBTTagCompound();
				dirtyEntry.getValue().writeToNBT(tag);
				this.savedTag.setTag(dirtyEntry.getKey(), tag);
			}
			this.dirtyStorage.clear();
			
			DataOutputStream dout = new DataOutputStream(new FileOutputStream(file));
			CompressedStreamTools.writeCompressed(this.savedTag, dout);
			dout.close();
		} catch(Exception ex) {
			IOLogger.logger.severe("Failed to save saved storage file!");
			throw new RuntimeException(ex);
			//TODO Fix error with files not being created
		}
	}
	
	@ForgeSubscribe
	public void onWorldSave(WorldEvent.Save event) {
		save();
	}
	
	public void onServerStarting(FMLServerStartingEvent event) {
		load();
	}
	
}
