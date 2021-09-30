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


/**
 * Log
 */
public class UserOrgWorkspace {

    private int userorgworkspaceid;
    private int userorgid;
    private int workspaceid;
    private UserOrg userorg;
    private boolean defaultorg;
    private boolean enabled;

    public UserOrgWorkspace() {
    }

    public UserOrgWorkspace(int userorgid, int workspaceid) {
        this.userorgid = userorgid;
        this.workspaceid = workspaceid;
    }

    public int getUserorgworkspaceid() {
        return this.userorgworkspaceid;
    }

    public void setUserorgworkspaceid(int userorgworkspaceid) {
        this.userorgworkspaceid = userorgworkspaceid;
    }

    public UserOrg getUserorg() {
        return this.userorg;
    }

    public void setUserorg(UserOrg userorg) {
        this.userorg = userorg;
    }

    public int getUserorgid() {
        return this.userorgid;
    }

    public void setUserorgid(int userorgid) {
        this.userorgid = userorgid;
    }

    public int getWorkspaceid() {
        return this.workspaceid;
    }

    public void setWorkspaceid(int workspaceid) {
        this.workspaceid = workspaceid;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isDefaultorg() {
        return this.defaultorg;
    }

    public void setDefaultorg(boolean defaultorg) {
        this.defaultorg = defaultorg;
    }
}
