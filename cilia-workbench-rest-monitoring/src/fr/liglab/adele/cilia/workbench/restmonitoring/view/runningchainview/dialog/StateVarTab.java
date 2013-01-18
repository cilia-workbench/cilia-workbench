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
package fr.liglab.adele.cilia.workbench.restmonitoring.view.runningchainview.dialog;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import fr.liglab.adele.cilia.workbench.common.misc.ImageBuilder;

/**
 * 
 * @author Etienne Gandrille
 */
public class StateVarTab {

	private static final Image imageChecked = ImageBuilder.getINSTANCE().getImage("icons/misc/checked.gif");
	private static final Image imageUnchecked = ImageBuilder.getINSTANCE().getImage("icons/misc/unchecked.gif");
	private TableViewer viewer;

	private final Composite composite;

	public StateVarTab(CTabFolder folder, String tabTitle) {
		CTabItem item = new CTabItem(folder, SWT.NONE);
		item.setText(tabTitle);
		composite = createComposite(folder);
		item.setControl(composite);
	}

	public Composite getComposite() {
		return composite;
	}

	public void setLayoutData(Object layoutData) {
		composite.setLayoutData(layoutData);
	}

	private void updateViewer() {
		viewer.setInput(getStateVariables());
		viewer.refresh();
	}

	private List<StateVar> getStateVariables() {
		return StateVarModelProvider.INSTANCE.getStateVar();
	}

	private void updateEnableVariable(StateVar stateVar, boolean isEnabled) {
		// TODO send message to platform
	}

	private Composite createComposite(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		// Label
		final Label label = new Label(composite, SWT.WRAP);
		label.setText("message in composite");
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// editor
		this.viewer = createViewer(composite);
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);

		return composite;
	}

	private TableViewer createViewer(Composite parent) {
		TableViewer viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		createColumns(parent, viewer);
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setInput(getStateVariables());

		return viewer;
	}

	private void createColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = { "", "name", "value" };
		int[] bounds = { 16, 100, 100 };

		TableViewerColumn col = createTableViewerColumn(viewer, titles[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return "";
			}

			@Override
			public Image getImage(Object element) {
				StateVar stateVar = (StateVar) element;
				if (stateVar.isEnabled())
					return imageChecked;
				else
					return imageUnchecked;
			}
		});
		col.setEditingSupport(new ActivatedEditingSupport(viewer));

		col = createTableViewerColumn(viewer, titles[1], bounds[1], 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				StateVar stateVar = (StateVar) element;
				return stateVar.getName();
			}
		});

		col = createTableViewerColumn(viewer, titles[2], bounds[2], 2);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				StateVar stateVar = (StateVar) element;
				return stateVar.getValue();
			}
		});
	}

	private static TableViewerColumn createTableViewerColumn(TableViewer viewer, String title, int bound, final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(false);
		return viewerColumn;
	}

	public class ActivatedEditingSupport extends EditingSupport {

		public ActivatedEditingSupport(TableViewer viewer) {
			super(viewer);
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return new CheckboxCellEditor(null, SWT.CHECK | SWT.READ_ONLY);
		}

		@Override
		protected boolean canEdit(Object element) {
			return true;
		}

		@Override
		protected Object getValue(Object element) {
			StateVar stateVar = (StateVar) element;
			return stateVar.isEnabled();
		}

		@Override
		protected void setValue(Object element, Object value) {
			StateVar stateVar = (StateVar) element;
			updateEnableVariable(stateVar, (Boolean) value);
			// viewer.update(element, null);
			updateViewer();
		}
	}
}
