/*
 * Copyright Adele Team LIG (http://www-adele.imag.fr/)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.liglab.adele.cilia.workbench.designer.repositoryview;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import fr.liglab.adele.cilia.workbench.common.view.ViewUtil;


/**
 * The Class ReloadHandler.
 */
public class ReloadHandler extends AbstractHandler {
		
	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		getRepositoryView(event).refresh();
		return null;
	}
	
	/**
	 * Gets the Repository View.
	 * 
	 * @param event the handler event
	 * @return the RepositoryView
	 */
	protected RepositoryView getRepositoryView(ExecutionEvent event) {
		String viewId = RepositoryView.viewId;		
		return (RepositoryView) ViewUtil.findViewWithId(event, viewId);
	}
}
