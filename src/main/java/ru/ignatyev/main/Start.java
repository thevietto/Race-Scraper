package ru.ignatyev.main;

import org.openqa.selenium.WebDriver;
import ru.ignatyev.dto.RaceResult;
import ru.ignatyev.pages.RaceResultsPage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Start {

    private static Map<String, String> PARAMS = new HashMap<String, String>() {{
        put("default_url", "https://www.sportstats.ca/display-results.xhtml");
        put("race_ids", "");
        put("out_file", "sportstatsscraperoutput.csv");
    }};
    private static ExecutorService executor;
    private static Lock lock = new ReentrantLock(true);
    private static PrintWriter fileWriter;

    public static void main(String[] args) {
        prepareParams(args);
        initFileWriter();
        WebDriverFactory driverFactory = new WebDriverFactory();
        WebDriver driver = driverFactory.createPhantomJSWebDriver();
        executor = Executors.newSingleThreadExecutor();
        try {
            processRaces(driver);
        } finally {
            driver.quit();
            executor.shutdown();
            fileWriter.close();
        }

    }

    private static void processRaces(WebDriver driver) {
        String[] raceIds = PARAMS.get("race_ids").split(",");
        List<Future<Integer>> saveJobs = new ArrayList<>();

        for (String raceId : raceIds) {
            System.out.println("Processing race " + raceId);

            String url = PARAMS.get("default_url") + "?raceid=" + raceId;
            RaceResultsPage page = new RaceResultsPage(driver);
            String raceName = page.openAndGetRaceName(url);
            System.out.println("Race name: " + raceName);
            String raceDate = page.getRaceDate();

            int pagesCount = page.getPagesCount();
            for (int i = 1; i < pagesCount + 1; i++) {
                System.out.println("Page " + i + " of " + pagesCount);
                if (i > 1) {
                    page.nextPage();
                }
                List<RaceResult> results = page.getResults(raceId, raceName, raceDate);
                saveJobs.add(executor.submit(() -> saveResults(results)));
            }

        }

        System.out.println("Waiting for saving results process...");
        int sum = saveJobs.stream().mapToInt((integerFuture) -> {
            try {
                return integerFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                return 0;
            }
        }).sum();
        System.out.println(sum + " results successfully processed.");
    }

    private static int saveResults(List<RaceResult> results) {
        lock.lock();
        try {
            int count = 0;
            for (RaceResult result : results) {
                fileWriter.println(result);
                count++;
            }
            fileWriter.flush();
            return count;
        } finally {
            lock.unlock();
        }
    }

    private static void prepareParams(String[] args) {
        for (String arg : args) {
            String[] split = arg.split("=");
            if (split.length != 2) {
                System.out.println("Wrong parameters syntax");
                System.exit(1);
            }
            String key = split[0];
            if (PARAMS.containsKey(key)) {
                PARAMS.put(key, split[1]);
            }
        }
        for (Map.Entry<String, String> entry : PARAMS.entrySet()) {
            if (entry.getValue() == null || entry.getValue().isEmpty()) {
                System.out.println("Please specify " + entry.getKey() + " parameter");
                System.exit(1);
            }
        }
    }

    private static void initFileWriter() {
        try {
            fileWriter = new PrintWriter(new BufferedWriter(new FileWriter(PARAMS.get("out_file"), true)));
            fileWriter.println("RaceID,RaceName,Date,BibNumber,Name,Category,Rank,CategoryPlace,GenderPlace,Swim,Bike,Run,Finish");
            fileWriter.flush();
        } catch (IOException e) {
            System.out.println("Failed to create output file " + PARAMS.get("out_file"));
            System.exit(1);
        }
    }

}
