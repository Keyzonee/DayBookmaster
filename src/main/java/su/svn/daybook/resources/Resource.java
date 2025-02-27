/*
 * This file was last modified at 2023.01.07 19:16 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Resource.java
 * $Id$
 */

package su.svn.daybook.resources;

import io.smallrye.mutiny.Uni;
import su.svn.daybook.models.Identification;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.Serializable;

public interface Resource<K extends Comparable<? extends Serializable>, V extends Identification<K>> {
    Uni<Response> get(K id, @Context UriInfo uriInfo);

    Uni<Response> page(@QueryParam("page") Long page, @QueryParam("limit") Short limit);

    Uni<Response> post(V entry, @Context UriInfo uriInfo);

    Uni<Response> put(V entry, @Context UriInfo uriInfo);

    Uni<Response> delete(K id, @Context UriInfo uriInfo);
}
