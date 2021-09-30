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
 * Form
 */
public class Form {

    private int formid;
    private Incident incident;
    private int incidentid;
    private int collabroomid;
    private String incidentname;
    private FormType formtype;
    private int formtypeid;
    private Usersession usersession;
    private int usersessionid;
    private long seqtime;
    private long seqnum;
    private String message;
    private Boolean distributed;
    private int senderuserid;

    public Form() {
    }

    public Form(int formid, Incident incident, FormType formtype,
                Usersession usersession, long seqtime, long seqnum, String message) {

        this.formid = formid;
        this.incident = incident;
        this.formtype = formtype;
        this.usersession = usersession;
        this.seqtime = seqtime;
        this.seqnum = seqnum;
        this.message = message;
    }

    public Form(int formid, Incident incident, FormType formtype,
                Usersession usersession, long seqtime, int seqnum, String message,
                Boolean distributed, int senderuserid) {

        this.formid = formid;
        this.incident = incident;
        this.formtype = formtype;
        this.usersession = usersession;
        this.seqtime = seqtime;
        this.seqnum = seqnum;
        this.message = message;
        this.distributed = distributed;
        this.senderuserid = senderuserid;
    }

    public int getFormId() {
        return this.formid;
    }

    public void setFormId(int formid) {
        this.formid = formid;
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

    public int getCollabroomid() {
        return this.collabroomid;
    }

    public void setCollabroomid(int collabroomid) {
        this.collabroomid = collabroomid;
    }

    public String getIncidentname() {
        return this.incidentname;
    }

    public void setIncidentname(String incidentname) {
        this.incidentname = incidentname;
    }

    public FormType getFormtype() {
        return this.formtype;
    }

    public void setFormtype(FormType formtype) {
        this.formtype = formtype;
    }

    public int getFormtypeid() {
        return this.formtypeid;
    }

    public void setFormtypeid(int formtypeid) {
        this.formtypeid = formtypeid;
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

    public long getSeqtime() {
        return this.seqtime;
    }

    public void setSeqtime(long seqtime) {
        this.seqtime = seqtime;
    }

    public long getSeqnum() {
        return this.seqnum;
    }

    public void setSeqnum(long seqnum) {
        this.seqnum = seqnum;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getDistributed() {
        return this.distributed;
    }

    public void setDistributed(Boolean distributed) {
        this.distributed = distributed;
    }
}
