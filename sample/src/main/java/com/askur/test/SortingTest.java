package com.askur.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.askur.sort.ContentHandler;
import com.askur.sort.RecordComparator;
import com.askur.sort.RecordReader;
import com.askur.sort.SimpleSorting;

public class SortingTest {
	public static void main(String[] args) {
		List<String> listFile = new ArrayList<String>();
		listFile.add("test.txt");
		File file=new File("hasiltest.txt");
		RecordComparator comparator=new RecordComparator();
		RecordReader recordReader = new RecordReader(listFile);
		ContentHandler test = new SimpleSorting(file,2,comparator);
		recordReader.addHandler(test);
		recordReader.parse();
	}
}
