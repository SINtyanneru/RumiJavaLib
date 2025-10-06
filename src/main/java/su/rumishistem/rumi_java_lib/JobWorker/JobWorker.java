package su.rumishistem.rumi_java_lib.JobWorker;

import static su.rumishistem.rumi_java_lib.LOG_PRINT.Main.LOG;
import su.rumishistem.rumi_java_lib.FIFO;
import su.rumishistem.rumi_java_lib.LOG_PRINT.LOG_TYPE;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JobWorker {
	private static final int CACHE_JOB = 10;
	private static final int RETRY_LIMIT = 10;

	private int pool_size = 0;									//同時に処理する最大数
	private ExecutorService pool;

	private String worker_name;
	private int working_job_count = 0;

	private FIFO<JobQueue> wait_job = new FIFO<>();				//待機中のジョブ
	private FIFO<JobQueue> end_job = new FIFO<>(CACHE_JOB);		//終了したジョブ
	private FIFO<JobQueue> failed_job = new FIFO<>();			//失敗し、再試行待ちのジョブ
	private FIFO<JobQueue> error_job = new FIFO<>(CACHE_JOB);	//失敗し、再試行しないジョブ

	public JobWorker(String worker_name, int pool_size) {
		this.worker_name = worker_name;
		this.pool_size = pool_size;
		pool = Executors.newFixedThreadPool(pool_size);

		LOG(LOG_TYPE.OK, "Jobworker["+worker_name+"] OK. size:" + pool_size);
	}

	public void regist(String name, boolean retry, ExceptionRunnable task) {
		String id = UUID.randomUUID().toString();
		wait_job.add(new JobQueue(id, name, task, retry));

		pool.submit(new Runnable() {
			@Override
			public void run() {
				JobQueue job = wait_job.get();
				if (job == null) return;
				working_job_count += 1;

				try {
					//ジョブ実行
					job.start_task();

					end_job.add(job);
				} catch (Exception ex) {
					job.set_exception(ex);

					if (job.is_retry()) {
						//再チャレンジ
						failed_job.add(job);
					} else {
						//再チャレンジしない
						error_job.add(job);
					}
				} finally {
					working_job_count -= 1;
				}
			}
		});
	}
}
