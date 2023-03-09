package com.random.explorerssuite.mixin;

import com.random.explorerssuite.ExplorersSuite;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.world.spawner.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {
    @Inject(at = @At("HEAD"), method = "place", cancellable = true)
    public void place(ItemPlacementContext context, CallbackInfoReturnable cir) {
        ItemStack stackOfThisItem = context.getStack();
        if (stackOfThisItem.isIn(ExplorersSuite.CANCEL_PLACEMENT))
            cir.setReturnValue(ActionResult.FAIL);
    }
}
