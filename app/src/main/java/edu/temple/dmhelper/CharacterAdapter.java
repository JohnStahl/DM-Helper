package edu.temple.dmhelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CharacterAdapter extends BaseAdapter {
    Context context;
    CharacterList characters;

    public CharacterAdapter(Context context, CharacterList characters){
        this.context = context;
        this.characters = characters;
    }

    @Override
    public int getCount() {
        return characters.size();
    }

    @Override
    public Object getItem(int position) {
        return characters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView nameTextView;

        if (!(convertView instanceof LinearLayout)){
            convertView = LayoutInflater.from(context).inflate(R.layout.character_adapter_layout, parent, false);
        }

        nameTextView = convertView.findViewById(R.id.nameTextView);
        nameTextView.setText(((Character) getItem(position)).getName());
        
        return convertView;
    }
}
