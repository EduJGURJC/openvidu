/*
 * (C) Copyright 2017-2019 OpenVidu (https://openvidu.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.openvidu.test.browsers;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.elastest.etm.model.SharedAsyncModel;

public class ChromeUser extends BrowserUser {
    public ChromeUser(String userName, int timeOfWaitInSeconds, boolean runningAsRoot,
            Map<String, Object> additionalCapabilities) {
        this(userName, timeOfWaitInSeconds, generateDefaultScreenChromeOptions(runningAsRoot),
                additionalCapabilities);
    }

    public ChromeUser(String userName, int timeOfWaitInSeconds, boolean runningAsRoot) {
        this(userName, timeOfWaitInSeconds, runningAsRoot, null);
    }

    public ChromeUser(String userName, int timeOfWaitInSeconds, String screenToCapture,
            boolean runningAsRoot, Map<String, Object> additionalCapabilities) {
        this(userName, timeOfWaitInSeconds, generateCustomScreenChromeOptions(screenToCapture,
                runningAsRoot, Paths.get("/opt/openvidu/fakeaudio.wav")), additionalCapabilities);
    }

    public ChromeUser(String userName, int timeOfWaitInSeconds, String screenToCapture,
            boolean runningAsRoot) {
        this(userName, timeOfWaitInSeconds, screenToCapture, runningAsRoot, null);
    }

    public ChromeUser(String userName, int timeOfWaitInSeconds, Path fakeVideoLocation,
            Map<String, Object> additionalCapabilities) {
        this(userName, timeOfWaitInSeconds, generateFakeVideoChromeOptions(fakeVideoLocation),
                additionalCapabilities);
    }

    public ChromeUser(String userName, int timeOfWaitInSeconds, Path fakeVideoLocation) {
        this(userName, timeOfWaitInSeconds, fakeVideoLocation, null);
    }

    private ChromeUser(String userName, int timeOfWaitInSeconds, ChromeOptions options,
            Map<String, Object> additionalCapabilities) {
        super(userName, timeOfWaitInSeconds);
        options.setAcceptInsecureCerts(true);
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);

        options.addArguments("--disable-infobars");

        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.media_stream_mic", 1);
        prefs.put("profile.default_content_setting_values.media_stream_camera", 1);
        options.setExperimentalOption("prefs", prefs);

        if (additionalCapabilities != null && additionalCapabilities.size() > 0) {
            for (HashMap.Entry<String, Object> additionalCap : additionalCapabilities.entrySet()) {
                if ("chromeVersion".equals(additionalCap.getKey())) {
                    options.setCapability("version", additionalCap.getValue());
                } else {
                    options.setCapability(additionalCap.getKey(), additionalCap.getValue());
                }
            }
        }

        String REMOTE_URL = System.getProperty("REMOTE_URL_CHROME");
        if (REMOTE_URL != null) {
            log.info("Using URL {} to connect to remote web driver", REMOTE_URL);
            try {
                this.driver = new RemoteWebDriver(new URL(REMOTE_URL), options);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            log.info("Using local web driver");
            this.driver = new ChromeDriver(options);
        }

        this.driver.manage().timeouts().setScriptTimeout(this.timeOfWaitInSeconds,
                TimeUnit.SECONDS);
        this.configureDriver();
    }

    private static ChromeOptions generateDefaultScreenChromeOptions(boolean runningAsRoot) {
        ChromeOptions options = new ChromeOptions();
        // This flag avoids to grant the user media
        options.addArguments("--use-fake-ui-for-media-stream");
        // This flag fakes user media with synthetic video
        options.addArguments("--use-fake-device-for-media-stream");
        // This flag selects the entire screen as video source when screen sharing
        options.addArguments("--auto-select-desktop-capture-source=Entire screen");

        if (runningAsRoot) {
            options.addArguments("--no-sandbox");
        }

        return options;
    }

    private static ChromeOptions generateCustomScreenChromeOptions(String screenToCapture,
            boolean runningAsRoot, Path audioFileLocation) {
        ChromeOptions options = new ChromeOptions();
        // This flag selects the entire screen as video source when screen sharing
        options.addArguments("--auto-select-desktop-capture-source=" + screenToCapture);
        options.addArguments("--use-fake-device-for-media-stream");
        options.addArguments("--use-file-for-fake-audio-capture=" + audioFileLocation.toString());

        if (runningAsRoot) {
            options.addArguments("--no-sandbox");
        }

        return options;
    }

    private static ChromeOptions generateFakeVideoChromeOptions(Path videoFileLocation) {
        ChromeOptions options = new ChromeOptions();
        // This flag avoids to grant the user media
        options.addArguments("--use-fake-ui-for-media-stream");
        // This flag fakes user media with synthetic video
        options.addArguments("--use-fake-device-for-media-stream");
        // This flag sets the video input as
        options.addArguments("--use-file-for-fake-video-capture=" + videoFileLocation.toString());
        return options;
    }

}