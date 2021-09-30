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
 * OrgOrgtype
 */
public class OrgOrgType {

    private int orgOrgtypeid;
    private Org Org;
    private OrgType Orgtype;
    private int orgid;
    private int orgtypeid;

    public OrgOrgType() {
    }

    public OrgOrgType(int orgOrgtypeid, Org Org) {
        this.orgOrgtypeid = orgOrgtypeid;
        this.Org = Org;
    }

    public OrgOrgType(int orgOrgtypeid, Org Org,
                      OrgType Orgtype) {
        this.orgOrgtypeid = orgOrgtypeid;
        this.Org = Org;
        this.Orgtype = Orgtype;
    }

    public int getOrgOrgtypeid() {
        return this.orgOrgtypeid;
    }

    public void setOrgOrgtypeid(int orgOrgtypeid) {
        this.orgOrgtypeid = orgOrgtypeid;
    }

    public Org getOrg() {
        return this.Org;
    }

    public void setOrg(Org Org) {
        this.Org = Org;
    }

    public int getOrgid() {
        return this.orgid;
    }

    public void setOrgid(int orgid) {
        this.orgid = orgid;
    }

    public OrgType getOrgtype() {
        return this.Orgtype;
    }

    public void setOrgtype(OrgType Orgtype) {
        this.Orgtype = Orgtype;
    }

    public int getOrgtypeid() {
        return this.orgtypeid;
    }

    public void setOrgtypeid(int orgtypeid) {
        this.orgtypeid = orgtypeid;
    }
}
