package kg.attractor.payment_system.dao;

import kg.attractor.payment_system.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
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

    public List<Transaction> findAll() {
        String sql = "SELECT * FROM transactions ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Transaction(
                rs.getLong("id"),
                rs.getLong("sender_account_id"),
                rs.getLong("receiver_account_id"),
                rs.getBigDecimal("amount"),
                rs.getString("status"),
                rs.getString("transaction_type"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("approved_at")
        ));
    }

    public List<Transaction> findPendingApprovalTransactions() {
        String sql = "SELECT * FROM transactions WHERE status = 'PENDING' ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Transaction(
                rs.getLong("id"),
                rs.getLong("sender_account_id"),
                rs.getLong("receiver_account_id"),
                rs.getBigDecimal("amount"),
                rs.getString("status"),
                rs.getString("transaction_type"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("approved_at")
        ));
    }

    public boolean existsById(Long transactionId) {
        String sql = "SELECT COUNT(*) FROM transactions WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, transactionId);
        return count != null && count > 0;
    }

    public Transaction findById(Long transactionId) {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Transaction(
                    rs.getLong("id"),
                    rs.getLong("sender_account_id"),
                    rs.getLong("receiver_account_id"),
                    rs.getBigDecimal("amount"),
                    rs.getString("status"),
                    rs.getString("transaction_type"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("approved_at")
            ), transactionId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int updateTransactionStatus(Transaction transaction) {
        String sql = "UPDATE transactions SET status = ?, approved_at = ? WHERE id = ?";
        return jdbcTemplate.update(sql, transaction.getStatus(), transaction.getApprovedAt(), transaction.getId());
    }

}
