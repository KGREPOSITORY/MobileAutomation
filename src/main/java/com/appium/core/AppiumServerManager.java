package com.appium.core;

import com.appium.device.Device;
import com.appium.device.DeviceData;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServerHasNotBeenStartedLocallyException;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;

@Service
@Log4j2
public class AppiumServerManager {

    private final EmulatorManager emulatorManager;
    private AppiumDriverLocalService appiumDriverLocalService;
    private final ThreadLocal<AppiumDriver> appiumDriver = new ThreadLocal<>();
    @Value("${application.appium.servier.path}")
    private String appiumUrl;
    @Value("${application.path}")
    private String appPath;
    @Value("${application.package.name}")
    private String packageName;

    @Autowired
    public AppiumServerManager(EmulatorManager emulatorManager) {
        this.emulatorManager = emulatorManager;
    }

    public void startAppiumServer() {
        try {
            log.info("Starting appium driver local service");
            appiumDriverLocalService = AppiumDriverLocalService.buildDefaultService();
            appiumDriverLocalService.start();
        } catch (AppiumServerHasNotBeenStartedLocallyException e) {
            throw new RuntimeException("Cant start appium service due to following issue : " + e + "Thread name" + Thread.currentThread().getName());
        }
    }

    public void stopAppiumServer() {
        log.info("Stopping appium driver service");
        if (appiumDriverLocalService.isRunning()) {
            appiumDriverLocalService.stop();
        }
    }

    public DesiredCapabilities prepareCapabilities(DeviceData capabilities) {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("platformName", capabilities.platformName());
        desiredCapabilities.setCapability("appium:deviceName", capabilities.deviceName());
        desiredCapabilities.setCapability("appium:automationName", capabilities.appiumAutomationName());
        desiredCapabilities.setCapability("appium:noReset", capabilities.appiumNoReset());
        desiredCapabilities.setCapability("appium:fullReset", capabilities.appiumFullReset());
        desiredCapabilities.setCapability("appium:app", appPath);
        desiredCapabilities.setCapability("appium:udid", capabilities.deviceName());

        return desiredCapabilities;
    }

    public AppiumDriver prepareAppiumDriver(DeviceData capabilities) {
        try {
            log.info("Prepare appium driver for device {} with capabilities {}", capabilities.deviceName(), capabilities);
            return new AppiumDriver(new URL(appiumUrl), prepareCapabilities(capabilities));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public void prepareDriver(Device device) {
        synchronized (this) {
            if (appiumDriverLocalService == null || !appiumDriverLocalService.isRunning()) {
                log.info("Appium server is not running. Start appium server");
                startAppiumServer();
            }
        }
        if (!emulatorManager.isDeviceRunning(device)) {
            log.info("Device {} is not running. Start device", device.getCapabilities().deviceName());
            emulatorManager.startDevice(device);
        }
        if (!emulatorManager.isApplicationInstalledForDevice(device, packageName)) {
            emulatorManager.installApplication(device);
        }
        appiumDriver.set(prepareAppiumDriver(device.getCapabilities()));
    }

    public void closeAppiumSessionAndStopDevice(Device device) {
        if (appiumDriver.get() != null) {
            log.info("Closing appium session for device {}", device.getCapabilities().deviceName());
            appiumDriver.get().quit();
            appiumDriver.remove();
        } else {
            log.info("There is no any session for {}", device.getCapabilities().deviceName());
        }

        if (emulatorManager.isDeviceRunning(device)) {
            log.info("Device {} is running. Stop device", device.getCapabilities().deviceName());
            emulatorManager.stopDevices(device.getCapabilities().deviceName());
        } else {
            log.info("There is no running devise with {} name", device.getCapabilities().deviceName());
        }
    }

    public AppiumDriver getDriver() {
        return appiumDriver.get();
    }

}
