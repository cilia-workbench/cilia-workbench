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
package fr.liglab.adele.cilia.workbench.common.view.ciliaerrorview;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;


/**
 * Static methods for creating and finding Cilia markers. An {@link IMarker} is
 * an object which can be used by Eclipse to display errors in an error view. As
 * soon as markers are created, they are automatically registered in the eclipse
 * workbench.
 * 
 * @author Etienne Gandrille
 */
public class CiliaMarkerUtil {

	/** The Cilia marker Type ID */
	public static String MARKER_TYPE = "fr.liglab.adele.cilia.workbench.common.marker";

	/**
	 * Creates an error Cilia marker.
	 * 
	 * @param description
	 *            the marker description
	 * @param rootSourceProvider
	 *            the root source provider, which should be a repository.
	 * @param sourceProvider
	 *            the source provider. Object responsible of this marker
	 *            creation.
	 * @return the marker
	 */
	public static IMarker createErrorMarker(String description, Object rootSourceProvider, Object sourceProvider) {
		return createMarker(IMarker.SEVERITY_ERROR, description, rootSourceProvider, sourceProvider);
	}

	/**
	 * Creates a warning Cilia marker.
	 * 
	 * @param description
	 *            the marker description
	 * @param rootSourceProvider
	 *            the root source provider, which should be a repository.
	 * @param sourceProvider
	 *            the source provider. Object responsible of this marker
	 *            creation.
	 * @return the marker
	 */
	public static IMarker createWarningMarker(String description, Object rootSourceProvider, Object sourceProvider) {
		return createMarker(IMarker.SEVERITY_WARNING, description, rootSourceProvider, sourceProvider);
	}

	/**
	 * Creates a Cilia marker.
	 * 
	 * @param severity
	 *            the markers's severity. see {@link IMarker} file for getting
	 *            constants.
	 * @param description
	 *            the marker description
	 * @param rootSourceProvider
	 *            the root source provider, which should be a repository.
	 * @param sourceProvider
	 *            the source provider. Object responsible of this marker
	 *            creation.
	 * @return the marker
	 */
	public static IMarker createMarker(int severity, String description, Object rootSourceProvider,
			Object sourceProvider) {

		IMarker marker = null;

		try {
			marker = ResourcesPlugin.getWorkspace().getRoot().createMarker(MARKER_TYPE);
			marker.setAttribute(IMarker.MESSAGE, description);
			marker.setAttribute(IMarker.SEVERITY, severity);
			marker.setAttribute(IMarker.TRANSIENT, true);
			if (rootSourceProvider != null)
				marker.setAttribute(RootSourceProviderField.FIELD_ID, rootSourceProvider);
			if (sourceProvider != null)
				marker.setAttribute(SourceProviderField.FIELD_ID, sourceProvider);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return marker;
	}

	/**
	 * Finds all existing Cilia markers, registered in the Eclipse Workbench.
	 * 
	 * @return
	 * @throws CoreException
	 */
	public static IMarker[] findMarkers() throws CoreException {

		List<IMarker> retval = new ArrayList<IMarker>();
		IMarker[] markers = ResourcesPlugin.getWorkspace().getRoot()
				.findMarkers(MARKER_TYPE, true, IResource.DEPTH_INFINITE);

		for (IMarker marker : markers)
			if (marker.exists())
				retval.add(marker);

		return retval.toArray(new IMarker[0]);
	}

	/**
	 * Finds markers, with a given root source provider.
	 * 
	 * @param sourceProvider
	 *            the source provider
	 * @return the markers
	 * @throws CoreException
	 *             the core exception
	 */
	public static IMarker[] findMarkers(Object rootSourceProvider) throws CoreException {

		List<IMarker> retval = new ArrayList<IMarker>();

		for (IMarker marker : findMarkers())
			if (marker.getAttribute(RootSourceProviderField.FIELD_ID) == rootSourceProvider)
				retval.add(marker);

		return retval.toArray(new IMarker[0]);
	}

	/**
	 * Deletes all markers, with a given root source provider.
	 * 
	 * @param sourceProvider
	 *            the source provider
	 * @return the markers
	 * @throws CoreException
	 *             the core exception
	 */
	public static void deleteMarkers(Object rootSourceProvider) throws CoreException {
		for (IMarker marker : findMarkers(rootSourceProvider))
			marker.delete();
	}
}
