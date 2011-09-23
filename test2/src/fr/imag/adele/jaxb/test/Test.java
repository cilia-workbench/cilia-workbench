package fr.imag.adele.jaxb.test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;


public class Test {

	
	public static void main(String[] args) throws IOException, SAXException, JAXBException {
	
		// Schema construction
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = sf.newSchema(new File("NewXMLSchema.xsd"));

//		JAXBContext jc = JAXBContext.newInstance(Ipojo.class);
//
//		Unmarshaller unmarshaller = jc.createUnmarshaller();
//		unmarshaller.setSchema(schema);
//
//		Ipojo a = (Ipojo) unmarshaller.unmarshal(new File("a.xml"));
//		System.out.println();
//
//		Marshaller marshaller = jc.createMarshaller();
//		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//		marshaller.marshal(a, new File("b.xml"));
//		marshaller.marshal(a, System.out);
		
	}

	public static void test() throws IOException, SAXException, JAXBException {
		
		String name = "simple-test-0.0.1-SNAPSHOT.jar";
		
		// JarEntry		
		JarFile file = new JarFile(name);
		ZipEntry entry = file.getEntry("Seattle.xml");
		
		BufferedInputStream is = new BufferedInputStream(file.getInputStream(entry));
		
		// Schema construction
		SchemaFactory sf = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = sf.newSchema(new File("NewXMLSchema.xsd"));

//		JAXBContext jc = JAXBContext.newInstance(Ipojo.class);
//
//		Unmarshaller unmarshaller = jc.createUnmarshaller();
//		unmarshaller.setSchema(schema);
//
//		Ipojo a = (Ipojo) unmarshaller.unmarshal(is);
//		System.out.println();
	}
}
