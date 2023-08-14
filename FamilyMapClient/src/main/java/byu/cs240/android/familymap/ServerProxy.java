package byu.cs240.android.familymap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import RequestResult.*;
import byu.cs240.android.familymapshared.ObjectCoder;

public class ServerProxy
{

    private static String serverHost;
    private static String serverPort;
    private final ObjectCoder oc = new ObjectCoder();
    private final DataCache dataCache = DataCache.getInstance();

    public LoginResult login(LoginRequest request)
    {
        try
        {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            String reqData = oc.fromLoginRequest(request);
            OutputStream reqBody = http.getOutputStream();
            oc.writeString(reqData, reqBody);
            reqBody.close();
            http.connect();
            LoginResult result;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream resBody = http.getInputStream();
                String resData = oc.readString(resBody);
                result = oc.toLoginResult(resData);
                dataCache.authenToken = result.getAuthtoken();
                dataCache.personID = result.getPersonID();
                dataCache.username = result.getUsername();
            }
            else
            {
                InputStream resBody = http.getInputStream();
                String resData = oc.readString(resBody);
                result = oc.toLoginResult(resData);
                System.out.println("Error: " + result.getMessage());
            }
            return result;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return new LoginResult("", false);
        }
    }
    public RegisterResult register(RegisterRequest request)
    {
        try
        {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            String reqData = oc.fromRegisterRequest(request);
            OutputStream reqBody = http.getOutputStream();
            oc.writeString(reqData, reqBody);
            reqBody.close();
            http.connect();
            RegisterResult result;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream resBody = http.getInputStream();
                String resData = oc.readString(resBody);
                result = oc.toRegisterResult(resData);
                dataCache.authenToken = result.getAuthtoken();
                dataCache.personID = result.getPersonID();
                dataCache.username = result.getUsername();
            }
            else
            {
                InputStream resBody = http.getInputStream();
                String resData = oc.readString(resBody);
                result = oc.toRegisterResult(resData);
                System.out.println("Error: " + result.getMessage());
            }
            return result;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return new RegisterResult("", false);
        }
    }

    public PAPIResult getPeople()
    {
        try
        {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", dataCache.authenToken);
            http.connect();
            PAPIResult result;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream resBody = http.getInputStream();
                String resData = oc.readString(resBody);
                result = oc.toPAPIResult(resData);
                dataCache.people = result.getData();
                dataCache.setPeopleByID();
                dataCache.setPersonFamily();
                String fullName = dataCache.people[0].getFirstName() + " " +
                        dataCache.people[0].getLastName();
                result.setMessage(fullName);
            }
            else
            {
                InputStream resBody = http.getInputStream();
                String resData = oc.readString(resBody);
                result = oc.toPAPIResult(resData);
                System.out.println("Error: " + result.getMessage());
            }
            return result;

        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return new PAPIResult("", false);
        }
    }
    public EAPIResult getEvents()
    {
        try
        {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", dataCache.authenToken);
            http.connect();
            EAPIResult result;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream resBody = http.getInputStream();
                String resData = oc.readString(resBody);
                result = oc.toEAPIResult(resData);
                dataCache.events = result.getData();
                dataCache.setEventsByID();
                dataCache.setGenderedEvents();
            }
            else
            {
                InputStream resBody = http.getInputStream();
                String resData = oc.readString(resBody);
                result = oc.toEAPIResult(resData);
                System.out.println("Error: " + result.getMessage());
            }
            return result;

        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return new EAPIResult("", false);
        }
    }

    public static void setServerHost(String serverHost) {
        ServerProxy.serverHost = serverHost;
    }

    public static void setServerPort(String serverPort) {
        ServerProxy.serverPort = serverPort;
    }
}
