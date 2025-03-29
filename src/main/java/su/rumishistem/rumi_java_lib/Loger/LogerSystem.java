package su.rumishistem.rumi_java_lib.Loger;

import su.rumishistem.rumi_java_lib.GetWorkDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogerSystem {
	private String LogDirPath = GetWorkDir.Get() + "/log/";
	private PrintStream DefaultOutPS = null;
	private PrintStream DefaultErrPS = null;
	private PrintStream OutPS = null;
	private PrintStream ErrPS = null;
	private int LogNumber = 0;
	private int PrintLineCount = 0;
	private FileOutputStream FOS = null;

	public LogerSystem() {
		//標準出力を傍受
		ByteArrayOutputStream OutBAOS = new ByteArrayOutputStream();
		OutPS = new PrintStream(OutBAOS) {
			@Override
			public void print(String Line) {
				try {
					DefaultOutPS.println(Line);
					FOS.write((GenLogPrefix() + Line + "\n").getBytes());
					PrintLineCount++;
				} catch (Exception EX) {
					//エラーをもみ消す
				}
			}
		};

		//標準エラー出力を傍受
		ByteArrayOutputStream ErrBAOS = new ByteArrayOutputStream();
		ErrPS = new PrintStream(ErrBAOS) {
			@Override
			public void print(String Line) {
				try {
					DefaultErrPS.println(Line);
					FOS.write((GenLogPrefix() + Line + "\n").getBytes());
					PrintLineCount++;
				} catch (Exception EX) {
					//エラーをもみ消す
				}
			}
		};

		//入れ替え
		DefaultOutPS = System.out;
		DefaultErrPS = System.err;
		System.setOut(OutPS);
		System.setErr(ErrPS);

		IncrementNumberLatest();

		try {
			//フォルダ作成
			if (!Files.exists(Path.of(LogDirPath))) {
				Files.createDirectory(Path.of(LogDirPath));
			}

			//ファイル作成
			if (!Files.exists(Path.of(LogDirPath + GenFileName()))) {
				Files.createFile(Path.of(LogDirPath + GenFileName()));
			}

			//ファイルオーペン
			FOS = new FileOutputStream(new File(LogDirPath + GenFileName()));
		} catch (Exception EX) {
			EX.printStackTrace();
			System.err.println("Loger system err");
			System.exit(1);
		}
	}

	public void SetLogDirPath(String PATH) {
		LogDirPath = PATH;
	}

	private void LineCountFucker() throws IOException {
		if (PrintLineCount >= 1000) {
			FOS.close();
			LogNumber++;
			Files.createFile(Path.of(LogDirPath + GenFileName()));
			FOS = new FileOutputStream(new File(LogDirPath + GenFileName()));
		}
	}

	private String GenLogPrefix() {
		return "[ " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")) + " ]";
	}

	private String GenFileName() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "_" + LogNumber + ".txt";
	}

	//最後の番号になるまでインクリメントする
	private void IncrementNumberLatest() {
		while (true) {
			if (Files.exists(Path.of(LogDirPath + GenFileName()))) {
				LogNumber++;
			} else {
				break;
			}
		}
	}
}
