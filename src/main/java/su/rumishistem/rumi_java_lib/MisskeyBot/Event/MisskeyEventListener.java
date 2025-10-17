package su.rumishistem.rumi_java_lib.MisskeyBot.Event;

public interface MisskeyEventListener {
	default void Ready() {}
	default void NewNote(NewNoteEvent e) {}
	default void NewFollow(NewFollowEvent e) {}
	default void UnFollow(UnFollowEvent e) {}
	default void NewBlock(NewBlockEvent e) {}
	default void UnBlock(UnBlockEvent e) {}
}
