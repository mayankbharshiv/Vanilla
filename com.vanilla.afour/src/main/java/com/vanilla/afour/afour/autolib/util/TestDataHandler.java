package com.vanilla.afour.afour.autolib.util;

import java.util.HashMap;

import com.vanilla.afour.afour.autolib.util.TestDataLoader1;
import com.vanilla.afour.afour.autolib.util.ObjectRepositoryLoader;

/**
 * Created by siddharth.p on 12/29/2016.
 */
public class TestDataHandler {

    private HashMap<String, String> testDataInfo = null;
    String currentDir = System.getProperty("user.dir");
    static ObjectRepositoryLoader objRepo = null;
    static TestDataLoader1 objTestDataLoader = null;

    public TestDataHandler() {

        objRepo = new ObjectRepositoryLoader();
        objTestDataLoader = new TestDataLoader1();

    }

    /**
     * @param filePath
     */
    public TestDataHandler(String filePath) throws Exception {
        TestDataLoader testDataLoad = new TestDataLoader();
        testDataInfo = testDataLoad.propertiesLoader(filePath);
//        testDataInfo = testDataLoad.testDataLoader(filePath);
    }

    /**
     * @param key
     * @return
     */
    public String getTestData(String key) {
        return testDataInfo.get(key);
    }

//    public Object[][] getDataMatrix(String fileName) {
//        TestDataLoader testDataLoad = new TestDataLoader();
//        return testDataLoad.getTestDataByRowAndColumn(fileName);
//    }

    public HashMap<String, HashMap<String, String>> readTestData(String FileName) throws Exception {
        HashMap<String, HashMap<String, String>> mHashMap = new HashMap<String, HashMap<String, String>>();
        String FilePath = currentDir + "//TestData//" + FileName;
        mHashMap = objTestDataLoader.xmlTestDataLoader(FilePath);
        return mHashMap;
    }

}
