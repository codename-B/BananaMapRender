package com.ubempire.render;

import java.io.File;
import org.bukkit.World;

public class FileList {

public String[] listTiles(World world) {
	
	File dir = new File("plugins/BukkitHTTPD/world");

	String[] children = dir.list();
	if (children == null) {
	    // Either dir does not exist or is not a directory
		
	} else {
		String[] filenames = new String[children.length];
	    int count = 0;
		for (int i=0; i<children.length; i++) {
	        // Get filename of file or directory
	    	if(children[i].endsWith(".png")&&children[i].contains(","))
	    	{
	    	
	        filenames[count] = children[i];
	        count++;
	    	}
	       
	    }
	return filenames;
	}
return null;
}	
}
