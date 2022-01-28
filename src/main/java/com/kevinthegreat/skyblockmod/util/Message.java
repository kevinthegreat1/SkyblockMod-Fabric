package com.kevinthegreat.skyblockmod.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Pair;

import java.util.LinkedList;
import java.util.Queue;

public class Message {
    private final Queue<Pair<String, Integer>> messageQueue = new LinkedList<>();
    private long lastMessage;

    public void sendMessage(String message) {
        if (lastMessage + 200 < System.currentTimeMillis()) {
            MinecraftClient.getInstance().player.sendChatMessage(message);
            lastMessage = System.currentTimeMillis();
        } else {
            messageQueue.add(new Pair<>(message, 0));
        }
    }

    public void queueMessage(String message, int ticks) {
        messageQueue.add(new Pair<>(message, ticks));
    }

    public void tick() {
        if (!messageQueue.isEmpty()) {
            if (messageQueue.peek().getRight() <= 0 && lastMessage + 200 < System.currentTimeMillis()) {
                MinecraftClient.getInstance().player.sendChatMessage(messageQueue.poll().getLeft());
                lastMessage = System.currentTimeMillis();
            } else {
                messageQueue.peek().setRight(messageQueue.peek().getRight() - 1);
            }
        }
    }
}
