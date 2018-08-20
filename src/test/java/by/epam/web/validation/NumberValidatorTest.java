package by.epam.web.validation;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class NumberValidatorTest {
    private NumberValidator validator = NumberValidator.getInstance();

    @DataProvider(name="money")
    public Object[][] provideMoneyData(){
        return new Object[][]{
                {"10", true},
                {"5.55", true},
                {"-1", false},
                {"0", false},
                {"66,6", false},
        };
    }

    @Test(dataProvider = "money")
    public void validateMoney(String money, boolean expected){
        Assert.assertEquals(validator.validateMoney(money), expected);
    }

    @DataProvider(name="page")
    public Object[][] providePageData(){
        return new Object[][]{
                {"10", true},
                {"5.55", false},
                {"-1", false},
                {"0", false},
                {"десять", false},
        };
    }

    @Test(dataProvider = "page")
    public void validatePage(String page, boolean expected){
        Assert.assertEquals(validator.validateNumber(page), expected);
    }
}
