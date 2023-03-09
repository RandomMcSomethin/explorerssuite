package io.github.randommcsomethin.explorerssuite.mixin;

import io.github.randommcsomethin.explorerssuite.ExplorersSuite;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class HUDMixin {
    @Inject(method = "render", at = @At("TAIL"))
    public void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        int     offset = 0,
                dayColor = ExplorersSuite.CONFIG.dayColor,
                nightColor = ExplorersSuite.CONFIG.nightColor;
        ItemStack mainHand = player.getMainHandStack();
        ItemStack offHand = player.getMainHandStack();

        // if player has coord displaying item
        if (mainHand.isIn(ExplorersSuite.DISPLAYS_COORDS) ||
                offHand.isIn(ExplorersSuite.DISPLAYS_COORDS)) {
            // display coords on HUD
            matrices.push();
            // direction in fancy letter form
            String pos = player.getHorizontalFacing().getName().toUpperCase().charAt(0) + " | ";
            // player coords
            pos += String.format("%d %d %d",
                    player.getBlockPos().getX(),
                    player.getBlockPos().getY(),
                    player.getBlockPos().getZ());
            float i = client.textRenderer.getWidth(pos);
            float j = (client.getWindow().getScaledWidth() - i) / 2;
            float k = client.getWindow().getScaledHeight() - 60 - offset;
            offset += 10;

            client.textRenderer.drawWithShadow(matrices, pos, j, k, 0xFFFFFF);
            matrices.pop();
        }

        // if player has time displaying item
        if (mainHand.isIn(ExplorersSuite.DISPLAYS_TIME) ||
                offHand.isIn(ExplorersSuite.DISPLAYS_TIME)) {
            // display time on HUD
            matrices.push();
            // time of day
            double time = (player.getWorld().getTimeOfDay() + 6000)/10.0 % 2400;
            String timeStr = "";
            if (ExplorersSuite.CONFIG.twelveHourFormat) {
                String amOrPM = (time >= 1200 ? " PM" : " AM");
                double dispTime = time % 1200;
                int hours = (int) Math.floor(dispTime / 100.0);
                int minutes = (int) Math.floor((dispTime % 100) * (0.6));
                // 0:00 should be 12:00
                timeStr =
                        hours + (dispTime < 100 ? 12 : 0) + ":" +
                                (minutes > 9 ? "" : "0") +
                                minutes +
                                amOrPM;
            } else {
                double dispTime = time % 2400;
                int hours = (int) Math.floor(dispTime / 100.0);
                int minutes = (int) Math.floor((dispTime % 100) * (0.6));
                timeStr =
                        hours + ":" +
                        (minutes > 9 ? "" : "0") +
                        minutes;
            }
            float i = client.textRenderer.getWidth(timeStr);
            float j = (client.getWindow().getScaledWidth() - i) / 2;
            float k = client.getWindow().getScaledHeight() - 60 - offset;

            client.textRenderer.drawWithShadow(matrices, timeStr, j, k,
                    (time >= 1900 || time < 600) ? nightColor : dayColor);
            matrices.pop();
        }
    }
}
