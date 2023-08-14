package Service;

import DataAccess.DataAccessException;
import Model.*;
import RequestResult.FillRequest;
import RequestResult.FillResult;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Performs the functionality for the fill WEB API
 */
public class FillService extends Service
{
    /**
     * Populates the Family Map byu.cs240.android.familymapserver.Server database with generated data for the specified username in the request body.
     * @param request the FillRequest object made from the request from the Family Map Client
     * @return the FillResult object to form the response for the Fill API from the Family Map byu.cs240.android.familymapserver.Server
     * @throws DataAccessException if SQL error occurs
     */
    public FillResult fill(FillRequest request, Name names, EventData locations) throws DataAccessException
    {
        FillResult result;
        try
        {
            //checks if connection to database is already established
            //and establishes it if it isn't
            if(!checkConnection()) { setConnection(); }

            //checks if the username(or user) given exists in the database
            if (!myUserDAO.userExists(request.getUsername()))
            {
                throw new DataAccessException("Username not found in database");
            }

            //checks if the desired # of generations given is valid
            if (!(request.getGenerations() >= 0))
            {
                throw new DataAccessException("Invalid # of generations");
            }

            //clears the database of previous data associated with the username(user)
            myPersonDAO.deletePeople(request.getUsername());
            myEventDAO.deleteEvents(request.getUsername());

            //generates the person and birth event for the user (or 0 generations)
            User user = myUserDAO.getUser(request.getUsername());
            Person person = new Person(user.getUsername(), user.getFirstName(), user.getLastName(), user.getGender());
            person.setPersonID(user.getPersonID());
            int currYear = LocalDate.now().getYear();

            //the bounds for getYear method ensure user is at least 12 but no older than 120 years old;
            int birthYear = locations.getYear(currYear - 11, currYear - 120);
            Location local = locations.getLocation();
            myEventDAO.insertEvent(new Event(user.getUsername(), person.getPersonID(), local.getLatitude(), local.getLongitude(),
                    local.getCountry(), local.getCity(), "birth", birthYear));

            ArrayList<Person> people = new ArrayList<>();
            ArrayList<Person> persons = new ArrayList<>();
            persons.add(person);
            int personCount = 1;
            int eventCount = 1;

            //generates people and events for next generations if applicable
            while (persons.size() < Math.pow(2, request.getGenerations() + 1))
            {
                int num = persons.size();
                ArrayList<Event> eventsToInsert = new ArrayList<>();
                birthYear = birthYear - 20;
                for (int j = 0; j < num; j++)
                {
                    //creates the father with corresponding event data for each person in the last considered generation
                    Person male = new Person(user.getUsername(), names.getMaleName(), names.getSurname(), "m");
                    local = locations.getLocation();
                    Event birthM = new Event(user.getUsername(), male.getPersonID(), local.getLatitude(), local.getLongitude(),
                            local.getCountry(), local.getCity(), "birth", birthYear);
                    eventsToInsert.add(birthM);
                    local = locations.getLocation();
                    Event marriageM = new Event(user.getUsername(), male.getPersonID(), local.getLatitude(), local.getLongitude(),
                        local.getCountry(), local.getCity(), "marriage", birthYear + 18);
                    eventsToInsert.add(marriageM);
                    local = locations.getLocation();
                    Event deathM = new Event(user.getUsername(), male.getPersonID(), local.getLatitude(), local.getLongitude(),
                            local.getCountry(), local.getCity(), "death", birthYear + 100);
                    eventsToInsert.add(deathM);

                    //creates the mother with corresponding event data for each person in the last considered generation
                    Person female = new Person(user.getUsername(), names.getFemaleName(), male.getLastName(), "f");
                    local = locations.getLocation();
                    Event birthF = new Event(user.getUsername(), female.getPersonID(), local.getLatitude(), local.getLongitude(),
                            local.getCountry(), local.getCity(), "birth", birthYear);
                    eventsToInsert.add(birthF);
                    Event marriageF = new Event(user.getUsername(), female.getPersonID(), marriageM.getLatitude(), marriageM.getLongitude(),
                            marriageM.getCountry(), marriageM.getCity(), "marriage", birthYear + 18);
                    eventsToInsert.add(marriageF);
                    local = locations.getLocation();
                    Event deathF = new Event(user.getUsername(), female.getPersonID(), local.getLatitude(), local.getLongitude(),
                            local.getCountry(), local.getCity(), "death", birthYear + 100);
                    eventsToInsert.add(deathF);

                    //sets the IDs accordingly for the parents and child and
                    //essentially moves on to the next considered generation
                    if (num < Math.pow(2, request.getGenerations()))
                    {
                        male.setSpouseID(female.getPersonID());
                        female.setSpouseID(male.getPersonID());
                        persons.get(0).setFatherID(male.getPersonID());
                        persons.get(0).setMotherID(female.getPersonID());
                    }
                    people.add(persons.get(0));
                    persons.remove(0);
                    persons.add(male);
                    persons.add(female);
                }
                if (persons.size() < Math.pow(2, request.getGenerations() + 1))
                {
                    personCount += persons.size();
                    eventCount += eventsToInsert.size();
                    for (Event event : eventsToInsert)
                    {
                        myEventDAO.insertEvent(event);
                    }
                }
            }
            for (Person value : people) { myPersonDAO.insertPerson(value); }
            String message = String.format("Successfully added %d persons and %d events to the database.", personCount, eventCount);
            result = new FillResult(message, true);

            //checks if connection is still established before closing
            //and closes it if it is
            if (checkConnection()) { closeConnection(true); }
        }
        catch (DataAccessException ex)
        {
            result = new FillResult("Error: " + ex.getMessage(), false);
            //checks if connection is still established before closing
            //and closes it if it is
            if (checkConnection()) { closeConnection(false); }
        }
        return result;
    }
}
