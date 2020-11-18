package edu.temple.dmhelper.bluetooth.message;

import java.util.ArrayList;

import edu.temple.dmhelper.Character;

public class PlayerListMessage extends BluetoothMessage {
    public static final String TYPE = "player_left";

    private final ArrayList<Character> players;

    public PlayerListMessage(ArrayList<Character> players) {
        super(TYPE);
        this.players = players;
    }

    public ArrayList<Character> getPlayers() {
        return players;
    }
}
