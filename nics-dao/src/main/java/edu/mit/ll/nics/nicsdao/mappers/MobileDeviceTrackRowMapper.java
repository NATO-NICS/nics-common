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
package edu.mit.ll.nics.nicsdao.mappers;

import edu.mit.ll.jdbc.JoinRowMapper;
import edu.mit.ll.nics.common.constants.SADisplayConstants;
import edu.mit.ll.nics.common.entity.MobileDeviceTrack;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MobileDeviceTrackRowMapper extends JoinRowMapper<MobileDeviceTrack> {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(MobileDeviceTrackRowMapper.class);

    public MobileDeviceTrackRowMapper() {
        super(SADisplayConstants.MDT_TABLE);
    }

    @Override
    public MobileDeviceTrack createRowObject(ResultSet rs, int rowNum) throws SQLException {
        MobileDeviceTrack mdt = new MobileDeviceTrack();
        mdt.setMobileDeviceTrackId(rs.getInt(SADisplayConstants.MDT_ID));
        mdt.setAccuracy(rs.getDouble(SADisplayConstants.MDT_ACCURACY));
        mdt.setCourse(rs.getDouble(SADisplayConstants.MDT_COURSE));
        mdt.setDeviceId(rs.getString(SADisplayConstants.MDT_DEVICEID));
        mdt.setUsername(rs.getString(SADisplayConstants.USER_NAME));
        mdt.setExtendeddata(rs.getString(SADisplayConstants.MDT_EXTENDEDDATA));

        // Process Geometry fields
        PGgeometry pGgeometry = (PGgeometry) rs.getObject(SADisplayConstants.MDT_LOCATION);
        try {
            Point point = (Point) pGgeometry.getGeometry();
            mdt.setLongitude(point.getX());
            mdt.setLatitude(point.getY());
            mdt.setAltitude(point.getZ());
        } catch(Exception e) {
            throw new SQLException("Exception processing geometry from MobileDeviceTrack with ID " +
                    mdt.getMobileDeviceTrackId() + ": " + e.getMessage());
        }

        mdt.setName(rs.getString(SADisplayConstants.MDT_NAME));
        mdt.setSpeed(rs.getDouble(SADisplayConstants.MDT_SPEED));
        mdt.setTimestamp(rs.getTimestamp(SADisplayConstants.MDT_TIMESTAMP));
        mdt.setDescription(rs.getString(SADisplayConstants.DESCRIPTION));
        mdt.setWorkspaceId(rs.getInt(SADisplayConstants.WORKSPACE_ID));

        return mdt;
    }

    public Integer getKey(ResultSet rs) throws SQLException {
        return rs.getInt(SADisplayConstants.MDT_ID);
    }
}