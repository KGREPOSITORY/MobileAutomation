package com.appium;

import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SpringBootTest
public class FirstTest extends BaseTest {

    @Test(description = "Check authentication stats",
            groups = "authenticationStatusTest",
            dataProvider = "authenticationStatusDataProvider")
    public void checkBiometricStatus(String buttonName, WebElement actionButton, String status) {
        actionButton.click();
        Assert.assertEquals(pageFactory.get().getBiometricAuthenticationPage().getAuthenticationStatusText().getText(),
                status,
                "Check that authentication status is " + status + " after click " + buttonName);

    }

    @BeforeMethod(onlyForGroups = {"authenticationStatusTest"},firstTimeOnly = true)
    public void openBiometricAuthenticationPage() {
        pageFactory.get().getMainPage()
                .goToBiometricAuthenticationPage();
    }

    @DataProvider(name = "authenticationStatusDataProvider")
    public Object[][] authenticationStatusDataProvider() {
        System.out.println("Using page factory for thread with name : " + Thread.currentThread().getName());
        return new Object[][]{
                {"Ask biometric authentication",
                        pageFactory.get().getBiometricAuthenticationPage().getAskBiometricAuthenticationButton(),
                        "FAILED"
                },
                {"Force pass callback",
                        pageFactory.get().getBiometricAuthenticationPage().getForcePassCallbackButton(),
                        "SUCCEEDED"
                },
                {"Force fail callback",
                        pageFactory.get().getBiometricAuthenticationPage().getForceFailCallbackButton(),
                        "FAILED"},
        };
    }
}
