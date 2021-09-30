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
package edu.mit.ll.nics.nicsdao;

import edu.mit.ll.nics.common.entity.OrgSymbology;
import edu.mit.ll.nics.common.entity.Symbology;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;


/**
 * Symbology DAO Interface. Manages CRUD operations on the Symbology and related entities.
 */
public interface SymbologyDAO extends BaseDAO {

    /**
     * Fetch all Symbology entities.
     *
     * @param filter Filter parameters to filter the symbologies by
     *
     * @return a collection of Symbology entities if found, an empty collection otherwise
     *
     * @throws DataAccessException when there was an exception trying to access the data
     */
    List<Symbology> getAllSymbology(Map<String, Object> filter);

    /**
     * Fetch an existing Symbology entity via its identifier.
     *
     * @param symbologyId identifier of the Symbology to retrieve
     *
     * @return a Symbology entity if found, null otherwise
     *
     * @throws DataAccessException when there was an exception trying to access the data
     */
    Symbology getSymbologyById(int symbologyId);

    /**
     * Fetch Symbology by Organization identifier.
     *
     * @param orgId identifier of the Organization to fetch Symbology by
     *
     * @return a Symbology entity if found, null otherwise
     *
     * @throws DataAccessException when there was an exception trying to access the data
     */
    List<Symbology> getSymbologyByOrgId(int orgId);

    /**
     * Persist a Symbology entity.
     *
     * @param symbology the Symbology entity to persist
     *
     * @return the Symbology entity created
     *
     * @throws DataAccessException when there was an exception trying to access the data
     */
    Symbology createSymbology(Symbology symbology);

    /**
     * Update a Symbology entity (only updates name, symbology, owner, and listing).
     *
     * @param symbology the existing symbology entity to update, must have a valid, existing symbologyId
     *
     * @return the updated Symbology entity if successful, null otherwise
     *
     * @throws DataAccessException when there was an exception trying to access the data
     */
    Symbology updateSymbology(Symbology symbology);

    /**
     * Delete the Symbology with the specified identifier.
     *
     * @param symbologyId identifier of the Symbology to delete
     *
     * @return number of rows affected by delete operation, 1 if successful, 0 otherwise
     *
     * @throws DataAccessException when there was an exception trying to access the data
     */
    int delete(int symbologyId);

    /**
     * Utility method to see if a Symbology with the given identifier exists.
     *
     * @param symbologyId identifier of the Symbology to check for
     *
     * @return true if found, false otherwise
     *
     * @throws DataAccessException when there was an exception trying to access the data
     */
    boolean exists(int symbologyId);

    /**
     * Responsible for creating an Organization Symbology mapping.
     *
     * @param orgId       identifier of the Organization to map to the specified Symbology
     * @param symbologyId identifier of the Symbology to map to the specified Organization
     *
     * @return number of rows affected by the insert, 1 if successful, 0 otherwise
     *
     * @throws DataAccessException when there was an error trying to access the data
     */
    int createOrgSymbology(int orgId, int symbologyId);

    /**
     * Get all Org to Symbology mappings.
     *
     * @return a list of {@link OrgSymbology} entities
     */
    List<OrgSymbology> getOrgSymbologyMappings();

    /**
     * Responsible for deleting an Organization Symbology mapping.
     *
     * @param orgId       identifier of the Org
     * @param symbologyId identifier of the Symbology
     *
     * @return number of rows affected by the deletion, 1 if successful, 0 otherwise
     *
     * @throws DataAccessException when there was an exception trying to access the data
     */
    int deleteOrgSymbology(int orgId, int symbologyId);
}