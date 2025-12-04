package com.rsavto.categories.web.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * @author Mykola Fedechko
 */
public class OrdersPage {

    @FindBy(xpath = "//table[@cellpadding='3']/tbody/tr")
    private List<WebElement> allRecords;

    private final WebDriver driver;

    public OrdersPage(final WebDriver driver) {
        this.driver = driver;
    }

}
