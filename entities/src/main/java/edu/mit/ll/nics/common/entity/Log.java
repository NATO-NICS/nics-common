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


/**
 * Log
 */
public class Log {

    private int logid;
    private LogType logType;
    private Usersession usersession;
    private int usersessionid;
    private String message;
    private Date created;
    private int logtypeid;
    private int status;
    private int workspaceid;

    public Log() {
    }

    public Log(int logid, LogType logtype, Date created) {
        this.logid = logid;
        this.logType = logtype;
        this.created = created;
    }

    public Log(int logid, LogType logtype, Usersession usersession, String message, Date created, int status) {
        this.logid = logid;
        this.logType = logtype;
        this.usersession = usersession;
        this.message = message;
        this.created = created;
        this.status = status;
    }

    public int getLogid() {
        return this.logid;
    }

    public void setLogid(int logid) {
        this.logid = logid;
    }

    public LogType getLogtype() {
        return this.logType;
    }

    public void setLogtype(LogType logtype) {
        this.logType = logtype;
    }

    public int getLogtypeid() {
        return this.logtypeid;
    }

    public void setLogtypeid(int logtypeid) {
        this.logtypeid = logtypeid;
    }

    public Usersession getUsersession() {
        return this.usersession;
    }

    public void setUsersession(Usersession usersession) {
        this.usersession = usersession;
    }

    public int getUsersessionid() {
        return this.usersessionid;
    }

    public void setUsersessionid(int usersessionid) {
        this.usersessionid = usersessionid;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getWorkspaceid() {
        return this.workspaceid;
    }

    public void setWorkspaceId(int workspaceid) {
        this.workspaceid = workspaceid;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
