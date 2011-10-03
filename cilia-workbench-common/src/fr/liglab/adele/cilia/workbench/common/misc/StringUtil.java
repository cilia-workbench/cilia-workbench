package fr.liglab.adele.cilia.workbench.common.misc;

import java.io.File;

import com.google.common.base.Preconditions;

public class StringUtil {

	private static final String SEPARATOR = ":";

	public static String getBeforeSeparatorOrAll(String name) {
		Preconditions.checkNotNull(name);
		
		int index = name.indexOf(SEPARATOR);
		if (index == -1)
			return name;
		return name.substring(0, index);
	}
	
	
	public static String getFileName(String path) {
		Preconditions.checkNotNull(path);
		
		int index = path.lastIndexOf(File.separator, path.length());
		if (index == -1)
			return path;
		else
			return path.substring(index + 1);
	}
}
