package com.askur.sort;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.askur.util.Constants;

public class RecordReader {
	private List<String> fileList;
	private List<ContentHandler> handlerList;

	public RecordReader(List<String> fileList) {
		this.fileList = fileList;
		this.handlerList = new ArrayList<ContentHandler>();
	}

	public void addHandler(ContentHandler contentHandler) {
		handlerList.add(contentHandler);
	}

	public void parse() {
		callOnStart();
		BufferedReader br;
		String line = null;

		try {
			for (Iterator<String> iter = fileList.iterator(); iter.hasNext();) {
				br = new BufferedReader(new FileReader(iter.next()),
						Constants.READ_BUFF_SIZE);
				while ((line = br.readLine()) != null) {
					callOnRead(line);
				}
				br.close();
			}
		} catch (IOException e) {
			callOnExceptionThrown(e);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.toString());
			System.err.println("line: " + line);
		}
		callOnEnd();
	}

	private void callOnStart() {
		for (int i = handlerList.size() - 1; i >= 0; i--) {
			handlerList.get(i).onStart();
		}
	}

	private void callOnRead(String line) {
		for (int i = handlerList.size() - 1; i >= 0; i--) {
			handlerList.get(i).onRead(line);
		}
	}

	private void callOnEnd() {
		for (int i = handlerList.size() - 1; i >= 0; i--) {
			handlerList.get(i).onEnd();
		}
	}

	private void callOnExceptionThrown(IOException exception) {
		for (int i = handlerList.size() - 1; i >= 0; i--) {
			handlerList.get(i).onExceptionThrown(exception);
		}
	}

}
