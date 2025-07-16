package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDbClient {
    private static final Config CFG = Config.getInstance();
    SpendDao spendDao = new SpendDaoJdbc();
    CategoryDao categoryDao = new CategoryDaoJdbc();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );


    public SpendJson create(SpendJson spend) {
        return jdbcTxTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            spendDao.create(spendEntity)
                    );
                }
        );
    }

    public Optional<SpendEntity> findSpendById(UUID id) {
        return jdbcTxTemplate.execute(() -> spendDao.findSpendById(id));
    }

    public List<SpendEntity> findSpendsByUsername(String username) {
        return jdbcTxTemplate.execute(() -> spendDao.findAllSpendsByUsername(username));
    }

    public void deleteSpend(SpendEntity spend) {
        jdbcTxTemplate.execute(() -> {spendDao.deleteSpend(spend);
            return null;
        });
    }

    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return jdbcTxTemplate.execute(() -> categoryDao.findCategoryByUsernameAndCategoryName(username, categoryName));
    }

    public List<CategoryEntity> findAllCategoriesByUsername(String username) {
        return jdbcTxTemplate.execute(() -> categoryDao.findAllCategoriesByUsername(username));
    }

    public void deleteCategory(CategoryEntity category) {
        jdbcTxTemplate.execute(() -> {
            categoryDao.deleteCategory(category);
            return null;
        });
    }

    public CategoryEntity createCategory(CategoryEntity category) {
        return jdbcTxTemplate.execute(() -> categoryDao.create(category));
    }
}
