package com.dmillerw.remoteIO.ender;

import com.dmillerw.remoteIO.item.ItemUpgrade.Upgrade;

public interface IEnderLink {

	public int getFrequency();

	public boolean hasUpgrade(Upgrade upgrade);
	
}
