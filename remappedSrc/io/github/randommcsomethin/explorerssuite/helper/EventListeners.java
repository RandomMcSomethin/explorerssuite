package io.github.randommcsomethin.explorerssuite.helper;

import io.github.randommcsomethin.explorerssuite.ExplorersSuite;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.BinomialLootNumberProvider;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.event.GameEvent;

import static io.github.randommcsomethin.explorerssuite.ExplorersSuite.CONFIG;

public class EventListeners {
    public static final Identifier PIG_LOOT_TABLE = new Identifier("minecraft", "entities/pig");

    //More special methods
    public static void injectLoot() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, supplier, setter) -> {
            if (PIG_LOOT_TABLE.equals(id) && setter.isBuiltin()) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .apply(SetCountLootFunction.builder(BinomialLootNumberProvider.create(3, 0.75f)).build())
                        .with(ItemEntry.builder(ExplorersSuite.TALLOW).build());

                supplier.pool(poolBuilder);
            }
        });
    }

    public static void injectLadderBehavior() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            // check if the player is holding a ladder or is sneaking
            if (!CONFIG.dropLadders || player.isSpectator() || player.isSneaking()) {
                return ActionResult.PASS;
            }
            ItemStack s = player.getStackInHand(hand);
            if (!s.isIn(ExplorersSuite.LADDERS)) {
                return ActionResult.PASS;
            }

            // obtain ladder variables
            BlockItem ladderItem = ((BlockItem) s.getItem());
            BlockState targetLadderState = world.getBlockState(hitResult.getBlockPos());

            // check if player is actually looking at a ladder
            if (targetLadderState.getBlock() != ladderItem.getBlock()) {
                return ActionResult.PASS;
            }

            // set ladder placement position
            BlockPos lowestLadderPos = hitResult.getBlockPos().down();
            for (int i = 0; world.getBlockState(lowestLadderPos).getBlock() == ladderItem.getBlock()
                            && player.canPlaceOn(lowestLadderPos, hitResult.getSide(), s); i++) {
                lowestLadderPos = lowestLadderPos.down();
            }

            // double check to make ABSOLUTELY SURE that a player can place a ladder there
            if (!player.canPlaceOn(lowestLadderPos, hitResult.getSide(), s) ||
                !targetLadderState.canPlaceAt(world, lowestLadderPos) ||
                !world.getBlockState(lowestLadderPos).getMaterial().isReplaceable()) {
                return ActionResult.PASS;
            }

            // do the placing
            BlockState newLadderState = ladderItem.getBlock().getPlacementState(new ItemPlacementContext(player, hand, s, hitResult.withBlockPos(lowestLadderPos)));
            world.setBlockState(lowestLadderPos, newLadderState);
            player.swingHand(hand, false);

            BlockSoundGroup blockSoundGroup = newLadderState.getSoundGroup();
            world.playSound(player, hitResult.getBlockPos(), blockSoundGroup.getPlaceSound(), SoundCategory.BLOCKS, (blockSoundGroup.getVolume() + 1.0F) / 2.0F, blockSoundGroup.getPitch() * 0.8F);
            world.emitGameEvent(player, GameEvent.BLOCK_PLACE, hitResult.getBlockPos());
            if (!player.getAbilities().creativeMode) {
                s.decrement(1);
            }
            return ActionResult.CONSUME;
        });
    }

    /*
    public static void injectBerriesBehavior() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (player.getStackInHand(hand).isIn(ExplorersSuite.CANCEL_PLACEMENT)) {
                return ActionResult.CONSUME;
            }
            return ActionResult.PASS;
        });
    }

    public static void injectBarkBehavior() {
        StripLogCallback.EVENT.register((player, pos, world) -> {
            if (player.getActiveHand() != Hand.OFF_HAND) {
                return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        });
    }
    */
}
