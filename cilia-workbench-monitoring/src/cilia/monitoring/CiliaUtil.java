package cilia.monitoring;

import fr.liglab.adele.cilia.AdapterReadOnly;
import fr.liglab.adele.cilia.ChainReadOnly;
import fr.liglab.adele.cilia.CiliaContextReadOnly;
import fr.liglab.adele.cilia.MediatorReadOnly;
import fr.liglab.adele.cilia.management.monitoring.MonitoredApplication;

/**
 * A few static methods, for common processing on Cila model objects.
 */
public abstract class CiliaUtil {

	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * Gets the ChainReadOnly which contains an adapter.
	 * 
	 * @param ccro
	 *            the CiliaContextReadOnly
	 * @param adapterId
	 *            the adapter id
	 * @return the ChainReadOnly object, null if not found.
	 */
	public static ChainReadOnly getAdapterParent(CiliaContextReadOnly ccro, String adapterId) {
		for (Object o1 : ccro.getAllChains()) {
			ChainReadOnly c = (ChainReadOnly) o1;
			for (Object o2 : c.getAdapters()) {
				AdapterReadOnly a = (AdapterReadOnly) o2;
				if (a.getId().equals(adapterId))
					return c;
			}
		}
		return null;
	}

	/**
	 * Gets the ChainReadOnly which contains an adapter.
	 * 
	 * @param ccro
	 *            the CiliaContextReadOnly
	 * @param adapter
	 *            the adapter
	 * @return the ChainReadOnly object, null if not found.
	 */
	public static ChainReadOnly getAdapterParent(CiliaContextReadOnly ccro, AdapterReadOnly adapter) {
		return getAdapterParent(ccro, adapter.getId());
	}

	/**
	 * Gets the ChainReadOnly which contains a mediator.
	 * 
	 * @param ccro
	 *            the CiliaContextReadOnly
	 * @param mediatorId
	 *            the mediator id
	 * @return the ChainReadOnly object, null if not found.
	 */
	public static ChainReadOnly getMediatorParent(CiliaContextReadOnly ccro, String mediatorId) {
		for (Object o1 : ccro.getAllChains()) {
			ChainReadOnly c = (ChainReadOnly) o1;
			for (Object o2 : c.getMediators()) {
				MediatorReadOnly m = (MediatorReadOnly) o2;
				if (m.getId().equals(mediatorId))
					return c;
			}
		}
		return null;
	}

	/**
	 * Gets the ChainReadOnly which contains a mediator.
	 * 
	 * @param ccro
	 *            the CiliaContextReadOnly
	 * @param mediator
	 *            the mediator
	 * @return the ChainReadOnly object, null if not found.
	 */
	public static ChainReadOnly getMediatorParent(CiliaContextReadOnly ccro, MediatorReadOnly mediator) {
		return getMediatorParent(ccro, mediator.getId());
	}

	public static Object getMonitoredElement(Object source) {

		MonitoredApplication monitoredApp = Activator.getMonitoredApplication();

		if (source == null || monitoredApp == null)
			return null;
		if (source instanceof CiliaContextReadOnly)
			return monitoredApp;
		if (source instanceof ChainReadOnly) {
			ChainReadOnly chain = (ChainReadOnly) source;
			String id = chain.getId();
			return monitoredApp.getMonitoredChain(id);
		}
		if (source instanceof MediatorReadOnly || source instanceof AdapterReadOnly) {
			CiliaContextReadOnly ccro = monitoredApp.getCiliaContextRO();
			if (ccro == null)
				return null;

			ChainReadOnly parent;
			String componentId;
			if (source instanceof AdapterReadOnly) {
				AdapterReadOnly adapter = (AdapterReadOnly) source;
				parent = CiliaUtil.getAdapterParent(ccro, adapter);
				componentId = adapter.getId();
			}
			else {
				MediatorReadOnly mediator = (MediatorReadOnly) source;
				parent = CiliaUtil.getMediatorParent(ccro, mediator);
				componentId = mediator.getId();
			}
			
			String chainId = parent.getId();
			return monitoredApp.getMonitoredComponent(chainId, componentId);
		}

		return null;
	}
}
