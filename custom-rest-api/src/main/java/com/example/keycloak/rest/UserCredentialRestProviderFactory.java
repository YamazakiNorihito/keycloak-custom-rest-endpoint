package com.example.keycloak.rest;

import org.keycloak.Config.Scope;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resource.RealmResourceProviderFactory;

public class UserCredentialRestProviderFactory implements RealmResourceProviderFactory {

    public static final String ID = "credential-api";

    @Override
    public RealmResourceProvider create(KeycloakSession session) {
        return new UserCredentialRestProvider(session);
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void close() {}

    @Override
    public void init(Scope arg0) {}

    @Override
    public void postInit(KeycloakSessionFactory arg0) {}
}
