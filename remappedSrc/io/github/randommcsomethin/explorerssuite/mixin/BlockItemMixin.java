package io.github.randommcsomethin.explorerssuite.mixin;

import io.github.randommcsomethin.explorerssuite.ExplorersSuite;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
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
