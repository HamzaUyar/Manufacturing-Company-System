package process;

public class CompletedState implements ProcessState {
    
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
        return "Product successfully manufactured and completed";
    }
} 