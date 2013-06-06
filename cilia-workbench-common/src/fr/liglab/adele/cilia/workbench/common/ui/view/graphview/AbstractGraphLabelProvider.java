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

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.graphics.Color;
import org.eclipse.zest.core.viewers.IConnectionStyleProvider;
import org.eclipse.zest.core.viewers.IEntityStyleProvider;
import org.eclipse.zest.core.widgets.ZestStyles;

import fr.liglab.adele.cilia.workbench.common.ui.view.CiliaLabelProvider;

/**
 * 
 * @author Etienne Gandrille
 */
public abstract class AbstractGraphLabelProvider extends CiliaLabelProvider implements IConnectionStyleProvider, IEntityStyleProvider {

	public static GraphConfig defaultConfig = new GraphConfig(null, null, -1);
	public static GraphConfig videoConfig = new GraphConfig(COLOR.BLACK.getColor(), COLOR.BLUE.getColor(), 2);

	private final GraphConfig config;

	public AbstractGraphLabelProvider() {
		this.config = defaultConfig;
	}

	public AbstractGraphLabelProvider(GraphConfig config) {
		this.config = config;
	}

	// ====================
	// IEntityStyleProvider
	// ====================

	@Override
	public Color getNodeHighlightColor(Object entity) {
		return null;
	}

	@Override
	public Color getBorderColor(Object entity) {
		return null;
	}

	@Override
	public Color getBorderHighlightColor(Object entity) {
		return null;
	}

	@Override
	public int getBorderWidth(Object entity) {
		return 0;
	}

	@Override
	public Color getBackgroundColour(Object entity) {
		return null;
	}

	@Override
	public Color getForegroundColour(Object entity) {
		return null;
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
		return config.getBaseColor();
	}

	@Override
	public Color getHighlightColor(Object rel) {
		return config.getHighightColor();
	}

	@Override
	public int getLineWidth(Object rel) {
		return config.getLineWidth();
	}

	@Override
	public IFigure getTooltip(Object entity) {
		return null;
	}

	public static class GraphConfig {

		private final Color baseColor;
		private final Color highightColor;
		private final int lineWidth;

		public GraphConfig(Color baseColor, Color highightColor, int lineWidth) {
			this.baseColor = baseColor;
			this.highightColor = highightColor;
			this.lineWidth = lineWidth;
		}

		public Color getBaseColor() {
			return baseColor;
		}

		public Color getHighightColor() {
			return highightColor;
		}

		public int getLineWidth() {
			return lineWidth;
		}
	}
}
