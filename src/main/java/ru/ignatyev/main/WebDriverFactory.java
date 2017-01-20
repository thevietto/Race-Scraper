package ru.ignatyev.main;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WebDriverFactory {

    public WebDriverFactory() {
    }

    public WebDriver createPhantomJSWebDriver() {
        final DesiredCapabilities capabilities = new DesiredCapabilities();

        // Configure our WebDriver to support JavaScript and be able to find the PhantomJS binary
        capabilities.setJavascriptEnabled(true);
        capabilities.setCapability("takesScreenshot", false);
        return new PhantomJSDriver(capabilities);
    }

}
