package kg.attractor.payment_system.dao;

import kg.attractor.payment_system.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
public class AccountDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Account> ACCOUNT_ROW_MAPPER = (rs, rowNum) -> Account.builder()
            .id(rs.getLong("id"))
            .userId(rs.getLong("user_id"))
            .currencyId(rs.getLong("currency_id"))
            .balance(rs.getBigDecimal("balance"))
            .accountNumber(rs.getString("account_number"))
            .build();

    public List<Account> findAccountsByUserId(Long userId) {
        String sql = "SELECT * FROM accounts WHERE user_id = ?";
        return jdbcTemplate.query(sql, ACCOUNT_ROW_MAPPER, userId);
    }

    public int createAccount(Long userId, Long currencyId, String accountNumber) {
        String sql = "INSERT INTO accounts (user_id, currency_id, balance, account_number) VALUES (?, ?, 0.00, ?)";
        return jdbcTemplate.update(sql, userId, currencyId, accountNumber);
    }

    public Account findAccountByAccountNumber(String accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        return jdbcTemplate.queryForObject(sql, ACCOUNT_ROW_MAPPER, accountNumber);
    }

    public boolean existsByAccountNumber(String accountNumber) {
        String sql = "SELECT COUNT(*) FROM accounts WHERE account_number = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, accountNumber);
        return count != null && count > 0;
    }
}

