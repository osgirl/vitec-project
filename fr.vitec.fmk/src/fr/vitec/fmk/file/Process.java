/*
 * Created on 23 mai 2005
 *
 */
package fr.vitec.fmk.file;

import java.io.File;

/**
 * @author jlebiannic
 *
 */
public class Process {

	public void traite(String s, int lineNumber){}
	
	public void traiteOuvFic (String fileName){}
	
	public void traiteFermeFic (String fileName){}

    public void nomFichierTrouve(String fileName) {}
    public void fichierTrouve(File file) {}

    public void nomRepertoireTrouve(String dirName, int niv) {}
    public void repertoireTrouve(File dir, int niv) {}


}
