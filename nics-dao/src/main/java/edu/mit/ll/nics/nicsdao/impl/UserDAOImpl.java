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
import edu.mit.ll.nics.common.entity.Contact;
import edu.mit.ll.nics.common.entity.ContactType;
import edu.mit.ll.nics.common.entity.User;
import edu.mit.ll.nics.common.entity.UserFeature;
import edu.mit.ll.nics.common.entity.UserOrg;
import edu.mit.ll.nics.common.entity.UserOrgWorkspace;
import edu.mit.ll.nics.nicsdao.GenericDAO;
import edu.mit.ll.nics.nicsdao.QueryManager;
import edu.mit.ll.nics.nicsdao.UserDAO;
import edu.mit.ll.nics.nicsdao.mappers.ContactRowMapper;
import edu.mit.ll.nics.nicsdao.mappers.ContactTypeRowMapper;
import edu.mit.ll.nics.nicsdao.mappers.CurrentUserSessionRowMapper;
import edu.mit.ll.nics.nicsdao.mappers.FeatureRowMapper;
import edu.mit.ll.nics.nicsdao.mappers.OrgRowMapper;
import edu.mit.ll.nics.nicsdao.mappers.SystemRoleRowMapper;
import edu.mit.ll.nics.nicsdao.mappers.UserFeatureRowMapper;
import edu.mit.ll.nics.nicsdao.mappers.UserOrgRowMapper;
import edu.mit.ll.nics.nicsdao.mappers.UserOrgWorkspaceRowMapper;
import edu.mit.ll.nics.nicsdao.mappers.UserRowMapper;

import java.util.*;

import org.hibernate.dialect.identity.SybaseAnywhereIdentityColumnSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class UserDAOImpl extends GenericDAO implements UserDAO {

    private Logger log;

    private PlatformTransactionManager txManager;
    private NamedParameterJdbcTemplate template;

    @Override
    public void initialize() {
        log = LoggerFactory.getLogger(UserDAOImpl.class);
        if(datasource != null) {
            this.template = new NamedParameterJdbcTemplate(datasource);
            txManager = new DataSourceTransactionManager(datasource);
        }
    }

    /**
     * Create a new User.
     *
     * @param user {@link User} to persist
     * @return userid of persisted entity
     */
    private int create(User user) {

        ArrayList<String> fields = new ArrayList<String>();
        //fields.add(SADisplayConstants.USER_ID);
        fields.add(SADisplayConstants.FIRSTNAME);
        fields.add(SADisplayConstants.LASTNAME);
        fields.add(SADisplayConstants.USER_NAME);
        fields.add(SADisplayConstants.ACTIVE);

        user.setActive(true);

        BeanPropertySqlParameterSource parameters = new BeanPropertySqlParameterSource(user);
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.USER_ESCAPED)
                .insertInto(fields).returnValue(SADisplayConstants.USER_ID);

        return this.template.queryForObject(queryModel.toString(), parameters, Integer.class);
    }


    /**
     * Updates the users first and last name in the db
     *
     * @param userId
     * @param firstName
     * @param lastName
     * @return
     */
    public void updateNames(int userId, String firstName, String lastName) {

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.USER_TABLE)
                .update().equals(SADisplayConstants.FIRSTNAME).comma().equals(SADisplayConstants.LASTNAME)
                .where().equals(SADisplayConstants.USER_ID);

        MapSqlParameterSource map = new MapSqlParameterSource(SADisplayConstants.USER_ID, userId);
        map.addValue(SADisplayConstants.FIRSTNAME, firstName);
        map.addValue(SADisplayConstants.LASTNAME, lastName);

        try {
            this.template.update(queryModel.toString(), map);
        } catch(Exception e) {
            log.info("Failed up update user first and last name.", e.getMessage());
        }

    }

    public boolean validateUser(String username, long userId) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.USER_ESCAPED)
                .selectFromTable(SADisplayConstants.USER_ID)
                .where().equals(SADisplayConstants.USER_NAME);

        MapSqlParameterSource map = new MapSqlParameterSource(SADisplayConstants.USER_NAME, username);

        try {
            return (template.queryForObject(queryModel.toString(), map, Integer.class) == userId);
        } catch(Exception e) {
            log.info("Could not find user #0", username);
        }
        return false;
    }

    /**
     * createContact
     *
     * @param contact {@link Contact} to insert
     * @return the id of the {@link Contact} entity persisted if successful, -1 otherwise
     */
    public int createContact(Contact contact) {

        ArrayList<String> fields = new ArrayList<>();
        fields.add(SADisplayConstants.USER_ID);
        fields.add(SADisplayConstants.VALUE);
        fields.add(SADisplayConstants.CREATED);
        fields.add(SADisplayConstants.ENABLED);
        fields.add(SADisplayConstants.CONTACT_TYPE_ID);

        try {
            BeanPropertySqlParameterSource parameters = new BeanPropertySqlParameterSource(contact);

            QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.CONTACT_TABLE)
                    .insertInto(fields)
                    .returnValue(SADisplayConstants.CONTACT_ID);

            return this.template.queryForObject(queryModel.toString(), parameters, Integer.class);

        } catch(Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * getMyUserID
     *
     * @param username - String
     * @return Long
     */
    public Long getMyUserID(String username) {
        if(this.template == null) {
            this.initialize();
        }

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.USER_ESCAPED)
                .selectFromTableWhere(SADisplayConstants.USER_ID).equals(SADisplayConstants.USER_NAME);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put(SADisplayConstants.USER_NAME, username);
        try {
            // TODO: replaced deprecated queryForLong with queryForObject, TEST
            //return this.template.queryForLong(queryModel.toString(), params);
            return this.template.queryForObject(queryModel.toString(), params, Long.class);
        } catch(Exception e) {
            log.info("Could not find user id for username " + username);
        }
        return null;
    }

    /**
     * getUser
     *
     * @param username - String
     * @return User
     */
    public User getUser(String username) {
        if(this.template == null) {
            this.initialize();
        }

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.USER_ESCAPED)
                .selectAllFromTable()
                .join(SADisplayConstants.USER_ORG_TABLE).using(SADisplayConstants.USER_ID)
                .join(SADisplayConstants.ORG_TABLE).using(SADisplayConstants.ORG_ID)
                .left().join(SADisplayConstants.CONTACT_TABLE).using(SADisplayConstants.USER_ID)
                .left().join(SADisplayConstants.CONTACT_TYPE_TABLE).using(SADisplayConstants.CONTACT_TYPE_ID)
                .where().equals(SADisplayConstants.USER_NAME);

        JoinRowCallbackHandler<User> handler = getHandlerWith(
                new UserOrgRowMapper().attachAdditionalMapper(new OrgRowMapper()),
                new ContactRowMapper().attachAdditionalMapper(new ContactTypeRowMapper()));

        this.template.query(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.USER_NAME, username),
                handler);

        try {
            return handler.getSingleResult();
        } catch(Exception e) {
            log.warn("Error getting user with username: " + username, e);
        }
        return null;
    }

    /**
     * Get {@link User} with currentusersessionid
     *
     * @param userId ID of the user to retrieve
     * @return User matching the specified ID
     */
    public User getUserWithSession(long userId) {
        if(this.template == null) {
            this.initialize();
        }
        QueryModel query = QueryManager.createQuery(SADisplayConstants.USER_ESCAPED)
                .selectAllFromTable()
                .join(SADisplayConstants.CURRENT_USERSESSION_TABLE).using(SADisplayConstants.USER_ID)
                .where().equals(SADisplayConstants.USER_ID, userId);

        JoinRowCallbackHandler<User> handler = getHandlerWith(new CurrentUserSessionRowMapper());

        template.query(query.toString(), query.getParameters(), handler);

        return handler.getSingleResult();
    }

    public List<User> findUser(String firstName, String lastName, boolean exact) {
        if(this.template == null) {
            this.initialize();
        }

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.USER_ESCAPED)
                .selectAllFromTable().where();

        if(exact) {
            queryModel = queryModel.equals(SADisplayConstants.FIRSTNAME).and().equals(SADisplayConstants.LASTNAME);
        } else {
            queryModel = queryModel.ilike(SADisplayConstants.FIRSTNAME).value("'%" + firstName + "%'")
                    .and().ilike(SADisplayConstants.LASTNAME).value("'%" + lastName + "%'");
        }

        JoinRowCallbackHandler<User> handler = getHandlerWith();

        this.template.query(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.FIRSTNAME, firstName)
                        .addValue(SADisplayConstants.LASTNAME, lastName),
                handler);

        try {
            return handler.getResults();
        } catch(Exception e) {
            log.info("No user was found with firstName " + firstName);
        }
        return null;
    }

    public List<User> findUserByLastName(String lastName, boolean exact) {
        return this.findUserByField(SADisplayConstants.LASTNAME, lastName, exact);
    }

    public List<User> findUserByFirstName(String firstName, boolean exact) {
        return this.findUserByField(SADisplayConstants.FIRSTNAME, firstName, exact);
    }

    private List<User> findUserByField(String field, String value, boolean exact) {
        if(this.template == null) {
            this.initialize();
        }

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.USER_ESCAPED)
                .selectAllFromTable().where();

        if(exact) {
            queryModel = queryModel.equals(field);
        } else {
            queryModel = queryModel.ilike(field).value("'%" + value + "%'");
        }

        JoinRowCallbackHandler<User> handler = getHandlerWith();

        this.template.query(queryModel.toString(),
                new MapSqlParameterSource(field, value),
                handler);

        try {
            return handler.getResults();
        } catch(Exception e) {
            log.info("No user was found with value " + value);
        }
        return null;
    }

    /**
     * getUserById
     *
     * @param userId
     * @return User
     */
    public User getUserById(long userId) {
        if(this.template == null) {
            this.initialize();
        }

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.USER_ESCAPED)
                .selectAllFromTable()
                .join(SADisplayConstants.USER_ORG_TABLE).using(SADisplayConstants.USER_ID)
                .join(SADisplayConstants.ORG_TABLE).using(SADisplayConstants.ORG_ID)
                .left().join(SADisplayConstants.CONTACT_TABLE).using(SADisplayConstants.USER_ID)
                .left().join(SADisplayConstants.CONTACT_TYPE_TABLE).using(SADisplayConstants.CONTACT_TYPE_ID)
                .where().equals(SADisplayConstants.USER_ID);

        JoinRowCallbackHandler<User> handler = getHandlerWith(
                new UserOrgRowMapper().attachAdditionalMapper(new OrgRowMapper()),
                new ContactRowMapper().attachAdditionalMapper(new ContactTypeRowMapper()));

        this.template.query(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.USER_ID, userId),
                handler);

        try {
            return handler.getSingleResult();
        } catch(Exception e) {
            log.info("No user was found with userid " + userId);
        }
        return null;
    }

    /**
     * getUserById
     *
     * @param username
     * @return User
     */
    public long getUserId(String username) {
        if(this.template == null) {
            this.initialize();
        }

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.USER_ESCAPED)
                .selectFromTable(SADisplayConstants.USER_ID)
                .where().equals(SADisplayConstants.USER_NAME);

        return this.template.queryForObject(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.USER_NAME, username), Long.class);
    }

    /**
     * getAllUserInfoById
     *
     * @param userId
     * @return User
     */
    public User getAllUserInfoById(long userId) {
        if(this.template == null) {
            this.initialize();
        }

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.USER_ESCAPED)
                .selectAllFromTable()
                .where().equals(SADisplayConstants.USER_ID);

        JoinRowCallbackHandler<User> handler = getHandlerWith();

        this.template.query(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.USER_ID, userId),
                handler);

        try {
            return handler.getSingleResult();
        } catch(Exception e) {
            log.info("No user was found with userid " + userId);
        }
        return null;
    }

    /**
     * getUserBySessionId
     *
     * @param userSessionId
     * @return User
     */
    public User getUserBySessionId(long userSessionId) {
        if(this.template == null) {
            this.initialize();
        }

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.USER_ESCAPED)
                .selectAllFromTable()
                .join(SADisplayConstants.CURRENT_USERSESSION_TABLE).using(SADisplayConstants.USER_ID)
                .where().equals(SADisplayConstants.USERSESSION_ID);

        JoinRowCallbackHandler<User> handler = getHandlerWith();

        this.template.query(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.USERSESSION_ID, userSessionId),
                handler);

        try {
            return handler.getSingleResult();
        } catch(Exception e) {
            log.info("No user was found with userid " + userSessionId);
        }
        return null;
    }

    /**
     * getUserFeaturesState
     *
     * @param id - userid
     * @return List<String> - a list of features represented as JSON
     */
    public List<UserFeature> getUserFeatures(int id, int workspaceId) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.USER_FEATURE)
                .selectAllFromTable().join(SADisplayConstants.FEATURE).using(SADisplayConstants.FEATURE_ID)
                .where().equals(SADisplayConstants.USER_ID)
                .and().equals(SADisplayConstants.WORKSPACE_ID);

        JoinRowCallbackHandler<UserFeature> handler = getUserFeatureHandlerWith(new FeatureRowMapper());

        template.query(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.USER_ID, id)
                        .addValue(SADisplayConstants.WORKSPACE_ID, workspaceId),
                handler);
        return handler.getResults();
    }

    public List<User> getActiveUsers(int workspaceId) {
        QueryModel query = QueryManager.createQuery(SADisplayConstants.USER_ESCAPED)
                .selectAllFromTable()
                .join(SADisplayConstants.CURRENT_USERSESSION_TABLE).using(SADisplayConstants.USER_ID)
                .where().equals(SADisplayConstants.WORKSPACE_ID)
                .and().last24Hours(SADisplayConstants.LAST_SEEN);

        JoinRowCallbackHandler<User> handler = getHandlerWith(new CurrentUserSessionRowMapper());

        template.query(query.toString(), new MapSqlParameterSource(SADisplayConstants.WORKSPACE_ID, workspaceId),
                handler);

        return handler.getResults();
    }

    public User getUserByPastSessionId(long userSessionId) {
        if(this.template == null) {
            this.initialize();
        }
        StringBuffer fields = new StringBuffer();
        fields.append(SADisplayConstants.FIRST_NAME);
        fields.append(QueryBuilder.COMMA);
        fields.append(SADisplayConstants.LAST_NAME);

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.USER_ESCAPED)
                .selectAllFromTable()
                .join(SADisplayConstants.USER_ORG_TABLE).using(SADisplayConstants.USER_ID)
                .join(SADisplayConstants.USERSESSION_TABLE).using(SADisplayConstants.USER_ORG_ID)
                .where().equals(SADisplayConstants.USERSESSION_ID);

        JoinRowCallbackHandler<User> handler = getHandlerWith();

        this.template.query(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.USERSESSION_ID, userSessionId),
                handler);


        try {
            return handler.getSingleResult();
        } catch(Exception e) {
            log.info("No user was found with userid " + userSessionId);
        }
        return null;
    }

    public int isActive(String username) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.USER_ESCAPED)
                .selectFromTable(SADisplayConstants.USER_ID)
                .where().equals(SADisplayConstants.ACTIVE);

        MapSqlParameterSource map = new MapSqlParameterSource(SADisplayConstants.USER_NAME, username)
                .addValue(SADisplayConstants.ACTIVE, true);

        try {
            return template.queryForObject(queryModel.toString(), map, Integer.class);
        } catch(Exception e) {
            log.info("Could not find user #0", username);
        }
        return -1;
    }

    public List<User> getEnabledUsersInWorkspace(int workspaceId) {
        return this.getEnabledUsersInWorkspace(workspaceId, -1);
    }

    public List<User> getEnabledUsersInWorkspace(int workspaceId, int orgId) {

        MapSqlParameterSource map = new MapSqlParameterSource(SADisplayConstants.WORKSPACE_ID, workspaceId);

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT u.* FROM public.user u, userorg o, userorg_workspace w ")
                .append("WHERE u.userid = o.userid AND o.userorgid = w.userorgid AND ")
                .append("w.enabled = true and u.active = true and w.workspaceid = :")
                .append(SADisplayConstants.WORKSPACE_ID).append(" ");

        if(orgId != -1) {
            sb.append("and o.orgid = :" + SADisplayConstants.ORG_ID).append(" ");
            map.addValue(SADisplayConstants.ORG_ID, orgId);
        }

        sb.append("ORDER BY u.userid ASC");
        log.info("getEnabledUsersInWorkspace:\n" + sb.toString() + "\n=====\n");

        JoinRowCallbackHandler<User> handler = getHandlerWith();


        template.query(sb.toString(), map, handler);

        return handler.getResults();
    }


    public List<User> getUsersNotInOrg(int notInOrgId) {

        QueryModel queryUserId = QueryManager.createQuery(SADisplayConstants.USER_ORG_TABLE)
                .selectFromTable(SADisplayConstants.USER_ID)
                .join(SADisplayConstants.USER_ORG_WORKSPACE_TABLE).using(SADisplayConstants.USER_ORG_ID)
                .where().equals(SADisplayConstants.ORG_ID);

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.USER_ESCAPED)
                .selectAllFromTable()
                .where().equals(SADisplayConstants.ACTIVE)
                .and().notIn(SADisplayConstants.USER_ID, queryUserId.toString());

        JoinRowCallbackHandler<User> handler = getHandlerWith();

        template.query(queryModel.toString(), new MapSqlParameterSource(
                SADisplayConstants.ORG_ID, notInOrgId)
                .addValue(SADisplayConstants.ACTIVE, true), handler);

        return handler.getResults();
    }

    @Override
    public List<User> getUsers(String orgName) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.USER_ESCAPED)
                .selectAllFromTable()
                .join(SADisplayConstants.USER_ORG_TABLE).using(SADisplayConstants.USER_ID)
                .join(SADisplayConstants.ORG_TABLE).using(SADisplayConstants.ORG_ID)
                .join(SADisplayConstants.USER_ORG_WORKSPACE_TABLE).using(SADisplayConstants.USER_ORG_ID)
                .where().equals(SADisplayConstants.ORG_NAME);

        JoinRowCallbackHandler<User> handler = getHandlerWith();

        this.template
                .query(queryModel.toString(), new MapSqlParameterSource(SADisplayConstants.ORG_NAME, orgName), handler);

        return handler.getResults();
    }

    public List<Map<String, Object>> getUsers(int orgId, int workspaceId) {
        StringBuffer fields = new StringBuffer();
        fields.append(SADisplayConstants.USER_NAME);
        fields.append(QueryBuilder.COMMA);
        fields.append(SADisplayConstants.USER_ID);

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.USER_ESCAPED)
                .selectFromTable(fields.toString())
                .join(SADisplayConstants.USER_ORG_TABLE).using(SADisplayConstants.USER_ID)
                .join(SADisplayConstants.USER_ORG_WORKSPACE_TABLE).using(SADisplayConstants.USER_ORG_ID)
                .where().equals(SADisplayConstants.ORG_ID)
                .and().equals(SADisplayConstants.WORKSPACE_ID);

        return template.queryForList(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.ORG_ID, orgId)
                        .addValue(SADisplayConstants.WORKSPACE_ID, workspaceId));
    }

    public List<User> getAdminUsers(int userId, int workspaceId){

        QueryModel orgQueryModel = QueryManager.createQuery(SADisplayConstants.USER_ORG_TABLE)
                .selectFromTable(SADisplayConstants.ORG_ID)
                .where().equals(SADisplayConstants.SYSTEM_ROLE_ID)
                .and().equals(SADisplayConstants.USER_ID);

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.USER_TABLE)
                .selectAllFromTable()
                .join(SADisplayConstants.USER_ORG_TABLE).using(SADisplayConstants.USER_ID)
                .join(SADisplayConstants.ORG_TABLE).using(SADisplayConstants.ORG_ID)
                .join(SADisplayConstants.SYSTEM_ROLE_TABLE).using(SADisplayConstants.SYSTEM_ROLE_ID)
                .join(SADisplayConstants.USER_ORG_WORKSPACE_TABLE).using(SADisplayConstants.USER_ORG_ID)
                .join(SADisplayConstants.CONTACT_TABLE).using(SADisplayConstants.USER_ID)
                .join(SADisplayConstants.CONTACT_TYPE_TABLE).using(SADisplayConstants.CONTACT_TYPE_ID)
                .where().inAsSQL(SADisplayConstants.ORG_ID, orgQueryModel.toString())
                .and().equals(SADisplayConstants.WORKSPACE_ID)
                .orderBy(SADisplayConstants.LAST_NAME);

        try {
            JoinRowCallbackHandler<User> handler = getHandlerWith(
                    new UserOrgRowMapper().attachAdditionalMapper(new OrgRowMapper())
                            .attachAdditionalMapper(new UserOrgWorkspaceRowMapper())
                            .attachAdditionalMapper(new SystemRoleRowMapper()),
                    new ContactRowMapper().attachAdditionalMapper(new ContactTypeRowMapper())
            );

            this.template.query(queryModel.toString(),
                    new MapSqlParameterSource(SADisplayConstants.WORKSPACE_ID, workspaceId)
                    .addValue(SADisplayConstants.USER_ID,userId)
                    .addValue(SADisplayConstants.SYSTEM_ROLE_ID, SADisplayConstants.ADMIN_ROLE_ID),
                    handler);

            return handler.getResults();
        } catch(Exception e) {
            e.printStackTrace();
            //log.info("No email found for " + username + " matching " + email);
        }
        return null;
    }

    public List<User> getAllUsers(int workspaceId){
        //select * from "user" join userorg using (userid)
        // where orgid in (select orgid from userorg where systemroleid=4 and userid=2)
        List<String> fields = Arrays.asList(SADisplayConstants.USER_NAME,
                SADisplayConstants.USER_ID, SADisplayConstants.FIRST_NAME,
                SADisplayConstants.LAST_NAME, SADisplayConstants.ORG_NAME,
                SADisplayConstants.USER_ORG_WORKSPACE_ENABLED, SADisplayConstants.SYSTEM_ROLE_NAME);

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.USER_TABLE)
                .selectAllFromTable()
                .join(SADisplayConstants.USER_ORG_TABLE).using(SADisplayConstants.USER_ID)
                .join(SADisplayConstants.ORG_TABLE).using(SADisplayConstants.ORG_ID)
                .join(SADisplayConstants.SYSTEM_ROLE_TABLE).using(SADisplayConstants.SYSTEM_ROLE_ID)
                .join(SADisplayConstants.USER_ORG_WORKSPACE_TABLE).using(SADisplayConstants.USER_ORG_ID)
                .join(SADisplayConstants.CONTACT_TABLE).using(SADisplayConstants.USER_ID)
                .join(SADisplayConstants.CONTACT_TYPE_TABLE).using(SADisplayConstants.CONTACT_TYPE_ID)
                .where().equals(SADisplayConstants.WORKSPACE_ID)
                .orderBy(SADisplayConstants.LAST_NAME);

        try {
            JoinRowCallbackHandler<User> handler = getHandlerWith(
                    new UserOrgRowMapper().attachAdditionalMapper(new OrgRowMapper())
                            .attachAdditionalMapper(new UserOrgWorkspaceRowMapper())
                            .attachAdditionalMapper(new SystemRoleRowMapper()),
                    new ContactRowMapper().attachAdditionalMapper(new ContactTypeRowMapper())
            );

            this.template.query(queryModel.toString(),
                    new MapSqlParameterSource(SADisplayConstants.WORKSPACE_ID, workspaceId),
                    handler);

            return handler.getResults();
        } catch(Exception e) {
            e.printStackTrace();
            //log.info("No email found for " + username + " matching " + email);
        }
        return null;
    }

    public boolean verifyEmailAddress(String username, String email) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.CONTACT_TABLE)
                .selectFromTable(SADisplayConstants.VALUE)
                .join(SADisplayConstants.USER_ESCAPED).using(SADisplayConstants.USER_ID)
                .where().equals(SADisplayConstants.VALUE)
                .and().equals(SADisplayConstants.USER_NAME);

        try {
            this.template.queryForObject(queryModel.toString(),
                    new MapSqlParameterSource(SADisplayConstants.VALUE, email)
                            .addValue(SADisplayConstants.USER_NAME, username),
                    String.class);
            return true;
        } catch(Exception e) {
            log.info("No email found for " + username + " matching " + email);
        }
        return false;
    }

    public Contact getContact(String value) {
        QueryModel query = QueryManager.createQuery(SADisplayConstants.CONTACT_TABLE)
                .selectAllFromTable()
                .join(SADisplayConstants.USER_ESCAPED).using(SADisplayConstants.USER_ID)
                .where().equals(SADisplayConstants.VALUE, value);

        JoinRowCallbackHandler<Contact> handler = getContactHandlerWith(new UserRowMapper());

        if(this.template == null) {
            this.initialize();
        }

        this.template.query(query.toString(), new MapSqlParameterSource(SADisplayConstants.VALUE, value), handler);

        try {
            return handler.getSingleResult();
        } catch(Exception e) {
            return null;
        }
    }

    /**
     * Get the specified ContactTypeName entry. If there's more than one of that type for the User, then
     * only get the first one.
     *
     * @param username Username of user to get contact for
     * @param contactTypeName the 'type' name of the ContactType to retrieve
     *
     * @return the value of the specified contact if found, null otherwise
     */
    public String getContact(String username, String contactTypeName) {

        QueryModel userIdInnerSelect = QueryManager.createQuery(SADisplayConstants.USER_TABLE)
                .selectFromTable(SADisplayConstants.USER_ID)
                .where().equals(SADisplayConstants.USER_NAME);

        QueryModel contactTypeInnerSelect = QueryManager.createQuery(SADisplayConstants.CONTACT_TYPE_TABLE)
                .selectFromTable(SADisplayConstants.CONTACT_TYPE_ID)
                .where().equals(SADisplayConstants.TYPE);

        QueryModel query = QueryManager.createQuery(SADisplayConstants.CONTACT_TABLE)
                .selectDistinctFromTable(SADisplayConstants.VALUE)
                .join(SADisplayConstants.USER_TABLE).using(SADisplayConstants.USER_ID)
                .where().equalsInnerSelect(SADisplayConstants.USER_ID, userIdInnerSelect.toString())
                .and().equalsInnerSelect(SADisplayConstants.CONTACT_TYPE_ID, contactTypeInnerSelect.toString());

        final List<String> contactValues = this.template.queryForList(query.toString(),
                new MapSqlParameterSource(SADisplayConstants.USER_NAME, username)
                        .addValue(SADisplayConstants.TYPE, contactTypeName),
                String.class);

        if(contactValues != null && !contactValues.isEmpty()) {
            return contactValues.get(0);
        }

        return null;
    }

    public List<String> getUsersWithPermission(int collabroomid, int roleid) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.USER_ESCAPED)
                .selectFromTable(SADisplayConstants.USER_NAME)
                .join(SADisplayConstants.COLLAB_ROOM_PERMISSION_TABLE).using(SADisplayConstants.USER_ID)
                .where().equals(SADisplayConstants.COLLAB_ROOM_ID)
                .and().equals(SADisplayConstants.SYSTEM_ROLE_ID);

        return this.template.queryForList(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.COLLAB_ROOM_ID, collabroomid)
                        .addValue(SADisplayConstants.SYSTEM_ROLE_ID, roleid), String.class);
    }

    public int getContactTypeId(String name) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.CONTACT_TYPE_TABLE)
                .selectFromTableWhere(SADisplayConstants.CONTACT_TYPE_ID)
                .equals(SADisplayConstants.TYPE);
        try {
            return this.template.queryForObject(queryModel.toString(),
                    new MapSqlParameterSource(SADisplayConstants.TYPE, name), Integer.class);
        } catch(Exception e) {
            log.info("Could not retrieve the contact id for contact name " + name);
        }
        return -1;
    }

    public List<ContactType> getContactTypes() {
        QueryModel query = QueryManager.createQuery(SADisplayConstants.CONTACT_TYPE_TABLE)
                .selectAllFromTable();

        JoinRowCallbackHandler<ContactType> handler = getContactTypeHandlerWith();

        this.template.query(query.toString(), new MapSqlParameterSource(), handler);

        return handler.getResults();
    }

    public List<Contact> getContacts(String username, String type) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.CONTACT_TABLE)
                .selectAllFromTable()
                .join(SADisplayConstants.USER_ESCAPED).using(SADisplayConstants.USER_ID)
                .join(SADisplayConstants.CONTACT_TYPE_TABLE).using(SADisplayConstants.CONTACT_TYPE_ID)
                .where().equals(SADisplayConstants.USER_NAME)
                .and().equals(SADisplayConstants.TYPE)
                .and().equals(SADisplayConstants.CONTACT_ENABLED);

        JoinRowCallbackHandler<Contact> handler = getContactHandlerWith();

        this.template.query(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.USER_NAME, username)
                        .addValue(SADisplayConstants.TYPE, type)
                        .addValue(SADisplayConstants.CONTACT_ENABLED, true),
                handler);

        return handler.getResults();
    }

    public List<Contact> getAllUserContacts(String username) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.CONTACT_TABLE)
                .selectAllFromTable()
                .join(SADisplayConstants.USER_ESCAPED).using(SADisplayConstants.USER_ID)
                .where().equals(SADisplayConstants.USER_NAME)
                .and().equals(SADisplayConstants.CONTACT_ENABLED);

        JoinRowCallbackHandler<Contact> handler = getContactHandlerWith();

        this.template.query(queryModel.toString(),
                new MapSqlParameterSource(SADisplayConstants.USER_NAME, username)
                        .addValue(SADisplayConstants.CONTACT_ENABLED, true),
                handler);

        return handler.getResults();
    }

    public boolean deleteContact(int contactId) {

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.CONTACT_TABLE)
                .deleteFromTableWhere().equals(SADisplayConstants.CONTACT_ID);
        try {
            this.template
                    .update(queryModel.toString(), new MapSqlParameterSource(SADisplayConstants.CONTACT_ID, contactId));

        } catch(DataAccessException e) {
            return false;
        }

        return true;
    }

    public String getUsernameFromEmail(String emailAddress) {

        try {
            QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.USER_ESCAPED)
                    .selectFromTable(SADisplayConstants.USER_NAME)
                    .join(SADisplayConstants.CONTACT_TABLE).using(SADisplayConstants.USER_ID)
                    .where().equalsLower(SADisplayConstants.VALUE)
                    .and().equals(SADisplayConstants.CONTACT_ENABLED);

            return this.template.queryForObject(queryModel.toString(),
                    new MapSqlParameterSource(SADisplayConstants.VALUE, emailAddress.toLowerCase())
                            .addValue(SADisplayConstants.CONTACT_ENABLED, true), String.class);
        } catch(DataAccessException e) {
            return null;
        }

    }

    public int setUserActive(int userId, boolean active) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.USER_TABLE)
                .update().equals(SADisplayConstants.ACTIVE)
                .where().equals(SADisplayConstants.USER_ID);

        MapSqlParameterSource map = new MapSqlParameterSource(SADisplayConstants.ACTIVE, active)
                .addValue(SADisplayConstants.USER_ID, userId);

        return template.update(queryModel.toString(), map);
    }

    /**
     * Persist a {@link UserOrg}
     *
     * @param userOrg
     * @return the userorgid of the persisted entity
     */
    public int createUserOrg(UserOrg userOrg) {

        ArrayList<String> fields = new ArrayList<String>();
        //fields.add(SADisplayConstants.USER_ORG_ID);
        fields.add(SADisplayConstants.USER_ID);
        fields.add(SADisplayConstants.ORG_ID);
        fields.add(SADisplayConstants.SYSTEM_ROLE_ID);
        fields.add(SADisplayConstants.CREATED);
        fields.add(SADisplayConstants.RANK);
        fields.add(SADisplayConstants.DESCRIPTION);
        fields.add(SADisplayConstants.JOB_TITLE);

        userOrg.setCreated(Calendar.getInstance().getTime());

        BeanPropertySqlParameterSource parameters = new BeanPropertySqlParameterSource(userOrg);

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.USER_ORG_TABLE)
                .insertInto(fields)
                .returnValue(SADisplayConstants.USER_ORG_ID);

        log.debug("Creating UserOrg: {}", queryModel.toString());

        return this.template.queryForObject(queryModel.toString(), parameters, Integer.class);
    }

    /**
     * Creates/persists a {@link UserOrgWorkspace} entity. The default sequence is used; it it will not use the entity's
     * userorg_workspace_id.
     *
     * @param userOrgWorkspace
     * @return the userorg_workspace_id of the persisted entity if successful
     */
    public long createUserOrgWorkspace(UserOrgWorkspace userOrgWorkspace) {

        ArrayList<String> fields = new ArrayList<>();
        fields.add(SADisplayConstants.WORKSPACE_ID);
        fields.add(SADisplayConstants.USER_ORG_ID);
        fields.add(SADisplayConstants.ENABLED);

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.USER_ORG_WORKSPACE_TABLE)
                .insertInto(fields)
                .returnValue(SADisplayConstants.USER_ORG_WORKSPACE_ID);

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue(SADisplayConstants.WORKSPACE_ID, userOrgWorkspace.getWorkspaceid());
        map.addValue(SADisplayConstants.USER_ORG_ID, userOrgWorkspace.getUserorgid());
        map.addValue(SADisplayConstants.ENABLED, false);

        return this.template.queryForObject(queryModel.toString(), map, Integer.class);
    }

    /**
     * Creates a new {@link User}, that User's {@link Contact}s, {@link UserOrg}s, and associated relations.
     *
     * <p>No IDs are presumed to be set on these entities. They are retrieved as persisted, and
     * set accordingly on dependent entities.</p>
     *
     * <p>All entity creation is part of a transaction, so if there is a failure creating any
     * of the entities, all changes are rolled back.</p>
     *
     * @param user         User entity to persist
     * @param contacts     List of the User's Contacts
     * @param userOrgs     List of UserOrg mappings for any Orgs to add the User to
     * @param workspaceIds List of IDs of all workspaces to add the userorg entries to
     * @return true if successfully persisted User and all related entities, false otherwise
     */
    public boolean registerUser(User user, List<Contact> contacts, List<UserOrg> userOrgs,
                                List<Integer> workspaceIds) {

        boolean status = false;
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = txManager.getTransaction(txDef);

        try {
            // Any failure in this try results in a transaction rollback

            int userId = create(user); // Persist the User
            user.setUserId(userId); // Set actual persisted userid

            if(contacts != null && contacts.size() > 0) {
                for(Contact contact : contacts) {
                    contact.setUserId(userId);
                    createContact(contact);
                }
            }

            // Create UserOrgs and UserOrgWorkspace
            int newUserOrgId = -1;
            UserOrgWorkspace newUserOrgWorkspace = null;
            if(userOrgs != null && userOrgs.size() > 0) {
                for(UserOrg userOrg : userOrgs) {
                    userOrg.setUserId(userId);
                    newUserOrgId = createUserOrg(userOrg);
                    for(int workspaceId : workspaceIds) {
                        newUserOrgWorkspace = new UserOrgWorkspace(newUserOrgId, workspaceId);
                        createUserOrgWorkspace(newUserOrgWorkspace);
                    }
                }
            }

            txManager.commit(txStatus);
            status = true;
        } catch(Exception e) {
            e.printStackTrace();
            log.error("Exception during process of persisting User, rolling back", e);
            txManager.rollback(txStatus);
        }

        return status;
    }

    public boolean addUserToOrg(int userId, List<UserOrg> userOrgs,
                                List<Integer> workspaceIds) {

        boolean status = false;
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = txManager.getTransaction(txDef);

        try {
            // Create UserOrgs
            int newUserOrgId = -1;
            UserOrgWorkspace newUserOrgWorkspace = null;

            if(userOrgs != null && userOrgs.size() > 0) {
                for(UserOrg userOrg : userOrgs) {
                    userOrg.setUserId(userId);
                    newUserOrgId = createUserOrg(userOrg);
                    for(int workspaceId : workspaceIds) {
                        newUserOrgWorkspace = new UserOrgWorkspace(newUserOrgId, workspaceId);
                        createUserOrgWorkspace(newUserOrgWorkspace);
                    }
                }
            }

            txManager.commit(txStatus);
            status = true;
        } catch(Exception e) {
            log.error("Exception during process of persisting User, rolling back", e);
            txManager.rollback(txStatus);
        }


        return status;
    }

    @Deprecated
    public int getNextUserId() {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.USER_TABLE)
                .selectNextVal(SADisplayConstants.USER_ID);
        try {
            return this.template.queryForObject(queryModel.toString(), new HashMap<String, Object>(), Integer.class);
        } catch(Exception e) {
            log.info("Could not retrieve next user id " + e.getMessage());
        }
        return -1;
    }

    @Deprecated
    public int getNextContactId() {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.CONTACT_TABLE)
                .selectNextVal(SADisplayConstants.CONTACT_ID);
        try {
            return this.template.queryForObject(queryModel.toString(), new HashMap<String, Object>(), Integer.class);
        } catch(Exception e) {
            log.info("Could not retrieve next contact id " + e.getMessage());
        }
        return -1;
    }

    /**
     * getHandlerWith
     *
     * @param mappers - optional additional mappers
     * @return JoinRowCallbackHandler<UserFeature>
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private JoinRowCallbackHandler<UserFeature> getUserFeatureHandlerWith(JoinRowMapper... mappers) {
        return new JoinRowCallbackHandler(new UserFeatureRowMapper(), mappers);
    }

    /**
     * getHandlerWith
     *
     * @param mappers - optional additional mappers
     * @return JoinRowCallbackHandler<User>
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private JoinRowCallbackHandler<User> getHandlerWith(JoinRowMapper... mappers) {
        return new JoinRowCallbackHandler(new UserRowMapper(), mappers);
    }

    private JoinRowCallbackHandler<ContactType> getContactTypeHandlerWith(JoinRowMapper... mappers) {
        return new JoinRowCallbackHandler(new ContactTypeRowMapper(), mappers);
    }

    /**
     * getHandlerWith
     *
     * @param mappers - optional additional mappers
     * @return JoinRowCallbackHandler<User>
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private JoinRowCallbackHandler<Contact> getContactHandlerWith(JoinRowMapper... mappers) {
        return new JoinRowCallbackHandler(new ContactRowMapper(), mappers);
    }
}
