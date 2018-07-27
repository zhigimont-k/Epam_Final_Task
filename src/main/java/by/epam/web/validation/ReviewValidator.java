package by.epam.web.validation;

import by.epam.web.entity.Review;
import by.epam.web.entity.User;

public class ReviewValidator implements AbstractValidator<Review> {
    private static final int MAX_MARK = 10;
    private static final int MIN_MARK = 1;
    private static final int MAX_COMMENT_LENGTH = 280;
    @Override
    public boolean validate(Review review) {
        return validateMark(review.getMark())&&
                validateMessage(review.getMessage());
    }

    public boolean checkAccess(User user, Review review) {
        return user.getId() == review.getUserId();
    }

    private boolean validateMark(int mark){
        return mark >= MIN_MARK && mark <= MAX_MARK;
    }

    private boolean validateMessage(String message){
        return message.isEmpty() || message.length() <= MAX_COMMENT_LENGTH;
    }
}
