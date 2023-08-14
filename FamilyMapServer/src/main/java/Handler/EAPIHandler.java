package Handler;

import DataAccess.DataAccessException;
import RequestResult.EAPIRequest;
import RequestResult.EAPIResult;
import Service.EAPIService;
import Service.Service;
import byu.cs240.android.familymapshared.ObjectCoder;

import java.io.*;
import com.sun.net.httpserver.*;
import java.net.HttpURLConnection;
public class EAPIHandler implements HttpHandler
{
    private final ObjectCoder oc = new ObjectCoder();
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        System.out.println("Starting Event API");
        boolean success = false;
        EAPIResult result = new EAPIResult("Error: invalid request data", false);
        OutputStream resBody;
        try
        {
            if (exchange.getRequestMethod().equalsIgnoreCase("get"))
            {
                System.out.println("Evaluating GET request");
                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization"))
                {
                    String authToken = reqHeaders.getFirst("Authorization");
                    if (!Service.checkConnection()) { Service.setConnection(); }
                    if (Service.myAuthTokenDAO.authTokenExists(authToken))
                    {
                        String username = Service.myAuthTokenDAO.getAuthToken(authToken).getUsername();
                        EAPIRequest request = new EAPIRequest(null, username);
                        String param = exchange.getRequestURI().toString().substring(1); //taking out the first '/'
                        if (param.contains("/"))
                        {
                            String eventID = param.substring(param.indexOf('/') + 1);
                            request.setEventID(eventID);
                        }
                        result = new EAPIService().eventAPI(request);
                        if (result.isSuccess())
                        {
                            System.out.println("Successful Event API.\nSending report to client");
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                            resBody = exchange.getResponseBody();
                            String resString = oc.fromEAPIResult(result);
                            oc.writeString(resString, resBody);
                            resBody.close();
                            success = true;
                            System.out.println("Report sent to client.\n\n");
                        }
                    }
                }
            }
            if (!success)
            {
                if (Service.checkConnection()) { Service.closeConnection(false); }
                System.out.println("Received bad request.\nSending report to client");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                resBody = exchange.getResponseBody();
                String resString = oc.fromEAPIResult(result);
                oc.writeString(resString, resBody);
                resBody.close();
                System.out.println("Report sent to client.\n\n");
            }
        }
        catch (IOException | DataAccessException ex)
        {
            System.out.println("Internal issue.\nSending report to client");
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            resBody = exchange.getResponseBody();
            String resString = oc.fromEAPIResult(result);
            oc.writeString(resString, resBody);
            resBody.close();
            System.out.println("Report sent to client.");
            System.out.println(ex.getMessage() + "\n\n");
        }
    }
}
