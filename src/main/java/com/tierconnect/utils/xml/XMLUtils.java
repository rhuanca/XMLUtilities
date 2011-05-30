package com.tierconnect.utils.xml;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Node;

public class XMLUtils {

	public static String prettyPrint(Node doc)  {

		StringOutputStream out = new StringOutputStream();
		
		TransformerFactory tfactory = TransformerFactory.newInstance();
		Transformer serializer;
		try {
			serializer = tfactory.newTransformer();
			// Setup indenting to "pretty print"
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource xmlSource = new DOMSource(doc);
			StreamResult outputTarget = new StreamResult(out);
			serializer.transform(xmlSource, outputTarget);
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		}
		return out.getString();
	}

	static class StringOutputStream extends OutputStream {
		
		StringBuilder mBuf = new StringBuilder();
		public String getString() {
			return mBuf.toString();
		}
		
		@Override
		public void write(int b) throws IOException {
			mBuf.append((char) b);
		}
	}

}
