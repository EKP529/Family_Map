package Model;

import java.util.Objects;
import java.util.UUID;

/**
 * an authentication token to represent a user's session in the Family Map app
 */

public class AuthToken
{
    /**
     * the unique authentication token for a user
     */
    private String authToken = UUID.randomUUID().toString();
    /**
     * the username of the user associated with the authentication token
     */
    private String username;
    /**
     * Constructs an Authentication Token object with necessary data for the Family Map Server.
     * @param username the username to whom the authentication token corresponds
     */
    public AuthToken(String username)
    {
        this.username = username;
    }
    /**
     * Another AuthToken constructor with the option to give an initial authentication token value
     * as well as the standard username and personID values.
     * @param authToken the desired authentication token
     * @param username the username to whom the authentication token corresponds
     */
    public AuthToken(String authToken, String username)
    {
        this.authToken = authToken;
        this.username = username;
    }
    /**
     * Checks if an object is equal in value to the current AuthToken object
     * @param obj the object in question
     * @return true or false
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AuthToken aToken = (AuthToken) obj;
        return (Objects.equals(authToken, aToken.authToken)
                && Objects.equals(username, aToken.username));
    }
    public String getAuthToken()
    {
        return authToken;
    }
    public void setAuthToken(String authToken) { this.authToken = authToken; }
    public String getUsername()
    {
        return username;
    }
    public void setUsername(String username)
    {
        this.username = username;
    }
}
