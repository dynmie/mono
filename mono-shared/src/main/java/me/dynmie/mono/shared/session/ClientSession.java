package me.dynmie.mono.shared.session;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/**
 * @author dynmie
 */
@Getter
@AllArgsConstructor
public class ClientSession {

    private final String name;
    private final UUID uniqueId;

}
