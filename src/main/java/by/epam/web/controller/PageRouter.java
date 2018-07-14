package by.epam.web.controller;

public class PageRouter {
    public enum TransitionType {
        FORWARD, REDIRECT
    }
    private String page;
    private TransitionType transitionType = TransitionType.FORWARD;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public TransitionType getTransitionType() {
        return transitionType;
    }

    public void setTransitionType(TransitionType transitionType) {
        if (transitionType == null){
            this.transitionType = TransitionType.FORWARD;
        } else {
            this.transitionType = transitionType;
        }
    }
}
