package com.tgmeng.common.util;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;

@Slf4j
public class CommonSeleniumUtil {
    static {
        // 程序启动时自动执行一次
        WebDriverManager.chromedriver().setup();
    }

    public static String getShenMeZhiDeMaiCookie(String userAgent) throws RuntimeException {
        ChromeDriver driver = null;
        try {
            driver = new ChromeDriver(getDefaultChromeOptions(userAgent));
            // 1️⃣ 访问页面
            driver.get("https://faxian.smzdm.com/h0s0t14995f0c0p1/");
            // 2️⃣ 等待页面完全加载（保证 JS 执行完毕）
            new WebDriverWait(driver, Duration.ofSeconds(10)) .until(webDriver -> ((JavascriptExecutor) webDriver) .executeScript("return document.readyState").equals("complete"));
            long startTime = System.currentTimeMillis();
            // 3️⃣ 开始循环等待核心 cookie 出现
            while (System.currentTimeMillis() - startTime < 10_000) { // 最多等待10秒
                Cookie cookieW = driver.manage().getCookieNamed("w_tsfp");
                Cookie cookieG = driver.manage().getCookieNamed("__ckguid");
                if (cookieW != null && cookieG != null) {
                    StringBuilder cookieHeader = new StringBuilder();
                    cookieHeader.append(cookieW.getName()).append("=").append(cookieW.getValue()).append("; ");
                    cookieHeader.append(cookieG.getName()).append("=").append(cookieG.getValue()).append("; ");
                    // 去掉最后多余的 "; "
                    if (!cookieHeader.isEmpty()) {
                        cookieHeader.setLength(cookieHeader.length() - 2);
                    }
                    return cookieHeader.toString();
                }
                try {
                    Thread.sleep(200); // 每200毫秒检查一次
                } catch (InterruptedException ignored) {
                }
            }

            // 超时未获取到 cookie
            throw new RuntimeException("超时未获取到 w_tsfp cookie");
        } catch (Exception e) {
            throw new RuntimeException("获取 cookie 出错", e);
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    public static ChromeOptions getDefaultChromeOptions(String userAgent) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");
        options.addArguments("--window-size=1366,768");
        options.addArguments("--enable-features=NetworkService,NetworkServiceInProcess");
        options.addArguments("--user-agent=" + userAgent);
        options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        return options;
    }
}
