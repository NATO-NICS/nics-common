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
package edu.mit.ll.nics.common.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Transient;


/**
 * Incident
 */
public class Incident {

    private Integer incidentid;
    private Integer parentincidentid;
    private Usersession usersession;
    private Integer usersessionid;
    private String incidentname;
    private double lat;
    private double lon;
    private Date created;
    private Date lastupdate;
    private boolean active;
    private String folder;
    private String folderId;
    private String description;
    private String bounds;
    private Integer workspaceid;
    private Set<CollabRoom> collabrooms = new HashSet<>(0);
    private Set<IncidentIncidentType> incidentIncidenttypes = new HashSet<>(0);
    private Set<IncidentOrg> incidentOrgs = new HashSet<>(0);

    //	Used in nics-web to display tree structure for multi-incident-view
    @Transient
    private List<Incident> children = null;

    @Transient
    private Boolean leaf = true;

    public Incident() {
    }

    public Incident(Integer incidentid, String incidentname, double lat,
                    double lon, Date created, Date lastupdate, boolean active, String folder) {

        this.incidentid = incidentid;
        this.incidentname = incidentname;
        this.lat = lat;
        this.lon = lon;
        this.created = created;
        this.active = active;
        this.folder = folder;
        this.lastupdate = lastupdate;
    }

    public Incident(Integer incidentid, Usersession usersession,
                    String incidentname, double lat, double lon, Date created, Date lastupdate,
                    boolean active, String folder, String bounds, Set<CollabRoom> collabrooms,
                    Set<IncidentIncidentType> incidentIncidenttypes, Set<Form> forms, Set<IncidentOrg> incidentOrgs) {

        this.incidentid = incidentid;
        this.usersession = usersession;
        this.incidentname = incidentname;
        this.lat = lat;
        this.lon = lon;
        this.created = created;
        this.active = active;
        this.folder = folder;
        this.bounds = bounds;
        this.collabrooms = collabrooms;
        this.incidentIncidenttypes = incidentIncidenttypes;
        this.lastupdate = lastupdate;
        this.incidentOrgs = incidentOrgs;
    }

    public Integer getIncidentid() {
        return this.incidentid;
    }

    public void setIncidentid(Integer incidentid) {
        this.incidentid = incidentid;
    }

    public Integer getParentincidentid() {
        return this.parentincidentid;
    }

    public void setParentincidentid(Integer parentincidentid) {
        this.parentincidentid = parentincidentid;
    }

    public Usersession getUsersession() {
        return this.usersession;
    }

    public void setUsersession(Usersession usersession) {
        this.usersession = usersession;
    }

    public String getIncidentname() {
        return this.incidentname;
    }

    public void setIncidentname(String incidentname) {
        this.incidentname = incidentname;
    }

    public double getLat() {
        return this.lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return this.lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastUpdate() {
        return this.lastupdate;
    }

    public void setLastUpdate(Date lastupdate) {
        this.lastupdate = lastupdate;
    }

    public boolean getActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getFolder() {
        return this.folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getFolderId() {
        return this.folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBounds() {
        return this.bounds;
    }

    public void setBounds(String bounds) {
        this.bounds = bounds;
    }

    public Set<CollabRoom> getCollabrooms() {
        return this.collabrooms;
    }

    public void setCollabrooms(Set<CollabRoom> collabrooms) {
        this.collabrooms = collabrooms;
    }

    public Set<IncidentIncidentType> getIncidentIncidenttypes() {
        return this.incidentIncidenttypes;
    }

    public void setIncidentIncidenttypes(Set<IncidentIncidentType> incidentIncidenttypes) {
        this.incidentIncidenttypes = incidentIncidenttypes;
    }

    public Integer getUsersessionid() {
        return this.usersessionid;
    }

    public void setUsersessionid(Integer id) {
        this.usersessionid = id;
    }

    public Integer getWorkspaceid() {
        return this.workspaceid;
    }

    public void setWorkspaceid(Integer id) {
        this.workspaceid = id;
    }

    @Transient
    public List<Incident> getChildren() {
        return this.children;
    }

    @Transient
    public void setChildren(List<Incident> children) {
        if(this.children == null) {
            this.children = new ArrayList<Incident>();
        }
        this.children = children;
    }

    @Transient
    public Boolean getLeaf() {
        return this.leaf;
    }

    @Transient
    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    public Set<IncidentOrg> getIncidentorgs() {
        return this.incidentOrgs;
    }

    public void setIncidentorgs(Set<IncidentOrg> incidentOrgs) {
        this.incidentOrgs = incidentOrgs;
    }
}
