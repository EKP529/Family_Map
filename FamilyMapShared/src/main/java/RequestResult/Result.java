package RequestResult;

public class Result
{
    /**
     * the possible error message for the result of the Register API from the Family Map Server
     */
    private String message;
    /**
     * the success status for the result of the Register API from the Family Map Server
     */
    boolean success;
    public Result() {}
    /**
     * Constructs the error result of the Register API from the Family Map Server
     * @param message the message for the result
     * @param status the status report for the result
     */
    public Result(String message, boolean status)
    {
        this.message = message;
        this.success = status;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
