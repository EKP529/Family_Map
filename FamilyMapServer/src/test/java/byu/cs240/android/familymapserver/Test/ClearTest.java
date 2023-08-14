package byu.cs240.android.familymapserver.Test;

import DataAccess.DataAccessException;
import byu.cs240.android.familymapshared.ObjectCoder;
import RequestResult.ClearResult;
import RequestResult.RegisterRequest;
import Service.RegisterService;
import Service.ClearService;
import Service.Service;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ClearTest
{
    @BeforeEach
    public void setUp() throws DataAccessException
    {
        RegisterRequest request = new RegisterRequest("susan", "mysecret", "susan@gmail.com",
                "Susan", "Ellis", "f");
        ObjectCoder oc = new ObjectCoder();
        new RegisterService().register(request, oc.toNames(), oc.toLocations());
    }

    @Test
    @DisplayName("Standard Clear")
    public void registerPassTest() throws DataAccessException
    {
        ClearResult result = new ClearService().clear();
        assertNotNull(result);
        assertTrue(result.isSuccess());
        Service.setConnection();
        assertThrows(DataAccessException.class, () -> Service.myPersonDAO.getAllPeople("susan"));
        assertThrows(DataAccessException.class, () -> Service.myEventDAO.getAllEvents("susan"));
        assertThrows(DataAccessException.class, () -> Service.myUserDAO.getUser("susan"));
        assertThrows(DataAccessException.class, () -> Service.myAuthTokenDAO.getAuthToken("susan"));
        Service.closeConnection(false);
    }

    @Test
    @DisplayName("Empty Clear")
    public void registerPassTest2() throws DataAccessException
    {
        registerPassTest();
        registerPassTest();
    }
}
