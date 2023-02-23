package com.kevinthegreat.skyblockmod.dungeons;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import com.kevinthegreat.skyblockmod.util.ChatListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class Reparty implements ChatListener {
    public boolean on = true;
    private boolean reparty = false;
    private int memberCount = 0;
    private final List<String> members = new ArrayList<>();
    private String leader = "";

    public void start() {
        reparty = true;
    }

    @Override
    public boolean onChatMessage(String message) {
        if (on && SkyblockMod.skyblockMod.info.hypixel) {
            if (reparty) {
                if (message.startsWith("Party Members (")) {
                    memberCount = Integer.parseInt(message.substring(15, message.length() - 1)) - 1;
                    if (memberCount == 0) {
                        SkyblockMod.skyblockMod.message.addMessage(Text.translatable("skyblockmod:party.empty"));
                        reparty = false;
                    }
                } else if (message.startsWith("Party Leader")) {
                    String[] messageContents = message.split(" ");
                    MinecraftClient minecraftClient = MinecraftClient.getInstance();
                    assert minecraftClient.player != null;
                    if (!messageContents[2].equals(minecraftClient.player.getEntityName()) && !messageContents[3].equals(minecraftClient.player.getEntityName())) {
                        SkyblockMod.skyblockMod.message.addMessage(Text.translatable("skyblockmod:party.notLeader"));
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
                        SkyblockMod.skyblockMod.message.queueMessage("/p disband", 4);
                        for (String player : members) {
                            SkyblockMod.skyblockMod.message.queueMessage("/p invite " + player, 6);
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
                    SkyblockMod.skyblockMod.message.sendMessageAfterCooldown("/p accept " + leader);
                    leader = "";
                }
            }
        }
        return true;
    }
}
