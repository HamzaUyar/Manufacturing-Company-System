package process;

public interface ProcessState {
    void handleProcess(ManufacturingProcess context);
    boolean isTerminal();
    
    /**
     * Returns a descriptive message for the current state
     */
    String getStateContext();
} 