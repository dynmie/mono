package me.dynmie.mono.shared.session;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.UUID;

/**
 * @author dynmie
 */
@Value
public class ClientSession {
    @EqualsAndHashCode.Exclude String name;
    UUID uniqueId;
}
