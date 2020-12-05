package edu.temple.dmhelper.utilities;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterMinMax implements InputFilter {
    private final int min;
    private final int max;

    public InputFilterMinMax(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String raw = dest.toString() + source.toString();
        if (raw.equals("-")) return null;
        int input;
        try {
            input = Integer.parseInt(raw);
        } catch (NumberFormatException ignored) {
            return "";
        }
        return isInRange(min, max, input) ? null : "";
    }

    public boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}