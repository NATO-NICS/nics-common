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
import edu.mit.ll.nics.common.entity.datalayer.Document;
import edu.mit.ll.nics.nicsdao.DocumentDAO;
import edu.mit.ll.nics.nicsdao.GenericDAO;
import edu.mit.ll.nics.nicsdao.QueryManager;
import edu.mit.ll.nics.nicsdao.mappers.DocumentRowMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


public class DocumentDAOImpl extends GenericDAO implements DocumentDAO {

    private NamedParameterJdbcTemplate template;

    @Override
    public void initialize() {
        this.template = new NamedParameterJdbcTemplate(datasource);
    }

    @Override
    public Document addDocument(Document doc) {

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue(SADisplayConstants.DOCUMENT_ID, UUID.randomUUID());
        map.addValue(SADisplayConstants.DISPLAY_NAME, doc.getDisplayname());
        map.addValue(SADisplayConstants.DATASOURCE_ID, doc.getDatasourceid());
        map.addValue(SADisplayConstants.FILENAME, doc.getFilename());
        map.addValue(SADisplayConstants.FILETYPE, doc.getFiletype());
        map.addValue(SADisplayConstants.FOLDER_ID, doc.getFolderid());
        map.addValue(SADisplayConstants.GLOBAL_VIEW, doc.getGlobalview());
        map.addValue(SADisplayConstants.USERSESSION_ID, doc.getUsersessionid());
        map.addValue(SADisplayConstants.DESCRIPTION, doc.getDescription());
        map.addValue(SADisplayConstants.CREATED, doc.getCreated());

        QueryModel model = QueryManager.createQuery(SADisplayConstants.DOCUMENT_TABLE)
                .insertInto(new ArrayList<String>(map.getValues().keySet()))
                .returnValue("*");

        JoinRowCallbackHandler<Document> handler = getHandlerWith();
        template.query(model.toString(), map, handler);
        return handler.getSingleResult();
    }

    public int deleteIncidentDocument(String folderId){
       QueryModel documentQuery = QueryManager.createQuery(SADisplayConstants.DOCUMENT_TABLE)
                .selectFromTable(SADisplayConstants.DOCUMENT_ID).where()
                .equals(SADisplayConstants.FOLDER_ID);

        QueryModel query = QueryManager.createQuery(SADisplayConstants.DOCUMENT_INCIDENT_TABLE)
                .deleteFromTableWhere().inAsSQL(SADisplayConstants.DOCUMENT_ID,
                        documentQuery.toString());

        int result = this.template.update(
                query.toString(),
                new MapSqlParameterSource(SADisplayConstants.FOLDER_ID, folderId));

        if(result != 0){
           return this.deleteDocument(folderId);
        }

        return -1;
    }

    public int deleteCollabroomDocument(String folderId){
        QueryModel documentQuery = QueryManager.createQuery(SADisplayConstants.DOCUMENT_TABLE)
                .selectFromTable(SADisplayConstants.DOCUMENT_ID).where()
                .equals(SADisplayConstants.FOLDER_ID);

        QueryModel query = QueryManager.createQuery(SADisplayConstants.DOCUMENT_COLLABROOM_TABLE)
                .deleteFromTableWhere().inAsSQL(SADisplayConstants.DOCUMENT_ID,
                        documentQuery.toString());

        int result = this.template.update(
                query.toString(),
                new MapSqlParameterSource(SADisplayConstants.FOLDER_ID, folderId));

        if(result != 0){
            return this.deleteDocument(folderId);
        }

        return -1;
    }

    public int deleteOrgDocument(String folderId){
        QueryModel documentQuery = QueryManager.createQuery(SADisplayConstants.DOCUMENT_TABLE)
                .selectFromTable(SADisplayConstants.DOCUMENT_ID).where()
                .equals(SADisplayConstants.FOLDER_ID);

        QueryModel query = QueryManager.createQuery(SADisplayConstants.DOCUMENT_ORG_TABLE)
                .deleteFromTableWhere().inAsSQL(SADisplayConstants.DOCUMENT_ID,
                        documentQuery.toString());

        int result = this.template.update(
                query.toString(),
                new MapSqlParameterSource(SADisplayConstants.FOLDER_ID, folderId));

        if(result != 0){
            return this.deleteDocument(folderId);
        }

        return -1;
    }

    public int setDocumentDescription(String folderId, String description){
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.DOCUMENT_TABLE)
                .update().equals(SADisplayConstants.DESCRIPTION)
                .where().equals(SADisplayConstants.FOLDER_ID);

       return this.template.update(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.FOLDER_ID, folderId)
                        .addValue(SADisplayConstants.DESCRIPTION, description));
    }

    public int setDocumentDisplayname(String folderId, String displayname){
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.DOCUMENT_TABLE)
                .update().equals(SADisplayConstants.DISPLAY_NAME)
                .where().equals(SADisplayConstants.FOLDER_ID);

        return this.template.update(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.FOLDER_ID, folderId)
                        .addValue(SADisplayConstants.DISPLAY_NAME, displayname));
    }

    private int deleteDocument(String folderId){
        QueryModel docQuery = QueryManager.createQuery(SADisplayConstants.DOCUMENT_TABLE)
                .deleteFromTableWhere().equals(SADisplayConstants.FOLDER_ID);

        return this.template.update(
                docQuery.toString(),
                new MapSqlParameterSource(SADisplayConstants.FOLDER_ID, folderId));
    }

    @Override
    public int addFeatureDocument(long featureId, String docId) {

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue(SADisplayConstants.FEATURE_ID, featureId);
        map.addValue(SADisplayConstants.DOCUMENT_ID, docId);

        QueryModel model = QueryManager.createQuery(SADisplayConstants.DOCUMENT_FEATURE_TABLE)
                .insertInto(new ArrayList<String>(map.getValues().keySet()))
                .returnValue(SADisplayConstants.DOCUMENT_FEATURE_ID);

        return template.queryForObject(model.toString(), map, Integer.class);
    }

    @Override
    public int addOrgDocument(long orgId, String docId) {

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue(SADisplayConstants.ORG_ID, orgId);
        map.addValue(SADisplayConstants.DOCUMENT_ID, docId);

        QueryModel model = QueryManager.createQuery(SADisplayConstants.DOCUMENT_ORG_TABLE)
                .insertInto(new ArrayList<String>(map.getValues().keySet()))
                .returnValue(SADisplayConstants.DOCUMENT_ORG_ID);

        return template.queryForObject(model.toString(), map, Integer.class);
    }

    @Override
    public int addIncidentDocument(long incidentId, String docId) {

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue(SADisplayConstants.INCIDENT_ID, incidentId);
        map.addValue(SADisplayConstants.DOCUMENT_ID, docId);

        QueryModel model = QueryManager.createQuery(SADisplayConstants.DOCUMENT_INCIDENT_TABLE)
                .insertInto(new ArrayList<String>(map.getValues().keySet()))
                .returnValue(SADisplayConstants.DOCUMENT_INCIDENT_ID);

        return template.queryForObject(model.toString(), map, Integer.class);
    }

    @Override
    public int addCollabroomDocument(long collabroomId, String docId) {

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue(SADisplayConstants.COLLAB_ROOM_ID, collabroomId);
        map.addValue(SADisplayConstants.DOCUMENT_ID, docId);

        QueryModel model = QueryManager.createQuery(SADisplayConstants.DOCUMENT_COLLABROOM_TABLE)
                .insertInto(new ArrayList<String>(map.getValues().keySet()))
                .returnValue(SADisplayConstants.DOCUMENT_COLLABROOM_ID);

        return template.queryForObject(model.toString(), map, Integer.class);
    }

    @Override
    public List<Document> getDocuments(String folderId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue(SADisplayConstants.FOLDER_ID, folderId);

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.DOCUMENT_TABLE)
                .selectAllFromTable()
                .where().equals(SADisplayConstants.FOLDER_ID);

        JoinRowCallbackHandler<Document> handler = this.getHandlerWith();
        template.query(queryModel.toString(), map, handler);
        return handler.getResults();
    }

    public List<String> getOrgDocumentFolderIds(int orgId, int workspaceId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue(SADisplayConstants.ORG_ID, orgId);
        map.addValue(SADisplayConstants.WORKSPACE_ID, workspaceId);

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.FOLDER_TABLE)
                .selectDistinctFromTable(SADisplayConstants.FOLDER_ID)
                .join(SADisplayConstants.DOCUMENT_TABLE).using(SADisplayConstants.FOLDER_ID)
                .join(SADisplayConstants.DOCUMENT_ORG_TABLE).using(SADisplayConstants.DOCUMENT_ID)
                .where().equals(SADisplayConstants.WORKSPACE_ID)
                .and().equals(SADisplayConstants.ORG_ID);

        return this.template.queryForList(queryModel.toString(), map, String.class);
    }

    public List<String> getIncidentDocumentFolderIds(int incidentId, int workspaceId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue(SADisplayConstants.INCIDENT_ID, incidentId);
        map.addValue(SADisplayConstants.WORKSPACE_ID, workspaceId);

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.FOLDER_TABLE)
                .selectDistinctFromTable(SADisplayConstants.FOLDER_ID)
                .join(SADisplayConstants.DOCUMENT_TABLE).using(SADisplayConstants.FOLDER_ID)
                .join(SADisplayConstants.DOCUMENT_INCIDENT_TABLE).using(SADisplayConstants.DOCUMENT_ID)
                .where().equals(SADisplayConstants.WORKSPACE_ID)
                .and().equals(SADisplayConstants.INCIDENT_ID);

        return this.template.queryForList(queryModel.toString(), map, String.class);
    }

    public List<String> getCollabroomDocumentFolderIds(int collabroomId, int workspaceId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue(SADisplayConstants.COLLAB_ROOM_ID, collabroomId);
        map.addValue(SADisplayConstants.WORKSPACE_ID, workspaceId);

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.FOLDER_TABLE)
                .selectDistinctFromTable(SADisplayConstants.FOLDER_ID)
                .join(SADisplayConstants.DOCUMENT_TABLE).using(SADisplayConstants.FOLDER_ID)
                .join(SADisplayConstants.DOCUMENT_COLLABROOM_TABLE).using(SADisplayConstants.DOCUMENT_ID)
                .where().equals(SADisplayConstants.WORKSPACE_ID)
                .and().equals(SADisplayConstants.COLLAB_ROOM_ID);

        return this.template.queryForList(queryModel.toString(), map, String.class);
    }


    /**
     * getHandlerWith
     *
     * @param mappers - optional additional mappers
     * @return JoinRowCallbackHandler<Folder>
     */
    private JoinRowCallbackHandler<Document> getHandlerWith(JoinRowMapper<?>... mappers) {
        return new JoinRowCallbackHandler<Document>(new DocumentRowMapper(), mappers);
    }

}
