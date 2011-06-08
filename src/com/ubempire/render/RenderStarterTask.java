package com.ubempire.render;

import java.util.TimerTask;

public class RenderStarterTask extends TimerTask {

    BananaMapRender plugin;
    RenderStarterTask(BananaMapRender plugin) {
        this.plugin = plugin;
    }
    
    public void run() {
        for (int i = 0; i < plugin.threadQueue.size(); i++) {
            GeneratorThread t = plugin.threadQueue.get(i);
            if (!t.isAlive()) {
                if (!t.done && plugin.renderThreads < plugin.getConfiguration().getInt("max-threads", 4)) {
                    plugin.renderThreads += 1;
                    try {
                        t.start();
                    } catch (IllegalThreadStateException e) {
                        t.done = true;
                    }
                }
                if (t.done) {
                    plugin.renderThreads -= 1;
                    plugin.threadQueue.remove(i);
                    i -= 1;
                }
            }
        }
    }

}
