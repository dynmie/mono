package me.dynmie.mono.shared.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dynmie
 */
@Getter
@AllArgsConstructor
public class PlayerConfig {
    private final boolean color;
    private final boolean trueColor;

    public Mutable toMutable() {
        return new Mutable(color, trueColor);
    }

    @Setter
    @Getter
    @AllArgsConstructor
    public static class Mutable {
        private boolean color;
        private boolean trueColor;

        public PlayerConfig toImmutable() {
            return new PlayerConfig(color, trueColor);
        }
    }
}
