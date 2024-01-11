/**
 * @author Brian Enochson
 * @date 01/01/2024
 */
package com.brianeno.app.repository;

import com.brianeno.app.model.ChargeSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChargeSessionRepository {

    public static HashMap<Integer, ChargeSession> sessions = new HashMap<>();

    static {
        sessions.put(1, new ChargeSession(1, "Ford", "F150", 420));
        sessions.put(2, new ChargeSession(2, "Tesla", "Model S", 385));
        sessions.put(3, new ChargeSession(3, "Kia", "EV9", 405));
    }

    public static List<ChargeSession> getSessions() {
        return new ArrayList<>(sessions.values());
    }

    public static ChargeSession getChargeSession(Integer id) {
        return sessions.get(id);
    }

    public static void updateChargeSession(Integer id, ChargeSession chargeSession) {
        sessions.put(id, chargeSession);
    }

    public static void removeChargeSession(Integer id) {
        sessions.remove(id);
    }
}
