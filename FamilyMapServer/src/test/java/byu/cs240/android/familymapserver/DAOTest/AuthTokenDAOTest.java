package byu.cs240.android.familymapserver.DAOTest;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.AuthTokenDAO;
import Model.AuthToken;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

public class AuthTokenDAOTest
{

    private Database db;
    private AuthToken authToken;
    private AuthTokenDAO aDAO;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        db = new Database();
        authToken = new AuthToken("Gale123A");
        db.setConnection();
        Connection conn = db.getConnection();
        aDAO = new AuthTokenDAO(conn);
        aDAO.clearAuthTokenData();
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
        aDAO.insertAuthToken(authToken);
        AuthToken compareTest = aDAO.getAuthToken(authToken.getAuthToken());
        assertNotNull(compareTest);
        assertEquals(authToken, compareTest);
    }


    @Test
    @DisplayName("Insertion Fail")
    public void insertFail() throws DataAccessException
    {
        aDAO.insertAuthToken(authToken);
        assertThrows(DataAccessException.class, () -> aDAO.insertAuthToken(authToken));
    }

    @Test
    @DisplayName("Retrieval Pass")
    public void retrievePass() throws DataAccessException
    {
        aDAO.insertAuthToken(authToken);
        AuthToken compareTest = aDAO.getAuthToken(authToken.getAuthToken());
        assertNotNull(compareTest);
        assertEquals(authToken, compareTest);
    }

    @Test
    @DisplayName("Retrieval Fail")
    public void retrieveFail()
    {
        assertThrows(DataAccessException.class, () -> aDAO.getAuthToken(authToken.getAuthToken()));
    }

    @Test
    @DisplayName("Clear")
    public void clearPass() throws DataAccessException
    {
        aDAO.insertAuthToken(authToken);
        authToken.setAuthToken("iscuWBD45");
        aDAO.insertAuthToken(authToken);
        assertEquals(2, aDAO.clearAuthTokenData());
        assertThrows(DataAccessException.class, () -> aDAO.getAuthToken(authToken.getAuthToken()));
    }

    @Test
    @DisplayName("Exists Pass")
    public void existsPass() throws DataAccessException
    {
        aDAO.insertAuthToken(authToken);
        assertTrue(aDAO.authTokenExists(authToken.getAuthToken()));
    }

    @Test
    @DisplayName("Exists Fail")
    public void existsFail()
    {
        assertFalse(aDAO.authTokenExists(authToken.getAuthToken()));
    }
}
