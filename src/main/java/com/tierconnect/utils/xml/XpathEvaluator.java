package com.tierconnect.utils.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XpathEvaluator {
	private XPath xpath;
	private List<Node> container = new ArrayList<Node>();
	
	public XpathEvaluator(NodeList nodes, XPath xpath) {
		this.xpath = xpath;
		for (int i = 0; i < nodes.getLength(); i++) {
			Node item = nodes.item(i);
			container.add(item);
		}
	}
	
	public XpathEvaluator(Node node, XPath xpath) {
		this.xpath = xpath;
		container.add(node);
	}

	/**
	 * It evaluates a xpath expression that returns a number.
	 * 
	 * @param xpathExpression
	 * @return
	 */
	public Double evaluateNumberFunction(String xpathExpression) {
		Double result;
		try {
			result = (Double) this.xpath.evaluate(xpathExpression, getBaseElement(), XPathConstants.NUMBER);
		} catch (XPathExpressionException e) {
			throw new RuntimeException("Unable to evaluate xpath expression", e);
		}
		return result;
	}

	/**
	 * It evaluates a xpath expression that returns a string.
	 * 
	 * @param xpathExpression
	 * @return
	 */
	public String evaluateStringFunction(String xpathExpression) {
		String result;
		try {
			result = (String) this.xpath.evaluate(xpathExpression, getBaseElement(),
					XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			throw new RuntimeException("Unable to evaluate xpath expression", e);
		}
		return result;
	}

	/**
	 * It evaluates a xpath expression that returns a string.
	 * 
	 * @param xpathExpression
	 * @return
	 */
	public Boolean evaluateBooleanFunction(String xpathExpression) {
		Boolean result;
		try {
			result = (Boolean) this.xpath.evaluate(xpathExpression, getBaseElement(),
					XPathConstants.BOOLEAN);
		} catch (XPathExpressionException e) {
			throw new RuntimeException("Unable to evaluate xpath expression", e);
		}
		return result;
	}
	
	/**
	 * Allows evaluate a xpath expression and return the result in a XMLResult
	 * object.
	 * 
	 * @param xpathExpression
	 * @return
	 */
	public XpathEvaluator evaluate(String xpathExpression) {
		XpathEvaluator result = null;
		try {
			NodeList nodes = (NodeList) this.xpath.evaluate(xpathExpression, this.getBaseElement(), XPathConstants.NODESET);
			result = new XpathEvaluator(nodes, this.xpath);
		} catch (XPathExpressionException e) {
			throw new RuntimeException("Unable to evaluate xpath expression", e);
		}
		return result;
	}

	/**
	 * It returns the base element of this instance.
	 * It will return null, if this instance contains more than one element.
	 * @return
	 */
	public Node getBaseElement() {
		return this.getNativeNode();
	}
	
	/**
	 * It returns the string value of the node.
	 * @return
	 */
	public String getString(){
		Node node = getNativeNode();
		return node.getTextContent();
	}
	
	/**
	 * It returns an integer value of the content of the node.
	 * @return
	 */
	public int getInt() {
		String s = this.getString();
		return Integer.parseInt(s);
	}
	
	/**
	 * Checks if the result is empty or not.
	 * @return
	 */
	public boolean isEmpty(){
		return container.isEmpty();
	}
	
	/**
	 * Checks if the result has multiple nodes.
	 * @return
	 */
	public boolean hasMultipleNodes(){
		return container.size() > 1;
	}

	/**
	 * Return the number of nodes.
	 * @return
	 */
	public int getLength(){
		return container.size();
	}

	/**
	 * It returns the w3c native node instance
	 * @return
	 */
	public Node getNativeNode() {
		if(isEmpty()) {
			throw new RuntimeException("It is not possible to get and string from a empty result");
		}
		if(hasMultipleNodes()) {
			throw new RuntimeException("It is not possible to get and string from multiple node result.");
		}
		return container.get(0);
	}
	
	/*
	 * It returns a list with wrapped nodes.
	 */
	public List<XpathEvaluator> getWrappedNodes(){
		List<XpathEvaluator> result = new ArrayList<XpathEvaluator>();
		for (int i = 0; i < container.size(); i++) {
			result.add(new XpathEvaluator(container.get(i), this.xpath));
		}
		return result;
	}

	/**
	 * Returns a string representation of the this instance.
	 * @return
	 */
	public String toXMLString(){
		StringBuffer sb = new StringBuffer();
		if(isEmpty()) {
			sb.append("EMPTY RESULT");
		} else
		if(!hasMultipleNodes()) {
			sb.append(XMLUtils.prettyPrint(getBaseElement()));
		} else {
			for (Iterator<Node> iterator = container.iterator(); iterator.hasNext();) {
				sb.append(XMLUtils.prettyPrint(iterator.next()));
				if(iterator.hasNext()) {
					sb.append("\n");
				}
			}
		}
		return sb.toString();
	}
}
