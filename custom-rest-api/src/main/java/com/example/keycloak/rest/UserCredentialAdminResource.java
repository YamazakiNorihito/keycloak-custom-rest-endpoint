package com.example.keycloak.rest;


import java.util.List;
import java.util.stream.Collectors;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.spi.InternalServerErrorException;
import org.keycloak.common.ClientConnection;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.SubjectCredentialManager;
import org.keycloak.models.UserModel;
import org.keycloak.services.resources.admin.AdminEventBuilder;
import org.keycloak.services.resources.admin.permissions.AdminPermissionEvaluator;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;

@jakarta.ws.rs.ext.Provider
public class UserCredentialAdminResource {

    protected final RealmModel realm;

    protected final AdminPermissionEvaluator auth;

    protected final AdminEventBuilder adminEvent;

    protected final ClientConnection clientConnection;


    protected final KeycloakSession session;

    protected final HttpHeaders headers;

    private static final Logger log = Logger.getLogger(UserCredentialAdminResource.class);

    public UserCredentialAdminResource(KeycloakSession session, AdminPermissionEvaluator auth,
            AdminEventBuilder adminEvent) {
        this.session = session;
        this.auth = auth;
        this.realm = session.getContext().getRealm();
        this.adminEvent = adminEvent;
        this.clientConnection = session.getContext().getConnection();
        this.headers = session.getContext().getRequestHeaders();
    }

    @GET
    @NoCache
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    @Path("users/{user-id}/credentials")
    public List<CredentialModel> getUserCredentials(@PathParam("user-id") String userId) {
        auth.realm().requireManageRealm();
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
}
