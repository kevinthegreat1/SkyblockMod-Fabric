package com.kevinthegreat.skyblockmod.screen;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import com.kevinthegreat.skyblockmod.option.SkyblockModOptions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.function.Function;

public abstract class AbstractSkyblockModOptionsScreen extends GameOptionsScreen {
    protected final SkyblockModOptions options;

    protected AbstractSkyblockModOptionsScreen(Screen parent, Text title) {
        super(parent, MinecraftClient.getInstance().options, title);
        options = SkyblockMod.skyblockMod.options;
    }

    @Override
    public void removed() {
        options.save();
    }

    protected ButtonWidget createScreenButton(Text message, Function<Screen, Screen> screenFunction) {
        return ButtonWidget.builder(message, button -> client.setScreen(screenFunction.apply(this))).build();
    }

    protected ButtonWidget createButton(Text message, ButtonWidget.PressAction onPress) {
        return ButtonWidget.builder(message, onPress).build();
    }
}
