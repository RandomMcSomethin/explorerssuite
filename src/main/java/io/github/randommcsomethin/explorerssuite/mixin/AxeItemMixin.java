package io.github.randommcsomethin.explorerssuite.mixin;

import io.github.randommcsomethin.explorerssuite.events.StripLogCallback;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AxeItem.class)
public class AxeItemMixin {
    @Inject(at = @At(value = "HEAD"), method = "useOnBlock", cancellable = true)
    public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        ActionResult result = StripLogCallback.EVENT.invoker().interact(context.getPlayer(), context.getBlockPos(), context.getWorld());

        if (result == ActionResult.FAIL) {
            cir.cancel();
        }
    }
}
