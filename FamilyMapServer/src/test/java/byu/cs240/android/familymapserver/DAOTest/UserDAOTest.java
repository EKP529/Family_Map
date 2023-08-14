package byu.cs240.android.familymapserver.DAOTest;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.UserDAO;
import Model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest
{
    private Database db;
    private User user;
    private UserDAO uDAO;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        db = new Database();
        user = new User("Gale123A", "Biking_123A", "Gale123A@gmail.com", "Gale", "Peterson", "m");
        db.setConnection();
        Connection conn = db.getConnection();
        uDAO = new UserDAO(conn);
        uDAO.clearUserData();
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
        uDAO.insertUser(user);
        User compareTest = uDAO.getUser(user.getUsername(), user.getPassword());
        assertNotNull(compareTest);
        assertEquals(user, compareTest);
    }


    @Test
    @DisplayName("Insertion Fail")
    public void insertFail() throws DataAccessException
    {
        uDAO.insertUser(user);
        assertThrows(DataAccessException.class, () -> uDAO.insertUser(user));
    }

    @Test
    @DisplayName("Retrieval Pass")
    public void retrievePass() throws DataAccessException
    {
        uDAO.insertUser(user);
        User compareTest = uDAO.getUser(user.getUsername(), user.getPassword());
        assertNotNull(compareTest);
        assertEquals(user, compareTest);
    }

    @Test
    @DisplayName("Retrieval Fail")
    public void retrieveFail()
    {
        assertThrows(DataAccessException.class, () -> uDAO.getUser(user.getUsername(), user.getPassword()));
    }

    @Test
    @DisplayName("Clear")
    public void clearPass() throws DataAccessException
    {
        uDAO.insertUser(user);
        user.setUsername("iscuWBD45");
        uDAO.insertUser(user);
        assertEquals(2, uDAO.clearUserData());
        assertThrows(DataAccessException.class, () -> uDAO.getUser(user.getUsername(), user.getPassword()));
    }

    @Test
    @DisplayName("Exists Pass")
    public void existsPass() throws DataAccessException
    {
        uDAO.insertUser(user);
        assertTrue(uDAO.userExists(user.getUsername()));
    }

    @Test
    @DisplayName("Exists Fail")
    public void existsFail()
    {
        assertFalse(uDAO.userExists(user.getUsername()));
    }


}
