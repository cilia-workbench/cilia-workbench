package fr.liglab.adele.cilia.workbench.restmonitoring.view.platformview;


import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import fr.liglab.adele.cilia.workbench.common.ui.dialog.TextsDialog;
import fr.liglab.adele.cilia.workbench.restmonitoring.service.platform.PlatformRepoService;

/**
 * Dialog used to create a new platform. Asks for 
 * <ul>
 * <li>name</li>
 * <li>host</li>
 * <li>port</li>
 * </ul>
 * @author Etienne Gandrille
 */
public class NewPlatformDialog extends TextsDialog {

	private final static String title = "New Platform";
	private final static Point initialSize = new Point(400, 200);
	private final static boolean isOkButtonEnable = false;
	private final static String labels[] = {"name", "host", "port"};
	private final static TextsDialogValidator validator = new NewPlatformValidator();
		
	NewPlatformDialog(Shell parentShell) {
		super(parentShell, title, initialSize, isOkButtonEnable, labels, validator);
	}

	private static class NewPlatformValidator implements TextsDialogValidator {
		@Override
		public String validate(ModifyEvent event, String[] values) {
			
			// File Name validation
			String msg = PlatformRepoService.getInstance().isNewFileNameAllowed(values[0]);
			if (msg != null)
				return msg;
			
			// empty values
			if (values[0].isEmpty())
				return "Name can't be empty";
			if (values[1].isEmpty())
				return "Host can't be empty";
			if (values[2].isEmpty())
				return "Port can't be empty";
			
			// Port validator
			try {
				Integer port = Integer.valueOf(values[2]);
				if (port < 10 || port > 65535)
					return "Wrong port number";
			} catch (NumberFormatException e) {
				return "Wrong port format";
			}

			return null;
		}
	}
}
