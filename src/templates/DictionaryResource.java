/*
 * This file was last modified at 2022.01.11 17:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * @Name@Resource.java
 * $Id$
 */

package su.svn.daybook.resources;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.enums.ResourcePath;
import su.svn.daybook.models.domain.@Name@;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.models.AbstractService;
import su.svn.daybook.services.models.@Name@Service;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.UUID;

@Path(ResourcePath.@TABLE@)
public class @Name@Resource extends AbstractResource implements Resource<@IdType@, @Name@> {

    @GET
    @Path(ResourcePath.ID)
    @Produces("application/json")
    public Uni<Response> get(@IdType@ id, @Context UriInfo uriInfo) {
        return request(EventAddress.@TABLE@_GET, id, uriInfo);
    }

    @GET
    @Produces("application/json")
    public Uni<Response> page(@QueryParam("page") Long page, @QueryParam("limit") Short limit) {
        return requestPage(EventAddress.@TABLE@_PAGE, new PageRequest(page, limit));
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Uni<Response> post(@Name@ entry, @Context UriInfo uriInfo) {
        return request(EventAddress.@TABLE@_ADD, entry, uriInfo);
    }

    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public Uni<Response> put(@Name@ entry, @Context UriInfo uriInfo) {
        return request(EventAddress.@TABLE@_PUT, entry, uriInfo);
    }

    @DELETE
    @Path(ResourcePath.ID)
    @Produces("application/json")
    public Uni<Response> delete(@IdType@ id, @Context UriInfo uriInfo) {
        return request(EventAddress.@TABLE@_DEL, id, uriInfo);
    }

    @ServerExceptionMapper
    public RestResponse<String> exception(Throwable x) {
        return badRequest(x);
    }

    @Path(ResourcePath.@TABLE@S)
    public static class @Name@Resources implements Resources<@IdType@, @Name@> {

        @Inject
        @Name@Service service;

        @GET
        @Path("/")
        @Produces("application/json")
        public Multi<@Name@> all() {
            return getAll();
        }

        @Override
        public AbstractService<@IdType@, @Name@> getService() {
            return service;
        }
    }
}