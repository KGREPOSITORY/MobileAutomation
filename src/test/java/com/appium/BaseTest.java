package com.appium;

import com.appium.core.AppiumServerManager;
import com.appium.device.Device;
import com.appium.page.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

@SpringBootTest(classes = AppiumApplication.class)
public class BaseTest extends AbstractTestNGSpringContextTests{

    @Autowired
    public AppiumServerManager appiumServerManager;

    protected ThreadLocal<PageFactory> pageFactory = new ThreadLocal<>();

    @Override
    @BeforeTest(alwaysRun = true)
    // fixing issue when Autowiring is not happens before @BeforeTest
    protected void springTestContextPrepareTestInstance() throws Exception {
        super.springTestContextPrepareTestInstance();
    }

    @BeforeTest(dependsOnMethods = "springTestContextPrepareTestInstance" )
    @Parameters({"device"})
    public void setupEnvironment(String deviceName){
        Device device = Device.getDeviceByName(deviceName);
        appiumServerManager.prepareDriver(device);
        System.out.println("Preparing page factory for thread with name : " + Thread.currentThread().getName());
        pageFactory.set(new PageFactory(appiumServerManager.getDriver()));
    }

    @AfterTest(alwaysRun = true)
    @Parameters({"device"})
    public void stopDeviceAndAppiumSession(String deviceName){
        Device device = Device.getDeviceByName(deviceName);
        appiumServerManager.closeAppiumSessionAndStopDevice(device);
        pageFactory.remove();
    }

    @AfterSuite(alwaysRun = true)
    public void stopAppiumServer(){
        appiumServerManager.stopAppiumServer();
    }

}
