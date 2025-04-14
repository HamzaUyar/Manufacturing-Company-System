package process;

public class CompletedState implements ProcessState {
    
    @Override
    public void handleProcess(ManufacturingProcess context) {
        // No action needed in completed state
    }
    
    @Override
    public boolean isTerminal() {
        return true;
    }
} 