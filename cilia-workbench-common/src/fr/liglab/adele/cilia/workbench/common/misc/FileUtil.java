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

package fr.liglab.adele.cilia.workbench.common.misc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import fr.liglab.adele.cilia.workbench.common.cilia.CiliaException;

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

	public static void closeFile(ZipFile file) {
		if (file != null) {
			try {
				file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeStream(InputStream stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeStream(OutputStream stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static JarFile getJarFile(File jarFile) throws CiliaException {
		try {
			return new JarFile(jarFile);
		} catch (IOException e) {
			throw new CiliaException("Can't open jar file " + jarFile.getAbsolutePath(), e);
		}
	}

	/**
	 * Gets an Input stream from a file embedded in a jar archive.
	 * 
	 * @param jarFile
	 *            the jar archive file, on the hard disk.
	 * @param fileName
	 *            the file name, in the archive. The file must be located at the
	 *            archive root.
	 * @return the input stream
	 * @throws CiliaException
	 *             if any error.
	 */
	public static InputStream inputStreamFromFileInJarArchive(File jarFile, String fileName) throws CiliaException {

		// Jar file
		JarFile file = getJarFile(jarFile);

		// Entry
		ZipEntry entry = file.getEntry(fileName);
		if (entry == null) {
			closeFile(file);
			throw new CiliaException("File " + fileName + " not found in archive");
		}

		// Stream
		BufferedInputStream is;
		try {
			is = new BufferedInputStream(file.getInputStream(entry));
		} catch (IOException e) {
			closeFile(file);
			throw new CiliaException("Can't access file " + fileName + " in jar file " + jarFile.getAbsolutePath(), e);
		}

		return is;
	}

	public static InputStream inputStreamFromFileInJarInDP(File jarFile, String subJar, String fileInSubJar) throws CiliaException {
		JarFile outerJarFile = null;
		InputStream in = null;
		File tempFile = null;
		FileOutputStream tempOut = null;
		InputStream retval = null;

		// Jar file
		outerJarFile = FileUtil.getJarFile(jarFile);

		// Entry in jar file
		ZipEntry outerZipEntry = outerJarFile.getEntry(subJar);
		if (outerZipEntry == null) {
			FileUtil.closeFile(outerJarFile);
			throw new CiliaException("File " + subJar + " not found in archive " + jarFile.getName());
		}
		JarEntry outerJarEntry = new JarEntry(outerZipEntry);

		Exception exception = null;
		try {
			in = outerJarFile.getInputStream(outerJarEntry);
			tempFile = File.createTempFile("tempFile", "jar");
			tempOut = new FileOutputStream(tempFile);
			copyStream(in, tempOut, outerJarEntry);
			retval = FileUtil.inputStreamFromFileInJarArchive(tempFile, fileInSubJar);
		} catch (Exception e) {
			exception = e;
		} finally {
			FileUtil.closeFile(outerJarFile);
			FileUtil.closeStream(in);
			FileUtil.closeStream(tempOut);
		}

		if (exception != null)
			throw new CiliaException(exception);

		return retval;
	}

	private static void copyStream(InputStream in, OutputStream out, JarEntry entry) throws IOException {
		byte[] buffer = new byte[1024 * 4];
		long count = 0;
		int n = 0;
		long size = entry.getSize();
		while (-1 != (n = in.read(buffer)) && count < size) {
			out.write(buffer, 0, n);
			count += n;
		}
	}
}
