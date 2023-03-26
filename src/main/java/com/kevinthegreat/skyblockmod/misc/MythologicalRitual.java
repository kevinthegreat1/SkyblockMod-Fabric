package com.kevinthegreat.skyblockmod.misc;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import com.kevinthegreat.skyblockmod.util.RenderHelper;
import it.unimi.dsi.fastutil.ints.IntIntMutablePair;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class MythologicalRitual {
    private static final int PARTICLE_TIMEOUT = 10;
    private final Map<BlockPos, IntIntMutablePair> particlesMap = new HashMap<>();
    private final Set<BlockPos> griffinBurrows = new HashSet<>();

    public void onParticle(ParticleS2CPacket packet) {
        if (SkyblockMod.skyblockMod.options.mythologicalRitual.getValue() && ParticleTypes.CRIT.equals(packet.getParameters().getType()) || ParticleTypes.ENCHANT.equals(packet.getParameters().getType())) {
            BlockPos pos = BlockPos.ofFloored(packet.getX(), packet.getY(), packet.getZ());
            IntIntMutablePair particlesAtPos = particlesMap.computeIfAbsent(pos, pos1 -> IntIntMutablePair.of(0, 0));
            if (ParticleTypes.CRIT.equals(packet.getParameters().getType())) {
                particlesAtPos.left(PARTICLE_TIMEOUT);
            } else if (ParticleTypes.ENCHANT.equals(packet.getParameters().getType())) {
                particlesAtPos.right(PARTICLE_TIMEOUT);
            }
            if (particlesAtPos.leftInt() > 0 && particlesAtPos.rightInt() > 0) {
                griffinBurrows.add(pos);
            }
        }
    }

    public void tick() {
        if (SkyblockMod.skyblockMod.options.mythologicalRitual.getValue()) {
            for (Iterator<Map.Entry<BlockPos, IntIntMutablePair>> posParticleIt = particlesMap.entrySet().iterator(); posParticleIt.hasNext(); ) {
                BlockPos pos = posParticleIt.next().getKey();
                IntIntMutablePair particlesAtPos = particlesMap.get(pos);
                particlesAtPos.left(Math.max(0, particlesAtPos.leftInt() - 1));
                particlesAtPos.right(Math.max(0, particlesAtPos.rightInt() - 1));
                if (particlesAtPos.leftInt() + particlesAtPos.rightInt() <= 0) {
                    griffinBurrows.remove(pos);
                    posParticleIt.remove();
                }
            }
        }
    }

    public void render(WorldRenderContext context) {
        if (SkyblockMod.skyblockMod.options.mythologicalRitual.getValue()) {
            for (BlockPos griffinBorrow : griffinBurrows) {
                RenderHelper.renderFilledThroughWallsWithBeaconBeam(context, griffinBorrow, DyeColor.GREEN.getColorComponents(), 0.5F);
            }
        }
    }
}
