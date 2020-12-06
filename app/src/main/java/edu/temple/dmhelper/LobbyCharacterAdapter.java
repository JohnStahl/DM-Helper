package edu.temple.dmhelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LobbyCharacterAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Character> characters;

    public LobbyCharacterAdapter(Context context) {
        this.context = context;
        this.characters = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return this.characters.size();
    }

    @Override
    public Character getItem(int position) {
        return characters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View root = convertView != null ? convertView :
                LayoutInflater.from(context).inflate(R.layout.character, parent, false);
        Character character = getItem(position);
        ((TextView) root.findViewById(R.id.charactersName)).setText(character.getName());
        ((TextView) root.findViewById(R.id.charactersInitiative)).setText(String.valueOf(character.getInitiative()));
        return root;
    }

    public void addItem(Character character) {
        if (!this.characters.contains(character)) {
            this.characters.add(character);
            this.notifyDataSetChanged();
        }
    }

    public void removeItem(Character character) {
        this.characters.remove(character);
        this.notifyDataSetChanged();
    }

    public void addItems(ArrayList<Character> characters) {
        for (Character character : characters) addItem(character);
        this.notifyDataSetChanged();
    }

    public ArrayList<Character> getAll() {
        return characters;
    }

    public void clear() {
        this.characters.clear();
        this.notifyDataSetInvalidated();
    }
}
