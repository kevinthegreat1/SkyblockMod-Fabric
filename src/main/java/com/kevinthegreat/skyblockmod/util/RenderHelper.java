package com.kevinthegreat.skyblockmod.util;

import com.kevinthegreat.skyblockmod.mixins.accessors.BeaconBlockEntityRendererInvoker;
import com.mojang.blaze3d.systems.RenderSystem;
import me.x150.renderer.render.Renderer3d;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RenderHelper {
    public static void renderFilledWithBeaconBeam(WorldRenderContext context, BlockPos pos, float[] colorComponents, float alpha, boolean throughWalls) {
        renderFilled(context, pos, colorComponents, alpha, throughWalls);
        renderBeaconBeam(context, pos, colorComponents);
    }

    public static void renderFilled(WorldRenderContext context, BlockPos pos, float[] colorComponents, float alpha, boolean throughWalls) {
        if (throughWalls) {
            Renderer3d.renderThroughWalls();
        }
        Renderer3d.renderFilled(context.matrixStack(), new Color(colorComponents[0], colorComponents[1], colorComponents[2], alpha), Vec3d.of(pos), new Vec3d(1, 1, 1));
        if (throughWalls) {
            Renderer3d.stopRenderThroughWalls();
        }
    }

    public static void renderBeaconBeam(WorldRenderContext context, BlockPos pos, float[] colorComponents) {
        context.matrixStack().push();
        context.matrixStack().translate(pos.getX() - context.camera().getPos().x, pos.getY() - context.camera().getPos().y, pos.getZ() - context.camera().getPos().z);
        BeaconBlockEntityRendererInvoker.renderBeam(context.matrixStack(), context.consumers(), context.tickDelta(), context.world().getTime(), 0, BeaconBlockEntityRenderer.MAX_BEAM_HEIGHT, colorComponents);
        context.matrixStack().pop();
    }

    /**
     * Renders the outline of a box with the specified color components and line width.
     * This does not use renderer since renderer draws outline using debug lines with a fixed width.
     *
     * @author AzureAaron
     * @author minor edits by Kevinthegreat
     */
    public static void renderOutline(WorldRenderContext context, Box box, float[] colorComponents, float lineWidth, boolean throughWalls) {
        MatrixStack matrices = context.matrixStack();
        Vec3d camera = context.camera().getPos();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder buffer = tessellator.getBuffer();

        RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.lineWidth(lineWidth);
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(throughWalls ? GL11.GL_ALWAYS : GL11.GL_LEQUAL);

        matrices.push();
        matrices.translate(-camera.getX(), -camera.getY(), -camera.getZ());

        buffer.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
        WorldRenderer.drawBox(matrices, buffer, box, colorComponents[0], colorComponents[1], colorComponents[2], 1f);
        tessellator.draw();

        matrices.pop();
        RenderSystem.lineWidth(1f);
        RenderSystem.enableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
    }

    /**
     * Draws lines from point to point.<br><br>
     * <p>
     * Tip: To draw lines from the center of a block, offset the X, Y and Z each by 0.5
     * <p>
     * Note: This is super messed up when drawing long lines. Tried different normals and {@link VertexFormat.DrawMode#LINES} but nothing worked.
     *
     * @param context         The WorldRenderContext which supplies the matrices and tick delta
     * @param points          The points from which to draw lines between
     * @param colorComponents An array of R, G and B color components
     * @param alpha           The alpha of the lines
     * @param lineWidth       The width of the lines
     * @param throughWalls    Whether to render through walls or not
     *
     * @author AzureAaron
     * @author minor edits by Kevinthegreat
     */
    public static void renderLinesFromPoints(WorldRenderContext context, Vec3d[] points, float[] colorComponents, float alpha, float lineWidth, boolean throughWalls) {
        Vec3d camera = context.camera().getPos();
        MatrixStack matrices = context.matrixStack();

        matrices.push();
        matrices.translate(-camera.x, -camera.y, -camera.z);

        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder buffer = tessellator.getBuffer();
        Matrix4f positionMatrix = matrices.peek().getPositionMatrix();
        Matrix3f normalMatrix = matrices.peek().getNormalMatrix();

        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

        RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.lineWidth(lineWidth);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(throughWalls ? GL11.GL_ALWAYS : GL11.GL_LEQUAL);

        buffer.begin(VertexFormat.DrawMode.LINE_STRIP, VertexFormats.LINES);

        for (int i = 0; i < points.length; i++) {
            Vec3d nextPoint = points[i + 1 == points.length ? i - 1 : i + 1];
            Vector3f normalVec = new Vector3f((float) nextPoint.getX(), (float) nextPoint.getY(), (float) nextPoint.getZ()).sub((float) points[i].getX(), (float) points[i].getY(), (float) points[i].getZ()).normalize().mul(normalMatrix);
            buffer
                    .vertex(positionMatrix, (float) points[i].getX(), (float) points[i].getY(), (float) points[i].getZ())
                    .color(colorComponents[0], colorComponents[1], colorComponents[2], alpha)
                    .normal(normalVec.x, normalVec.y, normalVec.z)
                    .next();
        }

        tessellator.draw();

        matrices.pop();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        RenderSystem.lineWidth(1f);
        RenderSystem.enableCull();
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
    }

    public static void renderText(WorldRenderContext context, Text text, Vec3d pos, boolean throughWalls) {
        renderText(context, text, pos, 1, throughWalls);
    }

    public static void renderText(WorldRenderContext context, Text text, Vec3d pos, float scale, boolean throughWalls) {
        renderText(context, text, pos, scale, 0, throughWalls);
    }

    public static void renderText(WorldRenderContext context, Text text, Vec3d pos, float scale, float yOffset, boolean throughWalls) {
        renderText(context, text.asOrderedText(), pos, scale, yOffset, throughWalls);
    }

    /**
     * Renders text in the world space.
     *
     * @param throughWalls whether the text should be able to be seen through walls or not.
     *
     * @author AzureAaron
     * @author minor edits by Kevinthegreat
     */
    public static void renderText(WorldRenderContext context, OrderedText text, Vec3d pos, float scale, float yOffset, boolean throughWalls) {
        MatrixStack matrices = context.matrixStack();
        Vec3d camera = context.camera().getPos();
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        scale *= 0.025f;

        matrices.push();
        matrices.translate(pos.getX() - camera.getX(), pos.getY() - camera.getY(), pos.getZ() - camera.getZ());
        matrices.peek().getPositionMatrix().mul(RenderSystem.getModelViewMatrix());
        matrices.multiply(context.camera().getRotation());
        matrices.scale(-scale, -scale, scale);

        Matrix4f positionMatrix = matrices.peek().getPositionMatrix();
        float xOffset = -textRenderer.getWidth(text) / 2f;

        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder buffer = tessellator.getBuffer();
        VertexConsumerProvider.Immediate consumers = VertexConsumerProvider.immediate(buffer);

        RenderSystem.depthFunc(throughWalls ? GL11.GL_ALWAYS : GL11.GL_LEQUAL);

        textRenderer.draw(text, xOffset, yOffset, 0xFFFFFFFF, false, positionMatrix, consumers, TextRenderer.TextLayerType.SEE_THROUGH, 0, LightmapTextureManager.MAX_LIGHT_COORDINATE);
        consumers.draw();

        RenderSystem.depthFunc(GL11.GL_LEQUAL);
        matrices.pop();
    }
}
