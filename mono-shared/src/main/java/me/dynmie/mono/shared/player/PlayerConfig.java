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
    private final boolean fullPixel;
    private final boolean textDithering;

    public Mutable toMutable() {
        return new Mutable(color, fullPixel, textDithering);
    }

    @Setter
    @Getter
    @AllArgsConstructor
    public static class Mutable {
        private boolean color;
        private boolean fullPixel;
        private boolean textDithering;

        public PlayerConfig toImmutable() {
            return new PlayerConfig(color, fullPixel, textDithering);
        }
    }
}
