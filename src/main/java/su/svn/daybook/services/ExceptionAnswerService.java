package su.svn.daybook.services;

import io.quarkus.security.AuthenticationFailedException;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.messages.Answer;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.util.Arrays;

@ApplicationScoped
public class ExceptionAnswerService {

    private static final Logger LOG = Logger.getLogger(ExceptionAnswerService.class);

    public boolean testAuthenticationFailedException(Throwable t) {
        LOG.tracef("%s: %s [[%s]]", t.getClass().getName(), t.getMessage(), Arrays.toString(t.getStackTrace()));
        return t instanceof AuthenticationFailedException;
    }

    @Deprecated
    public boolean testException(Throwable t) {
        LOG.tracef("%s: %s [[%s]]", t.getClass().getName(), t.getMessage(), Arrays.toString(t.getStackTrace()));
        return t instanceof Exception;
    }

    public Uni<Answer> authenticationFailedUniAnswer(Throwable t) {
        LOG.errorf("%s: %s", String.valueOf(t.getClass().getName()), String.valueOf(t.getMessage()));
        return Uni.createFrom().item(
                Answer.builder()
                        .message("unauthorized")
                        .error(Response.Status.UNAUTHORIZED.getStatusCode())
                        .payload(String.valueOf(t.getMessage()))
                        .build()
        );
    }
}
