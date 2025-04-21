package observer;

import process.ManufacturingProcess;
import process.ProcessState;

public interface ProcessObserver {
    void onStateChange(ManufacturingProcess process, ProcessState oldState, ProcessState newState);
    void onProcessCompleted(ManufacturingProcess process);
} 