package su.rumishistem.rumi_java_lib;

import su.rumishistem.rumi_java_lib.JobWorker.JobWorker;

public class Main {
	public static void main(String[] args) {
		try {
			JobWorker worker = new JobWorker("test", 2);

			worker.regist("1秒", false, new ExceptionRunnable() {
				@Override
				public void run() throws Exception {
					System.out.println("1秒開始");
					Thread.sleep(1000);
					System.out.println("1秒終了");
				}
			});
			worker.regist("2秒", false, new ExceptionRunnable() {
				@Override
				public void run() throws Exception {
					System.out.println("2秒開始");
					Thread.sleep(2000);
					System.out.println("2秒終了");
				}
			});
			worker.regist("3秒", false, new ExceptionRunnable() {
				@Override
				public void run() throws Exception {
					System.out.println("3秒開始");
					Thread.sleep(3000);
					System.out.println("3秒終了");
				}
			});
		} catch (Exception EX) {
			EX.printStackTrace();
		}
	}
}
