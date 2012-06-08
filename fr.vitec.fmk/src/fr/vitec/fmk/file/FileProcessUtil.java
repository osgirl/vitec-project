package fr.vitec.fmk.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;


/**
 * @author jlebiannic
 *
 */
public class FileProcessUtil {

//	static String sPatternToExtractDirFromFileWithExt = "(.+)\\\\\\w+\\.EXT";
//
//	static Pattern pExtractDirFromFileWithoutExt = Pattern.compile("^(.+)\\\\(\\w+)$");
//	static Pattern pExtractExtFromFile = Pattern.compile(".+\\\\\\w+\\.(\\w+)$");
//

	//lit et traite plusieurs fichiers récursivement
	public static void litEtTraiteFichiersRecursivement(File file, Process t, String ext) throws IOException {
		if ( file.isDirectory ( ) ) {
			File[] list = file.listFiles();
			for ( int i = 0; i < list.length; i++) {
				// Appel récursif sur les sous-répertoires
				litEtTraiteFichiersRecursivement( list[i], t, ext);
			} 
		} 
		else{
			String longFileName = file.getAbsolutePath();
			if(ext==null || longFileName.endsWith("."+ext))
				litEtTraiteFichier(longFileName,t);
		}
	}

	//	lit et traite plusieurs fichiers (non récursif)
	public static void litEtTraiteFichiers(File repertoire, Process t, String ext) throws IOException {

		File[] list = repertoire.listFiles();
		for ( int i = 0; i < list.length; i++) {
			if ( !list[i].isDirectory() && (ext==null || list[i].getName().endsWith(ext)) ) {
				litEtTraiteFichier(repertoire.getAbsolutePath(),t);
			}
		}
	}

	//lit et traite plusieurs fichiers récursivement ou non 
	public static void litEtTraiteFichiers(String dirName, Process t, boolean rec, String ext) throws IOException {
		File repertoire = new File(dirName);
		Pattern pExt = null;
		
		if(rec)
			litEtTraiteFichiersRecursivement(repertoire,t,ext);
		else
			litEtTraiteFichiers(repertoire,t,ext);
	}

	//lit et traite un fichier
	public static void litEtTraiteFichier(String fileName, Process t) throws IOException {
		BufferedReader reader = null;

		String currentLine;
		int l = 0;
		try {
			reader = new BufferedReader(new FileReader(fileName));

			t.traiteOuvFic(fileName);

			while ((currentLine = reader.readLine()) != null) {
				l++;
				t.traite(currentLine,l);
			}

		} finally {
			if (reader != null)
			{
				reader.close();
				t.traiteFermeFic(fileName);
			}
		}
	}

	//  lit les fichier d'un répertoire
	public static void litFichiers(String dirName, Process t, String ext) throws IOException {
		File repertoire = new File(dirName);
		File[] list = repertoire.listFiles();
		for ( int i = 0; i < list.length; i++) {
			if ( !list[i].isDirectory() && (ext==null || list[i].getName().endsWith("."+ext)) ) {
				t.nomFichierTrouve(list[i].getCanonicalPath());
				t.fichierTrouve(list[i]);
			}
		}	
	}

	public static void litFichiers(String dirName, Process t) throws IOException {
		litFichiers(dirName, t, null);
	}


	//lit les fichiers et répertoires récursivement
	public static void litFichiersRecursivement(String repertoireName, Process t, String ext, int niv) throws IOException {

		File repertoire = new File(repertoireName);
		if ( repertoire.isDirectory ( ) ) {
			File[] list = repertoire.listFiles();

			t.nomRepertoireTrouve(repertoire.getAbsolutePath(),niv);
			t.repertoireTrouve(repertoire, niv);

			litFichiers(repertoireName, t, ext);

			for ( int i = 0; i < list.length; i++) {
				// Appel récursif sur les sous-répertoires
				if(list[i].isDirectory()){
					niv = niv+1;
					litFichiersRecursivement( list[i].getAbsolutePath(), t, ext, niv);
					niv = niv-1;
				}
			}
		} 

	}

//	lit les fichiers et répertoires récursivement
	public static void litFichiersRecursivement(String repertoireName, Process t, String ext) throws IOException {
		litFichiersRecursivement(repertoireName, t, ext, 0);
	}


	public static BufferedWriter creationOuvertureFichier(String f)
	{
		BufferedWriter w = null;

//		Matcher m = pExtractExtFromFile.matcher(f);
//		Pattern p = null;
//		if(m.matches()){
//			sPatternToExtractDirFromFileWithExt = sPatternToExtractDirFromFileWithExt.replaceFirst("EXT",m.group(1));
//			p=Pattern.compile(sPatternToExtractDirFromFileWithExt);
//		}
//		else{
//			p=pExtractDirFromFileWithoutExt;
//		}
//
//		//réinitialisation
//		sPatternToExtractDirFromFileWithExt = "(.+)\\\\\\w+\\.EXT";

		try {
//			m = p.matcher(f);
//			m.matches();
//			String dir = m.group(1);

			File newDir = new File(f).getParentFile();
			if(!newDir.exists()){
				if(!newDir.mkdirs()){
					System.out.println("ERREUR création répertoire: "+newDir);
					return null;
				}
			}

			w = new BufferedWriter(new FileWriter(f));

		} catch (Exception e) {
			System.out.println("ERREUR récupération répertoire");
			return null;
		}
		return w;
	}

	public static void litRepertoires(String repertoireName, Process t){
		File repertoire = new File(repertoireName);
		if ( repertoire.isDirectory ( ) ) {
			List<File> lFiles = Arrays.asList(repertoire.listFiles());
			Collections.sort(lFiles);

			for ( int i = 0; i < lFiles.size(); i++) {
				if(lFiles.get(i).isDirectory()){
					t.repertoireTrouve(lFiles.get(i),0);
					t.nomRepertoireTrouve(lFiles.get(i).getName(), 0);
				}
			}
		} 


	}
}
