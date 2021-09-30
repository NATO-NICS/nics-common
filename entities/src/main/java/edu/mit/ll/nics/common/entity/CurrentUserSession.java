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

import java.io.Serializable;
import java.util.Date;


/**
 * CurrentUserSession
 */
public class CurrentUserSession implements Serializable {

    private int currentusersessionid;
    private Usersession usersession;
    private User user;
    private SystemRole systemrole;
    private String displayname;
    private Date loggedin;
    private Date lastseen;
    private int systemroleid;
    private int userid;
    private int usersessionid;
    private int workspaceid;
    private boolean mobile;

    public CurrentUserSession() {
    }

    public CurrentUserSession(int currentusersessionid, Usersession usersession, User user, String displayname,
                              Date loggedin, Date lastseen) {

        this.currentusersessionid = currentusersessionid;
        this.usersession = usersession;
        this.user = user;
        this.displayname = displayname;
        this.loggedin = loggedin;
        this.lastseen = lastseen;
    }

    public int getCurrentusersessionid() {
        return this.currentusersessionid;
    }

    public void setCurrentusersessionid(int currentusersessionid) {
        this.currentusersessionid = currentusersessionid;
    }

    public Usersession getUsersession() {
        return this.usersession;
    }

    public void setUsersession(Usersession usersession) {
        this.usersession = usersession;
    }

    public void setUsersessionid(int usersessionid) {
        this.usersessionid = usersessionid;
    }

    public int getUsersessionid() {
        return this.usersessionid;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getUserid() {
        return this.userid;
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

    public int getWorkspaceid() {
        return this.workspaceid;
    }

    public void setWorkspaceid(int workspaceid) {
        this.workspaceid = workspaceid;
    }

    public String getDisplayname() {
        return this.displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public Date getLoggedin() {
        return this.loggedin;
    }

    public void setLoggedin(Date loggedin) {
        this.loggedin = loggedin;
    }

    public Date getLastseen() {
        return this.lastseen;
    }

    public void setLastseen(Date lastseen) {
        this.lastseen = lastseen;
    }

    public boolean getMobile() { return this.mobile; }

    public void setMobile(boolean mobile){ this.mobile = mobile; }
}