package fr.liglab.adele.cilia.workbench.designer.repositoryview;

import java.io.File;
import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import fr.liglab.adele.cilia.workbench.designer.metadataparser.Bundle;
import fr.liglab.adele.cilia.workbench.designer.metadataparser.MetadataException;

class StreamFromFileStorage implements IStorage {

	private String fileName;

	StreamFromFileStorage(String fileName) {
		this.fileName = fileName;
	}

	public InputStream getContents() throws CoreException {
		try {
			return Bundle.inputStreamFromFile(fileName);
		} catch (MetadataException e) {
			e.printStackTrace();
			return null;
		}
	}

	public IPath getFullPath() {
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class adapter) {
		return null;
	}

	public String getName() {
		int index = fileName.lastIndexOf(File.separator, fileName.length());
		if (index == -1)
			return fileName;
		else
			return fileName.substring(index + 1);
	}

	public boolean isReadOnly() {
		return true;
	}
}