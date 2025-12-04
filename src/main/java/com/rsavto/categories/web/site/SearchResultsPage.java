package com.rsavto.categories.web.site;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

/**
 * @author Mykola Fedechko
 */
public class SearchResultsPage {

    @FindBy(name = "search")
    private WebElement searchBox;

    @FindBy(xpath = "//table[@id = 'table_sorted']/tbody/tr")
    private List<WebElement> resultsRows;

    private final WebDriver driver;

    public SearchResultsPage(final WebDriver driver) {
        this.driver = driver;
    }

    public SearchResultsPage findByArticle(final String article) {
        searchBox.sendKeys(article);
        searchBox.submit();
        return PageFactory.initElements(driver, SearchResultsPage.class);
    }

    public String getPartCategoryLink() {
        final var links = resultsRows.stream()
                .map(tr -> driver.findElements(By.xpath("//td/a[contains(text(), 'открыть')]")))
                .findFirst()
                .orElse(List.of());

        if (links.size() == 0) {
            return "";
        }
        return links.get(0).getAttribute("href");
    }

}
