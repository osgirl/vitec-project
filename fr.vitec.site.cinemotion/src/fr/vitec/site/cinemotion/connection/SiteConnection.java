package fr.vitec.site.cinemotion.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.vitec.model.BasicsInfos;
import fr.vitec.site.cinemotion.CinemotionBasicsInfos;



public class SiteConnection extends SocketConnection{

	protected static final String PARAMETERS = "#PARAMETERS#";
	protected static final String REQ_LENGTH = "#REQ_LENGTH#";
	private static final String chunkExpr = "^[A-Fa-f0-9]+$";
	private static final Pattern patternChunk = Pattern.compile(chunkExpr, Pattern.CASE_INSENSITIVE);

	private static final String transferEncodChunkedExpr = "^Transfer-Encoding: *chunked$";
	private static final Pattern patternTransferEncodChunked = Pattern.compile(transferEncodChunkedExpr, Pattern.CASE_INSENSITIVE);

	protected interface Mode{
		public static final int MODE_FULL = 0;
		public static final int MODE_ONE = 1;
		public static final int MODE_TWO = 2;
	}

	protected SitePropertiesManager propertiesManager = null;

	private StringBuffer allLinesInOne = null;
	private int lastEndPosMatching = 0;

	public SiteConnection(String adr, SitePropertiesManager propertiesManager){
		super(adr);
		this.propertiesManager = propertiesManager;
	}


	protected void logout(String s) {
	}
	/*===============================================================================*/
	public List<BasicsInfos> sendRequestAndGetBasicsInfosOnRequest(String header){
		this.initSocket();

		BufferedReader br = sendRequest(header);
		List<BasicsInfos>lbasicsInfos = null;
		String currentLine = "";
		String precedentLine = "";
		boolean chunked = false;
		StringBuffer sbuf = new StringBuffer("");
		try {
			boolean eof = false;
			while(!eof && br!=null){
				currentLine = br.readLine();

				if(currentLine!=null){

					if(!chunked){
						chunked = isChunked(currentLine);
					}
					if(chunked){
						currentLine = traiteChunk(currentLine, precedentLine, br);
					}
					sbuf.append(currentLine);
					precedentLine = currentLine;
					logout(currentLine);

				}
				else{
					eof=true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		lbasicsInfos = processForBasicInfo(sbuf);

		this.closeSocket();
		return lbasicsInfos;		
	}




	private List<BasicsInfos> processForBasicInfo(StringBuffer sbuf) {
		List<BasicsInfos> lbasicsInfos = new ArrayList<BasicsInfos>();
		BasicsInfos bi = null;
		String currentSep="";
		String str = sbuf.toString();
		
		int res = matchReponses(str);//positionne lastEndPosMatching
		
		//currentSep = matchExpression(str, "separateur", getLastEndPosMatching());
		if(res != 0){
			
			String[] films = split(str, "separateur", getLastEndPosMatching());
			
			for (String film : films) {
				bi = new CinemotionBasicsInfos();
				lbasicsInfos.add(bi);
				this.getInfos(film, "recherche", bi, Mode.MODE_TWO);
			}
		}
		return lbasicsInfos;
	}


	private String[] split(String str, String sep, int pos) {
		
		String regExpSep = getPropertiesManager().getValue(sep);
		List<String> lFilms = new ArrayList<String>(); //d�coupage de la ligne (un �l�ment par film)

		Pattern p = Pattern.compile(regExpSep, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		int cpt=0;
		int lastIdx = 0;
		int idx = 0;
		while(m.find(pos)){
			pos = m.end();
			idx = m.start();
			if(cpt!=0){
				lFilms.add(str.substring(lastIdx,idx));
			}
			lastIdx = idx;
			cpt++;
		}
		lFilms.add(str.substring(lastIdx,str.length()));
		
		return lFilms.toArray(new String[lFilms.size()]);
	}


	private int getLastEndPosMatching(){
		if(lastEndPosMatching == 0)
			throw new RuntimeException("getLastEndPosMatching");
		int pos = lastEndPosMatching;
		lastEndPosMatching = 0;
		return pos ;
	}
	
	private String matchExpression(String s, String regExpKey, int pos){
		String expRegReponses = getPropertiesManager().getValue(regExpKey);
		Pattern p = Pattern.compile(expRegReponses, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(s);
		
		if(m.find(pos)){
			lastEndPosMatching = m.end();
			
			if(m.groupCount()>0)
				return m.group(1);
			return m.group(0);
		}
		return "";
	}

	private String matchExpression(String s, String regExpKey){
		return matchExpression(s, regExpKey, 0);
	}
	
	private int matchReponses(String line) {
		String res = matchExpression(line, "nbReponses");
		if(!res.equals("")){
			return Integer.parseInt(res);
		}
		return 0;
	}
	/*===============================================================================*/

	public BasicsInfos fillFullInfos(BasicsInfos info, String header){
		this.initSocket();
		BufferedReader br = this.sendRequest(header);
		String precedentLine = "";
		String currentLine = "";
		this.allLinesInOne = new StringBuffer("");
		boolean chunked=false;
		try {
			boolean eof = false;
			while(!eof && br!=null){
				currentLine = br.readLine();

				if(currentLine!=null){
					if(!chunked){
						chunked = isChunked(currentLine);
					}
					if(chunked){
						currentLine = traiteChunkNR(currentLine, precedentLine, br);
					}
					precedentLine = currentLine;
					logout(currentLine);
					allLinesInOne.append(currentLine);

					if(this.getInfos(currentLine, "rechercheDetail", info, Mode.MODE_TWO).getSynopsis()!=null){
						eof=true;
					}
				}
				else{
					eof=true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.closeSocket();	
		return info;
	}

	/*===============================================================================*/
	private BasicsInfos getInfos(String strFilm, String keyRecherche, BasicsInfos bi, int mode) {
		return getInfos(strFilm, keyRecherche, bi, mode, 0);
	}
	
	private BasicsInfos getInfos(String strFilm, String keyRecherche, BasicsInfos bi, int mode, int pos) {

		String expRecherche = getPropertiesManager().getValue(keyRecherche);
		String[] lRecherche = expRecherche.split(",");

		Pattern p = null;
		Matcher m = null;
		String strFound = null;
		int idx=pos;

		for (int i = 0; i < lRecherche.length; i++) {//Recherche des infos d�crites dans la cl� "keyRecherche" du fichier de proprietes 
			p = Pattern.compile(getPropertiesManager().getValue(lRecherche[i]), Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			m = p.matcher(strFilm);
			if(m.find(idx)){

				if(bi==null){
					logout("bi null");
					return null;
				}
				int count = m.groupCount();
				if(count==1){
					strFound = m.group(1);
					bi.AddInfo(lRecherche[i], strFound);
					idx = m.start()+strFound.length();
				}
				else{
					String[] lKey = lRecherche[i].split("\\.");
					idx = m.start();
					for (int j = 0; j < count; j++) {
						strFound = m.group(j+1);
						idx = idx+strFound.length();
						bi.AddInfo(lKey[j], strFound);
					}
				}

				if(mode == Mode.MODE_ONE){
					return bi;
				}
			}
			else{
				if(mode == Mode.MODE_FULL){
					logout("ERREUR: "+lRecherche[i]+" ne match pas sur "+strFilm);
				}
			}
		}
		return bi;
	}

	protected String transfo(String motCle) {//remplace les blancs par un '+'
		String strRet = "";
		StringTokenizer st = new StringTokenizer(motCle);

		while(st.hasMoreTokens()){
			strRet+=st.nextToken()+(st.hasMoreTokens()?"+":"");
		}

		return strRet;
	}

	private boolean isChunked(String currentLine) {
		Matcher m = patternTransferEncodChunked.matcher(currentLine);
		if(m.matches()){
			return true;
		}
		return false;
	}

	private String traiteChunk(String currentLine, String precedentLine, BufferedReader br) throws IOException {
		Matcher m = patternChunk.matcher(currentLine);
		if(m.matches()){
			String nextLine = br.readLine();
			if(nextLine == null){//S�curit�: n'arrive jamais dans le cas de chunk
				return currentLine;
			}
			currentLine = nextLine;
		}

		return currentLine;
	}


	/**
	 * @deprecated
	 * @param currentLine
	 * @param precedentLine
	 * @param br
	 * @return
	 * @throws IOException
	 */
	private String traiteChunkNR(String currentLine, String precedentLine, BufferedReader br) throws IOException {
		Matcher m = patternChunk.matcher(currentLine);
		if(m.matches()){
			String nextLine = br.readLine();
			if(nextLine == null){//S�curit�: n'arrive jamais dans le cas de chunk
				return currentLine;
			}
			currentLine = precedentLine+nextLine;
		}
		
		return currentLine;
	}
	
	protected SitePropertiesManager getPropertiesManager() {
		return propertiesManager;
	}


	protected void setPropertiesManager(SitePropertiesManager propertiesManager) {
		this.propertiesManager = propertiesManager;
	}


	public StringBuffer getAllLinesInOne() {
		return allLinesInOne;
	}


	public void setAllLinesInOne(StringBuffer allLinesInOne) {
		this.allLinesInOne = allLinesInOne;
	}



}
