package test;
 
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
 
public class test {
    
    public static void main(String args[]) throws InterruptedException {
        
        //指定驱动的地址
        System.setProperty("webdriver.gecko.driver","D:\\chromedriver.exe");
        
        //实例化一个WebDriver的对象，此时会启动浏览器
        WebDriver driver = new ChromeDriver();
        
        //最大化浏览器窗口
        driver.manage().window().maximize();
        
        //打开百度首页
        driver.get("http://localhost:8081/store_v2.0/");
        
        //在搜索框中输入查询内容：Yoyo测试精品课程
        // driver.findElement(By.id("kw")).sendKeys("自动化测试");
        Thread.sleep(2000);
        driver.findElement(By.xpath("/html/body/div/div[1]/div[3]/ol/li[1]/a")).click();
        driver.findElement(By.id("username")).sendKeys("vvvvv");
        driver.findElement(By.id("inputPassword3")).sendKeys("11111");
        String code = driver.findElement(By.name("testCode")).getAttribute("value");
        System.out.println(code);
        driver.findElement(By.name("code")).sendKeys(code);
        Thread.sleep(1000 * 3);
        driver.findElement(By.name("submit")).click();
        
        //强制等待3秒
        Thread.sleep(3000);

        //点击“百度一下” 执行搜索
        // driver.findElement(By.id("su")).click();
        
        //强制等待3秒
        Thread.sleep(3000);
 
        //关闭火狐浏览器
        driver.quit();
    }
 
    
    
}