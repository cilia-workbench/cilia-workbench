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
package fr.liglab.adele.cilia.components.collectors;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import fr.liglab.adele.cilia.Data;
import fr.liglab.adele.cilia.framework.AbstractCollector;

/**
 * 
 * @author Etienne Gandrille
 */
public class BasicCollector extends AbstractCollector implements Runnable {

	private static int counter = 0;

	public void start() {
		ScheduledThreadPoolExecutor se = new ScheduledThreadPoolExecutor(1);
		se.scheduleAtFixedRate(this, 0, 1, TimeUnit.SECONDS);
	}

	public void run() {
		Data data = new Data(Integer.toString(counter++));
		notifyDataArrival(data);
	}
}