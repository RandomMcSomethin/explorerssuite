package io.github.randommcsomethin.explorerssuite.blocks;

import io.github.randommcsomethin.explorerssuite.ExplorersSuite;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

public class EffectCandleBlockEntity extends BlockEntity {
    public EffectCandleBlockEntity(BlockPos pos, BlockState state) {
        super(ExplorersSuite.EFFECT_CANDLE_ENTITY, pos, state);
    }

    public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState blockState, T t) {
        if (blockState.get(Properties.LIT)) {
            Box box = (new Box(pos)).expand(blockState.get(Properties.CANDLES)*4.0);
            List<PlayerEntity> list = world.getNonSpectatingEntities(PlayerEntity.class, box);
            Iterator var11 = list.iterator();

            PlayerEntity e;
            while (var11.hasNext()) {
                e = (PlayerEntity) var11.next();
                if (blockState.getBlock().equals(ExplorersSuite.INCENSED_CANDLE))
                    e.addStatusEffect(new StatusEffectInstance(ExplorersSuite.CALMING, 3610, 0, true, true));
                else if (blockState.getBlock().equals(ExplorersSuite.INFERNAL_CANDLE))
                    e.addStatusEffect(new StatusEffectInstance(ExplorersSuite.INSOMNIA, 3610, 0, true, true));
            }
        }
    }
}
