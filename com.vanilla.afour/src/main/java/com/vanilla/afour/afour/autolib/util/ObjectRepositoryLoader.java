package com.vanilla.afour.afour.autolib.util;

import com.opencsv.CSVReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by siddharth.p on 12/21/2016.
 */
public class ObjectRepositoryLoader {

    private String filePath = null;

    /**
     * Method to get all element info from a file in Object repository.
     *
     * @param filePath - from which file info needs to be fetched (Only .json, .xml and .csv).
     * @return - HashMap<String, HashMap<String, String>>
     */
    public HashMap<String, HashMap<String, String>> getObjectRepository(String fileName) throws Exception {
        HashMap<String, HashMap<String, String>> loadedMap = null;
        String repoPath = new TestDataLoader().propertiesLoader("config.properties").get("elementRepository");
        this.filePath = new FileOperationUtilites().getFilePath(repoPath + ":" + fileName);
        if (filePath.contains(".json")) {
            loadedMap = this.jsonObjectReprositoryLoader();
        } else if (filePath.contains(".xml")) {
            loadedMap = this.xmlObjectReprositoryLoader();
        } else if (filePath.contains(".csv")) {
            loadedMap = this.csvObjectReprositoryLoader();
        }
        return loadedMap;
    }

    /**
     * Method to get all element info from a JSON file in Object repository.
     *
     * @param filePath - from which file info needs to be fetched.
     * @return - HashMap<String, HashMap<String, String>>
     * @throws FileNotFoundException IOException ParseException
     */
    @SuppressWarnings("unchecked")
    private HashMap<String, HashMap<String, String>> jsonObjectReprositoryLoader() {
        HashMap<String, HashMap<String, String>> elements = new HashMap<String, HashMap<String, String>>();

        HashMap<String, String> locatorData = null;

        try {
            String elementName = null;
            String locatorType = null;
            String locatorValue = null;

            FileReader reader = new FileReader(filePath);
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            Set<String> keySet = jsonObject.keySet();
            for (String key : keySet) {
                elementName = key;
                JSONArray jsonArray = (JSONArray) jsonObject.get(key);
                locatorData = new HashMap<String, String>();

                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonInnerObject = (JSONObject) jsonArray.get(i);
                    Set<String> innerKeySet = jsonInnerObject.keySet();

                    for (String innerKey : innerKeySet) {
                        locatorType = innerKey;
                        locatorValue = (String) jsonInnerObject.get(innerKey);
                        locatorData.put(locatorType, locatorValue);
                    }
                }
                elements.put(elementName, locatorData);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return elements;
    }

    /**
     * Method to get all element info from a XML file in Object repository.
     *
     * @param filePath - from which file info needs to be fetched.
     * @return - HashMap<String, HashMap<String, String>>
     * @throws Exception
     */
    private HashMap<String, HashMap<String, String>> xmlObjectReprositoryLoader() {
        HashMap<String, HashMap<String, String>> mHashMap = new HashMap<String, HashMap<String, String>>();
        String val = null;
        String outerKey = null;
        String innerKey = null;

        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            // Get All Element with tag <ELEMENT>
            NodeList elementNodeList = doc.getElementsByTagName("Element");

            for (int temp = 0; temp < elementNodeList.getLength(); temp++) {
                HashMap<String, String> hTable = new HashMap<String, String>();
                Node elementNode = elementNodeList.item(temp);
                Element ele = (Element) elementNode;
                outerKey = ele.getAttribute("name");

                // Get All Element with tag <param>
                NodeList paramNodeList = elementNode.getChildNodes();
                ;
                for (int temp1 = 0; temp1 < paramNodeList.getLength(); temp1++) {
                    Element ele1 = null;
                    Node paramNode = paramNodeList.item(temp1);
                    if (paramNodeList.item(temp1).getNodeType() == Node.ELEMENT_NODE) {
                        ele1 = (Element) paramNode;
                        innerKey = ele1.getAttribute("type");
                        val = ele1.getAttribute("value");
                        hTable.put(innerKey, val);
                    }
                }
                mHashMap.put(outerKey, hTable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mHashMap;
    }

    /**
     * Method to get all element info from a CSV file in Object repository.
     *
     * @param filePath - from which file info needs to be fetched.
     * @return - HashMap<String, HashMap<String, String>>
     * @throws Exception
     */
    private HashMap<String, HashMap<String, String>> csvObjectReprositoryLoader() {
        HashMap<String, HashMap<String, String>> mapOuter = new HashMap<String, HashMap<String, String>>();

        try {
            CSVReader reader = new CSVReader(new FileReader(filePath));
            String nextLine[];
            String keyArray[];
            String innerKey = "";
            String outerKey = "";
            String value = "";

            while ((nextLine = reader.readNext()) != null) {
                HashMap<String, String> mapInner = new HashMap<String, String>();
                outerKey = nextLine[0];
                for (int i = 1; i < nextLine.length; i++) {
                    if (nextLine[i] != null) {
                        keyArray = nextLine[i].split("=");
                        if (keyArray.length != 1) {
                            innerKey = keyArray[0];
                            value = keyArray[1];
                            mapInner.put(innerKey, value);
                            System.out.println(outerKey + "\t" + innerKey + "\t" + value);
                        }
                    }
                }
                mapOuter.put(outerKey, mapInner);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return mapOuter;
    }
}
