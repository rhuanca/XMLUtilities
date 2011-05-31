package renidev.utils.xml;

import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;

public class XpathEvaluatorFactory {
	private static XPathFactory xPathFactory;
	
	static {
		xPathFactory = XPathFactory.newInstance();
	}
	
	public static XpathEvaluator create(String xml) {
		Document document = SimpleDocumentBuilder.buildDocument(xml);
		return createInstance(document, "/");
	}

	public static XpathEvaluator create(String xml, String encoding) {
		Document document = SimpleDocumentBuilder.buildDocument(xml);
		return createInstance(document, "/");
	}

	private static XpathEvaluator createInstance(Document document, String xpath) {
		XpathEvaluator evaluator = new XpathEvaluator(document, xPathFactory.newXPath());
		return evaluator.evaluate(xpath);
	}
}