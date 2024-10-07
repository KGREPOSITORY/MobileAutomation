package com.appium.core;

import com.appium.device.Device;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.testng.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class EmulatorManager {
    @Value("${application.path}")
    private String applicationPath;

    public void startDevice(Device device) {
        log.info("Starting device with name {}", device.name());
        String[] command = {"emulator", "-avd", device.name(), "-no-snapshot-load", "-no-snapshot-save", "-port",
                device.getCapabilities().deviceName().replaceAll("emulator-", "")};
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder
                    .inheritIO()
                    .start()
                    .waitFor(50000, TimeUnit.MILLISECONDS);
            log.info("Waiting for the device to be prepared");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        Assert.assertTrue(isDeviceRunning(device), "Is device running with following name " + device.name());
    }

    public void stopDevices(String deviceName) {
        log.info("Stopping device with name {}", deviceName);
        String[] command = {"adb", "-s", deviceName, "emu", "kill"};
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.inheritIO().start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isDeviceRunning(Device device) {
        try {
            log.info("Check if device {} has been attached", device.getCapabilities().deviceName());
            String[] abdCommand = {"adb", "devices"};
            Process devices = new ProcessBuilder(abdCommand).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(devices.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
            return result.toString().contains(device.getCapabilities().deviceName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isApplicationInstalledForDevice(Device device, String packageName) {
        ProcessBuilder processBuilder = new ProcessBuilder("adb", "-s", device.getCapabilities().deviceName(),
                "shell", "pm", "list", "packages");
        StringBuilder output = new StringBuilder();
        try {
            log.info("Checking if app installed on device");
            processBuilder.inheritIO().start().waitFor(1,TimeUnit.SECONDS);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(processBuilder.start().getInputStream()));
            String tempLine;
            while ((tempLine = bufferedReader.readLine()) != null){
                output.append(tempLine).append("\n");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return output.toString().contains(packageName);
    }

    public void installApplication(Device device){
        log.info("Installing application for device {}", device.name());
        String [] command = {"adb", "-s", device.getCapabilities().deviceName(), "install", applicationPath};

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        try {
            processBuilder.inheritIO().start().waitFor(5000, TimeUnit.MILLISECONDS);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
