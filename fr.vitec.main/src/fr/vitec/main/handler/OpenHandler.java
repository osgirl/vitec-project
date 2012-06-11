package fr.vitec.main.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;


import fr.vitec.fmk.dialog.RcpFileChooser;
import fr.vitec.fmk.rcp.RcpUtils;
import fr.vitec.main.Perspective;
import fr.vitec.main.ViewPartMaster;
import fr.vitec.main.util.id.Id;
import fr.vitec.model.VitecModel;

public class OpenHandler implements IHandler {

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String fileName = new RcpFileChooser().openFile(RcpFileChooser.XML_EXTENTION);
		open(fileName);

		return null;
	}

	public void open(String fileName) {
		if(fileName!=null){
			RcpUtils.switchPerspective(Perspective.ID);
			RcpUtils.activateActivity(Id.ACTIVITY_MASTER);
			ViewPartMaster viewMaster = (ViewPartMaster) RcpUtils.getView(ViewPartMaster.ID);

			VitecModel model = VitecModel.getInstance();
			model.open(fileName);
			viewMaster.setInput(model);
		}
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isHandled() {
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

}
