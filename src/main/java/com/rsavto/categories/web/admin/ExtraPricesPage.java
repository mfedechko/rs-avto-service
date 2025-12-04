package com.rsavto.categories.web.admin;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * @author Mykola Fedechko
 */
public class ExtraPricesPage {

    @FindBy(xpath = "//div[@id='postav_box_all']/table[2]/tbody/tr")
    private List<WebElement> extraPricesTemplates;

}
