package TVUpdates;

import org.json.*;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;

public class YouTubeLinks {

    private final String DEVELOPER_KEY = "****************************";

    private final String APPLICATION_NAME = "API code samples";
    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * Build and return an authorized API client service.
     *
     * @return an authorized API client service
     * @throws GeneralSecurityException, IOException
     */
    public YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
            .setApplicationName(APPLICATION_NAME)
            .build();
    }

    /**
     * Call function to create API service object. Define and
     * execute API request. Print API response.
     *
     * @throws GeneralSecurityException, IOException, GoogleJsonResponseException
     */
    public String getVideo(String content)
        throws GeneralSecurityException, IOException, GoogleJsonResponseException {
    	try {
    	content=content+" trailer";
        YouTube youtubeService = getService();
        YouTube.Search.List request = youtubeService.search()
            .list("snippet");
        SearchListResponse response = request.setKey(DEVELOPER_KEY)
            .setMaxResults(1L)
            .setOrder("relevance")
            .setQ(content)
            .setType("video")
            .execute();
        return (getURL(response.toString()));
    	}catch(Exception e) {
    		System.out.println(e);
    		return "";
    	}
    }
    
    public String getURL(String response) {
    	JSONObject obj = new JSONObject(response);
    	JSONArray arr = obj.getJSONArray("items");
    	String convertToString=arr.toString();
    	convertToString=convertToString.substring(1,convertToString.length()-1);
    	JSONObject getId= new JSONObject(convertToString);
    	String idTag = getId.get("id").toString();
    	JSONObject getVideoId= new JSONObject(idTag);
    	String videoID = getVideoId.get("videoId").toString();
    	System.out.println(videoID);
    	return "https://www.youtube.com/watch?v="+videoID+"\n";
    }
    
}