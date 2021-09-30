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
 * IncidentIncidenttype
 */
public class IncidentIncidentType {

    private int incidentIncidenttypeid;
    private Incident incident;
    private IncidentType incidentType;
    private int incidenttypeid;
    private int incidentid;

    public IncidentIncidentType() {
    }

    public IncidentIncidentType(int incidentIncidenttypeid, Incident incident) {
        this.incidentIncidenttypeid = incidentIncidenttypeid;
        this.incident = incident;
    }

    public IncidentIncidentType(int incidentIncidenttypeid, Incident incident,
                                IncidentType incidenttype) {
        this.incidentIncidenttypeid = incidentIncidenttypeid;
        this.incident = incident;
        this.incidentType = incidenttype;
    }

    public int getIncidentIncidenttypeid() {
        return this.incidentIncidenttypeid;
    }

    public void setIncidentIncidenttypeid(int incidentIncidenttypeid) {
        this.incidentIncidenttypeid = incidentIncidenttypeid;
    }

    public Incident getIncident() {
        return this.incident;
    }

    public void setIncident(Incident incident) {
        this.incident = incident;
    }

    public int getIncidentid() {
        return this.incidentid;
    }

    public void setIncidentid(int incidentid) {
        this.incidentid = incidentid;
    }

    public IncidentType getIncidentType() {
        return this.incidentType;
    }

    public void setIncidentType(IncidentType incidenttype) {
        this.incidentType = incidenttype;
    }

    public int getIncidenttypeid() {
        return this.incidenttypeid;
    }

    public void setIncidenttypeid(int incidenttypeid) {
        this.incidenttypeid = incidenttypeid;
    }
}
