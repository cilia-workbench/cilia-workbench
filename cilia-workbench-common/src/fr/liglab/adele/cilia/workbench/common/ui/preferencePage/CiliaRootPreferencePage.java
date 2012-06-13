/**
 * Copyright Universite Joseph Fourier (www.ujf-grenoble.fr)
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
package fr.liglab.adele.cilia.workbench.common.ui.preferencePage;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * A root page in the preference menu. Should be used by all Cilia Designer sub
 * pages.
 * 
 * @author Etienne Gandrille
 */
public class CiliaRootPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	/** Message displayed on the top of the page. */
	private static final String PAGE_DESCRIPTION = "CILIA workbench preferences.";

	public CiliaRootPreferencePage() {
	}

	public CiliaRootPreferencePage(String title) {
		super(title);
	}

	public CiliaRootPreferencePage(String title, ImageDescriptor image) {
		super(title, image);
	}

	@Override
	public void init(IWorkbench workbench) {
		setDescription(PAGE_DESCRIPTION);
	}

	@Override
	protected Control createContents(Composite parent) {
		return null;
	}
}
