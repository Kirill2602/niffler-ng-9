package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class SpendDaoJdbc implements SpendDao {
    private static final Config CFG = Config.getInstance();


    @Override
    public SpendEntity create(SpendEntity spend) {
        try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) " +
                        "VALUES ( ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, spend.getUsername());
            ps.setDate(2, spend.getSpendDate());
            ps.setString(3, spend.getCurrency().name());
            ps.setDouble(4, spend.getAmount());
            ps.setString(5, spend.getDescription());
            ps.setObject(6, spend.getCategory().getId());
            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            spend.setId(generatedKey);
            return spend;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SpendEntity> findAll() {
        List<SpendEntity> result = new ArrayList<>();
        try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM spend")){
            try (ResultSet rs = ps.getResultSet()){
                if (rs.next()){
                    while (rs.next()){
                        SpendEntity spend = new SpendEntity();
                        spend.setId(rs.getObject("id", UUID.class));
                        spend.setUsername(rs.getString("username"));
                        spend.setSpendDate(rs.getDate("spend_date"));
                        spend.setCurrency(rs.getObject("currency", CurrencyValues.class));
                        spend.setAmount(rs.getDouble("amount"));
                        spend.setDescription(rs.getString("description"));
                        result.add(spend);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public Optional<SpendEntity> findSpendById(UUID id) {
        try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM spend WHERE id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    SpendEntity spend = new SpendEntity();
                    spend.setId(rs.getObject("id", UUID.class));
                    spend.setUsername(rs.getString("username"));
                    spend.setSpendDate(rs.getDate("spend_date"));
                    spend.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                    spend.setAmount(rs.getDouble("amount"));
                    spend.setDescription(rs.getString("description"));
                    return Optional.of(spend);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SpendEntity> findAllSpendsByUsername(String username) {
        List<SpendEntity> spends = new ArrayList<>();
        try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM spend WHERE username = ?")) {
            ps.setString(1, username);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    SpendEntity spend = new SpendEntity();
                    spend.setId(rs.getObject("id", UUID.class));
                    spend.setUsername(rs.getString("username"));
                    spend.setSpendDate(rs.getDate("spend_date"));
                    spend.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                    spend.setAmount(rs.getDouble("amount"));
                    spend.setDescription(rs.getString("description"));
                    spends.add(spend);
                }
                return spends;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteSpend(SpendEntity spend) {
        try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "DELETE FROM spend WHERE id = ?")) {
            ps.setObject(1, spend.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
