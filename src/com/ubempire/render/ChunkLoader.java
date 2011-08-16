package com.ubempire.render;

import org.bukkit.ChunkSnapshot;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldListener;

public class ChunkLoader extends WorldListener {
	BananaMapRender bmr;

	ChunkLoader(BananaMapRender bmr) {
		this.bmr = bmr;
	}

	public void onChunkLoad(ChunkLoadEvent event) {
	
	}

	public void onChunkUnload(ChunkUnloadEvent event) {

	}
}
