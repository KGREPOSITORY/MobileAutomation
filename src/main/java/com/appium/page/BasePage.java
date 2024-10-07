package com.appium.page;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;

public class BasePage {

    protected AppiumDriver driver;
    @Value("${application.appium.element.timeout}")
    private long timeout;

    public BasePage(AppiumDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofMillis(timeout)), this);
    }


}
