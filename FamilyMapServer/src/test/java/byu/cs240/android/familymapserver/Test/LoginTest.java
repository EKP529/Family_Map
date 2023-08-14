package byu.cs240.android.familymapserver.Test;

import DataAccess.DataAccessException;
import Model.AuthToken;
import Model.User;
import RequestResult.LoginRequest;
import RequestResult.LoginResult;
import Service.ClearService;
import Service.LoginService;
import Service.Service;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest
{
    private AuthToken authToken;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        new ClearService().clear();
        User user = new User("susan", "mysecret", "susan@gmail.com",
                "Susan", "Ellis", "f", "personID1");
        Service.setConnection();
        Service.myUserDAO.insertUser(user);
        authToken = new AuthToken("susan");
        Service.myAuthTokenDAO.insertAuthToken(authToken);
        Service.closeConnection(true);
    }

    @Test
    @DisplayName("Login Pass")
    public void loginPassTest() throws DataAccessException
    {
        LoginResult result = new LoginService().login(new LoginRequest("susan", "mysecret"));
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("susan", result.getUsername());
        assertEquals("personID1", result.getPersonID());
        assertNotEquals(authToken.getAuthToken(), result.getAuthtoken());
    }

    @Test
    @DisplayName("Multiple Logins")
    public void loginPassTest2() throws DataAccessException
    {
        LoginResult result1 = new LoginService().login(new LoginRequest("susan", "mysecret"));
        assertNotNull(result1);
        assertTrue(result1.isSuccess());
        assertEquals("susan", result1.getUsername());
        assertEquals("personID1", result1.getPersonID());
        LoginResult result2 = new LoginService().login(new LoginRequest("susan", "mysecret"));
        assertNotNull(result2);
        assertTrue(result2.isSuccess());
        assertEquals("susan", result2.getUsername());
        assertEquals("personID1", result2.getPersonID());
        assertNotEquals(result1.getAuthtoken(), result2.getAuthtoken());
        assertNotEquals(authToken.getAuthToken(), result1.getAuthtoken());
        assertNotEquals(authToken.getAuthToken(), result2.getAuthtoken());
        Service.setConnection();
        assertTrue(Service.myAuthTokenDAO.authTokenExists(result1.getAuthtoken()));
        assertTrue(Service.myAuthTokenDAO.authTokenExists(result2.getAuthtoken()));
        Service.closeConnection(false);
    }

    @Test
    @DisplayName("Invalid Request Data")
    public void loginFailTest() throws DataAccessException
    {
        LoginResult result = new LoginService().login(new LoginRequest("chad", "secret"));
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("Error: Error occurred while getting user", result.getMessage());

        result = new LoginService().login(new LoginRequest("susan", "secret"));
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("Error: Error occurred while getting user", result.getMessage());

        result = new LoginService().login(new LoginRequest("chad", "mysecret"));
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("Error: Error occurred while getting user", result.getMessage());
    }


}
