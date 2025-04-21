package process;

public class FailedState implements ProcessState {
    
    @Override
    public void handleProcess(ManufacturingProcess context) {
        // This state is terminal, no further processing needed
    }
    
    @Override
    public boolean isTerminal() {
        return true;
    }
    
    @Override
    public String getStateContext() {
        return "Manufacturing process failed and cannot continue";
    }
} 