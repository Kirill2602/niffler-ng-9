package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;

public class SpendDbClient {
    private static final Config CFG = Config.getInstance();

    public SpendJson create(SpendJson spend, int transactionLevel) {
        return transaction(connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = new CategoryDaoJdbc(connection)
                                .create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            new SpendDaoJdbc(connection).create(spendEntity)
                    );
                },
                CFG.spendJdbcUrl(),
                transactionLevel
        );
    }

    public Optional<SpendEntity> findSpendById(UUID id, int transactionLevel) {
        return transaction(connection -> {
                    return new SpendDaoJdbc(connection).findSpendById(id);
                }, CFG.spendJdbcUrl()
                , transactionLevel);
    }

    public List<SpendEntity> findSpendsByUsername(String username, int transactionLevel) {
        return transaction(connection -> {
                    return new SpendDaoJdbc(connection).findAllSpendsByUsername(username);
                }, CFG.spendJdbcUrl()
                , transactionLevel);
    }

    public void deleteSpend(SpendEntity spend, int transactionLevel) {
        transaction(connection -> {
                    new SpendDaoJdbc(connection).deleteSpend(spend);
                }, CFG.spendJdbcUrl()
                , transactionLevel);
    }

    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName, int transactionLevel) {
        return transaction(connection -> {
                    return new CategoryDaoJdbc(connection).findCategoryByUsernameAndCategoryName(username, categoryName);
                }, CFG.spendJdbcUrl()
                , transactionLevel);
    }

    public List<CategoryEntity> findAllCategoriesByUsername(String username, int transactionLevel) {
        return transaction(connection -> {
                    return new CategoryDaoJdbc(connection).findAllCategoriesByUsername(username);
                }, CFG.spendJdbcUrl()
                , transactionLevel);
    }

    public void deleteCategory(CategoryEntity category, int transactionLevel) {
        transaction(connection -> {
                    new CategoryDaoJdbc(connection).deleteCategory(category);
                }, CFG.spendJdbcUrl()
                , transactionLevel);
    }

    public CategoryEntity createCategory(CategoryEntity category, int transactionLevel) {
        return transaction(connection -> {
                    return new CategoryDaoJdbc(connection).create(category);
                }, CFG.spendJdbcUrl()
                , transactionLevel);
    }
}
