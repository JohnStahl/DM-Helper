package edu.temple.dmhelper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CharacterTest {

    @Test
    public void testGoesBefore() {
        Character A = new Character("A", 100);
        Character B = new Character("B", 1);

        assertTrue(A.goesBefore(B));
    }

    @Test
    public void testGetInitiative() {
        Character test = new Character("Test", 5);
        assertEquals(5, test.getInitiative());
    }

    @Test
    public void testGetName() {
        Character test = new Character("Test", 5);
        assertEquals("Test", test.getName());
    }

    @Test
    public void testSetInitiative() {
        Character test = new Character("Test", 5);
        test.setInitiative(6);

        assertEquals(6, test.getInitiative());
    }

    @Test
    public void testSetName() {
        Character test = new Character("Test", 5);
        test.setName("Bob");

        assertEquals("Bob", test.getName());
    }
}
