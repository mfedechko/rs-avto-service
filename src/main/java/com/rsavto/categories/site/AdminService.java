package com.rsavto.categories.site;

import com.rsavto.categories.data.Category;
import com.rsavto.categories.service.DataService;
import com.rsavto.categories.service.FilesService;
import com.rsavto.categories.site.admin.AdminHomePage;
import com.rsavto.categories.site.admin.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Mykola Fedechko
 */
@Service
public class AdminService {

    private static final Logger LOG = LoggerFactory.getLogger(AdminService.class);

    private final static String RSAVTO_LOGIN_URL = "https://rsavto.com.ua/adm";
    private final static String RSAVTO_IMPORT_CATEGORIES_URL = "https://rsavto.com.ua/adm/?m=shop&type=catalog&vb=import";

    private final static String FORTUNA_LOGIN_URL = "https://fortunaavto.com.ua/adm";
    private final static String FORTUNA_IMPORT_URL = "https://fortunaavto.com.ua/adm/?m=shop&type=data";
    private final FilesService filesService;
    private final DataService dataService;

    public AdminService(final FilesService filesService,
                        final DataService dataService) {
        this.filesService = filesService;
        this.dataService = dataService;
        final var webDriverPath = filesService.getWebDriverPath();
        System.setProperty("webdriver.chrome.driver", webDriverPath);
    }

    public void uploadCategories(final List<Category> categories) {
        final var driver = getHeadlessChrome();
        final var homePage = login(driver);
        final var catalogPage = homePage.openCatalog();
        final var cataloguesPage = catalogPage.openCataloguesPage();
        final var importCategoriesPage = cataloguesPage.openImport();
        for (final var category : categories) {

            final var categoryFile = filesService.getCategoryFileName(category).toFile();
            if (!categoryFile.exists()) {
                LOG.warn("No file for " + category);
                continue;
            }

            if (category == Category.UNKNOWN && categoryFile.exists()) {
                LOG.warn("Unknown category has been found");
                continue;
            }

            LOG.info("Start uploading " + category.getFileName());
            importCategoriesPage.submitCategory(categoryFile.getPath(), String.valueOf(category.getId()));
            wait(10000);
            LOG.info("Category " + category.getFileName() + " has been successfully uploaded.");
            driver.get(RSAVTO_IMPORT_CATEGORIES_URL);
        }
        LOG.info("All categories have been uploaded.");
        driver.quit();
    }

    public double getRate() {
        final var driver = getHeadlessChrome();
        final var adminHomePage = login(driver);
        final var catalogPage = adminHomePage.openCatalog();
        final var additionalPage = catalogPage.openAdditional();
        return additionalPage.getEurRate();
    }

    private static WebDriver getHeadlessChrome() {
        final var options = new ChromeOptions();
        options.addArguments("headless");
        options.addArguments("disable-gpu");
        final WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return driver;
    }

    private  AdminHomePage login(final WebDriver driver) {
        driver.get(RSAVTO_LOGIN_URL);
        final var loginPage = PageFactory.initElements(driver, LoginPage.class);
        final var homePage = loginPage.loginToRsAvto();
        wait(dataService.getWaitForCategoryInSec() * 1000);
        return homePage;
    }

    private static void wait(final int mills) {
        try {
            Thread.sleep(mills);
        } catch (final InterruptedException exc) {
            throw new RuntimeException("While waiting", exc);
        }
    }
}
