package com.kevinthegreat.skyblockmod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class Reparty {
    public boolean reparty = false;
    private int memberCount = 0;
    private final List<String> members = new ArrayList<>();
    private String leader = "";

    public void onChatMessage(String message) {
        if (reparty) {
            if (message.startsWith("Party Members (")) {
                memberCount = Integer.parseInt(message.substring(15, message.length() - 1)) - 1;
                if (memberCount == 0) {
                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("The Party is empty."));
                    reparty = false;
                }
            } else if (message.startsWith("Party Leader")) {
                String[] messageContents = message.split(" ");
                MinecraftClient minecraftClient = MinecraftClient.getInstance();
                assert minecraftClient.player != null;
                if (!messageContents[2].equals(minecraftClient.player.getEntityName()) && !messageContents[3].equals(minecraftClient.player.getEntityName())) {
                    minecraftClient.inGameHud.getChatHud().addMessage(Text.of("You are not the party leader."));
                    reparty = false;
                }
            } else if (message.startsWith("Party M")) {
                String[] messageContents = message.split(" ");
                for (int i = 2; i < messageContents.length - 1; i++) {
                    if (!messageContents[i].contains("[") && !messageContents[i].contains("●")) {
                        members.add(messageContents[i]);
                        i++;
                    }
                }
                if (members.size() == memberCount) {
                    SkyblockMod.skyblockMod.queueMessage("/p disband", 200);
                    for (String player : members) {
                        SkyblockMod.skyblockMod.queueMessage("/p invite " + player, 300);
                    }
                    members.clear();
                    reparty = false;
                }
            } else if (message.startsWith("You are not currently in a party.")) {
                reparty = false;
            }
        } else if (message.endsWith("has disbanded the party!")) {
            String[] messageContents = message.split(" ");
            if (messageContents[0].contains("[")) {
                leader = messageContents[1];
            } else {
                leader = messageContents[0];
            }
        } else if (!leader.isEmpty() && message.contains(leader + " has invited you to join their party!")) {
            assert MinecraftClient.getInstance().player != null;
            if (!leader.equals(MinecraftClient.getInstance().player.getEntityName())) {
                SkyblockMod.skyblockMod.queueMessage("/p accept " + leader, 200);
                leader = "";
            }
        }
    }
}
