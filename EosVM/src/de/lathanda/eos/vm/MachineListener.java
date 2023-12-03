package de.lathanda.eos.vm;


public interface MachineListener {
	void handleError(Exception e);
	void missingProgram();
	void handleOrThrowException(Exception e);
	void handleException(Exception e);
	void startProgram();
	void stopProgram();
}
