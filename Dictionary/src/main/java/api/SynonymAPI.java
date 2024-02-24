package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.example.settings.InternetConnect;
import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SynonymAPI {
    public static JSONObject getSynonymList(String wordForm) {
        try {
            URL url = new URL("https://api.api-ninjas.com/v1/thesaurus?word=" + URLEncoder.encode(wordForm, StandardCharsets.UTF_8).replace("+", "%20"));
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.setRequestProperty("X-API-Host", "api.api-ninjas.com");
            request.setRequestProperty("X-API-Key", "GmbDQxVvvZW0zOi4WuQwOD2slfCbPUnVK2H46QhN");
            request.setRequestMethod("GET");
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = inputStream.readLine()) != null) {
                response.append(inputLine);
            }
            inputStream.close();
            return new JSONObject(StringEscapeUtils.unescapeHtml4(response.toString()));
        } catch (IOException e) {
            return new JSONObject("{\"synonyms\":[],\"antonyms\":[]}");
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println(getSynonymList("bright"));
    }

}