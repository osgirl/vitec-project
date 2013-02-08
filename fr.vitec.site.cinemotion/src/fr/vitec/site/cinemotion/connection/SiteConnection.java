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

	protected SitePropertiesManager propertiesManager = null;

	private int lastEndPosMatching = 0;

	public SiteConnection(String adr, SitePropertiesManager propertiesManager){
		super(adr);
		this.propertiesManager = propertiesManager;
	}


	protected void logout(String s) {
	}

	private String getRequestContent(String requestString){
		this.initSocket();
		StringBuilder content = new StringBuilder("");

		BufferedReader bufferReader = sendRequest(requestString, "ISO-8859-1");
		String currentLine = "";
		String precedentLine = "";
		boolean chunked = false;
		try {
			boolean eof = false;
			while(!eof && bufferReader!=null){
				currentLine = bufferReader.readLine();

				if(currentLine!=null){

					if(!chunked){
						chunked = isChunked(currentLine);
					}
					if(chunked){
						currentLine = traiteChunk(currentLine, precedentLine, bufferReader);
					}
					content.append(currentLine);
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

		return content.toString();
	}

	/*===============================================================================*/
	public List<BasicsInfos> sendRequestAndGetBasicsInfosOnRequest(String header){

		String requestContent = getRequestContent(header);

		List<BasicsInfos> lbasicsInfos = processForBasicInfo(requestContent);

		this.closeSocket();
		return lbasicsInfos;		
	}




	private List<BasicsInfos> processForBasicInfo(String content) {
		List<BasicsInfos> lbasicsInfos = new ArrayList<BasicsInfos>();
		BasicsInfos bi = null;

		int res = matchReponses(content);//positionne lastEndPosMatching

		//currentSep = matchExpression(str, "separateur", getLastEndPosMatching());
		if(res != 0){

			String[] films = split(content, "separateur", getLastEndPosMatching());

			for (String film : films) {
				bi = new CinemotionBasicsInfos();
				lbasicsInfos.add(bi);
				this.getInfos(film, "recherche", bi);
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

	public BasicsInfos sendRequestAndFillFullInfos(BasicsInfos basicsInfos, String header){
		BasicsInfos fullInfo = null;
		String content = getRequestContent(header);
		fullInfo = getInfos(content, "rechercheDetail", basicsInfos);
		return fullInfo;
	}

	/*===============================================================================*/
	private BasicsInfos getInfos(String strFilm, String keyRecherche, BasicsInfos bi) {
		return getInfos(strFilm, keyRecherche, bi, 0);
	}


	private BasicsInfos getInfos(String strFilm, String keyRecherche, BasicsInfos basicsInfos, int pos) {

		String expRecherche = getPropertiesManager().getValue(keyRecherche);
		String[] lRecherche = expRecherche.split(",");

		Pattern p = null;
		Matcher m = null;
		int globalIndex=pos;

		for (int i = 0; i < lRecherche.length; i++) {//Recherche des infos d�crites dans la cl� "keyRecherche" du fichier de proprietes 
			String key = lRecherche[i];

			if(key.contains("*")){//la clé est du type acteurs*acteur.role.
				String[] elements = key.split("\\*");
				String mainKey = elements[0];
				String secondaryKey = elements[1];
				StringBuilder finalMainValue = new StringBuilder("");

				String mainValue = getPropertiesManager().getValue(mainKey);
				p = Pattern.compile(mainValue, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
				m = p.matcher(strFilm);
				if(m.find(globalIndex)){
					String mainStr = m.group(0);
					globalIndex = globalIndex+mainStr.length();

					String secondaryValue = getPropertiesManager().getValue(secondaryKey);
					p = Pattern.compile(secondaryValue, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
					m = p.matcher(mainStr);

					int localIndex = 0;
					while(m.find(localIndex)){
						localIndex = localIndex+m.group(0).length();
						
						if(finalMainValue.length()!=0){
							finalMainValue.append(BasicsInfos.ELEMENTS_SEP);
						}

						int count = m.groupCount();
						StringBuilder finalSecondaryValue = new StringBuilder("");
						for (int j = 0; j < count; j++) {
							String secondaryStr = m.group(j+1);

							if(finalSecondaryValue.length()!=0){
								finalSecondaryValue.append(" ");
							}
							finalSecondaryValue.append(secondaryStr);
						}

						finalMainValue.append(finalSecondaryValue);
					}
					basicsInfos.addInfo(mainKey, finalMainValue.toString());
				}
			}else{// La clé est du type annee.duree
				String value = getPropertiesManager().getValue(key);
				p = Pattern.compile(value, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
				m = p.matcher(strFilm);
				if(m.find(globalIndex)){
					globalIndex = m.group(0).length();
					int count = m.groupCount();
					String[] lKey = key.split("\\.");
					for (int j = 0; j < count; j++) {
						String strFound = m.group(j+1);
						basicsInfos.addInfo(lKey[j], strFound);
					}
				}
			}
		}
		return basicsInfos;
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


	protected SitePropertiesManager getPropertiesManager() {
		return propertiesManager;
	}


	protected void setPropertiesManager(SitePropertiesManager propertiesManager) {
		this.propertiesManager = propertiesManager;
	}



}
