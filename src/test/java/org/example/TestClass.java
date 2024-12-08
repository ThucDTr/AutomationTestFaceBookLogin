package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;


public class TestClass {

    ChromeDriver driver;

    @BeforeMethod
    public void setup(){
        WebDriverManager.chromedriver().setup();

         driver = new ChromeDriver();
         driver.manage().window().maximize();
         driver.get("https://www.facebook.com/");
    }

    private void login(String email, String password) {
        WebElement emailField = driver.findElement(By.id("email"));
        WebElement passwordField = driver.findElement(By.id("pass"));
        WebElement loginButton = driver.findElement(By.name("login"));

        emailField.clear();
        emailField.sendKeys(email);
        passwordField.clear();
        passwordField.sendKeys(password);
        loginButton.click();
    }

    @Test
    public void KiemTraDangNhapThanhCong() {
        login("email_dung@gmail.com", "mat_khau_dung");
        // Kiểm tra đăng nhập thành công
        Assert.assertTrue(driver.getCurrentUrl().contains("facebook.com"), "[FAIL] Đăng nhập không thành công.");
    }

    @Test
    public void KiemTraLoiPassWord() {
        login("email_dung@gmail.com", "mat_khau_sai");
        // Kiểm tra thông báo lỗi
        WebElement errorMessage = driver.findElement(By.xpath("//div[contains(text(),'incorrect')]"));
        Assert.assertTrue(errorMessage.isDisplayed(), "[FAIL] Không hiển thị thông báo lỗi khi mật khẩu sai.");
    }

    @Test
    public void KiemTraLoiEmailKhongTonTai() {
        String expect = "The email address you entered isn't connected to an account. Find your account and log in.";
        login("email_sai@gmail.com", "any_password");
        // Kiểm tra thông báo lỗi
        WebElement errorMessage = driver.findElement(By.className("_9ay7"));
        String actual = errorMessage.getText();
        Assert.assertEquals(actual, expect);
    }

    @Test
    public void KiemTraLoiDeTrongFileName() {
        login("", "");
        // Kiểm tra thông báo lỗi
        WebElement errorMessage = driver.findElement(By.xpath("//div[contains(text(),'email and password')]"));
        Assert.assertTrue(errorMessage.isDisplayed(), "[FAIL] Không hiển thị thông báo lỗi khi trường trống.");
    }

    @Test
    public void KiemTraFomatEmailorPhone() {
        String expect = "The email address or mobile number you entered isn't connected to an account. Find your account and log in.";
        login("emailkhonghopdinhdang", "any_password");
        // Kiểm tra thông báo lỗi
        WebElement errorMessage = driver.findElement(By.className("_9ay7"));
        String actual = errorMessage.getText();

        Assert.assertEquals(actual, expect);
    }

    @Test
    public void testLoginLongCredentials() {
        String longEmail = "a".repeat(300) + "@gmail.com";
        String longPassword = "b".repeat(300);
        login(longEmail, longPassword);
        // Kiểm tra thông báo lỗi
        WebElement errorMessage = driver.findElement(By.xpath("//div[contains(text(),'invalid')]"));
        Assert.assertTrue(errorMessage.isDisplayed(), "[FAIL] Không hiển thị thông báo lỗi khi thông tin quá dài.");
    }

    @AfterMethod
    public void cleanUp(){
        if (driver != null) {
            sleep(1000);
            driver.quit();
        }
    }

    private void sleep(int time){
        try {
            Thread.sleep(time);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
