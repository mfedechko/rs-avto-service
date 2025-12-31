package com.rsavto.categories.site;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.rsavto.categories.data.PartPage;
import com.rsavto.categories.service.FilesService;
import com.rsavto.categories.web.site.MainPage;
import lombok.Setter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Mykola Fedechko
 */
@Service
public class RsAvtoWebSiteService {

    private static final Logger LOG = LoggerFactory.getLogger(RsAvtoWebSiteService.class);

    private final FilesService filesService;

    public RsAvtoWebSiteService(final FilesService filesService) {
//        System.setProperty("webdriver.chrome.driver", FileUtils.buildChromeDriverPath(webdriverFolder));
        this.filesService = filesService;
    }

    public Map<String, String> findArticleLinks(final List<String> articles) {
        final var articleLinks = new HashMap<String, String>();
        final var driver = getHeadlessChrome();
        driver.get(MainPage.URL);
        final var homePage = PageFactory.initElements(driver, MainPage.class);
        final var searchResults = homePage.findByArticle("start");
        for (final var article : articles) {
            LOG.info("Retrieving URL for " + article);
            searchResults.findByArticle(article);
            final var link = searchResults.getPartCategoryLink();
            LOG.info("URL is: " + link);
            articleLinks.put(article, link);
        }

        return articleLinks;
    }

    public Map<String, PartPage> getLinks(final List<String> articles) throws IOException {
        final var articleLinks = new HashMap<String, PartPage>();
        final var webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(false);
        var i = 0;
        for (final var article : articles) {
            i++;
            LOG.info("Retrieving URL for " + article);
            final var url = String.format("https://rsavto.com.ua/search-%s/", article);
            final HtmlPage page;
            try {
                page = webClient.getPage(url);
            } catch (final Exception exception) {
                LOG.error("Exceprion thrown for article " + article);
                articleLinks.put(article, new PartPage("", ""));
                continue;
            }

            final var links = page.getByXPath("//td/a[contains(text(), 'відкрити')]");
            var pageLink = "";
            var imageLink = "";
            if (links.size() > 0) {
                pageLink = MainPage.URL + ((HtmlAnchor) links.get(0)).getAttribute("href");
                final HtmlPage partPage = webClient.getPage(pageLink);
                final var images = partPage.getByXPath("//div[@id='image_big']/a");
                if (images.size() > 0) {
                    imageLink = MainPage.URL + ((HtmlAnchor) images.get(0)).getAttribute("href");
                }
            }
            final var partPage = new PartPage(pageLink, imageLink);
            articleLinks.put(article, partPage);
            LOG.info("URL is: " + pageLink);
        }

        return articleLinks;
    }

    private static WebDriver getHeadlessChrome() {
        final var options = new ChromeOptions();
        options.addArguments("headless");
        options.addArguments("disable-gpu");
        final WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return driver;
    }
}
