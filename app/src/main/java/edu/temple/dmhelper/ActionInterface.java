package edu.temple.dmhelper;

import java.util.List;

public interface ActionInterface {
    void joinGame();
    void createGame();
    void startGame(List<Character> characters);
    void showDiceRoller();
    void showWarhorn();
    void showMainMenu();
}
