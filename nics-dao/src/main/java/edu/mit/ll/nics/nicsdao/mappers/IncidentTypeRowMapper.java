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
import edu.mit.ll.nics.common.entity.IncidentType;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IncidentTypeRowMapper extends JoinRowMapper<IncidentType> {

    public IncidentTypeRowMapper() {
        super("incidenttype");
    }

    @Override
    public IncidentType createRowObject(ResultSet rs, int rowNum) throws SQLException {
        IncidentType incidentType = new IncidentType();
        incidentType.setIncidentTypeId(rs.getInt(SADisplayConstants.INCIDENT_TYPE_ID));
        incidentType.setIncidentTypeName(rs.getString(SADisplayConstants.INCIDENT_TYPE_NAME));
        incidentType.setDefaulttype(rs.getBoolean(SADisplayConstants.INCIDENT_TYPE_DEFAULTTYPE));
        return incidentType;
    }

    public Integer getKey(ResultSet rs) throws SQLException {
        return rs.getInt(SADisplayConstants.INCIDENT_TYPE_ID);
    }
}