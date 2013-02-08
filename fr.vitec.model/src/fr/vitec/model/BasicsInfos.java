package fr.vitec.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.vitec.fmk.exception.VitecException;
import fr.vitec.fmk.file.FileUtil;

public class BasicsInfos {
	public static final String ELEMENTS_SEP = "/";

	public static final String HTTP_BEGIN = "http://";

	private static final String BOLD_BEGIN = "<b>";
	private static final String BOLD_END = "</b>";
	public static final String TITRE = "titre";
	public static final String ACTEURS = "acteurs";
	private static final String ACTEURS_PREFIX = "avec ";
	public static final String IMAGE = "imagePath";
	public static final String ANNEE = "annee";
	public static final String REALISATEUR = "realisateur";
	private static final String REALISATEUR_PREFIX = "de ";
	public static final String REFERENCE="reference";
	public static final String SYNOPSIS="synopsis";
	public static final String BIGIMAGE="bigImage";
	public static final String NATIONALITE = "nationalite";
	public static final String GENRE = "genre";
	public static final String DUREE = "duree";

	private String titre;
	private List<String> lActeurs;
	private String imagePath;
	private String annee;
	private String realisateur;
	private String reference;
	private String synopsis;
	private String bigImage;
	private String duree;
	private String genre;
	private String nationalite;

	private Map<String, Object>mInfos = new HashMap<String, Object>();
	
	
	public void addInfo(String key, Object value){
		
		if(this.getInfo(key)!=null){
			return;
		}
			
		
		if(key.equals(TITRE)){
			String val = (String)value;
//			val = val.replaceAll(BOLD_BEGIN, "");
//			val = val.replaceAll(BOLD_END, "");
			this.setTitre(val);
			mInfos.put(key, val);
		}
		else if(key.equals(ACTEURS)){
			String val = (String)value;
//			if(val.startsWith(ACTEURS_PREFIX)){
//				val = val.substring(ACTEURS_PREFIX.length());
//			}
			String lStr[] = val.split(ELEMENTS_SEP);
			
			for (String str : lStr) {
				getLActeurs().add(str.trim());
			}
			
			mInfos.put(key, val);
		}
		else if(key.equals(IMAGE)){
			this.setImagePath((String)value);
			mInfos.put(key, value);
		}
		else if(key.equals(ANNEE)){
			this.setAnnee((String)value);
			mInfos.put(key, value);
		}
		else if(key.equals(REALISATEUR)){
			String val = (String)value;
//			if(val.startsWith(REALISATEUR_PREFIX)){
//				val = val.substring(REALISATEUR_PREFIX.length());
//			}
			this.setRealisateur(val);
			mInfos.put(key, val);
		}
		else if(key.equals(REFERENCE)){
			String ref = (String)value;
			mInfos.put(key, ref);
			this.setReference(ref);
		}
		else if(key.equals(SYNOPSIS)){
			String s = (String)value;
			s = s.replaceAll("(< *br */? *>)+", FileUtil.LINE_SEPARATOR);
			s = s.replaceAll("\\.([\\w\\t ]+)", "."+FileUtil.LINE_SEPARATOR+"$1");
			mInfos.put(key, s);
			this.setSynopsis(s);
		}
		else if(key.equals(BIGIMAGE)){
			mInfos.put(key, value);
			this.setBigImage((String)value);
		}
		else if(key.equals(NATIONALITE)){
			mInfos.put(key, value);
			this.setNationalite((String) value);
		}
		else if(key.equals(GENRE)){
			mInfos.put(key, value);
			this.setGenre((String) value);
		}
		else if(key.equals(DUREE)){
			mInfos.put(key, value);
			this.setDuree((String) value);
		}
		else{
			throw new VitecException(key+" est inconnu dans "+this.getClass().getName());
		}
	}

	public Object getInfo(String key){
		return mInfos.get(key);
	}
	

	public String getAnnee() {
		return annee;
	}
	public void setAnnee(String annee) {
		this.annee = annee;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public List<String> getLActeurs() {
		if(lActeurs==null){
			lActeurs = new ArrayList<String>();
		}
		return lActeurs;
	}
	public void setLActeurs(List<String> acteurs) {
		lActeurs = acteurs;
	}
	public String getRealisateur() {
		return realisateur;
	}
	public void setRealisateur(String realisateur) {
		this.realisateur = realisateur;
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}

	@Override
	public String toString() {
		String strRet="";
		Set<String> set = mInfos.keySet();
		for (String key : set) {
			strRet += key+": "+mInfos.get(key)+"\n";
		}
		
		
		return strRet;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getBigImage() {
		return bigImage;
	}

	public void setBigImage(String bigImage) {
		this.bigImage = bigImage;
	}
	
	public String getFullReference(){
		return getFullSiteAdr()+getReference();
	}
	
	public String getFullSiteAdr() {
		return HTTP_BEGIN+"???";
	}

	public String getDuree() {
		return duree;
	}

	public void setDuree(String duree) {
		this.duree = duree;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getNationalite() {
		return nationalite;
	}

	public void setNationalite(String nationalite) {
		this.nationalite = nationalite;
	}
}
