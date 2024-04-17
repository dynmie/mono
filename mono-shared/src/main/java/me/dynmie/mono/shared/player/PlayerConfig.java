package me.dynmie.mono.shared.player;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.With;

/**
 * @author dynmie
 */
@With
@Value
@AllArgsConstructor
public class PlayerConfig {
    boolean color;
    boolean fullPixel;
    boolean textDithering;
}
