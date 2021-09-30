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
 * OrgIncidenttype
 */
public class OrgIncidentType {

    private int orgIncidenttypeid;
    private Org Org;
    private IncidentType Incidenttype;
    private int orgid;
    private int incidenttypeid;
    private boolean defaulttype;

    public OrgIncidentType() {
    }

    public OrgIncidentType(int orgIncidenttypeid, Org Org) {
        this.orgIncidenttypeid = orgIncidenttypeid;
        this.Org = Org;
    }

    public OrgIncidentType(int orgIncidenttypeid, Org Org,
                           IncidentType Incidenttype) {
        this.orgIncidenttypeid = orgIncidenttypeid;
        this.Org = Org;
        this.Incidenttype = Incidenttype;
    }

    public int getOrgIncidenttypeid() {
        return this.orgIncidenttypeid;
    }

    public void setOrgIncidenttypeid(int orgIncidenttypeid) {
        this.orgIncidenttypeid = orgIncidenttypeid;
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

    public IncidentType getIncidenttype() {
        return this.Incidenttype;
    }

    public void setIncidenttype(IncidentType Incidenttype) {
        this.Incidenttype = Incidenttype;
    }

    public int getIncidenttypeid() {
        return this.incidenttypeid;
    }

    public void setIncidenttypeid(int incidenttypeid) {
        this.incidenttypeid = incidenttypeid;
    }

    public void setDefaulttype(boolean defaulttype) {
        this.defaulttype = defaulttype;
    }

    public boolean getDefaulttype() {
        return this.defaulttype;
    }

}
