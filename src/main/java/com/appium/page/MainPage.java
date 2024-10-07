package com.appium.page;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import lombok.Getter;
import org.openqa.selenium.WebElement;

@Getter
public class MainPage extends BasePage{

    public MainPage(AppiumDriver driver) {
        super(driver);
    }

    @AndroidFindBy(accessibility = "Biometric authentication")
//    @iOSXCUITFindBy() locator for IOS
     private WebElement biometricAuthenticationButton;


    public BiometricAuthenticationPage goToBiometricAuthenticationPage(){
        biometricAuthenticationButton.click();
        return new BiometricAuthenticationPage(driver);
    }




}
