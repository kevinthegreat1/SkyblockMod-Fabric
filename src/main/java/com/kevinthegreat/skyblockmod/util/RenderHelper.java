package com.kevinthegreat.skyblockmod.util;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import com.kevinthegreat.skyblockmod.mixins.accessors.BeaconBlockEntityRendererInvoker;
import com.mojang.blaze3d.systems.RenderSystem;
import me.x150.renderer.render.Renderer3d;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.awt.*;

public class RenderHelper {
    public static void renderFilledThroughWallsWithBeaconBeam(WorldRenderContext context, BlockPos pos, float[] colorComponents, float alpha) {
        renderFilledThroughWalls(context, pos, colorComponents, alpha);
        renderBeaconBeam(context, pos, colorComponents);
    }

    public static void renderFilledThroughWalls(WorldRenderContext context, BlockPos pos, float[] colorComponents, float alpha) {
        Renderer3d.renderThroughWalls();
        Renderer3d.renderFilled(context.matrixStack(), new Color(colorComponents[0], colorComponents[1], colorComponents[2], alpha), Vec3d.of(pos), new Vec3d(1, 1, 1));
        Renderer3d.stopRenderThroughWalls();
    }

    public static void renderBeaconBeam(WorldRenderContext context, BlockPos pos, float[] colorComponents) {
        context.matrixStack().push();
        context.matrixStack().translate(pos.getX() - context.camera().getPos().x, pos.getY() - context.camera().getPos().y, pos.getZ() - context.camera().getPos().z);
        BeaconBlockEntityRendererInvoker.renderBeam(context.matrixStack(), context.consumers(), context.tickDelta(), context.world().getTime(), 0, BeaconBlockEntityRenderer.MAX_BEAM_HEIGHT, colorComponents);
        context.matrixStack().pop();
    }

    public static void renderVerticalPlane(WorldRenderContext context, BlockPos pos, Vec3d pos1, Vec3d pos2, float[] colorComponents, float alpha) { //FIXME why not work?
        SkyblockMod.LOGGER.info("Rendering vertical plane at pos: " + pos + " with pos1: " + pos1 + " and pos2: " + pos2); //FIXME remove
        RenderSystem.disableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        context.matrixStack().push();
        context.matrixStack().translate(pos.getX() - context.camera().getPos().x, pos.getY() - context.camera().getPos().y, pos.getZ() - context.camera().getPos().z);
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        Matrix4f matrix = context.matrixStack().peek().getPositionMatrix();
        float x1 = (float) pos1.x;
        float z1 = (float) pos1.z;
        float x2 = (float) pos2.x;
        float z2 = (float) pos2.z;
        float red = colorComponents[0];
        float green = colorComponents[1];
        float blue = colorComponents[2];
        buffer.vertex(matrix, x1, -1, z1).color(red, green, blue, alpha).next(); //FIXME use large number (256?) for y
        buffer.vertex(matrix, x1, 1, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, 1, z2).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, -1, z2).color(red, green, blue, alpha).next();

        BufferRenderer.drawWithGlobalProgram(buffer.end());
        context.matrixStack().pop();
        RenderSystem.enableDepthTest();
    }
}
