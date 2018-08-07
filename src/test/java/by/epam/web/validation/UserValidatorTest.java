package by.epam.web.validation;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class UserValidatorTest {
    private UserValidator validator = UserValidator.getInstance();

    @DataProvider(name="login")
    public Object[][] provideLoginData(){
        return new Object[][]{
                {"howtowashacat", true},
                {"vasyan3000", true},
                {"yere534fmnd_m", true},
                {"my bloody valentine", false},
                {"qwerty$", false},
                {"STS-51L", false},
                {"1", false},
                {"skdjfgfdkjgsdfghlkfdjghksjdfghkdfjvhjdsfvndfkjvnkdfvndf4df", false},
                {"ооо моя оборона", false},
                {"лолкек", false}
        };
    }

    @Test(dataProvider = "login")
    public void validateLogin(String login, boolean expected){
        Assert.assertEquals(validator.validateLogin(login), expected);
    }

    @Test(dataProvider = "login")
    public void validatePassword(String password, boolean expected){
        Assert.assertEquals(validator.validatePassword(password), expected);
    }

    @DataProvider(name="email")
    public Object[][] provideEmailData(){
        return new Object[][]{
                {"howtowashacat@ww.ru", true},
                {"vasyan3000.k@sdf.com", true},
                {"yere534fmnd_m@ee.ru", true},
                {"my bloody valentine@lol.ru", false},
                {"qwerty$", false},
                {"STS-51L", false},
                {"1", false},
                {"skdjfgfdkjgsdfghlkfdjghksjdfghkdfjvhjdsfvndfkjvnkdfvndf4df@gg.gg", false},
                {"fff@@t8.ty", false},
                {"лолкек@ss.ru", false}
        };
    }

    @Test(dataProvider = "email")
    public void validateEmail(String email, boolean expected){
        Assert.assertEquals(validator.validateEmail(email), expected);
    }

    @DataProvider(name="phoneNumber")
    public Object[][] providePhoneNumberData(){
        return new Object[][]{
                {"+375448688888", true},
                {"+375174444444", true},
                {"+375294445566", true},
                {"375294444444", false},
                {"+37556666666", false},
                {"+3375477777777", false},
                {"+3h529uuu6677", false},
                {"-375298888888", false},
                {"+3333344@5$", false},
                {"     ", false}
        };
    }

    @Test(dataProvider = "phoneNumber")
    public void validatePhoneNumber(String phoneNumber, boolean expected){
        Assert.assertEquals(validator.validatePhoneNumber(phoneNumber), expected);
    }

    @DataProvider(name="userName")
    public Object[][] provideUserNameData(){
        return new Object[][]{
                {"rrr", true},
                {"иннокентий геннадьевич", true},
                {"ррр", true},
                {"kkk :)", false},
                {"+xzczxc", false},
                {"a", false},
                {"zxkcjnxzkcjxhncxzkcnzkxjcnkzxjcnkxzjncxzcxc", false},
                {"*&^W#*&$^W#$", false},
                {"3475834568457", false},
                {"    ", false}
        };
    }

    @Test(dataProvider = "userName")
    public void validateUserName(String userName, boolean expected){
        Assert.assertEquals(validator.validateUserName(userName), expected);
    }

    @DataProvider(name="cardNumber")
    public Object[][] provideCardNumberData(){
        return new Object[][]{
                {"1919191919191919", true},
                {"2222222222222222", true},
                {"111111111111", false},
                {"2222-2222-2222-2222", false},
                {"222222222222222222222", false},
                {"a2222222222222222", false},
                {"O0111111111111", false},
                {"+6666666666666666", false},
                {"4444<4444<4444<4444", false},
                {"    ", false}
        };
    }

    @Test(dataProvider = "cardNumber")
    public void validateCardNumber(String cardNumber, boolean expected){
        Assert.assertEquals(validator.validateCardNumber(cardNumber), expected);
    }

    @DataProvider(name="status")
    public Object[][] provideStatusData(){
        return new Object[][]{
                {"user", true},
                {"ADMiN", true},
                {"moderator", false},
                {"админ", false},
                {"забанен", false},
                {"ban", false},
                {"unbanned", false},
                {"юзер", false},
                {"administrator", false},
                {"    ", false}
        };
    }

    @Test(dataProvider = "status")
    public void validateStatus(String status, boolean expected){
        Assert.assertEquals(validator.validateStatus(status), expected);
    }
}
