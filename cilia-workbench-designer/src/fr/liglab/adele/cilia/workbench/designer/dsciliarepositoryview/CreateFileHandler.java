package fr.liglab.adele.cilia.workbench.designer.dsciliarepositoryview;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import fr.liglab.adele.cilia.workbench.designer.service.dsciliareposervice.DsciliaRepoService;

public class CreateFileHandler extends AbstractHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		// Validator
		IInputValidator validator = new IInputValidator() {
			@Override
			public String isValid(String newText) {
				return DsciliaRepoService.getInstance().isNewFileNameAllowed(newText);
			}
		};
		
		// Dialog creation
		Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		InputDialog dialog = new InputDialog(shell, "titre", "Please give a name for the new file.", ".dscilia", validator);
		
		if (dialog.open() == Window.OK) {
			String fileName = dialog.getValue();
			DsciliaRepoService.getInstance().createFile(fileName);
        }
		
		return null;
	}
	
}
