package com.appium.device;

public record DeviceData(String platformName,
                         String deviceName,
                         String appiumAutomationName,
                         boolean appiumNoReset,
                         boolean appiumFullReset,
                         String udid) {
}
