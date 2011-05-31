package renidev.utils.xml;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import static org.junit.Assert.*;

public class SimpleDocumentBuilderTest {

	@Test
	public void one_node(){
		String xml = "<data>hello world</data>";
		Document doc = SimpleDocumentBuilder.buildDocument(xml);
		Node dataNode = doc.getFirstChild();
		assertEquals(1, dataNode.getChildNodes().getLength());
		assertEquals("hello world", dataNode.getTextContent());
	}
	
	@Test
	public void one_level(){
		String xml = "<data><child>hello world</child></data>";
		Document doc = SimpleDocumentBuilder.buildDocument(xml);
		Node dataNode = doc.getFirstChild();
		assertEquals(1, dataNode.getChildNodes().getLength());
		assertEquals("hello world", dataNode.getFirstChild().getTextContent());
	}
	
	@Test 
	public void test_exception(){
		String xml = "<data>hello world<data>";
		try {
			@SuppressWarnings("unused")
			Document doc = SimpleDocumentBuilder.buildDocument(xml);
		} catch (RuntimeException e) {
			assertEquals("Unable to parse xml - Line: 1 - XML document structures must start and end within the same entity.", e.getMessage());
            return;
        }
        fail("No exception was throw");
    }
}
