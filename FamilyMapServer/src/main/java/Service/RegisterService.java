package Service;

import DataAccess.DataAccessException;
import Model.*;
import RequestResult.FillRequest;
import RequestResult.FillResult;
import RequestResult.RegisterRequest;
import RequestResult.RegisterResult;

/**
 * Performs the functionality for the register WEB API
 */
public class RegisterService extends Service
{
    /**
     * Creates a new user in the Family Map byu.cs240.android.familymapserver.Server database, with accompanying person and authentication token
     * entries, generates data for the new user, logs the user in,
     * and returns the response.
     * @param request the RegisterRequest object made from the request from the Family Map Client
     * @return RegisterResult object to form the response for the Register API from the Family Map byu.cs240.android.familymapserver.Server
     * @throws DataAccessException if SQL error occurs
     */
    public RegisterResult register(RegisterRequest request, Name names, EventData locations) throws DataAccessException
    {
        RegisterResult result;
        try
        {
            //check if connection to the Family Map byu.cs240.android.familymapserver.Server database is already established
            //and establishes it if it isn't
            if (!checkConnection()) { setConnection(); }

            //checks if the user already exists in the database
            if (myUserDAO.userExists(request.getUsername(), request.getPassword()))
            {
                throw new DataAccessException("Error occurred while inserting user");
            }
            User user = new User(request.getUsername(), request.getPassword(), request.getEmail(),
                    request.getFirstName(), request.getLastName(), request.getGender());

            //ensures the request gave all necessary values for the user
            if (user.isComplete())
            {
                myUserDAO.insertUser(user);
                AuthToken authToken = new AuthToken(user.getUsername());
                myAuthTokenDAO.insertAuthToken(authToken);
                FillRequest request1 = new FillRequest(request.getUsername());
                FillResult result1 = new FillService().fill(request1, names, locations);
                if (result1.isSuccess())
                {
                    result = new RegisterResult(authToken.getAuthToken(), authToken.getUsername(),
                            user.getPersonID(), true);
                    //checks if connection is still established before closing
                    //and closes it if it is
                    if (checkConnection()) { closeConnection(true); }
                }
                else
                {
                    result = new RegisterResult(result1.getMessage(), false);
                    if (checkConnection()) { closeConnection(false); }
                }
            }
            else
            {
                result = new RegisterResult("Error: Invalid request data" , false);
                //checks if connection is still established before closing
                //and closes it if it is
                if (checkConnection()) { closeConnection(false); }
            }
        }
        catch (DataAccessException ex)
        {
            result = new RegisterResult("Error: " + ex.getMessage(), false);
            //checks if connection is still established before closing
            //and closes it if it is
            if (checkConnection()) { closeConnection(false); }
        }
        return result;
    }
}
