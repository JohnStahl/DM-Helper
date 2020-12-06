package edu.temple.dmhelper;

import java.util.List;

public interface ActionInterface {
    void joinGame();
    void createGame();
    void setDiscoverable(boolean discoverable);
    void startGame(List<Character> characters);
    void endGame();
    void showDiceRoller();
    void showWarhorn();
    void showMainMenu();
    void sendNextRound();
    void addCharacter(Character character);
    void removeCharacter(Character character);
    boolean isDm();
}
