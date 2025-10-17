package su.rumishistem.rumi_java_lib;

import su.rumishistem.rumi_java_lib.MisskeyBot.MisskeyClient;

public class Main {
	public static void main(String[] args) {
		try {
			MisskeyClient mk = new MisskeyClient("eth.rumiserver.com", "DZHlTXcl61B66kidKAWlNMQvmVQ9dYfx");
		} catch (Exception EX) {
			EX.printStackTrace();
		}
	}
}
