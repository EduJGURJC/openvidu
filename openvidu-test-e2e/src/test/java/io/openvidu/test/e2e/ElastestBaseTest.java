package io.openvidu.test.e2e;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElastestBaseTest {
    protected static final Logger logger = LoggerFactory.getLogger(ElastestBaseTest.class);

    protected static String eusApiURL;

    protected Map<String, Object> additionalCapabilities;

    protected WebDriver driver;

    @BeforeAll
    public static void setupClass() {
        eusApiURL = System.getenv("ET_EUS_API");
        logger.info("REMOTE_URL_CHROME {}", System.getProperty("REMOTE_URL_CHROME"));
        logger.info("REMOTE_URL_FIREFOX {}", System.getProperty("REMOTE_URL_FIREFOX"));
    }

    @BeforeEach
    public void setupTest(TestInfo info) throws MalformedURLException {
        String testName = info.getTestMethod().get().getName();
        logger.info("##### Start test: {}", testName);

        additionalCapabilities = new HashMap<>();
        additionalCapabilities.put("testName", testName);

        String chromeVersion = System.getProperty("chromeVersion");
        if (chromeVersion != null) {
            logger.info("Using Chrome Version: {}", chromeVersion);
            additionalCapabilities.put("chromeVersion", chromeVersion);
        }

        String firefoxVersion = System.getProperty("firefoxVersion");
        if (firefoxVersion != null) {
            logger.info("Using Firefox Version: {}", firefoxVersion);
            additionalCapabilities.put("chromeVersion", firefoxVersion);
        }
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