package Service;

import DataAccess.DataAccessException;
import Model.Person;
import RequestResult.PAPIRequest;
import RequestResult.PAPIResult;

/**
 * Performs the functionality for the /event WEB API
 */
public class PAPIService extends Service
{
    /**
     * Gets all family members for a given user or a certain family member
     * @param request the PAPIRequest object made from the request from the Family Map Client
     * @return the PAPIResult object to form the response for the person API from the Family Map byu.cs240.android.familymapserver.Server
     * @throws DataAccessException if SQL error occurs
     */
    public PAPIResult personAPI(PAPIRequest request) throws DataAccessException
    {
        PAPIResult result;
        try
        {
            //check if connection to the Family Map byu.cs240.android.familymapserver.Server database is already established
            //and establishes it if it isn't
            if (!checkConnection()) { setConnection(); }

            if (request.getPersonID() == null)
            {
                Person[] people = myPersonDAO.getAllPeople(request.getUsername());
                if (people.length == 0)
                {
                    throw new DataAccessException("Error encountered while getting persons");
                }
                result = new PAPIResult(people, true);
            }
            else
            {
                Person person = myPersonDAO.getPerson(request.getPersonID());
                if (request.getUsername().equals(person.getAssociatedUsername()))
                {
                    result = new PAPIResult(person.getAssociatedUsername(), person.getPersonID(), person.getFirstName(), person.getLastName(),
                            person.getGender(), person.getFatherID(), person.getMotherID(), person.getSpouseID(), true);
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
            result = new PAPIResult("Error: " + ex.getMessage(), false);
            //checks if connection is still established before closing
            //and closes it if it is
            if (checkConnection()) { closeConnection(false); }
        }
        return result;
    }
}
