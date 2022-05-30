package com.kevinthegreat.skyblockmod.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class Api {
    private final Gson gson = new Gson();
    private final Map<String, String> accounts = new HashMap<>();
    public String apiKey; //e8bc7f4e-8a6a-4a65-aacb-fadcb2d2ab53

    public void getUuidByNameAsync(String name, Consumer<String> consumer) {
        String uuid = accounts.get(name);
        if (uuid != null) {
            consumer.accept(uuid);
        }
        CompletableFuture.supplyAsync(() -> getSync("https://api.mojang.com/users/profiles/minecraft/" + name)).thenApply(this::getUuidByNameAsyncCallback).thenAccept(consumer);
    }

    public String getUuidByNameAsyncCallback(JsonObject jsonObject) {
        String uuid = jsonObject.get("id").getAsString();
        if (uuid != null) {
            accounts.put(jsonObject.get("name").getAsString(), uuid);
            return uuid;
        }
        return null;
    }

    public void getHypixelWithKeyAsync(String url, Consumer<JsonObject> consumer) {
        if (apiKey == null) {
            consumer.accept(gson.fromJson("{\"success\":false,\"cause\":\"Invalid API key\"}", JsonObject.class));
        } else {
            getHypixelAsync(url + "&key=" + apiKey, consumer);
        }
    }

    public void getHypixelAsync(String url, Consumer<JsonObject> consumer) {
        getAsync("https://api.hypixel.net" + url, consumer);
    }

    public void getAsync(String url, Consumer<JsonObject> consumer) {
        CompletableFuture.supplyAsync(() -> getSync(url)).thenAccept(consumer);
    }

    public JsonObject getSync(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            return switch (connection.getResponseCode()) {
                case 200 -> gson.fromJson(new InputStreamReader(connection.getInputStream()), JsonObject.class);
                case 403 -> gson.fromJson("{\"success\":false,\"cause\":\"Invalid API key\"}", JsonObject.class);
                default -> gson.fromJson("{\"success\":false,\"cause\":\"Error response code\"}", JsonObject.class);
            };
        } catch (IOException e) {
            e.printStackTrace();
            return gson.fromJson("{\"success\":false,\"cause\":\"IOException\"}", JsonObject.class);
        }
    }
}
