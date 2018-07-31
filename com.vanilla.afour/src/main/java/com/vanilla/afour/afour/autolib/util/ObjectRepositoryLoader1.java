package com.vanilla.afour.afour.autolib.util;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by siddharth.p on 1/3/2017.
 */
public class ObjectRepositoryLoader1 {
    private HashMap<String, HashMap<String, String>> loadedMap = null;

    public ObjectRepositoryLoader1() {
    }

    public HashMap<String, HashMap<String, String>> objectRepositoryLoader(String filePath) {
        if (filePath.contains(".json")) {
            this.loadedMap = this.jsonObjectReprositoryLoader(filePath);
        } else if (filePath.contains(".xml")) {
            this.loadedMap = this.xmlObjectReprositoryLoader(filePath);
        } else if (filePath.contains(".csv")) {
            this.loadedMap = this.csvObjectReprositoryLoader(filePath);
        }

        return this.loadedMap;
    }

    private HashMap<String, HashMap<String, String>> jsonObjectReprositoryLoader(String filepath) {
        HashMap elements = new HashMap();
        HashMap locatorData = null;

        try {
            Object e = null;
            Object locatorType = null;
            String locatorValue = null;
            FileReader reader = new FileReader(filepath);
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            Set keySet = jsonObject.keySet();
            Iterator var12 = keySet.iterator();

            while (var12.hasNext()) {
                String key = (String) var12.next();
                JSONArray jsonArray = (JSONArray) jsonObject.get(key);
                locatorData = new HashMap();

                for (int i = 0; i < jsonArray.size(); ++i) {
                    JSONObject jsonInnerObject = (JSONObject) jsonArray.get(i);
                    Set innerKeySet = jsonInnerObject.keySet();
                    Iterator var18 = innerKeySet.iterator();

                    while (var18.hasNext()) {
                        String innerKey = (String) var18.next();
                        locatorValue = (String) jsonInnerObject.get(innerKey);
                        locatorData.put(innerKey, locatorValue);
                    }
                }

                elements.put(key, locatorData);
            }
        } catch (FileNotFoundException var19) {
            var19.printStackTrace();
        } catch (IOException var20) {
            var20.printStackTrace();
        } catch (ParseException var21) {
            var21.printStackTrace();
        }

        return elements;
    }

    private HashMap<String, HashMap<String, String>> xmlObjectReprositoryLoader(String filePath) {
        HashMap mHashMap = new HashMap();
        String val = null;
        String outerKey = null;
        String innerKey = null;

        try {
            File e = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(e);
            NodeList elementNodeList = doc.getElementsByTagName("Element");

            for (int temp = 0; temp < elementNodeList.getLength(); ++temp) {
                HashMap hTable = new HashMap();
                Node elementNode = elementNodeList.item(temp);
                Element ele = (Element) elementNode;
                outerKey = ele.getAttribute("name");
                NodeList paramNodeList = elementNode.getChildNodes();

                for (int temp1 = 0; temp1 < paramNodeList.getLength(); ++temp1) {
                    Element ele1 = null;
                    Node paramNode = paramNodeList.item(temp1);
                    if (paramNodeList.item(temp1).getNodeType() == 1) {
                        ele1 = (Element) paramNode;
                        innerKey = ele1.getAttribute("type");
                        val = ele1.getAttribute("value");
                        hTable.put(innerKey, val);
                    }
                }

                mHashMap.put(outerKey, hTable);
            }
        } catch (Exception var19) {
            var19.printStackTrace();
        }

        return mHashMap;
    }

    private HashMap<String, HashMap<String, String>> csvObjectReprositoryLoader(String filePath) {
        HashMap mapOuter = new HashMap();

        try {
            CSVReader e = new CSVReader(new FileReader(filePath));
            String innerKey = "";
            String outerKey = "";
            String value = "";

            String[] nextLine;
            while ((nextLine = e.readNext()) != null) {
                HashMap mapInner = new HashMap();
                outerKey = nextLine[0];

                for (int i = 1; i < nextLine.length; ++i) {
                    if (nextLine[i] != null) {
                        String[] keyArray = nextLine[i].split("=");
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

            e.close();
        } catch (Exception var11) {
            System.out.println(var11);
        }

        return mapOuter;
    }
}
