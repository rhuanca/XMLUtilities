package com.tierconnect.utils.xml;

import java.io.UnsupportedEncodingException;
import org.junit.Test;
import static junit.framework.Assert.*;

public class XMLReaderTest {
	@Test
	public void read_one_node(){
		assertEquals(XpathEvaluatorFactory.create("<root></root>").evaluate("/root").getString(), "");
		assertEquals(XpathEvaluatorFactory.create("<root>Test</root>").evaluate("/root").getString(), "Test");
		assertEquals(XpathEvaluatorFactory.create("<root>4</root>").evaluate("/root").getInt(), 4);
		assertEquals(XpathEvaluatorFactory.create("<root>0</root>").evaluate("/root").getInt(), 0);
		try {
			XpathEvaluatorFactory.create("<root></root>").evaluate("/root").getInt();
		} catch (Exception e) {
			assertTrue(e instanceof NumberFormatException);
		}
	}
	
	@Test
	public void read_xml_two_levels(){
		assertEquals(XpathEvaluatorFactory.create("<root><child></child></root>").evaluate("/root/child").getString(), "");
		assertEquals(XpathEvaluatorFactory.create("<root><child>Test</child></root>").evaluate("/root/child").getString(), "Test");
		assertEquals(XpathEvaluatorFactory.create("<root><child>4</child></root>").evaluate("/root/child").getInt(), 4);
		assertEquals(XpathEvaluatorFactory.create("<root><child>0</child></root>").evaluate("/root/child").getInt(), 0);
		
		try {
			XpathEvaluatorFactory.create("<root><child></child></root>").evaluate("/root/child").getInt();
		} catch (Exception e) {
			assertTrue(e instanceof NumberFormatException);
		}
	}

	@Test
	public void read_node_with_xml_result(){
		assertEquals(XpathEvaluatorFactory.create("<root><child></child></root>").evaluate("root").evaluate("child").getString(), "");
		assertEquals(XpathEvaluatorFactory.create("<root><child>Test</child></root>").evaluate("root").evaluate("child").getString(), "Test");
		assertEquals(XpathEvaluatorFactory.create("<root><child>4</child></root>").evaluate("root").evaluate("child").getInt(), 4);
		assertEquals(XpathEvaluatorFactory.create("<root><child>0</child></root>").evaluate("root").evaluate("child").getInt(), 0);
	}
	
	
	
	@Test
	public void read_node_with_xml_result_with_some_values(){
		String s = "";
		s += "<root>";
		s +=   "<child>";
		s +=     "<int_value>10</int_value>";
		s +=     "<string_value>test</string_value>";
		s +=   "</child>";
		s += "</root>";
		XpathEvaluator reader = XpathEvaluatorFactory.create(s);
		XpathEvaluator root = reader.evaluate("root");
		assertEquals(root.evaluate("child").evaluate("int_value").getInt(), 10);
		assertEquals(root.evaluate("child").evaluate("string_value").getString(), "test");
	}
	
	@Test
	public void read_succesive_child_nodes(){
		String s = "";
		s += "<root>";
		s +=   "<child>test</child>";
		s += "</root>";
		XpathEvaluator reader = XpathEvaluatorFactory.create(s);
		XpathEvaluator root = reader.evaluate("root");
		assertEquals(1, root.getNativeNode().getChildNodes().getLength());
		root.evaluate("child");
		assertEquals(1, root.getNativeNode().getChildNodes().getLength());
	}
	
	@Test
	public void read_single_attributes(){
		String s = "";
		s += "<root>";
		s +=   "<child int_value=\"10\" string_value=\"test\">";
		s +=   "</child>";
		s += "</root>";
		XpathEvaluator reader = XpathEvaluatorFactory.create(s);
		XpathEvaluator rootResult = reader.evaluate("root");
		assertEquals(rootResult.evaluate("child").evaluate("@int_value").getInt(), 10);
		assertEquals(rootResult.evaluate("child").evaluate("@string_value").getString(), "test");
	}
	
	@Test
	public void read_not_existing_node(){
		assertEquals(XpathEvaluatorFactory.create("<root><child></child></root>").
				evaluate("other_child").isEmpty(), true);
	}
	
	@Test
	public void read_existing_multiple_nodes(){
		assertEquals(XpathEvaluatorFactory.create("<root><child>child1</child><child>child2</child></root>").
				evaluate("root/child").hasMultipleNodes(), true);
	}
	
	@Test
	public void read_text(){
		assertEquals(XpathEvaluatorFactory.create("<root><child>hello</child></root>").
				evaluate("root/child").evaluate(".").getString(), "hello");
	}
	
	@Test
	public void test_functions(){
		String s = "";
		s += "<root>";
		s +=   "<child>val1</child>";
		s +=   "<child>val2</child>";
		s +=   "<child>val3</child>";
		s +=   "<child>val4</child>";
		s += "</root>";
		XpathEvaluator reader = XpathEvaluatorFactory.create(s);
		
		Double number = reader.evaluateNumberFunction("count(/root/child)");
		assertEquals(4, number.intValue());
		
		String  name = reader.evaluateStringFunction("name(/root)");
		assertEquals("root", name);
		
		Boolean  bool = reader.evaluateBooleanFunction("contains('XML','XM')");
		assertEquals(Boolean.TRUE, bool);
		
		bool = reader.evaluateBooleanFunction("contains('XML','ABC')");
		assertEquals(Boolean.FALSE, bool);
	}
	
	@Test
	public void test_functions_child_element(){
		String s = "";
		s += "<root>";
		s +=   "<parent>";
		s +=     "<child>val1</child>";
		s +=     "<child>val2</child>";
		s +=     "<child>val3</child>";
		s +=     "<child>val4</child>";
		s +=   "</parent>";
		s += "</root>";
		
		XpathEvaluator parent = XpathEvaluatorFactory.create(s).evaluate("/root/parent");
		Double number = parent.evaluateNumberFunction("count(child)");
		assertEquals(4, number.intValue());
		
		String  name = parent.evaluateStringFunction("name()");
		assertEquals("parent", name);
		
		Boolean  bool = parent.evaluateBooleanFunction("contains('XML','XM')");
		assertEquals(Boolean.TRUE, bool);
		
		bool = parent.evaluateBooleanFunction("contains('XML','ABC')");
		assertEquals(Boolean.FALSE, bool);
		
	}
	
	@Test 
	public void test_unicode_caracters() throws UnsupportedEncodingException{
		String s = "";
		s += "<root>";
		s +=     "<child>测试文本</child>";
		s += "</root>";
		
		// it seems that in this string can be decoded with UTF-8 and UTF-16 equally
		assertEquals("测试文本", XpathEvaluatorFactory.create(s, "UTF-16").evaluate("/root/child").getString());
		assertEquals("测试文本", XpathEvaluatorFactory.create(s, "UTF-8").evaluate("/root/child").getString());
		try {
			XpathEvaluatorFactory.create(s, "GB18030");
			// IMPORTANT NOTE. 
			// This call will print the following message in the default output stream
			//     [Fatal Error] :1:14: Invalid byte 1 of 1-byte UTF-8 sequence.
			// So in this case it is ok to see this in the console.
		} catch (Exception e) {
			assertEquals("Invalid byte 1 of 1-byte UTF-8 sequence.", e.getCause().getMessage());
		}
	}
		
	@Test 
	public void test_not_well_formed_document() throws UnsupportedEncodingException{
		String s = "";
		s += "<root>";
		s +=     "<child><child>"; // The wrong element.
		s += "</root>";
		try {
			@SuppressWarnings("unused")
			XpathEvaluator reader = XpathEvaluatorFactory.create(s);
		} catch (RuntimeException e) {
			assertEquals("Unable to parse xml - Line: 1 - The element type \"child\" must " +
					"be terminated by the matching end-tag \"</child>\".", e.getMessage());
			return;
		}
		fail("No exception was trown");
	}
	
	@Test 
	public void test_not_well_formed_document_with_lines() throws UnsupportedEncodingException{
		String s = "";
		s += "<root>\n";
		s +=     "<child><oops><oops></child>\n"; // The wrong element.
		s +=     "<child></child>\n"; // The wrong element.
		s +=     "<child></child>\n"; // The wrong element.
		s +=     "<child></child>\n"; // The wrong element.
		s +=     "<child></child>\n"; // The wrong element.
		s += "</root>\n";
		try {
			@SuppressWarnings("unused")
			XpathEvaluator reader = XpathEvaluatorFactory.create(s);
		} catch (RuntimeException e) {
			assertEquals("Unable to parse xml - Line: 2 - The element type \"oops\" must " +
					"be terminated by the matching end-tag \"</oops>\".", e.getMessage());
			return;
		}
		fail("No exception was trown");
	}

}
