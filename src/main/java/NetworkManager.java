import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.Arrays;

public class NetworkManager {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient httpClient = new OkHttpClient();

    public NetworkManager() {
        System.out.println("Network Manager started!");
    }

    //GET INIT Token
    public String getInitTokenRequest(String baseUrl)  {
        Request request = new Request.Builder().url(baseUrl).build();
        Response response = null;

        try {
            response = httpClient.newCall(request).execute();
            JSONObject obj = new JSONObject(response.body().string());
            return obj.getString("token");
        } catch (IOException e) {
            System.out.println("Init Token not Possible to Load!");
            e.printStackTrace();
        } catch (JSONException e) {
            System.out.println("Init Token not Possible to Load!");
            e.printStackTrace();
        }

        return "NOT FOUND!";
    }

    //GET Task
    public JSONObject getTaskFromStage(String baseUrl)  {
        Request request = new Request.Builder().url(baseUrl).build();
        Response response = null;

        try {
            response = httpClient.newCall(request).execute();
            JSONObject obj = new JSONObject(response.body().string());
            response.close();
            return obj;
        } catch (IOException e) {
            System.out.println("Not Possible For: " + baseUrl);
            e.printStackTrace();
        } catch (JSONException e) {
            System.out.println("Not Possible For: " + baseUrl);
            e.printStackTrace();
        }

        return new JSONObject();
    }

    //GET Finsih
    public String getFinishTask(String baseUrl)  {
        Request request = new Request.Builder().url(baseUrl).build();
        Response response = null;

        try {
            response = httpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return "";
    }

    //POST Task
    public JSONObject postStageOneTestCase(String baseUrl, JSONObject parameters)  {

        System.out.println(parameters.toString());
        RequestBody body = RequestBody.create(JSON, parameters.toString()); //"{\"answers\": [{\"questionId\": 0, \"reachable\": true}]}"
        Request request = new Request.Builder().url(baseUrl).post(body).build();

        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            String responseString = response.body().string();
            System.out.println("Post: " + responseString); // To Logging
            JSONObject obj = new JSONObject(responseString);
            return obj;
        } catch (IOException e) {
            System.out.println("Task for: " + baseUrl + " and: " + parameters + "Not Possobile!");
            e.printStackTrace();
        } catch (JSONException e) {
            System.out.println("Task for: " + baseUrl + " and: " + parameters + "Not Possobile!");
            e.printStackTrace();
        }

        return new JSONObject();
    }

    //GET PDF
    public boolean loadPdfFile(String baseUrl, int stage) {
        Request request = new Request.Builder().url(baseUrl).build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Failed to download file: " + response);
            }

            FileOutputStream fos = new FileOutputStream(String.format("/Users/itsedev/Documents/04_Java/Individual/51824673/pdfs/stage%s.pdf", stage));
            fos.write(response.body().bytes());
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}

