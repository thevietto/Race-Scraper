package ru.ignatyev.pages;


import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import ru.ignatyev.dto.RaceResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class RaceResultsPage {

    private final WebDriver driver;
    private final FluentWait<WebDriver> wait;

    @FindBy(css = "#mainForm\\3a bg-header > div > div:nth-child(1) > div.col-lg-4.col-md-12.col-sm-6.col-xs-12 > h1")
    private WebElement raceName;

    @FindBy(css = "#loader > img")
    private WebElement loader;

    @FindBy(css = "#mainForm\\3a athlete_extra_info_holder > p:nth-child(1)")
    private WebElement raceDate;

    @FindBy(css = "#mainForm\\3a pageNav > div > ul")
    private WebElement pagination;

    @FindBy(css = "#mainForm\\3a dataTable_data")
    private WebElement resultTable;

    @FindBy(css = "#mainForm\\3a pageNav > div > p")
    private WebElement pageCounter;

    @FindBy(css = "#mainForm\\3a pageNav > div > ul > li:nth-last-child(2) > a")
    private WebElement nextPage;

    public RaceResultsPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        wait = new FluentWait<>(driver)
                .withTimeout(60, TimeUnit.SECONDS)
                .pollingEvery(3, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }

    public String openAndGetRaceName(String url) {
        driver.get(url);
        waitFor(raceName);
        return raceName.getText();
    }

    public String getRaceDate() {
        waitFor(raceDate);
        return raceDate.getText().split("•")[0];
    }

    public int getPagesCount() {
        waitFor(pageCounter);
        String[] split = pageCounter.getText().split(" ");
        return Integer.valueOf(split[split.length - 1]);
    }

    public void goToPage(int page) {
        waitFor(pagination);
        pagination.findElements(By.cssSelector("li")).get(page + 1).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#loader > img")));
    }

    public void nextPage() {
        waitFor(nextPage);
        nextPage.click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#loader > img")));
    }

    public List<RaceResult> getResults(String raceId, String raceName, String raceDate) {
        waitFor(resultTable);

        int size = resultTable.findElements(By.cssSelector("tr")).size();
        List<RaceResult> results = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int index = i;
            List<String> tds = wait.until((ExpectedCondition<List<String>>) input -> driver.findElements(
                    By.cssSelector("#mainForm\\3a dataTable_data tr:nth-child(" + (index + 1) + ") td"))
                    .stream().skip(2)
                    .map(WebElement::getText).collect(Collectors.toList()));
            RaceResult result = new RaceResult();
            result.setRaceId(raceId);
            result.setRaceName(raceName);
            result.setDate(raceDate.trim());
            result.setBibNumber(tds.get(0));
            result.setName(tds.get(1));
            result.setCategory(tds.get(2));
            result.setRank(tds.get(3));
            result.setCategoryPlace(tds.get(4));
            result.setGenderPlace(tds.get(5));
            result.setSwim(tds.get(6));
            result.setBike(tds.get(7));
            result.setRun(tds.get(8));
            result.setFinish(tds.get(9));
            results.add(result);
        }
        return results;
    }

    private void waitFor(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }
}
