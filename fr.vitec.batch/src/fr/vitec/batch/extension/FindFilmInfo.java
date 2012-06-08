package fr.vitec.batch.extension;

import java.util.List;

import fr.vitec.model.BasicsInfos;


public abstract class FindFilmInfo {

	public abstract List<BasicsInfos> sendRequestAndGetBasicsInfos(String filmName);
	public abstract BasicsInfos fillFullInfos(BasicsInfos info);
	public abstract String getSiteName();
}
