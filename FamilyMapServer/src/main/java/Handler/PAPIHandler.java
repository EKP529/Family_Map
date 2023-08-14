package Handler;

import DataAccess.DataAccessException;
import RequestResult.PAPIRequest;
import RequestResult.PAPIResult;
import Service.PAPIService;
import Service.Service;
import byu.cs240.android.familymapshared.ObjectCoder;

import java.io.*;
import com.sun.net.httpserver.*;
import java.net.HttpURLConnection;

public class PAPIHandler implements HttpHandler
{
    private final ObjectCoder oc = new ObjectCoder();
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        System.out.println("Starting Person API");
        boolean success = false;
        PAPIResult result = new PAPIResult("Error: invalid request data", false);
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
                        PAPIRequest request = new PAPIRequest(null, username);
                        String param = exchange.getRequestURI().toString().substring(1); //taking out the first '/'
                        if (param.contains("/"))
                        {
                            String personID = param.substring(param.indexOf('/') + 1);
                            request.setPersonID(personID);
                        }
                        result = new PAPIService().personAPI(request);
                        if (result.isSuccess())
                        {
                            System.out.println("Successful Person API.\nSending report to client");
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                            resBody = exchange.getResponseBody();
                            String resString = oc.fromPAPIResult(result);
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
                String resString = oc.fromPAPIResult(result);
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
            String resString = oc.fromPAPIResult(result);
            oc.writeString(resString, resBody);
            resBody.close();
            System.out.println("Report sent to client.");
            System.out.println(ex.getMessage() + "\n\n");
        }
    }
}
