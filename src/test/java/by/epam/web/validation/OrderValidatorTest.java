package by.epam.web.validation;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.format.DateTimeParseException;

public class OrderValidatorTest {
    private OrderValidator validator = OrderValidator.getInstance();

    @DataProvider(name="date")
    public Object[][] provideDateData(){
        return new Object[][]{
                {"2019-01-01", true},
                {"01-01-2019", false},
                {"2019/1/1", false},
                {"1990-08-09", false},
                {"11.11.85", false},
                {"21.12.2012", false}
        };
    }

    @Test(dataProvider = "date")
    public void validateDate(String date, boolean expected){
        Assert.assertEquals(validator.validateDate(date), expected);
    }

    @DataProvider(name="time")
    public Object[][] provideTimeData(){
        return new Object[][]{
                {"11:00", "2019-01-01", true},
                {"23:00", "01-01-2019", false},
                {"23:32", "2019/1/1", false},
                {"15.28", "1990-08-09", false},
                {"21:58", "11.11.85", false},
                {"11-00", "21.12.2012", false}
        };
    }

    @Test(dataProvider = "time")
    public void validateTime(String time, String date,  boolean expected){
        Assert.assertEquals(validator.validateTime(time, date), expected);
    }

    @DataProvider(name="status")
    public Object[][] provideStatusData(){
        return new Object[][]{
                {"cancelled", true},
                {"PENDING", true},
                {"confirmed", true},
                {"finished", true},
                {"_finished", false},
                {"not_finished", false},
                {"завершён", false},
                {"отменён", false},
                {"cancel", false},
                {"ConFirmed", true}
        };
    }

    @Test(dataProvider = "status")
    public void validateStatus(String status, boolean expected){
        Assert.assertEquals(validator.validateStatus(status), expected);
    }
}
