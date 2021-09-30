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
package edu.mit.ll.nics.nicsdao.test.survey;

import edu.mit.ll.nics.common.entity.MobileDeviceTrack;
import edu.mit.ll.nics.common.entity.Survey;
import edu.mit.ll.nics.nicsdao.impl.MobileDeviceTrackDAOImpl;
import edu.mit.ll.nics.nicsdao.impl.SurveyDAOImpl;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests the SurveyDaoImpl methods
 */
public class SurveyTest {

    private int validSurvey1Id = -1;
    public static final String VALID_SURVEY_1_TITLE = "TEST Survey 1";
    public static final String VALID_SURVEY_1_SURVEY = "{\"field1\":\"value1\"}";
    public static final Timestamp VALID_SURVEY_1_TIMESTAMP = new Timestamp(System.currentTimeMillis());

    public static final String VALID_USERNAME = "nics-dao-test1@ll.mit.edu";
    public static final Double VALID_ALTITUDE = 57.5;
    public static final Integer VALID_WORKSPACEID = 1;
    public static final Timestamp VALID_TIMESTAMP = new Timestamp(System.currentTimeMillis());
    public static final String VALID_NAME = "Nics Dao";
    public static final Double VALID_LONGITUDE = -134.757472;
    public static final Double VALID_LATITUDE = 42.498242;
    public static final Double VALID_SPEED = 55.7;
    public static final String VALID_DEVICEID = "nicsdaotestdeviceid";
    public static final String VALID_DESCRIPTION = "This is a test description";
    public static final Double VALID_COURSE = 127.5;
    public static final Double VALID_ACCURACY = 0.59;
    public static final String VALID_EXTENDEDDATA = "{\"msg\":\"extended data\"}";

    public static final Double DOUBLE_DELTA = 0.0001;

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(SurveyTest.class);

    /**
     * SurveyDaoImpl
     */
    private SurveyDAOImpl surveyDAO;

    /**
     * DataSource
     */
    private DriverManagerDataSource dataSource;

    @BeforeTest
    public void beforeTest() {
        LOG.info("BeforeTest!");

        initDataSource();

        // Clean up data here

    }

    /**
     * Initializes the DAO
     */
    public void initDataSource() {
        dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/nics");
        dataSource.setUsername("nics");
        dataSource.setPassword("nicspassword");
        surveyDAO = new SurveyDAOImpl(dataSource);
    }


    /**
     * Removes Test Data
     */
    private void removeTestData() {
        // TODO: only delete tracks tests inserted, don't want to delete tracks someone may have in their db

        if(validSurvey1Id != -1) {
            surveyDAO.removeSurvey(validSurvey1Id);
        }

        /*NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
        LOG.info("Attempting to remove test MDT...");
        int mdtId = -1;
        try {
            mdtId = template.queryForObject(
                    "select mobiledevicetrackid from mobiledevicetrack where username=:username",
                    new MapSqlParameterSource("username", "nics-dao-test1@ll.mit.edu"), Integer.class);
        } catch(Exception e) {
            LOG.error("Failed to get mdtId, not removing mdt!", e);
            return;
        }
        LOG.info("\n!!!Retrieve test form ID result: {}", mdtId);

        int mdtResult = template.update("delete from mobiledevicetrack where mobiledevicetrackid=:mdtid",
                new MapSqlParameterSource("mdtid", mdtId));

        LOG.info("\n!!!Remove test MDT result: {}", mdtResult);*/
    }


    @Test(testName="createValidSurvey", description = "Inserts a valid Survey", groups = "write",
            dataProvider = "validSurveyProvider")
    public void testCreateValidSurvey(Survey survey) {
        int result = surveyDAO.createSurvey(survey);

        Survey newSurvey = surveyDAO.getById(result);
        validSurvey1Id = newSurvey.getSurveyid();

        Assert.assertNotEquals(validSurvey1Id, -1);
        Assert.assertEquals(VALID_SURVEY_1_TITLE, newSurvey.getTitle());
    }


















    // Data Providers

    @DataProvider(name = "validSurveyProvider")
    public Object[][] getValidSurvey() {

        Survey survey = new Survey();
        survey.setTitle(VALID_SURVEY_1_TITLE);
        survey.setSurvey(VALID_SURVEY_1_SURVEY);
        survey.setCreated(VALID_SURVEY_1_TIMESTAMP);

        return new Object[][] {{survey}};
    }


    //============================================ OLD ==========================================================

    /*@Test(testName = "InsertValidMDT", description = "Inserts a valid MDT", groups = "write", dataProvider =
            "validMdtProvider")
    public void testInsertMdt(MobileDeviceTrack mdt) {
        boolean result;
        try {
            //result = surveyDAO.insertOrUpdate(mdt);

            //Assert.assertEquals(result, true);

        } catch(Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Test(testName = "InsertInvalidMdt", description = "Inserts and MDT with a null deviceid", groups = "write",
            dataProvider = "nullDeviceIdMdtProvider")
    public void testInsertInvalidMdt(MobileDeviceTrack mdt) {
        boolean result = false;
        try {
            //result = surveyDAO.insertOrUpdate(mdt);
        } catch(Exception e) {
            LOG.error(e.getMessage(), e);
        }
        Assert.assertFalse(result);
    }

    *//**
     * Tests insertOrUpdate of an MDT with a bad lon, bad lat, and then both bad lon and lat
     *
     * @param mdt
     *//*
    @Test(testName = "updateInvalidLocation", description = "Inserts MDT with a bad latitude", groups = "write",
            dataProvider = "validMdtProvider")
    public void testUpdateInvalidLocation(MobileDeviceTrack mdt) {

        mdt.setAltitude(null);
        boolean nullAltitude = false;
        try {
            //nullAltitude = surveyDAO.insertOrUpdate(mdt);
        } catch(DataAccessException e) {
            System.out.print(e.getCause());
        }
        Assert.assertTrue(nullAltitude);
        mdt.setAltitude(VALID_ALTITUDE);

        mdt.setLatitude(null);
        boolean result = false;
        try {
            //result = surveyDAO.insertOrUpdate(mdt);
        } catch(Exception e) {
            LOG.error(e.getMessage(), e);
        }

        Assert.assertEquals(result, false);

        // Reset to valid latitude
        mdt.setLatitude(VALID_LATITUDE);

        // Set bad longitude
        mdt.setLongitude(null);

        boolean result2 = false;
        try {
            //result2 = surveyDAO.insertOrUpdate(mdt);
        } catch(Exception e) {
            LOG.error(e.getMessage(), e);
        }

        Assert.assertEquals(result2, false);

        // Now make both bad
        mdt.setLatitude(null);
        boolean result3 = false;
        try {
            //result3 = surveyDAO.insertOrUpdate(mdt);
        } catch(Exception e) {
            LOG.error(e.getMessage(), e);
        }

        Assert.assertEquals(result3, false);
    }


    @Test(testName = "sameUserDifferentDevices",
            description = "Tests the various allowable cases for the same user with different devices",
            groups = "write",
            dataProvider = "sameUserDifferentDevicesProvider")
    public void testSameUserDifferentDevices(MobileDeviceTrack mdtHasDeviceId, MobileDeviceTrack mdtNullWorkspaceId,
                                             MobileDeviceTrack mdtNewWorkspaceId) {

        boolean insertedMdtHasDeviceId = false;
        try {
            //insertedMdtHasDeviceId = surveyDAO.insertOrUpdate(mdtHasDeviceId);
        } catch(Exception e) {
            LOG.error(e.getMessage(), e);
        }
        Assert.assertEquals(insertedMdtHasDeviceId, true);

        boolean insertedMdtNullDeviceId = false;
        try {
            //insertedMdtNullDeviceId = surveyDAO.insertOrUpdate(mdtNullWorkspaceId);
        } catch(Exception e) {
            LOG.error(e.getMessage(), e);
        }
        Assert.assertEquals(insertedMdtNullDeviceId, true);

        boolean insertedMdtNewWorkspace = false;
        try {
            //insertedMdtNewWorkspace = surveyDAO.insertOrUpdate(mdtNewWorkspaceId);
        } catch(Exception e) {
            LOG.error(e.getMessage(), e);
        }
        Assert.assertEquals(insertedMdtNewWorkspace, true);

    }

    @Test(testName = "UpdateExisting", description = "Updates an existing track", dataProvider = "validMdtProvider",
            groups = "update", dependsOnGroups = "write")
    public void testUpdateToExisting(MobileDeviceTrack mdt) {

        mdt.setCourse(0.0);
        mdt.setSpeed(88.5);
        mdt.setAccuracy(42.0);
        mdt.setAltitude(15.3);
        mdt.setLatitude(45.29294);
        mdt.setLongitude(-122.34242);
        mdt.setTimestamp(new Timestamp(mdt.getTimestamp().getTime() + 15 * 60 * 1000));
        mdt.setDescription("Updated description");
        mdt.setExtendeddata("{\"testing\":\"updated\"}");

        MobileDeviceTrack current = getValidFromDb();
        Assert.assertNotNull(current);

        boolean updateResult = false;
        try {
            //updateResult = surveyDAO.insertOrUpdate(mdt);
        } catch(Exception e) {
            LOG.error(e.getMessage(), e);
        }
        Assert.assertTrue(updateResult);

        MobileDeviceTrack updated = getValidFromDb();
        Assert.assertNotNull(updated);

        Assert.assertEquals(updated.getCourse(), mdt.getCourse());
        Assert.assertEquals(updated.getSpeed(), mdt.getSpeed());
        Assert.assertEquals(updated.getAccuracy(), mdt.getAccuracy(), DOUBLE_DELTA);
        Assert.assertEquals(updated.getAltitude(), mdt.getAltitude(), DOUBLE_DELTA);
        Assert.assertEquals(updated.getLatitude(), mdt.getLatitude(), DOUBLE_DELTA);
        Assert.assertEquals(updated.getLongitude(), mdt.getLongitude(), DOUBLE_DELTA);
        Assert.assertEquals(updated.getTimestamp(), mdt.getTimestamp());
        Assert.assertEquals(updated.getDescription(), mdt.getDescription());
        Assert.assertEquals(updated.getExtendeddata(), mdt.getExtendeddata());
    }

    *//**
     * Helper function that gets the main "valid" mdt that was persisted in the insertValidMdt test
     *
     * @return
     *//*
    private MobileDeviceTrack getValidFromDb() {

        MobileDeviceTrack current = null;
        try {
           // current = surveyDAO.getMobileDeviceTrack(VALID_DEVICEID, VALID_USERNAME, VALID_WORKSPACEID);
        } catch(Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return current;
    }


    @DataProvider(name = "sameUserDifferentDevicesProvider")
    public Object[][] getMdtsSameUser() {

        MobileDeviceTrack mdtHasDeviceId = new MobileDeviceTrack();
        mdtHasDeviceId.setUsername("nics-mdt-test1@ll.mit.edu");
        mdtHasDeviceId.setAltitude(57.5);
        mdtHasDeviceId.setWorkspaceId(1);
        mdtHasDeviceId.setTimestamp(new Timestamp(System.currentTimeMillis()));
        mdtHasDeviceId.setName("Nics Dao");
        mdtHasDeviceId.setLongitude(-134.757472);
        mdtHasDeviceId.setLatitude(42.498242);
        mdtHasDeviceId.setSpeed(55.7);
        mdtHasDeviceId.setAltitude(23.572);
        mdtHasDeviceId.setDeviceId("nicsdaotestdeviceid");
        mdtHasDeviceId.setDescription("This is a test description");
        mdtHasDeviceId.setCourse(127.5);
        mdtHasDeviceId.setAccuracy(0.59);
        mdtHasDeviceId.setExtendeddata("{\"msg\":\"extended data\"}");

        MobileDeviceTrack mdtNullWorkspaceId = new MobileDeviceTrack(
                mdtHasDeviceId.getDeviceId(),
                mdtHasDeviceId.getUsername(),
                mdtHasDeviceId.getName(),
                mdtHasDeviceId.getCourse(),
                mdtHasDeviceId.getSpeed(),
                mdtHasDeviceId.getAltitude(),
                mdtHasDeviceId.getAccuracy(),
                new Timestamp(mdtHasDeviceId.getTimestamp().getTime() + 5 * 60 * 1000),
                mdtHasDeviceId.getDescription(),
                mdtHasDeviceId.getExtendeddata(),
                null,
                mdtHasDeviceId.getLongitude(),
                mdtHasDeviceId.getLatitude());

        MobileDeviceTrack mdtNewWorkspaceId = new MobileDeviceTrack(
                mdtHasDeviceId.getDeviceId(),
                mdtHasDeviceId.getUsername(),
                mdtHasDeviceId.getName(),
                mdtHasDeviceId.getCourse(),
                mdtHasDeviceId.getSpeed(),
                mdtHasDeviceId.getAltitude(),
                mdtHasDeviceId.getAccuracy(),
                new Timestamp(mdtHasDeviceId.getTimestamp().getTime() + 5 * 60 * 1000),
                mdtHasDeviceId.getDescription(),
                mdtHasDeviceId.getExtendeddata(),
                2,
                mdtHasDeviceId.getLongitude(),
                mdtHasDeviceId.getLatitude());

        return new Object[][] {{mdtHasDeviceId, mdtNullWorkspaceId, mdtNewWorkspaceId}};
    }

    @DataProvider(name = "validMdtProvider")
    public Object[][] getValidMDT() {

        MobileDeviceTrack mdt = new MobileDeviceTrack();
        mdt.setUsername(VALID_USERNAME);
        mdt.setAltitude(VALID_ALTITUDE);
        mdt.setWorkspaceId(VALID_WORKSPACEID);
        mdt.setTimestamp(VALID_TIMESTAMP);
        mdt.setName(VALID_NAME);
        mdt.setLongitude(VALID_LONGITUDE);
        mdt.setLatitude(VALID_LATITUDE);
        mdt.setSpeed(VALID_SPEED);
        mdt.setDeviceId(VALID_DEVICEID);
        mdt.setDescription(VALID_DESCRIPTION);
        mdt.setCourse(VALID_COURSE);
        mdt.setAccuracy(VALID_ACCURACY);
        mdt.setExtendeddata(VALID_EXTENDEDDATA);

        return new Object[][] {{mdt}};
    }

    @DataProvider(name = "nullUsernameMdtProvider")
    public Object[][] getNullUsernameMDT() {

        MobileDeviceTrack mdt = new MobileDeviceTrack();
        mdt.setUsername(null);
        mdt.setAltitude(57.5);
        mdt.setWorkspaceId(1);
        mdt.setTimestamp(new Timestamp(System.currentTimeMillis()));
        mdt.setName("Nics Dao");
        mdt.setLongitude(-134.757472);
        mdt.setLatitude(42.498242);
        mdt.setSpeed(55.7);
        mdt.setAltitude(23.572);
        mdt.setDeviceId("nicsdaotestdeviceid");
        mdt.setDescription("This is a test description");
        mdt.setCourse(127.5);
        mdt.setAccuracy(0.59);
        mdt.setExtendeddata("{\"msg\":\"extended data\"}");


        return new Object[][] {{mdt}};
    }

    @DataProvider(name = "nullDeviceIdMdtProvider")
    public Object[][] getNullDeviceIdMDT() {

        MobileDeviceTrack mdt = new MobileDeviceTrack();
        mdt.setUsername("nics-dao-test1@ll.mit.edu");
        mdt.setAltitude(57.5);
        mdt.setWorkspaceId(1);
        mdt.setTimestamp(new Timestamp(System.currentTimeMillis()));
        mdt.setName("Nics Dao");
        mdt.setLongitude(-134.757472);
        mdt.setLatitude(42.498242);
        mdt.setSpeed(55.7);
        mdt.setAltitude(23.572);
        mdt.setDeviceId(null);
        mdt.setDescription("This is a test description");
        mdt.setCourse(127.5);
        mdt.setAccuracy(0.59);
        mdt.setExtendeddata("{\"msg\":\"extended data\"}");

        return new Object[][] {{mdt}};
    }*/

    @AfterTest
    public void afterTest() {
        LOG.info("AfterTest!");
        removeTestData();
    }
}
