package fr.vitec.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import fr.vitec.fmk.exception.VitecException;
import fr.vitec.fmk.image.ImageUtil;
import fr.vitec.model.xmlbinding.ActorType;
import fr.vitec.model.xmlbinding.BaseType;
import fr.vitec.model.xmlbinding.DirectoryType;
import fr.vitec.model.xmlbinding.FileType;
import fr.vitec.model.xmlbinding.FilmType;
import fr.vitec.model.xmlbinding.ObjectFactory;
import fr.vitec.model.xmlbinding.PropertyType;
import fr.vitec.model.xmlbinding.SiteType;


public class VitecModel extends Observable{
	public static final String MESSAGE_ADD_FILM 	= "ADD_FILM";
	public static final String MESSAGE_SET_DIRTY 	= "SET_DIRTY";
	public static final String MESSAGE_SAVE 		= "SAVE";
	public static final String MESSAGE_UNSAVE 		= "UNSAVE";
	public static final String MESSAGE_DELETE 		= "DELETE";
	public static final String MESSAGE_DIR_CHANGE	= "DIR_CHANGE";
	
	private ObjectFactory of;
	//private List<FilmType> films;
	private BaseType baseType;
	private String basePath;
	private List<FilmType> newFilms;
	private List<FilmType> dirtyFilms;
	
	public String getBasePath() {
		return basePath;
	}

	private static VitecModel vitecModel = null;
	
	private VitecModel() {
		newFilms = new ArrayList<FilmType>();
		dirtyFilms = new ArrayList<FilmType>();
	}
	
	public static VitecModel getInstance(){
		if(vitecModel==null){
			vitecModel = new VitecModel();
		}
		return vitecModel;
	}

	public void open(String basePath){
		of = new ObjectFactory();
		baseType = of.createBaseType();
		PropertyType prop = of.createPropertyType();
		baseType.setProperty(prop);
		this.basePath = basePath;
		unmarshal();
	}
	
	public void create(String fileName) {
		of = new ObjectFactory();
		baseType = of.createBaseType();
		PropertyType prop = of.createPropertyType();
		baseType.setProperty(prop);
		this.basePath = fileName;
		
		marshal();
		
	}

	public void setSite( String site ){
		try{
			baseType.getProperty().setSite(SiteType.fromValue(site));
		}
		catch(IllegalArgumentException e){
			throw new VitecException(e);
		}
	}

	public void addDirectory(String path){
		addDirectory(path, "avi", FileType.FICHIER.value(), true);
		setChanged();
		notifyObservers(MESSAGE_DIR_CHANGE);
	}
	
	public void addDirectory(String path, String filter, String fileType, boolean recursive){
		DirectoryType dir = of.createDirectoryType();
		dir.setFile(FileType.fromValue(fileType));
		dir.setFilter(filter);
		dir.setName(path);
		dir.setPath(path);
		dir.setRecursive(recursive);
		baseType.getProperty().getDirectory().add(dir);
	}

	//La conso ne sera plus utile si lors de la creation de la base le lien film<->directory est cr�er
//	private void conso() {
//		for(FilmType film : baseType.getFilm()){
//			for(DirectoryType dir : baseType.getProperty().getDirectory()){
//				if(film.getPath().equals(dir.getPath())){
//					//film.getDirectory().add(dir);
//					dir.getFilm().add(film);
//					break;
//				}
//			}
//		}
//	}

	public FilmType addFilm(String imagePath, String title, String titleDisk, String year, String path, String genre, String runtime, String country, String director, String actorsName, String summary, String urlReference){
		String[] tActorsName = actorsName.split(",");
		return addFilm(imagePath, title, titleDisk, year, path, genre, runtime, country, director, Arrays.asList(tActorsName), summary, urlReference);
	}

	public FilmType addFilm(String imagePath, String title, String titleDisk, String year, String path, String genre, String runtime, String country, String director, List<String> actorsName, String summary, String urlReference){
		FilmType film = of.createFilmType();

		for (String actorName : actorsName) {
			ActorType actorObj = new ActorType();
			actorObj.setName(actorName);
			film.getActor().add(actorObj);
		}

		film.setCountry(country);
		film.setDirector(director);
		film.setGenre(genre);
		film.setRuntime(runtime);
		film.setSummary(summary);
		film.setTitle(title);
		film.setTitleDisk(titleDisk);
		film.setPath(path);
		film.setReference(urlReference);
		film.setImagePath(imagePath);
		film.setYear(year);
		
		film.setImageEncoded(ImageUtil.getEncodedStringFromImageUrl(imagePath));
		
		//Rattacher le film a une directory 
		List<DirectoryType> dirs = baseType.getProperty().getDirectory();
		for (DirectoryType dir : dirs) {
			if(dir.getPath().equals(film.getPath())){
				dir.getFilm().add(film);
				break;
			}
		}
		
		return film;
	}

	public void marshal() {
		try {
			JAXBElement<BaseType> gl =
				of.createBase(baseType);
			JAXBContext jc = JAXBContext.newInstance( BaseType.class.getPackage().getName() );
			Marshaller m = jc.createMarshaller();
			m.setProperty("jaxb.formatted.output", true);
			//DBG m.marshal( gl, System.out );

			File output = new File(basePath);
			m.marshal( gl, output );
		} catch( Exception jbe ){
			jbe.printStackTrace();
		}
	}

	public void unmarshal(){
		try {
			this.baseType = unmarshal(BaseType.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T> T unmarshal( Class<T> docClass) throws JAXBException {
		String packageName = docClass.getPackage().getName();
		JAXBContext jc = JAXBContext.newInstance( packageName );
		Unmarshaller u = jc.createUnmarshaller();
		JAXBElement<T> doc = (JAXBElement<T>)u.unmarshal( new File(basePath));
		return doc.getValue();
	}

	public BaseType getBaseType() {
		return baseType;
	}

	public void clearDirectories() {
		baseType.getProperty().getDirectory().clear();
	}

	public List<DirectoryType> getDirectories() {
		List<DirectoryType> directories = baseType.getProperty().getDirectory();
		return directories==null?new ArrayList<DirectoryType>():directories;
	}

	public List<FilmType> getFilms() {
		List<FilmType> allFilms = new ArrayList<FilmType>();
		List<DirectoryType> dirs = baseType.getProperty().getDirectory();
		for (DirectoryType dir : dirs) {
			List<FilmType> films = dir.getFilm();
			allFilms.addAll(films);
		}
		return allFilms;
	}

	public void save() {
		newFilms.clear();
		dirtyFilms.clear();
		setChanged();
		notifyObservers(MESSAGE_SAVE);
		marshal();
	}

//	public boolean containsFilmByPath(String path) {
//		return getFilmByPath(path)==null?true:false;
//	}
//	public FilmType getFilmByPath(String path) {
//		List<FilmType> films = this.getFilms();
//		for (FilmType film : films) {
//			if(film.getPath().equals(path)){
//				return film;
//			}
//		}
//		return null;
//	}

	public boolean containsFilmByTitleDisk(String titleDisk) {
		return getFilmByTitleDisk(titleDisk)==null?true:false;
	}
	public FilmType getFilmByTitleDisk(String titleDisk) {
		List<FilmType> films = this.getFilms();
		for (FilmType film : films) {
			if(film.getTitleDisk().equals(titleDisk)){
				return film;
			}
		}
		return null;
	}

	public void addFilm(BasicsInfos basicFilmInfos, String fullPath, String titleDisk) {
		String imagePath = basicFilmInfos.getImagePath();
		String title = basicFilmInfos.getTitre();
		String year = basicFilmInfos.getAnnee();
		String genre = basicFilmInfos.getGenre();
		String runtime = basicFilmInfos.getDuree(); 
		String country = basicFilmInfos.getNationalite();
		String director = basicFilmInfos.getRealisateur();
		List<String> actorsName = basicFilmInfos.getLActeurs();
		String summary = basicFilmInfos.getSynopsis();
		String urlReference = basicFilmInfos.getFullReference();
		FilmType filmType = addFilm(imagePath, title, titleDisk, year, fullPath, genre, runtime, country, director, actorsName, summary, urlReference);
		newFilms.add(filmType);
		setChanged();
		notifyObservers(MESSAGE_ADD_FILM);
	}

	public boolean removeDirectory(DirectoryType directory) {
		List<DirectoryType> directories = this.getDirectories();
		setChanged();
		notifyObservers(MESSAGE_DIR_CHANGE);
		return directories.remove(directory);
	}

	public boolean isNew(FilmType film) {
		return newFilms.contains(film);
	}

	public boolean isDirty(FilmType film) {
		return dirtyFilms.contains(film);
	}

	public void setDirty(FilmType film) {
		dirtyFilms.add(film);
		setChanged();
		notifyObservers(MESSAGE_SET_DIRTY);
	}

	public void notSave() {
		dirtyFilms.clear();
		setChanged();
		notifyObservers(MESSAGE_UNSAVE);
	}

	public void deleteFilms(List<FilmType> filmsToDelete) {
		List<DirectoryType> dirs = baseType.getProperty().getDirectory();
		for (FilmType filmToDelete : filmsToDelete) {
			for (DirectoryType dir : dirs) {
				List<FilmType> films = dir.getFilm();
				films.remove(filmToDelete);
				break;
			}
		}
		setChanged();
		notifyObservers(MESSAGE_DELETE);
	}



//	public List<FilmType> getFilms(DirectoryType dir) {
//		List<FilmType> films = new ArrayList<FilmType>();
//		String dirRef = dir.getPath();
//		for(FilmType film : baseType.getFilm()){
//			if(dirRef.equals(film.getPath())){
//				films.add(film);
//			}
//		}
//		return films;
//	}
}
