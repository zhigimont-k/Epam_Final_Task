package by.epam.web.validation;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ReviewValidatorTest {
    private ReviewValidator validator = ReviewValidator.getInstance();

    @DataProvider(name="mark")
    public Object[][] provideMarkData(){
        return new Object[][]{
                {"10", true},
                {"O", false},
                {"-1", false},
                {"0", false},
                {"десять", false},
        };
    }

    @Test(dataProvider = "mark")
    public void validateMark(String mark,boolean expected){
        Assert.assertEquals(validator.validateMark(mark), expected);
    }

    @DataProvider(name="message")
    public Object[][] provideMessageData(){
        return new Object[][]{
                {"бла бла бла", true},
                {"bjah blah blah!!", true},
                {"???", true},
                {"", true},
                {"qwerty$", false},
                {"       ", false}
        };
    }

    @Test(dataProvider = "message")
    public void validateMessage(String message, boolean expected){
        Assert.assertEquals(validator.validateMessage(message), expected);
    }
}
