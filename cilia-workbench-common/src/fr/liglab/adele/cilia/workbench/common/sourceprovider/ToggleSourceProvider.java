package fr.liglab.adele.cilia.workbench.common.sourceprovider;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;

public class ToggleSourceProvider extends AbstractSourceProvider {

	private static String STATE_TRUE;
	private static String STATE_FALSE;
	private static String VARIABLE_NAME; 
	private boolean value;
	
	public ToggleSourceProvider(String variable, String stateTrue, String stateFalse, boolean defaultValue) {
		STATE_TRUE = stateTrue;
		STATE_FALSE = stateFalse;
		VARIABLE_NAME = variable;
		value = defaultValue;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISourceProvider#getProvidedSourceNames()
	 */
	@Override
	public String[] getProvidedSourceNames() {
		return new String[] { VARIABLE_NAME };
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISourceProvider#getCurrentState()
	 */
	@Override
	public Map<String, String> getCurrentState() {
		Map<String, String> currentState = new HashMap<String, String>(1);
		String variableState = value ? STATE_TRUE : STATE_FALSE;
		currentState.put(VARIABLE_NAME, variableState);
		return currentState;
	}
	
	/**
	 * Set current state and fire notification if (and only if) state changes.
	 * 
	 * @param flag new state.
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
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISourceProvider#dispose()
	 */
	@Override
	public void dispose() {
	}
	
	@Override
	public String toString() {
		return (value ? STATE_TRUE : STATE_FALSE);
	}
}
