package com.example.keycloak.rest;

import org.keycloak.credential.CredentialModel;
import org.keycloak.models.*;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager.AuthResult;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

@jakarta.ws.rs.ext.Provider
public class UserCredentialResource {

    private final KeycloakSession session;

    public UserCredentialResource(KeycloakSession session) {
        this.session = session;
    }

    @GET
    @Path("users/{user-id}/credential")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CredentialModel> getUserCredentials(@PathParam("user-id") String userId) {
        final AuthResult auth = new AppAuthManager.BearerTokenAuthenticator(session).authenticate();
        if (auth == null || auth.getToken() == null) {
            throw new NotAuthorizedException("Bearer");
        }

        final UserModel find_user =
                session.users().getUserById(session.getContext().getRealm(), userId);
        final SubjectCredentialManager credentialStore = getCredentialStore(find_user);

        List<CredentialModel> credentials =
                credentialStore.getStoredCredentialsStream().collect(Collectors.toList());
        return credentials;
    }

    private SubjectCredentialManager getCredentialStore(UserModel user) {
        return user.credentialManager();
    }
}
