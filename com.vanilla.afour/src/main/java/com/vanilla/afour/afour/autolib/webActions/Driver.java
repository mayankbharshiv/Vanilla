package com.vanilla.afour.afour.autolib.webActions;

import static com.vanilla.afour.afour.autolib.util.BrowserType.CHROME;
import static com.vanilla.afour.afour.autolib.util.BrowserType.FIREFOX;
import static com.vanilla.afour.afour.autolib.util.BrowserType.IE;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.vanilla.afour.afour.autolib.util.BrowserType;
import com.vanilla.afour.afour.autolib.util.IdentifierMethod;
import com.vanilla.afour.afour.autolib.util.Log4J;
import com.vanilla.afour.afour.autolib.util.LogLevel;
import com.vanilla.afour.afour.autolib.util.Reporting;
import com.vanilla.afour.afour.autolib.util.TestDataLoader;


/**
 * @author Afourtech
 */
public class Driver {
    private static WebDriver webDriver = null;
    private static RemoteWebDriver remoteWebDriver = null;
    private DesiredCapabilities desiredCapabilities = null;
    public static WebDriverWait wait = null;
    public HashMap<String, String> config = null;
    public static Reporting report = new Log4J();
    private static String driverDir = System.getProperty("user.dir") + "\\drivers";

    public Driver() throws Exception {
        config = getConfigData();
        setCapabilities();
    }

    /**
     * Gets Config data from config.properties file
     *
     * @return HashMap
     */
    public HashMap<String, String> getConfigData() throws Exception {
        HashMap<String, String> hashMap = null;
        hashMap = new TestDataLoader().getConfigData();
//		hashMap = new TestDataLoader().testDataLoader("config.properties");
        return hashMap;
    }

    /**
     * Sets up the browser to be used for running automation
     *
     * @param browser : See {@link BrowserType}
     * @throws Exception
     */
    public void setWebDriver(BrowserType browser) throws Exception {

        DesiredCapabilities desiredCapabilities = null;
        if (config.get("execution").equals("local")) {
            switch (browser) {
                case FIREFOX:
                    File gekoDriverPath = new File(driverDir, "//geckodriver.exe");
                    System.setProperty("webdriver.gecko.driver", String.valueOf(gekoDriverPath));
                    desiredCapabilities = DesiredCapabilities.firefox();
                    break;
                case CHROME:
                    System.out.println(driverDir);
                    File chromeDriverPath = new File(driverDir, "//chromedriver.exe");
                    System.setProperty("webdriver.chrome.driver", String.valueOf(chromeDriverPath));
                    desiredCapabilities = DesiredCapabilities.chrome();
                    String downloadFilepath = System.getProperty("user.dir") + "\\Downloads";
                    HashMap<String, Object> chromePrefs = new HashMap<>();
                    chromePrefs.put("profile.default_content_settings.popups", 0);
                    chromePrefs.put("download.default_directory", downloadFilepath);
                    ChromeOptions options = new ChromeOptions();
                    options.setExperimentalOption("prefs", chromePrefs);
                    desiredCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                    desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
                    break;
                case IE:
                    File ieDriverPath = new File(driverDir, "//IEDriverServer.exe");
                    System.setProperty("webdriver.ie.driver", String.valueOf(ieDriverPath));
                    desiredCapabilities = DesiredCapabilities.internetExplorer();
                    break;
//			case IEEDGE:
//				throw new Exception("IEEDGE has not yet been implemented");
                default:
                    throw new Exception(browser + " Browser has not been Implemented");
            }
        } else if (config.get("execution").equals("grid")) {
            switch (browser) {
                case FIREFOX:
                    desiredCapabilities = DesiredCapabilities.firefox();
                    break;
                case CHROME:
                    desiredCapabilities = DesiredCapabilities.chrome();
                    break;
                case IE:
                    desiredCapabilities = DesiredCapabilities.internetExplorer();
                    break;
//			case IEEDGE:
//				throw new Exception("IEEDGE has not yet been implemented");
                default:
                    throw new Exception(browser + " Browser has not been Implemented");
            }
        }

        setWebDriver(browser, desiredCapabilities);
    }

    /**
     * Sets up the Webdriver for running automation using the supplied browser
     * type and desired capabilities
     *
     * @param browser
     * @param desiredCapabilities
     * @throws Exception
     */
    public void setWebDriver(BrowserType browser, DesiredCapabilities desiredCapabilities) throws Exception {
        if (config.get("execution").equals("local")) {
            switch (browser) {
                case FIREFOX:
                    webDriver = new FirefoxDriver(desiredCapabilities);
                    break;
                case IE:
                    desiredCapabilities.setCapability("nativeEvents", false);
                    desiredCapabilities.setCapability("unexpectedAlertBehaviour", "accept");
                    desiredCapabilities.setCapability("ignoreProtectedModeSettings", true);
                    desiredCapabilities.setCapability("disable-popup-blocking", true);
                    desiredCapabilities.setCapability("enablePersistentHover", true);
                    desiredCapabilities.setCapability(InternetExplorerDriver.ENABLE_ELEMENT_CACHE_CLEANUP, true);
                    webDriver = new InternetExplorerDriver(desiredCapabilities);
                    break;
                case CHROME:
                    webDriver = new ChromeDriver(desiredCapabilities);
                    break;
                default:
                    throw new Exception(browser + " Browser has not been Implemented");
            }
            wait = new WebDriverWait(getWebDriver(), Integer.parseInt(config.get("TimeOut")));
            webDriver.manage().timeouts().implicitlyWait(Long.parseLong(config.get("ImplicitWait")), TimeUnit.SECONDS);
            webDriver.manage().window().maximize();
        } else if (config.get("execution").equals("grid")) {
            webDriver = new RemoteWebDriver(new URL("http://" + config.get("hubIP").toString() + ":" + config.get("hubPort").toString() + "/wd/hub"), desiredCapabilities);
        }
    }

    public String SwichToNewWindowAndReturnParentWindowHandle() {
        String parentWindow = getWebDriver().getWindowHandle();

        Set<String> windows = getWebDriver().getWindowHandles();

        Iterator itr = windows.iterator();
        String childWindow = null;
        while (itr.hasNext()) {
            childWindow = itr.next().toString();

            if (!parentWindow.equals(childWindow)) {
                getWebDriver().switchTo().window(childWindow);
                getWebDriver().manage().window().maximize();

            }
        }
        return parentWindow;
    }

    /**
     * Gets the current instance of webDriver
     *
     * @return WebDriver
     */
    public WebDriver getWebDriver() {
        return webDriver;
    }

    /**
     * Opens URL in given browser
     *
     * @param browser
     * @param url
     * @throws Exception
     */
    public void openBrowser(BrowserType browser, String url) throws Exception {
        setWebDriver(browser);
        getWebDriver().get(url);
        report.log("Action performed : " + getActionName() + browser, LogLevel.INFO);
        report.log("Action performed : " + url + " URL opened ", LogLevel.INFO);
    }

    public void openBrowser(String productURL) throws Exception {
        String url = "http://" + config.get("hubIP") + ":" + config.get("hubPort") + "/wd/hub";
        webDriver = new RemoteWebDriver(new URL(url), desiredCapabilities);
        getWebDriver().get(productURL);

    }

    /**
     * Clicks on element
     *
     * @param elementIdentifier
     */
    public void click(HashMap<String, String> elementIdentifier) {

        WebElement element = find(elementIdentifier);
//		Assert.assertTrue(element.isDisplayed() && element.isEnabled());
        element.click();
        report.log("Action performed : " + getActionName() + element, LogLevel.INFO);
    }

    /**
     * Closes the browser
     */
    public void closeBrowser() {
        getWebDriver().quit();
        report.log("Action performed : " + getActionName(), LogLevel.INFO);
    }

    /**
     * Closes the window
     */
    public void closeWindow() {
        getWebDriver().close();
        report.log("Action performed : " + getActionName(), LogLevel.INFO);
    }

    /**
     * Deletes all the cookies
     */
    public void deleteAllCookies() {
        getWebDriver().manage().deleteAllCookies();
        report.log("Action performed : " + getActionName(), LogLevel.INFO);
    }

    /**
     * Deletes the given cookie
     *
     * @param cookieName
     */
    public void deleteCookie(Cookie cookieName) {
        getWebDriver().manage().deleteCookie(cookieName);
        report.log("Action performed : " + getActionName() + cookieName, LogLevel.INFO);
    }

    /**
     * Double clicks on the identified web element
     *
     * @param elementIdentifier
     */
    public void doubleClick(HashMap<String, String> elementIdentifier) {
        WebElement element = find(elementIdentifier);
        Actions action = new Actions(getWebDriver());
        try {
            if (element.isDisplayed() && element.isEnabled()) {
                action.doubleClick(element).perform();
                report.log("Action performed : " + getActionName() + element, LogLevel.INFO);
            }

        } catch (Exception e) {
            System.out.println("Failed while waiting for mouseover to appear.");
            e.printStackTrace();
        }

    }

    /**
     * Drags the source element to target element
     *
     * @param sourceIdentifier
     * @param targetIdentifier
     */
    public void dragAndDrop(HashMap<String, String> sourceIdentifier, HashMap<String, String> targetIdentifier)
            throws Exception {
        WebElement sourceElement = find(sourceIdentifier);
        WebElement targetElement = find(targetIdentifier);
        Actions action = new Actions(getWebDriver());

        try {
            if (sourceElement.isDisplayed() && sourceElement.isEnabled() && targetElement.isDisplayed()
                    && targetElement.isEnabled()) {
                action.clickAndHold(sourceElement).moveToElement(targetElement).perform();
                waitForTime(2);
                action.release(targetElement).build().perform();
                report.log("Action performed : " + getActionName(), LogLevel.INFO);
                report.log("Source : " + sourceElement + "\n Target : " + targetElement, LogLevel.INFO);
            }

        } catch (Exception e) {
            report.log("Action performed : " + getActionName() + sourceElement, LogLevel.ERROR);
            e.printStackTrace();
        }


    }

    /**
     * Clicks on web element using javascript
     *
     * @param elementIdentifier
     */
    public void clickJScript(HashMap<String, String> elementIdentifier) {
        WebElement element = find(elementIdentifier);
        JavascriptExecutor js = (JavascriptExecutor) getWebDriver();
        try {
            if (element.isDisplayed() && element.isEnabled()) {
                js.executeScript("arguemts[0].click();", element);
                report.log("Action performed : " + getActionName() + element, LogLevel.INFO);
            }
        } catch (Exception e) {
            report.log("Action performed : " + getActionName() + element, LogLevel.ERROR);
            e.printStackTrace();
        }

    }

    /**
     * Executes javascript on identified web element
     *
     * @param elementIdentifier
     * @param script
     */
    public void executeJavaScript(HashMap<String, String> elementIdentifier, String script) {
        WebElement element = find(elementIdentifier);
        JavascriptExecutor js = (JavascriptExecutor) getWebDriver();
//		Assert.assertTrue(element.isDisplayed() & element.isEnabled());
        js.executeScript(script, element);
        report.log("Action performed : " + getActionName() + element, LogLevel.INFO);
    }

    /**
     * Finds all WebElements
     *
     * @param elementIdentifier
     * @return List<WebElement>
     */
    public List<WebElement> findAll(HashMap<String, String> elementIdentifier) {
        List<WebElement> foundElements = null;
        IdentifierMethod[] identifierMethods = IdentifierMethod.values();
        Set<IdentifierMethod> method = new HashSet<>();
        for (IdentifierMethod value : identifierMethods) {
            method.add(value);
        }
        Iterator<IdentifierMethod> iterator = method.iterator();
        IdentifierMethod locator = null;
        do {
            locator = iterator.next();
            if (elementIdentifier.get(locator.toString()) != null) {
                foundElements = findAll(elementIdentifier, locator);
            }
        } while (foundElements == null && iterator.hasNext());

        if (foundElements == null) {
            report.log(getActionName() + " : " + elementIdentifier
                    + " Failed \n No such element could be found with existing repository locators "
                    + elementIdentifier, LogLevel.ERROR);
            throw new NoSuchElementException("No such element could be found with existing repository locators");
        }
        report.log("Action performed : " + getActionName() + foundElements, LogLevel.INFO);
        return foundElements;
    }

    /**
     * Finds all WebElements using locators
     *
     * @param elementIdentifier
     * @param method
     * @return List<WebElement>
     */
    private List<WebElement> findAll(HashMap<String, String> elementIdentifier, IdentifierMethod method) {
        List<WebElement> foundElements = null;
        try {
            switch (method) {
                case ID:
                    foundElements = getWebDriver().findElements(By.id(elementIdentifier.get(method.toString())));
                    break;
                case XPATH:
                    foundElements = getWebDriver().findElements(By.xpath(elementIdentifier.get(method.toString())));
                    break;
                case NAME:
                    foundElements = getWebDriver().findElements(By.name(elementIdentifier.get(method.toString())));
                    break;
                case LINKTEXT:
                    foundElements = getWebDriver().findElements(By.linkText(elementIdentifier.get(method.toString())));
                    break;
                case PARTIALLINKTEXT:
                    foundElements = getWebDriver()
                            .findElements(By.partialLinkText(elementIdentifier.get(method.toString())));
                    break;
                case CLASSNAME:
                    foundElements = getWebDriver().findElements(By.className(elementIdentifier.get(method.toString())));
                    break;
                case CSSSELECTOR:
                    foundElements = getWebDriver().findElements(By.cssSelector(elementIdentifier.get(method.toString())));
                    break;
                case TAGNAME:
                    foundElements = getWebDriver().findElements(By.tagName(elementIdentifier.get(method.toString())));
                    break;
                default:
                    find(elementIdentifier);
            }
        } catch (Exception e) {
            report.log("Element not available with " + method + " : " + elementIdentifier.get(method.toString()) + "\n"
                    + e.getMessage(), LogLevel.WARN);
        }
        return foundElements;
    }

    public void waitForElement(HashMap<String, String> elementIdentifier) {
        WebElement elementer = find(elementIdentifier);
        wait.until(ExpectedConditions.visibilityOf(elementer));
    }


    public boolean checkIfElementIsPresent(HashMap<String, String> elementIdentifier) {
        boolean elementFound = false;
        try {
            if (this.find(elementIdentifier) != null) {
                report.log("Element with identifier" + elementIdentifier + "present.", LogLevel.INFO);
                elementFound = true;
            }
        } catch (NoSuchElementException ne) {
            report.log("Element with identifier" + elementIdentifier + "not present.", LogLevel.INFO);
            elementFound = false;
        }
        return elementFound;
    }


    /**
     * Find element
     *
     * @param elementIdentifier present in element repository
     * @return WebElement
     */
    public WebElement find(HashMap<String, String> elementIdentifier) {
        WebElement foundElement = null;
        IdentifierMethod[] identifierMethods = IdentifierMethod.values();
        Set<IdentifierMethod> method = new HashSet<>();
        for (IdentifierMethod value : identifierMethods) {
            method.add(value);
        }
        Iterator<IdentifierMethod> iterator = method.iterator();
        IdentifierMethod locator = null;

        do {
            locator = iterator.next();
            if (elementIdentifier.get(locator.toString()) != null) {
                foundElement = find(elementIdentifier, locator);
            }
        } while (foundElement == null && iterator.hasNext());

        if (foundElement == null) {
            if(elementIdentifier.containsKey("XPATH")){
                report.log(
                        getActionName() + " : " + elementIdentifier
                                + " Failed \n No such element could be found with existing repository locators : "+elementIdentifier.get("XPATH"),
                        LogLevel.ERROR);
                throw new NoSuchElementException("No such element could be found with locator XPATH : "+elementIdentifier.get("XPATH") );
            }else {
                report.log(
                        getActionName() + " : " + elementIdentifier
                                + " Failed \n No such element could be found with existing repository locators ",
                        LogLevel.ERROR);
                throw new NoSuchElementException("No such element could be found with existing repository locators");
            }
        }
        report.log("Action performed : " + getActionName() + foundElement, LogLevel.INFO);
        return foundElement;
    }

    /**
     * Find element using locators
     *
     * @param IdentifierMethod  : See {@link IdentifierMethod}
     * @param elementIdentifier
     * @return WebElement
     */
    private WebElement find(HashMap<String, String> elementIdentifier, IdentifierMethod method) {
        WebElement foundElement = null;
        try {
            WebDriverWait wait = new WebDriverWait(getWebDriver(), 10);
            switch (method) {
                case ID:
                    try {
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(elementIdentifier.get(method.toString()))));
                    } catch (Exception e) {
                    }
                    foundElement = getWebDriver().findElement(By.id(elementIdentifier.get(method.toString())));
                    break;
                case XPATH:
                    try {
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(elementIdentifier.get(method.toString()))));
                    } catch (Exception e) {
                    }
                    foundElement = getWebDriver().findElement(By.xpath(elementIdentifier.get(method.toString())));
                    break;
                case NAME:
                    try {
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(elementIdentifier.get(method.toString()))));
                    } catch (Exception e) {
                    }
                    foundElement = getWebDriver().findElement(By.name(elementIdentifier.get(method.toString())));
                    break;
                case LINKTEXT:
                    try {
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(elementIdentifier.get(method.toString()))));
                    } catch (Exception e) {
                    }
                    foundElement = getWebDriver().findElement(By.linkText(elementIdentifier.get(method.toString())));
                    break;
                case PARTIALLINKTEXT:
                    try {
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(elementIdentifier.get(method.toString()))));
                    } catch (Exception e) {
                    }
                    foundElement = getWebDriver().findElement(By.partialLinkText(elementIdentifier.get(method.toString())));
                    break;
                case CLASSNAME:
                    try {
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(elementIdentifier.get(method.toString()))));
                    } catch (Exception e) {
                    }
                    foundElement = getWebDriver().findElement(By.className(elementIdentifier.get(method.toString())));
                    break;
                case CSSSELECTOR:
                    try {
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(elementIdentifier.get(method.toString()))));
                    } catch (Exception e) {
                    }
                    foundElement = getWebDriver().findElement(By.cssSelector(elementIdentifier.get(method.toString())));
                    break;
                case TAGNAME:
                    try {
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(elementIdentifier.get(method.toString()))));
                    } catch (Exception e) {
                    }
                    foundElement = getWebDriver().findElement(By.tagName(elementIdentifier.get(method.toString())));
                    break;
                default:
                    find(elementIdentifier);
            }
        } catch (Exception e) {
            report.log("Element not available with " + method + " : " + elementIdentifier.get(method.toString()) + "\n"
                    + e.getMessage(), LogLevel.WARN);
        }
        return foundElement;
    }

    /**
     * wait for textbox value to change
     */

    public void textBoxStatusChange(String identifier, String Expected) {
        while (!getWebDriver().findElement(By.xpath(identifier)).getText().equalsIgnoreCase(Expected)) {
            System.out.println("waiting for text to be loaded");
        }
    }


    /**
     * Gets the text on the web element
     *
     * @param elementIdentifier
     * @return String
     */
    public String getText(HashMap<String, String> elementIdentifier) {
        WebElement element = find(elementIdentifier);
        report.log("Action performed : " + getActionName() + element, LogLevel.INFO);
        return element.getText();
    }

    /**
     * Navigates to previous page in browser history
     */
    public void goBack() {
        getWebDriver().navigate().back();
        report.log("Action performed : " + getActionName(), LogLevel.INFO);
    }

    /**
     * Navigates to next page in browser history
     */
    public void goForward() {
        getWebDriver().navigate().forward();
        report.log("Action performed : " + getActionName(), LogLevel.INFO);
    }

    /**
     * Takes the mouse curser to identified web element
     *
     * @param identifier
     */
    public void mouseHover(HashMap<String, String> elementIdentifier) {
        WebElement element = find(elementIdentifier);
        Actions action = new Actions(getWebDriver());
//		Assert.assertTrue(element.isDisplayed());
        action.moveToElement(element).perform();
        try {
//			Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("Failed while waiting for mouseover to appear.");
            e.printStackTrace();
        }
        report.log("Action performed : " + getActionName() + element, LogLevel.INFO);

    }

    /**
     * Navigates to given URL
     *
     * @param URL
     */
    public void navigateToURL(String URL) {
        getWebDriver().navigate().to(URL);
        report.log("Action performed : " + getActionName(), LogLevel.INFO);
    }

    /**
     * Refreshes the current page
     */
    public void refresh() {
        getWebDriver().navigate().refresh();
        report.log("Action performed : " + getActionName(), LogLevel.INFO);
    }


    /**
     * Right clicks on identified web element
     *
     * @param elementIdentifier
     * @param identifier
     */
    public void rightClick(HashMap<String, String> elementIdentifier) {
        WebElement element = find(elementIdentifier);
        Actions action = new Actions(getWebDriver());
        action.contextClick(element).perform();
        report.log("Action performed : " + getActionName() + element, LogLevel.INFO);
    }

    /**
     * Selects element in dropdown by visible text
     *
     * @param elementIdentifier
     * @param visibleText
     */
    public void selectItemByVisibleText(HashMap<String, String> elementIdentifier, String visibleText) {
        WebElement element = find(elementIdentifier);

        Select dropdown = new Select(element);
        dropdown.selectByVisibleText(visibleText.toString());
//		new Select(element).selectByVisibleText(visibleText);
        report.log("Action performed : " + getActionName() + element, LogLevel.INFO);
    }

    /**
     * Selects element in dropdown by index
     *
     * @param elementIdentifier
     * @param index
     */
    public void selectItemByIndex(HashMap<String, String> elementIdentifier, int index) {
        WebElement element = find(elementIdentifier);

        try {
            if (element.isDisplayed() & element.isEnabled()) {
                new Select(element).selectByIndex(index);
                report.log("Action performed : " + getActionName() + element, LogLevel.INFO);
            }
        } catch (Exception e) {
            report.log("Action performed : " + getActionName() + element, LogLevel.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Selects element in dropdown by value
     *
     * @param elementIdentifier
     * @param value
     */
    public void selectItemByValue(HashMap<String, String> elementIdentifier, String value) {
        WebElement element = find(elementIdentifier);

        try {
            if (element.isDisplayed() & element.isEnabled()) {
                new Select(element).selectByValue(value);
                report.log("Action performed : " + getActionName() + element, LogLevel.INFO);
            }
        } catch (Exception e) {
            report.log("Action performed : " + getActionName() + element, LogLevel.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Sets text on the web element
     *
     * @param elementIdentifier
     * @param text
     */
    public void setText(HashMap<String, String> elementIdentifier, String text) {
        WebElement element = find(elementIdentifier);

        try {
            if (element.isDisplayed() && element.isEnabled()) {
                element.clear();
                element.sendKeys(text);
                report.log("Action performed : " + getActionName() + element, LogLevel.INFO);
            }

        } catch (Exception e) {
            report.log("Action performed : " + getActionName() + element, LogLevel.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Checks the checkbox or radiobutton
     *
     * @param elementIdentifier
     */
    public void check(HashMap<String, String> elementIdentifier) {
        WebElement element = find(elementIdentifier);
        try {
            if (element.isDisplayed() && element.isEnabled() && !element.isSelected()) {
                element.click();
                report.log("Action performed : " + getActionName() + element, LogLevel.INFO);
            }
        } catch (Exception e) {
            report.log("Action performed : " + getActionName() + element, LogLevel.ERROR);
            e.printStackTrace();
        }

    }

    /**
     * Unchecks the checkbox or radiobutton
     *
     * @param elementIdentifier
     */
    public void uncheck(HashMap<String, String> elementIdentifier) {
        WebElement element = find(elementIdentifier);
        try {
            if (element.isDisplayed() && element.isEnabled() && element.isSelected()) {
                element.click();
                report.log("Action performed : " + getActionName() + element, LogLevel.INFO);
            }
        } catch (Exception e) {
            report.log("Action performed : " + getActionName() + element, LogLevel.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Submits the web element
     *
     * @param elementIdentifier
     */
    public void submit(HashMap<String, String> elementIdentifier) {
        WebElement element = find(elementIdentifier);
        try {
            if (element.isDisplayed() & element.isEnabled()) {
                element.submit();
                report.log("Action performed : " + getActionName() + element, LogLevel.INFO);
            }

        } catch (Exception e) {
            report.log("Action performed : " + getActionName() + element, LogLevel.ERROR);
            e.printStackTrace();
        }

    }

    /**
     * Switches to default content
     */
    public void switchToDefaultContent() {
        getWebDriver().switchTo().defaultContent();
        report.log("Action performed : " + getActionName(), LogLevel.INFO);
    }

    /**
     * Switches to identified frame
     *
     * @param elementIdentifier
     */
    public void switchToFrame(String frameObject) {
        //TODO: Added code to dynamically get name / id / index  and use it in switch to frame function
        //String frameName = elementIdentifier;
        getWebDriver().switchTo().frame(frameObject);
        report.log("Action performed : " + getActionName() + frameObject, LogLevel.INFO);
    }


    public void switchToFrame(HashMap<String, String> elementIdentifier) {

        WebElement element = find(elementIdentifier);
        getWebDriver().switchTo().frame(element);
        report.log("Action performed : " + getActionName() + elementIdentifier, LogLevel.INFO);
    }

    /**
     * Switches to given window title
     *
     * @param windowTitle
     */

    /**
     * Switches to given window title
     *
     * @param windowHandle
     */
    public void switchToWindow(String handle) {
        getWebDriver().switchTo().window(handle);
        report.log("Action performed : " + getActionName() + handle, LogLevel.INFO);
    }

    public void SwichToNewWindow() {
        String parentWindow = getWebDriver().getWindowHandle();


        Set<String> windows = getWebDriver().getWindowHandles();

        Iterator itr = windows.iterator();
        String childWindow = null;
        while (itr.hasNext()) {
            childWindow = itr.next().toString();

            if (!parentWindow.equals(childWindow)) {
                getWebDriver().switchTo().window(childWindow);
                getWebDriver().manage().window().maximize();


            }
        }

    }

    /**
     * Returns current window handle
     *
     * @return windowHandle
     */
    public String getWindowHandle() {
        return getWebDriver().getWindowHandle();
    }

    /**
     * Returns all window handles
     *
     * @return Set<windowHandle>
     */
    public Set<String> getWindowHandles() {
        return getWebDriver().getWindowHandles();

    }

    /**
     * Verifies whether checkbox is selected or not
     *
     * @param elementIdentifier
     * @return boolean
     */
    public boolean verifyCheckbox(HashMap<String, String> elementIdentifier) {
        WebElement element = find(elementIdentifier);
        try {
            if (element.isDisplayed() && element.isEnabled() && element.isSelected()) {
                report.log("Action performed : " + getActionName() + element, LogLevel.INFO);
                return true;
            }

        } catch (Exception e) {
            report.log("Action performed : " + getActionName() + element, LogLevel.ERROR);
            e.printStackTrace();
        }
        return false;

    }

    /**
     * Verifies whether checkbox is selected or not
     *
     * @param elementIdentifier
     * @return boolean
     */
    public boolean verifyElementPresent(String elementIdentifier) {
        if (getWebDriver().findElements(By.xpath(elementIdentifier)).size() != 0) {

            return true;
        } else {
            getWebDriver().quit();
            return false;
        }

    }

    public boolean verifyElementPresentISU(String elementIdentifier) {
        if (getWebDriver().findElements(By.xpath(elementIdentifier)).size() != 0) {
            getWebDriver().quit();
            return true;
        } else {

            return false;
        }

    }

    /**
     * Verifies the given page title
     *
     * @param pageTitle
     * @return boolean
     */
    public boolean verifyPageTitle(String pageTitle) {
        try {
            if (getWebDriver().getTitle().equals(pageTitle)) {
                report.log("Action performed : " + getActionName() + " : " + pageTitle, LogLevel.INFO);
                return true;
            }

        } catch (Exception e) {
            report.log("Action performed : " + getActionName(), LogLevel.ERROR);
            e.printStackTrace();
            getWebDriver().quit();
        }
        getWebDriver().quit();
        return false;
    }

    /**
     * Verifies the text of identified web element
     *
     * @param elementIdentifier
     * @param text
     * @return boolean
     */
    public boolean verifyText(HashMap<String, String> elementIdentifier, String text) {
        WebElement element = find(elementIdentifier);

        try {

            if (element.isDisplayed() && element.getText().equals(text)) {
                report.log("Action performed : " + getActionName() + element, LogLevel.INFO);
                return true;
            }

        } catch (Exception e) {
            report.log("Action performed : " + getActionName() + element, LogLevel.ERROR);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Verifies the current URL
     *
     * @param URL
     * @return boolean
     */
    public boolean verifyURL(String URL) {
        try {
            if (getWebDriver().getCurrentUrl().equals(URL)) {
                report.log("Action performed : " + getActionName() + " : " + URL, LogLevel.INFO);
                return true;
            }

        } catch (Exception e) {
            report.log("Action performed : " + getActionName() + URL, LogLevel.ERROR);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Clicks on web element when element is clickable
     *
     * @param elementIdentifier
     */
    public void clickWhenReady(HashMap<String, String> elementIdentifier) {
        WebElement element = find(elementIdentifier);
        element = wait.until(ExpectedConditions.elementToBeClickable(element));


        element.click();
        report.log("Action performed : " + getActionName() + element, LogLevel.INFO);
    }

    /**
     * Waits for partiular element for specified amount of time
     *
     * @param elementIdentifier
     * @return WebElement
     * updated by aakash
     */
    public WebElement waitForElementExplicitly(HashMap<String, String> elementIdentifier) {
        WebElement element = null;

        if (wait == null) {
            wait = new WebDriverWait(getWebDriver(), Integer.parseInt(config.get("TimeOut")));
        }
        if (elementIdentifier.get("ID") != null) {
            element = wait.until(ExpectedConditions.elementToBeClickable(By.id(elementIdentifier.get("ID"))));
        } else if (elementIdentifier.get("XPATH") != null) {
            element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(elementIdentifier.get("XPATH"))));
        } else if (elementIdentifier.get("NAME") != null) {
            element = wait.until(ExpectedConditions.elementToBeClickable(By.name(elementIdentifier.get("NAME"))));
        } else if (elementIdentifier.get("LINKTEXT") != null) {
            element = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(elementIdentifier.get("LINKTEXT"))));
        } else if (elementIdentifier.get("PARTIALLINKTEXT") != null) {
            element = wait
                    .until(ExpectedConditions.elementToBeClickable(By.partialLinkText(elementIdentifier.get("PARTIALLINKTEXT"))));
        } else if (elementIdentifier.get("CLASSNAME") != null) {
            element = wait.until(ExpectedConditions.elementToBeClickable(By.className(elementIdentifier.get("CLASSNAME"))));
        } else if (elementIdentifier.get("TAGNAME") != null) {
            element = wait.until(ExpectedConditions.elementToBeClickable(By.tagName(elementIdentifier.get("TAGNAME"))));
        } else if (elementIdentifier.get("CSSSELECTOR") != null) {
            element = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(elementIdentifier.get("CSSSELECTOR"))));
        } else {
            report.log("Action Not performed : " + getActionName() + " : " + element, LogLevel.ERROR);
        }
        report.log("Action performed : " + getActionName() + element, LogLevel.INFO);
        return element;

    }


    /**
     * Waits for an element to appear within the given timeOut in seconds.
     *
     * @param elementIdentifier
     * @param timeOutInSeconds
     * @return boolean
     */
    public boolean waitForElementToDisappear(HashMap<String, String> elementIdentifier, int timeOutInSeconds) {
        boolean elementDisplayed = true;
        double startTime = System.currentTimeMillis();
        double endTime = startTime + timeOutInSeconds * 1000;
        try {
            do {
                Thread.sleep(1500);
                if (isElementDisplayed(elementIdentifier) && isElementEnabled(elementIdentifier)) {
                    report.log("Element with identifier" + elementIdentifier + "is displayed still displayed", LogLevel.INFO);
                    elementDisplayed = true;
                    System.out.println("Element displayed .... rechecking" + elementIdentifier.toString());
                } else {
                    elementDisplayed = false;
                    break;
                }
            } while (endTime > System.currentTimeMillis());
        } catch (NoSuchElementException e) {
            elementDisplayed = false;
        } catch (StaleElementReferenceException se) {
            elementDisplayed = false;
        } catch (InterruptedException i) {
            report.log("Thread interrupted while waiting for element to disappear", LogLevel.ERROR);
        }
        if (elementDisplayed) {
            report.log("Element displayed even after timeOut", LogLevel.ERROR);
        } else {
            report.log("Element with identifier " + elementIdentifier + "no longer displayed ", LogLevel.INFO);
        }
        return elementDisplayed;
    }

    /**
     * Waits for an element to appear within the default timeOut in seconds given in the config file.
     *
     * @param elementIdentifier
     * @return boolean
     */
    public boolean waitForElementToAppear(HashMap<String, String> elementIdentifier) {
        int timeOutInSeconds = Integer.parseInt(config.get("TimeOut"));
        return waitForElementToAppear(elementIdentifier, timeOutInSeconds);
    }

    /**
     * Waits for an element to appear within the given timeOut in seconds.
     *
     * @param elementIdentifier
     * @param timeOutInSeconds
     * @return boolean
     */
    public boolean waitForElementToAppear(HashMap<String, String> elementIdentifier, int timeOutInSeconds) {

//		WebDriverWait wait = new WebDriverWait(driver, 10);
//		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id("someid")));
//
        boolean flag = false;
        try {
            FluentWait<WebDriver> wait = new FluentWait<WebDriver>(getWebDriver()).withTimeout(timeOutInSeconds, TimeUnit.SECONDS)
                    .pollingEvery(5, TimeUnit.SECONDS).ignoring(NoSuchFrameException.class);
            wait.until(ExpectedConditions.visibilityOf(find(elementIdentifier)));
            report.log("Element with identifier" + elementIdentifier + "found within the timeout of " + timeOutInSeconds + "seconds", LogLevel.INFO);
            flag = true;

        } catch (Exception e) {
            report.log("Could not find element with identifier" + elementIdentifier + "within timeout of " + timeOutInSeconds + "seconds", LogLevel.ERROR);
            flag = false;
        }

        return flag;
        /*double startTime = System.currentTimeMillis();
//
		boolean flag = false;
		try{
			FluentWait<WebDriver> wait = new FluentWait<WebDriver>(getWebDriver()).withTimeout(timeOutInSeconds, TimeUnit.SECONDS)
					.pollingEvery(5, TimeUnit.SECONDS).ignoring(NoSuchFrameException.class);
			wait.until(ExpectedConditions.visibilityOf(find(elementIdentifier)));
			report.log("Element with identifier" + elementIdentifier + "found within the timeout of " + timeOutInSeconds + "seconds", LogLevel.INFO);
			flag = true;
		}catch(Exception e){
			report.log("Could not find element with identifier" + elementIdentifier + "within timeout of " + timeOutInSeconds + "seconds", LogLevel.ERROR);
			flag = false;
		}

		return flag;
		/*double startTime = System.currentTimeMillis();
		double endTime = startTime + timeOutInSeconds * 1000;
		do{
			if(checkIfElementIsPresent(elementIdentifier)!=true){
				report.log("Element with identifier" + elementIdentifier + "found within the timeout of " + timeOutInSeconds + "seconds", LogLevel.INFO);
				return true;
			}
		}while(endTime < System.currentTimeMillis());
		report.log("Could not find element with identifier" + elementIdentifier + "within timeout of " + timeOutInSeconds + "seconds", LogLevel.ERROR);
		return false;*/
    }


    /**
     * Verifies element is visible or not
     *
     * @param elementIdentifier
     * @return boolean
     */
    public boolean isElementDisplayed(HashMap<String, String> elementIdentifier) {
        WebElement element = find(elementIdentifier);
        report.log("Action performed : " + getActionName() + element, LogLevel.INFO);
        return element.isDisplayed();
    }

    /**
     * Verifies element is selected or not
     *
     * @param elementIdentifier
     * @return boolean
     */
    public boolean isElementSelected(HashMap<String, String> elementIdentifier) {
        WebElement element = find(elementIdentifier);
        report.log("Action performed : " + getActionName() + element, LogLevel.INFO);
        return element.isSelected();
    }

    /**
     * Verifies element is enabled or not
     *
     * @param elementIdentifier
     * @return boolean
     */
    public boolean isElementEnabled(HashMap<String, String> elementIdentifier) {
        WebElement element = find(elementIdentifier);
        report.log("Action performed : " + getActionName() + element, LogLevel.INFO);
        return element.isEnabled();
    }

    /**
     * Switches to identified Alert
     *
     * @return Alert
     */
    public Alert switchToAlert() {
        Alert alert = getWebDriver().switchTo().alert();
        report.log("Action performed : " + getActionName() + alert, LogLevel.INFO);
        return alert;
    }

    /**
     * Waits for specified amount of time in seconds
     *
     * @param timeout
     * @throws InterruptedException
     */
    public void waitForTime(int timeout) {

        try {
            Thread.sleep(timeout * 1000);
            report.log("Action performed : " + getActionName() + timeout, LogLevel.INFO);
        } catch (InterruptedException e) {
            report.log("InterruptedException in waiting for element: " + getActionName() + timeout, LogLevel.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Verify Alert Message
     *
     * @param String
     * @return boolean
     */
    public boolean verifyAlertMessage(String messageText) {
        Alert alert = getWebDriver().switchTo().alert();
        try {
            if (alert.getText().equals(messageText)) {
                report.log("Action performed : " + getActionName(), LogLevel.INFO);
                return true;
            }

        } catch (Exception e) {
            report.log("Action performed : " + getActionName(), LogLevel.ERROR);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Gets the text on the web element
     *
     * @param elementIdentifier
     * @param attribute
     * @return String
     */
    public String getTextByAttribute(HashMap<String, String> elementIdentifier, String attribute) {
        WebElement element = find(elementIdentifier);
        try {
            if (element.isDisplayed()) {
                report.log("Action performed : " + getActionName(), LogLevel.INFO);
                return element.getAttribute(attribute);
            }

        } catch (Exception e) {
            report.log("Action performed : " + getActionName() + element, LogLevel.ERROR);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Verifies the text of identified web element
     *
     * @param elementIdentifier
     * @param text
     * @return boolean
     */
    public boolean verifyTextAttribute(HashMap<String, String> elementIdentifier, String text) {
        WebElement element = find(elementIdentifier);
        try {

            if (element.isDisplayed() && element.getAttribute("value").equals(text)) {
                report.log("Action performed : " + getActionName(), LogLevel.INFO);
                return true;
            }

        } catch (Exception e) {
            report.log("Action performed : " + getActionName() + element, LogLevel.ERROR);
            e.printStackTrace();
        }
        return false;
    }

    private String getActionName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    public static com.vanilla.afour.afour.autolib.util.BrowserType getBrowser(String browser) {
        if (browser.equals("CHROME")) {
            return CHROME;
        } else if (browser.equals("FIREFOX")) {
            return FIREFOX;
        } else if (browser.equals("IE")) {
            return IE;
        } else {
            return null;
        }

    }

    public enum GridPlatform {

        VISTA, WIN8, WIN8_1, WIN10

    }


    public void setCapabilities() {
        String fromConfig = config.get("Browser").toString();
        String fromEnum = CHROME.toString();

        if (config.get("Browser").toString().equals(CHROME.toString())) {
            desiredCapabilities = new DesiredCapabilities().chrome();
            desiredCapabilities.setBrowserName("chrome");
        } else if (config.get("Browser").toString().equals(FIREFOX.toString())) {
            //System.setProperty("webdriver.gecko.driver","C:\\DSAHU\\geckodriver.exe");
            desiredCapabilities = new DesiredCapabilities().firefox();
            // desiredCapabilities.setCapability("marionette", true);
            desiredCapabilities.setBrowserName("Firefox");
        } else if (config.get("Browser").toString().equals(IE.toString())) {
            desiredCapabilities = new DesiredCapabilities().internetExplorer();
            desiredCapabilities.setBrowserName("internet explorer");
            System.setProperty("webdriver.ie.driver", "Drivers/IEDriverServer.exe");
            desiredCapabilities.setCapability("nativeEvents", false);
            desiredCapabilities.setCapability("unexpectedAlertBehaviour", "accept");
            desiredCapabilities.setCapability("ignoreProtectedModeSettings", true);
            desiredCapabilities.setCapability("disable-popup-blocking", true);
            desiredCapabilities.setCapability("enablePersistentHover", true);

        }
        // Need to add enum for platform at com.afour.automation.utilities;
//		if(config.get("gridPlatform").equals(GridPlatform.VISTA.toString()))
//			desiredCapabilities.setPlatform(Platform.VISTA);
//		else if(config.get("gridPlatform").equals(GridPlatform.WIN8.toString()))
//			desiredCapabilities.setPlatform(Platform.WIN8);
//		if(config.get("gridPlatform").equals(GridPlatform.WIN8_1.toString()))
//			desiredCapabilities.setPlatform(Platform.WIN8_1);
//		if(config.get("gridPlatform").equals(GridPlatform.WIN10.toString()))
//			desiredCapabilities.setPlatform(Platform.WIN10);


    }

    public String getDropdownVisibleText(HashMap<String, String> elementIdentifier) {
        WebElement element = find(elementIdentifier);
        WebElement elementSelected;
        String currentlyVisibleText;
        Select dropdown = new Select(element);
        elementSelected = dropdown.getFirstSelectedOption();
        currentlyVisibleText = elementSelected.getText();
        return currentlyVisibleText;
    }

    public void scrollToElement(HashMap<String, String> elementIdentifier){
        WebElement element = find(elementIdentifier);
        ((JavascriptExecutor) getWebDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Sets text on the web element
     *
     * @param elementIdentifier
     * @param attributeName
     */
    public String getCssValue(HashMap<String, String> elementIdentifier, String attributeName) {
        WebElement element = find(elementIdentifier);
        String value = "";
        try {
            if (element.isDisplayed() && element.isEnabled()) {
                report.log("Action performed : " + getActionName() + element, LogLevel.INFO);
                value =  element.getCssValue(attributeName);
                value = Color.fromString(value).asHex();
            }

        } catch (Exception e) {
            report.log("Action performed : " + getActionName() + element, LogLevel.ERROR);
            e.printStackTrace();
        }
        return value;
    }
}