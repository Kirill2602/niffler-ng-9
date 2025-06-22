package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.api.CategoryApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);
    private final CategoryApiClient categoryApiClient = new CategoryApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Faker faker = new Faker();
        String categoryName = faker.funnyName().name();
        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                Category.class
        ).ifPresent(annotation -> {
            CategoryJson categoryJson = new CategoryJson(
                    null,
                    annotation.name() + " " + categoryName,
                    annotation.username(),
                    annotation.archived()
            );
            CategoryJson created = categoryApiClient.addCategory(categoryJson);
            if (annotation.archived()) {
                CategoryJson archivedCategory = new CategoryJson(
                        created.id(),
                        created.name(),
                        created.username(),
                        true
                );
                created = categoryApiClient.updateCategory(archivedCategory);
            }
            context.getStore(NAMESPACE).put(context.getUniqueId(), created);
        });
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CategoryJson category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        if (!category.archived()) {
            CategoryJson archivedCategory = new CategoryJson(
                    category.id(),
                    category.name(),
                    category.username(),
                    true
            );
            context.getStore(NAMESPACE).put(context.getUniqueId(), categoryApiClient.updateCategory(archivedCategory));
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }
}
