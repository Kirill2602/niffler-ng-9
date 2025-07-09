package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

import static guru.qa.niffler.utils.RandomDataUtils.getRandomCategoryName;
import static java.sql.Connection.TRANSACTION_READ_COMMITTED;

public class SpendingExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);
    private final SpendDbClient spendDbClient = new SpendDbClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        User userAnnotation = AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                User.class
        ).orElse(null);
        if (userAnnotation != null && userAnnotation.spendings().length > 0) {
            CategoryJson categoryJson;
            SpendJson spendJson = null;
            categoryJson = context.getStore(CategoryExtension.NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
            if (categoryJson != null) {
                spendJson = createSpend(userAnnotation, categoryJson, userAnnotation.spendings()[0]);
            } else {
                spendJson = createSpend(userAnnotation, userAnnotation.spendings()[0]);
            }
            context.getStore(NAMESPACE).put(
                    context.getUniqueId(),
                    spendDbClient.create(spendJson, TRANSACTION_READ_COMMITTED)
            );
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
    }

    @Override
    public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), SpendJson.class);
    }

    private SpendJson createSpend(User user, CategoryJson categoryJson, Spending spend) {
        return new SpendJson(
                null,
                new Date(),
                categoryJson,
                spend.currency(),
                spend.amount(),
                spend.description(),
                user.username()
        );
    }

    private SpendJson createSpend(User user, Spending spend) {
        CategoryJson categoryJson = new CategoryJson(
                null,
                getRandomCategoryName(),
                user.username(),
                false
        );
        return new SpendJson(
                null,
                new Date(),
                categoryJson,
                spend.currency(),
                spend.amount(),
                spend.description(),
                user.username()
        );
    }
}
