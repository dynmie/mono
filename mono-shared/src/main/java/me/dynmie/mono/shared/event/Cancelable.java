package me.dynmie.mono.shared.event;

/**
 * Interface for marking events as cancelable.
 *
 * @see Event
 * @author dynmie
 */
public interface Cancelable {

    void setCanceled(boolean canceled);
    boolean isCanceled();

}
