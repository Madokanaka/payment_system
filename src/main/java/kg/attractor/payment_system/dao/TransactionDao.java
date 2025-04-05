package kg.attractor.payment_system.dao;

import kg.attractor.payment_system.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TransactionDao {

    private final JdbcTemplate jdbcTemplate;


    public List<Transaction> findTransactionsByAccountId(Long accountId) {
        String query = "SELECT t.id, t.sender_account_id, t.receiver_account_id, t.amount, t.status, t.transaction_type, t.created_at, t.approved_at " +
                "FROM transactions t " +
                "WHERE t.sender_account_id = ? OR t.receiver_account_id = ? " +
                "ORDER BY t.created_at DESC";

        return jdbcTemplate.query(query, (rs, rowNum) -> new Transaction(
                rs.getLong("id"),
                rs.getLong("sender_account_id"),
                rs.getLong("receiver_account_id"),
                rs.getBigDecimal("amount"),
                rs.getString("status"),
                rs.getString("transaction_type"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("approved_at")
        ), accountId, accountId);
    }
}
