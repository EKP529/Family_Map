package byu.cs240.android.familymapserver.Test;

import DataAccess.DataAccessException;
import Model.Event;
import Model.Person;
import Model.User;
import RequestResult.LoadRequest;
import RequestResult.LoadResult;
import Service.ClearService;
import Service.LoadService;
import Service.Service;
import org.junit.jupiter.api.*;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class LoadTest
{
    private LoadRequest request;
    private LoadResult result;
    User[] users;
    Person[] people;
    Event[] events;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        new ClearService().clear();
        users = new User[2];
        users[0] = new User("susan", "mysecret", "susan@gmail.com",
                "Susan", "Ellis", "f", "personID1");
        users[1] = new User("carter", "mysecret", "carter@gmail.com",
                "Carter", "Ellis", "m", "personID2");
        people = new Person[14];
        people[0] = new Person("susan", "Susan", "Ellis", "f",
                null, null, null, "personID1");
        people[1] = new Person("susan", "Susan", "Ellis", "f",
                null, null, null, "personID2");
        people[1].setAssociatedUsername("carter");
        events = new Event[38];
        events[0] = new Event("susan", "personID1", 67.33f, 54.82f,
                "USA", "Trenton", "birth", 2000);
        events[1] = new Event("carter", "personID2", 67.33f, 54.82f,
                "USA", "Trenton", "birth", 2000);
        for (int i = 2; i < people.length; i++)
        {
            if (i < 8)
            {
                people[i] = new Person("susan", "Susan", "Ellis", "f",
                        null, null, null, "personID");
                people[i].setPersonID("personID" + (i+1));
            }
            else
            {
                people[i] = new Person("carter", "Carter", "Ellis", "m",
                        null, null, null, "personID");
                people[i].setPersonID("personID" + (i+1));
            }

        }
        for (int i = 2; i < events.length; i++)
        {
            if (i < 20)
            {
                events[i] = new Event("susan", "personID", 67.33f, 54.82f,
                        "USA", "Trenton", "birth", 2000);
                int k = new Random().nextInt(6) + 3;
                events[i].setPersonID("personID" + k);
            }
            else
            {
                events[i] = new Event("carter", "personID", 67.33f, 54.82f,
                        "USA", "Trenton", "birth", 2000);
                int k = new Random().nextInt(6) + 9;
                events[i].setPersonID("personID" + k);
            }
        }
        request = new LoadRequest(users, people, events);
    }
    private void emptyTest() throws DataAccessException
    {
        Service.setConnection();
        assertThrows(DataAccessException.class, () -> Service.myUserDAO.getUser("susan"));
        assertThrows(DataAccessException.class, () -> Service.myUserDAO.getUser("carter"));
        assertThrows(DataAccessException.class, () -> Service.myPersonDAO.getAllPeople("susan"));
        assertThrows(DataAccessException.class, () -> Service.myPersonDAO.getAllPeople("susan"));
        assertThrows(DataAccessException.class, () -> Service.myEventDAO.getAllEvents("carter"));
        assertThrows(DataAccessException.class, () -> Service.myEventDAO.getAllEvents("carter"));
        Service.closeConnection(false);
    }

    @Test
    @DisplayName("Load Pass")
    public void loadPassTest() throws DataAccessException
    {
        result = new LoadService().load(request);

        assertTrue(result.isSuccess());
        assertEquals("Successfully added 2 users, 14 persons, and 38 events to the database.",
                result.getMessage());
        Service.setConnection();
        people = Service.myPersonDAO.getAllPeople("susan");
        events = Service.myEventDAO.getAllEvents("susan");
        assertEquals(7, people.length);
        assertEquals(19, events.length);
        people = Service.myPersonDAO.getAllPeople("carter");
        events = Service.myEventDAO.getAllEvents("carter");
        assertEquals(7, people.length);
        assertEquals(19, events.length);
        User user = new User("susan", "mysecret", "susan@gmail.com",
                "Susan", "Ellis", "f", "personID1");
        assertEquals(user, Service.myUserDAO.getUser("susan"));
        user = new User("carter", "mysecret", "carter@gmail.com",
                "Carter", "Ellis", "m", "personID2");
        assertEquals(user, Service.myUserDAO.getUser("carter"));
        Service.closeConnection(false);
    }

    @Test
    @DisplayName("Null Array in Request")
    public void loadFailTest() throws DataAccessException
    {
        users = request.getUsers();
        request.setUsers(null);
        result = new LoadService().load(request);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("Error: invalid request data",
                result.getMessage());
        request.setUsers(users);
        emptyTest();

        people = request.getPersons();
        request.setPersons(null);
        result = new LoadService().load(request);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("Error: invalid request data",
                result.getMessage());
        request.setPersons(people);
        emptyTest();

        events = request.getEvents();
        request.setEvents(null);
        result = new LoadService().load(request);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("Error: invalid request data",
                result.getMessage());
        emptyTest();
    }

    @Test
    @DisplayName("Empty Array in Request")
    public void loadFailTest2() throws DataAccessException
    {
        users = request.getUsers();
        request.setUsers(new User[0]);
        result = new LoadService().load(request);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("Error: invalid request data",
                result.getMessage());
        request.setUsers(users);
        emptyTest();

        people = request.getPersons();
        request.setPersons(new Person[0]);
        result = new LoadService().load(request);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("Error: invalid request data",
                result.getMessage());
        request.setPersons(people);
        emptyTest();

        events = request.getEvents();
        request.setEvents(new Event[0]);
        result = new LoadService().load(request);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("Error: invalid request data",
                result.getMessage());
        emptyTest();
    }

    @Test
    @DisplayName("Empty Array in Request")
    public void loadFailTest3() throws DataAccessException
    {
        users = request.getUsers();
        User[] badUsers = request.getUsers();
        badUsers[1].setUsername(null);
        request.setUsers(badUsers);
        result = new LoadService().load(request);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("Error: invalid request data",
                result.getMessage());
        request.setUsers(users);
        emptyTest();

        people = request.getPersons();
        Person[] badPersons = request.getPersons();
        badPersons[6].setAssociatedUsername(null);
        request.setPersons(badPersons);
        result = new LoadService().load(request);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("Error: invalid request data",
                result.getMessage());
        request.setPersons(people);
        emptyTest();

        events = request.getEvents();
        Event[] badEvents = request.getEvents();
        badEvents[18].setAssociatedUsername(null);
        request.setEvents(badEvents);
        result = new LoadService().load(request);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("Error: invalid request data",
                result.getMessage());
        emptyTest();
    }
}
