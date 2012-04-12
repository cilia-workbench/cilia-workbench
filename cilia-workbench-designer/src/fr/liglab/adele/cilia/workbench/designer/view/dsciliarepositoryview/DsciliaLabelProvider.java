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
package fr.liglab.adele.cilia.workbench.designer.view.dsciliarepositoryview;

import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.Chain;
import fr.liglab.adele.cilia.workbench.designer.parser.dscilia.DsciliaFile;
import fr.liglab.adele.cilia.workbench.designer.view.repositoryview.LabelProvider;

/**
 * LabelProvider for the DSCilia repository view.
 * 
 * @author Etienne Gandrille
 */
public class DsciliaLabelProvider extends LabelProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.liglab.adele.cilia.workbench.designer.view.repositoryview.LabelProvider#getImageDescriptor(java.lang.Object)
	 */
	protected ImageDescriptorEnum getImageDescriptor(Object obj) {
		ImageDescriptorEnum imageName;

		if (obj instanceof DsciliaFile)
			imageName = ImageDescriptorEnum.FILE;
		else if (obj instanceof Chain)
			imageName = ImageDescriptorEnum.CHAIN;
		else
			throw new RuntimeException("Unsupported type: " + obj.getClass());

		return imageName;
	}
}
