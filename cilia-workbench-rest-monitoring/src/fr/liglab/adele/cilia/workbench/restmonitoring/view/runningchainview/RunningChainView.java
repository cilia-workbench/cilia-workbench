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
package fr.liglab.adele.cilia.workbench.restmonitoring.view.runningchainview;

import java.util.List;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

import fr.liglab.adele.cilia.workbench.common.service.AbstractRepoService;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.IRepoServiceListener;
import fr.liglab.adele.cilia.workbench.common.ui.view.graphview.GraphView;
import fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform.PlatformChain;
import fr.liglab.adele.cilia.workbench.restmonitoring.service.platform.PlatformRepoService;
import fr.liglab.adele.cilia.workbench.restmonitoring.view.platformview.PlatformView;

/**
 * 
 * @author Etienne Gandrille
 */
public class RunningChainView extends GraphView implements IRepoServiceListener, ISelectionListener {

	public static final String viewId = "fr.liglab.adele.cilia.workbench.restmonitoring.view.runningchainview";

	private IBaseLabelProvider labelProvider = new PlatformChainLabelProvider();

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent, viewId);

		PlatformRepoService.getInstance().registerListener(this);
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(PlatformView.VIEW_ID, this);

		updateConfigAndModel(null);
	}

	private void updateConfigAndModel(PlatformChain chain) {

		if (viewer.getLabelProvider() != labelProvider)
			viewer.setLabelProvider(labelProvider);

		// TODO attention à l'ordre dans affectations et ré-affectations...
		viewer.setContentProvider(new PlatformChainContentProvider(chain));
		viewer.setInput(chain);

		viewer.refresh();
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub
		// si le changement de sélection a un impact sur ce qui doit être
		// affiché...
		System.out.println("RunningChainView - selection changed");
	}

	@Override
	public void repositoryContentUpdated(AbstractRepoService<?, ?> abstractRepoService, List<Changeset> changes) {
		// TODO Auto-generated method stub
		// si l'update du repository a un impact sur ce qui doit être affiché...
		System.out.println("RunningChainView - repo content updated");
	}

	@Override
	protected void onDoubleClick(Shell parentShell, Object element) {
		// TODO Auto-generated method stub
		System.out.println("RunningChainView - double click");
	}
}
