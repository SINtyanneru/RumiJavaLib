package su.rumishistem.rumi_java_lib.RESOURCE;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RESOURCE_MANAGER {
	private boolean ECLIPSE = false;
	private Class ResourceClass;

	public  RESOURCE_MANAGER(Class ResourceClass) {
		this.ResourceClass = ResourceClass;
	}

	/**
	 * エクリプスのエミュレーター上で動作させていることを設定する
	 */
	public void setECLIPSE_MODE() {
		ECLIPSE = true;
	}

	/**
	 * リソース内のファイルを一覧で取得します
	 * @param PATH 取得するリソースのフォルダ名、/ならルート
	 * @return RESOURCE_ENTRIEのListで返答します。
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public List<RESOURCE_ENTRIE> getResourceFileList(String PATH) throws IOException, URISyntaxException {
		List<RESOURCE_ENTRIE> LIST = new ArrayList<>();

		if (!ECLIPSE) {
			JarURLConnection JUC = (JarURLConnection) ResourceClass.getResource(PATH).openConnection();
			JarFile JAR = JUC.getJarFile();
			Enumeration<JarEntry> ENTRIE = JAR.entries();

			while (ENTRIE.hasMoreElements()) {
				JarEntry ROW = ENTRIE.nextElement();
				String ENTRIE_NAME = "/" + ROW.getName();

				if ((ENTRIE_NAME).startsWith(PATH)) {
					RESOURCE_ENTRIE.TYPE TYPE = RESOURCE_ENTRIE.TYPE.FILE;
					//ディレクトリならDIR
					if (ROW.isDirectory()) {
						TYPE = RESOURCE_ENTRIE.TYPE.DIR;
					}

					//追加
					LIST.add(new RESOURCE_ENTRIE(
						ENTRIE_NAME,
						ENTRIE_NAME.split("/")[ENTRIE_NAME.split("/").length-1].split("\\.")[0],
						TYPE
					));
				}
			}
		} else {//Eclipseのエミュでしか動かなかったコード
			Path DIR_PATH = Paths.get(ResourceClass.getResource(PATH).toURI());
			Stream<Path> FL_STREAM = Files.list(DIR_PATH);

			for (Path FILE:FL_STREAM.collect(Collectors.toList())) {
				String ENTRIE_NAME = PATH + FILE.getFileName();
				String NAME = FILE.getFileName().toString().split("\\.")[0];

				RESOURCE_ENTRIE.TYPE TYPE = RESOURCE_ENTRIE.TYPE.FILE;
				//ディレクトリならDIR
				if (FILE.toFile().isDirectory()) {
					TYPE = RESOURCE_ENTRIE.TYPE.DIR;
				}

				//追加
				LIST.add(new RESOURCE_ENTRIE(
					ENTRIE_NAME,
					NAME,
					TYPE
				));
			}
		}

		return LIST;
	}

	/**
	 * リソースのデータをbyte[]で取得します。
	 * @param PATH 取得するリソースのパスをしていします
	 * @return
	 * @throws IOException
	 */
	public byte[] getResourceData(String PATH) throws IOException {
		InputStream IS = ResourceClass.getResourceAsStream(PATH);
		ByteArrayOutputStream BAOS = new ByteArrayOutputStream();

		//Nullチェック
		if (IS == null) {
			throw new IOException("リソースが見つかりません：" + PATH);
		}

		byte[] BUFFER = new byte[1024];
		int BYTE_READ;
		while ((BYTE_READ = IS.read(BUFFER)) != -1) {
			BAOS.write(BUFFER, 0, BYTE_READ);
		}

		return BAOS.toByteArray();
	}

	/**
	 * リソースが存在するかをチェックします
	 * @param PATH
	 * @return
	 */
	public boolean Exists(String PATH) {
		if (ResourceClass.getResourceAsStream(PATH) != null) {
			return true;
		} else {
			return false;
		}
	}
}
