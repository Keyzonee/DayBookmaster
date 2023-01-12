/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SessionDao.java
 * $Id$
 */

package su.svn.daybook.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import su.svn.daybook.annotations.Logged;
import su.svn.daybook.annotations.SQL;
import su.svn.daybook.domain.model.SessionTable;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class SessionDao extends AbstractDao<UUID, SessionTable> {

    SessionDao() {
        super(SessionTable.ID, r -> r.getUUID(SessionTable.ID), SessionTable::from);
    }

    @Logged
    @SQL(SessionTable.COUNT_SECURITY_SESSION)
    public Uni<Optional<Long>> count() {
        return super.countSQL().map(Optional::ofNullable);
    }

    @Logged
    @SQL(SessionTable.DELETE_FROM_SECURITY_SESSION_WHERE_ID_$1)
    public Uni<Optional<UUID>> delete(UUID id) {
        return super.deleteSQL(id).map(Optional::ofNullable);
    }

    @Logged
    @SQL(SessionTable.SELECT_ALL_FROM_SECURITY_SESSION_ORDER_BY_ID_ASC)
    public Multi<SessionTable> findAll() {
        return super.findAllSQL();
    }

    @Logged
    @SQL(SessionTable.SELECT_FROM_SECURITY_SESSION_WHERE_ID_$1)
    public Uni<Optional<SessionTable>> findById(UUID id) {
        return super.findByIdSQL(id).map(Optional::ofNullable);
    }

    @Logged
    @SQL(SessionTable.SELECT_ALL_FROM_SECURITY_SESSION_ORDER_BY_ID_ASC_OFFSET_LIMIT)
    public Multi<SessionTable> findRange(long offset, long limit) {
        return super.findRangeSQL(offset, limit);
    }

    @Logged
    @SQL
    public Uni<Optional<UUID>> insert(SessionTable entry) {
        return super.insertSQL(entry).map(Optional::ofNullable);
    }

    @Logged
    @SQL(SessionTable.UPDATE_SECURITY_SESSION_WHERE_ID_$1)
    public Uni<Optional<UUID>> update(SessionTable entry) {
        return super.updateSQL(entry).map(Optional::ofNullable);
    }
}
