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

import edu.mit.ll.dao.QueryBuilder;
import edu.mit.ll.dao.QueryModel;
import edu.mit.ll.jdbc.JoinRowCallbackHandler;
import edu.mit.ll.jdbc.JoinRowMapper;
import edu.mit.ll.nics.common.constants.SADisplayConstants;
import edu.mit.ll.nics.common.entity.FormType;
import edu.mit.ll.nics.common.entity.Incident;
import edu.mit.ll.nics.common.entity.IncidentIncidentType;
import edu.mit.ll.nics.common.entity.IncidentOrg;
import edu.mit.ll.nics.common.entity.IncidentType;
import edu.mit.ll.nics.common.entity.datalayer.Folder;
import edu.mit.ll.nics.nicsdao.GenericDAO;
import edu.mit.ll.nics.nicsdao.IncidentDAO;
import edu.mit.ll.nics.nicsdao.QueryManager;
import edu.mit.ll.nics.nicsdao.mappers.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class IncidentDAOImpl extends GenericDAO implements IncidentDAO {

    private Logger LOG;

    private String TIME_CREATED_START = "createdStart";
    private String TIME_CREATED_END = "createdEnd";

    private NamedParameterJdbcTemplate template;

    @Override
    public void initialize() {
        LOG = LoggerFactory.getLogger(IncidentDAOImpl.class);
        this.template = new NamedParameterJdbcTemplate(datasource);
    }

    public int create(String incidentname, double lat, double lon, int usersessionid, int workspaceid, int parentid,
                      String description) {
        ArrayList<String> fields = new ArrayList<>();
        fields.add(SADisplayConstants.LONGITUDE);
        fields.add(SADisplayConstants.LATITUDE);
        fields.add(SADisplayConstants.INCIDENT_NAME);
        fields.add(SADisplayConstants.CREATED);
        fields.add(SADisplayConstants.ACTIVE);
        fields.add(SADisplayConstants.USERSESSION_ID);
        fields.add(SADisplayConstants.FOLDER);
        fields.add(SADisplayConstants.WORKSPACE_ID);
        fields.add(SADisplayConstants.DESCRIPTION);

        if(parentid > 0) {
            fields.add(SADisplayConstants.PARENT_INCIDENT_ID);
        }


        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE)
                .insertInto(fields)
                .returnValue(SADisplayConstants.INCIDENT_ID);

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue(SADisplayConstants.LONGITUDE, lon);
        map.addValue(SADisplayConstants.LATITUDE, lat);
        map.addValue(SADisplayConstants.INCIDENT_NAME, incidentname);
        map.addValue(SADisplayConstants.CREATED, Calendar.getInstance().getTime());
        map.addValue(SADisplayConstants.ACTIVE, true);
        map.addValue(SADisplayConstants.USERSESSION_ID, usersessionid);
        map.addValue(SADisplayConstants.FOLDER, "");
        map.addValue(SADisplayConstants.WORKSPACE_ID, workspaceid);
        map.addValue(SADisplayConstants.DESCRIPTION, description);

        if(fields.contains(SADisplayConstants.PARENT_INCIDENT_ID)) {
            map.addValue(SADisplayConstants.PARENT_INCIDENT_ID, parentid);
        }

        int incidentid = this.template.queryForObject(queryModel.toString(), map, Integer.class);

        return incidentid;
    }


    /**
     * Persists an Incident entity, and any associated IncidentTypes or IncidentOrgs if applicable
     *
     * @param incident
     * @return the newly created Incident entity if successful, null otherwise
     */
    public Incident create(Incident incident) {
        int incidentId = -1;
        Incident newIncident = null;
        try {
            MapSqlParameterSource map = new MapSqlParameterSource();
            map.addValue(SADisplayConstants.USERSESSION_ID, incident.getUsersessionid());
            map.addValue(SADisplayConstants.INCIDENT_NAME, incident.getIncidentname());
            map.addValue(SADisplayConstants.LATITUDE, incident.getLat());
            map.addValue(SADisplayConstants.LONGITUDE, incident.getLon());
            map.addValue(SADisplayConstants.DESCRIPTION, incident.getDescription());
            map.addValue(SADisplayConstants.WORKSPACE_ID, incident.getWorkspaceid());
            map.addValue(SADisplayConstants.BOUNDS, incident.getBounds());
            map.addValue(SADisplayConstants.PARENT_INCIDENT_ID, incident.getParentincidentid());

            QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE)
                    .insertInto(new ArrayList<String>(map.getValues().keySet()), SADisplayConstants.INCIDENT_ID)
                    .returnValue(SADisplayConstants.INCIDENT_ID);

            incidentId = this.template.queryForObject(queryModel.toString(), map, Integer.class);

            newIncident = getIncident(incidentId);
        } catch(Exception e) {
            LOG.error("Failed to add incident {}", incident.getIncidentname(), e);
            return null;
        }

        try {

            if(incident.getIncidentIncidenttypes() != null) {
                for(IncidentIncidentType type : incident.getIncidentIncidenttypes()) {

                    ArrayList<String> fields = new ArrayList<>();
                    fields.add(SADisplayConstants.INCIDENT_ID);
                    fields.add(SADisplayConstants.INCIDENT_TYPE_ID);

                    QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_INCIDENTTYPE_TABLE)
                            .insertInto(fields, SADisplayConstants.INCIDENT_INCIDENTTYPE_ID);

                    MapSqlParameterSource map = new MapSqlParameterSource();
                    map.addValue(SADisplayConstants.INCIDENT_ID, incidentId);
                    map.addValue(SADisplayConstants.INCIDENT_TYPE_ID, type.getIncidenttypeid());

                    this.template.update(queryModel.toString(), map);
                }
            }

        } catch(Exception e) {

            LOG.error("Failed to insert {} for Incident '{}'",
                    SADisplayConstants.INCIDENT_INCIDENTTYPE_TABLE, incident.getIncidentname(), e);

            rollbackIncident(newIncident);
            return null;
        }

        try {

            if(incident.getIncidentorgs() != null) {
                for(IncidentOrg incidentOrg : incident.getIncidentorgs()) {

                    MapSqlParameterSource map = new MapSqlParameterSource();
                    map.addValue(SADisplayConstants.USER_ID, incidentOrg.getUserid());
                    map.addValue(SADisplayConstants.ORG_ID, incidentOrg.getOrgid());
                    map.addValue(SADisplayConstants.INCIDENT_ID, incidentId);

                    QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_ORG_TABLE)
                            .insertInto(new ArrayList<String>(map.getValues().keySet()));

                    this.template.update(queryModel.toString(), map);
                }
            }

        } catch(Exception e) {
            LOG.error("Failed to add {} for Incident '{}'", SADisplayConstants.INCIDENT_ORG_TABLE,
                    incident.getIncidentname(), e);

            rollbackIncident(newIncident);
            return null;
        }

        try {
            return this.getIncident(incidentId);
        } catch(Exception e) {
            LOG.error("Could not find incident {}", incident.getIncidentname(), e);
        }

        return null;
    }

    /**
     * Utility method for deleting all traces of an incident when there was a failure in secondary resources being
     * created.
     *
     * @param incident the entity that was persisted, and has a valid incidentId
     * @see {@link IncidentDAOImpl#deleteIncidentIncidentTypes}, {@link IncidentDAOImpl#deleteIncidentOrgs}, and {@link
     * IncidentDAOImpl#deleteIncident(Incident)}
     */
    private void rollbackIncident(Incident incident) {

        if(incident == null || incident.getIncidentid() <= 0) {
            LOG.debug("Incident is null or has an invalid incidentId, unable to delete Incident");
            return;
        }

        deleteIncidentIncidentTypes(incident);
        deleteIncidentOrgs(incident);
        deleteIncident(incident);
    }

    private void deleteIncidentIncidentTypes(Incident incident) {

        try {
            QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_INCIDENTTYPE_TABLE)
                    .deleteFromTableWhere().equals(SADisplayConstants.INCIDENT_ID);

            LOG.debug("Deleting IncidentTypes for Incident '{}'", incident.getIncidentname());
            this.template.update(queryModel.toString(),
                    new MapSqlParameterSource(SADisplayConstants.INCIDENT_ID, incident.getIncidentid()));

        } catch(Exception e) {
            LOG.error("Failed to delete {} entries for Incident '{}'}",
                    SADisplayConstants.INCIDENT_INCIDENTTYPE_TABLE, incident.getIncidentname(), e);
        }
    }

    private void deleteIncidentOrgs(Incident incident) {
        try {
            QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_ORG_TABLE)
                    .deleteFromTableWhere().equals(SADisplayConstants.INCIDENT_ID);

            this.template.update(queryModel.toString(),
                    new MapSqlParameterSource(SADisplayConstants.INCIDENT_ID, incident.getIncidentid()));

        } catch(Exception e) {
            LOG.error("Failed to delete {} entries for Incident '{}'", SADisplayConstants.INCIDENT_ORG_TABLE,
                    incident.getIncidentname(), e);
        }
    }

    private void deleteIncident(Incident incident) {
        try {
            QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE)
                    .deleteFromTableWhere().equals(SADisplayConstants.INCIDENT_ID);

            this.template.update(queryModel.toString(),
                    new MapSqlParameterSource(SADisplayConstants.INCIDENT_ID, incident.getIncidentid()));

        } catch(Exception e) {
            LOG.error("Failed to delete Incident '{}'", incident.getIncidentname(), e);
        }
    }

    public int createIncidentIncidentTypes(int incidentid, List<IncidentType> types) {
        ArrayList<String> fields = new ArrayList<>();
        fields.add(SADisplayConstants.INCIDENT_TYPE_ID);
        fields.add(SADisplayConstants.INCIDENT_ID);

        int rows = 0;

        for(IncidentType incidentType : types) {
            QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_INCIDENTTYPE_TABLE)
                    .insertInto(fields);

            MapSqlParameterSource map = new MapSqlParameterSource();
            map.addValue(SADisplayConstants.INCIDENT_ID, incidentid);
            map.addValue(SADisplayConstants.INCIDENT_TYPE_ID, incidentType.getIncidentTypeId());

            rows += this.template.update(queryModel.toString(), map);
        }
        return rows;
    }

    public Incident updateIncident(int workspaceId, Incident incident) {

        try {

            QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE)
                    .update().equals(SADisplayConstants.INCIDENT_NAME).comma().equals(SADisplayConstants.DESCRIPTION)
                    .comma().equals(SADisplayConstants.PARENT_INCIDENT_ID).comma().equals(SADisplayConstants.LATITUDE)
                    .comma().equals(SADisplayConstants.LONGITUDE).where().equals(SADisplayConstants.INCIDENT_ID)
                    .returnValue("*");

            MapSqlParameterSource map =
                    new MapSqlParameterSource(SADisplayConstants.INCIDENT_ID, incident.getIncidentid());
            map.addValue(SADisplayConstants.INCIDENT_NAME, incident.getIncidentname());
            map.addValue(SADisplayConstants.DESCRIPTION, incident.getDescription());
            map.addValue(SADisplayConstants.PARENT_INCIDENT_ID, incident.getParentincidentid());
            map.addValue(SADisplayConstants.LATITUDE, incident.getLat());
            map.addValue(SADisplayConstants.LONGITUDE, incident.getLon());


            JoinRowCallbackHandler<Incident> handler = getIncidentHandlerWith();

            this.template.query(queryModel.toString(), map, handler);

        } catch(Exception e) {
            LOG.info("Failed to update incident #0", incident.getIncidentname());
            return null;
        }

        //need to delete/update incidenttypes

        try {

            QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_INCIDENTTYPE_TABLE)
                    .deleteFromTableWhere().equals(SADisplayConstants.INCIDENT_ID);

            this.template.update(queryModel.toString(),
                    new MapSqlParameterSource(SADisplayConstants.INCIDENT_ID, incident.getIncidentid()));


        } catch(Exception e) {
            LOG.info("Failed to delete incident types for incident #0", incident.getIncidentname());
            return null;
        }


        try {

            for(IncidentIncidentType type : incident.getIncidentIncidenttypes()) {

                ArrayList<String> fields = new ArrayList<String>();
                fields.add(SADisplayConstants.INCIDENT_ID);
                fields.add(SADisplayConstants.INCIDENT_TYPE_ID);

                QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_INCIDENTTYPE_TABLE)
                        .insertInto(fields, SADisplayConstants.INCIDENT_INCIDENTTYPE_ID);

                MapSqlParameterSource map = new MapSqlParameterSource();
                map.addValue(SADisplayConstants.INCIDENT_ID, incident.getIncidentid());
                map.addValue(SADisplayConstants.INCIDENT_TYPE_ID, type.getIncidenttypeid());

                this.template.update(queryModel.toString(), map);

            }
            //Return fully updated incident with all types
            return this.getIncident(incident.getIncidentid());
        } catch(Exception e) {
            LOG.info("Failed to update incident_incidenttypes for incident #0", incident.getIncidentname());

        }

        return null;
    }

    public boolean isAdmin(int workspaceId, int incidentId, String username) {
        try {
            QueryModel orgSql = QueryManager.createQuery(SADisplayConstants.USER_ORG_TABLE)
                    .selectFromTable(SADisplayConstants.ORG_ID)
                    .join(SADisplayConstants.USER_SESSION_TABLE).using(SADisplayConstants.USER_ORG_ID)
                    .join(SADisplayConstants.INCIDENT_TABLE).using(SADisplayConstants.USERSESSION_ID)
                    .where().equals(SADisplayConstants.INCIDENT_ID);


            //select systemroleid from userorg join "user" using (userid) where username='stephanie.foster@ll.mit
            // .edu' and
            //orgid = (select orgid from userorg join usersession using(userorgid) join incident using
            // (usersessionid) where incidentid=166);
            QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.USER_ORG_TABLE)
                    .selectFromTable(SADisplayConstants.SYSTEM_ROLE_ID)
                    .join(SADisplayConstants.USER_ESCAPED).using(SADisplayConstants.USER_ID)
                    .where().equals(SADisplayConstants.USER_NAME)
                    .and().equalsInnerSelect(SADisplayConstants.ORG_ID, orgSql.toString());

            int ret = this.template.queryForObject(queryModel.toString(),
                    new MapSqlParameterSource(SADisplayConstants.WORKSPACE_ID, workspaceId)
                            .addValue(SADisplayConstants.USER_NAME, username)
                            .addValue(SADisplayConstants.INCIDENT_ID, incidentId),
                    Integer.class);

            return (ret == SADisplayConstants.ADMIN_ROLE_ID);
        } catch(Exception e) {
            return false;
        }
    }

    public List<Map<String, Object>> getIncidentOrg(int workspaceId) {
        StringBuffer fields = new StringBuffer();
        fields.append(SADisplayConstants.INCIDENT_ID);
        fields.append(QueryBuilder.COMMA);
        fields.append(SADisplayConstants.ORG_ID);

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.ORG_TABLE)
                .selectFromTable(fields.toString())
                .join(SADisplayConstants.USER_ORG_TABLE).using(SADisplayConstants.ORG_ID)
                .join(SADisplayConstants.USER_SESSION_TABLE).using(SADisplayConstants.USER_ORG_ID)
                .join(SADisplayConstants.INCIDENT_TABLE).using(SADisplayConstants.USERSESSION_ID)
                .where().equals(SADisplayConstants.WORKSPACE_ID)
                .and().equals(SADisplayConstants.ACTIVE);

        MapSqlParameterSource map = new MapSqlParameterSource(SADisplayConstants.WORKSPACE_ID, workspaceId)
                .addValue(SADisplayConstants.ACTIVE, true);

        return template.queryForList(queryModel.toString(), map);
    }

    @Override
    public List<Map<String, Object>> getIncidentOrgRestricted(int workspaceId, List<Integer> orgIds) {
        StringBuffer fields = new StringBuffer();
        fields.append(SADisplayConstants.INCIDENT_ID);
        fields.append(QueryBuilder.COMMA);
        fields.append(SADisplayConstants.ORG_ID);

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.ORG_TABLE)
                .selectFromTable(fields.toString())
                .join(SADisplayConstants.USER_ORG_TABLE).using(SADisplayConstants.ORG_ID)
                .join(SADisplayConstants.USER_SESSION_TABLE).using(SADisplayConstants.USER_ORG_ID)
                .join(SADisplayConstants.INCIDENT_TABLE).using(SADisplayConstants.USERSESSION_ID)
                .where().equals(SADisplayConstants.WORKSPACE_ID)
                .and().equals(SADisplayConstants.ACTIVE)
                .and().notInAsInteger(SADisplayConstants.ORG_ID, orgIds);

        MapSqlParameterSource map = new MapSqlParameterSource(SADisplayConstants.WORKSPACE_ID, workspaceId)
                .addValue(SADisplayConstants.ACTIVE, true);

        return template.queryForList(queryModel.toString(), map);
    }

    public int getIncidentId(String name) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE)
                .selectFromTableWhere(SADisplayConstants.INCIDENT_ID)
                .equals(SADisplayConstants.INCIDENT_NAME);

        try {
	    	/*return this.template.queryForInt(queryModel.toString(),
	    			new MapSqlParameterSource(SADisplayConstants.INCIDENT_NAME, name));*/
            return this.template.queryForObject(queryModel.toString(),
                    new MapSqlParameterSource(SADisplayConstants.INCIDENT_NAME, name), Integer.class);
        } catch(Exception e) {
            LOG.info("Could not find incident id for incident #0", name);
        }
        return -1;
    }

    public List<IncidentType> getIncidentTypes() {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TYPE_TABLE)
                .selectAllFromTable().orderBy(SADisplayConstants.INCIDENT_TYPE_NAME);
        JoinRowCallbackHandler<IncidentType> handler = getIncidentTypeHandlerWith();
        template.query(queryModel.toString(), new MapSqlParameterSource(), handler);
        return handler.getResults();
    }

    public List<IncidentType> getIncidentTypes(boolean defaultType) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TYPE_TABLE)
                .selectAllFromTable().where().equals(SADisplayConstants.DEFAULT_INCIDENT_TYPE)
                .orderBy(SADisplayConstants.INCIDENT_TYPE_NAME);
        JoinRowCallbackHandler<IncidentType> handler = getIncidentTypeHandlerWith();
        template.query(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.DEFAULT_INCIDENT_TYPE, defaultType), handler);
        return handler.getResults();
    }

    /**
     * getIncidents - return all active incidents
     *
     * @return List<Incident>
     */
    public List<Incident> getIncidents(int workspaceId) {

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE).selectAllFromTable()
                .left().join(SADisplayConstants.INCIDENT_INCIDENTTYPE_TABLE).using(SADisplayConstants.INCIDENT_ID)
                .left().join(SADisplayConstants.INCIDENT_TYPE_TABLE).using(SADisplayConstants.INCIDENT_TYPE_ID)
                .where().equals(SADisplayConstants.ACTIVE)
                .and().equals(SADisplayConstants.WORKSPACE_ID)
                .orderBy(SADisplayConstants.CREATED).desc();

        JoinRowCallbackHandler<Incident> handler = getIncidentHandlerWith(
                new Incident_IncidentTypeRowMapper().attachAdditionalMapper(new IncidentTypeRowMapper()));
        template.query(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.ACTIVE, true)
                        .addValue(SADisplayConstants.WORKSPACE_ID, workspaceId), handler);
        return handler.getResults();
    }

    @Override
    public List<Incident> getIncidents(Integer workspaceId, List<Integer> orgIds) {

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE).selectAllFromTable()
                .left().join(SADisplayConstants.INCIDENT_INCIDENTTYPE_TABLE).using(SADisplayConstants.INCIDENT_ID)
                .left().join(SADisplayConstants.INCIDENT_TYPE_TABLE).using(SADisplayConstants.INCIDENT_TYPE_ID)
                .where().equals(SADisplayConstants.ACTIVE)
                .and().equals(SADisplayConstants.WORKSPACE_ID)
                .and().notIn(SADisplayConstants.INCIDENT_ID, getExcludedIncidentIdQuery(orgIds))
                .orderBy(SADisplayConstants.CREATED).desc();

        JoinRowCallbackHandler<Incident> handler = getIncidentHandlerWith(new Incident_IncidentTypeRowMapper()
                .attachAdditionalMapper(new IncidentTypeRowMapper()));

        template.query(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.ACTIVE, true)
                        .addValue(SADisplayConstants.WORKSPACE_ID, workspaceId), handler);
        return handler.getResults();
    }


    /**
     * getIncidents - return all active incidents
     *
     * @return List<Incident>
     */
    public List<Map<String, Object>> getActiveIncidents(int workspaceId, int orgId, boolean active, String folderId) {

        StringBuffer fields = new StringBuffer();
        fields.append(SADisplayConstants.INCIDENT_NAME);
        fields.append(QueryBuilder.COMMA);
        fields.append(SADisplayConstants.INCIDENT_ID);

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE)
                .selectFromTable(fields.toString())
                .join(SADisplayConstants.USER_SESSION_TABLE).using(SADisplayConstants.USERSESSION_ID)
                .join(SADisplayConstants.USER_ORG_TABLE).using(SADisplayConstants.USER_ORG_ID)
                .where().equals(SADisplayConstants.ACTIVE)
                .and().equals(SADisplayConstants.WORKSPACE_ID)
                .and().equals(SADisplayConstants.ORG_ID);
        if(folderId != null) {
            queryModel.and().equals(SADisplayConstants.FOLDER_ID);
        }
        queryModel.orderBy(SADisplayConstants.INCIDENT_CREATED).desc();

        try {
            if(folderId != null) {
                return template.queryForList(queryModel.toString(),
                        new MapSqlParameterSource(SADisplayConstants.ACTIVE, active)
                                .addValue(SADisplayConstants.WORKSPACE_ID, workspaceId)
                                .addValue(SADisplayConstants.ORG_ID, orgId)
                                .addValue(SADisplayConstants.FOLDER_ID, folderId));
            }
            return template.queryForList(queryModel.toString(),
                    new MapSqlParameterSource(SADisplayConstants.ACTIVE, active)
                            .addValue(SADisplayConstants.WORKSPACE_ID, workspaceId)
                            .addValue(SADisplayConstants.ORG_ID, orgId));
        } catch(Exception e) {
            return null;
        }
    }

    public boolean setIncidentActive(int incidentId, boolean active, String folderId) {
        List<String> values = new ArrayList<String>();
        values.add(SADisplayConstants.ACTIVE);
        values.add(SADisplayConstants.FOLDER_ID);

        MapSqlParameterSource map = new MapSqlParameterSource(SADisplayConstants.INCIDENT_ID, incidentId);
        map.addValue(SADisplayConstants.ACTIVE, active);
        map.addValue(SADisplayConstants.FOLDER_ID, folderId);

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE)
                .update(values).where().equals(SADisplayConstants.INCIDENT_ID);

        return (this.template.update(queryModel.toString(), map) == 1);
    }

    /**
     * Gets a list of Incident IDs a user has access to, taking account incidents that have been locked to one or more
     * organizations
     *
     * @param orgIds a list of orgIds the user has access to
     * @return a List of IncidentIds accessible by the user if any found, an empty list otherwise
     */
    public List<Integer> getIncidentIdsAccessibleToUser(List<Integer> orgIds) {
        List<Integer> ids = new ArrayList<Integer>();

        QueryModel incidentIds = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE)
                .selectFromTable(SADisplayConstants.INCIDENT_ID)
                .where().notIn(SADisplayConstants.INCIDENT_ID, getExcludedIncidentIdQuery(orgIds));

        // TODO: no params to map, still require an empty one?
        ids = this.template.queryForList(incidentIds.toString(), new MapSqlParameterSource(), Integer.class);

        LOG.debug("Got incidentIds accessible: {}", ids.size());

        return ids;
    }

    public List<Map<String, Object>> findIncidentsByPrefix(int workspaceId, String prefix, String name,
                                                           int incidentTypeId, boolean archived, long startDate,
                                                           long endDate,
                                                           List<Integer> orgIds) throws Exception {

        StringBuffer fields = this.getIncidentFields();

        List<Integer> incidentIds = getIncidentIdsAccessibleToUser(orgIds);

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE)
                .selectFromTable(fields.toString())
                .join(SADisplayConstants.USER_SESSION_TABLE).using(SADisplayConstants.USERSESSION_ID)
                .join(SADisplayConstants.USER_ORG_TABLE).using(SADisplayConstants.USER_ORG_ID)
                .join(SADisplayConstants.ORG_TABLE).using(SADisplayConstants.ORG_ID);


        if(incidentTypeId != -1) {
            queryModel = queryModel.join(SADisplayConstants.INCIDENT_INCIDENTTYPE_TABLE)
                    .using(SADisplayConstants.INCIDENT_ID)
                    .where().equals(SADisplayConstants.INCIDENT_TYPE_ID, incidentTypeId)
                    .and();
        } else {
            queryModel = queryModel.where();
        }

        if(name != null) {
            queryModel = queryModel.ilike(SADisplayConstants.INCIDENT_NAME).value("'%" + name + "%'");
        }

        if(startDate != -1) {
            if(incidentTypeId != -1 || name != null) {
                queryModel = queryModel.and();
            }
            Timestamp startValue = new Timestamp(startDate);
            queryModel = queryModel.greaterThanOrEquals(SADisplayConstants.INCIDENT_CREATED,
                    TIME_CREATED_START, startValue);
        }

        if(endDate != -1) {
            if(incidentTypeId != -1 || name != null || startDate != -1) {
                queryModel = queryModel.and();
            }
            Timestamp endValue = new Timestamp(endDate);
            queryModel = queryModel.lessThanOrEquals(SADisplayConstants.INCIDENT_CREATED,
                    TIME_CREATED_END, endValue);
        }

        if(incidentTypeId != -1 || name != null || startDate != -1 || endDate != -1) {
            queryModel = queryModel.and().equals(SADisplayConstants.PREFIX, prefix);
        } else {
            queryModel = queryModel.equals(SADisplayConstants.PREFIX, prefix);
        }

        queryModel = queryModel.and().equals(SADisplayConstants.ACTIVE, !archived)
                .and().equals(SADisplayConstants.WORKSPACE_ID, workspaceId)
                .and().inAsInteger(SADisplayConstants.INCIDENT_ID, incidentIds);

        System.out.println("Query: " + queryModel.toString());

        try {
            return template.queryForList(queryModel.toString(), queryModel.getParameters());
        } catch(Exception e) {
            e.printStackTrace();
            throw new Exception("There was an error searching for incidents by prefix", e);
        }
    }

    public List<Map<String, Object>> findIncidentsByName(
            int workspaceId, String name, int incidentTypeId,
            boolean archived, long startDate,
            long endDate, List<Integer> orgIds) throws Exception {

        StringBuffer fields = this.getIncidentFields();

        List<Integer> incidentIds = getIncidentIdsAccessibleToUser(orgIds);

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE)
                .selectFromTable(fields.toString());

        if(incidentTypeId != -1) {
            queryModel = queryModel.join(SADisplayConstants.INCIDENT_INCIDENTTYPE_TABLE)
                    .using(SADisplayConstants.INCIDENT_ID)
                    .where().equals(SADisplayConstants.INCIDENT_TYPE_ID, incidentTypeId)
                    .and().ilike(SADisplayConstants.INCIDENT_NAME).value("'%" + name + "%'");
        } else {
            queryModel = queryModel.where();
        }

        if(startDate != -1) {
            if(incidentTypeId != -1) {
                queryModel = queryModel.and();
            }
            Timestamp startValue = new Timestamp(startDate);
            queryModel = queryModel.greaterThanOrEquals(SADisplayConstants.INCIDENT_CREATED,
                    TIME_CREATED_START, startValue);
        }

        if(endDate != -1) {
            if(incidentTypeId != -1 || startDate != -1) {
                queryModel = queryModel.and();
            }
            Timestamp endValue = new Timestamp(endDate);
            queryModel = queryModel.lessThanOrEquals(SADisplayConstants.INCIDENT_CREATED,
                    TIME_CREATED_END, endValue);
        }

        if(incidentTypeId != -1 || startDate != -1 || endDate != -1) {
            queryModel = queryModel.and();
        }

        queryModel = queryModel.ilike(SADisplayConstants.INCIDENT_NAME).value("'%" + name + "%'")
                .and().equals(SADisplayConstants.ACTIVE, !archived)
                .and().equals(SADisplayConstants.WORKSPACE_ID, workspaceId)
                .and().inAsInteger(SADisplayConstants.INCIDENT_ID, incidentIds);

        try {
            return template.queryForList(queryModel.toString(), queryModel.getParameters());
        } catch(Exception e) {
            throw new Exception("There was an error searching for incidents by name", e);
        }
    }

    public List<Map<String, Object>> findIncidentsByIncidentTypeId(
            int workspaceId, int incidentTypeId, boolean archived,
            long startDate, long endDate, List<Integer> orgIds) throws Exception {

        StringBuffer fields = this.getIncidentFields();

        List<Integer> incidentIds = getIncidentIdsAccessibleToUser(orgIds);

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE)
                .selectFromTable(fields.toString())
                .join(SADisplayConstants.USER_SESSION_TABLE).using(SADisplayConstants.USERSESSION_ID)
                .join(SADisplayConstants.INCIDENT_INCIDENTTYPE_TABLE).using(SADisplayConstants.INCIDENT_ID)
                .where().equals(SADisplayConstants.INCIDENT_TYPE_ID, incidentTypeId)
                .and().equals(SADisplayConstants.ACTIVE, !archived)
                .and().equals(SADisplayConstants.WORKSPACE_ID, workspaceId)
                .and().inAsInteger(SADisplayConstants.INCIDENT_ID, incidentIds);

        if(startDate != -1) {
            Timestamp startValue = new Timestamp(startDate);
            queryModel = queryModel.and().greaterThanOrEquals(SADisplayConstants.INCIDENT_CREATED,
                    TIME_CREATED_START, startValue);
        }

        if(endDate != -1) {
            Timestamp endValue = new Timestamp(endDate);
            queryModel = queryModel.and().lessThanOrEquals(SADisplayConstants.INCIDENT_CREATED,
                    TIME_CREATED_END, endValue);
        }

        try {
            return template.queryForList(queryModel.toString(), queryModel.getParameters());
        } catch(Exception e) {
            e.printStackTrace();
            throw new Exception("There was an error searching for incidents by incident type", e);
        }
    }

    public List<Map<String, Object>> findIncidentsByTimeFrame(
            int workspaceId, boolean archived,
            long startDate, long endDate, List<Integer> orgIds) throws Exception {

        StringBuffer fields = this.getIncidentFields();

        List<Integer> incidentIds = getIncidentIdsAccessibleToUser(orgIds);

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE)
                .selectFromTable(fields.toString())
                .join(SADisplayConstants.USER_SESSION_TABLE).using(SADisplayConstants.USERSESSION_ID)
                .join(SADisplayConstants.INCIDENT_INCIDENTTYPE_TABLE).using(SADisplayConstants.INCIDENT_ID)
                .where().equals(SADisplayConstants.ACTIVE, !archived)
                .and().equals(SADisplayConstants.WORKSPACE_ID, workspaceId)
                .and().inAsInteger(SADisplayConstants.INCIDENT_ID, incidentIds);

        if(startDate != -1) {
            Timestamp startValue = new Timestamp(startDate);
            queryModel = queryModel.and().greaterThanOrEquals(SADisplayConstants.INCIDENT_CREATED,
                    TIME_CREATED_START, startValue);
        }

        if(endDate != -1) {
            Timestamp endValue = new Timestamp(endDate);
            queryModel = queryModel.and().lessThanOrEquals(SADisplayConstants.INCIDENT_CREATED,
                    TIME_CREATED_END, endValue);
        }

        try {
            return template.queryForList(queryModel.toString(), queryModel.getParameters());
        } catch(Exception e) {
            e.printStackTrace();
            throw new Exception("There was an error searching for incidents by incident type", e);
        }
    }

    public List<Map<String, Object>> findIncidents(int workspaceId, boolean archived,
                                                   List<Integer> orgIds) throws Exception {

        StringBuffer fields = this.getIncidentFields();

        List<Integer> incidentIds = getIncidentIdsAccessibleToUser(orgIds);

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE)
                .selectFromTable(fields.toString())
                .join(SADisplayConstants.USER_SESSION_TABLE).using(SADisplayConstants.USERSESSION_ID)
                .join(SADisplayConstants.INCIDENT_INCIDENTTYPE_TABLE).using(SADisplayConstants.INCIDENT_ID)
                .where().equals(SADisplayConstants.ACTIVE, !archived)
                .and().equals(SADisplayConstants.WORKSPACE_ID, workspaceId)
                .and().inAsInteger(SADisplayConstants.INCIDENT_ID, incidentIds);

        try {
            return template.queryForList(queryModel.toString(), queryModel.getParameters());
        } catch(Exception e) {
            e.printStackTrace();
            throw new Exception("There was an error searching for incidents by incident type", e);
        }
    }

    /**
     * getIncidentsAndChildren - return all active incidents and there children
     *
     * @return List<Incident>
     */
    public List<Incident> getIncidentsTree(int workspaceId) {

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE).selectAllFromTable()
                .left().join(SADisplayConstants.INCIDENT_INCIDENTTYPE_TABLE).using(SADisplayConstants.INCIDENT_ID)
                .left().join(SADisplayConstants.INCIDENT_TYPE_TABLE).using(SADisplayConstants.INCIDENT_TYPE_ID)
                .where().equals(SADisplayConstants.ACTIVE)
                .and().equals(SADisplayConstants.WORKSPACE_ID).and().isNull(SADisplayConstants.PARENT_INCIDENT_ID)
                .orderBy(SADisplayConstants.CREATED).desc();

        JoinRowCallbackHandler<Incident> handler = getIncidentHandlerWith(
                new Incident_IncidentTypeRowMapper().attachAdditionalMapper(new IncidentTypeRowMapper()));
        template.query(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.ACTIVE, true)
                        .addValue(SADisplayConstants.WORKSPACE_ID, workspaceId), handler);
        return getIncidentsTree(workspaceId, handler.getResults());
    }

    public List<Incident> getIncidentsTreeRestricted(int workspaceId, List<Integer> orgIds) {

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE).selectAllFromTable()
                .left().join(SADisplayConstants.INCIDENT_INCIDENTTYPE_TABLE).using(SADisplayConstants.INCIDENT_ID)
                .left().join(SADisplayConstants.INCIDENT_TYPE_TABLE).using(SADisplayConstants.INCIDENT_TYPE_ID)
                .where().equals(SADisplayConstants.ACTIVE)
                .and().equals(SADisplayConstants.WORKSPACE_ID).and().isNull(SADisplayConstants.PARENT_INCIDENT_ID)
                .and().notIn(SADisplayConstants.INCIDENT_ID, getExcludedIncidentIdQuery(orgIds))
                .orderBy(SADisplayConstants.CREATED).desc();

        JoinRowCallbackHandler<Incident> handler = getIncidentHandlerWith(
                new Incident_IncidentTypeRowMapper().attachAdditionalMapper(new IncidentTypeRowMapper()));
        template.query(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.ACTIVE, true)
                        .addValue(SADisplayConstants.WORKSPACE_ID, workspaceId), handler);
        return getIncidentsTreeRestricted(workspaceId, handler.getResults(), orgIds);
    }

    /**
     * getIncidents - return all active incidents
     *
     * @return List<Incident>
     */
    public List<Incident> getIncidents() {

        if(this.template == null) {
            this.initialize();
        }

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE).selectAllFromTable()
                .left().join(SADisplayConstants.INCIDENT_INCIDENTTYPE_TABLE).using(SADisplayConstants.INCIDENT_ID)
                .left().join(SADisplayConstants.INCIDENT_TYPE_TABLE).using(SADisplayConstants.INCIDENT_TYPE_ID)
                .where().equals(SADisplayConstants.ACTIVE)
                .orderBy(SADisplayConstants.CREATED).desc();

        JoinRowCallbackHandler<Incident> handler = getIncidentHandlerWith(
                new Incident_IncidentTypeRowMapper().attachAdditionalMapper(new IncidentTypeRowMapper()));
        template.query(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.ACTIVE, true), handler);
        return handler.getResults();
    }


    /**
     * getIncidents - return all active incidents
     *
     * @return List<Incident>
     */
    public List<Map<String, Object>> getArchivedIncidentNames(String prefix, int workspaceid) {

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE)
                .selectFromTableWhere(SADisplayConstants.INCIDENT_NAME + "," + SADisplayConstants.INCIDENT_ID + "," +
                        SADisplayConstants.PARENT_INCIDENT_ID)
                .equals(SADisplayConstants.FOLDER)
                .and().equals(SADisplayConstants.WORKSPACE_ID);

        return this.template.queryForList(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.FOLDER, SADisplayConstants.ARCHIVED + prefix)
                        .addValue(SADisplayConstants.WORKSPACE_ID, workspaceid));
    }

    @Deprecated
    public List<Incident> getNonArchivedIncidents(int workspaceid) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE)
                .selectAllFromTableWhere()
                .equals(SADisplayConstants.ACTIVE)
                .and().equals(SADisplayConstants.FOLDER)
                .orderBy(SADisplayConstants.CREATED).desc();

        JoinRowCallbackHandler<Incident> handler = getIncidentHandlerWith(
                new Incident_IncidentTypeRowMapper().attachAdditionalMapper(new IncidentTypeRowMapper()));
        template.query(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.ACTIVE, true)
                        .addValue(SADisplayConstants.FOLDER, ""), handler);
        return handler.getResults();
    }

    @Deprecated
    public List<Incident> getIncidentsByName(List<String> names, int workspaceid) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE)
                .selectAllFromTableWhere()
                .inAsString(SADisplayConstants.INCIDENT_NAME, names)
                .and().equals(SADisplayConstants.WORKSPACE_ID);

        JoinRowCallbackHandler<Incident> handler = getIncidentHandlerWith(
                new Incident_IncidentTypeRowMapper().attachAdditionalMapper(new IncidentTypeRowMapper()));
        template.query(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.WORKSPACE_ID, workspaceid), handler);
        return handler.getResults();

    }

    /**
     * getArchivedIncidents
     *
     * @return List<String> - list of incident names that have been archived
     */
    @Deprecated
    public List<Map<String, Object>> getActiveIncidentNames(int orgid, int workspaceid) {

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE)
                .selectFromTable(SADisplayConstants.INCIDENT_NAME + "," + SADisplayConstants.INCIDENT_ID + "," +
                        SADisplayConstants.PARENT_INCIDENT_ID)
                .join(SADisplayConstants.USER_SESSION_TABLE).using(SADisplayConstants.USERSESSION_ID)
                .join(SADisplayConstants.USER_ORG_TABLE).using(SADisplayConstants.USER_ORG_ID)
                .join(SADisplayConstants.ORG_TABLE).using(SADisplayConstants.ORG_ID)
                .where().equals(SADisplayConstants.ORG_ID).and().equals(SADisplayConstants.FOLDER)
                .and().notEqual(SADisplayConstants.INCIDENT_ID)
                .and().equals(SADisplayConstants.WORKSPACE_ID); //don't include the NoIncident row

        return this.template.queryForList(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.FOLDER, "")
                        .addValue(SADisplayConstants.ORG_ID, orgid)
                        .addValue(SADisplayConstants.INCIDENT_ID, 0)
                        .addValue(SADisplayConstants.WORKSPACE_ID, workspaceid));
    }

    /*
    public List<Incident> getIncidentsAccessibleByUser(int workspaceId, long accessibleByUserId) {
        List<Incident> incidents = new ArrayList<Incident>();



        return incidents;
    }*/
    @Deprecated
    public void updateIncidentFolder(List<String> incidentNames, String folder, int workspaceid) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE)
                .update().equals(SADisplayConstants.FOLDER).where()
                .inAsString(SADisplayConstants.INCIDENT_NAME, incidentNames)
                .and().equals(SADisplayConstants.WORKSPACE_ID);

        this.template.update(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.FOLDER, folder)
                        .addValue(SADisplayConstants.WORKSPACE_ID, workspaceid));
    }

    @Deprecated
    public List<String> getChildIncidentNames(List<String> incidentNames, int workspaceid) {
        QueryModel incidentQuery = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE)
                .selectFromTableWhere(SADisplayConstants.INCIDENT_ID)
                .inAsString(SADisplayConstants.INCIDENT_NAME, incidentNames)
                .and().equals(SADisplayConstants.WORKSPACE_ID);

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE)
                .selectFromTableWhere(SADisplayConstants.INCIDENT_NAME)
                .inAsSQL(SADisplayConstants.PARENT_INCIDENT_ID, incidentQuery.toString());

        return this.template.queryForList(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.WORKSPACE_ID, workspaceid), String.class);
    }

    @Deprecated
    public List<String> getParentIncidentNames(List<String> incidentNames, int workspaceid) {
        QueryModel incidentQuery = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE)
                .selectFromTableWhere(SADisplayConstants.PARENT_INCIDENT_ID)
                .inAsString(SADisplayConstants.INCIDENT_NAME, incidentNames)
                .and().equals(SADisplayConstants.WORKSPACE_ID);

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE)
                .selectFromTableWhere(SADisplayConstants.INCIDENT_NAME)
                .inAsSQL(SADisplayConstants.INCIDENT_ID, incidentQuery.toString());

        return this.template.queryForList(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.WORKSPACE_ID, workspaceid), String.class);
    }

    public List<Map<String, Object>> getIncidentMapAdmins(int incidentid, String roomname) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.CONTACT_TABLE)
                .selectFromTable(SADisplayConstants.VALUE + "," + SADisplayConstants.FIRSTNAME + "," +
                        SADisplayConstants.LASTNAME)
                .join(SADisplayConstants.USER_ESCAPED).using(SADisplayConstants.USER_ID)
                .join(SADisplayConstants.COLLAB_ROOM_PERMISSION_TABLE).using(SADisplayConstants.USER_ID)
                .join(SADisplayConstants.COLLAB_ROOM_TABLE).using(SADisplayConstants.COLLAB_ROOM_ID)
                .join(SADisplayConstants.INCIDENT_TABLE).using(SADisplayConstants.INCIDENT_ID)
                .where().equals(SADisplayConstants.INCIDENT_ID)
                .and().equals(SADisplayConstants.COLLABROOM_AND_NAME)
                .and().equals(SADisplayConstants.CONTACT_TYPE_ID)
                .and().equals(SADisplayConstants.SYSTEM_ROLE_ID);

        try {
            return this.template.queryForList(queryModel.toString(),
                    new MapSqlParameterSource(SADisplayConstants.INCIDENT_ID, incidentid)
                            .addValue(SADisplayConstants.COLLABROOM_AND_NAME, roomname)
                            .addValue(SADisplayConstants.CONTACT_TYPE_ID, SADisplayConstants.EMAIL_TYPE_ID)
                            .addValue(SADisplayConstants.SYSTEM_ROLE_ID, SADisplayConstants.ADMIN_ROLE_ID));
        } catch(Exception e) {
            LOG.info("Error retrieving incident map admins: " + e.getMessage());
            return null;
        }
    }

    public int setIncidentCenter(String incidentname) {
        QueryModel query = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE)
                .update().equals("lat", "#{incident.lat}").comma().equals("lon", "#{incident.lon}")
                .where().equals(SADisplayConstants.INCIDENT_NAME);

        return this.template
                .update(query.toString(), new MapSqlParameterSource(SADisplayConstants.INCIDENT_NAME, incidentname));
    }

    public Incident getIncident(int incidentid) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE).selectAllFromTable()
                .left().join(SADisplayConstants.INCIDENT_INCIDENTTYPE_TABLE).using(SADisplayConstants.INCIDENT_ID)
                .left().join(SADisplayConstants.INCIDENT_TYPE_TABLE).using(SADisplayConstants.INCIDENT_TYPE_ID)
                .where().equals(SADisplayConstants.INCIDENT_ID);

        JoinRowCallbackHandler<Incident> handler = getIncidentHandlerWith(new Incident_IncidentTypeRowMapper()
                .attachAdditionalMapper(new IncidentTypeRowMapper()));

        this.template
                .query(queryModel.toString(), new MapSqlParameterSource(SADisplayConstants.INCIDENT_ID, incidentid),
                        handler);

        try {
            return handler.getSingleResult();
        } catch(Exception e) {
            LOG.info("No Incident was found with incident id #0", incidentid);
        }
        return null;
    }

    public Incident getIncidentByName(String incidentname, int workspaceId) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE)
                .selectAllFromTableWhere().equals(SADisplayConstants.INCIDENT_NAME)
                .and().equals(SADisplayConstants.WORKSPACE_ID);

        JoinRowCallbackHandler<Incident> handler = getIncidentHandlerWith();

        this.template.query(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.INCIDENT_NAME, incidentname)
                        .addValue(SADisplayConstants.WORKSPACE_ID, workspaceId), handler);

        try {
            return handler.getSingleResult(); //Incident name is unique?
        } catch(Exception e) {
            LOG.info("Error retrieving incident with name #0: #1", incidentname, e.getMessage());
        }
        return null;
    }

    @Deprecated
    public List<Incident> getParentIncidents(int workspaceId) {
        QueryModel incidentQuery = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE)
                .selectAllFromTableWhere().isNull(SADisplayConstants.PARENT_INCIDENT_ID)
                .and()
                .equals(SADisplayConstants.ACTIVE)
                .and().equals(SADisplayConstants.FOLDER)
                .and().equals(SADisplayConstants.WORKSPACE_ID);

        JoinRowCallbackHandler<Incident> handler = getIncidentHandlerWith();

        this.template.query(incidentQuery.toString(),
                new MapSqlParameterSource(SADisplayConstants.WORKSPACE_ID, workspaceId)
                        .addValue(SADisplayConstants.ACTIVE, true)
                        .addValue(SADisplayConstants.FOLDER, ""), handler);

        try {
            return handler.getResults();
        } catch(Exception e) {
            LOG.info("Error getting Incident parents for workspace #0", workspaceId);
        }
        return new ArrayList(); //return empty list

    }

    @Deprecated
    public List<Incident> getChildIncidents(int parentId) {
        QueryModel incidentQuery = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE)
                .selectAllFromTableWhere()
                .equals(SADisplayConstants.PARENT_INCIDENT_ID);

        JoinRowCallbackHandler<Incident> handler = getIncidentHandlerWith();

        this.template.query(incidentQuery.toString(),
                new MapSqlParameterSource(SADisplayConstants.PARENT_INCIDENT_ID, parentId), handler);

        try {
            return handler.getResults();
        } catch(Exception e) {
            LOG.info("Error getting Incident children for parentid #0", parentId);
        }
        return new ArrayList(); //return empty list

    }

    /**
     * Query for the number of incidents associated with the specified workspace
     *
     * @param workspaceId ID specifying workspace to filter incident count by
     * @return Number of incidents associated with the specified workspace if successful, -1 otherwise
     */
    public int getIncidentCount(int workspaceId) {
        int count = -1;
        try {

            count = template.queryForObject("select count(*) from incident where workspaceid=:workspaceid",
                    new MapSqlParameterSource(SADisplayConstants.WORKSPACE_ID, workspaceId), Integer.class);
        } catch(Exception e) {
            LOG.error("Exception querying for number of incidents in workspace with ID: " + workspaceId +
                    ": " + e.getMessage());
        }

        return count;
    }

    /**
     * getIncidentsTree
     *
     * @param parents parent incidents
     * @return Setr<Incident>
     */

    private List<Incident> getIncidentsTree(int workspaceId, List<Incident> parents) {

        List<Incident> incidents = new ArrayList<Incident>();

        for(int i = 0; i < parents.size(); i++) {

            Incident newIncident = parents.get(i);

            QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE).selectAllFromTable()
                    .left().join(SADisplayConstants.INCIDENT_INCIDENTTYPE_TABLE).using(SADisplayConstants.INCIDENT_ID)
                    .left().join(SADisplayConstants.INCIDENT_TYPE_TABLE).using(SADisplayConstants.INCIDENT_TYPE_ID)
                    .where().equals(SADisplayConstants.ACTIVE)
                    .and().equals(SADisplayConstants.WORKSPACE_ID).and().equals(SADisplayConstants.PARENT_INCIDENT_ID)
                    .orderBy(SADisplayConstants.CREATED).desc();

            JoinRowCallbackHandler<Incident> handler = getIncidentHandlerWith(
                    new Incident_IncidentTypeRowMapper().attachAdditionalMapper(new IncidentTypeRowMapper()));
            template.query(queryModel.toString(),
                    new MapSqlParameterSource(SADisplayConstants.ACTIVE, true)
                            .addValue(SADisplayConstants.WORKSPACE_ID, workspaceId)
                            .addValue(SADisplayConstants.PARENT_INCIDENT_ID, parents.get(i).getIncidentid()), handler);

            List<Incident> currentChildren = handler.getResults();

            if(currentChildren.size() > 0) {
                getIncidentsTree(workspaceId, currentChildren);
                newIncident.setChildren(currentChildren);
                newIncident.setLeaf(false);
            }

            incidents.add(newIncident);
        }

        return incidents;
    }

    /**
     * Helper method for getting a query String that returns a list of Incidents to be EXCLUDED in another query.
     * Usually used in a ".and().notIn(SADisplayConstants.INCIDENT_ID, STRING)" clause at the end of another query.
     *
     * @param orgIds the list of orgIds that are being checked for permission to see incidents
     * @return a query String
     */
    private String getExcludedIncidentIdQuery(List<Integer> orgIds) {
        // Incident IDs that are locked down to orgId
        QueryModel hasAccessIncidentIds = QueryManager.createQuery(SADisplayConstants.INCIDENT_ORG_TABLE)
                .selectDistinctFromTable(SADisplayConstants.INCIDENT_ID).where()
                .inAsInteger(SADisplayConstants.ORG_ID, orgIds);

        // Incident IDs that are locked down, minus any from above they have explicit permission on
        QueryModel excludeIdQuery = QueryManager.createQuery(SADisplayConstants.INCIDENT_ORG_TABLE)
                .selectDistinctFromTable(SADisplayConstants.INCIDENT_ID).where()
                .notInAsInteger(SADisplayConstants.ORG_ID, orgIds)
                .and().notIn(SADisplayConstants.INCIDENT_ID, hasAccessIncidentIds.toString());

        return excludeIdQuery.toString();
    }

    private List<Incident> getIncidentsTreeRestricted(int workspaceId, List<Incident> parents, List<Integer> orgIds) {

        List<Incident> incidents = new ArrayList<Incident>();

        for(int i = 0; i < parents.size(); i++) {

            Incident newIncident = parents.get(i);

            QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_TABLE).selectAllFromTable()
                    .left().join(SADisplayConstants.INCIDENT_INCIDENTTYPE_TABLE).using(SADisplayConstants.INCIDENT_ID)
                    .left().join(SADisplayConstants.INCIDENT_TYPE_TABLE).using(SADisplayConstants.INCIDENT_TYPE_ID)
                    .where().equals(SADisplayConstants.ACTIVE)
                    .and().equals(SADisplayConstants.WORKSPACE_ID).and().equals(SADisplayConstants.PARENT_INCIDENT_ID)
                    .and().notIn(SADisplayConstants.INCIDENT_ID, getExcludedIncidentIdQuery(orgIds))
                    .orderBy(SADisplayConstants.CREATED).desc();

            JoinRowCallbackHandler<Incident> handler = getIncidentHandlerWith(
                    new Incident_IncidentTypeRowMapper().attachAdditionalMapper(new IncidentTypeRowMapper()));
            template.query(queryModel.toString(),
                    new MapSqlParameterSource(SADisplayConstants.ACTIVE, true)
                            .addValue(SADisplayConstants.WORKSPACE_ID, workspaceId)
                            .addValue(SADisplayConstants.PARENT_INCIDENT_ID, parents.get(i).getIncidentid()), handler);

            List<Incident> currentChildren = handler.getResults();

            if(currentChildren.size() > 0) {
                getIncidentsTreeRestricted(workspaceId, currentChildren, orgIds);
                newIncident.setChildren(currentChildren);
                newIncident.setLeaf(false);
            }

            incidents.add(newIncident);
        }

        return incidents;
    }

    @Override
    public List<Integer> getIncidentOrgIds(Integer incidentId) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_ORG_TABLE)
                .selectFromTable(SADisplayConstants.ORG_ID)
                .where().equals(SADisplayConstants.INCIDENT_ID);

        LOG.trace("Querying for orgIds on an incident: {}", queryModel.toString());

        Map<String, Object> params = new HashMap<String, Object>();
        params.put(SADisplayConstants.INCIDENT_ID, incidentId);


        List<Integer> results = new ArrayList<Integer>();
        try {
            results = this.template.queryForList(queryModel.toString(), params, Integer.class);
            LOG.debug("Got {} results for incidentId {}", results.size(), incidentId);
        } catch(DataAccessException e) {
            LOG.error("Exception querying for incidentorg orgids for incidentid {}", incidentId, e);
        }

        return results;
    }

    @Override
    public List<IncidentOrg> getIncidentOrgs(Integer incidentId) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_ORG_TABLE)
                .selectAllFromTable().where().
                        equals(SADisplayConstants.INCIDENT_ID);

        LOG.trace("Querying for incidentorgs on incidentid {}: {}", incidentId, queryModel.toString());

        JoinRowCallbackHandler<IncidentOrg> handler = getIncidentOrgHandlerWith();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put(SADisplayConstants.INCIDENT_ID, incidentId);

        this.template.query(queryModel.toString(), params, handler);
        List<IncidentOrg> results = handler.getResults();
        LOG.debug("Got {} results for incidentId {}", results.size(), incidentId);

        return results;
    }

    @Override
    public Integer getOwningOrgId(Integer incidentId) throws DataAccessException {
        QueryModel model = QueryManager.createQuery(SADisplayConstants.USER_ORG_TABLE)
                .selectFromTable(SADisplayConstants.ORG_ID)
                .left().join(SADisplayConstants.USER_SESSION_TABLE).using(SADisplayConstants.USER_ORG_ID)
                .left().join(SADisplayConstants.INCIDENT_TABLE).using(SADisplayConstants.USERSESSION_ID)
                .where().equals(SADisplayConstants.INCIDENT_ID);

        return this.template.queryForObject(model.toString(),
                new MapSqlParameterSource(SADisplayConstants.INCIDENT_ID, incidentId), Integer.class);
    }

    @Override
    @Deprecated
    public List<Integer> getIncidentOrgIncidentIdsNotInOrg(Integer orgId) throws DataAccessException {
        QueryModel model = QueryManager.createQuery(SADisplayConstants.INCIDENT_ORG_TABLE)
                .selectDistinctFromTable(SADisplayConstants.INCIDENT_ID)
                .where().notEqual(SADisplayConstants.ORG_ID);

        LOG.debug("getIncidentOrgIncidentIdsNotInOrg: {}", model.toString());

        List<Integer> incidentIds = this.template.queryForList(model.toString(),
                new MapSqlParameterSource(SADisplayConstants.ORG_ID, orgId), Integer.class);

        if(incidentIds != null) {
            LOG.debug("Found {} incidentIds NOT belonging to orgId {}", incidentIds.size(), orgId);
        } else {
            incidentIds = new ArrayList<Integer>();
        }

        return incidentIds;
    }

    @Override
    public Map<String, Object> addIncidentOrgs(Integer workspaceId, Integer incidentId,
                                               String username, Integer userId, List<IncidentOrg> incidentOrgs)
            throws DataAccessException {

        /* TODO: Just restricting in API for now. You also need to check for superuser here, since
           TODO: you may not be an admin, but be a superuser, and since you're not an admin, you won't get by
		if(!isAdmin(workspaceId, incidentId, username) ) {
            log.warn("user {} not authorized to add orgs to incident {}", userId, incidentId);

            // TODO: create unauthorized exception? Does spring jdbc not have one?
            throw new Exception(String.format("User %s not authorized to add orgs to incidentid %d",
                    username, incidentId));
        }*/

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("incidentId", incidentId);
        List<Integer> success = new ArrayList<Integer>();
        List<Integer> fail = new ArrayList<Integer>();
        List<String> messages = new ArrayList<String>();
        // TODO: maybe make messages just a string, and append messages with line breaks?

        List<String> fields = Arrays.asList(
                SADisplayConstants.ORG_ID,
                SADisplayConstants.INCIDENT_ID,
                SADisplayConstants.USER_ID);

        QueryModel model = QueryManager.createQuery(
                SADisplayConstants.INCIDENT_ORG_TABLE).insertInto(fields);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put(SADisplayConstants.INCIDENT_ID, incidentId);
        params.put(SADisplayConstants.USER_ID, userId);
        int result = -1;
        for(IncidentOrg incidentOrg : incidentOrgs) {

            if(incidentId == incidentOrg.getIncidentid()) {

                params.put(SADisplayConstants.ORG_ID, incidentOrg.getOrgid());

                try {
                    result = this.template.update(model.toString(), params);
                    if(result == 1) {
                        success.add(incidentOrg.getOrgid());
                    } else {
                        fail.add(incidentOrg.getOrgid());
                    }

                } catch(DuplicateKeyException e) {
                    // TODO: do we really want to call it a success if it was already there?
                    messages.add("orgId " + incidentOrg.getOrgid() + " already exists");
                    success.add(incidentOrg.getOrgid());
                    LOG.warn("Mapping for orgId already exists on Incident {}", incidentOrg.getOrgid(), incidentId);
                } catch(DataAccessException e) {
                    messages.add("Exception on orgId " + incidentOrg.getOrgid() + ": " + e.getMessage());
                    fail.add(incidentOrg.getOrgid());
                    LOG.error("Exception adding incidentOrg {} to incidentId {}", incidentOrg, incidentId, e);
                }
            } else {
                fail.add(incidentOrg.getOrgid());
                LOG.warn("orgid {} not being added because it doesn't match incoming incidentid: {}",
                        incidentOrg.getOrgid(), incidentId);
            }
        }

        LOG.debug("addIncidentOrgs successfully added {} orgId(s) and failed on {}", success.size(), fail.size());

        resultMap.put("success", success);
        resultMap.put("fail", fail);
        resultMap.put("messages", messages);

        return resultMap;
    }

    /*@Override
    @Deprecated
    public int updateIncidentOrgs(Integer workspaceId, Integer incidentId, String username,
                                  Integer userId, List<IncidentOrg> incidentOrgs) {
        int numChanges;

        // For attribution records:
        log.info("UserId({}) is attempting to update IncidentId({}) with the following incidentOrgs: {}",
				userId, incidentId, Arrays.toString(incidentOrgs.toArray()));

        // TODO: Just restricting in API for now. You also need to check for superuser here, since
        //   TODO: you may not be an admin, but be a superuser, and since you're not an admin, you won't get by
		//if(!isUserInOrgOwningIncident(incidentId, userId)) {
        //    log.warn("user {} not authorized to add orgs to incident {}", userId, incidentId);
        //    return -1; // TODO: should throw proper exception to get info back to caller
        //}

        // TODO: Need to go through and delete any entries that aren't in this list, but
        //        won't that impact other orgs that may not want impacted?


        List<Integer> orgIds = new ArrayList<Integer>();
        for(IncidentOrg incidentOrg : incidentOrgs) {
        	// Only add if incidentid matches
            if(incidentId == incidentOrg.getIncidentid() ) {
                orgIds.add(incidentOrg.getOrgid());
            } else {
                log.warn("orgid {} not being added, since it doesn't match the incoming incidentid: {}",
                        incidentOrg.getOrgid(), incidentId);
            }
		}

		if(orgIds.isEmpty()) {
            // No eligible entries
            return -3;
        }

        // TODO: seems excessive... may need to make sure this is only done by owning org or superuser maybe?
        // TODO: alternatively, add deleted column to schema, but that gets ugly since the primary key is
        //       orgid+incidentid
        QueryModel deleteQuery = QueryManager.createQuery(SADisplayConstants.INCIDENT_ORG_TABLE)
                .deleteFromTableWhere().equals(SADisplayConstants.INCIDENT_ID)
                .and().notInAsInteger(SADisplayConstants.ORG_ID, orgIds);

        int delCount = -1;
        int addCount = -1;
        try {
            delCount = template.update(deleteQuery.toString(),
                    new MapSqlParameterSource(SADisplayConstants.INCIDENT_ID, incidentId));

            log.debug("User {} deleted {} entries where the incidentId was {} and the orgId was NOT in {}",
                    userId, delCount, incidentId, Arrays.toString(incidentOrgs.toArray()));

            Map<String, Object> resultMap = addIncidentOrgs(workspaceId, incidentId, username, userId, incidentOrgs);
            //addCount = addIncidentOrgs(workspaceId, incidentId, username, userId, incidentOrgs);
            List<Integer> adds = (List<Integer>)resultMap.get("success");
            addCount = (adds == null) ? 0 : adds.size();

            log.debug("User {} deleted {} entries and added {} entries to incident with ID {}",
					userId, delCount, addCount, incidentId);

        } catch(Exception e) {
            log.error("Exception modifying orgs on incident: {}", e.getMessage(), e);
        }

        // TODO: ugly. Would be nice to have more of a json return object to explicitly state
		//       errors, and the number added, and the number removed, etc
        if(delCount == -1 && addCount == -1) { // Error
			numChanges = -2;
		} else {
        	// Number of changes, not counting if there was an error, although an exception should
			// probably be thrown if one of them errored?
        	numChanges = (addCount == -1 ? 0 : addCount) + (delCount == -1 ? 0 : delCount);
		}

        return numChanges;
    }*/

    @Override
    public Map<String, Object> removeIncidentOrgs(Integer workspaceId, Integer incidentId, String username,
                                                  Integer userId, List<IncidentOrg> incidentOrgs)
            throws DataAccessException {

		/* TODO: Just restricting in API for now. You also need to check for superuser here, since
           TODO: you may not be an admin, but be a superuser, and since you're not an admin, you won't get by
		if(!isAdmin(workspaceId, incidentId, username)) {
			log.warn("user {} not authorized to remove orgs from incident {}", userId, incidentId);

            // TODO: create unauthorized exception
            throw new Exception(String.format("User %s not authorized to remove orgs from incidentid %d",
                    username, incidentId));
		}*/

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("incidentId", incidentId);
        List<Integer> success = new ArrayList<Integer>();
        List<Integer> fail = new ArrayList<Integer>();
        List<String> messages = new ArrayList<String>();

        List<Integer> orgIds = new ArrayList<Integer>();
        for(IncidentOrg incidentOrg : incidentOrgs) {
            // Only add if incidentId matches
            if(incidentOrg.getIncidentid() == incidentId) {
                orgIds.add(incidentOrg.getOrgid());
            } else {
                LOG.warn("NOT including orgid {} because it doesn't match the incidentid passed to the endpoint: {}",
                        incidentOrg.getOrgid(), incidentId);
            }
        }

        if(orgIds.isEmpty()) {
            resultMap.put("messages", Arrays.asList("No eligible mappings"));
            return resultMap;
        }

        List<Integer> before = getIncidentOrgIds(incidentId);

        QueryModel model = QueryManager.createQuery(SADisplayConstants.INCIDENT_ORG_TABLE)
                .deleteFromTableWhere().equals(SADisplayConstants.INCIDENT_ID).and()
                .inAsInteger(SADisplayConstants.ORG_ID, orgIds);

        LOG.trace("removeIncidentOrgs: {}", model.toString());

        int result = this.template.update(model.toString(),
                new MapSqlParameterSource(SADisplayConstants.INCIDENT_ID, incidentId));

        LOG.debug("User {} deleted {} incidentOrgs from IncidentId {}", userId, result, incidentId);

        if(result != orgIds.size()) {
            // get diff?
            messages.add("Number of affected rows doesn't match the requested number of deletes");
            List<Integer> latest = getIncidentOrgIds(incidentId);
            for(Integer shouldBeGone : orgIds) {
                if(latest.contains(shouldBeGone)) {
                    // failed
                    fail.add(shouldBeGone);
                } else {
                    // Only add it as a success if it was actually there before, but don't count it as
                    // a failure either
                    if(before.contains(shouldBeGone)) {
                        success.add(shouldBeGone);
                    }
                }
            }
        } else {
            success.addAll(orgIds);
        }

        resultMap.put("success", success);
        resultMap.put("fail", fail);
        resultMap.put("messages", messages);

        return resultMap;
    }

    /**
     * Checks whether or not the specified userId is in the org that created the specified incidentId
     *
     * @param incidentId the id of the incident to check
     * @param userId     the user to check for org membership
     * @return true if the user is in the org that created the incident, false otherwise
     */
    private boolean isUserInOrgOwningIncident(Integer incidentId, Integer userId) {
        boolean isIn = false;

        QueryModel getOwningOrgId = QueryManager.createQuery(SADisplayConstants.USER_ORG_TABLE)
                .selectFromTable(SADisplayConstants.ORG_ID)
                .left().join(SADisplayConstants.USERSESSION_TABLE).using(SADisplayConstants.USER_ORG_ID)
                .left().join(SADisplayConstants.INCIDENT_TABLE).using(SADisplayConstants.USERSESSION_ID)
                .where().equals(SADisplayConstants.INCIDENT_ID);

        int newOwningOrgId = template.queryForObject(getOwningOrgId.toString(),
                new MapSqlParameterSource(SADisplayConstants.INCIDENT_ID, incidentId),
                Integer.class).intValue();

        QueryModel userOrgQuery = QueryManager.createQuery(SADisplayConstants.USER_ORG_TABLE)
                .selectFromTable(SADisplayConstants.ORG_ID)
                .where().equals(SADisplayConstants.USER_ID);

        List<Integer> orgIds = template.queryForList(userOrgQuery.toString(),
                new MapSqlParameterSource(SADisplayConstants.USER_ID, userId), Integer.class);

        for(Integer orgId : orgIds) {
            if(orgId == newOwningOrgId) {
                isIn = true;
                break;
            }
        }

        return isIn;
    }

    public int createIncidentFolder(int incidentId, String folderId){
        try {
            MapSqlParameterSource map = new MapSqlParameterSource();
            map.addValue(SADisplayConstants.FOLDER_ID, folderId);
            map.addValue(SADisplayConstants.INCIDENT_ID, incidentId);

            QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENT_FOLDER_TABLE)
                    .insertInto(new ArrayList<String>(map.getValues().keySet()))
                    .returnValue(SADisplayConstants.INCIDENT_ID);

            System.out.println("INCIDENT_FOLDER " + queryModel.toString());
            int incidentid = this.template.queryForObject(queryModel.toString(), map, Integer.class);

            return incidentid;
        }catch(Exception e){
            LOG.info("Failed to create incident_folder #0", incidentId);
        }
        return -1;
    }

    public Folder getIncidentFolder(int incidentId){
        try {

            QueryModel queryModel = QueryManager.createQuery(
                    SADisplayConstants.INCIDENT_FOLDER_TABLE)
                    .selectAllFromTable()
                    .join(SADisplayConstants.FOLDER_TABLE).using(SADisplayConstants.FOLDER_ID)
                    .where().equals(SADisplayConstants.INCIDENT_ID);

            JoinRowCallbackHandler<Folder> handler = getFolderHandlerWith();

            this.template.query(queryModel.toString(),
                    new MapSqlParameterSource(SADisplayConstants.INCIDENT_ID, incidentId),
                    handler);

            return handler.getSingleResult();

        }catch(Exception e){
            LOG.info("Failed to retrieve incident_folder #0", incidentId);
        }
        return null;
    }

    public List<FormType> getIncidentFormTypes(int workspaceId, int incidentId) {
        List<FormType> formTypes = new ArrayList<>();

        List<String> fields = new ArrayList<>();
        fields.add(SADisplayConstants.FORM_TYPE_ID);
        fields.add(SADisplayConstants.FORM_TYPE_NAME);

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.FORM_TYPE_TABLE)
                .selectFromTable(fields)
                .left().join(SADisplayConstants.INCIDENTTYPE_FORMTYPE_TABLE).using(SADisplayConstants.FORM_TYPE_ID)
                .left().join(SADisplayConstants.INCIDENT_INCIDENTTYPE_TABLE).using(SADisplayConstants.INCIDENT_TYPE_ID)
                .where()
                .equals(SADisplayConstants.INCIDENT_ID, incidentId);

        MapSqlParameterSource params = new MapSqlParameterSource(SADisplayConstants.INCIDENT_ID, incidentId);

        LOG.debug("Executing query for Incident ReportTypes for incident {}: {}",
                incidentId, queryModel.toString());

        List<Map<String, Object>> results;
        results = this.template.queryForList(queryModel.toString(), params);

        // TODO: update query to return a list of FormType entities?
        if(results != null && !results.isEmpty()) {
            for(Map<String, Object> entry : results) {
                FormType ft = new FormType();
                ft.setFormTypeId((int)entry.get("formTypeId"));
                ft.setFormTypeName((String)entry.get("formTypeName"));
                formTypes.add(ft);
            }
        }

        return formTypes;
    }

    public int createIncidentTypeReportTypes(int workspaceId, int incidentTypeId, List<Integer> formTypeIds) {
        int failCount = 0;

        for(int formTypeId : formTypeIds) {
            try {
                createIncidentTypeReportType(workspaceId, incidentTypeId, formTypeId);
            } catch(Exception e) {
                failCount++;
            }
        }

        return formTypeIds.size() - failCount;
    }

    public boolean createIncidentTypeReportType(int workspaceId, int incidentTypeId, int formTypeId) {
        List<String> fields = new ArrayList<>();
        fields.add(SADisplayConstants.INCIDENT_TYPE_ID);
        fields.add(SADisplayConstants.FORM_TYPE_ID);

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENTTYPE_FORMTYPE_TABLE)
                .insertInto(fields);

        MapSqlParameterSource params = new MapSqlParameterSource(SADisplayConstants.INCIDENT_TYPE_ID, incidentTypeId);
        params.addValue(SADisplayConstants.FORM_TYPE_ID, formTypeId);

        if(1 == this.template.update(queryModel.toString(), params)) {
            return true;
        }

        return false;
    }

    public boolean deleteIncidentTypeReportTypeById(int workspaceId, int incidentTypeReportTypeId) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENTTYPE_FORMTYPE_TABLE)
                .deleteFromTableWhere().equals(SADisplayConstants.INCIDENTTYPE_FORMTYPE_ID);
        MapSqlParameterSource params = new MapSqlParameterSource(SADisplayConstants.INCIDENTTYPE_FORMTYPE_ID,
                incidentTypeReportTypeId);

        if(1 == this.template.update(queryModel.toString(), params)) {
            return true;
        }

        return false;
    }

    public int deleteIncidentTypeReportTypes(int workspaceId, int incidentTypeId, List<Integer> formTypeIds) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.INCIDENTTYPE_FORMTYPE_TABLE)
                .deleteFromTableWhere().equals(SADisplayConstants.INCIDENT_TYPE_ID)
                .and().inAsInteger(SADisplayConstants.FORM_TYPE_ID, formTypeIds);

        MapSqlParameterSource params = new MapSqlParameterSource(SADisplayConstants.INCIDENT_TYPE_ID,
                incidentTypeId);

        LOG.debug("Query to delete formTypeIds specified that are mapped to incidenttypeid: ", queryModel.toString());
        int affected = this.template.update(queryModel.toString(), params);

        LOG.debug(String.format("Deleted %d mappings", affected));

        return affected;
    }


    /**
     * Returns list of fields requested by an incident search
     *
     * @return Incident Fields
     */
    private StringBuffer getIncidentFields() {
        StringBuffer fields = new StringBuffer();
        fields.append(SADisplayConstants.INCIDENT_NAME);
        fields.append(QueryBuilder.COMMA);
        fields.append(SADisplayConstants.INCIDENT_ID);
        fields.append(QueryBuilder.COMMA);
        fields.append(SADisplayConstants.LATITUDE);
        fields.append(QueryBuilder.COMMA);
        fields.append(SADisplayConstants.LONGITUDE);
        fields.append(QueryBuilder.COMMA);
        fields.append(SADisplayConstants.ACTIVE);

        return fields;
    }

    public IncidentType createIncidentType(IncidentType incidentType) {

        List<String> fields = Arrays.asList(
                SADisplayConstants.INCIDENT_TYPE_NAME,
                SADisplayConstants.INCIDENT_TYPE_DEFAULTTYPE);

        Map<String, Object> params = new HashMap<>();
        params.put(SADisplayConstants.INCIDENT_TYPE_NAME, incidentType.getIncidentTypeName());
        params.put(SADisplayConstants.INCIDENT_TYPE_DEFAULTTYPE, incidentType.isDefaulttype());

        QueryModel query = QueryManager.createQuery(
                SADisplayConstants.INCIDENT_TYPE_TABLE).insertInto(fields)
                    .returnValue(SADisplayConstants.INCIDENT_TYPE_ID);

        int incidentTypeId = -1;
        try {
            incidentTypeId = this.template.queryForObject(query.toString(),
                    new MapSqlParameterSource(params), Integer.class);
            if(incidentTypeId > 0) {
                incidentType.setIncidentTypeId(incidentTypeId);
            } else {
                // Failed
                incidentType = null;
            }
        } catch(DataAccessException e) {
            LOG.error("Exception persisting IncidentType {}", incidentType.getIncidentTypeName(), e);
            incidentType = null;
        }

        return incidentType;
    }

    public IncidentType updateIncidentType(IncidentType incidentType) {
        List<String> fields = Arrays.asList(
                SADisplayConstants.INCIDENT_TYPE_NAME,
                SADisplayConstants.INCIDENT_TYPE_DEFAULTTYPE);

        Map<String, Object> params = new HashMap<>();
        params.put(SADisplayConstants.INCIDENT_TYPE_ID, incidentType.getIncidentTypeId());
        params.put(SADisplayConstants.INCIDENT_TYPE_NAME, incidentType.getIncidentTypeName());
        params.put(SADisplayConstants.INCIDENT_TYPE_DEFAULTTYPE, incidentType.isDefaulttype());

        QueryModel query = QueryManager.createQuery(
                SADisplayConstants.INCIDENT_TYPE_TABLE).update(fields)
                .where().equals(SADisplayConstants.INCIDENT_TYPE_ID);

        try {
            int affected = this.template.update(query.toString(),
                    new MapSqlParameterSource(params));
            if(affected == 1) {
                incidentType = getIncidentTypeById(incidentType.getIncidentTypeId());
            } else {
                // Failed
                incidentType = null;
            }
        } catch(DataAccessException e) {
            LOG.error("Exception persisting IncidentType {}", incidentType.getIncidentTypeName(), e);
            incidentType = null;
        }

        return incidentType;
    }

    public IncidentType getIncidentTypeById(int incidentTypeId) {
        IncidentType incidentType = null;
        try {
            QueryModel query = QueryManager.createQuery(SADisplayConstants.INCIDENT_TYPE_TABLE)
                    .selectAllFromTable().where().equals(SADisplayConstants.INCIDENT_TYPE_ID);

            JoinRowCallbackHandler<IncidentType> handler = getIncidentTypeHandlerWith();

            this.template.query(query.toString(),
                    new MapSqlParameterSource(SADisplayConstants.INCIDENT_TYPE_ID, incidentTypeId),
                    handler);

            incidentType = handler.getSingleResult();

        } catch(Exception e) {
            LOG.error("Exception reading IncidentType with ID {}", incidentTypeId, e);
        }

        return incidentType;
    }

    public boolean deleteIncidentType(int incidentTypeId) throws Exception {
        QueryModel query = QueryManager.createQuery(SADisplayConstants.INCIDENT_TYPE_TABLE)
                .deleteFromTableWhere().equals(SADisplayConstants.INCIDENT_TYPE_ID);

        try {
            int affected = this.template.update(query.toString(),
                    new MapSqlParameterSource(SADisplayConstants.INCIDENT_TYPE_ID, incidentTypeId));
            return affected == 1;
        } catch(DataAccessException e) {
            LOG.error("Exception deleting IncidentType {}", incidentTypeId, e);

            LOG.error("DataAccessException type? {}", e.getClass().getTypeName());

            if(e instanceof DataIntegrityViolationException) {
                // TODO: Needs improvement, this isn't the only reason this exception would occur
                throw new Exception("IncidentType still in use. Cannot delete.");
            }
        }

        return false;
    }


    /**
     * getHandlerWith
     *
     * @param mappers - optional additional mappers
     * @return JoinRowCallbackHandler<Incident>
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private JoinRowCallbackHandler<Incident> getIncidentHandlerWith(JoinRowMapper... mappers) {
        return new JoinRowCallbackHandler(new IncidentRowMapper(), mappers);
    }

    /**
     * getHandlerWith
     *
     * @param mappers - optional additional mappers
     * @return JoinRowCallbackHandler<Incident>
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private JoinRowCallbackHandler<IncidentType> getIncidentTypeHandlerWith(JoinRowMapper... mappers) {
        return new JoinRowCallbackHandler(new IncidentTypeRowMapper(), mappers);
    }

    private JoinRowCallbackHandler<IncidentOrg> getIncidentOrgHandlerWith(JoinRowMapper... mappers) {
        return new JoinRowCallbackHandler(new IncidentOrgMapper(), mappers);
    }

    /**
     * getHandlerWith
     *
     * @param mappers - optional additional mappers
     * @return JoinRowCallbackHandler<Folder>
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private JoinRowCallbackHandler<Folder> getFolderHandlerWith(JoinRowMapper... mappers) {
        return new JoinRowCallbackHandler(new FolderRowMapper(), mappers);
    }

}
