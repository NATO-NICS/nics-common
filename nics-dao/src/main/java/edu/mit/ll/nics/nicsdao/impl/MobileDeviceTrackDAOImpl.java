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
import edu.mit.ll.nics.common.entity.MobileDeviceTrack;
import edu.mit.ll.nics.nicsdao.GenericDAO;
import edu.mit.ll.nics.nicsdao.MobileDeviceTrackDao;
import edu.mit.ll.nics.nicsdao.QueryManager;
import edu.mit.ll.nics.nicsdao.exceptions.InvalidMobileDeviceTrackingException;
import edu.mit.ll.nics.nicsdao.mappers.MobileDeviceTrackRowMapper;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * DAO For interacting with MobileDeviceTrack data
 */
public class MobileDeviceTrackDAOImpl extends GenericDAO implements MobileDeviceTrackDao {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(MobileDeviceTrackDAOImpl.class);

    /**
     * WellKnownText format for specifying a POINT with an SRID, e.g.: SRID=3875;POINT(longitude latitude altitude)
     */
    private static final String SRID_POINT_WKT = "SRID=%s;POINT(%s %s %s)";

    /**
     * SRID used by MDT for mapservers
     */
    private static final int MDT_SRID = 3857;

    /**
     * The SRID of lat/lon/alt
     */
    private static final int SRC_SRID = 4326;

    /**
     * WellKnownText for select on location to do transform to 3857
     */
    private static final String SELECT_LOCATION_TRANSFORM = String.format("ST_Transform(%s, %s) as %s",
            SADisplayConstants.MDT_LOCATION, SRC_SRID, SADisplayConstants.MDT_LOCATION);

    /**
     * WellKnownText for insert/update on location with transform
     */
    private static final String INSERT_LOCATION_TRANSFORM_TEMPLATE =
            "ST_Transform(ST_GeomFromText('POINT(%s %s %s)', %s), %s)";

    /**
     * List of ALL the fields, to be used when querying for a full MDT. Also includes location processing that the
     * {@link MobileDeviceTrackRowMapper} recognizes
     */
    private static final List<String> selectAllFields =
            Arrays.asList(SADisplayConstants.MDT_ID, SADisplayConstants.MDT_DEVICEID,
                    SADisplayConstants.USER_NAME, SADisplayConstants.MDT_NAME, SADisplayConstants.DESCRIPTION,
                    SADisplayConstants.MDT_COURSE, SADisplayConstants.MDT_SPEED, SADisplayConstants.MDT_ACCURACY,
                    SADisplayConstants.MDT_EXTENDEDDATA, SADisplayConstants.MDT_TIMESTAMP,
                    SADisplayConstants.WORKSPACE_ID, SELECT_LOCATION_TRANSFORM);

    /**
     * List of ALL the fields, to be used when INSERTING a full MDT
     */
    private static final List<String> insertAllFields = Arrays.asList(SADisplayConstants.MDT_DEVICEID,
            SADisplayConstants.USER_NAME, SADisplayConstants.MDT_NAME, SADisplayConstants.DESCRIPTION,
            SADisplayConstants.MDT_COURSE, SADisplayConstants.MDT_SPEED, SADisplayConstants.MDT_ACCURACY,
            SADisplayConstants.MDT_EXTENDEDDATA, SADisplayConstants.MDT_TIMESTAMP,
            SADisplayConstants.WORKSPACE_ID, SADisplayConstants.LOCATION);

    /**
     * List of the fields that are allowed to be updated
     */
    private static final List<String> updateFields = Arrays.asList(SADisplayConstants.DESCRIPTION,
            SADisplayConstants.MDT_COURSE, SADisplayConstants.MDT_SPEED, SADisplayConstants.MDT_ACCURACY,
            SADisplayConstants.MDT_EXTENDEDDATA, SADisplayConstants.MDT_TIMESTAMP, SADisplayConstants.LOCATION,
            SADisplayConstants.MDT_NAME);

    /**
     * JdbcTemplate
     */
    private NamedParameterJdbcTemplate template;


    /**
     * Default constructor
     */
    public MobileDeviceTrackDAOImpl() {
    }

    /**
     * Constructor with custom datasource
     *
     * @param datasource custom datasource for use in stand-alone mode
     */
    public MobileDeviceTrackDAOImpl(DataSource datasource) {
        super(datasource);
    }

    @Override
    public void initialize() {
        this.template = new NamedParameterJdbcTemplate(datasource);
    }


    @Override
    public boolean insertOrUpdate(MobileDeviceTrack mobileDeviceTrack) throws DataAccessException {

        MobileDeviceTrack currentTrack = getCurrentTrack(mobileDeviceTrack);
        if(currentTrack == null) {
            return insert(mobileDeviceTrack);
        } else {
            // Populate the new track with the id for update query
            mobileDeviceTrack.setMobileDeviceTrackId(currentTrack.getMobileDeviceTrackId());
            return update(mobileDeviceTrack);
        }
    }


    /**
     * Internal method for performing Insert. Only to be called by
     * {@link MobileDeviceTrackDAOImpl#insertOrUpdate(MobileDeviceTrack)}
     *
     * @param mobileDeviceTrack the {@link MobileDeviceTrack} to attempt to insert
     * @return true if successful, false otherwise
     *
     * @throws DataAccessException when there's an issue inserting
     */
    private boolean insert(MobileDeviceTrack mobileDeviceTrack) throws DataAccessException {

        QueryModel query = QueryManager.createQuery(SADisplayConstants.MDT_TABLE)
                .insertInto(insertAllFields);

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(SADisplayConstants.MDT_DEVICEID, mobileDeviceTrack.getDeviceId());
        params.addValue(SADisplayConstants.USER_NAME, mobileDeviceTrack.getUsername());
        params.addValue(SADisplayConstants.MDT_NAME, mobileDeviceTrack.getName());
        params.addValue(SADisplayConstants.DESCRIPTION, mobileDeviceTrack.getDescription());
        params.addValue(SADisplayConstants.MDT_COURSE, mobileDeviceTrack.getCourse());
        params.addValue(SADisplayConstants.MDT_SPEED, mobileDeviceTrack.getSpeed());
        params.addValue(SADisplayConstants.MDT_ACCURACY, mobileDeviceTrack.getAccuracy());
        params.addValue(SADisplayConstants.MDT_EXTENDEDDATA, mobileDeviceTrack.getExtendeddata());
        params.addValue(SADisplayConstants.MDT_TIMESTAMP, mobileDeviceTrack.getTimestamp());
        params.addValue(SADisplayConstants.WORKSPACE_ID, mobileDeviceTrack.getWorkspaceId());

        String queryWithLocation = query.toString();
        queryWithLocation = queryWithLocation.replace(":location",
                String.format(INSERT_LOCATION_TRANSFORM_TEMPLATE, mobileDeviceTrack.getLongitude(),
                        mobileDeviceTrack.getLatitude(),
                        (mobileDeviceTrack.getAltitude() == null) ? 0.0 : mobileDeviceTrack.getAltitude(),
                        SRC_SRID, MDT_SRID));

        int result = template.update(queryWithLocation, params);

        return result == 1;
    }

    /**
     * Internal method for performing Update. Only to be called by
     * {@link MobileDeviceTrackDAOImpl#insertOrUpdate(MobileDeviceTrack)}
     *
     * @param mobileDeviceTrack the {@link MobileDeviceTrack} to attempt to update
     * @return true if successfully updates, false otherwise
     *
     * @throws DataAccessException when there's an issue updating the row
     */
    private boolean update(MobileDeviceTrack mobileDeviceTrack) throws DataAccessException {

        QueryModel query = QueryManager.createQuery(SADisplayConstants.MDT_TABLE)
                .update(updateFields).where().equals(SADisplayConstants.MDT_ID);

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(SADisplayConstants.MDT_ID, mobileDeviceTrack.getMobileDeviceTrackId());
        params.addValue(SADisplayConstants.DESCRIPTION, mobileDeviceTrack.getDescription());
        params.addValue(SADisplayConstants.MDT_COURSE, mobileDeviceTrack.getCourse());
        params.addValue(SADisplayConstants.MDT_SPEED, mobileDeviceTrack.getSpeed());
        params.addValue(SADisplayConstants.MDT_ACCURACY, mobileDeviceTrack.getAccuracy());
        params.addValue(SADisplayConstants.MDT_EXTENDEDDATA, mobileDeviceTrack.getExtendeddata());
        params.addValue(SADisplayConstants.MDT_TIMESTAMP, mobileDeviceTrack.getTimestamp());
        params.addValue(SADisplayConstants.MDT_NAME, mobileDeviceTrack.getName());

        String tformQuery = query.toString();
        tformQuery = tformQuery.replace(":location", String.format(INSERT_LOCATION_TRANSFORM_TEMPLATE,
                mobileDeviceTrack.getLongitude(), mobileDeviceTrack.getLatitude(),
                (mobileDeviceTrack.getAltitude() == null) ? 0.0 : mobileDeviceTrack.getAltitude(),
                SRC_SRID, MDT_SRID));

        int result = template.update(tformQuery, params);

        return result == 1;
    }

    /**
     * Utility method for calling {@link MobileDeviceTrackDAOImpl#getMobileDeviceTrack(String, String, Integer)}
     *
     * @param mobileDeviceTrack the incoming mobileDeviceTrack object being checked
     * @return the track if found, null otherwise
     *
     * @throws DataAccessException when there's an issue querying for results
     */
    private MobileDeviceTrack getCurrentTrack(MobileDeviceTrack mobileDeviceTrack)
            throws DataAccessException {

        return getMobileDeviceTrack(mobileDeviceTrack.getDeviceId(), mobileDeviceTrack.getUsername(),
                mobileDeviceTrack.getWorkspaceId());
    }

    @Override
    public MobileDeviceTrack getTrackById(long mobileDeviceTrackId) throws DataAccessException {

        QueryModel query = QueryManager.createQuery(SADisplayConstants.MDT_TABLE)
                .selectFromTable(selectAllFields).where()
                .equals(SADisplayConstants.MDT_ID);

        JoinRowCallbackHandler<MobileDeviceTrack> handler = getHandlerWith();

        template.query(query.toString(), new MapSqlParameterSource(SADisplayConstants.MDT_ID, mobileDeviceTrackId),
                handler);

        MobileDeviceTrack mdt = handler.getSingleResult();

        LOG.debug("{} MDT with {}: {}", ((mdt == null) ? "Did NOT find " : "Found "),
                SADisplayConstants.MDT_ID, mobileDeviceTrackId);

        return mdt;
    }


    @Override
    public MobileDeviceTrack getMobileDeviceTrack(String deviceId, String username, Integer workspaceId)
            throws DataAccessException {

        MobileDeviceTrack mdt;

        MapSqlParameterSource params = new MapSqlParameterSource(SADisplayConstants.MDT_DEVICEID, deviceId)
                .addValue(SADisplayConstants.USER_NAME, username);

        QueryModel query = QueryManager.createQuery(SADisplayConstants.MDT_TABLE)
                .selectFromTable(selectAllFields).where().equals(SADisplayConstants.MDT_DEVICEID)
                .and().equals(SADisplayConstants.USER_NAME);

        if(workspaceId == null) {
            query = query.and().isNull(SADisplayConstants.WORKSPACE_ID);
        } else {
            query = query.and().equals(SADisplayConstants.WORKSPACE_ID);
            params.addValue(SADisplayConstants.WORKSPACE_ID, workspaceId);
        }

        JoinRowCallbackHandler<MobileDeviceTrack> handler = getHandlerWith();
        template.query(query.toString(), params, handler);

        mdt = handler.getSingleResult();

        if(mdt == null) {
            LOG.debug("Track with workspaceId({}), deviceId({}), and username({}) NOT found",
                    workspaceId, deviceId,
                    username);
        } else {
            LOG.debug("Found track {}, {}, {}", mdt.getWorkspaceId(),
                    mdt.getDeviceId(), mdt.getUsername());
        }

        return mdt;
    }

    @Override
    public boolean delete(String deviceId, String username, Integer workspaceId) throws DataAccessException {

        MapSqlParameterSource params = new MapSqlParameterSource(SADisplayConstants.MDT_DEVICEID, deviceId)
                .addValue(SADisplayConstants.USER_NAME, username);

        QueryModel query = QueryManager.createQuery(SADisplayConstants.MDT_TABLE)
                .deleteFromTableWhere().equals(SADisplayConstants.MDT_DEVICEID).and()
                .equals(SADisplayConstants.USER_NAME);

        if(workspaceId == null) {
            query = query.and().isNull(SADisplayConstants.WORKSPACE_ID);
        } else {
            params.addValue(SADisplayConstants.WORKSPACE_ID, workspaceId);
            query = query.and().equals(SADisplayConstants.WORKSPACE_ID);
        }

        int result = template.update(query.toString(), params);

        LOG.debug("Delete result on track with deviceid({}), username({}), and workspaceId({}): {}",
                deviceId, username, workspaceId, result);

        return result == 1;
    }

    /**
     * getHandlerWith
     *
     * @param mappers - optional additional mappers
     * @return JoinRowCallbackHandler<Log>
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private JoinRowCallbackHandler<MobileDeviceTrack> getHandlerWith(JoinRowMapper... mappers) {
        return new JoinRowCallbackHandler(new MobileDeviceTrackRowMapper(), mappers);
    }

    /**
     * Creates a PGgeometry object for persisting from the latitude, longitude, and altitude on the {@link
     * MobileDeviceTrack} along with the SRID specified by MDT_SRID
     *
     * @param mobileDeviceTrack the track containing the latitude, longitude, and altitude to form geometry from
     * @return a geometry point with the specified latitude, longitude, and srid
     *
     * @throws DataAccessException if there's a failure processing the lat/lon into a Geometry
     */
    @Deprecated
    private PGgeometry getGeometryFromMobileDeviceTrack(MobileDeviceTrack mobileDeviceTrack)
            throws DataAccessException {
        // Default to altitude of 0 if it's not set
        if(mobileDeviceTrack.getAltitude() == null) {
            mobileDeviceTrack.setAltitude(0D);
        }

        String locationWkt = String.format(SRID_POINT_WKT, SRC_SRID,
                mobileDeviceTrack.getLongitude(), mobileDeviceTrack.getLatitude(), mobileDeviceTrack.getAltitude());

        Point pgpoint;
        PGgeometry pggeom;
        try {
            pggeom = new PGgeometry();
            pgpoint = new Point(locationWkt);
            pggeom.setGeometry(pgpoint);
        } catch(SQLException e) {
            throw new InvalidMobileDeviceTrackingException("Failed to generate Point geometry from " +
                    String.format(SRID_POINT_WKT, SRC_SRID, mobileDeviceTrack.getLongitude(),
                            mobileDeviceTrack.getLatitude(), mobileDeviceTrack.getAltitude()));
        }

        return pggeom;
    }
}
