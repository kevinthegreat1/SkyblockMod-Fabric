package com.kevinthegreat.messagereplacer;

import java.util.*;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Pair;

public class MessageReplacer implements ModInitializer {

    public static final String MOD_ID = "messagereplacer";
    public static MessageReplacer messageReplacer;
    public static final Map<String, String> commands = new HashMap<>();
    public static final Map<String, String> commandsArgs = new HashMap<>();
    public static Queue<Pair<String, Long>> messageQueue = new LinkedList<>();
    public static final Reparty reparty = new Reparty();

    @Override
    public void onInitialize() {
        messageReplacer = this;

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
        commands.put("/n", "/warp nether");
        commands.put("/nether", "/warp nether");

        commands.put("/deep", "/warp deep");
        commands.put("/cavern", "/warp deep");
        commands.put("/caverns", "/warp deep");

        commands.put("/dw", "/warp mines");
        commands.put("/dwarven", "/warp mines");
        commands.put("/mi", "/warp mines");
        commands.put("/mines", "/warp mines");

        commands.put("/f", "/warp forge");
        commands.put("/for", "/warp forge");
        commands.put("/forge", "/warp forge");

        commands.put("/c", "/warp crystals");
        commands.put("/crystal", "/warp crystals");
        commands.put("/ho", "/warp crystals");
        commands.put("/hollows", "/warp crystals");
        commands.put("/ch", "/warp crystals");

        commands.put("/g", "/warp gold");
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

        commands.put("/p", "/warp park");
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

        commandsArgs.put("/m","/msg");

        commandsArgs.put("/pa", "/p accept");
        commands.put("/pv", "/p leave");
        commands.put("/pd", "/p disband");
        commands.put("/rp", "/reparty");
        commands.put("/pr", "/reparty");

        commandsArgs.put("/v", "/visit");
        commands.put("/vp", "/visit portalhub");
        commands.put("/visit p", "/visit portalhub");
        System.out.println("MessageReplacer Initialized");
    }

    public void queueMessage(String message, long millis) {
        messageQueue.add(new Pair<>(message, millis / 50));
    }

    public void tick() {
        if (!messageQueue.isEmpty()) {
            messageQueue.peek().setRight(messageQueue.peek().getRight() - 1);
            assert messageQueue.peek() != null;
            if (messageQueue.peek().getRight() <= 0) {
                assert MinecraftClient.getInstance().player != null;
                assert messageQueue.peek() != null;
                MinecraftClient.getInstance().player.sendChatMessage(messageQueue.poll().getLeft());
            }
        }
    }
}
