package Service;

import DataAccess.DataAccessException;
import Model.Event;
import RequestResult.EAPIRequest;
import RequestResult.EAPIResult;

/**
 * Performs the functionality for the /event WEB API
 */
public class EAPIService extends Service
{
    /**
     * Gets all events for all family members of a given user or a certain event
     * @param request the EAPIRequest object made from the request from the Family Map Client
     * @return the EAPIResult object to form the response for the event API from Family Map byu.cs240.android.familymapserver.Server
     * @throws DataAccessException if SQL error occurs
     */
    public EAPIResult eventAPI(EAPIRequest request) throws DataAccessException
    {
        EAPIResult result;
        try
        {
            //check if connection to the Family Map byu.cs240.android.familymapserver.Server database is already established
            //and establishes it if it isn't
            if (!checkConnection()) { setConnection(); }

            if (request.getEventID() == null)
            {
                Event[] events = myEventDAO.getAllEvents(request.getUsername());
                if (events.length == 0)
                {
                    throw new DataAccessException("Error encountered while getting events");
                }
                result = new EAPIResult(events, true);
            }
            else
            {
                Event event = myEventDAO.getEvent(request.getEventID());
                if (request.getUsername().equals(event.getAssociatedUsername()))
                {
                    result = new EAPIResult(event.getAssociatedUsername(), event.getEventID(), event.getPersonID(), event.getLatitude(),
                            event.getLongitude(), event.getCountry(), event.getCity(), event.getEventType(), event.getYear(), true);
                }
                else
                {
                    throw new DataAccessException("invalid request data");
                }
            }
            if (checkConnection()) { closeConnection(true); }
        }
        catch (DataAccessException ex)
        {
            result = new EAPIResult("Error: " + ex.getMessage(), false);
            //checks if connection is still established before closing
            //and closes it if it is
            if (checkConnection()) { closeConnection(false); }
        }
        return result;
    }
}
