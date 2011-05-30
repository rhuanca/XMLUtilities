package com.tierconnect.utils.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class SimpleDocumentBuilder {
	private static DocumentBuilder documentBuilder;

	static {
		try {
			documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("Unable to get document builder. - "+ e.getMessage(), e);
		}
	}
	
	/**
	 * Allows to create a new documents instance.
	 * 
	 * @param xml
	 * @return
	 */
	public static Document buildDocument(String xml, String encoding) {
		Document doc = null;
		try {
			doc = documentBuilder.parse(new ByteArrayInputStream(xml
					.getBytes(encoding)));
		} catch (IOException e) {
			throw new RuntimeException(
					"Unable to read xml - " + e.getMessage(), e);
		} catch (SAXParseException e) {
			throw new RuntimeException("Unable to parse xml - Line: "
					+ e.getLineNumber() + " - " + e.getMessage(), e);
		} catch (SAXException e) {
			throw new RuntimeException("Unable to parse xml - "
					+ e.getMessage(), e);
		}
		return doc;
	}

	public static Document buildDocument(InputStream stream) {
        Document doc = null;
		try {
			doc = documentBuilder.parse(stream);
		} catch (SAXException e) {
			throw new RuntimeException("Unable to parse document",e);
		} catch (IOException e) {
			throw new RuntimeException("Unable to parse document",e);
		}
        return doc;
	}
}
