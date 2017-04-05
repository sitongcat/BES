package org.bes.task;

import org.bes.model.entity.CostCategory;
import org.bes.model.entity.CostType;
import org.bes.model.entity.Dept;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InitDataManager {

	private static final String DATA_FILE = "data.xml";
	private Document document;

	private Document getDocument() throws IOException, SAXException, ParserConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		this.document = docBuilder.parse(InitDataManager.class.getClassLoader().getResourceAsStream(DATA_FILE));
		return this.document;
	}

	public List<Dept> getDepts() {
		List<Dept> deptList = new ArrayList<>();
		NodeList nodeList = this.document.getElementsByTagName("depts");
		if (nodeList == null || nodeList.getLength() == 0) {
			return deptList;
		}

		NodeList depts = nodeList.item(0).getChildNodes();
		if (depts == null || depts.getLength() == 0) {
			return deptList;
		}

		for (int i = 0; i < depts.getLength(); i++) {
			Node node = depts.item(i);
			NamedNodeMap attrs = node.getAttributes();
			Dept dept = new Dept();
			dept.setDeptId(attrs.getNamedItem("code").getNodeValue());
			dept.setDeptName(attrs.getNamedItem("name").getNodeValue());

			deptList.add(dept);
		}

		return deptList;
	}

	public Map<CostType, List<CostCategory>> getCostCategories() throws Exception {
		Map<CostType, List<CostCategory>> dataMap = new LinkedHashMap<>();

		NodeList nodeList = this.document.getElementsByTagName("costCategories");
		if (nodeList == null || nodeList.getLength() == 0) {
			return dataMap;
		}

		NodeList costTypes = nodeList.item(0).getChildNodes();
		if (costTypes == null || costTypes.getLength() == 0) {
			return dataMap;
		}

		for (int i = 0; i < costTypes.getLength(); i++) {
			Node node = costTypes.item(i);
			NamedNodeMap attrs = node.getAttributes();
			CostType costType = new CostType();
			costType.setName(attrs.getNamedItem("name").getNodeValue());
			costType.setSort(Integer.parseInt(attrs.getNamedItem("sort").getNodeValue()));

			List<CostCategory> costCategories = new ArrayList<>();
			NodeList categories = node.getChildNodes();
			for (int j = 0; j < categories.getLength(); j++) {
				Node node1 = categories.item(j);
				NamedNodeMap attrs1 = node1.getAttributes();
				CostCategory costCategory = new CostCategory();
				costCategory.setCode(attrs1.getNamedItem("code").getNodeValue());
				costCategory.setName(attrs1.getNamedItem("name").getNodeValue());

				costCategories.add(costCategory);
			}
			dataMap.put(costType, costCategories);
		}

		return dataMap;
	}
}
