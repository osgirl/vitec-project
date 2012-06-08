package pojo;

import fr.vitec.model.VitecModel;

public class BaseSample {
//	private List<FilmType> films;
	private VitecModel model;
	
	
	
	public BaseSample() {
		model = VitecModel.getInstance();
		model.open("D:\\Dev\\java\\workspace_vitec\\Copie de a.xml");
		
//		films = new ArrayList<FilmType>();
//		FilmType film = new FilmType();
//		
//		ActorType actor = new ActorType();
//		actor.setName("acteur1");
//		film.getActor().add(actor);
//		film.setTitle("Film1");
//		film.setSummary("the summary");
//		films.add(film);
		
		
	}
	public VitecModel getModel() {
		return model;
	}
}
