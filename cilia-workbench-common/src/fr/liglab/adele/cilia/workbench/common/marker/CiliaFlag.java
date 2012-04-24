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

/**
 * 
 * @author Etienne Gandrille
 */
public class CiliaFlag {

	private int severity;
	private String message;
	private Object sourceProvider;

	public CiliaFlag(int severity, String message, Object sourceProvider) {
		this.severity = severity;
		this.message = message;
		this.sourceProvider = sourceProvider;
	}

	public static CiliaFlag[] generateTab(CiliaFlag... flags) {
		List<CiliaFlag> retval = new ArrayList<CiliaFlag>();

		for (CiliaFlag flag : flags)
			if (flag != null)
				retval.add(flag);

		return retval.toArray(new CiliaFlag[0]);
	}

	public static CiliaFlag[] generateTab(Iterable<CiliaFlag> tab, CiliaFlag... flags) {
		List<CiliaFlag> retval = new ArrayList<CiliaFlag>();

		for (CiliaFlag flag : tab)
			if (flag != null)
				retval.add(flag);

		for (CiliaFlag flag : flags)
			if (flag != null)
				retval.add(flag);

		return retval.toArray(new CiliaFlag[0]);
	}

	public static CiliaFlag[] generateTab(CiliaFlag[] tab, CiliaFlag... flags) {
		List<CiliaFlag> retval = new ArrayList<CiliaFlag>();

		for (CiliaFlag flag : tab)
			if (flag != null)
				retval.add(flag);

		for (CiliaFlag flag : flags)
			if (flag != null)
				retval.add(flag);

		return retval.toArray(new CiliaFlag[0]);
	}

	public int getSeverity() {
		return severity;
	}

	public String getMessage() {
		return message;
	}

	public Object getSourceProvider() {
		return sourceProvider;
	}
}
