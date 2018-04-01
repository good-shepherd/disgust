package com.block.disgust.utils;

import java.util.Comparator;

public class ArrayComparator implements Comparator<String[]> {

    @Override
    public int compare(String[] o1, String[] o2) {
        return o2[3].compareToIgnoreCase(o1[3]);
    }
}
