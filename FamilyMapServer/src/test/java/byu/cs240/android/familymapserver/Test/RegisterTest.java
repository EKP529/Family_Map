package byu.cs240.android.familymapserver.Test;

import DataAccess.DataAccessException;
import byu.cs240.android.familymapshared.ObjectCoder;
import Model.AuthToken;
import Model.Person;
import Model.User;
import RequestResult.RegisterRequest;
import RequestResult.RegisterResult;
import Service.RegisterService;
import Service.ClearService;
import Service.Service;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest
{
    private RegisterRequest request;
    private RegisterResult result;
    private final ObjectCoder oc = new ObjectCoder();

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        String jsonString = "{\n" +
                "\n" +
                "        \"username\":\"susan\",                // string\n" +
                "\n" +
                "        \"password\":\"mysecret\",        // string\n" +
                "\n" +
                "        \"email\":\"susan@gmail.com\",        // string                \n" +
                "\n" +
                "        \"firstName\":\"Susan\",                // string\n" +
                "\n" +
                "        \"lastName\":\"Ellis\",                // string\n" +
                "\n" +
                "\"gender\":\"f\"                        // string: \"f\" or \"m\"\n" +
                "\n" +
                "}";
        request = oc.toRegisterRequest(jsonString);
        result = null;
        new ClearService().clear();
    }

    @AfterEach
    public void tearDown() throws DataAccessException
    {
        new ClearService().clear();
    }

    @Test
    @DisplayName("Register Pass")
    public void registerPassTest() throws DataAccessException
    {
        result = new RegisterService().register(request, oc.toNames(), oc.toLocations());
        assertNotNull(result);
        assertTrue(result.isSuccess());
        Service.setConnection();
        User user = Service.myUserDAO.getUser("susan", "mysecret");
        assertNotNull(user);
        assertEquals("susan", user.getUsername());
        Person person = Service.myPersonDAO.getPerson(user.getPersonID());
        assertNotNull(person);
        assertEquals(user.getPersonID(), person.getPersonID());
        AuthToken authToken = Service.myAuthTokenDAO.getAuthToken("susan");
        assertNotNull(authToken);
        assertEquals("susan", authToken.getUsername());
        Service.closeConnection(false);
    }

    @Test
    @DisplayName("Register Fail")
    public void registerFailTest() throws DataAccessException
    {
        result = new RegisterService().register(request, oc.toNames(), oc.toLocations());
        result = new RegisterService().register(request, oc.toNames(), oc.toLocations());
        assertNotNull(result);
        assertFalse(result.isSuccess());
    }
}
