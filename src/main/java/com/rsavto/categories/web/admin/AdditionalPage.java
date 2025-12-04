package com.rsavto.categories.web.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author Mykola Fedechko
 */
public class AdditionalPage {

    @FindBy(id = "tabH4")
    private WebElement currenciesTab;

    @FindBy(xpath = "//td/input[@value='EUR']/../..//input[@name='cena']")
    private WebElement currentEurRate;

    @FindBy(xpath = "//td/input[@value='EUR']/../../td[5]/input")
    private WebElement saveEurRateButton;

    private final WebDriver driver;

    public AdditionalPage(final WebDriver driver) {
        this.driver = driver;
    }

    public double getEurRate() {
        final var rateStr = currentEurRate.getAttribute("value");
        return Double.parseDouble(rateStr);
    }

    public void changeEurRate(final double rate) {
        currenciesTab.click();
        setNewEurRate(rate);
        saveEurRate();
    }

    private void saveEurRate() {
        saveEurRateButton.click();
        driver.switchTo().alert().accept();
    }

    private void setNewEurRate(final double rate) {
        currentEurRate.clear();
        currentEurRate.sendKeys(String.valueOf(rate));
    }

}
