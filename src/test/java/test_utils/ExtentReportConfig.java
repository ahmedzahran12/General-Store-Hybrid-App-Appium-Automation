package test_utils;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
public class ExtentReportConfig {
    static ExtentReports extent;

    public static ExtentReports getReporterObject(){

    String path = System.getProperty("user.dir") + "//reports//index.html";
    new java.io.File(System.getProperty("user.dir") + "//reports").mkdirs();
    ExtentSparkReporter reporter = new ExtentSparkReporter(path);
    reporter.config().setReportName("General Store Tests");
    reporter.config().setDocumentTitle("Test Results");
    extent =new ExtentReports();
    extent.attachReporter(reporter);
    extent.setSystemInfo("Tester","Rahul Shetty");
    return extent;

}
 }




