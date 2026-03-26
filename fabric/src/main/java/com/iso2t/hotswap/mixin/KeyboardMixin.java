package com.iso2t.hotswap.mixin;

import com.iso2t.hotswap.api.InputEventType;
import com.iso2t.hotswap.callbacks.InputPreCallback;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Injects before Keyboard#onKey(...)
 */
@Mixin(KeyboardHandler.class)
public class KeyboardMixin {

    @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
    private void beforeKey(long handle, int action, KeyEvent event, CallbackInfo ci) {
        if (InputPreCallback.EVENT.invoker().onInput(InputEventType.KEY, event.key(), action, event.modifiers())) {
            ci.cancel();
        }
    }
}
