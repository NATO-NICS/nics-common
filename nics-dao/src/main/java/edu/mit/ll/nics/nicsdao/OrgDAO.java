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

import edu.mit.ll.nics.common.entity.Cap;
import edu.mit.ll.nics.common.entity.IncidentType;
import edu.mit.ll.nics.common.entity.Org;
import edu.mit.ll.nics.common.entity.OrgCap;
import edu.mit.ll.nics.common.entity.OrgType;
import edu.mit.ll.nics.common.entity.IncidentType;
import edu.mit.ll.nics.common.entity.OrgIncidentType;
import java.util.List;
import java.util.Map;


public interface OrgDAO extends BaseDAO {
    public List<String> getOrgFolderIds(int orgid);

    public List<String> getOrgAdmins(int orgid);

    public List<Org> getUserOrgs(int userid, int workspaceId);

    public List<Org> getAdminOrgs(int userid, int workspaceId);

    public List<Org> getUserOrgsByUsername(String username, int workspaceId);

    public List<String> getOrgNames();

    public List<Org> getOrgsByType(int orgtypeid);

    public int removeOrgOrgType(int orgId, int orgTypeId);

    public List<Org> getOrganizations();

    public List<Org> getOrganizations(List<Integer> orgIds);

    public List<OrgType> getOrgTypes();

    public Org getOrganization(String name);

    public String getOrgNameByAgency(String agency);

    public Org getLoggedInOrg(int userid);

    public String getDistributionList(int incidentid);

    public List<OrgCap> getOrgCaps(int orgId);

    public List<Cap> getCaps();

    public OrgCap updateOrgCaps(int orgCapId, String activeWeb, String activeMobile);

    public List<String> getOrgDatalayerIds(int orgid);

    /**
     * Retrieves organizationIds that have userorg_workspace entries for the specified workspaceId
     *
     * @param workspaceId the workspace to retrieve orgs for
     * @return a list of orgIds if found, an empty list otherwise
     */
    List<Integer> getOrganizationIds(int workspaceId);

    public List<OrgIncidentType> getIncidentTypes(int orgId);

    public List<IncidentType> getInactiveIncidentTypes(int orgId);

    /**
     * Retrieves a list of Organization identifier/Organization name pairs that have the specified
     * Incident Type identifier set as a default type.
     *
     * @param incidentTypeId the Incident Type identifier to query for
     *
     * @return a list of orgid/name pairs of Organizations that have the specified Incident Type as default
     */
    List<Map<String, Object>> getOrgsWithDefaultIncidentType(int incidentTypeId);

    /**
     * Retrieves a list of Organization identifier/Organization name pairs that have the specified
     * Incident Type identifier set as either active or inactive as specified by the 'active' parameter.
     *
     * @param incidentTypeId the Incident Type identifier to query for
     * @param active whether the Incident Type is set to active or inactive
     *
     * @return a list of orgid/name pairs of Organizations that match the search criteria
     */
    List<Map<String, Object>> getOrgsWithActiveIncidentType(int incidentTypeId, boolean active);

}
