package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

public class RandomDataUtils {
    private static final Faker faker = new Faker();

    public static String getRandomUsername() {
        return faker.name().username();
    }

    public static String getRandomName() {
        return faker.name().fullName();
    }

    public static String getRandomSurname() {
        return faker.name().lastName();
    }

    public static String getRandomCategoryName() {
        return faker.funnyName().name();
    }
}
