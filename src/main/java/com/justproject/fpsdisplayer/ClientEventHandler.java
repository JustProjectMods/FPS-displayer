package com.justproject.fpsdisplayer;

import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.LinkedList;
import java.util.Queue;

public class ClientEventHandler {
    private static final Queue<Long> leftClicks = new LinkedList<>();
    private static final Queue<Long> rightClicks = new LinkedList<>();
    private static final long CLICK_TIME_WINDOW = 1000;
    
    @SubscribeEvent
    public void onMouseClick(InputEvent.RawMouseEvent event) {
        if (event.getButton() == 0 && event.getAction() == 1) {
            leftClicks.add(System.currentTimeMillis());
        } else if (event.getButton() == 1 && event.getAction() == 1) {
            rightClicks.add(System.currentTimeMillis());
        }
    }
    
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        cleanOldClicks(leftClicks);
        cleanOldClicks(rightClicks);
    }
    
    private void cleanOldClicks(Queue<Long> clicks) {
        long currentTime = System.currentTimeMillis();
        while (!clicks.isEmpty() && currentTime - clicks.peek() > CLICK_TIME_WINDOW) {
            clicks.poll();
        }
    }
    
    public static int getLeftCPS() {
        return leftClicks.size();
    }
    
    public static int getRightCPS() {
        return rightClicks.size();
    }
}