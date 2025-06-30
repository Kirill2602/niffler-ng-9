package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UserQueueExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {
    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserQueueExtension.class);

    public record StaticUser(
            String username,
            String password,
            String friend,
            String income,
            String outcome
    ) {

    }

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();

    static {
        EMPTY_USERS.add(new StaticUser("Kirill", "12345", null, null, null));
        WITH_FRIEND_USERS.add(new StaticUser("marjory.sanford", "12345", "Kirill2", null, null));
        WITH_INCOME_REQUEST_USERS.add(new StaticUser("duck", "12345", null, "Kirill1", null));
        WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("Kirill1", "12345", null, null, "duck"));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        Type value() default Type.EMPTY;

        enum Type {
            EMPTY, WITH_FRIEND, WITH_INCOME_REQUEST, WITH_OUTCOME_REQUEST
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Map<UserType, Queue<StaticUser>> users = new HashMap<>();
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .map(parameter -> parameter.getAnnotation(UserType.class))
                .forEach(userType -> {
                    Optional<StaticUser> user = Optional.empty();
                    StopWatch sw = StopWatch.createStarted();
                    while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                        user = switch (userType.value()) {
                            case EMPTY -> Optional.ofNullable(EMPTY_USERS.poll());
                            case WITH_FRIEND -> Optional.ofNullable(WITH_FRIEND_USERS.poll());
                            case WITH_INCOME_REQUEST -> Optional.ofNullable(WITH_INCOME_REQUEST_USERS.poll());
                            case WITH_OUTCOME_REQUEST -> Optional.ofNullable(WITH_OUTCOME_REQUEST_USERS.poll());
                        };
                    }
                    Allure.getLifecycle()
                            .updateTestCase(
                                    testCase -> testCase.setStart(new Date().getTime()));
                    user.ifPresentOrElse(u ->
                                    users.computeIfAbsent(userType, k -> new ConcurrentLinkedQueue<>()).add(u),
                            () -> {
                                throw new IllegalStateException("Can't find user after 30 seconds");
                            }
                    );
                    context.getStore(NAMESPACE).put(context.getUniqueId(), users);
                });
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Map<UserType, Queue<StaticUser>> users = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);
        for (Map.Entry<UserType, Queue<StaticUser>> entry : users.entrySet()) {
            for (StaticUser user : entry.getValue()) {
                switch (entry.getKey().value()) {
                    case WITH_FRIEND -> WITH_FRIEND_USERS.add(user);
                    case WITH_INCOME_REQUEST -> WITH_INCOME_REQUEST_USERS.add(user);
                    case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUEST_USERS.add(user);
                    case EMPTY -> EMPTY_USERS.add(user);
                }
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        HashMap<UserType, Queue<StaticUser>> users =
                extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), HashMap.class);
        UserType annotation = parameterContext.getParameter().getAnnotation(UserType.class);

        if (users == null || !users.containsKey(annotation)) {
            throw new IllegalStateException("User type " + annotation + " not found");
        }
        return users.get(annotation).poll();
    }
}
