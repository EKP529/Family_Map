package RequestResult;

import java.util.Objects;

/**
 * The class to hold the request from the Family Map Client
 * to the Family Map Server from the login web API.
 */
public class LoginRequest
{
    /**
     * the username from the request from the Login API for the Family Map Client
     */
    private String username;
    /**
     * the password from the request from the Login API for the Family Map Client
     */
    private String password;
    /**
     * Constructs LoginRequest object to hold the data from the request from the Login API for the Family Map Client
     * @param username the username for the Login request
     * @param password the password for the Login request
     */
    public LoginRequest(String username, String password)
    {
        this.username = username;
        this.password = password;
    }
    public LoginRequest() {}
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isComplete()
    {
        return ((!Objects.equals(username, null))
                && (!Objects.equals(password, null)));
    }
}
