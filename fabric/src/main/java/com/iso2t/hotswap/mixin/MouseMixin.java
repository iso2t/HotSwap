package com.iso2t.hotswap.mixin;

import com.iso2t.hotswap.api.InputEventType;
import com.iso2t.hotswap.callbacks.InputPreCallback;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Injects before MouseHandler#onPress(...)
 */
@Mixin(MouseHandler.class)
public class MouseMixin {

    @Inject(method = "onButton", at = @At("HEAD"), cancellable = true)
    private void beforeMouseButton(long handle, MouseButtonInfo rawButtonInfo, int action, CallbackInfo ci) {
        if (InputPreCallback.EVENT.invoker().onInput(InputEventType.MOUSE, rawButtonInfo.button(), action, rawButtonInfo.modifiers())) {
            ci.cancel();
        }
    }
}
