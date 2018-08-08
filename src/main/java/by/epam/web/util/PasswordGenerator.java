package by.epam.web.util;

import org.apache.commons.text.RandomStringGenerator;

public class PasswordGenerator {
    private static final int DEFAULT_LENGTH = 16;
    private static int length = DEFAULT_LENGTH;

    public static String generatePassword(){
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(Character::isLetterOrDigit)
                .build();
        return generator.generate(length);
    }

    public static void setLength(int newLength){
        length = newLength;
    }

    private PasswordGenerator(){}
}
