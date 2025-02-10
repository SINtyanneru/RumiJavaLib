const WebSocket = require("ws");
const net = require("net");
const encodeBase64 = (str) => Buffer.from(str, "utf-8").toString("base64");
const decodeBase64 = (str) => Buffer.from(str, "base64").toString("utf-8");

StartServer(Number.parseInt(process.argv[2]));

function StartServer(PORT) {
	console.log("Stating WebSocketServer " + (PORT));
	console.log("Connecting Telnet " + (PORT + 1));

	const WSS = new WebSocket.Server({ port: PORT });

	WSS.on("connection", (SOCKET, REQ) => {
		const TelnetClient = net.createConnection({host: "localhost", port:PORT + 1})

		TelnetClient.on("data", (DATA)=>{
			SOCKET.send(decodeBase64(DATA.toString()));
		});

		TelnetClient.on("end", () => {
			SOCKET.close();
		});

		SOCKET.on("message", (message) => {
			//メッセージを受信したことを知らせる
			const MSG = encodeBase64(message);
			TelnetClient.write(MSG + "\r\n");
		});

		SOCKET.on("close", () => {
			//通信が閉じたことを知らせる
			TelnetClient.destroy();
		});
	});
}