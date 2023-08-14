package byu.cs240.android.familymapserver.DAOTest;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.PersonDAO;
import Model.Person;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

public class PersonDAOTest
{
    private Database db;
    private Person person;
    private PersonDAO pDAO;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        db = new Database();
        person = new Person( "Gale123A", "Gale", "Peterson", "m");
        person.setPersonID("Biking_123A");
        db.setConnection();
        Connection conn = db.getConnection();
        pDAO = new PersonDAO(conn);
        pDAO.clearPersonData();
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
        pDAO.insertPerson(person);
        Person compareTest = pDAO.getPerson(person.getPersonID());
        assertNotNull(compareTest);
        assertEquals(person, compareTest);
    }

    @Test
    @DisplayName("Insertion Fail")
    public void insertFail() throws DataAccessException
    {
        pDAO.insertPerson(person);
        assertThrows(DataAccessException.class, () -> pDAO.insertPerson(person));
    }

    @Test
    @DisplayName("Retrieval Pass")
    public void retrievePass() throws DataAccessException
    {
        pDAO.insertPerson(person);
        Person person2 = pDAO.getPerson(person.getPersonID());
        assertNotNull(person2);
        assertEquals(person, person2);
    }

    @Test
    @DisplayName("Retrieval Fail")
    public void retrieveFail()
    {
        assertThrows(DataAccessException.class, () -> pDAO.getPerson(person.getPersonID()));
    }

    @Test
    @DisplayName("Exists Pass")
    public void existsPass() throws DataAccessException
    {
        pDAO.insertPerson(person);
        assertTrue(pDAO.personExists(person.getPersonID()));
    }

    @Test
    @DisplayName("Exists Fail")
    public void existsFail()
    {
        assertFalse(pDAO.personExists(person.getPersonID()));
    }

    @Test
    @DisplayName("Clear")
    public void clearPass() throws DataAccessException
    {
        pDAO.insertPerson(person);
        person.setPersonID("iscuWBD45");
        pDAO.insertPerson(person);
        assertEquals(2, pDAO.clearPersonData());
        assertThrows(DataAccessException.class, () -> pDAO.getPerson(person.getPersonID()));
    }

    @Test
    @DisplayName("Get All People")
    public void getAllPeoplePass() throws DataAccessException
    {
        pDAO.insertPerson(person);
        person.setPersonID("iscuWBD45");
        pDAO.insertPerson(person);
        Person[] people = pDAO.getAllPeople("Gale123A");
        assertEquals(2, people.length);
    }

    @Test
    @DisplayName("Empty Person Table")
    public void getAllPeopleFail()
    {
        assertThrows(DataAccessException.class, () -> pDAO.getAllPeople("Gale123A"));
    }

    @Test
    @DisplayName("Delete User's Persons")
    public void deletePeoplePass() throws DataAccessException
    {
        pDAO.insertPerson(person);
        person.setPersonID("iscuWBD45");
        pDAO.insertPerson(person);
        person.setAssociatedUsername("Faux");
        person.setPersonID("iefuvsweih");
        pDAO.insertPerson(person);
        assertEquals(2, pDAO.deletePeople("Gale123A"));
        Person[] people = pDAO.getAllPeople("Faux");
        assertEquals(1,people.length);
        assertThrows(DataAccessException.class, () -> pDAO.getAllPeople("Gale123A"));
    }

    @Test
    @DisplayName("No People for User")
    public void deletePeoplePass2() throws DataAccessException
    {
        assertEquals(0, pDAO.deletePeople("Gale123A"));
    }
}
