package fr.vitec.site.cinemotion;

import fr.vitec.model.BasicsInfos;

public class CinemotionBasicsInfos extends BasicsInfos {
	@Override
	public String getFullSiteAdr() {
		return HTTP_BEGIN+FindFilmInfoFromCinemotion.WWW_CINEMOTIONS;
	}

}
