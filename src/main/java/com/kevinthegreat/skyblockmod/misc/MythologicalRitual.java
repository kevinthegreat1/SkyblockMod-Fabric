package com.kevinthegreat.skyblockmod.misc;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import com.kevinthegreat.skyblockmod.util.RenderHelper;
import it.unimi.dsi.fastutil.ints.IntIntMutablePair;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.*;

public class MythologicalRitual {
    private static final int PARTICLE_TIMEOUT = 10;
    private static final int GUIDING_DUST_SIZE = 20;
    private final Map<BlockPos, IntIntMutablePair> potentialGriffinBurrows = new HashMap<>();
    private final Set<BlockPos> griffinBurrows = new HashSet<>();
    private final Map<BlockPos, GuidingDust> potentialGuidingDusts = new HashMap<>();
    private final Map<BlockPos, Pair<Vec3d, Vec3d>> guidingDusts = new HashMap<>();

    public void onParticle(ParticleS2CPacket packet) {
        if (SkyblockMod.skyblockMod.options.mythologicalRitual.getValue()) {
            BlockPos pos = BlockPos.ofFloored(packet.getX(), packet.getY(), packet.getZ());
            if (ParticleTypes.CRIT.equals(packet.getParameters().getType()) || ParticleTypes.ENCHANT.equals(packet.getParameters().getType())) {
                IntIntMutablePair particlesAtPos = potentialGriffinBurrows.computeIfAbsent(pos, pos1 -> IntIntMutablePair.of(0, 0));
                if (ParticleTypes.CRIT.equals(packet.getParameters().getType())) {
                    particlesAtPos.left(PARTICLE_TIMEOUT);
                } else if (ParticleTypes.ENCHANT.equals(packet.getParameters().getType())) {
                    particlesAtPos.right(PARTICLE_TIMEOUT);
                }
                if (particlesAtPos.leftInt() > 0 && particlesAtPos.rightInt() > 0) {
                    griffinBurrows.add(pos);
                }
            } else if (ParticleTypes.DUST.equals(packet.getParameters().getType())) {
                SkyblockMod.LOGGER.info("Received dust at: " + pos + " with offset: " + packet.getOffsetX() + ", " + packet.getOffsetY() + ", " + packet.getOffsetZ()); //FIXME remove
                GuidingDust guidingDust = potentialGuidingDusts.computeIfAbsent(pos, pos1 -> new GuidingDust());
                guidingDust.addDust(packet.getX(), packet.getY(), packet.getZ());
                guidingDust.resetTimeout();
                if (guidingDust.hasEnoughDust()) {
                    guidingDust.calculateRenderingPlane();
                    SkyblockMod.LOGGER.info("Adding confirmed guiding dusts at: " + pos + " with rendering plane pos 1: " + guidingDust.getRenderingPlane().getLeft() + " and pos 2: " + guidingDust.getRenderingPlane().getRight()); //FIXME remove
                    guidingDusts.put(pos, guidingDust.getRenderingPlane());
                }
            }
        }
    }

    public void tick() {
        if (SkyblockMod.skyblockMod.options.mythologicalRitual.getValue()) {
            for (Iterator<Map.Entry<BlockPos, IntIntMutablePair>> potentialGriffinBurrowIt = potentialGriffinBurrows.entrySet().iterator(); potentialGriffinBurrowIt.hasNext(); ) {
                Map.Entry<BlockPos, IntIntMutablePair> potentialGriffinBurrow = potentialGriffinBurrowIt.next();
                BlockPos pos = potentialGriffinBurrow.getKey();
                IntIntMutablePair particlesAtPos = potentialGriffinBurrow.getValue();
                particlesAtPos.left(Math.max(0, particlesAtPos.leftInt() - 1));
                particlesAtPos.right(Math.max(0, particlesAtPos.rightInt() - 1));
                if (particlesAtPos.leftInt() + particlesAtPos.rightInt() <= 0) {
                    griffinBurrows.remove(pos);
                    potentialGriffinBurrowIt.remove();
                }
            }
            for (Iterator<Map.Entry<BlockPos, GuidingDust>> potentialGuidingDustIt = potentialGuidingDusts.entrySet().iterator(); potentialGuidingDustIt.hasNext(); ) {
                Map.Entry<BlockPos, GuidingDust> potentialGuidingDust = potentialGuidingDustIt.next();
                BlockPos pos = potentialGuidingDust.getKey();
                GuidingDust guidingDust = potentialGuidingDust.getValue();
                guidingDust.tick();
                if (guidingDust.isExpired()) {
                    guidingDusts.remove(pos);
                    potentialGuidingDustIt.remove();
                }
            }
        }
    }

    public void render(WorldRenderContext context) {
        if (SkyblockMod.skyblockMod.options.mythologicalRitual.getValue()) {
            for (BlockPos griffinBorrow : griffinBurrows) {
                RenderHelper.renderFilledThroughWallsWithBeaconBeam(context, griffinBorrow, DyeColor.GREEN.getColorComponents(), 0.5F);
            }
            for (Map.Entry<BlockPos, Pair<Vec3d, Vec3d>> guidingDust : guidingDusts.entrySet()) {
                RenderHelper.renderVerticalPlane(context, guidingDust.getKey(), guidingDust.getValue().getLeft(), guidingDust.getValue().getRight(), DyeColor.GREEN.getColorComponents(), 0.5F);
            }
        }
    }

    private static class GuidingDust {
        private final Set<Vec3d> dusts = new HashSet<>();
        private Vec3d renderPlanePos1;
        private Vec3d renderPlanePos2;
        private int timeout = PARTICLE_TIMEOUT;

        public void addDust(double x, double y, double z) {
            dusts.add(new Vec3d(x, y, z));
        }

        public boolean hasEnoughDust() {
            return dusts.size() >= GUIDING_DUST_SIZE;
        }

        public void tick() {
            timeout = Math.max(0, timeout - 1);
        }

        public void resetTimeout() {
            timeout = PARTICLE_TIMEOUT;
        }

        public boolean isExpired() {
            return timeout <= 0;
        }

        public Pair<Vec3d, Vec3d> getRenderingPlane() {
            if (renderPlanePos1 == null || renderPlanePos2 == null) {
                calculateRenderingPlane();
            }
            return new Pair<>(renderPlanePos1, renderPlanePos2);
        }

        public void calculateRenderingPlane() {
            Vec3d bestFitVerticalPlane = calculateBestFitVerticalPlane();
            renderPlanePos1 = bestFitVerticalPlane.multiply(-1); //FIXME use large number (256?) for multiply
            renderPlanePos2 = bestFitVerticalPlane.multiply(1);
        }

        private Vec3d calculateBestFitVerticalPlane() {
            int points = 0;
            double meanX = 0;
            double meanZ = 0;
            double meanXSquared = 0;
            double meanXZ = 0;
            for (Vec3d dust : dusts) {
                points++;
                meanX += dust.x;
                meanZ += dust.z;
                meanXSquared += dust.x * dust.x;
                meanXZ += dust.x * dust.z;
            }
            meanX /= points;
            meanZ /= points;
            meanXSquared /= points;
            meanXZ /= points;
            double slope = (meanXZ - meanX * meanZ) / (meanXSquared - meanX * meanX);
            return new Vec3d(1, 0, slope).normalize();
        }
    }
}
