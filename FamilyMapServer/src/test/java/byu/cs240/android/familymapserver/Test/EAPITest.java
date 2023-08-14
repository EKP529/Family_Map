package byu.cs240.android.familymapserver.Test;

import DataAccess.DataAccessException;
import byu.cs240.android.familymapshared.ObjectCoder;
import Model.Event;
import RequestResult.RegisterRequest;
import RequestResult.EAPIRequest;
import RequestResult.EAPIResult;
import Service.ClearService;
import Service.RegisterService;
import Service.EAPIService;
import Service.Service;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class EAPITest
{
    @BeforeEach
    public void setUp() throws DataAccessException
    {
        new ClearService().clear();
        RegisterRequest request = new RegisterRequest("susan", "mysecret", "susan@gmail.com",
                "Susan", "Ellis", "f");
        ObjectCoder oc = new ObjectCoder();
        new RegisterService().register(request, oc.toNames(), oc.toLocations());
        Event event = new Event("eventID1", "susan", "personID1", 67.33f,
                29.392f, "USA", "Provo", "Test", 2022);
        Event event2 = new Event("eventID2", "chad", "personID1", 67.33f,
                29.392f, "USA", "Provo", "Test", 2022);
        Service.setConnection();
        Service.myEventDAO.insertEvent(event);
        Service.myEventDAO.insertEvent(event2);
        Service.closeConnection(true);
    }

    @Test
    @DisplayName("Event API")
    public void eAPIPassTest() throws DataAccessException
    {
        EAPIResult result = new EAPIService().eventAPI(new EAPIRequest(null, "susan"));
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        Service.setConnection();
        Event[] events = Service.myEventDAO.getAllEvents("susan");
        Service.closeConnection(false);
        assertEquals(events.length, result.getData().length);
    }

    @Test
    @DisplayName("Event API w/ EventID")
    public void eAPIPassTest2() throws DataAccessException
    {
        EAPIResult result = new EAPIService().eventAPI(new EAPIRequest("eventID1", "susan"));
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNull(result.getData());
        Service.setConnection();
        Event event = Service.myEventDAO.getEvent("eventID1");
        Service.closeConnection(false);
        assertEquals(event.getEventID(), result.getEventID());
        assertEquals(event.getAssociatedUsername(), result.getAssociatedUsername());
        assertEquals(event.getPersonID(), result.getPersonID());
        assertEquals(event.getLatitude(), result.getLatitude());
        assertEquals(event.getLongitude(), result.getLongitude());
        assertEquals(event.getCountry(), result.getCountry());
        assertEquals(event.getCity(), result.getCity());
        assertEquals(event.getEventType(), result.getEventType());
        assertEquals(event.getYear(), result.getYear());
    }

    @Test
    @DisplayName("Invalid Request Data")
    public void eAPIFailTest() throws DataAccessException
    {
        EAPIResult result = new EAPIService().eventAPI(new EAPIRequest("eventID2", "susan"));
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
        EAPIResult result = new EAPIService().eventAPI(new EAPIRequest("eventID2", "susan"));
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertNull(result.getData());
        assertEquals("Error: Error encountered while finding an event", result.getMessage());
    }

    @Test
    @DisplayName("Empty Database")
    public void eAPIFailTest3() throws DataAccessException
    {
        new ClearService().clear();
        EAPIResult result = new EAPIService().eventAPI(new EAPIRequest(null, "susan"));
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertNull(result.getData());
        assertEquals("Error: no events in database", result.getMessage());
    }
}
