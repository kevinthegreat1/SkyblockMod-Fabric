package com.kevinthegreat.skyblockmod.screen;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import com.kevinthegreat.skyblockmod.option.SkyblockModOptions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import java.util.function.Function;

public abstract class AbstractSkyblockModOptionsScreen extends GameOptionsScreen {
    protected final SkyblockModOptions options;
    protected GridWidget gridWidget;
    protected GridWidget.Adder adder;

    protected AbstractSkyblockModOptionsScreen(Screen parent, Text title) {
        super(parent, MinecraftClient.getInstance().options, title);
        options = SkyblockMod.skyblockMod.options;
    }

    @Override
    protected void init() {
        super.init();
        initGridWidget();
    }

    private void initGridWidget() {
        gridWidget = new GridWidget();
        gridWidget.getMainPositioner().marginX(5).marginBottom(4).alignHorizontalCenter();
        adder = gridWidget.createAdder(2);
    }

    protected void addGridWidget() {
        adder.add(ButtonWidget.builder(ScreenTexts.DONE, button -> client.setScreen(parent)).width(200).build(), 2, adder.copyPositioner().marginTop(6));
        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, height / 6 - 12, width, height, 0.5f, 0);
        gridWidget.forEachChild(this::addDrawableChild);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackGroundAndTitle(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }

    protected void renderBackGroundAndTitle(MatrixStack matrices) {
        renderBackground(matrices);
        drawCenteredTextWithShadow(matrices, textRenderer, title, width / 2, 15, 0xFFFFFF);
    }

    @Override
    public void removed() {
        options.save();
    }

    protected ButtonWidget addScreenButton(Text message, Function<Screen, Screen> screenFunction){
        return adder.add(createButton(message, screenFunction));
    }

    protected <T> ClickableWidget addOptionButton(SimpleOption<T> option){
        return adder.add(createButton(option));
    }

    protected ButtonWidget addButton(Text message, ButtonWidget.PressAction onPress){
        return adder.add(ButtonWidget.builder(message, onPress).build());
    }

    protected ButtonWidget createButton(Text message, Function<Screen, Screen> screenFunction) {
        return ButtonWidget.builder(message, button -> client.setScreen(screenFunction.apply(this))).build();
    }

    protected <T> ClickableWidget createButton(SimpleOption<T> option){
        return option.createWidget(gameOptions, 0, 0, ButtonWidget.DEFAULT_WIDTH);
    }
}
