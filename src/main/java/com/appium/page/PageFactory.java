package com.appium.page;

import io.appium.java_client.AppiumDriver;

public class PageFactory {

    private final AppiumDriver appiumDriver;

    public PageFactory(AppiumDriver appiumDriver) {
        this.appiumDriver = appiumDriver;
    }

    public MainPage getMainPage(){
        return new MainPage(appiumDriver);
    }

    public BiometricAuthenticationPage getBiometricAuthenticationPage(){
        return new BiometricAuthenticationPage(appiumDriver);
    }

}
