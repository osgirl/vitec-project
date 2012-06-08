package fr.vitec.site.cinemotion;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.vitec.batch.extension.FindFilmInfo;
import fr.vitec.fmk.string.StringOperations;
import fr.vitec.model.BasicsInfos;
import fr.vitec.site.cinemotion.connection.SiteConnection;
import fr.vitec.site.cinemotion.connection.SitePropertiesManager;

public class FindFilmInfoFromCinemotion extends FindFilmInfo {
	
	private static final String CINEMOTION = "Cinémotion";
	public static final String WWW_CINEMOTIONS = "www.cinemotions.com";
	private SiteConnection siteConnection;
	private SitePropertiesManager propertiesManager;

	public FindFilmInfoFromCinemotion() {
		propertiesManager = new SitePropertiesManager("cinemotions");
		siteConnection = new SiteConnection(WWW_CINEMOTIONS, propertiesManager);
	}

	@Override
	public String getSiteName() {
		return CINEMOTION;
	}

	private String transfo2(String motCle) {
		return StringOperations.clean(motCle, "-");
	}
	
	@Override
	public List<BasicsInfos> sendRequestAndGetBasicsInfos(String filmName) {
		
		String fullUrl = "/recherche/"+this.transfo2(filmName)+".html";
		String header = 
			"GET "+fullUrl+" HTTP/1.1\n"
			+"Connection: close\n"
			+"Host: "+WWW_CINEMOTIONS+"\n"
			+"User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; fr; rv:1.8.1.11) Gecko/20071127 Firefox/2.0.0.11\n"
			+"Content-Type: application/x-www-form-urlencoded\n"
			//+"Cookie: users_resolution=1024x768; users_resolution=1024x768; PHPSESSID="+sessionID+"\n"
			+"Content-Length: "+20+"\n\n"
			+"HTTP/1.1 200 OK\n"
			+"Server: Apache\n";
		
			return siteConnection.sendRequestAndGetBasicsInfosOnRequest(header);

		
	}

	@Override
	public BasicsInfos fillFullInfos(BasicsInfos info){
		String header = 
			"GET "+info.getReference()+" HTTP/1.1\n"
			+"Connection: close\n"
			+"Host: "+WWW_CINEMOTIONS+"\n"
			+"User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; fr; rv:1.8.1.11) Gecko/20071127 Firefox/2.0.0.11\n"
			+"Content-Type: application/x-www-form-urlencoded\n"
			+"Content-Length: "+20+"\n\n"
			+"HTTP/1.1 200 OK\n"
			+"Server: Apache\n";
		//sleep();
		info = siteConnection.fillFullInfos(info, header);
		
		if(info.getInfo(BasicsInfos.SYNOPSIS)==null){

			StringBuffer line = siteConnection.getAllLinesInOne();
			//System.out.println(line);
			String synRegExp = propertiesManager.getValue("synopsis.multilignes");
			Pattern p = Pattern.compile(synRegExp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			Matcher m = p.matcher(line);
			if(m.find()){
				info.AddInfo("synopsis", m.group(1));
			}
		}
		
		return info;
	}

}
