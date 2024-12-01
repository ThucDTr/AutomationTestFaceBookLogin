package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


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
        login("email_sai@gmail.com", "any_password");
        // Kiểm tra thông báo lỗi
        WebElement errorMessage = driver.findElement(By.xpath("//div[contains(text(),'account')]"));
        Assert.assertTrue(errorMessage.isDisplayed(), "[FAIL] Không hiển thị thông báo lỗi khi email không tồn tại.");
    }

    @Test
    public void KiemTraLoiDeTrongFileName() {
        login("", "");
        // Kiểm tra thông báo lỗi
        WebElement errorMessage = driver.findElement(By.xpath("//div[contains(text(),'email and password')]"));
        Assert.assertTrue(errorMessage.isDisplayed(), "[FAIL] Không hiển thị thông báo lỗi khi trường trống.");
    }

    @Test
    public void KiemTraFomatEmail() {
        login("emailkhonghopdinhdang", "any_password");
        // Kiểm tra thông báo lỗi
        WebElement errorMessage = driver.findElement(By.xpath("//div[contains(text(),'valid email')]"));
        Assert.assertTrue(errorMessage.isDisplayed(), "[FAIL] Không hiển thị thông báo lỗi khi email sai định dạng.");
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

    @Test
    public void testLoginLockedAccount() {
        login("email_bi_khoa@gmail.com", "any_password");
        // Kiểm tra thông báo lỗi
        WebElement errorMessage = driver.findElement(By.xpath("//div[contains(text(),'locked')]"));
        Assert.assertTrue(errorMessage.isDisplayed(), "[FAIL] Không hiển thị thông báo lỗi khi tài khoản bị khóa.");
    }

    @Test
    public void KiemTraLoginVoiPhone() {
        login("+84123456789", "mat_khau_dung");
        // Kiểm tra đăng nhập thành công
        Assert.assertTrue(driver.getCurrentUrl().contains("facebook.com"), "[FAIL] Đăng nhập không thành công với số điện thoại.");
    }

    @AfterMethod
    public void cleanUp(){
        if (driver != null) {
            sleep(5000);
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
