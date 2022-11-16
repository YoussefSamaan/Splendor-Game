package eu.kartoffelquadrat.lobbyservice.samplegame.controller.communcationbeans;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GameServiceRegistrationDetails {

    @Value("${lobbyservice.location}")
    private String associatedLobbyLocation;

    @Value("${gameservice.name}")
    private String gameServiceName;

    @Value("${oauth2.name}")
    private String oAuth2Name;

    @Value("${long.poll.timeout}")
    private String longPollTimeout;

    @Value("${debug.skip.registration}")
    private String debugSkipRegistration;

    public String getAssociatedLobbyLocation() {
        return associatedLobbyLocation;
    }
}
