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
package edu.mit.ll.nics.common.entity.datalayer;

import edu.mit.ll.nics.common.entity.CollabroomDatalayer;
import edu.mit.ll.nics.common.entity.Usersession;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public class Datalayer {

    private String datalayerid;
    private Usersession usersession;
    private int usersessionid;
    private Datalayersource datalayersource;
    private String datalayersourceid;
    private boolean baselayer;
    private String displayname;
    private Date created;
    private String legend;
    private Set<CollabroomDatalayer> collabroomDatalayers = new HashSet<>(0);
    private Set<Datalayerfolder> datalayerfolders = new HashSet<>(0);
    private Set<DatalayerOrg> datalayerOrgs = new HashSet<>(0);


    public Datalayer() {
    }

    public Datalayer(String datalayerid, Usersession usersession,
                     Datalayersource datalayersource, Folder folder, boolean baselayer,
                     String displayname, Date created, String legend) {

        this.datalayerid = datalayerid;
        this.usersession = usersession;
        this.datalayersource = datalayersource;
        this.baselayer = baselayer;
        this.displayname = displayname;
        this.created = created;
        this.legend = legend;
    }

    public Datalayer(String datalayerid, Usersession usersession,
                     Datalayersource datalayersource, Folder folder, boolean baselayer,
                     String displayname, Date created,
                     Set<CollabroomDatalayer> collabroomDatalayers, String legend) {

        this.datalayerid = datalayerid;
        this.usersession = usersession;
        this.datalayersource = datalayersource;
        this.baselayer = baselayer;
        this.displayname = displayname;
        this.created = created;
        this.collabroomDatalayers = collabroomDatalayers;
        this.legend = legend;
    }

    public String getDatalayerid() {
        return this.datalayerid;
    }

    public void setDatalayerid(String datalayerid) {
        this.datalayerid = datalayerid;
    }

    public Usersession getUsersession() {
        return this.usersession;
    }

    public void setUsersession(Usersession usersession) {
        this.usersession = usersession;
    }

    public Datalayersource getDatalayersource() {
        return this.datalayersource;
    }

    public void setDatalayersource(Datalayersource datalayersource) {
        this.datalayersource = datalayersource;
    }

    public boolean getBaselayer() {
        return this.baselayer;
    }

    public void setBaselayer(boolean baselayer) {
        this.baselayer = baselayer;
    }

    public String getDisplayname() {
        return this.displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Set<CollabroomDatalayer> getCollabroomDatalayers() {
        return this.collabroomDatalayers;
    }

    public void setCollabroomDatalayers(Set<CollabroomDatalayer> collabroomDatalayers) {
        this.collabroomDatalayers = collabroomDatalayers;
    }

    public Set<Datalayerfolder> getDatalayerfolders() {
        return this.datalayerfolders;
    }

    public void setDatalayerfolders(Set<Datalayerfolder> datalayerfolders) {
        this.datalayerfolders = datalayerfolders;
    }

    public Set<DatalayerOrg> getDatalayerOrgs() {
        return this.datalayerOrgs;
    }

    public void setDatalayerOrgs(Set<DatalayerOrg> datalayerOrgs) {
        this.datalayerOrgs = datalayerOrgs;
    }

    public String getDatalayersourceid() {
        return datalayersourceid;
    }

    public void setDatalayersourceid(String datalayersourceid) {
        this.datalayersourceid = datalayersourceid;
    }

    public int getUsersessionid() {
        return usersessionid;
    }

    public void setUsersessionid(int usersessionid) {
        this.usersessionid = usersessionid;
    }

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }
}