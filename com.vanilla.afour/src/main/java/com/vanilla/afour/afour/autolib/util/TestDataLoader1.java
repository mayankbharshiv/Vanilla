package com.vanilla.afour.afour.autolib.util;

import com.opencsv.CSVReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.*;

/**
 * Created by siddharth.p on 1/3/2017.
 */
public class TestDataLoader1 {

    private HashMap<String, String> loadedMap = null;

    public TestDataLoader1() {
    }

    public HashMap<String, String> testDataLoader(String filePath) {
        if (filePath.contains(".json")) {
            this.loadedMap = this.jsonTestDataLoader(filePath);
        }else if (filePath.contains(".csv")) {
            this.loadedMap = this.csvTestDataLoader(filePath);
        } else if (filePath.contains(".properties")) {
            this.loadedMap = this.propertyTestDataLoader(filePath);
        }

        return this.loadedMap;
    }

    private HashMap<String, String> jsonTestDataLoader(String filepath) {
        HashMap testData = new HashMap();

        try {
            Object e = null;
            String testDataValue = null;
            FileReader reader = new FileReader(filepath);
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            Set keySet = jsonObject.keySet();
            Iterator var10 = keySet.iterator();

            while (var10.hasNext()) {
                String key = (String) var10.next();
                testDataValue = jsonObject.get(key).toString();
                testData.put(key, testDataValue);
            }
        } catch (FileNotFoundException var11) {
            var11.printStackTrace();
        } catch (IOException var12) {
            var12.printStackTrace();
        } catch (ParseException var13) {
            var13.printStackTrace();
        }

        return testData;
    }

    /**
     * Method to get all element info from a XML file in Object repository.
     *
     * @param filePath - from which file info needs to be fetched.
     * @return - HashMap<String, HashMap<String, String>>
     * @throws Exception
     */
    public HashMap<String, HashMap<String, String>> xmlTestDataLoader(String filePath) {
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

    private HashMap<String, String> csvTestDataLoader(String filePath) {
        HashMap mapOuter = new HashMap();

        try {
            CSVReader e = new CSVReader(new FileReader(filePath));
            String outerKey = "";
            String value = "";

            String[] nextLine;
            while ((nextLine = e.readNext()) != null) {
                outerKey = nextLine[0];
                value = nextLine[1];
                mapOuter.put(outerKey, value);
            }

            e.close();
        } catch (Exception var7) {
            System.out.println(var7);
        }

        return mapOuter;
    }

    public HashMap<String, String> propertyTestDataLoader(String filePath) {
        HashMap HMap = new HashMap();
        Properties prop = new Properties();
        FileInputStream input = null;

        try {
            input = new FileInputStream(filePath);
            prop.load(input);
            Enumeration e = prop.propertyNames();

            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = prop.getProperty(key);
                HMap.put(key, value);
            }
        } catch (FileNotFoundException var8) {
            var8.printStackTrace();
        } catch (IOException var9) {
            var9.printStackTrace();
        } catch (Exception var10) {
            var10.printStackTrace();
        }

        return HMap;
    }

    public Object[][] getTestDataByRowAndColumn(String fileName) {
        String[] temp = null;
        String splitBy = ",";
        int lineCount = 0;
        int count = 1;
        int i = 0;
        Object[][] objectArray = new Object[][]{new Object[0]};

        try {
            String e = System.getProperty("user.dir");
            String csvfilepath = e + "/TestData/" + fileName;
            FileInputStream fstream = new FileInputStream(csvfilepath);
            FileInputStream fstream1 = new FileInputStream(csvfilepath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String strLine;
            BufferedReader br1;
            for (br1 = new BufferedReader(new InputStreamReader(fstream1)); (strLine = br.readLine()) != null; temp = strLine.split(splitBy, -1)) {
                ++lineCount;
            }

            for (objectArray = new Object[lineCount - 1][temp.length]; (strLine = br1.readLine()) != null; ++count) {
                if (count != 1) {
                    temp = strLine.split(splitBy);

                    for (int j = 0; j < temp.length; ++j) {
                        objectArray[i][j] = temp[j];
                    }

                    System.out.println();
                    ++i;
                }
            }

            br.close();
            br1.close();
        } catch (Exception var16) {
            System.out.println(var16);
        }

        return objectArray;
    }
}
