package byu.cs240.android.familymapserver.DAOTest;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.EventDAO;
import Model.Event;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

public class EventDAOTest
{

    private Database db;
    private Event event;
    private EventDAO eDAO;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        db = new Database();
        event = new Event("susan", "personID1", 67.33f, 54.82f,
                "USA", "Trenton", "birth", 2000);
        db.setConnection();
        Connection conn = db.getConnection();
        eDAO = new EventDAO(conn);
        eDAO.clearEventData();
    }

    @AfterEach
    public void tearDown() throws DataAccessException
    {
        db.closeConnection(false);
    }

    @Test
    @DisplayName("Insertion Pass")
    public void insertPass() throws DataAccessException
    {
        eDAO.insertEvent(event);
        Event compareTest = eDAO.getEvent(event.getEventID());
        assertNotNull(compareTest);
        assertEquals(event, compareTest);
    }

    @Test
    @DisplayName("Insertion Fail")
    public void insertFail() throws DataAccessException
    {
        eDAO.insertEvent(event);
        assertThrows(DataAccessException.class, () -> eDAO.insertEvent(event));
    }

    @Test
    @DisplayName("Retrieval Pass")
    public void retrievePass() throws DataAccessException
    {
        eDAO.insertEvent(event);
        Event event2 = eDAO.getEvent(event.getEventID());
        assertNotNull(event2);
        assertEquals(event, event2);
    }

    @Test
    @DisplayName("Retrieval Fail")
    public void retrieveFail()
    {
        assertThrows(DataAccessException.class, () -> eDAO.getEvent(event.getEventID()));
    }

    @Test
    @DisplayName("Exists Pass")
    public void existsPass() throws DataAccessException
    {
        eDAO.insertEvent(event);
        assertTrue(eDAO.eventExists(event.getEventID()));
    }

    @Test
    @DisplayName("Exists Fail")
    public void existsFail()
    {
        assertFalse(eDAO.eventExists(event.getEventID()));
    }

    @Test
    @DisplayName("Clear")
    public void clearPass() throws DataAccessException
    {
        eDAO.insertEvent(event);
        event.setEventID("iscuWBD45");
        eDAO.insertEvent(event);
        assertEquals(2, eDAO.clearEventData());
        assertThrows(DataAccessException.class, () -> eDAO.getEvent(event.getEventID()));
    }

    @Test
    @DisplayName("Get All Events")
    public void getAllPeoplePass() throws DataAccessException
    {
        eDAO.insertEvent(event);
        event.setEventID("iscuWBD45");
        eDAO.insertEvent(event);
        Event[] events = eDAO.getAllEvents("susan");
        assertEquals(2, events.length);
    }

    @Test
    @DisplayName("Empty Person Table")
    public void getAllPeopleFail()
    {
        assertThrows(DataAccessException.class, () -> eDAO.getAllEvents("susan"));
    }

    @Test
    @DisplayName("Delete User's Persons")
    public void deletePeoplePass() throws DataAccessException
    {
        eDAO.insertEvent(event);
        event.setEventID("iscuWBD45");
        eDAO.insertEvent(event);
        event.setAssociatedUsername("False");
        event.setEventID("iefuvsweih");
        eDAO.insertEvent(event);
        assertEquals(2, eDAO.deleteEvents("susan"));
        Event[] events = eDAO.getAllEvents("False");
        assertEquals(1,events.length);
        assertThrows(DataAccessException.class, () -> eDAO.getAllEvents("susan"));
    }

    @Test
    @DisplayName("No People for User")
    public void deletePeoplePass2() throws DataAccessException
    {
        assertEquals(0, eDAO.deleteEvents("susan"));
    }
}
