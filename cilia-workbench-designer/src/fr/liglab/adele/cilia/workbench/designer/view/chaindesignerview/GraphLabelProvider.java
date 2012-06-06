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
package fr.liglab.adele.cilia.workbench.designer.view.chaindesignerview;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.IConnectionStyleProvider;
import org.eclipse.zest.core.widgets.ZestStyles;

import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.AdapterComponent;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.Binding;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.Chain;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.Component;
import fr.liglab.adele.cilia.workbench.designer.parser.abstractcompositions.MediatorComponent;
import fr.liglab.adele.cilia.workbench.designer.parser.common.element.IAdapter.AdapterType;
import fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice.GenericContentProvider;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.LabelProvider;

/**
 * Label provider for the tree viewer.
 * 
 * @author Etienne Gandrille
 */
public class GraphLabelProvider extends LabelProvider implements IConnectionStyleProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.liglab.adele.cilia.workbench.designer.view.repositoryview.LabelProvider
	 * #getContentProvider()
	 */
	@Override
	protected GenericContentProvider getContentProvider() {
		return null;
	}

	@Override
	protected ImageDescriptorEnum getImageDescriptor(Object obj) {

		ImageDescriptorEnum imageName;
		if (isCompatible(obj, AdapterComponent.class)) {
			AdapterComponent adapter = (AdapterComponent) obj;
			if (adapter.getReferencedObject() != null) {
				if (adapter.getReferencedObject().getType() == AdapterType.IN)
					imageName = ImageDescriptorEnum.ADAPTER_IN;
				else
					imageName = ImageDescriptorEnum.ADAPTER_OUT;
			} else
				imageName = ImageDescriptorEnum.ADAPTER_IN;
		} else if (isCompatible(obj, MediatorComponent.class))
			imageName = ImageDescriptorEnum.MEDIATOR;
		else if (isCompatible(obj, EntityConnectionData.class))
			imageName = ImageDescriptorEnum.ONLY_TEXT;
		else
			throw new RuntimeException("Unsupported type: " + obj.getClass());

		return imageName;
	}

	@Override
	public String getText(Object element) {
		String defval = super.getText(element);

		if (isCompatible(element, EntityConnectionData.class)) {
			EntityConnectionData ecd = (EntityConnectionData) element;
			Component src = (Component) ecd.source;
			Component dst = (Component) ecd.dest;
			Chain chain = src.getChain();
			Binding binding = chain.getBinding(src, dst);

			if (binding == null)
				return "";
			else {
				String from = binding.getFromCardinality().stringId();
				String to = binding.getToCardinality().stringId();
				return from + " --> " + to;
			}
		} else
			return defval;
	}

	@Override
	public int getConnectionStyle(Object rel) {
		return ZestStyles.CONNECTIONS_DIRECTED;
	}

	@Override
	public Color getColor(Object rel) {
		return Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	}

	@Override
	public Color getHighlightColor(Object rel) {
		return Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
	}

	@Override
	public int getLineWidth(Object rel) {
		return 2; // -1 = default value
	}

	@Override
	public IFigure getTooltip(Object entity) {
		// return new Label("something");
		return null;
	}
}
