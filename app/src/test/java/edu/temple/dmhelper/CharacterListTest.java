package edu.temple.dmhelper;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CharacterListTest {

    @Test
    public void addTest(){
        CharacterList test = new CharacterList();
        test.add(new Initiative("Test", 5));

        assertEquals(1, test.size());
    }
    @Test
    public void addHigherInitiativeTest(){
        CharacterList test = new CharacterList();
        test.add(new Initiative("Last", 1));
        test.add(new Initiative("First", 100));

        assertEquals("First", test.get(0).getName());

    }

    @Test
    public void addLowerInitiativeTest(){
        CharacterList test = new CharacterList();
        test.add(new Initiative("Last", 1));
        test.add(new Initiative("First", 100));
        test.add(new Initiative("Middle", 50));

        assertEquals("Middle", test.get(1).getName());
    }

    @Test
    public void removeTest(){
        CharacterList test = new CharacterList();
        test.add(new Initiative("Test", 5));
        test.remove(0);

        assertEquals(0, test.size());
    }

    @Test
    public void nextTurnTest(){
        CharacterList test = new CharacterList();
        test.add(new Initiative("Last", 1));
        test.add(new Initiative("First", 100));
        test.nextTurn();

        assertEquals("Last", test.get(0).getName());
    }
}
