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

    public int createTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (sender_account_id, receiver_account_id, amount, status, " +
                "transaction_type, created_at, approved_at) VALUES (?, ?, ?, ?, ?, ?, ?)";

        return jdbcTemplate.update(sql,
                transaction.getSenderAccountId(),
                transaction.getReceiverAccountId(),
                transaction.getAmount(),
                transaction.getStatus(),
                transaction.getTransactionType(),
                transaction.getCreatedAt(),
                transaction.getApprovedAt());
    }
}
