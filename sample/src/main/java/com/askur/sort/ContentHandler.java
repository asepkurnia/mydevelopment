package com.askur.sort;

import java.io.IOException;

public interface ContentHandler {
	public void onStart();

	public String onRead(String line);

	public void onEnd();

	public void onExceptionThrown(IOException exception);
}
