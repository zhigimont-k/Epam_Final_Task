package by.epam.web.tag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

@SuppressWarnings("serial")
public class WelcomeTag extends TagSupport{
    private String name;
    private String lang;

    public void setName(String name) {
        this.name = name;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            StringBuilder result = new StringBuilder();
            ResourceBundle bundle = ResourceBundle.getBundle(
                    LocaleConstants.LOCALE_BASENAME, new Locale(lang));
            result.append(bundle.getString(LocaleConstants.WELCOME_MESSAGE));
            result.append(", ");
            result.append(name);
            pageContext.getOut().write(result.toString());
        } catch (IOException e){
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }


}
