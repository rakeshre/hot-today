package TVUpdates;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;

public class SendAlert {

	public void sendNow(Map<String, List<String>> dateVsShows, String todaysDate) throws GoogleJsonResponseException, GeneralSecurityException, IOException {
		String messageContent="";
		StringBuilder sb=new StringBuilder(messageContent);
		List<String> listOFShowsToday=dateVsShows.get(todaysDate);
		YouTubeLinks link=new YouTubeLinks();
		for(String show:listOFShowsToday) {
			sb.append(show).append(":");
			sb.append(link.getVideo(show)).append("\n");
		}/*
		for(String s:dateVsShows.keySet()) {
			List<String> listOFShowsToday=dateVsShows.get(s);
			YouTubeLinks link=new YouTubeLinks();
			for(String show:listOFShowsToday) {
				//sb.append(show).append("\n");
				sb.append(link.getVideo(show));
			}
		}*/
		TwillioUpdates twillio=new TwillioUpdates();
		twillio.sendSMS(sb.toString());
	}
}
