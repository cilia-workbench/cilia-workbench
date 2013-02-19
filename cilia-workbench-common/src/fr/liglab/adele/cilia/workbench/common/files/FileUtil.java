/**
 * Copyright 2012-2013 France Télécom 
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

package fr.liglab.adele.cilia.workbench.common.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;

/**
 * 
 * @author Etienne Gandrille
 */
public class FileUtil {

	public static File[] getFiles(File dir, final String extension) {
		if (dir == null)
			return new File[0];
		File[] list = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(extension);
			}
		});

		if (list == null)
			return new File[0];
		else
			return list;
	}

	public static String sha1sum(File file) throws FileNotFoundException {
		String localSha1Sum = null;
		if (file.exists() && file.isFile() && file.canRead()) {
			DigestInputStream dis = null;
			try {
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				dis = new DigestInputStream(new FileInputStream(file), md);
				dis.on(true);

				while (dis.read() != -1) {
					// do nothing, but loop !
				}
				byte[] b = md.digest();
				localSha1Sum = getHexString(b);
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				if (dis != null) {
					try {
						dis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			throw new FileNotFoundException("file not found " + file.getName());
		}
		return localSha1Sum;
	}

	private static String getHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (byte b : bytes) {
			if (b <= 0x0F && b >= 0x00)
				sb.append('0');
			sb.append(String.format("%x", b));
		}
		return sb.toString();
	}
}
