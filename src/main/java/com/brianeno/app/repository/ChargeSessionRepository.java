/**
 * @author Brian Enochson
 * @date 01/01/2024
 */
package com.brianeno.app.repository;

import com.brianeno.app.model.ChargeSession;
import com.brianeno.app.model.mapper.ChargeSessionMapper;
import com.codahale.metrics.annotation.Timed;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@Timed
public interface ChargeSessionRepository {

    @SqlQuery("SELECT id, make, model, watt_hours FROM charge_sessions")
    @RegisterRowMapper(ChargeSessionMapper.class)
    List<ChargeSession> getSessions();

    @SqlQuery("SELECT id, make, model, watt_hours FROM charge_sessions " +
        "WHERE id = :id")
    @RegisterRowMapper(ChargeSessionMapper.class)
    ChargeSession getChargeSession(@Bind("id") Integer id);

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO charge_sessions (MAKE, MODEL, WATT_HOURS, CREATED_AT, UPDATED_AT) " +
        "VALUES (:make, :model, :wattHours, current_timestamp, current_timestamp)")
    @RegisterRowMapper(ChargeSessionMapper.class)
    ChargeSession insertNewChargeSession(@Bind("make") String make,
                                         @Bind("model") String model, @Bind("wattHours") Integer wattHours);

    @SqlUpdate("UPDATE charge_sessions SET " +
        "MAKE = :make, " +
        "MODEL = :model, " +
        "WATT_HOURS = :wattHours, " +
        "UPDATED_AT = current_timestamp " +
        "WHERE ID = :id")
    void updateChargeSession(@Bind("id") Integer id, @Bind("make") String make,
                             @Bind("model") String model, @Bind("wattHours") Integer wattHours);

    @SqlUpdate("DELETE FROM charge_sessions where id = :id")
    void removeChargeSession(@Bind("id") Integer id);
}
