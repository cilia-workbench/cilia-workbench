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
package fr.liglab.adele.cilia.workbench.restmonitoring.parser.platform;

import java.util.ArrayList;
import java.util.List;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaError;
import fr.liglab.adele.cilia.workbench.common.marker.CiliaFlag;
import fr.liglab.adele.cilia.workbench.common.marker.ErrorsAndWarningsFinder;
import fr.liglab.adele.cilia.workbench.common.parser.element.IDispatcher;
import fr.liglab.adele.cilia.workbench.common.parser.element.IMediator;
import fr.liglab.adele.cilia.workbench.common.parser.element.IPort;
import fr.liglab.adele.cilia.workbench.common.parser.element.IProcessor;
import fr.liglab.adele.cilia.workbench.common.parser.element.IScheduler;
import fr.liglab.adele.cilia.workbench.common.parser.element.Property;
import fr.liglab.adele.cilia.workbench.common.service.Changeset;
import fr.liglab.adele.cilia.workbench.common.service.Mergeable;
import fr.liglab.adele.cilia.workbench.common.ui.view.propertiesview.DisplayedInPropertiesView;

/**
 * 
 * @author Etienne Gandrille
 */
public class MediatorInstance implements IMediator, DisplayedInPropertiesView, ErrorsAndWarningsFinder, Mergeable {

	private final String name;
	private final PlatformChain chain;

	public MediatorInstance(String name, PlatformChain chain) {
		this.name = name;
		this.chain = chain;
	}

	@Override
	public Object getId() {
		return chain.getId().toString() + name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public List<Changeset> merge(Object other) throws CiliaException {
		return new ArrayList<Changeset>();
	}

	@Override
	public CiliaFlag[] getErrorsAndWarnings() {
		CiliaFlag e1 = CiliaError.checkStringNotNullOrEmpty(this, name, "name");
		return CiliaFlag.generateTab(e1);
	}

	@Override
	public List<? extends IPort> getPorts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends IPort> getInPorts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends IPort> getOutPorts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasInPort(String name) {
		return (getInPorts().size() != 0);
	}

	@Override
	public boolean hasOutPort(String name) {
		return (getOutPorts().size() != 0);
	}

	@Override
	public ComponentNature getNature() {
		return ComponentNature.INSTANCE;
	}

	@Override
	public List<? extends Property> getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Property getProperty(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IScheduler getScheduler() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IProcessor getProcessor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDispatcher getDispatcher() {
		// TODO Auto-generated method stub
		return null;
	}
}
