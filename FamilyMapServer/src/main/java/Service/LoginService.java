package Service;

import DataAccess.DataAccessException;
import Model.AuthToken;
import Model.User;
import RequestResult.LoginRequest;
import RequestResult.LoginResult;

/**
 * Performs the functionality for the login WEB API
 */
public class LoginService extends Service
{
    /**
     * Constructs the LoginService, initializing any needed Data Access tools
     */
    public LoginService() {}
    /**
     * Logs the user into the Family Map byu.cs240.android.familymapserver.Server and returns an authentication token in the result body.
     * @param request the LoginRequest object made from the request from the Family Map Client
     * @return the LoginResult object to form the response for the Login API from the Family Map byu.cs240.android.familymapserver.Server
     * @throws DataAccessException if SQL error occurs
     */
    public LoginResult login(LoginRequest request) throws DataAccessException
    {
        LoginResult result;
        try
        {
            if (!checkConnection()) { setConnection(); }
            if (!myUserDAO.userExists(request.getUsername(), request.getPassword()))
            {
                throw new DataAccessException("Error occurred while getting user");
            }
            AuthToken aToken = new AuthToken(request.getUsername());
            myAuthTokenDAO.insertAuthToken(aToken);
            User user = myUserDAO.getUser(request.getUsername(), request.getPassword());
            result = new LoginResult(aToken.getAuthToken(), aToken.getUsername(),
                    user.getPersonID(), true);
            if (checkConnection()) { closeConnection(true); }
        }
        catch (DataAccessException ex)
        {
            result = new LoginResult("Error: " + ex.getMessage(), false);
            if (checkConnection()) { closeConnection(false); }
        }
        return result;
    }
}
