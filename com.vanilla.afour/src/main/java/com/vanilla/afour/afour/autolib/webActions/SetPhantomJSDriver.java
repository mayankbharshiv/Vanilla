//package com.vanilla.afour.afour.autolib.webActions;
//
//import com.vanilla.afour.afour.autolib.util.Log4J;
//import com.vanilla.afour.afour.autolib.util.Reporting;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.phantomjs.PhantomJSDriver;
//import org.openqa.selenium.phantomjs.PhantomJSDriverService;
//import org.openqa.selenium.remote.DesiredCapabilities;
//import org.openqa.selenium.remote.RemoteWebDriver;
//import org.openqa.selenium.support.ui.WebDriverWait;
//
//import java.util.HashMap;
//import com.vanilla.afour.afour.autolib.util.*;
//
//import static com.vanilla.afour.afour.autolib.util.BrowserType.CHROME;
//import static com.vanilla.afour.afour.autolib.util.BrowserType.FIREFOX;
//import static com.vanilla.afour.afour.autolib.util.BrowserType.IE;
//
///**
// * Created by siddharth.p on 3/7/2017.
// */
//
//
//public class SetPhantomJSDriver {
//    private static PhantomJSDriver pjDriver = null;
//
//    public SetPhantomJSDriver() throws Exception {
////        config = getConfigData();
//        setPhantomJSDriver();
//    }
//
//
//    public void setPhantomJSDriver(){
//        DesiredCapabilities caps = new DesiredCapabilities();
//        caps.setJavascriptEnabled(true);
//        caps.setCapability("takesScreenshot", true);
//        caps.setCapability(
//                PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
//                "D:\\lib\\phantomJS\\bin\\phantomjs.exe"
//        );
//        caps.setCapability("databaseEnabled", true);
//        caps.setCapability("locationContextEnabled", true);
//        caps.setCapability("applicationCacheEnabled", true);
//        caps.setCapability("browserConnectionEnabled", true);
//        caps.setCapability("webStorageEnabled", true);
//        caps.setCapability("rotatable", true);
//        caps.setCapability("acceptSslCerts", true);
//        caps.setCapability("localToRemoteUrlAccessEnabled", true);
//
//
//        caps.setCapability("handlesAlerts", true);
//        caps.setCapability("databaseEnabled", true);
//        caps.setCapability("locationContextEnabled", true);
//        caps.setCapability("localToRemoteUrlAccessEnabled", true);
//        caps.setCapability("localToRemoteUrlAccessEnabled", true);
//
//        pjDriver = new PhantomJSDriver(caps);
//    }
//
//    public WebDriver getPhantomJSDriver(){
//        return pjDriver;
//    }
//
//}
