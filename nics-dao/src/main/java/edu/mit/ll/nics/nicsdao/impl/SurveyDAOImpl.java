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

import edu.mit.ll.nics.common.constants.SADisplayConstants;
import edu.mit.ll.nics.common.entity.Survey;
import edu.mit.ll.nics.common.entity.SurveyHistory;
import edu.mit.ll.nics.common.entity.SurveyResult;
import edu.mit.ll.nics.nicsdao.GenericDAO;
import edu.mit.ll.nics.nicsdao.QueryManager;
import edu.mit.ll.nics.nicsdao.SurveyDAO;
import edu.mit.ll.nics.nicsdao.exceptions.SurveyHistoryException;
import edu.mit.ll.nics.nicsdao.mappers.SurveyHistoryRowMapper;
import edu.mit.ll.nics.nicsdao.mappers.SurveyMetadataRowMapper;
import edu.mit.ll.nics.nicsdao.mappers.SurveyResultRowMapper;
import edu.mit.ll.nics.nicsdao.mappers.SurveyRowMapper;

import java.rmi.UnexpectedException;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import javax.sql.DataSource;
import org.postgresql.util.PGobject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


/**
 * DAO for Survey and SurveyResult tables
 */
public class SurveyDAOImpl extends GenericDAO implements SurveyDAO {

    /** Logger */
    private static Logger LOG = LoggerFactory.getLogger(SurveyDAOImpl.class);

    /** JDBC Template */
    private NamedParameterJdbcTemplate template;

    /**
     * Default constructor
     */
    public SurveyDAOImpl() {

    }

    /**
     * Constructor with custom datasource
     *
     * @param datasource custom datasource for use in stand-alone mode
     */
    public SurveyDAOImpl(DataSource datasource) {
        super(datasource);
    }

    @Override
    public void initialize() {

        try {
            this.template = new NamedParameterJdbcTemplate(datasource);
        } catch(Exception e) {
            e.printStackTrace();
            LOG.warn("Exception initializing datasource! Continuing to allow for setting manually with "
                    + "setDataSource()");
        }
    }


    @Override
    public int createSurvey(Survey survey) {
        int retval = -1; // Default to error value

        if(survey == null) {
            throw new IllegalArgumentException("survey cannot be null");
        }

        try {
            // TODO: ignoring timestamp on entity, and using now() on insert...

            List<String> fields = Arrays.asList(SADisplayConstants.SURVEY_TITLE, SADisplayConstants.SURVEY_SURVEY,
                    SADisplayConstants.SURVEY_CREATED);

            QueryModel query = QueryManager.createQuery(SADisplayConstants.SURVEY_TABLE)
                    .insertInto(fields).returnValue(SADisplayConstants.SURVEY_ID);

            MapSqlParameterSource params = new MapSqlParameterSource(SADisplayConstants.SURVEY_TITLE,
                    survey.getTitle());
            PGobject pgJson = new PGobject();
            pgJson.setType("json");
            pgJson.setValue(survey.getSurvey());
            params.addValue(SADisplayConstants.SURVEY_SURVEY, pgJson);
            params.addValue(SADisplayConstants.SURVEY_CREATED, survey.getCreated());

            int newid = this.template.queryForObject(query.toString(), params, Integer.class);

            if(newid > 0) {
                return newid;
            }

        } catch(DataAccessException e) {
            LOG.error("Exception creating survey", e);
            if(e instanceof DuplicateKeyException) {
                retval = -2;
            }
        } catch(Exception e) {
            LOG.error("Exception creating survey", e);
        }

        return retval;
    }

    @Override
    public Survey getById(int surveyId) {
        JoinRowCallbackHandler<Survey> handler = new JoinRowCallbackHandler<>(new SurveyRowMapper());

        try {
            QueryModel query = QueryManager.createQuery(SADisplayConstants.SURVEY_TABLE)
                    .selectAllFromTableWhere().equals(SADisplayConstants.SURVEY_ID);

            this.template.query(query.toString(),
                    new MapSqlParameterSource(SADisplayConstants.SURVEY_ID, surveyId), handler);
            Survey survey = handler.getSingleResult();
            if (survey != null) {
                return survey;
            } else {
                LOG.debug(String.format("Could not find survey with id: %d", surveyId));
            }
        } catch(DataAccessException e) {
            LOG.error("Exception getting survey", e);
        } catch(Exception e) {
            LOG.error("Exception getting survey", e);
        }

        return null;
    }

    @Override
    public Survey getByFormTypeName(String formTypeName) {

        try {
            QueryModel query = QueryManager.createQuery(SADisplayConstants.FORMTYPE_SURVEY_TABLE)
                    .selectFromTable(SADisplayConstants.SURVEY_ID)
                    .where().equals(SADisplayConstants.FORM_TYPE_NAME);

            int surveyId = this.template.queryForObject(query.toString(),
                    new MapSqlParameterSource(SADisplayConstants.FORM_TYPE_NAME, formTypeName), Integer.class);
            Survey survey = getById(surveyId);
            if (survey != null) {
                return survey;
            } else {
                LOG.debug(String.format("Could not find survey associated with formtypename: %s", formTypeName));
            }
        } catch(DataAccessException e) {
            LOG.error("Exception getting survey", e);
        } catch(Exception e) {
            LOG.error("Exception getting survey", e);
        }

        return null;
    }

    @Override
    public List<Survey> getSurveys() {
        return getSurveys(false);
    }

    @Override
    public List<Survey> getSurveys(boolean metadata) {

        JoinRowCallbackHandler<Survey> handler;
        if(metadata) {
            handler = new JoinRowCallbackHandler<>(new SurveyMetadataRowMapper());
        } else {
            handler = new JoinRowCallbackHandler<>(new SurveyRowMapper());
        }

        String which = "*";
        if(metadata) {
            which = "surveyid, title";
        }
        String sql = String.format("select %s from survey", which);

        try {
            this.template.query(sql, handler);
            List<Survey> surveys = handler.getResults();
            return surveys;
        } catch (DataAccessException e) {
            LOG.error("exception getting surveys", e);
        }

        return null;
    }

    private boolean copyToHistory(int surveyId) {

        Survey survey = getById(surveyId);
        if(survey == null) {
            return false;
        }

        SurveyHistory history = new SurveyHistory();
        history.setSurveyid(surveyId);
        history.setSurvey(survey.getSurvey());
        history.setTitle(survey.getTitle());
        history.setOriginaldate(survey.getCreated());

        int result = createSurveyHistory(history);

        return result > 0;
    }

    @Override
    public int updateSurvey(Survey survey) {

        if(survey == null || survey.getSurveyid() == 0) {
            final String msg = "Incoming Survey does not have a valid surveyid. Cannot update.";
            LOG.debug(msg);
            throw new IllegalArgumentException(msg);
        }

        if(!copyToHistory(survey.getSurveyid()) ) {
            // TODO: fail and don't update? Don't want to lose edit?
            throw new SurveyHistoryException("Failed to create copy of original Survey");
        }

        int rows = 0;

        try {
            QueryModel query = QueryManager.createQuery(SADisplayConstants.SURVEY_TABLE)
                    .update().equals(SADisplayConstants.SURVEY_SURVEY)
                    .where().equals(SADisplayConstants.SURVEY_ID, survey.getSurveyid());

            MapSqlParameterSource params = new MapSqlParameterSource(SADisplayConstants.SURVEY_ID,
                    survey.getSurveyid());

            PGobject pgJson = new PGobject();
            pgJson.setType("json");
            pgJson.setValue(survey.getSurvey());
            params.addValue(SADisplayConstants.SURVEY_SURVEY, pgJson);

            rows = this.template.update(query.toString(), params);

        } catch (DataAccessException e) {
            LOG.error("Exception updating survey", e);
            rows = -1;
        } catch(Exception e) {
            LOG.error("Exception updating survey", e);
            rows = -1;
        }

        return rows;
    }

    @Override
    public int removeSurvey(int surveyId) {
        int row;
        try {
            final String sql = "delete from survey where surveyid = ?";
            row = this.template.update(sql, new MapSqlParameterSource()
                    .addValue(SADisplayConstants.SURVEY_ID, surveyId));

        } catch(Exception e) {
            LOG.error("Exception removing survey", e);
            row = -1;
        }

        return row;
    }


    @Override
    public SurveyResult getSurveyResult(int resultId) {


        try {
            QueryModel query = QueryManager.createQuery(SADisplayConstants.SURVEYRESULT_SURVEYRESULT)
                    .selectAllFromTableWhere().equals(SADisplayConstants.SURVEYRESULT_ID);

            JoinRowCallbackHandler<SurveyResult> handler = new JoinRowCallbackHandler<>(new SurveyResultRowMapper());

            this.template.query(query.toString(),
                    new MapSqlParameterSource(SADisplayConstants.SURVEYRESULT_ID, resultId), handler);
            SurveyResult result = handler.getSingleResult();
            if (result != null) {
                LOG.debug("Found SurveyResult");
                return result;
            } else {
                LOG.debug("Could not find surveyresult with id: " + resultId);
            }
        } catch (DataAccessException e) {
            LOG.error("Exception getting surveyresult", e);
        } catch(Exception e) {
            LOG.error("Exception updating surveyresult", e);
        }

        return null;
    }

    @Override
    public List<SurveyResult> getSurveyResults() {
        QueryModel query = QueryManager.createQuery(SADisplayConstants.SURVEYRESULT_SURVEYRESULT).selectAllFromTable();
        JoinRowCallbackHandler<SurveyResult> handler = new JoinRowCallbackHandler<>(new SurveyResultRowMapper());

        try {
            template.query(query.toString(), handler);
            List<SurveyResult> results = handler.getResults();
            return results;
        } catch(Exception e) {
            LOG.error("Exception getting surveyresults", e);
        }

        return null;
    }

    @Override
    public List<SurveyResult> getSurveyResultsForSurvey(int surveyId) {
        if(surveyId <= 0) {
            LOG.debug("Invalid surveyid {}, not querying for results", surveyId);
            return null;
        }

        JoinRowCallbackHandler<SurveyResult> handler = new JoinRowCallbackHandler<>(new SurveyResultRowMapper());
        QueryModel query = QueryManager.createQuery(SADisplayConstants.SURVEYRESULT_SURVEYRESULT)
                .selectAllFromTableWhere().equals(SADisplayConstants.SURVEY_ID);

        try {
            this.template.query(query.toString(), new MapSqlParameterSource(SADisplayConstants.SURVEY_ID, surveyId),
                    handler);
            List<SurveyResult> results = handler.getResults();
            if(results != null && !results.isEmpty()) {
                LOG.debug("Found SurveyResults");
                return results;
            } else {
                LOG.debug("No surveyresults with surveyid: " + surveyId);
                return results; // does handler ever return null, or empty lists?
            }
        } catch (DataAccessException e) {
            LOG.error("Exception getting surveyresult", e);
        } catch(Exception e) {
            LOG.error("Exception updating surveyresult", e);
        }

        return null;
    }

    @Override
    public int createSurveyResult(SurveyResult result) {
        if(result == null) {
            throw new IllegalArgumentException("surveyresult cannot be null");
        }

        try {
            if (result.getCreated() == null) {
                result.setCreated(new Timestamp(System.currentTimeMillis()));
            } else {
                // TODO: validate timestamp? Do we want to allow old timestamp?
            }

            List<String> fields = new ArrayList<>();
            fields.add(SADisplayConstants.SURVEY_ID);
            fields.add(SADisplayConstants.SURVEYRESULT_SURVEYRESULT);
            fields.add(SADisplayConstants.SURVEYRESULT_USER);

            QueryModel query = QueryManager.createQuery(SADisplayConstants.SURVEYRESULT_SURVEYRESULT)
                    .insertInto(fields).returnValue(SADisplayConstants.SURVEYRESULT_ID);

            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(SADisplayConstants.SURVEY_ID, result.getSurveyid());
            PGobject pgJsonObject = new PGobject();
            pgJsonObject.setType("json");
            pgJsonObject.setValue(result.getSurveyresult());
            params.addValue(SADisplayConstants.SURVEYRESULT_SURVEYRESULT, pgJsonObject);
            params.addValue(SADisplayConstants.SURVEYRESULT_USER, result.getUserid());

            int newid = this.template.queryForObject(query.toString(), params, Integer.class);

            if(newid > 0) {
                return newid;
            }

        } catch(DataAccessException e) {
            LOG.error("Exception creating survey", e);
        } catch(Exception e) {
            LOG.error("Exception creating survey", e);
        }

        return -1;
    }

    // TODO: probably won't allow updating once submitted. It'll be a "version" at that point
    @Override
    public int updateSurveyResult(SurveyResult surveyResult) {
        if(surveyResult == null || surveyResult.getSurveyresultid() <= 0) {
            final String msg = "Incoming SurveyResult does not have a valid surveyresultid. Cannot update.";
            LOG.debug(msg);
            throw new IllegalArgumentException(msg);
        }

        int rows = 0;
        try {
            String sql = "update surveyresult set userid=?, surveyresult=cast(? as JSON), created=? where surveyresultid=?";

            BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(surveyResult);
            rows = this.template.update(sql, params);

        } catch (DataAccessException e) {
            LOG.error("Exception updating surveyResult", e);
            rows = -1;
        } catch(Exception e) {
            LOG.error("Exception updating surveyResult", e);
            rows = -1;
        }

        return rows;
    }

    @Override
    public int removeSurveyResult(int resultId) {
        int row;
        try {
            QueryModel query = QueryManager.createQuery(SADisplayConstants.SURVEYRESULT_SURVEYRESULT)
                    .deleteFromTableWhere().equals(SADisplayConstants.SURVEYRESULT_ID);

            row = this.template.update(query.toString(),
                    new MapSqlParameterSource(SADisplayConstants.SURVEYRESULT_ID, resultId));
            LOG.debug("Delete query result: " + row);

        } catch(Exception e) {
            LOG.error("Exception removing survey", e);
            row = -1;
        }

        return row;
    }

    @Override
    public int createSurveyHistory(SurveyHistory history) {
        if(history == null) {
            throw new IllegalArgumentException("SurveyHistory cannot be null");
        }

        try {
            if (history.getOriginaldate() == null) {
                // TODO: should be invalid if it doesn't have an OriginalDate... but don't want to fail to
                //  back it up, either... so set to current anyway?
                history.setOriginaldate(new Timestamp(System.currentTimeMillis()));
            } else {
                // TODO: validate timestamp? Do we want to allow old timestamp?
            }

            List<String> fields = new ArrayList<>();
            fields.add(SADisplayConstants.SURVEY_ID);
            fields.add(SADisplayConstants.SURVEY_TITLE);
            fields.add(SADisplayConstants.SURVEY_SURVEY);
            fields.add(SADisplayConstants.SURVEYHISTORY_ORIGINALDATE);
            //fields.add(SADisplayConstants.SURVEYHISTORY_ARCHIVEDDATE);

            QueryModel query = QueryManager.createQuery(SADisplayConstants.SURVEYHISTORY_TABLE)
                    .insertInto(fields).returnValue(SADisplayConstants.SURVEYHISTORY_ID);

            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(SADisplayConstants.SURVEY_ID, history.getSurveyid());
            params.addValue(SADisplayConstants.SURVEY_TITLE, history.getTitle());
            PGobject pgJsonObject = new PGobject();
            pgJsonObject.setType("json");
            pgJsonObject.setValue(history.getSurvey());
            params.addValue(SADisplayConstants.SURVEY_SURVEY, pgJsonObject);
            params.addValue(SADisplayConstants.SURVEYHISTORY_ORIGINALDATE, history.getOriginaldate());
            //params.addValue(SADisplayConstants.SURVEYHISTORY_ARCHIVEDDATE, history.getArchiveddate());

            int newid = this.template.queryForObject(query.toString(), params, Integer.class);

            if(newid > 0) {
                return newid;
            }

        } catch(DataAccessException e) {
            LOG.error("Exception creating survey", e);
        } catch(Exception e) {
            LOG.error("Exception creating survey", e);
        }

        return -1;
    }

    @Override
    public SurveyHistory getSurveyHistoryById(long surveyHistoryId) {
        JoinRowCallbackHandler<SurveyHistory> handler = new JoinRowCallbackHandler<>(new SurveyHistoryRowMapper());

        try {
            QueryModel query = QueryManager.createQuery(SADisplayConstants.SURVEYHISTORY_TABLE)
                    .selectAllFromTableWhere().equals(SADisplayConstants.SURVEYHISTORY_ID);

            this.template.query(query.toString(),
                    new MapSqlParameterSource(SADisplayConstants.SURVEYHISTORY_ID, surveyHistoryId), handler);
            SurveyHistory surveyHistory = handler.getSingleResult();
            if (surveyHistory != null) {
                return surveyHistory;
            } else {
                LOG.debug(String.format("Could not find survey history with id: %d", surveyHistoryId));
            }
        } catch(DataAccessException e) {
            LOG.error("Exception getting survey", e);
        } catch(Exception e) {
            LOG.error("Exception getting survey", e);
        }

        return null;
    }

    @Override
    public SurveyHistory getLatestSurveyHistoryForSurvey(int surveyId) {
        JoinRowCallbackHandler<SurveyHistory> handler = new JoinRowCallbackHandler<>(new SurveyHistoryRowMapper());

        try {
            QueryModel query = QueryManager.createQuery(SADisplayConstants.SURVEYHISTORY_TABLE)
                    .selectAllFromTableWhere().equals(SADisplayConstants.SURVEY_ID)
                    .orderBy(SADisplayConstants.SURVEYHISTORY_ARCHIVEDDATE).desc()
                    .limit("1");

            this.template.query(query.toString(),
                    new MapSqlParameterSource(SADisplayConstants.SURVEYHISTORY_ID, surveyId), handler);
            SurveyHistory surveyHistory = handler.getSingleResult();
            if (surveyHistory != null) {
                return surveyHistory;
            } else {
                LOG.debug(String.format("Could not find survey history with surveyid: %d", surveyId));
            }
        } catch(DataAccessException e) {
            LOG.error("Exception getting survey", e);
        } catch(Exception e) {
            LOG.error("Exception getting survey", e);
        }

        return null;
    }

    @Override
    public List<SurveyHistory> getSurveyHistory(int surveyId) {
        JoinRowCallbackHandler<SurveyHistory> handler = new JoinRowCallbackHandler<>(new SurveyHistoryRowMapper());
        List<SurveyHistory> results;
        try {
            QueryModel query = QueryManager.createQuery(SADisplayConstants.SURVEYHISTORY_TABLE)
                    .selectAllFromTableWhere().equals(SADisplayConstants.SURVEY_ID)
                    .orderBy(SADisplayConstants.SURVEYHISTORY_ARCHIVEDDATE).desc();

            this.template.query(query.toString(),
                    new MapSqlParameterSource(SADisplayConstants.SURVEY_ID, surveyId), handler);
            results = handler.getResults();
            if (results != null) {
                return results;
            } else {
                LOG.debug(String.format("Could not find survey history with surveyid: %d", surveyId));
            }
        } catch(DataAccessException e) {
            LOG.error("Exception getting survey", e);
        } catch(Exception e) {
            LOG.error("Exception getting survey", e);
        }

        return new ArrayList<>();
    }

    @Override
    public int removeSurveyHistory(int surveyHistoryId) {
        int row;
        try {
            final String sql = "delete from surveyhistory where surveyhistoryid = ?";
            row = this.template.update(sql, new MapSqlParameterSource()
                    .addValue(SADisplayConstants.SURVEYHISTORY_ID, surveyHistoryId));

        } catch(Exception e) {
            LOG.error("Exception removing survey", e);
            row = -1;
        }

        return row;    }

    @Override
    public int removeSurveyHistories(int surveyId) {
        int row;
        try {
            final String sql = "delete from surveyhistory where surveyid = ?";
            row = this.template.update(sql, new MapSqlParameterSource()
                    .addValue(SADisplayConstants.SURVEY_ID, surveyId));

        } catch(Exception e) {
            LOG.error("Exception removing survey", e);
            row = -1;
        }

        return row;
    }
}
