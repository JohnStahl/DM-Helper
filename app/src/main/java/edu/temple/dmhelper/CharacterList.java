package edu.temple.dmhelper;
import java.util.ArrayList;

public class CharacterList extends ArrayList<Character> {
    private int head;

    public CharacterList(){
        super();
        this.head = 0;
    }

    /**
     * Adds a character to the list based off of their initiative score
     * while maintaining proper turn order.
     */
    @Override
    public boolean add(Character character) {
        if (this.size() == 0){
            super.add(character);
            return true;
        }

        /*If replacing the head*/
        if(character.goesBefore(this.get(head))){
            this.add(head, character);
            return true;
        }

        /*After the head in list*/
        for (int i = head + 1; i < this.size(); i++){
            if(character.goesBefore(this.get(i))){
                this.add(i, character);
                return true;
            }
        }

        /*Before the head in list*/
        for (int i = 0; i < head; i++){
            if(character.goesBefore(this.get(i))){
                this.add(i, character);
                head++; //update the position of the head
                return true;
            }
        }

        /*Character is last in the list*/
        if (head == 0){
            super.add(character);
        } else{
            this.add(head, character);
            head++;
        }
        return true;
    }

    @Override
    public Character remove(int index) {
        if (index < 0 || index >= this.size()) return null;

        /*Head is being removed and is last in the list*/
        if (index == head && head == this.size() - 1){
            head = 0;
        }else if (index < head){ //Character being removed comes before the head
            head--;
        }

        return super.remove(index);
    }

    /**
     * Moves the first character in the list to the end of the list.
     */
    public void nextTurn(){
        if (this.size() == 0) return;
        this.add(this.size(), this.get(0));
        this.remove(0);
        if (head == 0){
            head = this.size()-1;
        } else {
            head--;
        }
    }

    @Override
    public String toString() {
        String list = "";
        for(Character c : this){
            list = list + c.getInitiative() + " " + c.getName() + "\n";
        }

        return list;
    }

}
