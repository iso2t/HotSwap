package com.iso2t.hotswap.callbacks;

import com.iso2t.hotswap.api.InputEventType;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Fired before any key or mouse‐button event is processed.
 * Return true to cancel the event (prevent vanilla/client handling).
 */
@FunctionalInterface
public interface InputPreCallback {
    /**
     * @param type      KEY or MOUSE
     * @param code      GLFW key code or mouse‐button code
     * @param action    GLFW_PRESS (1) or GLFW_RELEASE (0)
     * @param modifiers bitmask of shift/ctrl/alt/etc.
     * @return true to cancel further processing
     */
    boolean onInput(InputEventType type, int code, int action, int modifiers);

    Event<InputPreCallback> EVENT = EventFactory.createArrayBacked(
            InputPreCallback.class,
            listeners -> (type, code, action, modifiers) -> {
                for (InputPreCallback cb : listeners) {
                    if (cb.onInput(type, code, action, modifiers)) {
                        return true;
                    }
                }
                return false;
            }
    );
}
