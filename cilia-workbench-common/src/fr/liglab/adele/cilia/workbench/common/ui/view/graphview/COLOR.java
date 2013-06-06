/**
 * Copyright 2012-2013 France Télécom 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package fr.liglab.adele.cilia.workbench.common.ui.view.graphview;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public enum COLOR {
	WHITE(SWT.COLOR_WHITE), BLACK(SWT.COLOR_BLACK), RED(SWT.COLOR_RED), DARK_RED(SWT.COLOR_DARK_RED), GREEN(SWT.COLOR_GREEN), DARK_GREEN(SWT.COLOR_DARK_GREEN), YELLOW(
			SWT.COLOR_YELLOW), DARK_YELLOW(SWT.COLOR_DARK_YELLOW), BLUE(SWT.COLOR_BLUE), DARK_BLUE(SWT.COLOR_DARK_BLUE), MAGENTA(SWT.COLOR_MAGENTA), DARK_MAGENTA(
			SWT.COLOR_DARK_MAGENTA), CYAN(SWT.COLOR_CYAN), DARK_CYAN(SWT.COLOR_DARK_CYAN), GRAY(SWT.COLOR_GRAY), DARK_GRAY(SWT.COLOR_DARK_GRAY);

	private final Color _color;

	private COLOR(int color) {
		_color = Display.getDefault().getSystemColor(color);
	}

	Color getColor() {
		return _color;
	}
}
