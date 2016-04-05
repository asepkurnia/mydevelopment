package com.askur.sort;

import java.util.Comparator;

public class RecordComparator implements Comparator<String> {

	public int compare(String obj1, String obj2) {
		if ((obj1 == null) || (obj2 == null)) {
			return obj1 == null ? -1 : 1;
		} else {
			return obj1.compareTo(obj2);
		}
	}

}
