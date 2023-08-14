package Handler;

import DataAccess.DataAccessException;
import RequestResult.FillRequest;
import RequestResult.FillResult;
import Service.FillService;
import byu.cs240.android.familymapshared.ObjectCoder;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.HttpURLConnection;

public class FillHandler implements HttpHandler
{
    private final ObjectCoder oc = new ObjectCoder();
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        System.out.println("Starting Fill");
        boolean success = false;
        FillResult result = new FillResult("Error: invalid request data", false);
        OutputStream resBody;
        try
        {
            if (exchange.getRequestMethod().equalsIgnoreCase("post"))
            {
                System.out.println("Evaluating POST request");
                String params = exchange.getRequestURI().toString().substring(6); // taking out "/fill/"
                FillRequest request;
                if (!params.contains("/"))
                {
                    request = new FillRequest(params);
                }
                else
                {
                    String username = params.substring(0, params.indexOf('/'));
                    int generations = Integer.parseInt(params.substring(params.indexOf('/') + 1));
                    request = new FillRequest(username, generations);
                }
                result = new FillService().fill(request, oc.toNames(), oc.toLocations());
                if (result.isSuccess())
                {
                    System.out.println("Successful fill.\nSending report to client");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    resBody = exchange.getResponseBody();
                    String resString = oc.fromFillResult(result);
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
                String resString = oc.fromFillResult(result);
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
            String resString = oc.fromFillResult(result);
            oc.writeString(resString, resBody);
            resBody.close();
            System.out.println("Report sent to client.");
            System.out.println(ex.getMessage() + "\n\n");
        }
    }
}
