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
package fr.liglab.adele.cilia.workbench.designer.service.abstractreposervice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Base abstract class for implementing content providers. A ContentProvider
 * knows relationships between objects. It's useful for navigating an abstract
 * tree, representing our objects.
 * It's used for model navigation, and for tree rendering.
 */
public abstract class GenericContentProvider implements ITreeContentProvider {

	/** Maps to get the children from the parent. */
	private Map<Object, List<Object>> children = new HashMap<Object, List<Object>>();

	/** Map to get the parent from a child. */
	private Map<Object, Object> parent = new HashMap<Object, Object>();

	/**
	 * Adds the root.
	 *
	 * @param root the root
	 */
	protected void addRoot(Object root) {
		parent.put(root, new Object[0]);
		children.put(root, new ArrayList<Object>());
	}
	
	/**
	 * Add a relationship in the content provider.
	 * 
	 * IMPORTANT : if one argument is null, this function does nothing.
	 * 
	 * @param theParent
	 * @param theChild
	 */
	protected void addRelationship(Object theParent, Object theChild) {
		if (theParent != null && theChild != null) {
			parent.put(theChild, theParent);
			children.get(theParent).add(theChild);
			children.put(theChild, new ArrayList<Object>());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object
	 * )
	 */
	@Override
	public Object getParent(Object element) {
		return parent.get(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.
	 * Object)
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		List<Object> a = children.get(parentElement);
		if (a == null)
			return new Object[0];
		return a.toArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.
	 * Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		return (getChildren(element).length != 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.
	 * Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface
	 * .viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
}
