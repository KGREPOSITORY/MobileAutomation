package com.appium.device;

import lombok.Getter;

@Getter
public enum Device {
    Pixel_API_35(
            new DeviceData("Android",
                    "emulator-5554",
                    "UIAutomator2",
                    true,
                    false,
                    "1")),

    Medium_Phone_API_35(
            new DeviceData("Android",
                    "emulator-5556",
                    "UIAutomator2",
                    true,
                    false,
                    "2")
    );

    private final DeviceData capabilities;

    Device(DeviceData capabilities) {
        this.capabilities = capabilities;
    }

    public static Device getDeviceByName(String name) {
        for (Device device : Device.values()) {
            if (device.name().equalsIgnoreCase(name)) {
                return device;
            }
        }
        return null;
    }
}
