package byu.cs240.android.familymapshared;

import Model.EventData;
import Model.Name;
import RequestResult.ClearResult;
import RequestResult.EAPIResult;
import RequestResult.FillRequest;
import RequestResult.FillResult;
import RequestResult.LoadRequest;
import RequestResult.LoadResult;
import RequestResult.LoginRequest;
import RequestResult.LoginResult;
import RequestResult.PAPIRequest;
import RequestResult.PAPIResult;
import RequestResult.RegisterRequest;
import RequestResult.RegisterResult;

import com.google.gson.*;
import java.io.*;

public class ObjectCoder
{
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public Name toNames()
    {
        try
        {
            Name names = new Name();
            Name data = gson.fromJson(new FileReader("json/fnames.json"), Name.class);
            names.setFemaleNames(data.getData());
            data = gson.fromJson(new FileReader("json/mnames.json"), Name.class);
            names.setMaleNames(data.getData());
            data = gson.fromJson(new FileReader("json/snames.json"), Name.class);
            names.setSurnames(data.getData());
            return names;
        }
        catch (FileNotFoundException e) { return null; }
    }
    public EventData toLocations()
    {
        try { return gson.fromJson(new FileReader("json/locations.json"), EventData.class); }
        catch (FileNotFoundException e) { return null; }
    }
    public LoadRequest toLoadRequest(String jsonString)
    {
        return gson.fromJson(jsonString, LoadRequest.class);
    }
    public LoginRequest toLoginRequest(String jsonString)
    {
        return gson.fromJson(jsonString, LoginRequest.class);
    }
    public RegisterRequest toRegisterRequest(String jsonString)
    {
        return gson.fromJson(jsonString, RegisterRequest.class);
    }
    public LoadResult toLoadResult(String jsonString)
    {
        return gson.fromJson(jsonString, LoadResult.class);
    }
    public LoginResult toLoginResult(String jsonString)
    {
        return gson.fromJson(jsonString, LoginResult.class);
    }
    public RegisterResult toRegisterResult(String jsonString)
    {
        return gson.fromJson(jsonString, RegisterResult.class);
    }
    public PAPIResult toPAPIResult(String jsonString)
    {
        return gson.fromJson(jsonString, PAPIResult.class);
    }
    public EAPIResult toEAPIResult(String jsonString)
    {
        return gson.fromJson(jsonString, EAPIResult.class);
    }
    public String fromRegisterResult(RegisterResult result)
    {
        return gson.toJson(result);
    }
    public String fromLoginResult(LoginResult result)
    {
        return gson.toJson(result);
    }
    public String fromLoadResult(LoadResult result) { return gson.toJson(result); }
    public String fromClearResult(ClearResult result) { return gson.toJson(result); }
    public String fromFillResult(FillResult result) { return gson.toJson(result); }
    public String fromEAPIResult(EAPIResult result) { return gson.toJson(result); }
    public String fromPAPIResult(PAPIResult result) { return gson.toJson(result); }
    public String fromRegisterRequest(RegisterRequest request) { return gson.toJson(request); }
    public String fromLoginRequest(LoginRequest request) { return gson.toJson(request); }
    public String fromLoadRequest(LoadRequest request) { return gson.toJson(request); }
    public String fromFillRequest(FillRequest request) { return gson.toJson(request); }
    public String fromEAPIRequest(EAPIResult request) { return gson.toJson(request); }
    public String fromPAPIRequest(PAPIRequest request) { return gson.toJson(request); }
    public String readString(InputStream is) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }
    public void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
