package kg.attractor.payment_system.dao;

import kg.attractor.payment_system.model.TransactionRollback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TransactionRollbackDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int createRollback(TransactionRollback rollback) {
        String sql = "INSERT INTO transaction_rollbacks (transaction_id, rollbacked_at, amount) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, rollback.getTransactionId(), rollback.getRollbackedAt(), rollback.getAmount());
    }

    public boolean existsById(Long transactionRollbackId) {
        String sql = "SELECT COUNT(*) FROM transaction_rollbacks WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, transactionRollbackId);
        return count != null && count > 0;
    }

    public TransactionRollback findById(Long transactionRollbackId) {
        String sql = "SELECT * FROM transaction_rollbacks WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new TransactionRollback(
                    rs.getLong("id"),
                    rs.getLong("transaction_id"),
                    rs.getTimestamp("rollbacked_at"),
                    rs.getBigDecimal("amount")
            ), transactionRollbackId);
        } catch (Exception e) {
            return null;
        }
    }


}
