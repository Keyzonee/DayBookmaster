/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * CodifierDao.java
 * $Id$
 */

package su.svn.daybook.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import su.svn.daybook.annotations.Logged;
import su.svn.daybook.annotations.SQL;
import su.svn.daybook.domain.model.CodifierTable;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class CodifierDao extends AbstractDao<String, CodifierTable> implements DaoIface<String, CodifierTable> {

    CodifierDao() {
        super(CodifierTable.ID, r -> r.getString(CodifierTable.ID), CodifierTable::from);
    }

    @Logged
    @SQL(CodifierTable.COUNT_DICTIONARY_CODIFIER)
    public Uni<Optional<Long>> count() {
        return super.countSQL().map(Optional::ofNullable);
    }

    @Logged
    @SQL(CodifierTable.DELETE_FROM_DICTIONARY_CODIFIER_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<String>> delete(String id) {
        return super.deleteSQL(id).map(Optional::ofNullable);
    }

    @Logged
    @SQL(CodifierTable.SELECT_ALL_FROM_DICTIONARY_CODIFIER_ORDER_BY_S)
    public Multi<CodifierTable> findAll() {
        return super.findAllSQL();
    }

    @Logged
    @SQL(CodifierTable.SELECT_FROM_DICTIONARY_CODIFIER_WHERE_ID_$1)
    public Uni<Optional<CodifierTable>> findById(String id) {
        return super.findByIdSQL(id).map(Optional::ofNullable);
    }

    @Logged
    @SQL(CodifierTable.SELECT_FROM_DICTIONARY_CODIFIER_WHERE_KEY_$1)
    public Uni<Optional<CodifierTable>> findByKey(String key) {
        return super.findByKeySQL(key).map(Optional::ofNullable);
    }

    @Logged
    @SQL(CodifierTable.SELECT_FROM_DICTIONARY_CODIFIER_WHERE_VALUE_$1)
    public Multi<CodifierTable> findByValue(String value) {
        return super.findByValueSQL(value);
    }

    @Logged
    @SQL(CodifierTable.SELECT_ALL_FROM_DICTIONARY_CODIFIER_ORDER_BY_S_OFFSET_$1_LIMIT_$2)
    public Multi<CodifierTable> findRange(long offset, long limit) {
        return super.findRangeSQL(offset, limit);
    }

    @Logged
    @SQL
    public Uni<Optional<String>> insert(CodifierTable entry) {
        return super.insertSQL(entry).map(Optional::ofNullable);
    }

    @Logged
    @SQL
    public Uni<Optional<CodifierTable>> insertEntry(CodifierTable entry) {
        return super.insertSQLEntry(entry).map(Optional::ofNullable);
    }

    @Logged
    @SQL(CodifierTable.UPDATE_DICTIONARY_CODIFIER_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<String>> update(CodifierTable entry) {
        return super.updateSQL(entry).map(Optional::ofNullable);
    }

    @Logged
    @SQL(CodifierTable.UPDATE_DICTIONARY_CODIFIER_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<CodifierTable>> updateEntry(CodifierTable entry) {
        return super.updateSQLEntry(entry).map(Optional::ofNullable);
    }
}
