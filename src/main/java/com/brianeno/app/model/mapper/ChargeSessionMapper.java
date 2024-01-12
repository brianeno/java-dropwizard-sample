package com.brianeno.app.model.mapper;

import com.brianeno.app.model.ChargeSession;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class ChargeSessionMapper implements RowMapper<ChargeSession> {

    @Override
    public ChargeSession map(final ResultSet rs, final StatementContext ctx) throws SQLException {
        return new ChargeSession(rs.getInt("ID"),
            rs.getString("MAKE"),
            rs.getString("MODEL"),
            rs.getInt("WATT_HOURS"));
    }

    private Instant toInstant(final ResultSet rs, final String dateColumn) throws SQLException {
        final LocalDateTime date = rs.getObject(dateColumn, LocalDateTime.class);
        return date == null ? null : date.toInstant(ZoneOffset.UTC);
    }
}

