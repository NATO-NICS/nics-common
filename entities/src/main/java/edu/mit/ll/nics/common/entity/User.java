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

import edu.mit.ll.nics.common.entity.datalayer.DocumentUser;
import java.util.HashSet;
import java.util.Set;


public class User {

    private int userId;
    private String username;
    private String firstname;
    private String lastname;
    private boolean active;
    private Set<CurrentUserSession> currentusersessions = new HashSet<>(0);
    private Set<DocumentUser> documentUsers = new HashSet<>(0);
    private Set<Contact> contacts = new HashSet<>(0);
    private Set<UserOrg> userorgs = new HashSet<>(0);
    private Set<CollabroomPermission> collabroompermissions = new HashSet<>(0);


    public User() {
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userid) {
        this.userId = userid;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<CurrentUserSession> getCurrentusersessions() {
        return this.currentusersessions;
    }

    public void setCurrentusersessions(
            Set<CurrentUserSession> currentusersessions) {
        this.currentusersessions = currentusersessions;
    }

    public Set<DocumentUser> getDocumentUsers() {
        return this.documentUsers;
    }

    public void setDocumentUsers(Set<DocumentUser> documentUsers) {
        this.documentUsers = documentUsers;
    }

    public Set<Contact> getContacts() {
        return this.contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }

    public Set<UserOrg> getUserorgs() {
        return this.userorgs;
    }

    public void setUserorgs(Set<UserOrg> userorgs) {
        this.userorgs = userorgs;
    }

    public Set<CollabroomPermission> getCollabroompermissions() {
        return this.collabroompermissions;
    }

    public void setCollabroompermissions(Set<CollabroomPermission> collabroompermissions) {
        this.collabroompermissions = collabroompermissions;
    }
}
