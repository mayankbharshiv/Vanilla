package com.vanilla.afour.afour.autolib.util;

import java.util.HashMap;

import com.vanilla.afour.afour.autolib.util.TestDataLoader1;
import com.vanilla.afour.afour.autolib.util.ObjectRepositoryLoader1;

/**
 * Created by siddharth.p on 1/3/2017.
 */
public class TestDataHandler1 {

    private HashMap<String, String> testDataInfo = null;
    String currentDir = System.getProperty("user.dir");
    static ObjectRepositoryLoader1 objRepo = null;

    public TestDataHandler1() {
        objRepo = new ObjectRepositoryLoader1();
    }

    public TestDataHandler1(String filePath) {
        TestDataLoader1 testDataLoad = new TestDataLoader1();
        this.testDataInfo = testDataLoad.testDataLoader(filePath);
    }

    public String getTestData(String key) {
        return (String) this.testDataInfo.get(key);
    }

    public Object[][] getDataMatrix(String fileName) {
        TestDataLoader1 testDataLoad = new TestDataLoader1();
        return testDataLoad.getTestDataByRowAndColumn(fileName);
    }

    public HashMap<String, HashMap<String, String>> readTestData(String FileName) {
        new HashMap();
        String FilePath = this.currentDir + "//TestData//" + FileName;
        HashMap mHashMap = objRepo.objectRepositoryLoader(FilePath);
        return mHashMap;
    }
}
