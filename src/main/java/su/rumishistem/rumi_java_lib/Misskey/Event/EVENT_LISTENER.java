package su.rumishistem.rumi_java_lib.Misskey.Event;

import java.util.EventListener;

public interface EVENT_LISTENER extends EventListener {
	public void onReady();
	public void onNewNote(NewNoteEvent E);
	public void onNewFollower(NewFollower E);
}
