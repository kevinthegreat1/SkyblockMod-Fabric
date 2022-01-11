package com.kevinthegreat.messagereplacer;

import com.kevinthegreat.messagereplacer.util.Util;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Pair;
import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class MessageReplacer implements ModInitializer {

    public static final String MOD_ID = "messagereplacer";
    public static MessageReplacer messageReplacer;
    public int tick = 0;
    public final Map<String, String> commands = new HashMap<>();
    public final Map<String, String> commandsArgs = new HashMap<>();
    public Queue<Pair<String, Long>> messageQueue = new LinkedList<>();
    public final Util util = new Util();
    public final Reparty reparty = new Reparty();

    public float mapScale = 1;
    public int mapOffsetx = 0;
    public int mapOffsety = 0;

    @Override
    public void onInitialize() {
        messageReplacer = this;
        load();
        DungeonMap.register();

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

        commandsArgs.put("/m", "/msg");

        commandsArgs.put("/pa", "/p accept");
        commands.put("/pv", "/p leave");
        commands.put("/pd", "/p disband");
        commands.put("/rp", "/reparty");
        commands.put("/pr", "/reparty");

        commandsArgs.put("/v", "/visit");
        commands.put("/vp", "/visit portalhub");
        commands.put("/visit p", "/visit portalhub");

        LogManager.getLogger().info("MessageReplacer Initialized");
    }

    public void load() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(MOD_ID + ".txt"));
            String line = reader.readLine();
            while (line != null) {
                String[] args = line.split(":");
                assert args.length == 2;
                switch (args[0]) {
                    case "mapScale" -> mapScale = Float.parseFloat(args[1]);
                    case "mapOffsetX" -> mapOffsetx = Integer.parseInt(args[1]);
                    case "mapOffsetY" -> mapOffsety = Integer.parseInt(args[1]);
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException ignored) {
        }
    }

    public void tick() {
        if (tick % 20 == 0) {
            util.check();
            tick = 0;
        }
        if (!messageQueue.isEmpty()) {
            messageQueue.peek().setRight(messageQueue.peek().getRight() - 1);
            assert messageQueue.peek() != null;
            if (messageQueue.peek().getRight() <= 0) {
                assert MinecraftClient.getInstance().player != null;
                assert messageQueue.peek() != null;
                MinecraftClient.getInstance().player.sendChatMessage(messageQueue.poll().getLeft());
            }
        }
        tick++;
    }

    public void stop() {
        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(MOD_ID + ".txt")));
            if (mapScale != 1) {
                writer.println("mapScale:" + mapScale);
            }
            if (mapOffsetx != 0) {
                writer.println("mapOffsetX:" + mapOffsetx);
            }
            if (mapOffsety != 0) {
                writer.println("mapOffsetY:" + mapOffsety);
            }
            writer.close();
        } catch (IOException ignored) {
        }
    }

    public void queueMessage(String message, long millis) {
        messageQueue.add(new Pair<>(message, millis / 50));
    }
}
