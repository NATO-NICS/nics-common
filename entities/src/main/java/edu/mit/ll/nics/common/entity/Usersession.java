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
import java.util.Set;


/**
 * Usersession
 */
public class Usersession {

    private int usersessionid;
    private int userorgid;
    private String sessionid;
    private Date loggedin;
    private Date loggedout;

    public Usersession() {
    }

    public Usersession(int usersessionid, UserOrg userorg, String sessionid,
                       Date loggedin) {
        this.usersessionid = usersessionid;
        this.sessionid = sessionid;
        this.loggedin = loggedin;
    }

    public Usersession(int usersessionid, UserOrg userorg, String sessionid,
                       Date loggedin, Date loggedout, Set<CollabRoom> collabrooms, Set<Feature> features,
                       Set<CurrentUserSession> currentusersessions, Set<Chat> chats,
                       Set<Log> logs, Set<Incident> incidents, Set<Form> forms) {

        this.usersessionid = usersessionid;
        this.sessionid = sessionid;
        this.loggedin = loggedin;
        this.loggedout = loggedout;
    }

    public int getUsersessionid() {
        return this.usersessionid;
    }

    public void setUsersessionid(int usersessionid) {
        this.usersessionid = usersessionid;
    }

    public int getUserorgid() {
        return this.userorgid;
    }

    public void setUserorgid(int userorgid) {
        this.userorgid = userorgid;
    }

    public String getSessionid() {
        return this.sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public Date getLoggedin() {
        return this.loggedin;
    }

    public void setLoggedin(Date loggedin) {
        this.loggedin = loggedin;
    }

    public Date getLoggedout() {
        return this.loggedout;
    }

    public void setLoggedout(Date loggedout) {
        this.loggedout = loggedout;
    }
}
