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
package edu.mit.ll.nics.nicsdao.test.datalayer;

import edu.mit.ll.nics.common.entity.datalayer.Datalayer;
import edu.mit.ll.nics.nicsdao.DatalayerDAO;
import edu.mit.ll.nics.nicsdao.impl.DatalayerDAOImpl;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Tests the DatalayerDAOImpl methods
 */
public class DatalayerDAOTest {
    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(DatalayerDAOTest.class);

    /**
     * DatalayerDAO
     */
    private DatalayerDAO datalayerDao;

    /**
     * DataSource
     */
    private DriverManagerDataSource dataSource;

    @BeforeTest
    public void beforeTest() {
        LOG.info("BeforeTest!");

        initDataSource();
    }

    /**
     * Initializes the MDT DAO
     */
    public void initDataSource() {
        dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/nics");
        dataSource.setUsername("nics");
        dataSource.setPassword("nicspassword");

        datalayerDao = new DatalayerDAOImpl(dataSource);
    }

    @Test(testName = "GetDatalayerCollabrooms", description = "Get datalayers for Collabroom", groups = "read")
    public void testGetCollabRoomDatalayers() {
        final int[] collabrooms = new int[] {1083, 1039, 931, 896, 895};
        try {
            for(int id : collabrooms) {
                LOG.info("Searching for id: " + id);
                List<Datalayer> res = datalayerDao.getCollabRoomDatalayers(id);
                for(Datalayer d : res) {
                    LOG.info("Datalayer: " + d.getDisplayname());
                    Assert.assertNotNull(d);
                }

                Assert.assertNotNull(res);
            }
        } catch(Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @AfterTest
    public void afterTest() {
        LOG.info("AfterTest!");

    }
}
