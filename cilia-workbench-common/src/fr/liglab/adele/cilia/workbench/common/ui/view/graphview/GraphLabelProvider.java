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
package fr.liglab.adele.cilia.workbench.common.ui.view.graphview;

import org.eclipse.core.resources.IMarker;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.graphics.Color;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.IConnectionStyleProvider;
import org.eclipse.zest.core.viewers.IEntityStyleProvider;
import org.eclipse.zest.core.widgets.ZestStyles;

import fr.liglab.adele.cilia.workbench.common.parser.chain.ComponentRef;
import fr.liglab.adele.cilia.workbench.common.service.chain.ChainContentProvider;
import fr.liglab.adele.cilia.workbench.common.ui.view.CiliaLabelProvider;
import fr.liglab.adele.cilia.workbench.common.ui.view.GenericContentProvider;

/**
 * 
 * @author Etienne Gandrille
 */
public class GraphLabelProvider extends CiliaLabelProvider implements IConnectionStyleProvider, IEntityStyleProvider {

	private final ChainContentProvider contentProvider;
	private final GraphTextLabelProvider graphTextLabelProvider;

	public GraphLabelProvider(ChainContentProvider contentProvider) {
		this.contentProvider = contentProvider;
		this.graphTextLabelProvider = new DefaultGraphTextLabelProvider();
	}

	public GraphLabelProvider(ChainContentProvider contentProvider, GraphTextLabelProvider graphTextLabelProvider) {
		this.contentProvider = contentProvider;
		this.graphTextLabelProvider = graphTextLabelProvider;
	}

	// ===============
	// ContentProvider
	// ===============

	@Override
	protected GenericContentProvider getContentProvider() {
		return contentProvider;
	}

	// ==============
	// Label Provider
	// ==============

	@Override
	public String getText(Object element) {
		String defaultValue = super.getText(element);

		if (isCompatible(element, EntityConnectionData.class))
			return graphTextLabelProvider.getBindingText((EntityConnectionData) element, defaultValue);
		else
			return graphTextLabelProvider.getNodeText(element, defaultValue);
	}

	// ====================
	// IEntityStyleProvider
	// ====================

	protected Color getBackgroundColourForComponentRef(Object entity) {
		if (entity instanceof ComponentRef) {
			switch (((ComponentRef) entity).getComponentRefType()) {
			case SPEC:
				return getConfig().getNodeSpecColor();
			case IMPLEM:
				return getConfig().getNodeImplemColor();
			case INSTANCE:
				return getConfig().getNodeInstanceColor();
			}
		}
		return getConfig().getNodeImplemColor();
	}

	@Override
	public Color getBackgroundColour(Object entity) {
		return getBackgroundColourForComponentRef(entity);
	}

	@Override
	public Color getNodeHighlightColor(Object entity) {
		return getConfig().getNodeHighlightColor();
	}

	@Override
	public Color getBorderColor(Object entity) {
		return getConfig().getNodeBorderColor();
	}

	@Override
	public Color getBorderHighlightColor(Object entity) {
		return getConfig().getNodeBorderHighlightColor();
	}

	@Override
	public int getBorderWidth(Object entity) {
		return getConfig().getNodeBorderWidth();
	}

	@Override
	public Color getForegroundColour(Object entity) {
		return getConfig().getNodeTextColor();
	}

	@Override
	public boolean fisheyeNode(Object entity) {
		return false;
	}

	// ========================
	// IConnectionStyleProvider
	// ========================

	@Override
	public int getConnectionStyle(Object rel) {
		return ZestStyles.CONNECTIONS_DIRECTED;
	}

	@Override
	public Color getColor(Object rel) {
		EntityConnectionData link = (EntityConnectionData) rel;
		ComponentRef src = (ComponentRef) link.source;
		ComponentRef dst = (ComponentRef) link.dest;

		Object binding = contentProvider.getBinding(src, dst);
		if (binding == null)
			return getConfig().getDefaultLineColor();

		switch (hasErrorOrWarning(binding)) {
		case IMarker.SEVERITY_INFO:
			return getConfig().getDefaultLineColor();
		case IMarker.SEVERITY_WARNING:
			return getConfig().getLineWarningColor();
		case IMarker.SEVERITY_ERROR:
			return getConfig().getLineErrorColor();
		default:
			throw new RuntimeException("Unknown IMarker severity");
		}
	}

	@Override
	public Color getHighlightColor(Object rel) {
		return getConfig().getLineHighightColor();
	}

	@Override
	public int getLineWidth(Object rel) {
		return getConfig().getLineWidth();
	}

	@Override
	public IFigure getTooltip(Object entity) {
		return null;
	}

	protected GraphConfig getConfig() {
		return GraphColorService.getInstance().getConfig();
	}
}
