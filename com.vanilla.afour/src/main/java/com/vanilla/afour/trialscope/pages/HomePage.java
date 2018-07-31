package com.vanilla.afour.trialscope.pages;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.vanilla.afour.afour.autolib.util.LogLevel;
import com.vanilla.afour.afour.autolib.util.ObjectRepositoryLoader;

/**
 * Methods are used to work on home page functionality of PharmaCM
 * @author AfourTech
 */
public class HomePage extends PharmaCMBasePage
{
    private static final String SEARCHBOX = "SearchBox";
    private static final String STUDYID = "StudySearchResultsByID";
    private static final String STUDYTITLE = "StudySearchResultsByTitle";
    HashMap<String, HashMap<String, String>> homePageElement = null;

    public HashMap<String, String> config = null;

    public HomePage() throws Exception
    {
        super();
        config = driver.getConfigData();
        this.homePageElement = new ObjectRepositoryLoader().getObjectRepository("web:HomePage.xml");
    }

    /**
     * Click into study search box
     */
    public void clickStudySearchBox()
    {
        driver.click(homePageElement.get("SearchBox"));
    }

    /**
     * Enter study id/study title in the text box
     * 
     * @param text text to be entered in text box
     * @param textBoxName Name of the text box in application
     */
    public void enterTextIntoTextBox(String text, String textBoxName)
    {
        switch(textBoxName)
        {
            case SEARCHBOX:
                driver.setText(homePageElement.get(SEARCHBOX), text);
                break;

            default:
                Assert.fail("Text Box Name Name doesn't exist");
                break;
        }
        report.log("User has entered into the text box",LogLevel.INFO);
    }

    /**
     * Click on study search box button
     * 
     * @throws InterruptedException
     */
    public void clickStudySearchButton() throws InterruptedException
    {

        driver.click(homePageElement.get("StudySearchButton"));
        driver.waitForElementExplicitly(homePageElement.get("CancelStudySearchButton"));
        report.log("Study search button is clicked",LogLevel.INFO);
    }

    /**
     * Click on study search cancel button
     */
    public void clickCancelStudySearchButton()
    {
        driver.click(homePageElement.get("CancelStudySearchButton"));
        report.log("Cancel Study Search button is clicked",LogLevel.INFO);
    }

    /**
     * Method to verify whether study is present in the study search results
     * grid
     * 
     * @param study study id/study title
     * @param studyAttribute search study by ID/Title
     * @return true if study is found in the search results grid else false
     */
    public boolean isStudyFoundInStudySearchResults(String study, String studyAttribute)
    {
        List<WebElement> studySearchResultsElements = null;
        switch(studyAttribute)
        {
            case STUDYID:
                studySearchResultsElements = driver.findAll(homePageElement.get(STUDYID));
                break;

            case STUDYTITLE:
                studySearchResultsElements = driver.findAll(homePageElement.get(STUDYTITLE));
                break;

            default:
                Assert.fail("Study Title/ID doesn't exist");
                break;
        }
        if(!studySearchResultsElements.isEmpty())
        {
            for(WebElement allStudySearchResult : studySearchResultsElements)
            {
                if(allStudySearchResult.getText().equals(study))
                {
                    return true;
                }
            }
        }
        return false;
    }
}