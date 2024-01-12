/**
 * @author Brian Enochson
 * @date 01/01/2024
 */
package com.brianeno.app.auth;

import lombok.Getter;

import java.security.Principal;
import java.util.Set;

@Getter
public class User implements Principal {

    private final String name;
    private final Set<String> roles;

    public User(String name) {
        this.name = name;
        this.roles = null;
    }

    public User(String name, Set<String> roles) {
        this.name = name;
        this.roles = roles;
    }

    public int getId() {
        return (int) (Math.random() * 100);
    }
}
