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
package fr.liglab.adele.cilia.workbench.designer.service.common;

/**
 * Exception thrown while parsing a metadata.xml file which is not well formed.
 * 
 * @author Etienne Gandrille
 */
public class MetadataException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6517857644001289861L;

	/**
	 * Instantiates a new metadata exception.
	 */
	public MetadataException() {
		super();
	}

	/**
	 * Instantiates a new metadata exception.
	 * 
	 * @param message
	 *            the message
	 */
	public MetadataException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new metadata exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public MetadataException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new metadata exception.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public MetadataException(String message, Throwable cause) {
		super(message, cause);
	}
}
