package edu.temple.dmhelper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InitiativeTest {

    @Test
    public void testGoesBefore() {
        Initiative A = new Initiative("A", 100);
        Initiative B = new Initiative("B", 1);

        assertTrue(A.goesBefore(B));
    }

    @Test
    public void testGetInitiative() {
        Initiative test = new Initiative("Test", 5);
        assertEquals(5, test.getInitiative());
    }

    @Test
    public void testGetName() {
        Initiative test = new Initiative("Test", 5);
        assertEquals("Test", test.getName());
    }

    @Test
    public void testSetInitiative() {
        Initiative test = new Initiative("Test", 5);
        test.setInitiative(6);

        assertEquals(6, test.getInitiative());
    }

    @Test
    public void testSetName() {
        Initiative test = new Initiative("Test", 5);
        test.setName("Bob");

        assertEquals("Bob", test.getName());
    }
}
