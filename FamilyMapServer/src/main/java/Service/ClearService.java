package Service;

import DataAccess.DataAccessException;
import RequestResult.ClearResult;

/**
 * Performs the functionality for the clear WEB API
 */
public class ClearService extends Service
{
    /**
     * Clears all the data in the Family Map byu.cs240.android.familymapserver.Server database.
     * @return ClearResult object to form the response for the Clear API from the Family Map byu.cs240.android.familymapserver.Server
     * @throws DataAccessException if SQL error occurs
     */
    public ClearResult clear() throws DataAccessException
    {
        ClearResult result;
        try
        {
            if (!checkConnection()) { setConnection(); }
            myAuthTokenDAO.clearAuthTokenData();
            myEventDAO.clearEventData();
            myPersonDAO.clearPersonData();
            myUserDAO.clearUserData();
            result = new ClearResult("Clear succeeded.", true);
            if (checkConnection()) { closeConnection(true); }
        }
        catch (DataAccessException ex)
        {
            result = new ClearResult("Error: " + ex.getMessage(), false);
            if (checkConnection()) { closeConnection(false); }
        }
        return result;
    }
}
