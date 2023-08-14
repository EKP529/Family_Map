package RequestResult;

/**
 * The class to hold the response from the Family Map Server
 * to the Family Map Client from the load web API.
 */
public class LoadResult extends Result
{
    /**
     * Constructs the result of the Load API from the Family Map Server
     * @param message the message for the result
     * @param status the status report for the result
     */
    public LoadResult(String message, boolean status) { super(message, status); }
}
