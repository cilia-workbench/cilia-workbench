package fr.liglab.adele.cilia.workbench.monitoring.topologyview;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;

/**
 * Handles the reload require variable using a SourceProvider. 
 */
public class VariablesSourceProvider extends AbstractSourceProvider {

	/**
	 * The name of the reload required variable.
	 */
	public final static String RELOAD_REQUIRED_VARIABLE = "fr.liglab.adele.cilia.workbench.monitoring.topologyview.reloadRequired";

	/**
	 * The enabled state value.
	 */
	private final static String RELOAD_REQUIRED = "required";

	/**
	 * The disabled state value.
	 */
	private final static String RELOAD_NOT_REQUIRED = "not required";

	/**
	 * The current state of the variable.
	 */
	private boolean reloadRequiredValue = true;

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISourceProvider#getProvidedSourceNames()
	 */
	@Override
	public String[] getProvidedSourceNames() {
		return new String[] { RELOAD_REQUIRED_VARIABLE };
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISourceProvider#getCurrentState()
	 */
	@Override
	public Map<String, String> getCurrentState() {
		Map<String, String> currentState = new HashMap<String, String>(1);
		String variableState = reloadRequiredValue ? RELOAD_REQUIRED : RELOAD_NOT_REQUIRED;
		currentState.put(RELOAD_REQUIRED_VARIABLE, variableState);
		return currentState;
	}
	
	/**
	 * Set current state and fire notification if (and only if) state changes.
	 * 
	 * @param flag new state.
	 */
	public void setReloadRequired(boolean flag) {
		if (this.reloadRequiredValue == flag)
			return; // no change
		this.reloadRequiredValue = flag;
		String currentState = reloadRequiredValue ? RELOAD_REQUIRED : RELOAD_NOT_REQUIRED;
		fireSourceChanged(ISources.WORKBENCH, RELOAD_REQUIRED_VARIABLE, currentState);
	}

	/**
	 * Gets the state of the reload required variable.
	 * 
	 * @return the state of the reload required variable.
	 */
	public boolean getReloadRequired() {
		return reloadRequiredValue;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISourceProvider#dispose()
	 */
	@Override
	public void dispose() {
	}
}
