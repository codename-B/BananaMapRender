package com.ubempire.render;

import java.util.TimerTask;

public class RenderStarterTask extends TimerTask {

	BananaMapRender plugin;
    RenderStarterTask(BananaMapRender plugin) {
        this.plugin = plugin;
    }
    
    @Override
	public void run() {
        for (int i = 0; i < plugin.threadQueue.size(); i++) {
            GeneratorThread t = plugin.threadQueue.get(i);
            if (!t.isAlive()) {
                if (!t.done && plugin.renderThreads < plugin.varMaxThreads()) {
                    plugin.renderThreads++;
                    try {
                        t.start();
                    } catch (IllegalThreadStateException e) {
                        t.done = true;
                    }
                }
                if (t.done) {
                    plugin.renderThreads--;
                    plugin.threadQueue.remove(i);
                    i--;
                }
            }
        }
    }

}
