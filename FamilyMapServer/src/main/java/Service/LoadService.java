package Service;

import DataAccess.DataAccessException;
import Model.Event;
import Model.Person;
import Model.User;
import RequestResult.LoadRequest;
import RequestResult.LoadResult;

/**
 * Performs the functionality for the load WEB API
 */
public class LoadService extends Service
{
    /**
     * Clears all data from the database and loads the user, person, and event
     * data from the request body into the database.
     * @param request the LoadRequest object made from the request from the Family Map Client
     * @return the LoadResult object to form the response for the Load API from the Family Map byu.cs240.android.familymapserver.Server
     * @throws DataAccessException if SQL error occurs
     */
    public LoadResult load(LoadRequest request) throws DataAccessException
    {
        LoadResult result;
        try
        {
            if(!checkConnection()) { setConnection(); }

            //clears all data from the database
            myAuthTokenDAO.clearAuthTokenData();
            myEventDAO.clearEventData();
            myPersonDAO.clearPersonData();
            myUserDAO.clearUserData();

            //checks if any of the arrays from the request weren't initialized
            if ((request.getUsers() == null) || (request.getPersons() == null) || (request.getEvents() == null))
            {
                throw new DataAccessException("invalid request data");
            }

            //checks if any of the arrays from the request were initialized but empty
            if ((request.getUsers().length == 0) || (request.getPersons().length == 0) || (request.getEvents().length == 0))
            {
                throw new DataAccessException("invalid request data");
            }

            //begins to add the data to the database if previous checks didn't throw exception
            else
            {
                for (int i = 0; i < request.getUsers().length; i++)
                {
                    User user = request.getUsers()[i];
                    if (!user.isComplete())
                    {
                        throw new DataAccessException("invalid request data");
                    }
                    myUserDAO.insertUser(user);
                }
                for (int i = 0; i < request.getPersons().length; i++)
                {
                    Person person = request.getPersons()[i];
                    if (!person.isComplete())
                    {
                        throw new DataAccessException("invalid request data");
                    }
                    if (!myUserDAO.userExists(person.getAssociatedUsername()))
                    {
                        throw new DataAccessException("invalid request data");
                    }
                    myPersonDAO.insertPerson(person);
                }
                for (int i = 0; i < request.getEvents().length; i++)
                {
                    Event event = request.getEvents()[i];
                    if (!event.isComplete())
                    {
                        throw new DataAccessException("invalid request data");
                    }
                    if (!myUserDAO.userExists(event.getAssociatedUsername()))
                    {
                        throw new DataAccessException("invalid request data");
                    }
                    if (!myPersonDAO.personExists(event.getPersonID()))
                    {
                        throw new DataAccessException("invalid request data");
                    }
                    myEventDAO.insertEvent(event);
                }
                String resultString = String.format("Successfully added %d users, %d persons, and %d events to the database.",
                        request.getUsers().length, request.getPersons().length, request.getEvents().length);
                result = new LoadResult(resultString, true);
                if (checkConnection()) { closeConnection(true); }
            }
        }
        catch (DataAccessException ex)
        {
            result = new LoadResult("Error: " + ex.getMessage(), false);
            if (checkConnection()) { closeConnection(false); }
        }
        return result;
    }
}
