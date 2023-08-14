package Handler;

import DataAccess.DataAccessException;
import RequestResult.RegisterRequest;
import RequestResult.RegisterResult;
import Service.RegisterService;
import byu.cs240.android.familymapshared.ObjectCoder;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.HttpURLConnection;
public class RegisterHandler implements HttpHandler
{
    private final ObjectCoder oc = new ObjectCoder();
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        System.out.println("Starting Registration");
        boolean success = false;
        RegisterResult result = new RegisterResult("Error: invalid request data", false);
        OutputStream resBody;
        try
        {
            if (exchange.getRequestMethod().equalsIgnoreCase("post"))
            {
                System.out.println("Evaluating POST request");
                InputStream reqBody = exchange.getRequestBody();
                String reqData = oc.readString(reqBody);
                RegisterRequest request = oc.toRegisterRequest(reqData);
                result = new RegisterService().register(request, oc.toNames(), oc.toLocations());
                if (result.isSuccess())
                {
                    System.out.println("Successful registration.\nSending report to client");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    resBody = exchange.getResponseBody();
                    String resString = oc.fromRegisterResult(result);
                    oc.writeString(resString, resBody);
                    resBody.close();
                    success = true;
                    System.out.println("Report sent to client.\n\n");
                }
            }
            if (!success)
            {
                System.out.println("Received bad request.\nSending report to client");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                resBody = exchange.getResponseBody();
                String resString = oc.fromRegisterResult(result);
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
            String resString = oc.fromRegisterResult(result);
            oc.writeString(resString, resBody);
            resBody.close();
            System.out.println("Report sent to client.");
            System.out.println(ex.getMessage() + "\n\n");
        }
    }
}
