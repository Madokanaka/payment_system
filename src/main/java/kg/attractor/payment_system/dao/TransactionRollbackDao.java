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
}
