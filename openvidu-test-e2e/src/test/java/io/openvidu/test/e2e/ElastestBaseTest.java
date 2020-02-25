package io.openvidu.test.e2e;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ElastestBaseTest {
    protected static final Logger logger = LoggerFactory.getLogger(ElastestBaseTest.class);

    protected static final String CHROME = "chrome";
    protected static final String FIREFOX = "firefox";

    protected static String browserType;
    protected static String browserVersion;
    protected static String eusApiURL;
    protected static String sutUrl;

    protected WebDriver driver;

    @BeforeAll
    public static void setupClass() {
        String sutHost = System.getenv("ET_SUT_HOST");
        String sutPort = System.getenv("ET_SUT_PORT");
        String sutProtocol = System.getenv("ET_SUT_PROTOCOL");

        if (sutHost == null) {
            sutUrl = "http://localhost:8080/";
        } else {
            sutPort = sutPort != null ? sutPort : "8080";
            sutProtocol = sutProtocol != null ? sutProtocol : "http";

            sutUrl = sutProtocol + "://" + sutHost + ":" + sutPort;
        }
        logger.info("SuT URL: " + sutUrl);

        browserType = System.getProperty("browser");
        logger.info("Browser Type: {}", browserType);
        eusApiURL = System.getenv("ET_EUS_API");

        logger.info("REMOTE_URL_CHROME {}", System.getProperty("REMOTE_URL_CHROME"));
        logger.info("REMOTE_URL_FIREFOX {}", System.getProperty("REMOTE_URL_FIREFOX"));
//        if (eusApiURL == null) {
//            if (browserType == null || browserType.equals(CHROME)) {
//                WebDriverManager.chromedriver().setup();
//            } else {
//                WebDriverManager.firefoxdriver().setup();
//            }
//        } else {
//            logger.info("Using ElasTest EUS Api url for browsers: {}", eusApiURL);
//        }
    }

    @BeforeEach
    public void setupTest(TestInfo info) throws MalformedURLException {
        String testName = info.getTestMethod().get().getName();
        logger.info("##### Start test: {}", testName);
//
//        if (eusApiURL == null) {
//            if (browserType == null || browserType.equals(CHROME)) {
//                driver = new ChromeDriver();
//            } else {
//                driver = new FirefoxDriver();
//            }
//        } else {
//            DesiredCapabilities caps;
//            if (browserType == null || browserType.equals(CHROME)) {
//                caps = DesiredCapabilities.chrome();
//            } else {
//                caps = DesiredCapabilities.firefox();
//            }
//
//            browserVersion = System.getProperty("browserVersion");
//            if (browserVersion != null) {
//                logger.info("Browser Version: {}", browserVersion);
//                caps.setVersion(browserVersion);
//            }
//
//            caps.setCapability("testName", testName);
//            driver = new RemoteWebDriver(new URL(eusApiURL), caps);
//        }
//
//        driver.get(sutUrl);
    }

    @AfterEach
    public void teardown(TestInfo info) {
        if (driver != null) {
            driver.quit();
        }

        String testName = info.getTestMethod().get().getName();
        logger.info("##### Finish test: {}", testName);
    }

}