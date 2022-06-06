package com.random.explorerssuite.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/*
 *  Callback for stripping a log.
 * Called before the log is stripped, items are dropped, and items are damaged.
 * Upon return:
 * - SUCCESS cancels further processing and continues with normal stripping behavior.
 * - PASS falls back to further processing and defaults to SUCCESS if no other listeners are available
 * - FAIL cancels further processing and does not strip the log.
 */
public interface StripLogCallback {
    Event<StripLogCallback> EVENT = EventFactory.createArrayBacked(StripLogCallback.class,
            (listeners) -> (player, blockpos, world) -> {
                for (StripLogCallback listener : listeners) {
                    ActionResult result = listener.interact(player, blockpos, world);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult interact(PlayerEntity player, BlockPos pos, World world);
}
