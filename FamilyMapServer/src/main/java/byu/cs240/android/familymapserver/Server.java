package byu.cs240.android.familymapserver;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import Handler.*;

public class Server
{
    private static final int MAX_WAITING_CONNECTIONS = 12;
    private HttpServer server;
    private void run(String portNumber)
    {
        System.out.println("Initializing HTTP byu.cs240.android.familymapserver.Server");
        try
        {
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        server.setExecutor(null);
        System.out.println("Creating contexts");
        server.createContext("/clear", new ClearHandler());
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/fill/", new FillHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/person", new PAPIHandler());
        server.createContext("/event", new EAPIHandler());
        server.createContext("/", new FileHandler());
        System.out.println("Starting server");
        server.start();
        System.out.println("byu.cs240.android.familymapserver.Server started\n\n");
    }
    public static void main(String[] args)
    {
        String portNumber = args[0];
        new Server().run(portNumber);
    }
}
