package com.kcube.golaju.util;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XmlUtil 
{
	public static Document readXml(String url) throws Exception
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		factory.setNamespaceAware(true);

		return factory.newDocumentBuilder().parse(url);
	}
	
	/**
	 * InputStream에서 XML 로드
	 * @param is : XML의 Input Stream
	 * @return : 환경 설정 Document
	 * @throws Exception
	 */
	public static Document readXml(InputStream is) throws Exception
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		factory.setNamespaceAware(true);

		return factory.newDocumentBuilder().parse(is);
	}

	/**
	 * InputSource에서 XML 로드
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static Document readXml(InputSource is) throws Exception
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		factory.setNamespaceAware(true);

		return factory.newDocumentBuilder().parse(is);
	}
	
	/**
	 * Xpath data를 구한다.
	 * @param document : Document
	 * @param xpath : 구하고자하는 XPath
	 * @return : xpath 값
	 */
	public static String getXPath(Document document, String xpath) throws Exception
	{
		NodeList nl = org.apache.xpath.XPathAPI.selectNodeList(document, xpath);
		nl = nl.item(0).getChildNodes();
		return nl.item(0).getNodeValue();
	}

	/**
	 * Node 에서 xpath 로 값을 구한다.
	 * @param node : Node
	 * @param xpath : 구하고자하는 XPath
	 * @return
	 * @throws Exception
	 */
	public static String getXPath(Node node, String xpath) throws Exception
	{
		NodeList nl = org.apache.xpath.XPathAPI.selectNodeList(node, xpath);
		nl = nl.item(0).getChildNodes();
		return nl.item(0).getNodeValue();
	}

	/**
	 * Dom에서 Xpath를 가지고 Cdata를 구한다.
	 * @param document : Document
	 * @param xpath : 구하고자하는 XPath
	 * @return : xpath Cdata 값
	 */
	public static String getXPathCData(Document document, String xpath) throws Exception
	{
		String filemsg = "";
		NodeList nl = org.apache.xpath.XPathAPI.selectNodeList(document, xpath);
		for (int i = 0; i < nl.getLength(); i++)
		{
			if (nl.item(i).getNodeType() == Node.CDATA_SECTION_NODE)
			{
				return nl.item(i).getNodeValue();
			}
			else if (nl.item(i).getNodeType() == Node.ELEMENT_NODE)
			{
				NodeList nlc = nl.item(i).getChildNodes();
				for (int k = 0; k < nlc.getLength(); k++)
				{
					if (nlc.item(k).getNodeType() == Node.CDATA_SECTION_NODE)
					{
						return nlc.item(k).getNodeValue();
					}
				}
			}
		}
		return filemsg;
	}

	/**
	 * Dom에서 Xpath로 NodeList를 구한다
	 * @param document : Document
	 * @param xpath : 구하고자하는 XPath
	 * @return : Node List
	 */
	public static NodeList getXPathes(Document document, String xpath) throws Exception
	{
		return org.apache.xpath.XPathAPI.selectNodeList(document, xpath);
	}

	/**
	 * Node에서 NodeList를 구한다
	 * @param node : Node
	 * @param xpath : 구하고자하는 XPath
	 * @return : NodeList
	 */
	public static NodeList getXPathes(Node node, String xpath) throws Exception
	{
		return org.apache.xpath.XPathAPI.selectNodeList(node, xpath);
	}

	/**
	 * Node에서 Attribute를 구한다
	 * @param node : Node
	 * @param xpath : 구하고자하는 XPath
	 * @param attribute : Attribute 명
	 * @return : attribute 값
	 */
	public static String getAttribute(Node node, String xpath, String attribute)
		throws Exception
	{
		NodeList nl = org.apache.xpath.XPathAPI.selectNodeList(node, xpath);
		NamedNodeMap attributes = nl.item(0).getAttributes();

		for (int i = 0; i < attributes.getLength(); i++)
		{
			Node current = attributes.item(i);
			if (attribute.compareTo(current.getNodeName()) == 0)
			{
				return current.getNodeValue();
			}
		}
		return null;
	}

	/**
	 * Xpath 존재 여부를 돌려줌
	 * @param document : Document
	 * @param xpath : 구하고자하는 XPath
	 * @return : xpath 존재여부
	 */
	public static boolean isXPath(Document document, String xpath)
	{
		try
		{
			NodeList nl = org.apache.xpath.XPathAPI.selectNodeList(document, xpath);
			if (nl.getLength() == 0)
			{
				return false;
			}
			return true;

		}
		catch (Exception e)
		{
			return false;
		}
	}

	/**
	 * Node에서 xpath로 하위 Node를 구한다
	 * @param node : Node
	 * @param xpath : 구하고자하는 XPath
	 * @return : Node
	 */
	public static Node getXPathNode(Node node, String xpath) throws Exception
	{
		return org.apache.xpath.XPathAPI.selectSingleNode(node, xpath);
	}
}
