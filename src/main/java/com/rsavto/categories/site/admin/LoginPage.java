package com.rsavto.categories.site.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * @author Mykola Fedechko
 */
public class LoginPage {

    private static final String RSAVTO_LOGIN = "anton";
    private static final String RSAVTO_PASSWORD = "Superior12";

    private static final String FORTUNA_LOGIN = "fortuna";
    private static final String FORTUNA_PASSWORD = "fortuna2402442";

    @FindBy(id = "email")
    private WebElement loginField;
    @FindBy(id = "password")
    private WebElement passwordField;
    @FindBy(id = "send")
    private WebElement loginButton;

    private final WebDriver driver;

    public LoginPage(final WebDriver driver) {
        this.driver = driver;
    }

    public AdminHomePage loginToRsAvto() {
        loginField.sendKeys(RSAVTO_LOGIN);
        passwordField.sendKeys(RSAVTO_PASSWORD);
        loginButton.click();
        return PageFactory.initElements(driver, AdminHomePage.class);
    }

    public AdminHomePage loginToFortuna() {
        loginField.sendKeys(FORTUNA_LOGIN);
        passwordField.sendKeys(FORTUNA_PASSWORD);
        loginButton.click();
        return PageFactory.initElements(driver, AdminHomePage.class);
    }

}
