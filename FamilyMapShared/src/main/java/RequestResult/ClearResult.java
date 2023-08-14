package RequestResult;

/**
 * The class to hold the response from the Family Map Server
 * to the Family Map Client from the clear web API.
 */
public class ClearResult extends Result
{
    /**
     * Constructs the result of the Clear API from the Family Map Server
     * @param message the message for the result
     * @param status the status report for the result
     */
    public ClearResult(String message, boolean status) { super(message, status); }
}
