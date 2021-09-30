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

import edu.mit.ll.nics.common.entity.Incident;
import edu.mit.ll.nics.common.entity.IncidentOrg;
import edu.mit.ll.nics.common.entity.IncidentType;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;


public interface IncidentDAO extends BaseDAO {
    public List<Incident> getIncidents(int workspaceId);

    public List<Incident> getIncidents();

    public List<Map<String, Object>> getArchivedIncidentNames(String prefix, int workspaceid);

    public List<Map<String, Object>> getActiveIncidentNames(int orgid, int workspaceid);

    public void updateIncidentFolder(List<String> incidentNames, String folder, int workspaceid);

    public List<Map<String, Object>> getActiveIncidents(int workspaceId, int orgId, boolean active, String folderId);

    public List<Map<String, Object>> getIncidentMapAdmins(int incidentid, String roomname);

    public List<Incident> getNonArchivedIncidents(int workspaceid);

    public int setIncidentCenter(String incidentname);

    public Incident getIncident(int incidentid);

    public Incident getIncidentByName(String incidentname, int workspaceId);

    public int create(String incidentname, double lat, double lon, int usersessionid, int workspaceid, int parentid,
                      String description);

    public int createIncidentIncidentTypes(int incidentid, List<IncidentType> types);

    public List<IncidentType> getIncidentTypes();

    public int getIncidentId(String name);

    public List<Incident> getParentIncidents(int workspaceId);

    public List<Incident> getChildIncidents(int parentId);

    public List<String> getChildIncidentNames(List<String> incidentNames, int workspaceid);

    public List<String> getParentIncidentNames(List<String> incidentNames, int workspaceid);

    public List<Incident> getIncidentsByName(List<String> names, int workspaceid);

    public Incident updateIncident(int workspaceId, Incident incident);

    /**
     * Gets any orgIds that are attached to the specified incident via the incident_org table
     *
     * @param incidentId the incident to get incidentorg orgids from
     * @return a list of ogIds if any found, an empty list otherwise
     */
    List<Integer> getIncidentOrgIds(Integer incidentId);

    /**
     * Retrieves list of IncidentOrg objects for the given incidentId
     *
     * @param incidentId the incidentId to query
     * @return a list of IncidentOrg objects if any found, an empty list otherwise
     */
    List<IncidentOrg> getIncidentOrgs(Integer incidentId);

    /**
     * Gets the list of incidentIds that are in the incident org table that are NOT associated with the specified
     * orgId.
     *
     * @param orgId
     * @return list of Incident IDs if found, an empty list otherwise
     */
    List<Integer> getIncidentOrgIncidentIdsNotInOrg(Integer orgId);

    /**
     * Gets the ID of the Organization that created the Incident with the specified ID
     *
     * @param incidentId the ID of the incident to find the owning orgid for
     * @return the ID of the owning org if found
     *
     * @throws DataAccessException when there's a problem with the query
     */
    Integer getOwningOrgId(Integer incidentId) throws DataAccessException;

    /**
     * Persists one or more IncidentOrg entities. Each one is added separately, and a failure does not stop the rest
     * from being attempted. If the mapping already exists, a warning is logged, but it is treated as a successful
     * insert. The incidentId on the IncidentOrg objects must match the incidentId parameter, or else it will not be
     * persisted.
     *
     * @param workspaceId  the ID of the Workspace in use
     * @param incidentId   the ID of the Incident to add org restrictions for
     * @param username     the username of the user attempting to add these restrictions
     * @param userId       the ID of the User making the request
     * @param incidentOrgs a list of IncidentOrg entities to persist
     * @return a Map containing three entries:
     * <ul>
     *     <li>success - A List of orgIds that were successfully persisted</li>
     *     <li>fail - A list of orgIds that failed to be persisted</li>
     *     <li>messages - A list of Strings containing information pertaining to the successes/failures</li>
     * </ul>
     *
     * @throws Exception
     */
    Map<String, Object> addIncidentOrgs(Integer workspaceId, Integer incidentId, String username,
                                        Integer userId, List<IncidentOrg> incidentOrgs) throws Exception;

    /**
     * TODO: Don't want to use this unless we refactor to let the caller know exactly what was added, what was removed,
     *      like add and remove now return a Map instead of an int
     *
     * @param workspaceId
     * @param incidentId
     * @param username
     * @param userId
     * @param incidentOrgs
     *
     * @return
     *
     @Deprecated int updateIncidentOrgs(Integer workspaceId, Integer incidentId, String username, Integer userId,
     List<IncidentOrg> incidentOrgs);
     */

    /**
     * Removes the list of IncidentOrgs. If one wasn't there to remove, it's not a failure. IncidentID parameter must
     * match the incidentId on the incidentOrg object, or else it will not be removed.
     *
     * @param workspaceId  the active workspace ID
     * @param incidentId   the ID of the Incident these IncidentOrgs are associated with
     * @param username     the username of the User making the request
     * @param userId       the ID of the user making the request
     * @param incidentOrgs a list of IncidentOrgs to remove
     * @return a Map containing three entries:
     * <ul>
     *     <li>success - A List of orgIds that were successfully removed</li>
     *     <li>fail - A list of orgIds that failed to be removed</li>
     *     <li>messages - A list of Strings containing information pertaining to the successes/failures</li>
     * </ul>
     *
     * @throws Exception
     */
    Map<String, Object> removeIncidentOrgs(Integer workspaceId, Integer incidentId, String username, Integer userId,
                                           List<IncidentOrg> incidentOrgs) throws Exception;

    /**
     * Get all active incidents not locked down for the given list of orgIds
     *
     * @param workspaceId ID of active workspace
     * @param orgIds      list of orgIds allowed to see the queried incidents
     * @return
     */
    List<Incident> getIncidents(Integer workspaceId, List<Integer> orgIds);

    /**
     * Version of getIncidentOrg(int workspaceId) that restricts it to the orgIds specified
     *
     * @param workspaceId the ID of the active workspace
     * @param orgIds      the orgIds allowed
     * @return a map with incidentId and orgid pairs
     */
    List<Map<String, Object>> getIncidentOrgRestricted(int workspaceId, List<Integer> orgIds);

    List<IncidentType> getIncidentTypes(boolean defaultTypes);

    /**
     * Get an incidenttype by its id.
     *
     * @param incidentTypeId the id of the IncidentType to retrieve
     *
     * @return the IncidentType if found, null otherwise
     */
    IncidentType getIncidentTypeById(int incidentTypeId);

    /**
     * Create a new IncidentType.
     *
     * @param incidentType the IncidentType to persist
     *
     * @return the newly persisted IncidentType if successful, null otherwise
     */
    IncidentType createIncidentType(IncidentType incidentType);

    /**
     * Update the given IncidentType.
     *
     * @param incidentType the IncidentType to update
     *
     * @return the updated IncidentType if successful, null otherwise
     */
    IncidentType updateIncidentType(IncidentType incidentType);

    /**
     * Deletes the IncidentType specified by the incidentTypeId
     *
     * @param incidentTypeId the id of the IncidentType to delete
     *
     * @return true if successful, false otherwise
     *
     * @throws Exception when there's a data access issue, such as attempting to delete an IncidentType that
     *          is still being referenced by another entity
     */
    boolean deleteIncidentType(int incidentTypeId) throws Exception;

}
