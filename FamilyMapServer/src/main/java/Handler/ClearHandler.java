package Handler;

import DataAccess.DataAccessException;
import RequestResult.ClearResult;
import byu.cs240.android.familymapshared.ObjectCoder;
import Service.ClearService;
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.HttpURLConnection;

public class ClearHandler implements HttpHandler
{
    private final ObjectCoder oc = new ObjectCoder();
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        System.out.println("Starting Clear");
        boolean success = false;
        ClearResult result = new ClearResult("Error: invalid request data", false);
        OutputStream resBody;
        try
        {
            if (exchange.getRequestMethod().equalsIgnoreCase("post"))
            {
                System.out.println("Evaluating POST request");
                result = new ClearService().clear();
                if (result.isSuccess())
                {
                    System.out.println("Successful clear.\nSending report to client");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    resBody = exchange.getResponseBody();
                    String resString = oc.fromClearResult(result);
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
                String resString = oc.fromClearResult(result);
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
            String resString = oc.fromClearResult(result);
            oc.writeString(resString, resBody);
            resBody.close();
            System.out.println("Report sent to client.");
            System.out.println(ex.getMessage() + "\n\n");
        }
    }
}
