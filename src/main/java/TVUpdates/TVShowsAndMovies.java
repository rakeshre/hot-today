package TVUpdates;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;

public class TVShowsAndMovies {
	public static void main(String args[]) throws ParseException, GoogleJsonResponseException, GeneralSecurityException, IOException {
		InformationFromWeb ifw=new InformationFromWeb();
		ifw.getTVandMovieUpdates();
	}

}
