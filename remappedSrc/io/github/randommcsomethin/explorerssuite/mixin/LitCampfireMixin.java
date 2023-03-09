package io.github.randommcsomethin.explorerssuite.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;

import static io.github.randommcsomethin.explorerssuite.ExplorersSuite.CONFIG;

@Mixin(CampfireBlockEntity.class)
public class LitCampfireMixin {
    @Inject(at = @At("RETURN"), method = "litServerTick")
    private static void litServerTick(World world, BlockPos pos, BlockState state, CampfireBlockEntity campfire, CallbackInfo ci) {
        Box box = (new Box(pos)).expand(4.0);
        List<LivingEntity> list = world.getNonSpectatingEntities(LivingEntity.class, box);
        Iterator var11 = list.iterator();

        LivingEntity e;
        while(var11.hasNext()) {
            e = (LivingEntity)var11.next();
            if (e instanceof PlayerEntity && CONFIG.campfiresHealPlayers) {
                e.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 1810, 0, true, false));
            }
            if (e instanceof PassiveEntity && CONFIG.campfiresHealPassives) {
                e.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 1810, 0, true, true));
            }
        }
    }
}
