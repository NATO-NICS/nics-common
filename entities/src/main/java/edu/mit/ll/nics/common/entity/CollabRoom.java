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
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.locationtech.jts.geom.Polygon;


public class CollabRoom {
    private static final long serialVersionUID = 1L;

    private int collabRoomId;
    private Incident incident;
    private int incidentid;
    private Usersession usersession;
    private int usersessionid;
    private String name;
    private Date created;
    private Polygon bounds;

    private Collection<Integer> adminUsers = new ArrayList<>();

    private Collection<Integer> readWriteUsers = new ArrayList<>();

    private Collection<Integer> readOnlyUsers = new ArrayList<>();

    private List<Map<String, Object>> incidentMapAdmins = new ArrayList<>();

    public CollabRoom() {
    }

    public CollabRoom(int collabRoomId, Usersession usersession, String name, Date created) {
        this.collabRoomId = collabRoomId;
        this.usersession = usersession;
        this.name = name;
        this.created = created;
    }

    public CollabRoom(int collabRoomId, Incident incident, Usersession usersession, String name, Date created,
                      Polygon bounds, Set<CollabroomFeature> features, Set<Chat> chats) {

        this.collabRoomId = collabRoomId;
        this.incident = incident;
        this.usersession = usersession;
        this.name = name;
        this.created = created;
        this.bounds = bounds;
    }

    public int getCollabRoomId() {
        return this.collabRoomId;
    }

    public void setCollabRoomId(int collabRoomId) {
        this.collabRoomId = collabRoomId;
    }

    public Incident getIncident() {
        return this.incident;
    }

    public void setIncident(Incident incident) {
        this.incident = incident;
    }

    public Usersession getUsersession() {
        return this.usersession;
    }

    public void setUsersession(Usersession usersession) {
        this.usersession = usersession;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Polygon getBounds() {
        return this.bounds;
    }

    public void setBounds(Polygon bounds) {
        this.bounds = bounds;
    }

    public int getIncidentid() {
        return incidentid;
    }

    public void setIncidentid(int incidentid) {
        this.incidentid = incidentid;
    }

    public int getUsersessionid() {
        return usersessionid;
    }

    public void setUsersessionid(int usersessionid) {
        this.usersessionid = usersessionid;
    }

    public Collection<Integer> getAdminUsers() {
        return this.adminUsers;
    }

    public Collection<Integer> getReadWriteUsers() {
        return this.readWriteUsers;
    }

    public Collection<Integer> getReadOnlyUsers() {
        return this.readOnlyUsers;
    }

    public void setAdminUsers(Collection<Integer> adminUsers) {
        this.adminUsers = adminUsers;
    }

    public void setReadWriteUsers(Collection<Integer> readWriteUsers) {
        this.readWriteUsers = readWriteUsers;
    }

    public void setReadOnlyUsers(Collection<Integer> readOnlyUsers) {
        this.readOnlyUsers = readOnlyUsers;
    }

    public List<Map<String, Object>> getIncidentMapAdmins() {
        return this.incidentMapAdmins;
    }

    public void setIncidentMapAdmins(List<Map<String, Object>> admins) {
        this.incidentMapAdmins = admins;
    }
}