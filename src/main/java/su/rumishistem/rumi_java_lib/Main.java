package su.rumishistem.rumi_java_lib;

import su.rumishistem.rumi_java_lib.RSCP.FileUploader;
import su.rumishistem.rumi_java_lib.RSCP.RSCP;

import java.io.File;

public class Main {
	public static void main(String[] args) {
		try {
			FIFO<String> fifo = new FIFO<>();
			fifo.add("あ");
			fifo.add("い");
			fifo.add("う");
			fifo.add("お");
			fifo.add("え");

			System.out.println(fifo.get());
			System.out.println(fifo.get());
			System.out.println(fifo.get());
			System.out.println(fifo.get());
			System.out.println(fifo.get());
		} catch (Exception EX) {
			EX.printStackTrace();
		}
	}
}
