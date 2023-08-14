package byu.cs240.android.familymapserver.Test;

import DataAccess.DataAccessException;
import byu.cs240.android.familymapshared.ObjectCoder;
import Model.Person;
import RequestResult.RegisterRequest;
import RequestResult.PAPIRequest;
import RequestResult.PAPIResult;
import Service.ClearService;
import Service.RegisterService;
import Service.PAPIService;
import Service.Service;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class PAPITest
{
    @BeforeEach
    public void setUp() throws DataAccessException
    {
        new ClearService().clear();
        RegisterRequest request = new RegisterRequest("susan", "mysecret", "susan@gmail.com",
                "Susan", "Ellis", "f");
        ObjectCoder oc = new ObjectCoder();
        new RegisterService().register(request, oc.toNames(), oc.toLocations());
        Person person = new Person("susan", "Susan", "Ellis", "f",
                null, null, null, "personID1");
        Person person2 = new Person("chad", "Chad", "Ellis", "m",
                null, null, null, "personID2");
        Service.setConnection();
        Service.myPersonDAO.insertPerson(person);
        Service.myPersonDAO.insertPerson(person2);
        Service.closeConnection(true);
    }

    @Test
    @DisplayName("Person API")
    public void pAPIPassTest() throws DataAccessException
    {
        PAPIResult result = new PAPIService().personAPI(new PAPIRequest(null, "susan"));
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        Service.setConnection();
        Person[] people = Service.myPersonDAO.getAllPeople("susan");
        Service.closeConnection(false);
        assertEquals(people.length, result.getData().length);
    }

    @Test
    @DisplayName("Event API w/ EventID")
    public void eAPIPassTest2() throws DataAccessException
    {
        PAPIResult result = new PAPIService().personAPI(new PAPIRequest("personID1", "susan"));
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNull(result.getData());
        Service.setConnection();
        Person person = Service.myPersonDAO.getPerson("personID1");
        Service.closeConnection(false);
        assertEquals(person.getPersonID(), result.getPersonID());
        assertEquals(person.getAssociatedUsername(), result.getAssociatedUsername());
        assertEquals(person.getFirstName(), result.getFirstName());
        assertEquals(person.getLastName(), result.getLastName());
        assertEquals(person.getGender(), result.getGender());
        assertEquals(person.getFatherID(), result.getFatherID());
        assertEquals(person.getMotherID(), result.getMotherID());
        assertEquals(person.getSpouseID(), result.getSpouseID());

    }

    @Test
    @DisplayName("Invalid Request Data")
    public void eAPIFailTest() throws DataAccessException
    {
        PAPIResult result = new PAPIService().personAPI(new PAPIRequest("personID2", "susan"));
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertNull(result.getData());
        assertEquals("Error: invalid request data", result.getMessage());
    }

    @Test
    @DisplayName("Empty Database w/ EventID")
    public void eAPIFailTest2() throws DataAccessException
    {
        new ClearService().clear();
        PAPIResult result = new PAPIService().personAPI(new PAPIRequest("personID2", "susan"));
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertNull(result.getData());
        assertEquals("Error: Error encountered while getting a person", result.getMessage());
    }

    @Test
    @DisplayName("Empty Database")
    public void eAPIFailTest3() throws DataAccessException
    {
        new ClearService().clear();
        PAPIResult result = new PAPIService().personAPI(new PAPIRequest(null, "susan"));
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertNull(result.getData());
        assertEquals("Error: no persons in database", result.getMessage());
    }
}
