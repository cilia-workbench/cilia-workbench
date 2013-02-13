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
package fr.liglab.adele.cilia.workbench.helpers.collectors;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import fr.liglab.adele.cilia.framework.AbstractCollector;

/**
 * Base class for implementing periodic collectors.
 * 
 * @author Etienne Gandrille
 */
public abstract class PeriodicCollector extends AbstractCollector implements Runnable {

	/**
	 * Initialize the thread.
	 */
	public void start() {
		ScheduledThreadPoolExecutor se = new ScheduledThreadPoolExecutor(1);
		se.scheduleAtFixedRate(this, getPeriodInMillis(), getPeriodInMillis(), TimeUnit.MILLISECONDS);
	}

	/**
	 * The action itself, called each period.
	 */
	public abstract void run();

	/**
	 * Gets the period in milliseconds between two calls of {@link #run()}
	 * method.
	 * 
	 * @return
	 */
	public int getPeriodInMillis() {
		return 1000;
	}
}
