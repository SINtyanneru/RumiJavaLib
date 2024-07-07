package com.rumisystem.rumi_java_lib.HTTP_SERVER;

import java.util.EventListener;

public interface HTTP_EVENT_LISTENER extends EventListener {
	public void REQUEST_EVENT(HTTP_EVENT E);
}
