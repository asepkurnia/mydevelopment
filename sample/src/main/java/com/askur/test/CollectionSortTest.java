package com.askur.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.askur.sort.RecordComparator;

public class CollectionSortTest {
public static void main(String[] args) {
	List<String> recordList=new ArrayList<String>();
	recordList.add("b");
	recordList.add("d");
	recordList.add("c");
	recordList.add("a");
	RecordComparator comparator=new RecordComparator();
	Collections.sort(recordList,comparator);
	for(String string:recordList){
System.out.println(string);
	}
}
}
