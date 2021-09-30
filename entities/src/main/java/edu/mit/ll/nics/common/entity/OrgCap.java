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


public class OrgCap {

    private int orgcapid;
    private Cap cap;
    private int orgid;
    private boolean activemobile;
    private boolean activeweb;
    private Date lastupdate;

    public OrgCap() {

    }

    public OrgCap(int orgcapid, Cap cap, int orgid, boolean activemobile, boolean activeweb, Date lastupdate) {
        this.orgcapid = orgcapid;
        this.cap = cap;
        this.orgid = orgid;
        this.activemobile = activemobile;
        this.activeweb = activeweb;
        this.lastupdate = lastupdate;
    }


    public int getOrgCapId() {
        return this.orgcapid;
    }

    public void setOrgCapId(int orgcapid) {
        this.orgcapid = orgcapid;
    }

    public Cap getCap() {
        return this.cap;
    }

    public void setCapId(Cap cap) {
        this.cap = cap;
    }

    public int getOrgId() {
        return this.orgid;
    }

    public void setOrgId(int orgid) {
        this.orgid = orgid;
    }

    public boolean getActiveMobile() {
        return this.activemobile;
    }

    public void setActiveMobile(boolean activemobile) {
        this.activemobile = activemobile;
    }

    public boolean getActiveWeb() {
        return this.activeweb;
    }

    public void setActiveWeb(boolean activeweb) {
        this.activeweb = activeweb;
    }

    public Date getUpdated() {
        return this.lastupdate;
    }

    public void setUpdated(Date lastupdate) {
        this.lastupdate = lastupdate;
    }
}
