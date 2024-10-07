package com.appium.page;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import lombok.Getter;
import org.openqa.selenium.WebElement;

@Getter
public class BiometricAuthenticationPage extends BasePage {

    public BiometricAuthenticationPage(AppiumDriver driver) {
        super(driver);
    }

    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"CHECK IF AVIALABLE\"]")
    //    @iOSXCUITFindBy() locator for IOS
    private WebElement checkIfAvailableButton;
    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"ASK BIOMETRIC AUTHENTICATION\"]")
    //    @iOSXCUITFindBy() locator for IOS
    private WebElement askBiometricAuthenticationButton;
    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"FORCE PASS CALLBACK\"]")
    //    @iOSXCUITFindBy() locator for IOS
    private WebElement forcePassCallbackButton;
    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"FORCE FAIL CALLBACK\"]")
    //    @iOSXCUITFindBy() locator for IOS
    private WebElement forceFailCallbackButton;
    @AndroidFindBy(accessibility = "Authentication status value")
    //    @iOSXCUITFindBy() locator for IOS
    private WebElement authenticationStatusText;
}
