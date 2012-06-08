package fr.vitec.batch.process.logger;

public class BatchBasicLogger implements IBatchLogger{

	@Override
	public void logTitle(String msg) {
		System.out.println(msg);				
	}

	@Override
	public void logInfo(String msg) {
		System.out.println(msg);
	}


}
