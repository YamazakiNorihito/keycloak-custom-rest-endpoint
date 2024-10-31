package com.example.keycloak.rest;

import java.util.List;
import java.util.stream.Collectors;
import org.jboss.logging.Logger;
import org.jboss.resteasy.spi.InternalServerErrorException;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.*;
import org.keycloak.services.resource.RealmResourceProvider;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@jakarta.ws.rs.ext.Provider
public class UserCredentialRestProvider implements RealmResourceProvider {
    private static final Logger log = Logger.getLogger(UserCredentialRestProvider.class);
    private KeycloakSession session;

    public UserCredentialRestProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public Object getResource() {
        return this;
    }

    @GET
    @Path("users/{user-id}/credentials")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CredentialModel> getUserCredentials(@PathParam("user-id") String userId) {
        log.infof("getUserCredentials() method called with user-id: %s", userId);

        try {
            final UserModel user =
                    session.users().getUserById(session.getContext().getRealm(), userId);
            if (user == null) {
                log.warnf("User with ID %s not found", userId);
                throw new NotFoundException("User not found");
            }

            final SubjectCredentialManager credentialStore = user.credentialManager();

            List<CredentialModel> credentials =
                    credentialStore.getStoredCredentialsStream().peek(cred -> {
                        // Optional: log each credential type for debugging
                        log.debugf("Credential ID: %s, Type: %s", cred.getId(), cred.getType());
                    }).collect(Collectors.toList());

            log.infof("Number of credentials retrieved for user-id %s: %d", userId,
                    credentials.size());
            return credentials;

        } catch (Exception e) {
            log.errorf("Error in getUserCredentials() method for user-id %s: %s", userId,
                    e.getMessage());
            throw new InternalServerErrorException(
                    "Error processing request for user-id " + userId);
        }
    }

    @Override
    public void close() {}


    /*
     * private final KeycloakSession session;
     * 
     * public UserCredentialRestProvider(KeycloakSession session) { this.session = session; }
     * 
     * @Override public Object getResource() { return new UserCredentialResource(session); }
     * 
     * @Override public void close() { // No action needed }
     */
}
