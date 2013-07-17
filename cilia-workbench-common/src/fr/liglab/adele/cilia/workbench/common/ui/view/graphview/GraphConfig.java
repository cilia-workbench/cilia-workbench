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

import org.eclipse.swt.graphics.Color;

/**
 * Sets of colors used to draw graphs. Two sets are provided :
 * <ul>
 * <li>DEFAULT with the zest default colors</li>
 * <li>VIDEO cleaner with a videoprojector</li>
 * </ul>
 * 
 * @author Etienne Gandrille
 */
public enum GraphConfig {

	DEFAULT(COLOR.LIGHT_BLUE, COLOR.YELLOW, COLOR.DARK_BLUE, 1, COLOR.GRAY, COLOR.BLUE, COLOR.GRAY, COLOR.DARK_BLUE, 1), VIDEO(COLOR.LIGHT_BLUE, COLOR.YELLOW,
			COLOR.DARK_BLUE, 1, COLOR.GRAY, COLOR.BLUE, COLOR.BLACK, COLOR.BLUE, 2);

	private final Color nodeColor;
	private final Color nodeHighlightColor;
	private final Color nodeTextColor;
	private final int nodeBorderWidth;
	private final Color nodeBorderColor;
	private final Color nodeBorderHighlightColor;
	private final Color lineColor;
	private final Color lineHighightColor;
	private final int lineWidth;

	private GraphConfig(COLOR nodeColor, COLOR nodeHighlightColor, COLOR nodeTextColor, int nodeBorderWidth, COLOR nodeBorderColor,
			COLOR nodeBorderHighlightColor, COLOR lineColor, COLOR lineHighightColor, int lineWidth) {

		this.nodeColor = nodeColor.getColor();
		this.nodeHighlightColor = nodeHighlightColor.getColor();
		this.nodeTextColor = nodeTextColor.getColor();
		this.nodeBorderWidth = nodeBorderWidth;
		this.nodeBorderColor = nodeBorderColor.getColor();
		this.nodeBorderHighlightColor = nodeBorderHighlightColor.getColor();

		this.lineColor = lineColor.getColor();
		this.lineHighightColor = lineHighightColor.getColor();
		this.lineWidth = lineWidth;
	}

	// ====
	// NODE
	// ====

	public Color getNodeColor() {
		return nodeColor;
	}

	public Color getNodeHighlightColor() {
		return nodeHighlightColor;
	}

	public Color getNodeTextColor() {
		return nodeTextColor;
	}

	public int getNodeBorderWidth() {
		return nodeBorderWidth;
	}

	public Color getNodeBorderColor() {
		return nodeBorderColor;
	}

	public Color getNodeBorderHighlightColor() {
		return nodeBorderHighlightColor;
	}

	// ====
	// LINE
	// ====

	public Color getLineColor() {
		return lineColor;
	}

	public Color getLineHighightColor() {
		return lineHighightColor;
	}

	public int getLineWidth() {
		return lineWidth;
	}
}