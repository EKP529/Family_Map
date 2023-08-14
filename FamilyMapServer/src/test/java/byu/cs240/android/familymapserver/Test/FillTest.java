package byu.cs240.android.familymapserver.Test;

import DataAccess.DataAccessException;
import byu.cs240.android.familymapshared.ObjectCoder;
import Model.Event;
import Model.Person;
import Model.User;
import RequestResult.FillRequest;
import RequestResult.FillResult;
import Service.ClearService;
import Service.FillService;
import Service.Service;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class FillTest
{
    private FillRequest request;
    private FillResult result;
    private final ObjectCoder oc = new ObjectCoder();

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        new ClearService().clear();
        User user = new User("susan", "mysecret", "susan@gmail.com",
                "Susan", "Ellis", "f", "personID1");
        Service.setConnection();
        Service.myUserDAO.insertUser(user);
        Service.closeConnection(true);
    }

    @Test
    @DisplayName("Standard Fill Pass")
    public void fillPassTest() throws DataAccessException
    {
        request = new FillRequest("susan");
        result = new FillService().fill(request, oc.toNames(), oc.toLocations());
        assertTrue(result.isSuccess());
        assertEquals("Successfully added 31 persons and 91 events to the database.",
                result.getMessage());
        Service.setConnection();
        Person[] people = Service.myPersonDAO.getAllPeople("susan");
        Event[] events = Service.myEventDAO.getAllEvents("susan");
        Service.closeConnection(false);
        assertEquals(31, people.length);
        Person person = people[0];
        assertEquals("personID1", person.getPersonID());
        assertEquals(91, events.length);
    }

    @Test
    @DisplayName("Fill 3 Generations")
    public void fillPassTest2() throws DataAccessException
    {
        request = new FillRequest("susan", 3);
        result = new FillService().fill(request, oc.toNames(), oc.toLocations());
        assertTrue(result.isSuccess());
        assertEquals("Successfully added 15 persons and 43 events to the database.",
                result.getMessage());
        Service.setConnection();
        Person[] people = Service.myPersonDAO.getAllPeople("susan");
        Event[] events = Service.myEventDAO.getAllEvents("susan");
        Service.closeConnection(false);
        assertEquals(15, people.length);
        Person person = people[0];
        assertEquals("personID1", person.getPersonID());
        assertEquals(43, events.length);
    }

    @Test
    @DisplayName("Fill 5 Generations")
    public void fillPassTest3() throws DataAccessException
    {
        request = new FillRequest("susan", 5);
        result = new FillService().fill(request, oc.toNames(), oc.toLocations());
        assertTrue(result.isSuccess());
        assertEquals("Successfully added 63 persons and 187 events to the database.",
                result.getMessage());
        Service.setConnection();
        Person[] people = Service.myPersonDAO.getAllPeople("susan");
        Event[] events = Service.myEventDAO.getAllEvents("susan");
        Service.closeConnection(false);
        assertEquals(63, people.length);
        Person person = people[0];
        assertEquals("personID1", person.getPersonID());
        assertEquals(187, events.length);
    }

    @Test
    @DisplayName("Invalid Generations")
    public void fillFailTest() throws DataAccessException
    {
        request = new FillRequest("susan", -3);
        result = new FillService().fill(request, oc.toNames(), oc.toLocations());
        assertFalse(result.isSuccess());
        assertEquals("Error: Invalid # of generations", result.getMessage());
        assertThrows(DataAccessException.class, () -> Service.myPersonDAO.getAllPeople("susan"));
        assertThrows(DataAccessException.class, () -> Service.myEventDAO.getAllEvents("susan"));
    }

    @Test
    @DisplayName("Invalid Username")
    public void fillFailTest2() throws DataAccessException
    {
        request = new FillRequest("fail");
        result = new FillService().fill(request, oc.toNames(), oc.toLocations());
        assertFalse(result.isSuccess());
        assertEquals("Error: Username not found in database", result.getMessage());
        assertThrows(DataAccessException.class, () -> Service.myPersonDAO.getAllPeople("susan"));
        assertThrows(DataAccessException.class, () -> Service.myEventDAO.getAllEvents("susan"));
    }
}
