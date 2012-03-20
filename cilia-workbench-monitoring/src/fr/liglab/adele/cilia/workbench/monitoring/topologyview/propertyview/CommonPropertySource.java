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
package fr.liglab.adele.cilia.workbench.monitoring.topologyview.propertyview;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;


import fr.liglab.adele.cilia.management.monitoring.RuntimePerformance;
import fr.liglab.adele.cilia.management.monitoring.StateVariableSet;
import fr.liglab.adele.cilia.workbench.monitoring.CiliaUtil;

/**
 * A high level abstract class, used to handle Cilia PropertySource.
 *
 * @param <T> the generic type
 * @author Etienne Gandrille
 */
public abstract class CommonPropertySource<T> implements IPropertySource {

	/** The model object, source of the properties. */
	protected T modelObject;

	/* Properties categories. */
	protected final String BASIC_PROPERTIES_CATEGORY = "Basic properties";
	protected final String STATE_VARIABLES_CATEGORY = "State variables";

	/** List of basic properties descriptor. */
	private IPropertyDescriptor[] basicPropertyDescriptors = null;

	/** Prefix used to compute properties names. */
	protected final String PROPERTY_PREFIX = this.getClass().getName() + ".";

	/**
	 * Constructor. Child class MUST link to this constructor.
	 * 
	 * @param model
	 *            the model object.
	 */
	public CommonPropertySource(T model) {
		modelObject = model;
	}

	/* ================================================ */
	/* METHODS WHICH MUST BE IMPLEMENTED BY CHILD CLASS */
	/* ================================================ */

	/**
	 * Gets an array with all the basic property descriptors.
	 *
	 * @return the basic property descriptors
	 */
	public abstract IPropertyDescriptor[] getBasicPropertyDescriptors();

	/**
	 * Gets a basic property value from its id.
	 *
	 * @param id the id
	 * @return the basic property value
	 */
	public abstract Object getBasicPropertyValue(Object id);

	/* ========================================= */
	/* UTILITY METHODS FOR CHILD IMPLEMENTATIONS */
	/* ========================================= */

	/**
	 * Creates the new basic property descriptor.
	 *
	 * @param propertyId the property id
	 * @param displayName the display name
	 * @return the property descriptor
	 */
	protected IPropertyDescriptor createBasicPropertyDescriptor(String propertyId, String displayName) {
		PropertyDescriptor descriptor = new PropertyDescriptor(propertyId, displayName);
		descriptor.setCategory(BASIC_PROPERTIES_CATEGORY);
		return descriptor;
	}

	/**
	 * Returns true if the elements has state variables. This method is not
	 * designed to be overwritten. If you want to specify if an element has
	 * state variable, please use
	 *
	 * @return true, if successful
	 * {@link CommonPropertySource#getRuntimeElement()} instead.
	 */
	public final boolean hasStateVariable() {
		return getRuntimeElement() != null;
	}

	/* ======== */
	/* INTERNAL */
	/* ======== */

	private final String STATE_VARIABLE_UNAVAILABLE = "State variable unavailable";

	/**
	 * Gets the runtime element, or null if not available.
	 *
	 * @return the runtime element
	 */
	private RuntimePerformance getRuntimeElement() {
		
		Object me = CiliaUtil.getMonitoredElement(modelObject);
		if (me != null && me instanceof RuntimePerformance)
			return (RuntimePerformance) me;
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
	 */
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {

		if (basicPropertyDescriptors == null)
			basicPropertyDescriptors = getBasicPropertyDescriptors();

		IPropertyDescriptor[] stateVariablePropertyDescriptor = getStateVariablePropertyDescriptors();
		if (stateVariablePropertyDescriptor == null)
			return basicPropertyDescriptors;

		List<IPropertyDescriptor> list = new ArrayList<IPropertyDescriptor>();
		for (IPropertyDescriptor desc : basicPropertyDescriptors)
			list.add(desc);
		for (IPropertyDescriptor desc : stateVariablePropertyDescriptor)
			list.add(desc);

		return list.toArray(new IPropertyDescriptor[0]);
	}

	/**
	 * Gets a propertyDescriptor array for all the state variables.
	 *
	 * @return the array.
	 */
	private IPropertyDescriptor[] getStateVariablePropertyDescriptors() {

		RuntimePerformance runtimePerf = getRuntimeElement();
		if (runtimePerf == null)
			return null;

		List<IPropertyDescriptor> list = new ArrayList<IPropertyDescriptor>();
		for (String key : runtimePerf.getStateVariableSet().keysStateVariable()) {
			PropertyDescriptor descriptor = new PropertyDescriptor(PROPERTY_PREFIX + key, key.replaceAll("\\.", " "));
			descriptor.setCategory(STATE_VARIABLES_CATEGORY);
			list.add(descriptor);
		}

		return list.toArray(new IPropertyDescriptor[0]);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 */
	@Override
	public Object getPropertyValue(Object id) {

		Object basicPropertyValue = getBasicPropertyValue(id);
		if (basicPropertyValue != null)
			return basicPropertyValue;

		Object stateVariableValue = getStateVariableValue(id);
		if (stateVariableValue != null)
			return stateVariableValue;

		// Not found
		return null;
	}

	/**
	 * Gets the state variable value from its id.
	 *
	 * @param id the id
	 * @return the state variable value, or null if not available.
	 */
	private Object getStateVariableValue(Object id) {

		// Is state variable set available ?
		if (!(id instanceof String))
			return null;
		String key = (String) id;
		if (!key.startsWith(PROPERTY_PREFIX))
			return null;
		if (getRuntimeElement() == null)
			return null;

		// Getting state variable set
		StateVariableSet set = getRuntimeElement().getStateVariableSet();

		String stateVariableName = key.substring(PROPERTY_PREFIX.length());
		Object[] measures = set.getMeasures(stateVariableName);

		if (measures != null) {
			if (measures.length != 0)
				return measures[measures.length - 1];
			else
				return STATE_VARIABLE_UNAVAILABLE;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
	 */
	@Override
	public Object getEditableValue() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.IPropertySource#isPropertySet(java.lang
	 * .Object)
	 */
	@Override
	public boolean isPropertySet(Object id) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.IPropertySource#resetPropertyValue(java
	 * .lang.Object)
	 */
	@Override
	public void resetPropertyValue(Object id) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java
	 * .lang.Object, java.lang.Object)
	 */
	@Override
	public void setPropertyValue(Object id, Object value) {
	}
}
