package process;

public class FailedState implements ProcessState {
    
    @Override
    public void handleProcess(ManufacturingProcess context) {
        // No action needed in failed state
    }
    
    @Override
    public boolean isTerminal() {
        return true;
    }
} 