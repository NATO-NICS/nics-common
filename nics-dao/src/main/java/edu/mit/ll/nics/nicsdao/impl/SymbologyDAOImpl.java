/*
 * Copyright (c) 2008-2021, Massachusetts Institute of Technology (MIT)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.mit.ll.nics.nicsdao.impl;

import edu.mit.ll.dao.QueryModel;
import edu.mit.ll.jdbc.JoinRowCallbackHandler;
import edu.mit.ll.jdbc.JoinRowMapper;
import edu.mit.ll.nics.common.constants.SADisplayConstants;
import edu.mit.ll.nics.common.entity.OrgSymbology;
import edu.mit.ll.nics.common.entity.Symbology;
import edu.mit.ll.nics.nicsdao.GenericDAO;
import edu.mit.ll.nics.nicsdao.QueryManager;
import edu.mit.ll.nics.nicsdao.SymbologyDAO;
import edu.mit.ll.nics.nicsdao.exceptions.ExceptionUtil;
import edu.mit.ll.nics.nicsdao.exceptions.JSONInsertException;
import edu.mit.ll.nics.nicsdao.mappers.OrgSymbologyRowMapper;
import edu.mit.ll.nics.nicsdao.mappers.SymbologyRowMapper;

import java.sql.SQLException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.postgresql.util.PGobject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


/**
 * Symbology DAO Implementation performs basic CRUD operations for the Symbology and related entities.
 */
public class SymbologyDAOImpl extends GenericDAO implements SymbologyDAO {

    /**
     * Logger instance
     */
    private static final Logger LOG = LoggerFactory.getLogger(SymbologyDAOImpl.class);

    /**
     * JDBC Template
     */
    private transient NamedParameterJdbcTemplate template;


    @Override
    public void initialize() {
        this.template = new NamedParameterJdbcTemplate(datasource);
    }

    @Override
    public List<Symbology> getAllSymbology(final Map<String, Object> filter) {

        final MapSqlParameterSource params = new MapSqlParameterSource();

        try {
            QueryModel query = QueryManager.createQuery(SADisplayConstants.SYMBOLOGY_TABLE)
                    .selectAllFromTable();

            int filters = 0;
            if(!filter.isEmpty()) {
                query = query.where();

                for(final Map.Entry<String, Object> entry : filter.entrySet()) {
                    if(filters > 0) {
                        query = query.and();
                    }

                    query = query.equals(entry.getKey());
                    params.addValue(entry.getKey(), entry.getValue());
                    filters++;
                }
            }

            final JoinRowCallbackHandler<Symbology> handler = getSymbologyHandlerWith();

            this.template.query(query.toString(), params, handler);

            return handler.getResults();

        } catch(DataAccessException e) {
            throw ExceptionUtil.getFriendlySanitizedException("Error fetching all Symbologies", e);
        }
    }

    @Override
    public Symbology getSymbologyById(final int symbologyId) {
        try {
            final QueryModel query = QueryManager.createQuery(SADisplayConstants.SYMBOLOGY_TABLE)
                    .selectAllFromTable().where().equals(SADisplayConstants.SYMBOLOGY_ID);

            final JoinRowCallbackHandler<Symbology> handler = getSymbologyHandlerWith();

            this.template.query(query.toString(),
                    new MapSqlParameterSource(SADisplayConstants.SYMBOLOGY_ID, symbologyId),
                    handler);

            return handler.getSingleResult();
        } catch(DataAccessException e) {
            throw ExceptionUtil.getFriendlySanitizedException("Error fetching Symbology by identifier", e);
        }
    }

    @Override
    public List<Symbology> getSymbologyByOrgId(final int orgId) {
        try {
            final QueryModel query = QueryManager.createQuery(SADisplayConstants.SYMBOLOGY_TABLE)
                    .selectAllFromTable()
                    .join(SADisplayConstants.ORG_SYMBOLOGY_TABLE).using(SADisplayConstants.SYMBOLOGY_ID)
                    .where().equals(SADisplayConstants.ORG_ID)
                    .orderBy(SADisplayConstants.SYMBOLOGY_NAME);

            final JoinRowCallbackHandler<Symbology> handler = getSymbologyHandlerWith();

            this.template.query(query.toString(),
                    new MapSqlParameterSource(SADisplayConstants.ORG_ID, orgId),
                    handler);

            return handler.getResults();
        } catch(DataAccessException e) {
            throw ExceptionUtil.getFriendlySanitizedException("Error fetching Symbology by Organization ID", e);
        }
    }

    @Override
    public Symbology createSymbology(final Symbology symbology) {

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(SADisplayConstants.SYMBOLOGY_NAME, symbology.getName());
        params.addValue(SADisplayConstants.SYMBOLOGY_DESCRIPTION, symbology.getDescription());
        params.addValue(SADisplayConstants.SYMBOLOGY_OWNER, symbology.getOwner());

        final PGobject pgJson = new PGobject();
        pgJson.setType("json");
        try {
            pgJson.setValue(symbology.getListing());
        } catch(SQLException e) {
            throw new JSONInsertException("Error setting Symbology Metadata", e);
        }
        params.addValue(SADisplayConstants.SYMBOLOGY_LISTING, pgJson);

        // TODO: can externalize as a final
        final List<String> fields = Arrays.asList(SADisplayConstants.SYMBOLOGY_NAME,
                SADisplayConstants.SYMBOLOGY_DESCRIPTION,
                SADisplayConstants.SYMBOLOGY_OWNER,
                SADisplayConstants.SYMBOLOGY_LISTING);

        final QueryModel query = QueryManager.createQuery(SADisplayConstants.SYMBOLOGY_TABLE)
                .insertInto(fields)
                .returnValue(SADisplayConstants.SYMBOLOGY_ID);

        int newSymbologyId;
        try {
            newSymbologyId = this.template.queryForObject(query.toString(), params, Integer.class);
        } catch(DataAccessException e) {
            throw ExceptionUtil.getFriendlySanitizedException("Error persisting Symbology", e);
        }

        return getSymbologyById(newSymbologyId);
    }

    @Override
    public Symbology updateSymbology(final Symbology symbology) {

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(SADisplayConstants.SYMBOLOGY_NAME, symbology.getName());
        params.addValue(SADisplayConstants.SYMBOLOGY_DESCRIPTION, symbology.getDescription());
        params.addValue(SADisplayConstants.SYMBOLOGY_OWNER, symbology.getOwner());
        final PGobject pgJson = new PGobject();
        pgJson.setType("json");
        try {
            pgJson.setValue(symbology.getListing());
        } catch(SQLException e) {
            throw new JSONInsertException("Error setting Symbology Metadata", e);
        }
        params.addValue(SADisplayConstants.SYMBOLOGY_LISTING, pgJson);
        params.addValue(SADisplayConstants.SYMBOLOGY_ID, symbology.getSymbologyid());

        // TODO: can externalize as a final
        final List<String> fields = Arrays.asList(SADisplayConstants.SYMBOLOGY_NAME,
                SADisplayConstants.SYMBOLOGY_DESCRIPTION,
                SADisplayConstants.SYMBOLOGY_OWNER,
                SADisplayConstants.SYMBOLOGY_LISTING);

        final QueryModel query = QueryManager.createQuery(SADisplayConstants.SYMBOLOGY_TABLE)
                .update(fields).where().equals(SADisplayConstants.SYMBOLOGY_ID);

        int result;
        try {
            result = this.template.update(query.toString(), params);
        } catch(DataAccessException e) {
            throw ExceptionUtil.getFriendlySanitizedException("Error updating Symbology", e);
        }

        return result == 1 ? getSymbologyById(symbology.getSymbologyid()) : null;
    }

    @Override
    public int delete(final int symbologyId) {
        final QueryModel query = QueryManager.createQuery(SADisplayConstants.SYMBOLOGY_TABLE)
                .deleteFromTableWhere().equals(SADisplayConstants.SYMBOLOGY_ID);

        try {
            return this.template.update(query.toString(),
                    new MapSqlParameterSource(SADisplayConstants.SYMBOLOGY_NAME, symbologyId));
        } catch(DataAccessException e) {
            throw ExceptionUtil.getFriendlySanitizedException("Error deleting Symbology", e);
        }
    }

    @Override
    public boolean exists(final int symbologyId) {
        return getSymbologyById(symbologyId) != null;
    }

    @Override
    public int createOrgSymbology(final int orgId, final int symbologyId) {
        final List<String> fields = Arrays.asList(SADisplayConstants.ORG_ID, SADisplayConstants.SYMBOLOGY_ID);

        final MapSqlParameterSource params = new MapSqlParameterSource(SADisplayConstants.ORG_ID, orgId);
        params.addValue(SADisplayConstants.SYMBOLOGY_ID, symbologyId);

        final QueryModel query = QueryManager.createQuery(SADisplayConstants.ORG_SYMBOLOGY_TABLE)
                .insertInto(fields);

        try {
            return template.update(query.toString(), params);
        } catch(DataAccessException e) {
            throw ExceptionUtil.getFriendlySanitizedException("Error adding Organization Symbology mapping", e);
        }
    }

    @Override
    public List<OrgSymbology> getOrgSymbologyMappings() {

        final QueryModel query = QueryManager.createQuery(SADisplayConstants.ORG_SYMBOLOGY_TABLE)
                .selectAllFromTable();

        final JoinRowCallbackHandler<OrgSymbology> handler = getOrgSymbologyHandlerWith();

        template.query(query.toString(), new MapSqlParameterSource(), handler);

        return handler.getResults();
    }

    @Override
    public int deleteOrgSymbology(final int orgId, final int symbologyId) {
        final QueryModel query = QueryManager.createQuery(SADisplayConstants.ORG_SYMBOLOGY_TABLE)
                .deleteFromTableWhere()
                    .equals(SADisplayConstants.ORG_ID).and()
                    .equals(SADisplayConstants.SYMBOLOGY_ID);

        final MapSqlParameterSource params = new MapSqlParameterSource(SADisplayConstants.ORG_ID, orgId);
        params.addValue(SADisplayConstants.SYMBOLOGY_ID, symbologyId);

        try {
            return this.template.update(query.toString(), params);
        } catch(DataAccessException e) {
            throw ExceptionUtil.getFriendlySanitizedException("Error deleting Org Symbology mapping", e);
        }
    }

    /**
     * Returns a JoinRowCallbackHandler of type Symbology for use in query results.
     *
     * @param mappers Optional row mappers of other types
     *
     * @return JoinRowCallbackHandler of type SymbologyRowMapper and any optional mappers provided
     */
    private JoinRowCallbackHandler<Symbology> getSymbologyHandlerWith(final JoinRowMapper... mappers) {
        return new JoinRowCallbackHandler(new SymbologyRowMapper(), mappers);
    }

    /**
     * Returns a JoinRowCallbackHandler of type OrgSymbology for use in query results.
     *
     * @param mappers Optional row mappers of other types
     *
     * @return JoinRowCallbackHandler of type OrgSymbologyRowMapper and any optional mappers provided
     */
    private JoinRowCallbackHandler<OrgSymbology> getOrgSymbologyHandlerWith(final JoinRowMapper... mappers) {
        return new JoinRowCallbackHandler(new OrgSymbologyRowMapper(), mappers);
    }
}