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
package fr.liglab.adele.cilia.workbench.common.providers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;

/**
 * An helper class for creating toggle variables.
 * 
 * @author Etienne Gandrille
 */
public class ToggleSourceProvider extends AbstractSourceProvider {

	/** The toggle variable name. */
	private final String VARIABLE_NAME;

	/** The name of the state, when the variable is <code>true</code>. */
	private final String STATE_TRUE;

	/** The name of the state, when the variable is <code>false</code>. */
	private final String STATE_FALSE;

	/** The variable current value. */
	private boolean value;

	/**
	 * Instantiates a new toggle source provider.
	 * 
	 * @param variable
	 *            the variable name
	 * @param stateTrue
	 *            The name of the state, when the variable is <code>true</code>
	 * @param stateFalse
	 *            The name of the state, when the variable is <code>false</code>
	 * @param initialValue
	 *            the initial boolean value
	 */
	public ToggleSourceProvider(String variable, String stateTrue, String stateFalse, boolean initialValue) {
		STATE_TRUE = stateTrue;
		STATE_FALSE = stateFalse;
		VARIABLE_NAME = variable;
		value = initialValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISourceProvider#getProvidedSourceNames()
	 */
	@Override
	public String[] getProvidedSourceNames() {
		return new String[] { VARIABLE_NAME };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISourceProvider#getCurrentState()
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Map getCurrentState() {
		Map<String, String> currentState = new HashMap<String, String>(1);
		String variableState = value ? STATE_TRUE : STATE_FALSE;
		currentState.put(VARIABLE_NAME, variableState);
		return currentState;
	}

	/**
	 * Set current state and fire notification if (and only if) state changes.
	 * 
	 * @param flag
	 *            new state.
	 */
	public void setValue(boolean flag) {
		if (this.value == flag)
			return; // no change
		this.value = flag;
		String currentState = value ? STATE_TRUE : STATE_FALSE;
		fireSourceChanged(ISources.WORKBENCH, VARIABLE_NAME, currentState);
	}

	/**
	 * Gets the state of the reload required variable.
	 * 
	 * @return the state of the reload required variable.
	 */
	public boolean getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISourceProvider#dispose()
	 */
	@Override
	public void dispose() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (value ? STATE_TRUE : STATE_FALSE);
	}
}
