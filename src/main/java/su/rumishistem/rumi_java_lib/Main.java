package su.rumishistem.rumi_java_lib;

import su.rumishistem.rumi_java_lib.MisskeyBot.Builder.NoteBuilder;
import su.rumishistem.rumi_java_lib.MisskeyBot.Event.MisskeyEventListener;
import su.rumishistem.rumi_java_lib.MisskeyBot.Event.NewFollowEvent;
import su.rumishistem.rumi_java_lib.MisskeyBot.Event.NewNoteEvent;
import su.rumishistem.rumi_java_lib.MisskeyBot.Event.UnFollowEvent;
import su.rumishistem.rumi_java_lib.MisskeyBot.MisskeyClient;

import java.io.File;
import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		try {
			MisskeyClient mk = new MisskeyClient("eth.rumiserver.com", "i76C85ODE7hlmFaRZWztl4g3gDt5j3pc");

			mk.add_event_listener(new MisskeyEventListener() {
				@Override
				public void Ready() {
					System.out.println("ログインしたユーザー：" + mk.get_self().get_user().get_name());
				}

				@Override
				public void NewNote(NewNoteEvent e) {
					try {
						if (e.get_note().get_text() == null) return;
						System.out.println("新しいノート：" + e.get_note().get_text());

						if (e.get_note().is_mention()) {
							mk.create_reaction(e.get_note(), "1039992459209490513");

							NoteBuilder nb = new NoteBuilder();
							nb.set_text("なんですか？");
							nb.set_reply(e.get_note());
							mk.create_note(nb);
						}

						if (e.get_note().get_text().equals("r.test")) {
							NoteBuilder nb = new NoteBuilder();
							nb.set_text("テスト中");
							nb.set_reply(e.get_note());
							nb.add_file(new File("/home/rumisan/Downloads/A.png"));
							mk.create_note(nb);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

				@Override
				public void NewFollow(NewFollowEvent e) {
					System.out.println(e.get_user().get_name() + "にフォローされた");
				}

				@Override
				public void UnFollow(UnFollowEvent e) {
					System.out.println(e.get_user().get_name() + "にフォロー解除された");
				}
			});
		} catch (Exception EX) {
			EX.printStackTrace();
		}
	}
}
