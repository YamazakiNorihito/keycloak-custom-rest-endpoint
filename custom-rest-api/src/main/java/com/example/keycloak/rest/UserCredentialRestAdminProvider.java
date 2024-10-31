package com.example.keycloak.rest;

import org.keycloak.models.*;
import org.keycloak.services.resources.admin.AdminEventBuilder;
import org.keycloak.services.resources.admin.ext.AdminRealmResourceProvider;
import org.keycloak.services.resources.admin.permissions.AdminPermissionEvaluator;

@jakarta.ws.rs.ext.Provider
public class UserCredentialRestAdminProvider implements AdminRealmResourceProvider {

    private final KeycloakSession session;

    public UserCredentialRestAdminProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public void close() {}

    @Override
    public Object getResource(KeycloakSession session, RealmModel realm,
            AdminPermissionEvaluator auth, AdminEventBuilder adminEvent) {
        return new UserCredentialAdminResource(this.session, auth, adminEvent);
    }
}
