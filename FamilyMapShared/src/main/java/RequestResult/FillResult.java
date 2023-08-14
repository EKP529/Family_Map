package RequestResult;

/**
 * The class to hold the response from the Family Map Server
 * to the Family Map Client from the fill web API.
 */
public class FillResult extends Result
{
    /**
     * Constructs the result of the Fill API from the Family Map Server
     * @param message the message for the result
     * @param status the status report for the result
     */
    public FillResult(String message, boolean status) { super(message, status); }
}
