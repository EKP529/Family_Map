package Handler;

import com.sun.net.httpserver.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
public class FileHandler implements HttpHandler
{
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        boolean success = false;
        try
        {
            if (exchange.getRequestMethod().equalsIgnoreCase("get"))
            {
                String filePath = "web" + exchange.getRequestURI().toString();
                File file = new File(filePath);
                if (file.exists())
                {
                    if (filePath.equals("web/favicon.ico") || filePath.equals("web/favicon.jpg")
                            || filePath.equals("web/css/main.css") || filePath.equals("web/img/background.png"))
                    {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        OutputStream respBody = exchange.getResponseBody();
                        Files.copy(file.toPath(), respBody);
                        respBody.close();
                    }
                    else
                    {
                        file = new File("web/index.html");
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        OutputStream respBody = exchange.getResponseBody();
                        Files.copy(file.toPath(), respBody);
                        respBody.close();
                    }
                }
                else
                {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    file = new File("web/HTML/404.html");
                    OutputStream respBody = exchange.getResponseBody();
                    Files.copy(file.toPath(), respBody);
                    respBody.close();

                }
                success = true;
            }
            if (!success)
            {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }
        }
        catch(IOException ex)
        {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            exchange.getResponseBody().close();
            ex.printStackTrace();
        }
    }
}
