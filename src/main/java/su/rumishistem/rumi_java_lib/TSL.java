package su.rumishistem.rumi_java_lib;

import javax.net.ssl.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.util.Base64;

public class TSL {
	private Certificate CF = null;
	private PrivateKey SK = null;
	private SSLContext SSL_CONTEXT = null;
	private String DOMAIN = null;

	public TSL(String FULLCHAIN_PATH, String PRIVKEY_PATH, String DOMAIN) throws CertificateException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
		this.DOMAIN = DOMAIN;

		//-----------------------------------------------------------------FullChain
		//工場を建てる
		CertificateFactory CFF = CertificateFactory.getInstance("X.509");

		//Fullchain.pemを読んでなんかする
		InputStream CF_IS = new FileInputStream(FULLCHAIN_PATH);
		CF = CFF.generateCertificate(CF_IS);
		CF_IS.close();

		//-----------------------------------------------------------------Privkey
		//privkey.pem読み込む
		String privatePem = new String(Files.readAllBytes(Path.of(PRIVKEY_PATH)))
				.replaceAll("\\r\\n", "")
				.replaceAll("\\n", "")
				.replaceAll("-----BEGIN PRIVATE KEY-----", "")
				.replaceAll("-----END PRIVATE KEY-----", "");
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privatePem));
		SK = KeyFactory.getInstance("EC").generatePrivate(keySpec);


		//-----------------------------------------------------------------KeyStore
		//お店を開く
		KeyStore KS = KeyStore.getInstance("JKS");
		KS.load(null, null);
		KS.setKeyEntry("alias", SK, new char[0], new Certificate[]{CF});

		//キーマネージャーを作る
		KeyManagerFactory KMF = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		KMF.init(KS, new char[0]);

		SSL_CONTEXT = SSLContext.getInstance("TLS");
		SSL_CONTEXT.init(KMF.getKeyManagers(), null, null);


	}

	public SSLServerSocket CreateSocket(int PORT) throws IOException {
		SSLServerSocketFactory SSF = SSL_CONTEXT.getServerSocketFactory();
		SSLServerSocket SS = (SSLServerSocket) SSF.createServerSocket(PORT);

		return SS;
	}

	public UpgradeRESULT UpgradeTSL(Socket SOCKET) throws IOException {
		SSLSocketFactory SSF = SSL_CONTEXT.getSocketFactory();
		SSLSocket SS = (SSLSocket) SSF.createSocket(SOCKET, DOMAIN, SOCKET.getPort(), true);
		SS.setEnabledProtocols(new String[]{"TLSv1.2", "TLSv1.3"});

		SS.startHandshake();

		BufferedReader BR = new BufferedReader(new InputStreamReader(SS.getInputStream()));
		BufferedWriter BW = new BufferedWriter(new OutputStreamWriter(SS.getOutputStream()));

		UpgradeRESULT RESULT = new UpgradeRESULT(BR, BW);
		return RESULT;
	}

	public class UpgradeRESULT{
		private BufferedReader BR = null;
		private BufferedWriter BW = null;

		public UpgradeRESULT(BufferedReader BR, BufferedWriter BW){
			this.BR = BR;
			this.BW = BW;
		}

		public BufferedReader getBR(){
			return BR;
		}

		public BufferedWriter getBW(){
			return BW;
		}
	}
}
