package TVUpdates;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;

public class InformationFromWeb {
	Map<String, List<String>> dateVsShows=new HashMap<>();
	String month="06",year="2020";
	String urlLink="";
	boolean thisMonthCompleted=true;
	public void getTVandMovieUpdates() throws ParseException, GoogleJsonResponseException, GeneralSecurityException, IOException{
		getCurrentMonthsUpdate();
		while(true) {
			//If it is First of the month, it updates the hashmap with new shows of that month
				if(getTodaysDate()&&thisMonthCompleted) {		
				getCurrentMonthsUpdate();
				thisMonthCompleted=false;
			}
				else
					thisMonthCompleted=true;
			DateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
			Date date = new Date();
			String todaysDate=(String)dateFormat.format(date).toLowerCase();
			if(dateVsShows.containsKey(todaysDate)) {
				SendAlert alert=new SendAlert();
				alert.sendNow(dateVsShows,todaysDate);
				dateVsShows.remove(todaysDate);
			}
		}
			
	}

	private void getCurrentMonthsUpdate() throws ParseException {
		urlLink="http://www.thefutoncritic.com/listings/"+year+"/"+month+"/premieres/";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(urlLink);
		CloseableHttpResponse response = null;
		try {
			response=httpClient.execute(httpget);
			if(response.getStatusLine().getStatusCode()== HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
		        if (entity != null) {
		            String result = EntityUtils.toString(entity);
		            getListOfShows(result);
		        }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean getTodaysDate() {
		DateFormat dateFormat = new SimpleDateFormat("d", Locale.ENGLISH);
		Date date= new Date();
		String todaysDate=(String)dateFormat.format(date).toLowerCase();
		if(todaysDate.contentEquals("01")) {
			DateFormat monthFormat = new SimpleDateFormat("MM", Locale.ENGLISH);
			Date thisMonth= new Date();
			String newMonth=(String)monthFormat.format(thisMonth).toLowerCase();
			month=newMonth;
			DateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
			Date thisYear= new Date();
			String newYear=(String)yearFormat.format(thisYear).toLowerCase();
			year=newYear;
			return true;
		}
		return false;
	}

	public void getListOfShows(String showsHTML) throws ParseException {
		String monday=".*monday, .* [0-9][0-9], [0-9][0-9][0-9][0-9].*";
		String tuesday="tuesday, .* [0-9][0-9], [0-9][0-9][0-9][0-9]";
		String wednesday=".*wednesday, .* [0-9][0-9], [0-9][0-9][0-9][0-9].*";
		String thursday=".*thursday, .* [0-9][0-9], [0-9][0-9][0-9][0-9].*";
		String friday=".*friday, .* [0-9][0-9], [0-9][0-9][0-9][0-9].*";
		String saturday=".*saturday, .* [0-9][0-9], [0-9][0-9][0-9][0-9].*";
		String sunday=".*sunday, .* [0-9][0-9], [0-9][0-9][0-9][0-9].*";
		String newDate = "";
		String header="";
		String lines[] = showsHTML.split("\\r?\\n");
		int lineNumber=0;
		String show="";
		Date day=null;
		List<String> shows=new ArrayList<>();
		for(String line: lines) {
			lineNumber++;
			if(line.matches(monday)||line.matches(tuesday)||line.matches(wednesday)||line.matches(thursday)||line.matches(friday)||line.matches(saturday)||line.matches(sunday)) {
				newDate=line.substring(line.indexOf("day,")+4,line.indexOf("]"));
				shows=new ArrayList<>();
				newDate=newDate.trim();						
				dateVsShows.put(newDate, shows);
			}
			if(line.contains("APPLE TV+")) {
				header="APPLE TV+";
			}
			if(line.contains("NETFLIX")) {
				header="NETFLIX";
			}
			if(line.contains("AMAZON")){
				header="AMAZON";
			}
			if(header.length()!=0) {
				header = addShowToMap(dateVsShows, header, lines, lineNumber, newDate);
			}
		}
	
	}
	private String addShowToMap(Map<String, List<String>> dateVsShows, String header, String[] lines, int lineNumber,
			String newDate) {
		String show;
		List<String> shows;
		show=lines[lineNumber];
		show=show.substring(show.indexOf("listings/\">")+11,show.indexOf("</a>"));
		//show=header+":"+show;
		if(show.indexOf('(')!=-1)
			show=show.substring(0,show.indexOf('('));
		shows=dateVsShows.get(newDate);
		shows.add(show);
		dateVsShows.put(newDate, shows);
		header="";
		return header;
	}
	
}
