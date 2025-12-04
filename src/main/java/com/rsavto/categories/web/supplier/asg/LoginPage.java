package com.rsavto.categories.web.supplier.asg;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * @author Mykola Fedechko
 */
public class LoginPage {

    @FindBy(name = "login")
    private WebElement loginField;
    @FindBy(id = "password")
    private WebElement passwordField;
    @FindBy(xpath = "//form[@class='form-login']/button")
    private WebElement loginButton;

    private final WebDriver driver;

    public LoginPage(final WebDriver driver) {
        this.driver = driver;
    }

    public MainPage login() {
        loginField.sendKeys("650");
        passwordField.sendKeys("фортуна");
        loginButton.click();
        return PageFactory.initElements(driver, MainPage.class);
    }

}
