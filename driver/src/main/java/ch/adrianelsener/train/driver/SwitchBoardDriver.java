package ch.adrianelsener.train.driver;

import ch.adrianelsener.train.driver.SwitchBoardV1.WeicheMitState;

/**
 * Defines a SwitchBoard which would allow to change states on it.
 */
public interface SwitchBoardDriver {

    public void turn(WeicheMitState statedRelay);

}