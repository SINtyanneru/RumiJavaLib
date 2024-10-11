//標準入力をUTF-8に設定
process.stdin.setEncoding("utf8");

process.stdin.addListener("data", (DATA) => {
	let CMD = DATA.replaceAll("\r", "").replaceAll("\n", "").split(" ");

	if (CMD[0] === "CONNECT") {
		ConnectWebSocket(decodeURIComponent(escape(atob(CMD[1]))));
	}
});

function ConnectWebSocket(URL_) {
	let WS = new WebSocket(URL_);

	function stdinReceive(DATA) {
		let CMD = DATA.replaceAll("\r", "").replaceAll("\n", "").split(" ");

		switch(CMD[0]) {
			case "SEND": {
				WS.send(decodeURIComponent(escape(atob(CMD[1]))));
				break;
			}
		}
	}

	process.stdin.addListener("data", stdinReceive);

	WS.addEventListener("open", (E) => {
		console.log("OPEN");
	});

	WS.addEventListener("message", (E) => {
		console.log(`RECEIVE ${btoa(unescape(encodeURIComponent(E.data)))}`);
	});

	WS.addEventListener("close", (E) => {
		console.log("CLOSE");
	});
}