package com.rumisystem.rumi_java_lib.LOG_PRINT;

public class Main {
	public static void LOG(LOG_TYPE LEVEL, String TEXT){
		switch (LEVEL){
			case LOG_TYPE.OK:{
				System.out.println("[  \u001B[32mOK\u001B[0m  ]" + TEXT);
				break;
			}

			case LOG_TYPE.FAILED:{
				System.out.println("[\u001B[31mFAILED\u001B[0m]" + TEXT);
				break;
			}

			case LOG_TYPE.INFO:{
				System.out.println("[ INFO ]" + TEXT);
				break;
			}

			case LOG_TYPE.PROCESS:{
				System.out.println("[ **** ]" + TEXT);
				break;
			}

			case LOG_TYPE.PROCESS_END_OK:{
				System.out.println("\u001B[1F[  \u001B[32mOK\u001B[0m  ]");
				break;
			}

			case LOG_TYPE.PROCESS_END_FAILED:{
				System.out.println("\u001B[1F[\u001B[31mFAILED\u001B[0m]");
				break;
			}
		}
	}
}
