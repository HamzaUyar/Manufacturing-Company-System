package process;

public interface ProcessState {
    void handleProcess(ManufacturingProcess context);
    boolean isTerminal();
} 