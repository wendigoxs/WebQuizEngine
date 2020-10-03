package engine;

public class SolveResponse {
    private boolean success;
    private String feedback;

    public SolveResponse(boolean success, String feedback) {
        this.success = success;
        this.feedback = feedback;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getFeedback() {
        return feedback;
    }
}
