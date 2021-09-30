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
import edu.mit.ll.nics.common.entity.SurveyResult;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * RowMapper for SurveyResult table
 */
public class SurveyResultRowMapper extends JoinRowMapper<SurveyResult> {
    public SurveyResultRowMapper() {
        super("surveyresult");
    }

    /**
     * Mapping for a row
     *
     * @param rs
     * @param rowNum
     *
     * @return SurveyResult
     *
     * @throws SQLException
     */
    @Override
    public SurveyResult createRowObject(ResultSet rs, int rowNum) throws SQLException {
        // initialize Survey
        SurveyResult s = new SurveyResult();
        s.setSurveyresultid(rs.getInt(SADisplayConstants.SURVEYRESULT_ID));
        s.setSurveyid(rs.getInt(SADisplayConstants.SURVEY_ID));
        s.setUserid(rs.getString(SADisplayConstants.SURVEYRESULT_USER));

        /*JSONObject surveyJson = null;
        try {
            surveyJson = new JSONObject(rs.getString(Constants.SURVEYRESULT_SURVEYRESULT));
        } catch (JSONException e) {
            throw new SQLException("JSON exception reading surveyresult", e);
        }
        s.setSurveyresult(surveyJson);*/
        s.setSurveyresult(rs.getString(SADisplayConstants.SURVEYRESULT_SURVEYRESULT));

        s.setCreated(rs.getTimestamp(SADisplayConstants.SURVEY_CREATED));

        return s;
    }

    @Override
    public Object getKey(ResultSet rs) throws SQLException {
        return rs.getInt(SADisplayConstants.SURVEYRESULT_ID);
    }
}
