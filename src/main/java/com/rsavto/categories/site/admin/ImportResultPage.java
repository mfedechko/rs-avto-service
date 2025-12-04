package com.rsavto.categories.site.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author Mykola Fedechko
 */
public class ImportResultPage{

    @FindBy(xpath = "//div[@class='content']/div[last()]")
    private WebElement content;

    private final WebDriver driver;

    public ImportResultPage(final WebDriver driver) {
        this.driver = driver;
    }

    public int getPartsQuantity() {
        final var contentText = content.getText();
        final var startIndex = contentText.indexOf("добавлено") + 9;
        final var endIndex = contentText.indexOf("запч");
        final var partsQuantityStr = contentText.substring(startIndex, endIndex).trim();
        return Integer.parseInt(partsQuantityStr);
    }


}
