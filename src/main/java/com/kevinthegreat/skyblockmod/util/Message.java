package com.kevinthegreat.skyblockmod.util;

import de.hysky.skyblocker.utils.scheduler.MessageScheduler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;

import java.util.*;

public class Message {
    public final Map<String, String> commands = new HashMap<>();
    public final Map<String, String> commandsArgs = new HashMap<>();
    public final SortedMap<String, String> commandsAll = new TreeMap<>();
    private final Queue<Pair<String, Integer>> messageQueue = new LinkedList<>();
    private long lastMessage;

    public Message() {
        commands.put("/s", "/skyblock");
        commands.put("/sk", "/skyblock");
        commands.put("/sky", "/skyblock");
        commands.put("/i", "/is");
        commands.put("/h", "/hub");
        commands.put("/hu", "/hub");

        commands.put("/d", "/warp dungeon_hub");
        commands.put("/dn", "/warp dungeon_hub");
        commands.put("/dun", "/warp dungeon_hub");
        commands.put("/dungeon", "/warp dungeon_hub");

        commands.put("/bl", "/warp nether");
        commands.put("/blazing", "/warp nether");
        commands.put("/fortress", "/warp nether");
        commands.put("/crimson", "/warp nether");
        commands.put("/isles", "/warp nether");
        commands.put("/ci", "/warp nether");
        commands.put("/crimson isles", "/warp nether");
        commands.put("/n", "/warp nether");
        commands.put("/nether", "/warp nether");

        commands.put("/deep", "/warp deep");
        commands.put("/cavern", "/warp deep");
        commands.put("/caverns", "/warp deep");

        commands.put("/dw", "/warp mines");
        commands.put("/dwarven", "/warp mines");
        commands.put("/mi", "/warp mines");
        commands.put("/mines", "/warp mines");

        commands.put("/fo", "/warp forge");
        commands.put("/for", "/warp forge");
        commands.put("/forge", "/warp forge");

        commands.put("/cry", "/warp crystals");
        commands.put("/crystal", "/warp crystals");
        commands.put("/ho", "/warp crystals");
        commands.put("/hollows", "/warp crystals");
        commands.put("/ch", "/warp crystals");
        commands.put("/crystal hollows", "/warp crystals");

        commands.put("/ga", "/warp garden");
        commands.put("/garden", "/warp garden");
        commands.put("/go", "/warp gold");
        commands.put("/gold", "/warp gold");

        commands.put("/des", "/warp desert");
        commands.put("/desert", "/warp desert");
        commands.put("/mu", "/warp desert");
        commands.put("/mushroom", "/warp desert");

        commands.put("/sp", "/warp spider");
        commands.put("/spider", "/warp spider");
        commands.put("/spiders", "/warp spider");

        commands.put("/ba", "/warp barn");
        commands.put("/barn", "/warp barn");

        commands.put("/e", "/warp end");
        commands.put("/end", "/warp end");

        commands.put("/park", "/warp park");

        commands.put("/castle", "/warp castle");
        commands.put("/museum", "/warp museum");
        commands.put("/da", "/warp da");
        commands.put("/dark", "/warp da");
        commands.put("/crypt", "/warp crypt");
        commands.put("/crypts", "/warp crypt");
        commands.put("/nest", "/warp nest");
        commands.put("/magma", "/warp magma");
        commands.put("/void", "/warp void");
        commands.put("/drag", "/warp drag");
        commands.put("/dragon", "/warp drag");
        commands.put("/jungle", "/warp jungle");
        commands.put("/howl", "/warp howl");

        commands.put("/ca", "/chat all");
        commands.put("/cp", "/chat party");
        commands.put("/cg", "/chat guild");
        commands.put("/co", "/chat officer");
        commands.put("/cc", "/chat coop");

        commandsArgs.put("/m", "/msg");

        commandsArgs.put("/pa", "/p accept");
        commands.put("/pv", "/p leave");
        commands.put("/pd", "/p disband");
        commands.put("/rp", "/reparty");
        commands.put("/pr", "/reparty");

        commandsArgs.put("/v", "/visit");
        commands.put("/vp", "/visit portalhub");
        commands.put("/visit p", "/visit portalhub");

        commandsAll.putAll(commands);
        commandsAll.putAll(commandsArgs);
    }

    public void addMessage(Text message) {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(message);
    }

    public void sendMessageAfterCooldown(String message) {
        if (Info.SKYBLOCKER_LOADED) {
            MessageScheduler.INSTANCE.sendMessageAfterCooldown(message, message.startsWith("/"));
        } else {
            if (lastMessage + 200 < System.currentTimeMillis()) {
                sendMessage(message);
                lastMessage = System.currentTimeMillis();
            } else {
                messageQueue.add(new Pair<>(message, 0));
            }
        }
    }

    private void sendMessage(String message) {
        if (MinecraftClient.getInstance().player != null) {
            if (message.startsWith("/")) {
                MinecraftClient.getInstance().player.networkHandler.sendCommand(message.substring(1));
            } else {
                MinecraftClient.getInstance().inGameHud.getChatHud().addToMessageHistory(message);
                MinecraftClient.getInstance().player.networkHandler.sendChatMessage(message);
            }
        }
    }

    public void queueMessage(String message, int ticks) {
        messageQueue.add(new Pair<>(message, ticks));
    }

    public void tick() {
        if (!messageQueue.isEmpty()) {
            if (messageQueue.peek().getRight() <= 0 && lastMessage + 200 < System.currentTimeMillis()) {
                sendMessage(messageQueue.poll().getLeft());
                lastMessage = System.currentTimeMillis();
            } else {
                messageQueue.peek().setRight(messageQueue.peek().getRight() - 1);
            }
        }
    }
}
