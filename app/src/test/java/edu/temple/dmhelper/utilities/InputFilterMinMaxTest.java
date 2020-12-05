package edu.temple.dmhelper.utilities;

import android.text.Spanned;
import android.text.SpannedString;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InputFilterMinMaxTest {
    private InputFilterMinMax filter;
    private InputFilterMinMax negativeFilter;

    @Before
    public void setUp() {
        this.filter = new InputFilterMinMax(0, 10);
        this.negativeFilter = new InputFilterMinMax(-10, 10);
    }

    private Spanned makeMock(String text) {
        Spanned spanned = mock(SpannedString.class);
        when(spanned.toString()).thenReturn(text);
        return spanned;
    }

    @Test
    public void isInRange_isCorrect() {
        assertTrue(this.filter.isInRange(0, 10, 5));
        assertFalse(this.filter.isInRange(0, 10, 20));
        assertFalse(this.filter.isInRange(0, 10, -5));
        assertTrue(this.filter.isInRange(10, 0, 5));
        assertFalse(this.filter.isInRange(10, 0, 20));
        assertFalse(this.filter.isInRange(10, 0, -5));
    }

    @Test
    public void filter_allowsForNegatives() {
        assertNull(this.negativeFilter.filter("-", 0, 0, makeMock(""), 0, 0));
        assertNotNull(this.negativeFilter.filter("--", 0, 0, makeMock(""), 0, 0));
        assertNotNull(this.negativeFilter.filter("-", 0, 0, makeMock("-"), 0, 0));
        assertNull(this.negativeFilter.filter("5", 0, 0, makeMock("-"), 0, 0));
    }

    @Test
    public void filter_isCorrect() {
        assertNull(this.filter.filter("5", 0, 0, makeMock(""), 0, 0));
        assertNull(this.filter.filter("1", 0, 0, makeMock("0"), 0, 0));
        assertNotNull(this.filter.filter("2", 0, 0, makeMock("5"), 0, 0));
        assertNotNull(this.filter.filter("A", 0, 0, makeMock(""), 0, 0));
        assertNotNull(this.filter.filter("12F", 0, 0, makeMock(""), 0, 0));
        assertNotNull(this.filter.filter("&", 0, 0, makeMock(""), 0, 0));
    }
}
