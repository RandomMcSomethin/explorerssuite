package io.github.randommcsomethin.explorerssuite.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.BooleanProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.randommcsomethin.explorerssuite.ExplorersSuite.CONFIG;

@Mixin(CampfireBlock.class)
public class CampfireMixin {
    @Shadow @Final public static BooleanProperty LIT;

    @Inject(at = @At("RETURN"), method = "getPlacementState", cancellable = true)
    public void getPlacementState(ItemPlacementContext ctx, CallbackInfoReturnable cir) {
        BlockState fireState = (BlockState)cir.getReturnValue();
        cir.setReturnValue(fireState.with(LIT, !CONFIG.campfiresPlaceUnlit));
    }
}
