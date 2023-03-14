package com.kevinthegreat.skyblockmod.option;

import com.mojang.serialization.Codec;

public class StringTextFieldCallbacks extends TextFieldCallbacks<String> {
    public static StringTextFieldCallbacks INSTANCE = new StringTextFieldCallbacks();
    public static StringTextFieldCallbacks INSTANCE_256 = new StringTextFieldCallbacks(256);

    public StringTextFieldCallbacks() {
        super();
    }

    public StringTextFieldCallbacks(int maxLength) {
        super(maxLength);
    }

    @Override
    public String toValue(String text) {
        return text;
    }

    @Override
    public Codec<String> codec() {
        return Codec.STRING;
    }
}
