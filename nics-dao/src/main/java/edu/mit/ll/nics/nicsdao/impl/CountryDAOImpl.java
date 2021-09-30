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
import edu.mit.ll.nics.common.entity.Country;
import edu.mit.ll.nics.common.entity.Region;
import edu.mit.ll.nics.nicsdao.CountryDAO;
import edu.mit.ll.nics.nicsdao.GenericDAO;
import edu.mit.ll.nics.nicsdao.QueryManager;
import edu.mit.ll.nics.nicsdao.mappers.CountryRowMapper;
import edu.mit.ll.nics.nicsdao.mappers.RegionRowMapper;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


public class CountryDAOImpl extends GenericDAO implements CountryDAO {

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

    /**
     * getCountries
     *
     * @return List<Country>
     */
    public List<Country> getCountries() {
        QueryModel query = QueryManager.createQuery(SADisplayConstants.COUNTRY_TABLE)
                .selectAllFromTable();

        JoinRowCallbackHandler<Country> handler = getHandlerWith();

        try {
            template.query(query.toString(), query.getParameters(), handler);

            return handler.getResults();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * getRegions
     *
     * @param countryId
     * @return All regions in that country
     */
    public List<Region> getRegions(int countryId) {
        QueryModel query = QueryManager.createQuery(SADisplayConstants.REGION_TABLE)
                .selectAllFromTable().where().equals(SADisplayConstants.COUNTRY_ID, countryId);

        JoinRowCallbackHandler<Region> handler = getRegionHandlerWith();

        try {
            template.query(query.toString(), query.getParameters(), handler);

            return handler.getResults();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * addRegion
     *
     * @param region
     * @return new region id
     */
    public int addRegion(Region region) {

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue(SADisplayConstants.REGION_NAME, region.getRegionName());
        map.addValue(SADisplayConstants.COUNTRY_ID, region.getCountryId());
        map.addValue(SADisplayConstants.REGION_CODE, region.getRegionCode());

        QueryModel query = QueryManager.createQuery(SADisplayConstants.REGION_TABLE)
                .insertInto(new ArrayList(map.getValues().keySet()),
                        SADisplayConstants.REGION_ID)
                .returnValue(SADisplayConstants.REGION_ID);

        return template.queryForObject(query.toString(), map, Integer.class);
    }

    /**
     * updateRegion
     *
     * @param region
     * @return number of rows updated
     */
    public int updateRegion(Region region) {

        QueryModel query = QueryManager.createQuery(SADisplayConstants.REGION_TABLE)
                .update().equals(SADisplayConstants.REGION_NAME).comma()
                .equals(SADisplayConstants.REGION_CODE)
                .where().equals(SADisplayConstants.REGION_ID);


        MapSqlParameterSource map = new MapSqlParameterSource(
                SADisplayConstants.REGION_ID, region.getRegionId());
        map.addValue(SADisplayConstants.REGION_NAME, region.getRegionName());
        map.addValue(SADisplayConstants.REGION_CODE, region.getRegionCode());

        return this.template.update(query.toString(), map);
    }

    /**
     * deleteRegion
     *
     * @param regionId
     * @return new region id
     */
    public boolean deleteRegion(int regionId) {

        QueryModel query = QueryManager.createQuery(SADisplayConstants.REGION_TABLE)
                .deleteFromTableWhere().equals(SADisplayConstants.REGION_ID);

        int result = this.template.update(
                query.toString(),
                new MapSqlParameterSource(SADisplayConstants.REGION_ID, regionId));

        return (result == 1);
    }

    /**
     * getHandlerWith
     *
     * @param mappers - optional additional mappers
     * @return JoinRowCallbackHandler<Alert>
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private JoinRowCallbackHandler<Country> getHandlerWith(JoinRowMapper... mappers) {
        return new JoinRowCallbackHandler(new CountryRowMapper(), mappers);
    }

    /**
     * getRegionHandlerWith
     *
     * @param mappers - optional additional mappers
     * @return JoinRowCallbackHandler<Alert>
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private JoinRowCallbackHandler<Region> getRegionHandlerWith(JoinRowMapper... mappers) {
        return new JoinRowCallbackHandler(new RegionRowMapper(), mappers);
    }
}
