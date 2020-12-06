package edu.temple.dmhelper;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Character implements Serializable {
    private int initiative;
    private String name;
    private UUID id;

    public Character(String name, int initiative, UUID id){
        this.name = name;
        this.initiative = initiative;
        this.id = id;
    }

    /**
     * Compares the initiative of two characters. Will only return true if the current Character has
     * a higher initiative than the one being passed to it as a parameter.
     *
     * @param character A character whose initiative you want to compare against.
     * @return True if <b>this</b> character has a higher initiative than the given character, false otherwise.
     */
    public boolean goesBefore(Character character){
        return this.initiative > character.initiative;
    }

    /**
     * Get the initiative of the current character.
     * @return An integer representing the current character's initiative.
     */
    public int getInitiative() {
        return initiative;
    }

    /**
     * Get the name for the current character.
     * @return A string with the current character's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the id for the current character.
     * @return A UUID associated with the current character.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Set the initiative for the current character.
     * @param initiative An initiative for this character.
     */
    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }

    /**
     * Set the name for the current character.
     * @param name A name for this character.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the id for the current character.
     * @param id A UUID for this character.
     */
    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    @NonNull
    public String toString() {
        return "Character{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", initiative=" + initiative +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Character character = (Character) o;
        return id.equals(character.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}