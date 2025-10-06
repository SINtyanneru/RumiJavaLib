package su.rumishistem.rumi_java_lib.JobWorker;

public class JobQueue {
	private String id;
	private String name;
	private ExceptionRunnable task;
	private boolean retry = false;
	private int retry_count = 0;
	private Exception ex;

	public JobQueue(String id, String name, ExceptionRunnable task, boolean retry) {
		this.id = id;
		this.name = name;
		this.task = task;
		this.retry = retry;
	}

	public String get_id() {
		return id;
	}

	public String get_name() {
		return name;
	}

	public void start_task() throws Exception {
		task.run();
	}

	public boolean is_retry() {
		return retry;
	}

	public int get_retry_count() {
		return retry_count;
	}

	public Exception get_exception() {
		return ex;
	}

	public void set_exception(Exception ex) {
		this.ex = ex;
	}
}
