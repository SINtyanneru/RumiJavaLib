const WebSocket = require('ws');

let IDI = 0;

//標準入力をUTF-8に設定
process.stdin.setEncoding("utf8");

process.stdin.addListener("data", (DATA) => {
	let CMD = DATA.replaceAll("\r", "").replaceAll("\n", "").split(" ");

	if (CMD[0] === "START") {
		StartServer(CMD[1]);
	}
});

function StartServer(PORT) {
	const WSS = new WebSocket.Server({ port: PORT });

	console.log(`port [${PORT}] de server start`);

	WSS.on("connection", (SOCKET, REQ) => {
		let ID = IDI;

		console.log(`NEW ${ID} ${REQ.remoteAddress}`);

		//インクリ
		IDI++;

		//標準入力を受信
		function stdinReceive(DATA) {
			let CMD = DATA.replaceAll("\r", "").replaceAll("\n", "").split(" ");

			switch(CMD[0]) {
				case "SEND": {
					if (CMD[1] == ID) {
						SOCKET.send(decodeURIComponent(escape(atob(CMD[2]))));
					}
					break;
				}
			}
		}

		//標準入力のイベントリスナーを登録する
		process.stdin.addListener("data", stdinReceive);

		SOCKET.on("message", (message) => {
			//メッセージを受信したことを知らせる
			console.log(`RECEIVE ${ID} ${btoa(unescape(encodeURIComponent(message)))}`);
		});

		SOCKET.on("close", () => {
			//通信が閉じたことを知らせる
			console.log(`CLOSE ${ID}`);

			//標準入力のイベントリスナーを消す
			process.stdin.removeListener("data", stdinReceive);
		});
	});
}