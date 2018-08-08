package by.epam.web.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class CopyrightTag extends TagSupport {
    private String year;
    private String projectName;
    private String message;


    @Override
    public int doStartTag() throws JspException {
        try {
            StringBuilder result = new StringBuilder();
            result.append("© ");
            result.append(year);
            result.append(" ");
            result.append(projectName);
            result.append(". ");
            result.append(message);
            pageContext.getOut().write(result.toString());
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }

    public void setYear(String year) {
        String current = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        if (current.equalsIgnoreCase(year)) {
            this.year = year;
        } else {
            this.year = year + " – " + current;
        }

    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

