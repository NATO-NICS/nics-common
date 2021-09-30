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
import edu.mit.ll.nics.common.entity.RemoteLog;
import edu.mit.ll.nics.common.entity.RemoteLogType;
import edu.mit.ll.nics.nicsdao.GenericDAO;
import edu.mit.ll.nics.nicsdao.QueryManager;
import edu.mit.ll.nics.nicsdao.RemoteLogDAO;
import edu.mit.ll.nics.nicsdao.mappers.RemoteLogRowMapper;
import edu.mit.ll.nics.nicsdao.mappers.RemoteLogTypeRowMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


/**
 * DAO Implementation for the RemoteLog table
 */
public class RemoteLogDAOImpl extends GenericDAO implements RemoteLogDAO {

    /**
     * Logger
     */
    private static final Logger log = LoggerFactory
            .getLogger(RemoteLogDAOImpl.class);

    /**
     * Template for executing queries on the datasource with
     */
    private NamedParameterJdbcTemplate template;


    /**
     * Initializes the template with the configured datasource
     */
    @Override
    public void initialize() {
        this.template = new NamedParameterJdbcTemplate(datasource);
    }


    /**
     * Retrieves all remotelogs in a specific workspace
     *
     * @param workspaceId of the workspace to retrieve logs for
     * @return A list of RemoteLog entities retrieved from the database
     */
    @Override
    public List<RemoteLog> getLogs(int workspaceId) {
        JoinRowCallbackHandler<RemoteLog> handler = getHandlerWith();

        QueryModel queryModel = QueryManager
                .createQuery(SADisplayConstants.REMOTELOG_TABLE)
                .selectAllFromTable().where()
                .equals(SADisplayConstants.WORKSPACE_ID);

        template.query(queryModel.toString(), new MapSqlParameterSource(
                SADisplayConstants.WORKSPACE_ID, workspaceId), handler);

        try {
            return handler.getResults();
        } catch(Exception e) {
            log.error("Exception retrieving remotelogs for workspace : {}",
                    workspaceId, e);
        }
        return null;
    }


    /**
     * Retrieves all RemoteLogType entries from the database
     *
     * @return list of RemotLogType entities in the database, if any
     */
    @Override
    public List<RemoteLogType> getLogTypes() {

        QueryModel query = QueryManager
                .createQuery(SADisplayConstants.REMOTELOGTYPE_TABLE)
                .selectAllFromTable();

        return template.query(query.toString(), query.getParameters(),
                new RemoteLogTypeRowMapper());
    }


    /**
     * Persists the given RemoteLog entity
     * <p>
     * Does not expect a remotelogid to be set on the RemoteLog object, as one is automatically given by the sequence
     * associated with the table
     * </p>
     *
     * @param workspaceId the workspace this log is associated with
     * @param log         the RemoteLog entity to persist
     * @return true if entity successfully persisted, false otherwise
     */
    @Override
    public boolean persistLog(int workspaceId, RemoteLog log) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put(SADisplayConstants.USERSESSION_ID, log.getUsersessionid());
        params.put(SADisplayConstants.USER_NAME, log.getUsername());
        params.put(SADisplayConstants.TYPE, log.getType());
        params.put(SADisplayConstants.MESSAGE, log.getMessage());
        params.put(SADisplayConstants.WORKSPACE_ID, workspaceId);
        params.put(SADisplayConstants.REMOTELOG_ERROR, log.getError());

        List<String> fields = new ArrayList<String>(params.keySet());
        QueryModel model = QueryManager
                .createQuery(SADisplayConstants.REMOTELOG_TABLE)
                .insertInto(fields);

        int ret = this.template.update(model.toString(), params);

        return (ret == 1);
    }


    /**
     * Deletes the RemoteLog entity with the specified id
     *
     * @param id the id of the RemoteLog to be deleted *
     * @return true if RemoteLog with matching ID exists and was successfully deleted, false otherwise
     */
    @Override
    public boolean deleteLog(long id) {
        try {
            MapSqlParameterSource paramMap = new MapSqlParameterSource(
                    SADisplayConstants.ID, id);

            QueryModel logQuery = QueryManager
                    .createQuery(SADisplayConstants.REMOTELOG_TABLE)
                    .deleteFromTableWhere().equals(SADisplayConstants.ID);

            return (this.template.update(logQuery.toString(), paramMap) == 1);

        } catch(Exception e) {
            log.info("Failed to delete log entry with id {}", id);
        }

        return false;
    }


    /**
     * getHandlerWith
     *
     * @param mappers optional additional mappers
     * @return a JoinRowCallbackHandler of type RemoteLog
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private JoinRowCallbackHandler<RemoteLog> getHandlerWith(
            JoinRowMapper... mappers) {
        return new JoinRowCallbackHandler(new RemoteLogRowMapper(), mappers);
    }
}
