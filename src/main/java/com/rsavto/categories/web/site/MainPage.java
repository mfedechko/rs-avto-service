package com.rsavto.categories.web.site;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * @author Mykola Fedechko
 */
public class MainPage {

    public static final String URL = "https://rsavto.com.ua";

    @FindBy(name = "search")
    private WebElement searchBox;

    private final WebDriver driver;

    public MainPage(final WebDriver driver) {
        this.driver = driver;
    }

    public SearchResultsPage findByArticle(final String article) {
        searchBox.sendKeys(article);
        searchBox.submit();
        return PageFactory.initElements(driver, SearchResultsPage.class);
    }

}
