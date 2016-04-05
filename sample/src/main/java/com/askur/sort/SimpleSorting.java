package com.askur.sort;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.askur.util.Constants;

public class SimpleSorting implements ContentHandler {
	private ArrayList<String> recordList;
	private ArrayList<File> fileList;
	private int count=0;
	private int recordNum;
	private int mergeCount;
	private PrintWriter writer;
	private String tempFilename;
	private final static String TEMP = "temp";
	private final static String FILE_EXT = ".txt";
	private File outputFilename;
	private RecordComparator comparator;
	
	public SimpleSorting(File outputFilename,int recordNum,RecordComparator comparator){
		this.outputFilename=outputFilename;
		this.recordNum=recordNum;
		this.comparator=comparator;
	}

	public void onStart() {
		System.out.println("onstart");
		tempFilename = createTempDir(outputFilename) + File.separator + TEMP;
		fileList = new ArrayList<File>();
		recordList = new ArrayList<String>();
	}

	public String onRead(String line) {
		System.out.println("onread : " + line);
		recordList.add(line);
		if ((count % recordNum) == (recordNum - 1)) {
			sort(recordList);
			recordList.clear();
		}
		count++;
		return line;
	}

	public void onEnd() {
		System.out.println("onend");
		merge();
	}

	private void sort(List<String> recordList) {
		System.out.println("sort : " + recordList);
		File file = new File(tempFilename + (count / recordNum) + FILE_EXT);
		try {
			setWriter(new PrintWriter(new BufferedOutputStream(
					new FileOutputStream(file), Constants.WRITE_BUFF_SIZE),
					true));
		} catch (FileNotFoundException e) {
			onExceptionThrown(e);
		}
		mergeCount++;
		fileList.add(file);
		Collections.sort(recordList,comparator);
		for (int i = 0; i < recordList.size(); i++) {
			getWriter().println(recordList.get(i));
		}
		getWriter().close();
	}

	private String createTempDir(File outputFile) {
		String tempDir = outputFile.getParent() + File.separator + TEMP
				+ outputFile.getName();
		new File(tempDir).mkdir();
		return tempDir;
	}

	private void merge() {
		System.out.println("merge");
		int size = fileList.size();
		if (size > 1) {
			// merge them!
			try {
				File file1, file2, tempOutFile;
				BufferedReader reader1, reader2;
				PrintWriter writer;
				String line1, line2;
				int c;

				if (size % 2 != 0) {
					file1 = fileList.remove(0);
					fileList.add(file1);
					size--;
				}

				for (int i = 0; i < size / 2; i++) {
					file1 = fileList.remove(0);
					file2 = fileList.remove(0);
					reader1 = new BufferedReader(new FileReader(file1),
							Constants.READ_BUFF_SIZE);
					reader2 = new BufferedReader(new FileReader(file2),
							Constants.READ_BUFF_SIZE);
					tempOutFile = new File(tempFilename + (mergeCount++)
							+ FILE_EXT);
					writer = new PrintWriter(new BufferedWriter(new FileWriter(
							tempOutFile), Constants.WRITE_BUFF_SIZE));
					fileList.add(tempOutFile);

					boolean stop = false;
					line1 = reader1.readLine();
					line2 = reader2.readLine();
					while (!stop) {
						c = comparator.compare(line1, line2);
						if (c == 0) {
							writer.println(line2);
							line1 = reader1.readLine();
							line2 = reader2.readLine();
							if (line1 == null) {
								readRest(writer, line2, reader2);
								stop = true;
							} else {
								if (line2 == null) {
									readRest(writer, line1, reader1);
									stop = true;
								}
							}
						} else if (c < 0) {
							writer.println(line1);
							if ((line1 = reader1.readLine()) == null) {
								readRest(writer, line2, reader2);
								stop = true;
							}
						} else {
							writer.println(line2);
							if ((line2 = reader2.readLine()) == null) {
								readRest(writer, line1, reader1);
								stop = true;
							}
						}
					}
					writer.flush();
					writer.close();
					reader1.close();
					reader2.close();
					file1.delete();
					file2.delete();
				}
			} catch (IOException e) {
				onExceptionThrown(e);
			}
			merge();
		} else {
			File fo = fileList.remove(0);
			File tempDir = fo.getParentFile();
			if (!fo.renameTo(outputFilename)) {
				throw new SecurityException("Cannot rename file from "
						+ fo.getAbsolutePath() + " to " + outputFilename);
			} else {
				boolean canDelete = tempDir.delete();
				System.out.println("Deleting " + tempDir.getAbsolutePath()
						+ " : " + canDelete);
			}
		}	
	}

	private void readRest(PrintWriter writer, String line, BufferedReader reader)
			throws IOException {
		if (line != null) {
			writer.println(line);
			while ((line = reader.readLine()) != null) {
				writer.println(line);
			}
		}
	}

	public void onExceptionThrown(IOException exception) {
		System.out.println("onExceptionThrown : " + exception);
	}

	public PrintWriter getWriter() {
		return writer;
	}

	public void setWriter(PrintWriter writer) {
		this.writer = writer;
	}

}
