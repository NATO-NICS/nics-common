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
import edu.mit.ll.nics.common.entity.MediaStream;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Row Mapper for the MediaStream table. Constructs a {@link MediaStream} entity, initializing it with the id, title,
 * and url.
 */
public class MediaStreamRowMapper extends JoinRowMapper<MediaStream> {
    public MediaStreamRowMapper() {
        super(SADisplayConstants.MEDIASTREAM_TABLE);
    }

    @Override
    public MediaStream createRowObject(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong(SADisplayConstants.MEDIASTREAM_ID);
        if(id != 0) {
            MediaStream ms = new MediaStream();
            ms.setMsid(id);
            ms.setTitle(rs.getString(SADisplayConstants.MEDIASTREAM_TITLE));
            ms.setUrl(rs.getString(SADisplayConstants.MEDIASTREAM_URL));

            return ms;
        }

        return null;
    }

    @Override
    public Object getKey(ResultSet rs) throws SQLException {
        return rs.getLong(SADisplayConstants.MEDIASTREAM_ID);
    }
}