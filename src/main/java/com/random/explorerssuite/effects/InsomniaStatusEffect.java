package com.random.explorerssuite.effects;

import com.random.explorerssuite.ExplorersSuite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;

public class InsomniaStatusEffect extends StatusEffect {
    public InsomniaStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 0xAA99FF);
    }

    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof ServerPlayerEntity) {
            int timeSinceRest = ((ServerPlayerEntity)entity).getStatHandler().getStat(
                    Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST)
            );
            if (!entity.hasStatusEffect(ExplorersSuite.CALMING) && timeSinceRest < 72000) {
                ((ServerPlayerEntity) entity).getStatHandler().setStat(
                        (PlayerEntity) entity,
                        Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST),
                        72000);
            }
            ((ServerPlayerEntity) entity).getStatHandler().setStat(
                    (PlayerEntity) entity,
                    Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST),
                    timeSinceRest + 2*amplifier - 1);
        }
    }
}
