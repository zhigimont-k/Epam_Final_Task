package by.epam.web.validation;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ActivityValidatorTest {
    private ActivityValidator validator = ActivityValidator.getInstance();

    @DataProvider(name="name")
    public Object[][] provideNameData(){
        return new Object[][]{
                {"howtowashacat", true},
                {"hdkjfg23423 fdgkjsdh4", false},
                {"yere534fmnd_m", false},
                {"blah blah blah", true},
                {"qwerty$", false},
                {"STS-51L", false},
                {"1", false},
                {"skdjfgfdkjgsdfghlkfdjghksjdfghkdfjvhjdsfvndfkjvnkdfvndf4df", false},
                {"какое-то название", true},
                {"some name", true}
        };
    }

    @Test(dataProvider = "name")
    public void validateName(String name, boolean expected){
        Assert.assertEquals(validator.validateName(name), expected);
    }

    @DataProvider(name="description")
    public Object[][] provideDescriptionData(){
        return new Object[][]{
                {"бла бла бла", true},
                {"bjah blah blah!!", true},
                {"???", true},
                {"", false},
                {"qwerty$", false},
                {"       ", false}
        };
    }

    @Test(dataProvider = "description")
    public void validateDescription(String description, boolean expected){
        Assert.assertEquals(validator.validateDescription(description), expected);
    }

    @DataProvider(name="price")
    public Object[][] providePriceData(){
        return new Object[][]{
                {"1200", true},
                {"6.66", true},
                {"yere534fmnd_m", false},
                {"0100", false},
                {"1111111111111", false},
                {"  ", false},
                {"120$", false},
                {"90O", false},
                {" 8", false},
                {"600 ", false}
        };
    }

    @Test(dataProvider = "price")
    public void validatePrice(String price, boolean expected){
        Assert.assertEquals(validator.validatePrice(price), expected);
    }
}
