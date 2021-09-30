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
import edu.mit.ll.nics.common.entity.Alert;
import edu.mit.ll.nics.nicsdao.AlertDAO;
import edu.mit.ll.nics.nicsdao.GenericDAO;
import edu.mit.ll.nics.nicsdao.QueryManager;
import edu.mit.ll.nics.nicsdao.mappers.AlertRowMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


/**
 * DAO implementation class for working with Alert entities
 */
public class AlertDAOImpl extends GenericDAO implements AlertDAO {

    /**
     * Logger
     */
    private Logger log;

    /**
     * NamedParameterJdbcTemplate used to perform queries
     */
    private NamedParameterJdbcTemplate template;


    /**
     * Initializes the log and the template with the datasource
     */
    @Override
    public void initialize() {
        log = LoggerFactory.getLogger(CollabRoomDAOImpl.class);
        this.template = new NamedParameterJdbcTemplate(datasource);
    }

    @Override
    public List<Alert> getAlerts(int incidentId, int userId) {
        checkInit();

        QueryModel alertUserQuery = QueryManager.createQuery(SADisplayConstants.ALERTUSER_TABLE)
                .selectFromTable(SADisplayConstants.ALERT_ID)
                .where()
                .open()
                .equals(SADisplayConstants.ALERTUSER_USER_ID, userId)
                .or().isNull(SADisplayConstants.ALERTUSER_USER_ID)
                .close()
                .and().equals(SADisplayConstants.INCIDENT_ID, incidentId);

        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.ALERT_TABLE)
                .selectAllFromTable().where().inAsSQL(SADisplayConstants.ALERT_ID, alertUserQuery.toString())
                .orderBy(SADisplayConstants.CREATED).desc();

        JoinRowCallbackHandler<Alert> handler = getHandlerWith();

        try {
            template.query(queryModel.toString(), alertUserQuery.getParameters(), handler);

            return handler.getResults();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Alert getAlert(int alertId) {
        QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.ALERT_TABLE)
                .selectAllFromTable().where().equals(SADisplayConstants.ALERT_ID, alertId);

        JoinRowCallbackHandler<Alert> handler = getHandlerWith();

        template.query(queryModel.toString(), queryModel.getParameters(), handler);

        return handler.getSingleResult();
    }


    @Override
    public boolean persistUserAlert(int alertId, int incidentId, int userId) {
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(SADisplayConstants.ALERT_ID, alertId);
            params.put(SADisplayConstants.INCIDENT_ID, incidentId);
            if(userId != -1) {
                params.put(SADisplayConstants.USER_ID, userId);
            }

            List<String> fields = new ArrayList<String>(params.keySet());
            QueryModel model = QueryManager.createQuery(SADisplayConstants.ALERTUSER_TABLE).insertInto(fields);

            this.template.update(model.toString(), params);
        } catch(Exception e) {
            return false;
        }

        return true;
    }

    /**
     * Persists an Alert, as well as inserts an SADisplayConstants.ALERT_USER_TABLE entry, for each specified
     * userid.<br/><br/>
     * <p>
     * If there's an exception in any phase, the whole transaction is rolled back.
     *
     * @param userIds Array of userids to map to this alert
     * @param alert   The Alert to persist
     * @return If successful, the alertid of the persisted Alert, otherwise -1
     *
     * @throws Exception
     */
    @Override
    public int persistAlert(Alert alert) throws Exception {
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(SADisplayConstants.MESSAGE, alert.getMessage());
            params.put(SADisplayConstants.CREATED, alert.getCreated());
            params.put(SADisplayConstants.USER_NAME, alert.getUsername());

            List<String> fields = new ArrayList<String>(params.keySet());
            QueryModel model = QueryManager.createQuery(SADisplayConstants.ALERT_TABLE).insertInto(fields)
                    .returnValue(SADisplayConstants.ALERT_ID);

            return this.template.queryForObject(model.toString(), params, Integer.class);
        } catch(Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int delete(int alertId) {
        checkInit();
        try {
            QueryModel queryModel = QueryManager.createQuery(SADisplayConstants.ALERTUSER_TABLE)
                    .deleteFromTableWhere().equals(SADisplayConstants.ALERT_ID, alertId);

            int ret = template.update(queryModel.toString(), queryModel.getParameters());

            if(ret == 1) {
                QueryModel alertQueryModel = QueryManager.createQuery(SADisplayConstants.ALERT_TABLE)
                        .deleteFromTableWhere().equals(SADisplayConstants.ALERT_ID, alertId);

                return template.update(alertQueryModel.toString(), alertQueryModel.getParameters());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Utility method for checking if the template needs initialized
     */
    private void checkInit() {
        if(this.template == null) {
            this.initialize();
        }
    }

    /**
     * getHandlerWith
     *
     * @param mappers - optional additional mappers
     * @return JoinRowCallbackHandler<Alert>
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private JoinRowCallbackHandler<Alert> getHandlerWith(JoinRowMapper... mappers) {
        return new JoinRowCallbackHandler(new AlertRowMapper(), mappers);
    }

}
