package com.kevinthegreat.skyblockmod.util;

import com.kevinthegreat.skyblockmod.dungeons.DungeonMap;
import com.kevinthegreat.skyblockmod.SkyblockMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class Config {

    public void load() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(SkyblockMod.MOD_ID + ".txt"));
            String line = reader.readLine();
            DungeonMap dungeonMap = SkyblockMod.skyblockMod.dungeonMap;
            while (line != null) {
                String[] args = line.split(":");
                try {
                    switch (args[0]) {
                        case "mapScale" -> dungeonMap.mapScale = Float.parseFloat(args[1]);
                        case "mapOffsetX" -> dungeonMap.mapOffsetx = Integer.parseInt(args[1]);
                        case "mapOffsetY" -> dungeonMap.mapOffsety = Integer.parseInt(args[1]);
                    }
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                    LogManager.getLogger().error("Unable to parse configuration \"" + args[0] + "\".");
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            LogManager.getLogger().info("Configuration file not found.");
        } catch (IOException e) {
            LogManager.getLogger().error("Failed to read configuration file.");
        }
    }

    public void save() {
        DungeonMap dungeonMap = SkyblockMod.skyblockMod.dungeonMap;
        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(SkyblockMod.MOD_ID + ".txt")));
            writer.println("mapScale:" + dungeonMap.mapScale);
            writer.println("mapOffsetX:" + dungeonMap.mapOffsetx);
            writer.println("mapOffsetY:" + dungeonMap.mapOffsety);
            writer.close();
        } catch (IOException e) {
            Logger logger = LogManager.getLogger();
            logger.error("Failed to write configuration file. Logging configuration.");
            logger.info("mapScale:" + dungeonMap.mapScale);
            logger.info("mapOffsetX:" + dungeonMap.mapOffsetx);
            logger.info("mapOffsetY:" + dungeonMap.mapOffsety);
        }
    }
}