/*
 * This file was last modified at 2022.03.24 13:26 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * 175_date_2021_01_15_time_00_37_55_issues_52_dictionary_tag_label_user_name_index.sql
 * $Id$
 */

--liquibase formatted sql
--

--
--changeset a18578179:175 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/175_date_2021_01_15_time_00_37_55_issues_52_dictionary_tag_label_user_name_index.sql
--

--
CREATE INDEX IF NOT EXISTS IDX_dictionary_tag_label_user_name
    ON dictionary.tag_label (user_name);
--

--
--rollback DROP INDEX IF EXISTS dictionary.IDX_dictionary_tag_label_user_name;
