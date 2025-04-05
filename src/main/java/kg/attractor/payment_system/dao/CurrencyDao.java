package kg.attractor.payment_system.dao;
import kg.attractor.payment_system.model.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CurrencyDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Currency> CURRENCY_ROW_MAPPER = (rs, rowNum) -> Currency.builder()
            .id(rs.getLong("id"))
            .currencyName(rs.getString("currency_name"))
            .build();

    public Currency findCurrencyById(Long currencyId) {
        String sql = "SELECT * FROM currencies WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, CURRENCY_ROW_MAPPER, currencyId);
    }

    public Currency findCurrencyByName(String currencyName) {
        String sql = "SELECT * FROM currencies WHERE currency_name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, CURRENCY_ROW_MAPPER, currencyName);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean existsByCurrencyName(String currencyName) {
        String sql = "SELECT COUNT(*) FROM currencies WHERE currency_name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, currencyName);
        return count != null && count >0;
    }
}
