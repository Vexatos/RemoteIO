package com.dmillerw.remoteIO.block.render;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.tileentity.TileEntity;

public class EnderLinkRenderHelper {

	public static class BlockDetail {
		public int meta;
		
		public String tileClass;
		
		public BlockDetail(int meta) {
			this.meta = meta;
		}
		
		public BlockDetail(TileEntity tile) {
			this.meta = tile.blockMetadata;
			this.tileClass = tile.getClass().getName().substring(tile.getClass().getName().lastIndexOf(".") + 1);
		}
		
		public BlockDetail(int meta, String tileClass) {
			this.meta = meta;
			this.tileClass = tileClass;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof BlockDetail) {
				equals(obj);
			}
			return false;
		}
		
		public boolean equals(BlockDetail detail) {
			return meta == detail.meta && (this.tileClass != null && detail.tileClass != null && this.tileClass.equals(detail.tileClass));
		}
		
		@Override
		public String toString() {
			return "META: " + meta + " TILE: " + (this.tileClass != null ? this.tileClass : "NULL");
		}
	}
	
	public static Map<BlockDetail, Float> blockToAngleMapping = new HashMap<BlockDetail, Float>();
	
	static {
		/* IC2 CABLES */
		blockToAngleMapping.put(new BlockDetail(0, "TileEntityCable"), 0F);
		blockToAngleMapping.put(new BlockDetail(1, "TileEntityCable"), 25F);
		blockToAngleMapping.put(new BlockDetail(2, "TileEntityCable"), 30F);
		blockToAngleMapping.put(new BlockDetail(3, "TileEntityCable"), 0F);
		blockToAngleMapping.put(new BlockDetail(5, "TileEntityCable"), 0F);
		blockToAngleMapping.put(new BlockDetail(9, "TileEntityCable"), 25F);
		blockToAngleMapping.put(new BlockDetail(10, "TileEntityCable"), 25F);
	
		/* BC PIPE */
		blockToAngleMapping.put(new BlockDetail(0, "TileGenericPipe"), -23F);
	}
	
	public static float getAngle(BlockDetail detail) {
		for (Entry<BlockDetail, Float> entry : blockToAngleMapping.entrySet()) {
			if (entry.getKey().equals(detail)) {
				return entry.getValue();
			}
		}
		return 0F;
	}
	
}
