package com.vanilla.afour.trialscope.pages;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import com.vanilla.afour.afour.autolib.util.ObjectRepositoryLoader;

/**
 * Created by afourtech on 3/8/2017.
 */
public class CommonFunctionPage extends PharmaCMBasePage {
    HashMap<String, HashMap<String, String>> commonPageElement = null;


    public HashMap<String, String> config = null;

    public CommonFunctionPage() throws Exception {

        config = driver.getConfigData();
        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");

    }

    /**
     * Method to select record type and click continue button
     * @param recordType
     * @return
     */
    public boolean selectRecordType(String recordType) {
        driver.waitForElementToAppear(commonPageElement.get("selectRecordTypeNewRecord"), 10);
        driver.selectItemByVisibleText(commonPageElement.get("selectRecordTypeNewRecord"), recordType);
        driver.click(commonPageElement.get("continue"));
        return true;
    }

    public boolean verifyPageDescription(String textVerification) {
        driver.waitForElementToAppear(commonPageElement.get("verifyPageDescription"), 10);
        String verifyText = driver.getText(commonPageElement.get("verifyPageDescription"));
        return verifyText.contains(textVerification);

    }

    public boolean verifyPageType(String textVerification) {
        boolean isPageTextVerify = false;
        driver.waitForElementToAppear(commonPageElement.get("verifyPageType"), 10);
        String verifyText = driver.getText(commonPageElement.get("verifyPageType"));
        verifyText.contains(textVerification);
        isPageTextVerify = true;
        return isPageTextVerify;
    }

    public boolean verifyLoggedInUser(String textVerification) {
        driver.waitForElementToAppear(commonPageElement.get("verifyLoggedInUser"), 10);
        String verifyText = driver.getText(commonPageElement.get("verifyLoggedInUser"));
        return verifyText.contains(textVerification);

    }

    public String getPageType() {
        driver.waitForElementToAppear(commonPageElement.get("verifyPageType"), 10);
        return driver.getText(commonPageElement.get("verifyPageType"));
    }

    public String getPageDescription() {
        driver.waitForElementToAppear(commonPageElement.get("verifyPageDescription"), 20);
        return driver.getText(commonPageElement.get("verifyPageDescription"));
    }

    /**
     * Function to get main title of the page
     * @return String : e.g. "Plan Detail", "Event Registration Detail" etc.
     */
    public String getPageMainTitle() {
        driver.waitForElementToAppear(commonPageElement.get("getMainTitle"), 20);
        return driver.getText(commonPageElement.get("getMainTitle"));
    }

    public boolean delete() {
        driver.waitForElementToAppear(commonPageElement.get("delete"), 10);
        driver.click(commonPageElement.get("delete"));
        return true;

    }

    public boolean alertOk() throws InterruptedException {
        try {
        	WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), 30);
            wait.until(ExpectedConditions.alertIsPresent());
            driver.getWebDriver().switchTo().alert().accept();
            Thread.sleep(5000);
            boolean isAccountTeamMember = true;
        } catch (Exception e) {
        }
        return true;

    }

    public String alertMessage() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), 30);
        wait.until(ExpectedConditions.alertIsPresent());
        return driver.getWebDriver().switchTo().alert().getText();
    }

    /**
     * Function to hover on hover link present on the page
     * @param linkName : name of the link to hover on
     * @return : Boolean
     * @throws Exception
     */
    public boolean hoverOnLink(String linkName) throws Exception {
        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        String name = (commonPageElement.get("hoverLinks").get("XPATH")).replace("##replaceString##", linkName);
        commonPageElement.get("hoverLinks").put("XPATH", name);
        driver.mouseHover(commonPageElement.get("hoverLinks"));
        // verify link name on the pop-up
        commonPageElement.get("hoverLinksPopUpLabel").put("XPATH", (commonPageElement.get("hoverLinksPopUpLabel").get("XPATH")).replace("##replaceString##", linkName));
        List<WebElement> list = driver.findAll(commonPageElement.get("hoverLinksPopUpLabel"));
        if (list.size() == 0) {
            System.out.println(list.size());
            return false;
        }
        return true;
    }

    /**
     * Function to select Detail Tab
     */
    public void switchToDetailTab()
    {
        try {
            driver.waitForElementToAppear(commonPageElement.get("DetailsTab"), 10);
            driver.click(commonPageElement.get("DetailsTab"));
        }
        catch (Exception e){
            System.out.println("Details tab not found and not clicked");
        }
    }

    /**
     * Function to get data from any hover table
     * Note: it will exclude the first column as it contains Edit|Del link
     *
     * @return
     */
    public HashMap<String, String[]> getDataFromHoverTable() throws Exception{
        HashMap<String, String[]> data = new HashMap<>();
        Thread.sleep(2000);
        driver.switchToFrame("RLPanelFrame");
        List<WebElement> rows = driver.findAll(commonPageElement.get("hoverLinksDataRow"));
        List<WebElement> cols = rows.get(0).findElements(By.xpath("./*"));
        for (int i = 0; i < rows.size(); i++) {
            String[] rowData = new String[(cols.size() - 1)];
            for (int j = 2; j <= cols.size(); j++) {
                rowData[(j - 2)] = rows.get(i).findElement(By.xpath("./*[" + j + "]")).getText();
            }
            data.put("Record_" + i, rowData);
        }
        driver.switchToDefaultContent();
        return data;
    }

    /**
     * Function to click on Delete button on Details Page (of any type Contact , Account etc)
     *
     * @return : void
     * @throws Exception
     */
    public void clickDeleteBtnOnDetailsPage() throws Exception {
        driver.waitForElementToAppear(commonPageElement.get("deleteButtonDetailsPage"), 10);
        driver.click(commonPageElement.get("deleteButtonDetailsPage"));
    }

    /**
     * Function to click on Edit button on Details Page (of any type Contact , Account etc)
     *
     * @return : void
     * @throws Exception
     */
    public void clickEditBtnOnDetailsPage() throws Exception {
        driver.waitForElementToAppear(commonPageElement.get("editButtonDetailsPage"), 10);
        driver.click(commonPageElement.get("editButtonDetailsPage"));
    }

    /**
     * Function to click on Save button on Edit Page (of any type Contact , Account etc)
     *
     * @return : void
     * @throws Exception
     */
    public void clickSaveBtnOnEditPage() throws Exception {
        driver.waitForElementToAppear(commonPageElement.get("saveButtonEditPage"), 10);
        try {
            driver.click(commonPageElement.get("saveButtonEditPage"));
        }catch (Exception e){
            Reporter.log("Try clicking on Save button on top");
            driver.click(commonPageElement.get("topSaveButtonEditPage"));
        }
    }

    /**
     * Function to click on Save Ignore Alert button on Edit Page (of any type Contact , Account etc)
     *
     * @return : void
     * @throws Exception
     */
    public void clickSaveIgnoreAlertBtnOnEditPage() throws Exception {
        try {
//            driver.waitForElementToAppear(commonPageElement.get("saveIgnoreAlert"), 2);
            driver.click(commonPageElement.get("saveIgnoreAlert"));
//            driver.waitForElementToAppear(commonPageElement.get("pageDetailsTab"),1);
//            driver.click(commonPageElement.get("pageDetailsTab"));
        } catch (Exception e) {
            System.out.println("In Catch of save ignore alert");
//            try {
//                driver.click(commonPageElement.get("pageDetailsTab"));
//            }catch (Exception e2){}
        }
    }

    /**
     * Function to get value of given field
     *
     * @param fieldName : label of the field
     * @return : String
     * @throws Exception
     */
    public String getFieldValue(String fieldName) throws Exception {
        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        String name = (commonPageElement.get("fieldValue").get("XPATH")).replace("##replaceString##", fieldName);
        commonPageElement.get("fieldValue").put("XPATH", name);
        return driver.getText(commonPageElement.get("fieldValue"));
    }

    /**
     * Function to select values from View dropList present on Home page(of any tab) and click Go button
     *
     * @param optionName
     * @throws Exception
     */
    public void selectOptionFromViewDropList(String optionName) throws Exception {
        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        // open vie drop-list to see options
        driver.click(commonPageElement.get("viewDropList"));
        String name = (commonPageElement.get("viewOptionName").get("XPATH")).replace("##replaceString##", optionName);
        commonPageElement.get("viewOptionName").put("XPATH", name);
        // click on value to select
        driver.click(commonPageElement.get("viewOptionName"));
        // click on go button
        try {
            driver.click(commonPageElement.get("goButton"));
        } catch (Exception e){}
    }

    /**
     * Function to click on provided record (AccountName , Contact , Training Event etc ) present in the table view of selected tab home page
     *
     * @param OptionToSelect
     * @return String : Text of the clicked record
     * @throws Exception
     */

    public String clickRecordPresentInTable(String OptionToSelect) throws Exception {
        String clickedLink = null;
        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        String name = (commonPageElement.get("nameInTable").get("XPATH")).replace("##replaceString##", OptionToSelect);
        commonPageElement.get("nameInTable").put("XPATH", name);

        do {
            List<WebElement> AccountContacts = driver.findAll(commonPageElement.get("nameInTable"));
            if (AccountContacts.size() > 0) {
                //driver.click(contactHomePageElement.get("myContactNames"));

/*                WebElement ele = driver.find(commonPageElement.get("nameInTable"));
                clickedLink = ele.getText();
                ele.click();
                break;
//                driver.find(commonPageElement.get("nameInTable")).click();
*/       	
            WebElement ele = driver.find(commonPageElement.get("nameInTable"));
		   ((JavascriptExecutor) driver.getWebDriver()).executeScript("arguments[0].scrollIntoView(true);", ele);
		    Thread.sleep(500);
			clickedLink = ele.getText();
			ele.click();
			break;
			//driver.find(commonPageElement.get("nameInTable")).click();      
            	} else {
                driver.click(commonPageElement.get("nextInTable"));
            }
            // i++;
        } while (driver.isElementDisplayed(commonPageElement.get("allListinTable")));//i <= page

        return clickedLink;

    }
//    public String clickRecordPresentInTable(String OptionToSelect) throws Exception {
//        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
//        String name = (commonPageElement.get("nameInTable").get("XPATH")).replace("##replaceString##", OptionToSelect);
//        commonPageElement.get("nameInTable").put("XPATH", name);
//        WebElement ele = driver.find(commonPageElement.get("nameInTable"));
//        String clickedLink = ele.getText();
//        ele.click();
//        return clickedLink;
//
//
//    }

    /**
     * Function to get value of Account team member
     *
     * @param fieldName : label of the field
     * @return : String
     * @throws Exception
     */
    public String getFieldValueForAccTeamMem(String fieldName) throws Exception {
        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        String name = (commonPageElement.get("fieldValueForAccTeamMem").get("XPATH")).replace("##replaceString##", fieldName);
        commonPageElement.get("fieldValueForAccTeamMem").put("XPATH", name);
        return driver.getText(commonPageElement.get("fieldValueForAccTeamMem"));
    }

    /**
     * Function to get value of given field with ?
     *
     * @param fieldNameWithQuestionMark : label of the field
     * @return : String
     * @throws Exception
     */
    public String getFieldValueWithQuestionMark(String fieldNameWithQuestionMark) throws Exception {
        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        String name = (commonPageElement.get("fieldValueWithQuestionMark").get("XPATH")).replace("##replaceString##", fieldNameWithQuestionMark);
        commonPageElement.get("fieldValueWithQuestionMark").put("XPATH", name);
        String City = driver.getText(commonPageElement.get("fieldValueWithQuestionMark"));
        return driver.getText(commonPageElement.get("fieldValueWithQuestionMark"));
    }


    public void selectItemForLookUpField(HashMap<String, String> elementIdentifier, String valueToSelect) throws Exception {

        if (valueToSelect.equals("GEN:GEN")) {
            String parentHandle = driver.getWindowHandle(); // get the current window handle
            // click on magnifying icon of the field
            driver.click(elementIdentifier);
            Thread.sleep(3000);
            // switch to next window
            ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
            tabs.remove(parentHandle);
            for (String winHandle : tabs) {
                driver.getWebDriver().switchTo().window(winHandle);
                if (driver.waitForElementToAppear(commonPageElement.get("lookupFrame"), 20)) {
                    driver.getWebDriver().switchTo().frame(new WebDriverWait(driver.getWebDriver(), 10)
                            .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#searchFrame"))));
                    driver.setText(commonPageElement.get("searchBoxLookupWindow"), valueToSelect);
                    driver.click(commonPageElement.get("goButtonLookUpWindow"));
                    Thread.sleep(2000);
                    driver.getWebDriver().switchTo().defaultContent();
                    WebDriverWait webDriverWait = new WebDriverWait(driver.getWebDriver(), 30);
                    webDriverWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath("//*[@id='resultsFrame']")));

                    this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");

                    String locator = commonPageElement.get("clickSearchedItemOnLookUpWindow").get("XPATH").replace("##replaceString##", valueToSelect);
                    commonPageElement.get("clickSearchedItemOnLookUpWindow").put("XPATH", locator);
                    driver.click(commonPageElement.get("clickSearchedItemOnLookUpWindow"));
                }
                driver.getWebDriver().switchTo().window(parentHandle);
            }
        } else {
            driver.waitForElementToAppear(elementIdentifier, 10);
            driver.setText(elementIdentifier, valueToSelect);
        }
    }


    /**
     * Function to click the "printable view" link on the top right on any details page
     *
     * @return :
     * @throws Exception
     */
    public void clickPrintableView() throws Exception {
        driver.waitForElementToAppear(commonPageElement.get("printableView"), 10);
        driver.click(commonPageElement.get("printableView"));
    }

    /**
     * Function to click the "clickPrintThisPageLink" link on the top right on any details page
     *
     * @return :
     * @throws Exception
     */
    public void clickPrintThisPageLink() throws Exception {
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        Integer winToSwitch = tabs.size() - 1;
        System.out.print(winToSwitch);
        driver.switchToWindow(tabs.get(winToSwitch));
        driver.waitForElementToAppear(commonPageElement.get("printThisPageLink"), 10);
        driver.click(commonPageElement.get("printThisPageLink"));
    }

    /**
     * Function to click the "clickPrintThisPageLink" link on the top right on any details page
     *
     * @return :
     * @throws Exception
     */
    public void switchBackToHomePage() throws Exception {
        driver.getWebDriver().switchTo().defaultContent();
        driver.getWebDriver().close();
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        Integer winToSwitch = tabs.size() - 1;
        System.out.print(winToSwitch);
        driver.switchToWindow(tabs.get(winToSwitch));
    }

    /**
     * Function to click on the link with the given field
     *
     * @param fieldName : label of the field
     * @return :
     * @throws Exception
     */
    public void clickLinkWithFiledValue(String fieldName) throws Exception {
        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        String name = (commonPageElement.get("fieldValue").get("XPATH")).replace("##replaceString##", fieldName);
        commonPageElement.get("fieldValue").put("XPATH", name);
        driver.click(commonPageElement.get("fieldValue"));
    }


    /**
     * Common Function to click on links at the top of the Account Details page
     * for e.g. Contact Info, Procedures Sales Account Team, Affiliation , Address , Quota etc
     */
    public void clickListLinks(String nameOfLink) throws Exception {
        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        String name = (commonPageElement.get("listLinks").get("XPATH")).replace("##replaceString##", nameOfLink);
        commonPageElement.get("listLinks").put("XPATH", name);
        driver.click(commonPageElement.get("listLinks"));
    }

    public boolean newInput() {
//        switchNextWindow();
        boolean isNewInputClicked = false;

        driver.waitForElementToAppear(commonPageElement.get("new"), 10);
        driver.click(commonPageElement.get("new"));
        isNewInputClicked = true;
        return isNewInputClicked;
    }

    /**
     * Common Function to click on Field Value of Link type by providing Field Name on all details page
     * for e.g. On Contact Details Page : field Account Name
     *
     * @param fieldName : Name of the label
     * @throws Exception :
     */
    public void clickFieldValueWithLink(String fieldName) throws Exception {
        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        String name = (commonPageElement.get("clickFieldValueWithLink").get("XPATH")).replace("##replaceString##", fieldName);
        commonPageElement.get("clickFieldValueWithLink").put("XPATH", name);
        driver.waitForElementToAppear(commonPageElement.get("clickFieldValueWithLink"), 10);
        driver.click(commonPageElement.get("clickFieldValueWithLink"));
//        String tempText = fieldName.toLowerCase();
//        if (tempText.contains("contact")|| tempText.contains("account") || tempText.contains("trainer") || tempText.contains("surgeon")){
//            try {
//            	driver.waitForElementToAppear(commonPageElement.get("pageDetailsTab"), 10);
//                driver.click(commonPageElement.get("pageDetailsTab"));
//            }catch (Exception e){}
//        }

    }

    /**
     * Method to return the content of confirmation message
     * @return
     */
    public String getConfirmationMessage(){
        driver.waitForElementToAppear(commonPageElement.get("confirmationMessage"),20);
        return driver.getText(commonPageElement.get("confirmationMessage"));
    }

    public String getErrorMessage(String type) throws Exception {
        HashMap<String, String> locator;
        switch (type) {
            case "TitleErrors":
                locator = commonPageElement.get("titleErrors");
                driver.waitForElementToAppear(locator, 10);
                break;
            default:
                throw new Exception("No such option available");
        }
        return driver.getText(locator);
    }

    /**
     * @returns true if go button is clicked
     */
    public boolean clickGoButton() {
        boolean isGoButtonClicked = false;
        try {

            driver.waitForElementToAppear(commonPageElement.get("goButton"), 10);
            driver.click(commonPageElement.get("goButton"));
            isGoButtonClicked = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isGoButtonClicked;
    }

    /**
     * Function to delete the duplicate affiliation ID and Address ID
     *
     * @param affiliationID : Affiliation ID
     * @return :
     * @throws Exception
     */
    public void clickDeleteDuplicateAffiliationID(String affiliationID) throws Exception {
        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        String name = (commonPageElement.get("affiliationID").get("XPATH")).replace("##replaceString##", affiliationID);
        commonPageElement.get("affiliationID").put("XPATH", name);
        driver.click(commonPageElement.get("affiliationID"));
    }

    /**
     * Function to click on any of the new button on the details page
     *
     * @param ButtonLable : Lable of the button which is to be clicked
     * @return :
     * @throws Exception
     */
    public void clickAnyNewButtonOnDetails(String ButtonLable) throws Exception {
        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        String name = (commonPageElement.get("createNewBtutton").get("XPATH")).replace("##replaceString##", ButtonLable);
        commonPageElement.get("createNewBtutton").put("XPATH", name);
        driver.click(commonPageElement.get("createNewBtutton"));
    }

    /**
     * Function to enter subject while adding a new open activity event
     *
     * @param subject : Subject to enter
     * @return :
     * @throws Exception
     */
    public void enterSubject(String subject) throws Exception {
        commonPageElement.get("subject");
        driver.setText(commonPageElement.get("subject"), subject);
    }

    /**
     * select Sales Step for creation of new event
     *
     * @param salesStep
     * @return
     */
    public boolean selectSalesStep(String salesStep) {
        boolean isSelectAccountType = false;
        try {
            driver.waitForElementToAppear(commonPageElement.get("salesStep"), 10);
            driver.selectItemByVisibleText(commonPageElement.get("salesStep"), salesStep);
            System.out.println("salesStep  " + salesStep);
            isSelectAccountType = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSelectAccountType;
    }

    /**
     * Method to click show more option to display remaining list of elements
     * @param title
     * @return
     */
    public boolean clickShowMoreOnDetailPage(String title) throws InterruptedException {
        commonPageElement.get("showMoreLink").put("XPATH",commonPageElement.get("showMoreLink").get("XPATH").replace("##replaceString##",title));
        if(driver.waitForElementToAppear(commonPageElement.get("showMoreLink"),10)) {
            driver.click(commonPageElement.get("showMoreLink"));
        }
        Thread.sleep(1500);
        return true;
    }

    public void deleteAllProceduresLinked() throws Exception {
        driver.waitForElementToAppear(commonPageElement.get("verifyPageDescription"), 10);
        List<WebElement> list = driver.findAll(commonPageElement.get("linkedProcedureDeleteButton"));
        for (int i = 1; i <= list.size(); i++) {
            this.clickListLinks("Procedures");
            driver.click(commonPageElement.get("procedureDeleteButtonOnTop"));
            this.alertOk();
            driver.waitForElementToAppear(commonPageElement.get("verifyPageDescription"), 10);
        }

    }

    /**
     * Function to click choose file button for PDF upload
     *
     * @return : void
     * @throws Exception
     */
    public void clickChooseFileForUpload() throws Exception {
        driver.waitForElementToAppear(commonPageElement.get("chooseFileButton"), 10);
        driver.click(commonPageElement.get("chooseFileButton"));
        Thread.sleep(2000);
    }

    /**
     * Function to click Attach File button for PDF upload
     *
     * @return : void
     * @throws Exception
     */
    public void clickAttachFileButton() throws Exception {
        driver.waitForElementToAppear(commonPageElement.get("attachButton"), 10);
        driver.click(commonPageElement.get("attachButton"));
    }

    /**
     * Function to click Done button for PDF upload
     *
     * @return : void
     * @throws Exception
     */
    public void clickDoneButton() throws Exception {
        driver.waitForElementToAppear(commonPageElement.get("done"), 10);
        driver.click(commonPageElement.get("done"));
    }

    /**
     * Function to get the attachment file name
     *
     * @param rowNum : Row number
     * @return : String
     * @throws Exception
     */
    public String getNotesAttachementNameByRowNumber(String rowNum) throws Exception {
        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        String name = (commonPageElement.get("notesAndAttachmentTitle").get("XPATH")).replace("##replaceString##", rowNum);
        commonPageElement.get("notesAndAttachmentTitle").put("XPATH", name);
        return driver.getText(commonPageElement.get("notesAndAttachmentTitle"));
    }

    /**
     * Function to click on any of the new button on the details page
     *
     * @param
     * @return boolean
     * @throws Exception
     */
    public boolean checkFirstCheckBox() throws Exception {
        boolean isFirstCheckBoxClicked = false;
        driver.waitForElementToAppear(commonPageElement.get("firstCheckBox"), 10);
        driver.click(commonPageElement.get("firstCheckBox"));
        isFirstCheckBoxClicked = true;
        return isFirstCheckBoxClicked;
    }

    /**
     * Function to enter user name and password for marking complete/Incomplete Traning Events
     *
     * @param AssignedTrainerDay1 : Name of the assigned trainer
     * @return : window handle of parent window
     * @throws Exception :
     */
    public String enterUserPassForStatusChange(String AssignedTrainerDay1, String User, String Password,
                                               int numOfattenders) throws Exception {

        String parentHandle = driver.getWindowHandle();
        int count = 3;
        do{
            Thread.sleep(4000);
            count--;
            if (count==0)
                throw new Exception("Mark Training Complete/Incomplete pop-up did not opened");
        }while (driver.getWindowHandles().size()==1);
        Thread.sleep(3000);
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        if (tabs.size() > 2) {
            tabs.remove(parentHandle);
            for (String handle : tabs) {
                driver.switchToWindow(handle);
                try {
                    String url = driver.getWebDriver().getCurrentUrl();
                    if (url.contains("EventRegistration"))
                        break;
                } catch (Exception e) {
                }
            }
        } else {
            Integer winToSwitch = tabs.size() - 1;
            driver.switchToWindow(tabs.get(winToSwitch));
        }
        Thread.sleep(1000);
        //enter user name
        driver.setText(commonPageElement.get("userNameForStatusChange"), User);
        //enter password
        driver.setText(commonPageElement.get("passwordForStatusChange"), Password);
        //select trainer
        for (int i = 1; i <= numOfattenders; i++) {
            selectTraniersForMarkCompIncom(Integer.toString(i), AssignedTrainerDay1);
        }
        //click submit button
        driver.click(commonPageElement.get("submitStatusChange"));
        Thread.sleep(2000);

        return parentHandle;
    }

    /**
     * Function to get the Event Registration status from the Lists , without actually going to the Event Registration page.
     *
     * @param rowNum : Row number
     * @return : String
     * @throws Exception
     */
    public String getEventRegistrationStatusByRowNumber(String rowNum) throws Exception {
        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        String name = (commonPageElement.get("notesAndAttachmentTitle").get("XPATH")).replace("##replaceString##", rowNum);
        commonPageElement.get("notesAndAttachmentTitle").put("XPATH", name);
        return driver.getText(commonPageElement.get("notesAndAttachmentTitle"));
    }

    /**
     * Function to click all the registrtion events on the traning event page
     *
     * @param
     * @return : boolean
     * @throws Exception
     */
    public boolean checkAllEventRegistrations() throws Exception {
        boolean isAllCheckBoxCheckedForEventRegistration = false;
        driver.waitForElementToAppear(commonPageElement.get("checkAllEventRegistrations"), 10);
        driver.click(commonPageElement.get("checkAllEventRegistrations"));
        isAllCheckBoxCheckedForEventRegistration = true;
        return isAllCheckBoxCheckedForEventRegistration;
    }

    /**
     * Function to get all the status and verify if its Complete
     *
     * @param
     * @return : boolean
     * @throws Exception
     */
    public boolean verifyAllStatusOfEventRegistration(String statusToVerify) throws Exception {
        driver.waitForElementToAppear(commonPageElement.get("verifyPageDescription"), 10);
        List<WebElement> list = driver.findAll(commonPageElement.get("getCountOfEventRegistrations"));
        for (int i = 1; i <= list.size(); i++) {
            this.clickListLinks("Event Registrations");
            String status = driver.getText(commonPageElement.get("getTopStatusOfEventRegistration"));
            if (!status.equals(statusToVerify)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Function to click cancel button while marking tRegistered event as complete or incomplete
     *
     * @return : void
     * @throws Exception
     */
    public void clickCancelButton() throws Exception {
        driver.waitForElementToAppear(commonPageElement.get("cancelButtonForMarkingTraningEvent"), 10);
        driver.click(commonPageElement.get("cancelButtonForMarkingTraningEvent"));
        // switch back to previous window
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        Integer winToSwitch = tabs.size() - 1;
        System.out.print(winToSwitch);
        driver.switchToWindow(tabs.get(winToSwitch));
    }

    /**
     * Method to click cancel button on edit page
     */
    public void clickCancelBtnOnEditPage(){
        driver.waitForElementToAppear(commonPageElement.get("cancelBtnOnEditPage"),10);
        driver.click(commonPageElement.get("cancelBtnOnEditPage"));
    }

    /**
     * Function to set any parameter string to the system's clipboard.
     *
     * @return : void
     * @throws Exception
     */
    public static void setClipboardData(String string) {
        StringSelection stringSelection = new StringSelection(string);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
    }

    /**
     * Function to download PDF
     *
     * @return : void
     * @throws Exception
     */
    public void downloadPDF() throws Exception {
        Robot robot = new Robot();

        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);

        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);

        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        Thread.sleep(1000);

        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);

        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);

        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);

        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        Thread.sleep(1000);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        Thread.sleep(1000);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        Thread.sleep(1000);
    }

    public void attachPDF() throws Exception {
        Robot robot = new Robot();
        Thread.sleep(1000);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_V);
        Thread.sleep(500);
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);

        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);

        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    /**
     * Function to set Trainers while marking mutiple registered events as complete or incomplete.
     *
     * @param : row num for which the trainer is to be set
     * @return : void
     * @throws Exception
     */
    public void selectTraniersForMarkCompIncom(String rowNum, String TrainerName) throws Exception {
        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        String name = (commonPageElement.get("trainerNameMArkComplete").get("XPATH")).replace("##replaceString##", rowNum);
        commonPageElement.get("trainerNameMArkComplete").put("XPATH", name);
        driver.setText(commonPageElement.get("trainerNameMArkComplete"), TrainerName);
    }

    /**
     * Function to Save for viewing 100 records at a time while global search
     *
     * @return : void
     * @throws Exception
     */
    public void clickSaveForViewing100Records() throws Exception {
        try {
            driver.waitForElementToAppear(commonPageElement.get("clickSaveForViewing100Records"), 5);
            driver.click(commonPageElement.get("clickSaveForViewing100Records"));
        } catch (Exception e) {

        }
    }


    /**
     * Function to delete All vendor payment number associated with SSA
     *
     * @throws Exception
     */
    public void deleteAllVendorPaymentName() throws Exception {
        driver.waitForElementToAppear(commonPageElement.get("verifyPageDescription"), 10);
        List<WebElement> list = driver.findAll(commonPageElement.get("linkedVendorPaymentDeleteButton"));
        for (int i = 1; i <= list.size(); i++) {
            this.clickListLinks("Vendor Payment Information");
            driver.click(commonPageElement.get("vendorPaymentDeleteButtonOnTop"));
            this.alertOk();
            driver.waitForElementToAppear(commonPageElement.get("verifyPageDescription"), 10);
        }

    }

    /**
     * Function to delete provided linked entity on the selected page
     *
     * @throws Exception
     */
    public void deleteAllLinkedEntity(String entityName) throws Exception {
        boolean flag = true;
        List<WebElement> list = null;
        commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        commonPageElement.get("linkedEntityDeleteButton").put("XPATH",
                (commonPageElement.get("linkedEntityDeleteButton").get("XPATH")).replace("##replaceString##", entityName));

        driver.waitForElementToAppear(commonPageElement.get("verifyPageDescription"), 10);
        try {
            list = driver.findAll(commonPageElement.get("linkedEntityDeleteButton"));
        } catch (Exception e) {
            flag = false;
        }
        if (flag) {
            for (int i = 1; i <= list.size(); i++) {
                this.clickListLinks(entityName);
                commonPageElement.get("entityDeleteButtonOnTop").put("XPATH",
                        (commonPageElement.get("entityDeleteButtonOnTop").get("XPATH")).replace("##replaceString##", entityName));
                driver.click(commonPageElement.get("entityDeleteButtonOnTop"));
                this.alertOk();
                driver.waitForElementToAppear(commonPageElement.get("verifyPageDescription"), 10);
            }
        }
    }

    /**
     * Function to click on any registration link
     *
     * @return : boolean
     * @throws Exception
     * @param: row number
     */
    public boolean clickOnAnyRegistrationLink(String rownum) throws Exception {
        boolean isclickOnAnyRegistrationLink = false;
        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        String locator = commonPageElement.get("eventRegistrationLink").get("XPATH").replace("##replaceString##", rownum);
        commonPageElement.get("eventRegistrationLink").put("XPATH", locator);
        driver.waitForElementToAppear(commonPageElement.get("eventRegistrationLink"), 10);
        driver.click(commonPageElement.get("eventRegistrationLink"));
        isclickOnAnyRegistrationLink = true;
        return isclickOnAnyRegistrationLink;
    }

    /**
     * Function to go to the social accounts settings tab with system admin login.
     *
     * @return : boolean
     * @throws Exception
     * @param:
     */
    public boolean goToSocialAccountSettingsTab() {
        boolean isgoToSocialAccountSettingsTab = false;

        driver.waitForElementToAppear(commonPageElement.get("setup"), 10);
        driver.click(commonPageElement.get("setup"));
        driver.waitForElementToAppear(commonPageElement.get("customize"), 10);
        driver.click(commonPageElement.get("customize"));
        driver.waitForElementToAppear(commonPageElement.get("socialMedia"), 10);
        driver.click(commonPageElement.get("socialMedia"));
        driver.waitForElementToAppear(commonPageElement.get("socialAccountsContactsLeads"), 10);
        driver.click(commonPageElement.get("socialAccountsContactsLeads"));
        driver.waitForElementToAppear(commonPageElement.get("saveSettingsForSocial"), 10);
        driver.click(commonPageElement.get("saveSettingsForSocial"));
        isgoToSocialAccountSettingsTab = true;
        return isgoToSocialAccountSettingsTab;
    }

    /**
     * Function to Verify that under the Contact Name the following Social Media buttons are not available: LinkedIn, Twitter, Facebook, Klout, YouTube.
     *
     * @return : boolean
     * @throws Exception
     * @param:
     */
    public boolean verifySocialMediaButtons() {
        boolean isverifySocialMediaButtons = true;
        try {
            driver.find(commonPageElement.get("facebookIcon"));
            isverifySocialMediaButtons = false;
        } catch (Exception e) {
            System.out.print("Facebook Icon not present");
        }
        try {
            driver.find(commonPageElement.get("twitterIcon"));
            isverifySocialMediaButtons = false;
        } catch (Exception e) {
            System.out.print("Twitter Icon not present");
        }
        try {
            driver.find(commonPageElement.get("kloutIcon"));
            isverifySocialMediaButtons = false;
        } catch (Exception e) {
            System.out.print("Klout Icon not present");
        }
        try {
            driver.find(commonPageElement.get("youtubeIcon"));
            isverifySocialMediaButtons = false;
        } catch (Exception e) {
            System.out.print("Youtube Icon not present");
        }

        return isverifySocialMediaButtons;
    }

    /**
     * method to find whether search entity is present in webpage or not
     *
     * @param entityName
     * @return
     */
    public boolean isEntityPresentOnGlobalSearch(String entityName) throws Exception {
        boolean isPresent = true;
        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        String locator = commonPageElement.get("entityOnGlobalSearch").get("XPATH").replace("##replaceString##", entityName);
        commonPageElement.get("entityOnGlobalSearch").put("XPATH", locator);
        String entityCount = driver.find(commonPageElement.get("entityOnGlobalSearch")).getText();
        if (entityCount.contains("0"))
            isPresent = false;
        return isPresent;
    }

    /**
     * Function to click Edit button
     *
     * @return : void
     * @throws Exception
     */
    public void clickOnEditButton() throws Exception {
        driver.waitForElementToAppear(commonPageElement.get("editButton"), 10);
        driver.click(commonPageElement.get("editButton"));
    }

    /**
     * Function to verify status of check box on Details page of any entity
     *
     * @param fieldName : Name of the field
     * @return : boolean
     * @throws Exception :
     */
    public boolean getStatusOfCheckBoxOnDetailsPage(String fieldName) throws Exception {
        boolean flag = false;
        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        String locator = commonPageElement.get("checkBoxOnDetailsPage").get("XPATH").
                replace("##replaceString##", fieldName);
        commonPageElement.get("checkBoxOnDetailsPage").put("XPATH", locator);
        driver.waitForElementToAppear(commonPageElement.get("checkBoxOnDetailsPage"),60);
        String status = driver.find(commonPageElement.get("checkBoxOnDetailsPage")).getAttribute("title");
            if (status.equals("Checked"))
                flag = true;
        return flag;
    }

    /**
     * Function to wait for loading image to disappear
     *
     * @param timeOutForAppear    : timeout in seconds
     * @param timeOutForDisappear : timeout in seconds
     */
    public void waitForLoadingImageToDisappear(int timeOutForAppear, int timeOutForDisappear) throws Exception {
        try {
            new WebDriverWait(driver.getWebDriver(), timeOutForAppear).until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//img[@class='loadingGif']")));
        } catch (Exception e) {
        }
        System.out.println("loading spinner appeared");
        try {

            new WebDriverWait(driver.getWebDriver(), timeOutForDisappear).until(
                    ExpectedConditions.invisibilityOfElementLocated(By.xpath("//img[@class='loadingGif']")));
        } catch (Exception e) {

        }
        System.out.println("loading spinner disappeared");
    }

    /**
     * Function to verify field is editable by Checking and then unChecking it
     *
     * @return boolean
     * @param: status1 : previous status
     * @param: status2 : new status
     */
    public boolean verifyFiledIsEditable(String status1, String status2) throws Exception {
        boolean isEditable = true;
        if (status1.equals(status2))
            isEditable = false;
        return isEditable;
    }

    /**
     * Function to click check box for ISI or NON ISI trained
     *
     * @return : void
     * @throws Exception
     * @param: filedName
     */
    public void clickCheckBoxOnEditPage(String filedName) throws Exception {
        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        String locator = commonPageElement.get("isEditableFieldCheckboxOnDetailsPage").get("XPATH").replace("##replaceString##", filedName);
        commonPageElement.get("isEditableFieldCheckboxOnDetailsPage").put("XPATH", locator);
        driver.waitForElementToAppear(commonPageElement.get("isEditableFieldCheckboxOnDetailsPage"), 10);
        driver.click(commonPageElement.get("isEditableFieldCheckboxOnDetailsPage"));
    }

    /**
     * Function to comp[are two strings n return false if same
     *
     * @return boolean
     * @param: s1 : string1
     * @param: s2 : string2
     */
    public boolean compareStrings(String s1, String s2) throws Exception {
        boolean isEditable = true;
        if (s1.equals(s2))
            isEditable = false;
        return isEditable;
    }

    /**
     * Function to to check if input field is displayed on the page
     */
    public boolean isInputFieldDispalyed(String fieldName) throws Exception {
        boolean isFieldDisplayed = false;

        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        String locator = commonPageElement.get("inputFieldsOnPage").get("XPATH").replace("##replaceString##", fieldName);
        commonPageElement.get("inputFieldsOnPage").put("XPATH", locator);

        isFieldDisplayed = driver.isElementDisplayed(commonPageElement.get("inputFieldsOnPage"));


        return isFieldDisplayed;
    }

    /**
     * Function to check if the input boxis editable or not
     */
    public boolean isInputFieldEditable(String fieldName) throws Exception {
        boolean isFieldDisplayed = false;

        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        String locator = commonPageElement.get("inputFieldsOnPage").get("XPATH").replace("##replaceString##", fieldName);
        commonPageElement.get("inputFieldsOnPage").put("XPATH", locator);

        isFieldDisplayed = driver.isElementEnabled(commonPageElement.get("inputFieldsOnPage"));

        return isFieldDisplayed;
    }

    /**
     * Function to click on the link on specific row on hover link
     */
    public boolean clickOnlinkOnHoverTable(String linkName) throws Exception {
        boolean isClicked = false;
        driver.switchToFrame("RLPanelFrame");
        List<WebElement> rows = driver.findAll(commonPageElement.get("hoverLinksDataRow"));

        for (int i = 1; i <= rows.size(); i++) {
            this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
            String locator = commonPageElement.get("linkOnHoverTable").get("XPATH").replace("##replaceString##", linkName);
            commonPageElement.get("linkOnHoverTable").put("XPATH", locator);

            if (driver.find(commonPageElement.get("linkOnHoverTable")) != null) {
                driver.click(commonPageElement.get("linkOnHoverTable"));
                isClicked = true;
                break;
            }
        }
        driver.switchToDefaultContent();

        return isClicked;
    }

    /**
     * Function to get current sorted order of given column Name
     * @param columnName : name of the column
     * @return : String - ASCENDING/DESCENDING/NOT_SORTED
     * @throws Exception :
     */
    public String getColumnSortedStatus(String columnName) throws Exception{

        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        String locator = commonPageElement.get("columnCurrentStatus").get("XPATH").replace("##replaceString##", columnName);
        commonPageElement.get("columnCurrentStatus").put("XPATH", locator);
        String attributeValue = null;
        String sortedStatus = null;
        try {
            attributeValue = driver.find(commonPageElement.get("columnCurrentStatus")).getAttribute("class");
            if (attributeValue.contains("DESC"))
                sortedStatus = "DESCENDING";
            else if (attributeValue.contains("ASC"))
                sortedStatus = "ASCENDING";
        } catch (Exception e){
            sortedStatus = "NOT_SORTED";
        }
        return sortedStatus;
    }

    /**
     * Function to click on letters links
     * @param letter : letter to click
     * @throws Exception :
     */
    public void clickOnAlphabetLink(String letter) throws Exception{
        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        String locator = commonPageElement.get("alphabetLinks").get("XPATH").replace("##replaceString##", letter);
        commonPageElement.get("alphabetLinks").put("XPATH", locator);
        driver.click(commonPageElement.get("alphabetLinks"));
        Thread.sleep(3000);
    }

    /**
     * Function to check presence of any text in the table displayed after selecting value from View drop-list
     * @param text : String
     * @return : Boolean
     * @throws Exception :
     */
    public boolean checkPresenceOfTextInTable(String text) throws Exception{
        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        String locator = commonPageElement.get("checkTextPresenceInTable").get("XPATH").replace("##replaceString##", text);
        commonPageElement.get("checkTextPresenceInTable").put("XPATH", locator);
        List<WebElement> lst = driver.findAll(commonPageElement.get("checkTextPresenceInTable"));
        if(lst.size()>0)
            return true;
        else
            return false;
    }

    public void scrollPageToPerticularSection(String sectionName)throws Exception{
        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        String locator = commonPageElement.get("pageSection").get("XPATH").replace("##replaceString##", sectionName);
        commonPageElement.get("pageSection").put("XPATH", locator);
        driver.waitForElementToAppear(commonPageElement.get("pageSection"),20);
        Thread.sleep(2000);
        driver.scrollToElement(commonPageElement.get("pageSection"));
    }

    /**
     * Function to select any CheckBox on Details page
     * Eg. Generate Certification on Training Record details page
     */
    public void clickAnyCheckBoxOnDetailsPage(String fieldName) throws Exception {
        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        String locator = commonPageElement.get("selectCheckBoxOnDetailsPage").get("XPATH").replace("##replaceString##", fieldName);
        commonPageElement.get("selectCheckBoxOnDetailsPage").put("XPATH", locator);
        WebElement checkBoxField = driver.find(commonPageElement.get("selectCheckBoxOnDetailsPage"));
        driver.doubleClick(commonPageElement.get("selectCheckBoxOnDetailsPage"));
        checkBoxField.findElement(By.xpath(".//input[@type='checkbox']")).click();
        driver.click(commonPageElement.get("selectCheckBoxOnDetailsPage"));
        clickSaveBtnOnEditPage();
        Thread.sleep(3000);
    }

    public boolean isTextPresentOnPage(String text) throws Exception
    {
        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        String locator = commonPageElement.get("anyText").get("XPATH").replace("##replaceString##", text);
        commonPageElement.get("anyText").put("XPATH", locator);
        driver.waitForElementToAppear(commonPageElement.get("anyText"), 5);
        String data =driver.getText(commonPageElement.get("anyText"));
        if (data.equalsIgnoreCase(text))
            return true;
        else
        return false;


    }

      /**
     * Method to sort on the basis  column name by clicking
     * @param columnName : name of the column header
     */
    public void clickOnColumnHeader(String columnName) throws Exception{
        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        String name = (commonPageElement.get("clickColumnHeader").get("XPATH")).replace("##replaceString##", columnName);
        commonPageElement.get("clickColumnHeader").put("XPATH", name);
        driver.waitForElementToAppear(commonPageElement.get("clickColumnHeader"), 10);
        driver.click(commonPageElement.get("clickColumnHeader"));
       Thread.sleep(5000);
    }

    /**
     * Method to sort on the basis  column name by clicking
     * @param id : Id of the item e.g PlanId (for Plan) , TRE (for Training event) , RN (for event Registration) ,
     *           Name(for Contact) , External ID( for Account )etc.
     */
    public String clickDelBtnInViewResultTable(String id) throws Exception{
        String clickedLink = null;
        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        String locator = (commonPageElement.get("nameInTable").get("XPATH")).replace("##replaceString##", id);
        commonPageElement.get("nameInTable").put("XPATH", locator);
        do {
            List<WebElement> nameList = driver.findAll(commonPageElement.get("nameInTable"));
            if (nameList.size() > 0) {
                WebElement ele = driver.find(commonPageElement.get("nameInTable"));
                clickedLink = ele.getText();
                ((JavascriptExecutor) driver.getWebDriver()).executeScript("arguments[0].scrollIntoView(true);", ele);
                Thread.sleep(1000);
                WebElement deleteLink = ele.findElement(
                        By.xpath("//span[text()='"+id+"']/../../../preceding-sibling::td//span[text()='Del']"));
                deleteLink.click();
                alertOk();
                break;
            } else {
                driver.click(commonPageElement.get("nextInTable"));
                Thread.sleep(1000);
            }
        } while (driver.isElementDisplayed(commonPageElement.get("allListinTable")));
        return clickedLink;
    }

    /**
     * Method to tick the checkbox from particular row in any section using unique row value
     * @param rowValue
     * @return
     * @throws Exception
     */
    public boolean tickCheckBoxFromSection(String rowValue) throws Exception {
        this.commonPageElement = new ObjectRepositoryLoader().getObjectRepository("web:commonPageRepo.xml");
        String path = commonPageElement.get("tickCheckBoxFromSection").get("XPATH").replace("##replaceString##",rowValue);
        commonPageElement.get("tickCheckBoxFromSection").put("XPATH",path);
        driver.waitForElementToAppear(commonPageElement.get("tickCheckBoxFromSection"),10);
        driver.click(commonPageElement.get("tickCheckBoxFromSection"));
        return true;
    }

    /**
     * Mehtod to verify substring in complete chatter feed
     * @param content
     * @return
     */
    public boolean verifyContentInChatterFeed(String content){
        driver.waitForElementToAppear(commonPageElement.get("chatterFeedContent"),10);
        return driver.getText(commonPageElement.get("chatterFeedContent")).contains(content);
    }
}
