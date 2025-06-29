package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

public class SpendingExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);
    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        User userAnnotation = AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                User.class
        ).orElse(null);
        if (userAnnotation != null && userAnnotation.spendings().length > 0) {
            CategoryJson categoryJson = context.getStore(CategoryExtension.NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
            SpendJson spendJson = createSpend(userAnnotation, categoryJson, userAnnotation.spendings()[0]);
            context.getStore(NAMESPACE).put(
                    context.getUniqueId(),
                    spendApiClient.addSpend(spendJson)
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
}
