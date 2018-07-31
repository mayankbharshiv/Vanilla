package com.vanilla.afour.afour.autolib.util;

import com.opencsv.CSVReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by siddharth.p on 12/21/2016.
 */
public class TestDataLoader {

    public HashMap<String, String> config = null;
    private String filePath = null;

    /**
     *
     */
    public TestDataLoader(String file) throws Exception {
        if (config == null) {
            config = getConfigData();
        }
        this.filePath = System.getProperty("user.dir") + "//" + config.get("TestData") + "//" + file;
    }

    /**
     *
     */
    public TestDataLoader() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Use when test data is in json or xml file, get test data using jsonpath or xpath
     *
     * @param expression
     * @return data
     * @example jsonpath : "$.TC01.RegId"
     * @example xpath : "/TC01/RegId"
     */
    public String getTestData(String expression) throws Exception {
        String data = null;
        if (expression.startsWith("$")) {
            try {
                data = this.jsonTestDataLoader(expression);
            } catch (Exception e) {
                throw e;
            }
        } else if (expression.startsWith("/")) {
            try {
                data = this.xmlTestDataLoader(expression);
            } catch (Exception e) {
                throw e;
            }
        }
        return data;
    }

    /**
     * Use when test data is in csv file
     *
     * @return data
     */
    public HashMap<String, String> getTestData() throws Exception {
        HashMap<String, String> data = csvTestDataLoader();
        return data;
    }

    /**
     * Method to get all element info from a CSV file.
     *
     * @param filePath - from which file info needs to be fetched.
     * @return - HashMap<String,String>
     * @throws Exception
     */
    private HashMap<String, String> csvTestDataLoader() throws Exception {
        HashMap<String, String> mapOuter = new HashMap<String, String>();

        try {
            CSVReader reader = new CSVReader(new FileReader(filePath));
            String nextLine[];
            String outerKey = "";
            String value = "";

            while ((nextLine = reader.readNext()) != null) {
                outerKey = nextLine[0];
                value = nextLine[1];
                mapOuter.put(outerKey, value);
            }
            reader.close();
        } catch (Exception e) {
            throw e;
        }
        return mapOuter;
    }

    /**
     * Method to get all element info from a property file.
     *
     * @param filePath - from which file info needs to be fetched
     * @return - HashMap<String,String>
     */
    public HashMap<String, String> propertiesLoader(String filePath) throws Exception {
        HashMap<String, String> HMap = new HashMap<String, String>();
        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream(filePath);
            prop.load(input);
            Enumeration<?> e = prop.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = prop.getProperty(key);
                HMap.put(key, value);
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (IOException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return HMap;

    }

    /**
     * This method return double dimensional array to provide data driven testing
     *
     * @return Object Array
     */

    public Object[][] getTestDataMatrix() throws Exception {
        String strLine, temp[] = null, splitBy = ",";
        int lineCount = 0, count = 1, i = 0;
        Object[][] objectArray = {{}};
        try {
            FileInputStream fstream = new FileInputStream(filePath);
            FileInputStream fstream1 = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            BufferedReader br1 = new BufferedReader(new InputStreamReader(fstream1));
            while ((strLine = br.readLine()) != null) {
                lineCount++;
                temp = strLine.split(splitBy, -1);

            }
            objectArray = new Object[lineCount - 1][temp.length];
            while ((strLine = br1.readLine()) != null) {
                if (count != 1) {
                    temp = strLine.split(splitBy);
                    for (int j = 0; j < temp.length; j++) {
                        objectArray[i][j] = temp[j];
                    }
                    System.out.println();
                    i++;
                }
                count++;
            }
            br.close();
            br1.close();
        } catch (Exception e) {
//			System.out.println(e);
            throw e;
        }
        return objectArray;
    }

    /**
     * Reads test data using xpath expression from xml files
     *
     * @param xpathExpression
     * @return
     */
    private String xmlTestDataLoader(String xpathExpression) throws SAXException, IOException, XPathExpressionException, ParserConfigurationException {
        String testData = null;
        try {
            File fXmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            XPath xPath = XPathFactory.newInstance().newXPath();
            testData = (String) xPath.compile(xpathExpression).evaluate(doc, XPathConstants.STRING);
        } catch (SAXException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            throw e;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            throw e;
        }
        return testData;
    }

    /**
     * Reads test data using jsonpath expression from json files
     *
     * @param jsonPathExpression
     * @return
     */
    private String jsonTestDataLoader(String jsonPathExpression) throws IOException, ParseException {
        String testData = null;
        try {
            FileReader reader = new FileReader(filePath);
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            //DocumentContext jsonContext = JsonPath.parse(jsonObject);
            //testData = jsonContext.read(jsonPathExpression).toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;

        } catch (ParseException e) {
            e.printStackTrace();
            throw e;
        }
        return testData;
    }

    /**
     * Gets Config data from config.properties file
     *
     * @return HashMap
     */
    public HashMap<String, String> getConfigData() throws Exception {
        if (config == null) {
            HashMap<String, String> hashMap = null;
            hashMap = new TestDataLoader().propertiesLoader("config.properties");
            config = hashMap;
            return hashMap;
        } else {
            return config;
        }
    }


}