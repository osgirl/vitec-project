package fr.vitec.batch.process;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import fr.vitec.batch.extension.FindFilmInfo;
import fr.vitec.batch.process.logger.BatchBasicLogger;
import fr.vitec.batch.process.logger.IBatchLogger;
import fr.vitec.batch.process.progressmanager.IProgressManager;
import fr.vitec.batch.process.progressmanager.ProgressBasicManager;
import fr.vitec.batch.util.ChoixFilm;
import fr.vitec.fmk.exception.VitecException;
import fr.vitec.fmk.file.FileProcessUtil;
import fr.vitec.fmk.file.Process;
import fr.vitec.model.BasicsInfos;
import fr.vitec.model.VitecModel;
import fr.vitec.model.xmlbinding.DirectoryType;
import fr.vitec.model.xmlbinding.FilmType;

public class BatchManager {

	private IBatchLogger batchLogger;
	private FindFilmInfo site;
	private List<DirectoryType> directories;
	private ChoixFilm choixFilm;
	private DirProcess process;
	private LinkedHashMap<DirectoryType, List<File>> map;
	private IProgressManager progressManager;

	public BatchManager(FindFilmInfo site,	List<DirectoryType> directories, ChoixFilm choixFilm) {
		this.site = site;
		this.directories = directories;
		this.choixFilm = choixFilm;
		setLogger(new BatchBasicLogger());
		setProgressManager(new ProgressBasicManager());

		map = new LinkedHashMap<DirectoryType, List<File>>();

		process = new DirProcess();
	}

	public void setProgressManager(IProgressManager progressManager) {
		this.progressManager = progressManager;
	}

	public void setLogger(IBatchLogger batchLogger) {
		this.batchLogger = batchLogger;
	}

	public void execute() {
		new Thread() {
			public void run() {
				processDirectories();
			}


		}.start();

	}

	private void processDirectories() {
		int nbFiles = 0;
		for(DirectoryType dir : directories){
			try {
				process.setDirectory(dir);
				FileProcessUtil.litFichiersRecursivement(dir.getPath(), process, dir.getFilter());
				nbFiles += process.getNbFiles();
			} catch (IOException e) {
				throw new VitecException(e);
			}
		}
		progressManager.init(nbFiles);
		Set<Entry<DirectoryType, List<File>>> entries = map.entrySet();

		for (Entry<DirectoryType, List<File>> entry : entries) {
			DirectoryType dir = entry.getKey();
			List<File> files = entry.getValue();
			batchLogger.logTitle(dir.getPath());
			for(File file : files){
				String path = file.getParentFile().getAbsolutePath();
				
				String titleDisk = getSimpleName(file);
				FilmType film = VitecModel.getInstance().getFilmByTitleDisk(titleDisk);
				//Si le model ne contient pas deja le film courant
				if(film == null){
					List<BasicsInfos> basicFilmsInfos = site.sendRequestAndGetBasicsInfos(titleDisk);
					BasicsInfos basicFilmInfos = null;
					if(!basicFilmsInfos.isEmpty()){
						if(choixFilm == ChoixFilm.NEVER){
							basicFilmInfos = basicFilmsInfos.get(0);
							site.fillFullInfos(basicFilmInfos);
							VitecModel.getInstance().addFilm(basicFilmInfos, path, titleDisk);
						}
					}
				}else{//Mise à jour du chemin
					film.setPath(path);
				}
				batchLogger.logInfo(titleDisk);
				progressManager.progress();
			}
		}
	}

	private String getSimpleName(File file) {
		String fileName = file.getName();
		int posExt = fileName.indexOf(".");
		if(posExt != -1){
			fileName = fileName.substring(0, posExt);
		}
		return fileName;
	}

	class DirProcess extends Process{

		private DirectoryType dir;
		int nbFiles = 0;

		@Override
		public void fichierTrouve(File file) {
			addFile(dir, file);
			nbFiles++;
		}

		public int getNbFiles() {
			return nbFiles;
		}

		public void setDirectory(DirectoryType dir) {
			this.dir = dir;
			nbFiles = 0;
		}
	}

	public void addFile(DirectoryType dir, File file) {
		List<File> files = map.get(dir);
		if(files == null){
			files = new ArrayList<File>();
		}
		files.add(file);
		map.put(dir, files);
	} 
}

//		Job job = new Job("batch process") {
//			@Override
//			protected IStatus run(IProgressMonitor monitor) {
//				processDirectories(monitor);
//				// Use this to open a Shell in the UI thread
//				return Status.OK_STATUS;
//			}
//
//			private void processDirectories(IProgressMonitor monitor) {
//				monitor.beginTask("Doing something time consuming here", 100);
//				for (int i = 0; i < 5; i++) {
//					try {
//							Thread.sleep(5000);
//							monitor.subTask("I'm doing something here " + i);
//							// Report that 20 units are done
//							monitor.worked(20);
//						} catch (InterruptedException e1) {
//							e1.printStackTrace();
//						}
//					}
//
//				
//			}
//
//		};
//		
//		job.setUser(true);
//		job.schedule();