package su.rumishistem.rumi_java_lib;

import su.rumishistem.rumi_java_lib.RSCP.FileUploader;
import su.rumishistem.rumi_java_lib.RSCP.RSCP;

import java.io.File;

public class Main {
	public static void main(String[] args) {
		try {
			//new RSCP("192.168.0.120").remove("test", "test/file.txt");
			FileUploader fu = new RSCP("192.168.0.120").upload("test", "test/file.txt", true);
			fu.file(new File("/home/rumisan/Downloads/G17GfbCaYAEYSL4.png"));
			//fu.close();
		} catch (Exception EX) {
			EX.printStackTrace();
		}
	}
}
