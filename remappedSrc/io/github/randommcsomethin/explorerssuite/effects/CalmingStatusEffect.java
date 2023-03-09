package io.github.randommcsomethin.explorerssuite.effects;

import io.github.randommcsomethin.explorerssuite.ExplorersSuite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;

public class CalmingStatusEffect extends StatusEffect {

    public CalmingStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0x44AAFF);
    }

    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof ServerPlayerEntity) {
            int timeSinceRest = ((ServerPlayerEntity)entity).getStatHandler().getStat(
                    Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST)
            );
            if (!entity.hasStatusEffect(ExplorersSuite.INSOMNIA) && timeSinceRest > 72000) {
                ((ServerPlayerEntity) entity).getStatHandler().setStat(
                        (PlayerEntity) entity,
                        Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST),
                        72000);
            }
            ((ServerPlayerEntity) entity).getStatHandler().setStat(
                    (PlayerEntity) entity,
                    Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST),
                    timeSinceRest - 2*amplifier);
        }
    }
}
