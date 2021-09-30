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
import edu.mit.ll.nics.common.entity.Cap;
import edu.mit.ll.nics.common.entity.FormType;
import edu.mit.ll.nics.common.entity.IncidentType;
import edu.mit.ll.nics.common.entity.Org;
import edu.mit.ll.nics.common.entity.OrgCap;
import edu.mit.ll.nics.common.entity.OrgIncidentType;
import edu.mit.ll.nics.common.entity.OrgOrgType;
import edu.mit.ll.nics.common.entity.OrgType;
import edu.mit.ll.nics.nicsdao.GenericDAO;
import edu.mit.ll.nics.nicsdao.OrgDAO;
import edu.mit.ll.nics.nicsdao.QueryManager;
import edu.mit.ll.nics.nicsdao.mappers.CapRowMapper;
import edu.mit.ll.nics.nicsdao.mappers.IncidentTypeRowMapper;
import edu.mit.ll.nics.nicsdao.mappers.OrgCapRowMapper;
import edu.mit.ll.nics.nicsdao.mappers.OrgIncidentTypeRowMapper;
import edu.mit.ll.nics.nicsdao.mappers.OrgOrgTypeRowMapper;
import edu.mit.ll.nics.nicsdao.mappers.OrgRowMapper;
import edu.mit.ll.nics.nicsdao.mappers.OrgTypeRowMapper;
import edu.mit.ll.nics.nicsdao.mappers.UserOrgRowMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


public class OrgDAOImpl extends GenericDAO implements OrgDAO {

    private Logger log;

    private NamedParameterJdbcTemplate template;

    private String ADMIN_ID = "adminsystemroleid";
    private String SUPER_ID = "supersystemroleid";

    @Override
    public void initialize() {
        log = LoggerFactory.getLogger(OrgDAOImpl.class);
        this.template = new NamedParameterJdbcTemplate(datasource);
    }

    /**
     * getOrgAdmins
     *
     * @param orgid
     * @return String - return a comma delimited list of email addresses
     */
    public List<String> getOrgAdmins(int orgid) {

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.CONTACT_TABLE)
                .selectFromTable(SADisplayConstants.VALUE)
                .join(SADisplayConstants.USER_ORG_TABLE).using(SADisplayConstants.USER_ID)
                .join(SADisplayConstants.USER_ESCAPED).using(SADisplayConstants.USER_ID)
                .where().equals(SADisplayConstants.CONTACT_TYPE_ID)
                .and().equals(SADisplayConstants.ORG_ID)
                .and().equals(SADisplayConstants.ACTIVE)
                .and().open().equals(SADisplayConstants.SYSTEM_ROLE_ID, ADMIN_ID, null) //open = (
                .or().equals(SADisplayConstants.SYSTEM_ROLE_ID, SUPER_ID, null)
                .close(); //close = )

        Map<String, Object> params = new HashMap<String, Object>();
        params.put(SADisplayConstants.CONTACT_TYPE_ID, SADisplayConstants.EMAIL_TYPE_ID);
        params.put(SADisplayConstants.ORG_ID, orgid);
        params.put(ADMIN_ID, SADisplayConstants.ADMIN_ROLE_ID);
        params.put(SUPER_ID, SADisplayConstants.SUPER_ROLE_ID);
        params.put(SADisplayConstants.ACTIVE, true);

        return this.template.queryForList(queryModel.toString(), params, String.class);
    }

    /**
     * getOrgAdmins
     *
     * @param orgid
     * @param workspaceId
     * @return String - return a comma delimited list of email addresses
     */
    public List<Integer> getOrgAdmins(int orgid, int workspaceId) {

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.USER_TABLE)
                .selectFromTable(SADisplayConstants.USER_ID)
                .join(SADisplayConstants.USER_ORG_TABLE).using(SADisplayConstants.USER_ID)
                .join(SADisplayConstants.USER_ORG_WORKSPACE_TABLE).using(SADisplayConstants.USER_ORG_ID)
                .where().equals(SADisplayConstants.ORG_ID)
                .and().equals(SADisplayConstants.ACTIVE)
                .and().equals(SADisplayConstants.USER_ORG_WORKSPACE_ENABLED)
                .and().equals(SADisplayConstants.WORKSPACE_ID)
                .and().open().equals(SADisplayConstants.SYSTEM_ROLE_ID, ADMIN_ID, null) //open = (
                .or().equals(SADisplayConstants.SYSTEM_ROLE_ID, SUPER_ID, null)
                .close(); //close = )

        Map<String, Object> params = new HashMap<String, Object>();
        params.put(SADisplayConstants.ORG_ID, orgid);
        params.put(ADMIN_ID, SADisplayConstants.ADMIN_ROLE_ID);
        params.put(SUPER_ID, SADisplayConstants.SUPER_ROLE_ID);
        params.put(SADisplayConstants.USER_ORG_WORKSPACE_ENABLED, true);
        params.put(SADisplayConstants.ACTIVE, true);
        params.put(SADisplayConstants.WORKSPACE_ID, workspaceId);

        return this.template.queryForList(queryModel.toString(), params, Integer.class);
    }

    /**
     * getChildOrgIds
     *
     * @param orgIds
     * @return List<Integer> Return a list of children org ids for these organizations
     */
    public List<Integer> getChildOrgIds(List<Integer> orgIds) {
        // Incident IDs that are locked down, minus any from above they have explicit permission on
        QueryModel query = QueryManager.createQuery(SADisplayConstants.ORG_TABLE)
                .selectDistinctFromTable(SADisplayConstants.ORG_ID).where()
                .inAsInteger(SADisplayConstants.PARENT_ORG_ID, orgIds);
        //orgid not in (orglist) ? so it won't return duplicates?

        return this.template.queryForList(query.toString(),
                (new HashMap<String, Object>()), Integer.class);
    }

    /**
     * getParentOrgIds
     *
     * @param orgIds
     * @return List<Integer> Return a list of children org ids for these organizations
     */
    public List<Integer> getParentOrgIds(List<Integer> orgIds) {
        // Incident IDs that are locked down, minus any from above they have explicit permission on
        QueryModel query = QueryManager.createQuery(SADisplayConstants.ORG_TABLE)
                .selectDistinctFromTable(SADisplayConstants.PARENT_ORG_ID).where()
                .inAsInteger(SADisplayConstants.ORG_ID, orgIds)
                .and().isNotNull(SADisplayConstants.PARENT_ORG_ID)
                .and().notEqual(SADisplayConstants.PARENT_ORG_ID);
        //orgid not in (orglist) ? so it won't return duplicates?

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue(SADisplayConstants.PARENT_ORG_ID, 0);

        return this.template.queryForList(query.toString(),
                //		(new HashMap<String, Object>()), Integer.class);
                map, Integer.class);
    }

    /**
     * getAllChildren
     *
     * @param orgIds
     * @return List<Integer> Recursively search for all children
     */
    public List<Integer> getAllChildren(List<Integer> orgIds) {
        if(orgIds == null || orgIds.isEmpty()) {
            return new ArrayList<>();
        }

        //Find all of the children that the user has access to...
        //ids is set to the first level of children for these orgs
        List<Integer> ids = this.getChildOrgIds(orgIds);

        //Keep track of children until there are no more returned
        List<Integer> childIds = new ArrayList<>();

        //Array of children to return
        List<Integer> foundChildren = new ArrayList<>();

        if(ids != null) {
            while(ids != null && ids.size() != 0) {
                childIds.clear();
                for(int id : ids) {
                    //Make sure we haven't already searched on the id
                    if(!orgIds.contains(id) &&
                            !foundChildren.contains(id)) {
                        //Add to the list of orgs
                        foundChildren.add(id);

                        //Search for children
                        childIds.add(id);
                    }
                }
                //Search for children in the new list
                if(childIds.size() > 0) {
                    ids = this.getChildOrgIds(childIds);
                } else {
                    //Done searching
                    ids.clear();
                }
            }
        }
        //returns a list of all original orgIds plus children
        return foundChildren;
    }

    /**
     * Calls {@link OrgDAOImpl#getAllChildren(List)} to then fetch the child orgs to
     * return a list of child orgs for the given orgid.
     *
     * @param orgId the ID of the Org to fetch children for.
     *
     * @return a list containing all child organizations if successful, or an empty list of none.
     */
    public List<Org> getChildOrgs(int orgId) {
        List<Org> children = null;

        List<Integer> childOrgIds = getAllChildren(Arrays.asList(orgId));
        if(childOrgIds.size() > 0) {

            QueryModel query = QueryManager.createQuery(SADisplayConstants.ORG_TABLE)
                    .selectAllFromTableWhere()
                    .inAsInteger(SADisplayConstants.ORG_ID, childOrgIds);

            MapSqlParameterSource map = new MapSqlParameterSource();
            JoinRowCallbackHandler<Org> handler = getHandlerWith();

            this.template.query(query.toString(), map, handler);
            children = handler.getResults();
        }

        return children == null ? new ArrayList<>() : children;
    }

    /**
     * getAllParents
     *
     * @param orgIds
     * @return List<Integer> Recursively search for all children
     */
    public List<Integer> getAllParents(List<Integer> orgIds) {
        //Find all of the parents that the in this list of orgs
        List<Integer> ids = this.getParentOrgIds(orgIds);

        //Keep track of parents until there are no more returned
        List<Integer> parentIds = new ArrayList<>();

        //Array of parents to return
        List<Integer> foundParents = new ArrayList<>();

        while(ids.size() != 0) {
            parentIds.clear();
            for(int id : ids) {
                //Make sure we haven't already searched on the id
                if(!orgIds.contains(id) &&
                        !foundParents.contains(id)) {
                    //Add to the list of orgs
                    foundParents.add(id);

                    //Search for parents
                    parentIds.add(id);
                }
            }
            //Search for children in the new list
            if(parentIds.size() > 0) {
                ids = this.getParentOrgIds(parentIds);
            } else {
                //Done searching
                ids.clear();
            }
        }
        //returns a list of all original orgIds plus children
        return foundParents;
    }

    /**
     * getOrgFolders
     *
     * @param orgid
     * @return List<String> - return a list of folder ids that belong to the given organization
     */
    public List<String> getOrgFolderIds(int orgid) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.ORG_FOLDER_TABLE)
                .selectFromTable(SADisplayConstants.FOLDER_ID)
                .where().equals(SADisplayConstants.ORG_ID);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put(SADisplayConstants.ORG_ID, orgid);
        return this.template.queryForList(queryModel.toString(), params, String.class);
    }

    /**
     * getUserOrgs
     *
     * @param userid
     * @param workspaceId
     * @return List<Org> - List of organizations this user belongs to
     */
    public List<Org> getUserOrgs(int userid, int workspaceId) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.ORG_TABLE)
                .selectAllFromTable()
                .join(SADisplayConstants.USER_ORG_TABLE).using(SADisplayConstants.ORG_ID)
                .join(SADisplayConstants.USER_ORG_WORKSPACE_TABLE).using(SADisplayConstants.USER_ORG_ID)
                .where().equals(SADisplayConstants.USER_ID)
                .and().equals(SADisplayConstants.USER_ORG_WORKSPACE_ENABLED)
                .and().equals(SADisplayConstants.WORKSPACE_ID).orderBy(SADisplayConstants.ORG_NAME);

        JoinRowCallbackHandler<Org> handler = getHandlerWith(new UserOrgRowMapper());
        template.query(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.USER_ID, userid)
                        .addValue(SADisplayConstants.USER_ORG_WORKSPACE_ENABLED, true)
                        .addValue(SADisplayConstants.WORKSPACE_ID, workspaceId),
                handler);
        return handler.getResults();
    }

    /**
     * getUserOrgs
     *
     * @param userid      - userid
     * @param workspaceId
     * @return List<Org> - List of organizations this user belongs to
     */
    public List<Org> getAdminOrgs(int userid, int workspaceId) {
        String roles = StringUtils.join(Arrays.asList(new Integer(SADisplayConstants.SUPER_ROLE_ID).toString(),
                new Integer(SADisplayConstants.ADMIN_ROLE_ID).toString()), ",");

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.ORG_TABLE)
                .selectAllFromTable()
                .join(SADisplayConstants.USER_ORG_TABLE).using(SADisplayConstants.ORG_ID)
                .join(SADisplayConstants.USER_ORG_WORKSPACE_TABLE).using(SADisplayConstants.USER_ORG_ID)
                .join(SADisplayConstants.ORG_ORGTYPE_TABLE).using(SADisplayConstants.ORG_ID)
                .where().equals(SADisplayConstants.USER_ID)
                .and().equals(SADisplayConstants.USER_ORG_WORKSPACE_ENABLED)
                .and().equals(SADisplayConstants.WORKSPACE_ID)
                .and().inAsSQL(SADisplayConstants.SYSTEM_ROLE_ID, roles)
                .orderBy(SADisplayConstants.ORG_NAME);

        JoinRowCallbackHandler<Org> handler = getHandlerWith(new UserOrgRowMapper(), new OrgOrgTypeRowMapper());
        template.query(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.USER_ID, userid)
                        .addValue(SADisplayConstants.USER_ORG_WORKSPACE_ENABLED, true)
                        .addValue(SADisplayConstants.WORKSPACE_ID, workspaceId),
                handler);
        return handler.getResults();
    }

    /**
     * getUserOrgs
     *
     * @param orgIds
     * @return List<Org> - List of organizations this user belongs to
     */
    public List<Org> getAdminOrgs(List<Integer> orgIds) {

        if(orgIds == null || orgIds.isEmpty()) {
            return new ArrayList<>();
        }

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.ORG_TABLE)
                .selectAllFromTable()
                .join(SADisplayConstants.USER_ORG_TABLE).using(SADisplayConstants.ORG_ID)
                .join(SADisplayConstants.USER_ORG_WORKSPACE_TABLE).using(SADisplayConstants.USER_ORG_ID)
                .join(SADisplayConstants.ORG_ORGTYPE_TABLE).using(SADisplayConstants.ORG_ID)
                .where().inAsInteger(SADisplayConstants.ORG_ID, orgIds)
                .orderBy(SADisplayConstants.ORG_NAME);

        JoinRowCallbackHandler<Org> handler = getHandlerWith(new UserOrgRowMapper(), new OrgOrgTypeRowMapper());
        template.query(queryModel.toString(),
                new MapSqlParameterSource(),
                handler);
        return handler.getResults();
    }

    // TODO: Old API call, revisit necessity of this call
    // this version returns key/value pairs, whereas the old one returned just an array of values. New one is better,
    // but will break Mobile calls to it
    public List<Map<String, Object>> getUserOrgsWithOrgName(int userid, int workspaceId) {
        List<String> fields = Arrays.asList(SADisplayConstants.USER_ORG_ID, SADisplayConstants.ORG_NAME,
                SADisplayConstants.ORG_ID, SADisplayConstants.SYSTEM_ROLE_ID, SADisplayConstants.DEFAULT_ORG,
                SADisplayConstants.COUNTRY_ID, SADisplayConstants.STATE);

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.ORG_TABLE)
                .selectFromTable(fields)
                .join(SADisplayConstants.USER_ORG_TABLE).using(SADisplayConstants.ORG_ID)
                .join(SADisplayConstants.USER_ORG_WORKSPACE_TABLE).using(SADisplayConstants.USER_ORG_ID)
                .where().equals(SADisplayConstants.USER_ID)
                .and().equals(SADisplayConstants.ENABLED)
                .and().equals(SADisplayConstants.WORKSPACE_ID)
                .orderBy(SADisplayConstants.ORG_NAME);

        return template.queryForList(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.USER_ID, userid)
                        .addValue(SADisplayConstants.ENABLED, true)
                        .addValue(SADisplayConstants.WORKSPACE_ID, workspaceId));
    }

    public List<Org> getUserOrgsByUsername(String username, int workspaceId) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.ORG_TABLE)
                .selectAllFromTable()
                .join(SADisplayConstants.USER_ORG_TABLE).using(SADisplayConstants.ORG_ID)
                .join(SADisplayConstants.USER_ORG_WORKSPACE_TABLE).using(SADisplayConstants.USER_ORG_ID)
                .where().equals(SADisplayConstants.USER_NAME)
                .and().equals(SADisplayConstants.USER_ORG_WORKSPACE_ENABLED)
                .and().equals(SADisplayConstants.WORKSPACE_ID);

        JoinRowCallbackHandler<Org> handler = getHandlerWith(new UserOrgRowMapper());
        template.query(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.USER_NAME, username)
                        .addValue(SADisplayConstants.USER_ORG_WORKSPACE_ENABLED, true)
                        .addValue(SADisplayConstants.WORKSPACE_ID, workspaceId),
                handler);
        return handler.getResults();
    }

    public List<String> getOrgNames() {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.ORG_TABLE)
                .selectDistinctFromTable(SADisplayConstants.ORG_NAME);

        return this.template.queryForList(queryModel.toString(), new MapSqlParameterSource(), String.class);
    }

    public List<Org> getOrgsByType(int orgtypeid) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.ORG_ORGTYPE_TABLE)
                .selectAllFromTable()
                .join(SADisplayConstants.ORG_ORGTYPE_TABLE).using(SADisplayConstants.ORG_ID)
                .where().equals(SADisplayConstants.ORG_TYPE_ID);

        JoinRowCallbackHandler<Org> handler = getHandlerWith();

        this.template.query(queryModel.toString(), new MapSqlParameterSource(SADisplayConstants.ORG_TYPE_ID, orgtypeid),
                handler);
        return handler.getResults();
    }

    public int removeOrgOrgType(int orgId, int orgTypeId) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.ORG_ORGTYPE_TABLE)
                .deleteFromTableWhere().equals(SADisplayConstants.ORG_ID).and().equals(SADisplayConstants.ORG_TYPE_ID);

        return this.template.update(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.ORG_TYPE_ID, orgTypeId)
                        .addValue(SADisplayConstants.ORG_ID, orgId));
    }

    @Override
    public List<Integer> getOrganizationIds(int workspaceId) {
        List<Integer> orgIds = new ArrayList<Integer>();
        QueryModel model = QueryManager.createQuery(SADisplayConstants.USER_ORG_TABLE)
                .selectDistinctFromTable(SADisplayConstants.ORG_ID)
                .join(SADisplayConstants.USER_ORG_WORKSPACE_TABLE).using(SADisplayConstants.USER_ORG_ID)
                .where().equals(SADisplayConstants.WORKSPACE_ID);

        try {
            orgIds = this.template.queryForList(model.toString(),
                    new MapSqlParameterSource().addValue(SADisplayConstants.WORKSPACE_ID, workspaceId), Integer.class);
        } catch(DataAccessException e) {
            log.error("Exception querying orgIds for workspaceid {}", workspaceId, e);
        }

        return orgIds;
    }

    public List<Org> getOrganizations() {
        List<Org> orgs = new ArrayList<Org>();

        //Search for Orgs associated with Org Types
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.ORG_TABLE)
                .selectAllFromTable().join(SADisplayConstants.ORG_ORGTYPE_TABLE).using(SADisplayConstants.ORG_ID)
                .orderBy(SADisplayConstants.ORG_NAME);

        JoinRowCallbackHandler<Org> handler = getHandlerWith(new OrgOrgTypeRowMapper());
        this.template.query(queryModel.toString(), new MapSqlParameterSource(), handler);

        orgs.addAll(handler.getResults());

        //Search for Orgs without Org Types
        JoinRowCallbackHandler<Org> allOrgsHandler = getHandlerWith();
        QueryModel allOrgQuery = QueryManager.createQuery(SADisplayConstants.ORG_TABLE)
                .selectAllFromTable().where().notIn(SADisplayConstants.ORG_ID,
                        QueryManager.createQuery(SADisplayConstants.ORG_ORGTYPE_TABLE)
                                .selectFromTable(SADisplayConstants.ORG_ID).toString());

        this.template.query(allOrgQuery.toString(), new MapSqlParameterSource(), allOrgsHandler);

        orgs.addAll(allOrgsHandler.getResults());
        return orgs;
    }

    public List<Org> getOrganizations(List<Integer> orgIds) {
        List<Org> orgs = new ArrayList<Org>();

        //Search for Orgs associated with Org Types
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.ORG_TABLE)
                .selectAllFromTable().join(SADisplayConstants.ORG_ORGTYPE_TABLE).using(SADisplayConstants.ORG_ID)
                .where().inAsInteger(SADisplayConstants.ORG_ID, orgIds)
                .orderBy(SADisplayConstants.ORG_NAME);

        JoinRowCallbackHandler<Org> handler = getHandlerWith(new OrgOrgTypeRowMapper());
        this.template.query(queryModel.toString(), new MapSqlParameterSource(), handler);

        orgs.addAll(handler.getResults());

        //Search for Orgs without Org Types
        JoinRowCallbackHandler<Org> allOrgsHandler = getHandlerWith();
        QueryModel allOrgQuery = QueryManager.createQuery(SADisplayConstants.ORG_TABLE)
                .selectAllFromTable().where().notIn(SADisplayConstants.ORG_ID,
                        QueryManager.createQuery(SADisplayConstants.ORG_ORGTYPE_TABLE)
                                .selectFromTable(SADisplayConstants.ORG_ID).toString())
                .and().inAsInteger(SADisplayConstants.ORG_ID, orgIds);

        this.template.query(allOrgQuery.toString(), new MapSqlParameterSource(), allOrgsHandler);

        orgs.addAll(allOrgsHandler.getResults());

        return orgs;
    }

    public List<OrgOrgType> getOrgTypes(int orgId) {
        QueryModel query = QueryManager.createQuery(SADisplayConstants.ORG_ORGTYPE_TABLE)
                .selectAllFromTable().where().equals(SADisplayConstants.ORG_ID);

        JoinRowCallbackHandler<OrgOrgType> handler = getOrgOrgTypeHandlerWith();

        this.template.query(query.toString(), new MapSqlParameterSource(SADisplayConstants.ORG_ID, orgId), handler);

        return handler.getResults();

    }

    public List<OrgType> getOrgTypes() {

        QueryModel query = QueryManager.createQuery(SADisplayConstants.ORG_TYPE_TABLE)
                .selectAllFromTable().orderBy(SADisplayConstants.ORG_TYPE_NAME).asc();

        JoinRowCallbackHandler<OrgType> handler = getOrgTypeHandlerWith();

        this.template.query(query.toString(), new MapSqlParameterSource(), handler);

        return handler.getResults();
    }

    public List<OrgOrgType> getOrgOrgTypes() {

        QueryModel query = QueryManager.createQuery(SADisplayConstants.ORG_ORGTYPE_TABLE)
                .selectAllFromTable().orderBy(SADisplayConstants.ORG_TYPE_ID).asc();

        JoinRowCallbackHandler<OrgOrgType> handler = getOrgOrgTypeHandlerWith();
        try {
            this.template.query(query.toString(), new MapSqlParameterSource(), handler);

            return handler.getResults();
        } catch(Exception e) {
            return new ArrayList<OrgOrgType>();
        }
    }

    public List<OrgIncidentType> getOrgIncidentTypes() {
        QueryModel query = QueryManager.createQuery(SADisplayConstants.ORG_INCIDENTTYPE_TABLE)
                .selectAllFromTable().orderBy(SADisplayConstants.ORG_INCIDENTTYPE_ID).asc();

        JoinRowCallbackHandler<OrgIncidentType> handler = getOrgIncidentTypeHandlerWith();
        try {
            this.template.query(query.toString(), new MapSqlParameterSource(), handler);
            return handler.getResults();
        } catch(Exception e) {
            return new ArrayList<OrgIncidentType>();
        }
    }

    public List<OrgIncidentType> getOrgIncidentTypes(int orgId) {
        QueryModel query = QueryManager.createQuery(SADisplayConstants.ORG_INCIDENTTYPE_TABLE)
                .selectAllFromTable()
                .where().equals(SADisplayConstants.ORG_ID)
                .orderBy(SADisplayConstants.ORG_INCIDENTTYPE_ID).asc();

        JoinRowCallbackHandler<OrgIncidentType> handler = getOrgIncidentTypeHandlerWith();
        try {
            this.template.query(query.toString(),
                    new MapSqlParameterSource(SADisplayConstants.ORG_ID, orgId),
                    handler);

            return handler.getResults();
        } catch(Exception e) {
            return new ArrayList<OrgIncidentType>();
        }
    }

    public List<OrgIncidentType> getIncidentTypes(int orgId) {

    	QueryModel query = QueryManager.createQuery(SADisplayConstants.ORG_INCIDENTTYPE_TABLE)
                .selectAllFromTable()
                .join(SADisplayConstants.INCIDENT_TYPE_TABLE).using(SADisplayConstants.INCIDENT_TYPE_ID)
                .where().equals(SADisplayConstants.ORG_ID).orderBy(SADisplayConstants.INCIDENT_TYPE_NAME);

        JoinRowCallbackHandler<OrgIncidentType> handler = getOrgIncidentTypeHandlerWith(
				new IncidentTypeRowMapper()
		);

        try {
            this.template.query(query.toString(),
                    new MapSqlParameterSource(SADisplayConstants.ORG_ID, orgId),
                    handler);

            return handler.getResults();
        } catch(Exception e) {
            return new ArrayList<OrgIncidentType>();
        }
    }

    public List<IncidentType> getInactiveIncidentTypes(int orgId) {
        QueryModel subQuery = QueryManager.createQuery(SADisplayConstants.ORG_INCIDENTTYPE_TABLE)
                .selectFromTable(SADisplayConstants.INCIDENT_TYPE_ID).where().equals(SADisplayConstants.ORG_ID);

        QueryModel query = QueryManager.createQuery(SADisplayConstants.INCIDENT_TYPE_TABLE)
                .selectAllFromTable()
                .where().notIn(SADisplayConstants.INCIDENT_TYPE_ID, subQuery.toString())
                .orderBy(SADisplayConstants.INCIDENT_TYPE_NAME);

        JoinRowCallbackHandler<IncidentType> handler = getIncidentTypeHandlerWith();
        try {
            this.template.query(query.toString(),
                    new MapSqlParameterSource(SADisplayConstants.ORG_ID, orgId),
                    handler);

            return handler.getResults();
        } catch(Exception e) {
            return new ArrayList<IncidentType>();
        }
    }

    public int addOrgIncidentTypes(int orgId, List<Integer> incidentTypeIds) {
        int count = 0;

        if(incidentTypeIds != null && !incidentTypeIds.isEmpty()) {
            int result;
            for(int incidentTypeId : incidentTypeIds) {
                try {
                    result = addOrgIncidentType(orgId, incidentTypeId);
                    if(result > 0) {
                        count += result;
                    }
                } catch(Exception e) {
                    log.error("Exception adding org incidenttype mapping", e);
                }
            }
        } else {
            return -1;
        }

        if(count != incidentTypeIds.size()) {
            log.warn("Not all incidentTypes were added");
        }

        return count;
    }

    public int addOrgIncidentType(int orgId, int incidentTypeId) throws Exception {
        List<String> fields = Arrays.asList(
                SADisplayConstants.INCIDENT_TYPE_ID,
                SADisplayConstants.ORG_ID);

        QueryModel model = QueryManager.createQuery(SADisplayConstants.ORG_INCIDENTTYPE_TABLE).insertInto(fields);

        try {
            return this.template.update(model.toString(), new MapSqlParameterSource(
                    SADisplayConstants.INCIDENT_TYPE_ID, incidentTypeId)
                    .addValue(SADisplayConstants.ORG_ID, orgId));
        } catch(Exception e) {
            e.printStackTrace();
            throw new Exception("Unhandled exception while persisting Org_incidenttype entity:", e);
        }
    }

	public List<Org> getOrgsWithIncidentType(int incidentId){
		QueryModel incidentTypeQuery = QueryManager.createQuery(SADisplayConstants.INCIDENT_INCIDENTTYPE_TABLE)
						.selectFromTable(SADisplayConstants.INCIDENT_TYPE_ID)
						.where().equals(SADisplayConstants.INCIDENT_ID);

		QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.ORG_TABLE)
				.selectAllFromTable()
				.join(SADisplayConstants.ORG_INCIDENTTYPE_TABLE).using(SADisplayConstants.ORG_ID)
				.where().equals(SADisplayConstants.DEFAULT_ORG_INCIDENT_TYPE)
				.and().inAsSQL(SADisplayConstants.INCIDENT_TYPE_ID, incidentTypeQuery.toString());

		JoinRowCallbackHandler<Org> handler = getHandlerWith();

		template.query(queryModel.toString(),
						new MapSqlParameterSource(SADisplayConstants.INCIDENT_ID, incidentId)
										.addValue(SADisplayConstants.DEFAULT_ORG_INCIDENT_TYPE, true),
				handler);
		return handler.getResults();
	}

	@Override
    public List<Map<String, Object>> getOrgsWithDefaultIncidentType(int incidentTypeId) {
        QueryModel incidentTypeQuery = QueryManager.createQuery(SADisplayConstants.ORG_INCIDENTTYPE_TABLE)
                .selectFromTable(SADisplayConstants.ORG_ID)
                .where().equals(SADisplayConstants.INCIDENT_TYPE_ID)
                .and().equals(SADisplayConstants.DEFAULT_ORG_INCIDENT_TYPE);

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.ORG_TABLE)
                .selectFromTable(Arrays.asList(SADisplayConstants.ORG_ID, SADisplayConstants.ORG_NAME))
                .where()
                .inAsSQL(SADisplayConstants.ORG_ID, incidentTypeQuery.toString());

        return template.queryForList(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.INCIDENT_TYPE_ID, incidentTypeId)
                        .addValue(SADisplayConstants.DEFAULT_ORG_INCIDENT_TYPE, true));

    }

    @Override
    public List<Map<String, Object>> getOrgsWithActiveIncidentType(int incidentTypeId, boolean active) {

        final QueryModel orgsByActiveIncidentType = QueryManager.createQuery(SADisplayConstants.ORG_INCIDENTTYPE_TABLE)
                .selectFromTable(SADisplayConstants.ORG_ID)
                .where().equals(SADisplayConstants.INCIDENT_TYPE_ID, incidentTypeId);

        final QueryModel orgAndNameActive = QueryManager.createQuery(SADisplayConstants.ORG_TABLE)
                .selectFromTable(Arrays.asList(SADisplayConstants.ORG_ID, SADisplayConstants.ORG_NAME))
                .where()
                .inAsSQL(SADisplayConstants.ORG_ID, orgsByActiveIncidentType.toString());

        final QueryModel orgAndNameInactive = QueryManager.createQuery(SADisplayConstants.ORG_TABLE)
                .selectFromTable(Arrays.asList(SADisplayConstants.ORG_ID, SADisplayConstants.ORG_NAME))
                .where()
                .notIn(SADisplayConstants.ORG_ID, orgsByActiveIncidentType.toString());

        if(active) {
            return this.template.queryForList(orgAndNameActive.toString(),
                    new MapSqlParameterSource(SADisplayConstants.INCIDENT_TYPE_ID, incidentTypeId));
        } else {
            return this.template.queryForList(orgAndNameInactive.toString(),
                    new MapSqlParameterSource(SADisplayConstants.INCIDENT_TYPE_ID, incidentTypeId));
        }

    }

    public int updateOrgIncidentType(OrgIncidentType orgIncidentType) throws Exception {
		QueryModel model = QueryManager.createQuery(
						SADisplayConstants.ORG_INCIDENTTYPE_TABLE)
						.update().equals(SADisplayConstants.DEFAULT_ORG_INCIDENT_TYPE)
						.where().equals(SADisplayConstants.ORG_INCIDENTTYPE_ID);

		try {
			System.out.println(model.toString());

			return this.template.update(model.toString(), new MapSqlParameterSource(
					SADisplayConstants.ORG_INCIDENTTYPE_ID,
						orgIncidentType.getOrgIncidenttypeid())
					.addValue(SADisplayConstants.DEFAULT_ORG_INCIDENT_TYPE,
							orgIncidentType.getDefaulttype()));
		} catch(Exception e) {
			e.printStackTrace();
			throw new Exception("Unhandled exception while persisting Org_incidenttype entity:", e);
		}
	}

    public int removeOrgIncidentTypes(int orgId, List<Integer> incidentTypeIds) {
        int count = 0;

        if(incidentTypeIds != null && !incidentTypeIds.isEmpty()) {
            int result;
            for(int incidentTypeId : incidentTypeIds) {
                try {
                    result = removeOrgIncidentType(orgId, incidentTypeId);
                    if(result > 0) {
                        count += result;
                    }
                } catch(Exception e) {
                    log.error("Exception removing org incidenttype mapping", e);
                }
            }
        } else {
            return -1;
        }

        if(count != incidentTypeIds.size()) {
            log.warn("Not all incidentTypes were removed");
        }

        return count;
    }

    public int removeOrgIncidentType(int orgId, int incidentTypeId) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.ORG_INCIDENTTYPE_TABLE)
                .deleteFromTableWhere().equals(SADisplayConstants.ORG_ID)
                .and().equals(SADisplayConstants.INCIDENT_TYPE_ID);

        return this.template.update(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.INCIDENT_TYPE_ID, incidentTypeId)
                        .addValue(SADisplayConstants.ORG_ID, orgId));
    }

    public Org getOrganization(String name) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.ORG_TABLE)
                .selectAllFromTableWhere().equals(SADisplayConstants.ORG_NAME);

        JoinRowCallbackHandler<Org> handler = getHandlerWith();
        this.template
                .query(queryModel.toString(), new MapSqlParameterSource(SADisplayConstants.ORG_NAME, name), handler);

        Org ret = null;
        try {
            ret = handler.getSingleResult();
        } catch(Exception e) {
            log.error("Exception querying for Organization(#0): #1", name, e.getMessage());
        }

        return ret;
    }

    public Org getOrganization(int orgId) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.ORG_TABLE)
                .selectAllFromTableWhere().equals(SADisplayConstants.ORG_ID);

        JoinRowCallbackHandler<Org> handler = getHandlerWith();
        this.template
                .query(queryModel.toString(), new MapSqlParameterSource(SADisplayConstants.ORG_ID, orgId), handler);

        Org ret = null;
        try {
            ret = handler.getSingleResult();
        } catch(Exception e) {
            log.error("Exception querying for Organization(#0): #1", orgId, e.getMessage());
        }

        return ret;
    }


    public String getOrgNameByAgency(String agency) {
        try {
            QueryModel query = QueryManager.createQuery(SADisplayConstants.ORG_TABLE)
                    .selectFromTableWhere(SADisplayConstants.ORG_NAME)
                    .equals(SADisplayConstants.PREFIX, agency);

            return this.template.queryForObject(query.toString(),
                    new MapSqlParameterSource(SADisplayConstants.PREFIX, agency), String.class);
        } catch(Exception e) {
            log.info("No organization was found for agency #0", agency);
            return "No Organization was found.";
        }
    }

    public Org getLoggedInOrg(int userid) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.ORG_TABLE)
                .selectAllFromTable()
                .join(SADisplayConstants.USER_ORG_TABLE).using(SADisplayConstants.ORG_ID)
                .join(SADisplayConstants.USERSESSION_TABLE).using(SADisplayConstants.USER_ORG_ID)
                .join(SADisplayConstants.CURRENT_USERSESSION_TABLE).using(SADisplayConstants.USERSESSION_ID)
                .where().equals(SADisplayConstants.CURRENT_USERSESSION_USER_ID);

        JoinRowCallbackHandler<Org> handler = getHandlerWith();
        this.template.query(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.CURRENT_USERSESSION_USER_ID, userid),
                handler);

        try {
            return handler.getSingleResult();
        } catch(Exception e) {
            log.info("No Orgnaization was found for userid #0. User may not be logged in.", userid);
        }
        return null;
    }

    public String getDistributionList(int incidentid) {
        //select distribution from org join userorg using(orgid) join usersession using(userorgid) join incident
        // using(usersessionid) where incidentid=851;
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.ORG_TABLE)
                .selectFromTable(SADisplayConstants.DISTRIBUTION)
                .join(SADisplayConstants.USER_ORG_TABLE).using(SADisplayConstants.ORG_ID)
                .join(SADisplayConstants.USER_SESSION_TABLE).using(SADisplayConstants.USER_ORG_ID)
                .join(SADisplayConstants.INCIDENT_TABLE).using(SADisplayConstants.USERSESSION_ID)
                .where().equals(SADisplayConstants.INCIDENT_ID);
        try {
            return this.template.queryForObject(queryModel.toString(),
                    new MapSqlParameterSource(SADisplayConstants.INCIDENT_ID, incidentid),
                    String.class);
        } catch(Exception e) {
            log.info("No distribution list was found for incident id #0", incidentid);
        }
        return null;
    }

    public int addOrg(Org org) throws Exception {
        // Disallow setting an invalid parentorgid
        if(org.getParentorgid() != null && org.getParentorgid() <= 0) {
            org.setParentorgid(null);
        }

        if(org.getOrgId() > 0) {
            QueryModel query = QueryManager.createQuery(SADisplayConstants.ORG_TABLE).update()
                    .equals(SADisplayConstants.ORG_NAME).comma()
                    .equals(SADisplayConstants.COUNTY).comma()
                    .equals(SADisplayConstants.STATE).comma()
                    .equals(SADisplayConstants.PREFIX).comma()
                    .equals(SADisplayConstants.DISTRIBUTION).comma()
                    .equals(SADisplayConstants.DEFAULT_LAT).comma()
                    .equals(SADisplayConstants.DEFAULT_LON).comma()
                    .equals(SADisplayConstants.DEFAULT_LANGUAGE).comma()
                    .equals(SADisplayConstants.ORG_RESTRICT_INCIDENTS).comma()
                    .equals(SADisplayConstants.ORG_CREATE_INCIDENT_REQUIRES_ADMIN).comma()
                    .equals(SADisplayConstants.PARENT_ORG_ID);

            BeanPropertySqlParameterSource map = new BeanPropertySqlParameterSource(org);

            if(org.getCountryId() > 0) {
                query.comma().equals(SADisplayConstants.COUNTRY_ID);
            }

            query.where().equals(SADisplayConstants.ORG_ID);

            int ret = -1;
            try {
                ret = this.template.update(query.toString(), map);
            } catch(Exception e) {
                throw new Exception("Error updating the org with id: " +
                        org.getOrgId() + ": " + e.getMessage());
            }

            return org.getOrgId();

        } else {
            List<String> fields = new ArrayList<>(Arrays.asList(
                    SADisplayConstants.ORG_NAME, SADisplayConstants.COUNTY,
                    SADisplayConstants.STATE, SADisplayConstants.PREFIX, SADisplayConstants.DISTRIBUTION,
                    SADisplayConstants.DEFAULT_LAT, SADisplayConstants.DEFAULT_LON,
                    SADisplayConstants.DEFAULT_LANGUAGE, SADisplayConstants.ORG_RESTRICT_INCIDENTS,
                    SADisplayConstants.ORG_CREATE_INCIDENT_REQUIRES_ADMIN,
                    SADisplayConstants.PARENT_ORG_ID));

            if(org.getCountryId() > 0) {
                fields.add(SADisplayConstants.COUNTRY_ID);
            }

            QueryModel model = QueryManager.createQuery(SADisplayConstants.ORG_TABLE).insertInto(fields)
                    .returnValue(SADisplayConstants.ORG_ID);

            try {
                //this.template.update(model.toString(), new BeanPropertySqlParameterSource(org));
                int orgId = this.template.queryForObject(model.toString(), new BeanPropertySqlParameterSource(org),
                        Integer.class);

                if(orgId <= 0) {
                    log.warn("Inserted org returned an invalid orgId: {}", orgId);
                }

                return orgId;
            } catch(Exception e) {
                throw new Exception("Unhandled exception while persisting Org entity:", e);
            }
        }
    }

    public int addOrgOrgType(int orgId, int orgTypeId) throws Exception {
        List<String> fields = Arrays.asList(
                SADisplayConstants.ORG_TYPE_ID,
                SADisplayConstants.ORG_ID);

        QueryModel model = QueryManager.createQuery(SADisplayConstants.ORG_ORGTYPE_TABLE).insertInto(fields);

        try {
            return this.template.update(model.toString(), new MapSqlParameterSource(
                    SADisplayConstants.ORG_TYPE_ID, orgTypeId)
                    .addValue(SADisplayConstants.ORG_ID, orgId));
        } catch(Exception e) {
            throw new Exception("Unhandled exception while persisting Org entity:", e);
        }
    }

    public List<OrgCap> getOrgCaps(int orgId) {

        QueryModel query = QueryManager.createQuery(SADisplayConstants.ORG_CAP_TABLE).selectAllFromTable()
                .left().join(SADisplayConstants.CAP_TABLE).using(SADisplayConstants.CAP_ID)
                .where().equals(SADisplayConstants.ORG_ID);

        JoinRowCallbackHandler<OrgCap> handler = getOrgCapHandlerWith(new CapRowMapper());

        try {
            this.template.query(query.toString(), new MapSqlParameterSource(SADisplayConstants.ORG_ID, orgId), handler);

            return handler.getResults();
        } catch(Exception e) {
            log.info("No orgcaps found for org id #0", orgId);
        }

        return null;

    }

    public OrgCap updateOrgCaps(int orgCapId, String activeWeb, String activeMobile) {

        QueryModel query;
        int result = -1;

        if(activeWeb != null && activeMobile == null) {
            query = QueryManager.createQuery(SADisplayConstants.ORG_CAP_TABLE).update()
                    .equals(SADisplayConstants.ACTIVE_WEB, Boolean.parseBoolean(activeWeb)).comma()
                    .equals(SADisplayConstants.LAST_UPDATE, new Date())
                    .where().equals(SADisplayConstants.ORG_CAP_ID, orgCapId);
        } else if(activeWeb == null && activeMobile != null) {
            query = QueryManager.createQuery(SADisplayConstants.ORG_CAP_TABLE).update()
                    .equals(SADisplayConstants.ACTIVE_MOBILE, Boolean.parseBoolean(activeMobile)).comma()
                    .equals(SADisplayConstants.LAST_UPDATE, new Date())
                    .where().equals(SADisplayConstants.ORG_CAP_ID, orgCapId);
        } else {
            query = QueryManager.createQuery(SADisplayConstants.ORG_CAP_TABLE).update()
                    .equals(SADisplayConstants.ACTIVE_WEB, Boolean.parseBoolean(activeWeb)).comma()
                    .equals(SADisplayConstants.ACTIVE_MOBILE, Boolean.parseBoolean(activeMobile)).comma()
                    .equals(SADisplayConstants.LAST_UPDATE, new Date())
                    .where().equals(SADisplayConstants.ORG_CAP_ID, orgCapId);
        }

        try {

            if(activeWeb != null && activeMobile == null) {
                result = this.template.update(query.toString(),
                        new MapSqlParameterSource(SADisplayConstants.ORG_CAP_ID, orgCapId)
                                .addValue(SADisplayConstants.ACTIVE_WEB, Boolean.parseBoolean(activeWeb))
                                .addValue(SADisplayConstants.LAST_UPDATE, new Date()));
            } else if(activeWeb == null && activeMobile != null) {
                result = this.template.update(query.toString(),
                        new MapSqlParameterSource(SADisplayConstants.ORG_CAP_ID, orgCapId)
                                .addValue(SADisplayConstants.ACTIVE_MOBILE, Boolean.parseBoolean(activeMobile))
                                .addValue(SADisplayConstants.LAST_UPDATE, new Date()));
            } else {
                result = this.template.update(query.toString(),
                        new MapSqlParameterSource(SADisplayConstants.ORG_CAP_ID, orgCapId)
                                .addValue(SADisplayConstants.ACTIVE_WEB, Boolean.parseBoolean(activeWeb))
                                .addValue(SADisplayConstants.ACTIVE_MOBILE, Boolean.parseBoolean(activeMobile))
                                .addValue(SADisplayConstants.LAST_UPDATE, new Date()));
            }

            if(result != 1) {
                return null;
            }

        } catch(Exception e) {
            log.info("Unable to update orgcaps table with orgcap id #0 and activeWeb #1 and activeMobile #2"
                    , orgCapId, activeWeb, activeMobile);
        }

        query = QueryManager.createQuery(SADisplayConstants.ORG_CAP_TABLE).selectAllFromTable()
                .left().join(SADisplayConstants.CAP_TABLE).using(SADisplayConstants.CAP_ID)
                .where().equals(SADisplayConstants.ORG_CAP_ID);

        JoinRowCallbackHandler<OrgCap> handler = getOrgCapHandlerWith(new CapRowMapper());

        try {
            this.template.query(query.toString(), new MapSqlParameterSource(SADisplayConstants.ORG_CAP_ID, orgCapId),
                    handler);

            return handler.getSingleResult();
        } catch(Exception e) {
            log.info("No orgcap found for orgcap id #0", orgCapId);
        }

        return null;
    }

    public List<Cap> getCaps() {

        QueryModel query = QueryManager.createQuery(SADisplayConstants.CAP_TABLE)
                .selectAllFromTable();

        JoinRowCallbackHandler<Cap> handler = getCapHandlerWith();

        try {
            this.template.query(query.toString(), new MapSqlParameterSource(), handler);

            return handler.getResults();
        } catch(Exception e) {
            log.info("Unable to get caps");
        }

        return null;
    }


    /**
     * getOrgDatalayerIds
     *
     * @param orgid - id of the organization to search
     * @return List<String> - return a list of datalayer ids that belong to the given organization
     */
    public List<String> getOrgDatalayerIds(int orgid) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.DATALAYER_ORG_TABLE)
                .selectFromTable(SADisplayConstants.DATALAYER_ID)
                .where().equals(SADisplayConstants.ORG_ID);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put(SADisplayConstants.ORG_ID, orgid);
        return this.template.queryForList(queryModel.toString(), params, String.class);
    }



    /**
     * getHandlerWith
     *
     * @param mappers - optional additional mappers
     * @return JoinRowCallbackHandler<UserOrg>
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private JoinRowCallbackHandler<Org> getHandlerWith(JoinRowMapper... mappers) {
        return new JoinRowCallbackHandler(new OrgRowMapper(), mappers);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private JoinRowCallbackHandler<OrgType> getOrgTypeHandlerWith(JoinRowMapper... mappers) {
        return new JoinRowCallbackHandler(new OrgTypeRowMapper(), mappers);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private JoinRowCallbackHandler<OrgOrgType> getOrgOrgTypeHandlerWith(JoinRowMapper... mappers) {
        return new JoinRowCallbackHandler(new OrgOrgTypeRowMapper(), mappers);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private JoinRowCallbackHandler<OrgIncidentType> getOrgIncidentTypeHandlerWith(JoinRowMapper... mappers) {
        return new JoinRowCallbackHandler(new OrgIncidentTypeRowMapper(), mappers);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private JoinRowCallbackHandler<IncidentType> getIncidentTypeHandlerWith(JoinRowMapper... mappers) {
        return new JoinRowCallbackHandler(new IncidentTypeRowMapper(), mappers);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private JoinRowCallbackHandler<OrgCap> getOrgCapHandlerWith(JoinRowMapper... mappers) {
        return new JoinRowCallbackHandler(new OrgCapRowMapper(), mappers);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private JoinRowCallbackHandler<Cap> getCapHandlerWith(JoinRowMapper... mappers) {
        return new JoinRowCallbackHandler(new CapRowMapper(), mappers);
    }
}
