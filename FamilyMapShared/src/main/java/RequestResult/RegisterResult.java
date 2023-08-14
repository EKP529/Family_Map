package RequestResult;

/**
 * The class to hold the response from the Family Map Server
 * to the Family Map Client from the register web API.
 */
public class RegisterResult extends Result
{
    /**
     * the authentication token for the result of the Register API from the Family Map Server
     */
    private String authtoken;
    /**
     * the username for the result of the Register API from the Family Map Server
     */
    private String username;
    /**
     * the person ID for the result of the Register API from the Family Map Server
     */
    private String personID;
    /**
     * Constructs the error result of the Register API from the Family Map Server
     * @param message the error message
     * @param status the failed status
     */
    public RegisterResult(String message, boolean status) { super(message, status); }
    /**
     * Constructs the success result of the Register API from the Family Map Server
     * @param authtoken the authentication token for the result
     * @param username the username for the result
     * @param personID the person ID for the result
     * @param status the status report for the result
     */
    public RegisterResult(String authtoken, String username, String personID, boolean status)
    {
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
        super.success = status;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getUsername() {
        return username;
    }

    public String getPersonID() {
        return personID;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }
}
