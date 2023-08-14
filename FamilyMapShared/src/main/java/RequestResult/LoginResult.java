package RequestResult;

/**
 * The class to hold the response from the Family Map Server
 * to the Family Map Client from the login web API.
 */
public class LoginResult extends Result
{
    /**
     * the authToken for the result of the Login API from the Family Map Server
     */
    private String authtoken;
    /**
     * the username for the result of the Login API from the Family Map Server
     */
    private String username;
    /**
     * the personID for the result of the Login API from the Family Map Server
     */
    private String personID;
    /**
     * Constructs the successful result of the Login API from the Family Map Server
     * @param authtoken the authentication token for the result
     * @param username the username for the result
     * @param personID the person ID for the result
     * @param success the success status for the result
     */
    public LoginResult(String authtoken, String username, String personID, boolean success) {
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
        super.success = success;
    }
    /**
     * Constructs the error result of the Login API from the Family Map Server
     * @param message the message for the result
     * @param status the status report for the result
     */
    public LoginResult(String message, boolean status) { super(message, status); }

    public String getUsername() {
        return username;
    }

    public String getPersonID() {
        return personID;
    }

    public String getAuthtoken() {
        return authtoken;
    }
}
