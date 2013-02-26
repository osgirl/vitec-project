package fr.vitec.main.handler;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.expressions.EvaluationContext;

import fr.vitec.model.VitecModel;
import fr.vitec.model.xmlbinding.FilmType;

public class PopupDeleteHandler implements IHandler {

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		EvaluationContext appContext = (EvaluationContext) event.getApplicationContext();
		List<FilmType> filmsToDelete = (List<FilmType>) appContext.getDefaultVariable();
		VitecModel.getInstance().deleteFilms(filmsToDelete);
		
		return null;
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

	}

}
