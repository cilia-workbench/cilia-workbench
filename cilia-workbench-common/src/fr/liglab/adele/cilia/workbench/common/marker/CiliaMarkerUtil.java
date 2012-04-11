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
package fr.liglab.adele.cilia.workbench.common.marker;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import fr.liglab.adele.cilia.workbench.common.view.ciliaerrorview.FilePathField;
import fr.liglab.adele.cilia.workbench.common.view.ciliaerrorview.RootSourceProviderField;
import fr.liglab.adele.cilia.workbench.common.view.ciliaerrorview.SourceProviderField;

/**
 * Static methods for creating and finding cilia markers.
 * 
 * @author Etienne Gandrille
 */
public class CiliaMarkerUtil {

	/** The Cilia marker Type ID */
	public static String MARKER_TYPE = "fr.liglab.adele.cilia.workbench.common.marker";

	/**
	 * Creates a Cilia marker.
	 * 
	 * @param description
	 *            the standard marker description
	 * @param sourceProvider
	 *            the source provider. It should be a repository
	 * @param filePath
	 *            the file path, stored *outside* the standard filepath field.
	 * @return the marker
	 */
	public static IMarker createMarker(String description, Object rootSourceProvider, Object sourceProvider, String filePath) {

		IMarker marker = null;

		try {
			marker = ResourcesPlugin.getWorkspace().getRoot().createMarker(MARKER_TYPE);
			marker.setAttribute(IMarker.MESSAGE, description);
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			marker.setAttribute(IMarker.TRANSIENT, true);
			if (rootSourceProvider != null)
				marker.setAttribute(RootSourceProviderField.FIELD_ID, rootSourceProvider);
			if (sourceProvider != null)
				marker.setAttribute(SourceProviderField.FIELD_ID, sourceProvider);
			if (filePath != null)
				marker.setAttribute(FilePathField.FIELD_ID, filePath);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return marker;
	}

	/**
	 * Finds all cilia markers.
	 * 
	 * @return
	 * @throws CoreException
	 */
	public static IMarker[] findMarkers() throws CoreException {
		return ResourcesPlugin.getWorkspace().getRoot().findMarkers(MARKER_TYPE, true, IResource.DEPTH_INFINITE);
	}

	/**
	 * Find markers, with a given root source provider.
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
	 * Deletes markers, with a given root source provider.
	 * 
	 * @param sourceProvider
	 *            the source provider
	 * @return the markers
	 * @throws CoreException
	 *             the core exception
	 */
	public static void deleteMarkers(Object rootSourceProvider) throws CoreException {

		for (IMarker marker : findMarkers(rootSourceProvider))
			if (marker.exists())
				marker.delete();
	}
}
