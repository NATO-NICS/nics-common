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

import static edu.mit.ll.nics.common.constants.SADisplayConstants.MEDIASTREAM_ID;
import static edu.mit.ll.nics.common.constants.SADisplayConstants.MEDIASTREAM_NOT_FOUND;
import static edu.mit.ll.nics.common.constants.SADisplayConstants.MEDIASTREAM_TABLE;
import static edu.mit.ll.nics.common.constants.SADisplayConstants.MEDIASTREAM_TITLE;
import static edu.mit.ll.nics.common.constants.SADisplayConstants.MEDIASTREAM_URL;
import static edu.mit.ll.nics.common.constants.SADisplayConstants.MEDIASTREAM_WORKSPACE_TABLE;
import static edu.mit.ll.nics.common.constants.SADisplayConstants.WORKSPACE_ID;


import edu.mit.ll.jdbc.JoinRowCallbackHandler;
import edu.mit.ll.jdbc.JoinRowMapper;
import edu.mit.ll.nics.common.entity.MediaStream;
import edu.mit.ll.nics.nicsdao.GenericDAO;
import edu.mit.ll.nics.nicsdao.MediaStreamDAO;
import edu.mit.ll.nics.nicsdao.mappers.MediaStreamRowMapper;
import java.util.List;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;


/**
 * Implementation of the MediaStreamDAO Interface.
 *
 * <p>Responsible for implementing the CRUD operations on the mediastream table</p>
 */
public class MediaStreamDAOImpl extends GenericDAO implements MediaStreamDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MediaStreamDAOImpl.class);

    private JdbcTemplate template;
    private SimpleJdbcInsert insertTemplate;

    /**
     * Default constructor.
     */
    public MediaStreamDAOImpl() {
        super();
    }

    /**
     * Constructor specifying datasource object.
     *
     * @param datasource the datasource to initialize the superclass with
     */
    public MediaStreamDAOImpl(DataSource datasource) {
        super(datasource);
    }

    @Override
    public void initialize() {
        LOG.debug("Initializing mediastream dao");

        this.template = new JdbcTemplate(datasource);
        this.insertTemplate = new SimpleJdbcInsert(datasource).withTableName(MEDIASTREAM_TABLE)
                .usingGeneratedKeyColumns(MEDIASTREAM_ID);

        LOG.debug("Initialized!");
    }

    @Override
    public List<MediaStream> readMediaStreamsByWorkspaceId(int workspaceId) {
        LOG.trace("Getting media streams for workspace: {}", workspaceId);

        final String sql = String.format("select * from %s ms inner join %s mw on (ms.%s = mw.%s) where %s = ?",
                MEDIASTREAM_TABLE, MEDIASTREAM_WORKSPACE_TABLE, MEDIASTREAM_ID,
                MEDIASTREAM_ID, WORKSPACE_ID);

        JoinRowCallbackHandler<MediaStream> handler = getHandler();

        this.template.query(sql, new Object[] {workspaceId}, handler);
        List<MediaStream> res = handler.getResults();

        LOG.trace("Returning {} mediastream{} for workspaceid {}", res.size(),
                (res.size() == 1) ? "" : "s", workspaceId);

        return res;
    }

    @Override
    public List<MediaStream> findMediaStreamsByWorkspaceId(int workspaceId, String title, String url)
            throws DataAccessException {

        // Default values should be empty string for title and url, but to guard against nulls, setting
        // to empty String
        if(title == null) {
            title = "";
        }

        if(url == null) {
            url = "";
        }

        // "select * from mediastream where title=? and url = ?"

        LOG.trace("Finding media streams for workspace {}, with name {} and url {}",
                workspaceId, title, url);

        boolean useWhere = false;
        Object[] params = null;
        StringBuilder whereClause = new StringBuilder();
        if(!title.isEmpty()) {
            whereClause.append(" and ").append(MEDIASTREAM_TITLE).append("=?");
            useWhere = true;
            params = new Object[] {workspaceId, title};
        }

        if(!url.isEmpty()) {
            whereClause.append(" and ").append(MEDIASTREAM_URL).append("=?");
            useWhere = true;

            if(title.isEmpty()) {
                params = new Object[] {workspaceId, url};
            } else {
                params = new Object[] {workspaceId, title, url};
            }
        }

        String whereClauseQuery;
        if(!useWhere) {
            whereClauseQuery = "";
        } else {
            whereClauseQuery = whereClause.toString();
        }

        final String sql = String.format("select * from %s ms inner join %s mw on (ms.%s = mw.%s) where %s = ? %s",
                MEDIASTREAM_TABLE, MEDIASTREAM_WORKSPACE_TABLE, MEDIASTREAM_ID,
                MEDIASTREAM_ID, WORKSPACE_ID, whereClauseQuery);

        JoinRowCallbackHandler<MediaStream> handler = getHandler();

        if(params == null) {
            params = new Object[] {workspaceId};
        }

        this.template.query(sql, params, handler);

        List<MediaStream> res = handler.getResults();

        LOG.trace("Returning {} mediastream{} for workspaceid {}", res.size(),
                (res.size() == 1) ? "" : "s", workspaceId);

        return res;

    }

    @Override
    public MediaStream createMediaStream(MediaStream stream, int workspaceId) throws Exception {

        if(stream == null || stream.getTitle() == null || stream.getTitle().isEmpty()
                || stream.getUrl() == null || stream.getUrl().isEmpty() || workspaceId < 1) {

            LOG.trace("Invalid MediaStream or workspace value");

            throw new IllegalArgumentException("Invalid MediaStream or workspace value");
        }

        LOG.debug("inserting new stream");

        // Ensure id is unset
        stream.setMsid(-1);

        Number key;

        try {
            key = insertTemplate.executeAndReturnKey(new BeanPropertySqlParameterSource(stream));
        } catch(DuplicateKeyException e) {
            LOG.error("Error inserting {} and retrieving key: {}", MEDIASTREAM_TABLE, e.getMessage(), e);
            throw new DuplicateKeyException("Stream with title and url already exists");
        } catch(Exception e) {
            LOG.error("Unhandled exception inserting {}", MEDIASTREAM_TABLE, e);
            throw new Exception(String.format("Unhandled exception inserting %s", MEDIASTREAM_TABLE));
        }

        if(key == null) {
            LOG.debug("Failed to get generated key from insert call");
            throw new Exception("Failed to get generated key from insert call. Presume insertion failure.");
        }

        long msId = key.longValue();

        if(msId > 0) {
            stream.setMsid(msId);
            LOG.trace("Inserted new MediaStream, id: {}", msId);

            if(!createMediaStreamWorkspace(workspaceId, msId)) {
                // Failed to insert workspace join, so fallback and delete mediastream as well
                if(!deleteMediaStream(msId)) {
                    throw new Exception("Partial insertion failure. Please try again");
                }
            }

            return stream;
        }

        return null;
    }

    /**
     * Creates the MediaStream Workspace join table entry for the specified workspace ID and mediastream ID.
     *
     * @param workspaceId   the workspace ID this stream is associated with
     * @param mediaStreamId the ID of the mediastream
     * @return true if successful, false otherwise
     *
     * @throws DataAccessException when there's a dataaccess issue inserting the entry
     */
    private boolean createMediaStreamWorkspace(int workspaceId, long mediaStreamId) throws DataAccessException {
        final String mswSql = String.format("INSERT INTO %s (%s, %s) values (?, ?)",
                MEDIASTREAM_WORKSPACE_TABLE, MEDIASTREAM_ID, WORKSPACE_ID);

        int rows = this.template.update(mswSql, new Object[] {mediaStreamId, workspaceId});

        return rows == 1;
    }

    @Override
    public MediaStream updateMediaStream(MediaStream mediaStream) throws Exception {
        if(mediaStream == null || mediaStream.getMsid() <= 0) {
            LOG.debug("NOT updating {}, entity was null or ID was <= 0", MEDIASTREAM_TABLE);
            return null;
        }

        if(!exists(mediaStream.getMsid())) {
            throw new Exception(MEDIASTREAM_NOT_FOUND);
        }

        LOG.debug("Updating {}: {}", MEDIASTREAM_TABLE, mediaStream.getMsid());
        final String sql = String.format("update %s set %s = ?, %s = ? where %s = ?",
                MEDIASTREAM_TABLE, MEDIASTREAM_TITLE, MEDIASTREAM_URL, MEDIASTREAM_ID);

        int rows = this.template.update(sql, new Object[] {mediaStream.getTitle(),
                mediaStream.getUrl(), mediaStream.getMsid()});

        if(rows > 0) {
            return mediaStream;
        }

        return null;
    }

    @Override
    public boolean deleteMediaStream(long streamId) throws Exception {

        if(!exists(streamId)) {
            throw new Exception(MEDIASTREAM_NOT_FOUND);
        }

        final String sql = String.format("delete from %s where %s = ?",
                MEDIASTREAM_TABLE, MEDIASTREAM_ID);

        try {
            LOG.debug("Deleting mediastream with streamId: " + streamId);
            int rows = this.template.update(sql, streamId);
            if(rows > 0) {
                return true;
            }
        } catch(Exception e) {
            LOG.error("Exception trying to delete {}} with ID {}", MEDIASTREAM_TABLE, streamId, e);
        }

        return false;
    }


    @Override
    public boolean exists(long streamId) throws Exception {
        boolean exists;
        final String sql = String.format("select exists(select 1 from %s where %s = ?)",
                MEDIASTREAM_TABLE, MEDIASTREAM_ID);

        try {
            exists = this.template.queryForObject(sql, new Object[] {streamId}, Boolean.class);
        } catch(DataAccessException e) {
            throw new Exception("Data access error checking for existence of stream");
        }

        return exists;
    }


    /**
     * Get a MediaStreaming callback handler.
     *
     * @param mappers optional mappers
     * @return a MediaStream handler with the mappers applied, if any
     */
    private JoinRowCallbackHandler<MediaStream> getHandler(JoinRowMapper<MediaStream>... mappers) {
        return new JoinRowCallbackHandler<>(new MediaStreamRowMapper(), mappers);
    }
}
