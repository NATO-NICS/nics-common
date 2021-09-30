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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public class UserOrg {

    private int userorgid;
    private Org org;
    private int orgId;
    private SystemRole systemrole;
    private User user;
    private Date created;
    private String unit;
    private String rank;
    private Set<Usersession> usersessions = new HashSet<>(0);
    private String description;
    private String jobTitle;
    private int userId;
    private int systemroleid;
    private String defaultLanguage;

    private Set<UserOrgWorkspace> userorgworkspaces = new HashSet<>(0);

    public UserOrg() {
    }

    public UserOrg(int userorgid, Org org, SystemRole systemrole, User user, Date created) {
        this.userorgid = userorgid;
        this.org = org;
        this.systemrole = systemrole;
        this.user = user;
        this.created = created;
    }

    public UserOrg(int userorgid, Org org, SystemRole systemrole, User user,
                   Date created, String unit, String rank,
                   Set<Usersession> usersessions) {
        this.userorgid = userorgid;
        this.org = org;
        this.systemrole = systemrole;
        this.user = user;
        this.created = created;
        this.unit = unit;
        this.rank = rank;
        this.usersessions = usersessions;
    }

    public int getUserorgid() {
        return this.userorgid;
    }

    public void setUserorgid(int userorgid) {
        this.userorgid = userorgid;
    }

    public Org getOrg() {
        return this.org;
    }

    public void setOrg(Org org) {
        this.org = org;
    }

    public int getOrgId() {
        return this.orgId;
    }

    public void setOrgId(int orgid) {
        this.orgId = orgid;
    }

    public SystemRole getSystemrole() {
        return this.systemrole;
    }

    public void setSystemrole(SystemRole systemrole) {
        this.systemrole = systemrole;
    }

    public int getSystemroleid() {
        return this.systemroleid;
    }

    public void setSystemroleid(int systemroleid) {
        this.systemroleid = systemroleid;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userid) {
        this.userId = userid;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getRank() {
        return this.rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Set<Usersession> getUsersessions() {
        return this.usersessions;
    }

    public void setUsersessions(Set<Usersession> usersessions) {
        this.usersessions = usersessions;
    }

    public String getDefaultLanguage() {
        return this.defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public Set<UserOrgWorkspace> getUserorgworkspaces() {
        return this.userorgworkspaces;
    }

    public void setUserorgworkspaces(Set<UserOrgWorkspace> userorgworkspaces) {
        this.userorgworkspaces = userorgworkspaces;
    }
}
