package fr.liglab.adele.cilia.workbench.designer.metadataparser;

public class MetadataException extends Exception {

	private static final long serialVersionUID = 6517857644001289861L;


	public MetadataException() {
		super();
	}
	
	public MetadataException(String message) {
		super(message);
	}
	
	public MetadataException(Throwable cause) {
		super(cause);
	}
	
	
	public MetadataException(String message, Throwable cause) {
		super(message, cause);
	}
}
