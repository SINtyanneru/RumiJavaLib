package su.rumishistem.rumi_java_lib.WebSocket.Server.EVENT;

public class MESSAGE_EVENT {
	private String MESSAGE = null;

	public MESSAGE_EVENT(String MESSAGE){
		this.MESSAGE = MESSAGE;
	}

	public String getMessage(){
		return MESSAGE;
	}
}
