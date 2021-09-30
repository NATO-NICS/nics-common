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

import edu.mit.ll.nics.common.entity.Usersession;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * Document
 */
public class Document {

    private String documentid;
    private Usersession usersession;
    private int usersessionid;
    private Folder folder;
    private String folderid;
    private Datasource datasource;
    private String datasourceid;
    private String displayname;
    private String filetype;
    private String filename;
    private boolean globalview;
    private Date created;
    private String description;
    private Set<DocumentOrg> documentOrgs = new HashSet<>(0);
    private Set<DocumentIncident> documentIncidents = new HashSet<>(
            0);
    private Set<DocumentUser> documentUsers = new HashSet<>(0);
    private Set<DocumentCollabroom> documentCollabrooms = new HashSet<>(0);

    public Document() {
    }

    public Document(String documentid, Usersession usersession, Folder folder,
                    Datasource datasource, String displayname, String filetype,
                    boolean globalview, Date created) {

        this.documentid = documentid;
        this.usersession = usersession;
        this.folder = folder;
        this.datasource = datasource;
        this.displayname = displayname;
        this.filetype = filetype;
        this.globalview = globalview;
        this.created = created;
    }

    public Document(String documentid, Usersession usersession, Folder folder,
                    Datasource datasource, String displayname, String filetype,
                    boolean globalview, Date created, Set<DocumentOrg> documentOrgs,
                    Set<DocumentIncident> documentIncidents,
                    Set<DocumentUser> documentUsers,
                    Set<DocumentCollabroom> documentCollabrooms) {

        this.documentid = documentid;
        this.usersession = usersession;
        this.folder = folder;
        this.datasource = datasource;
        this.displayname = displayname;
        this.filetype = filetype;
        this.globalview = globalview;
        this.created = created;
        this.documentOrgs = documentOrgs;
        this.documentIncidents = documentIncidents;
        this.documentUsers = documentUsers;
        this.documentCollabrooms = documentCollabrooms;
    }

    public String getDocumentid() {
        return this.documentid;
    }

    public void setDocumentid(String documentid) {
        this.documentid = documentid;
    }

    public Usersession getUsersession() {
        return this.usersession;
    }

    public void setUsersession(Usersession usersession) {
        this.usersession = usersession;
    }

    public Folder getFolder() {
        return this.folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public Datasource getDatasource() {
        return this.datasource;
    }

    public void setDatasource(Datasource datasource) {
        this.datasource = datasource;
    }

    public String getDisplayname() {
        return this.displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getFiletype() {
        return this.filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public boolean getGlobalview() {
        return this.globalview;
    }

    public void setGlobalview(boolean globalview) {
        this.globalview = globalview;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Set<DocumentOrg> getDocumentOrgs() {
        return this.documentOrgs;
    }

    public void setDocumentOrgs(Set<DocumentOrg> documentOrgs) {
        this.documentOrgs = documentOrgs;
    }

    public Set<DocumentIncident> getDocumentIncidents() {
        return this.documentIncidents;
    }

    public void setDocumentIncidents(Set<DocumentIncident> documentIncidents) {
        this.documentIncidents = documentIncidents;
    }

    public Set<DocumentUser> getDocumentUsers() {
        return this.documentUsers;
    }

    public void setDocumentUsers(Set<DocumentUser> documentUsers) {
        this.documentUsers = documentUsers;
    }

    public Set<DocumentCollabroom> getDocumentCollabrooms() {
        return this.documentCollabrooms;
    }

    public void setDocumentCollabrooms(Set<DocumentCollabroom> documentCollabrooms) {
        this.documentCollabrooms = documentCollabrooms;
    }

    public String getDatasourceid() {
        return datasourceid;
    }

    public void setDatasourceid(String datasourceid) {
        this.datasourceid = datasourceid;
    }

    public String getFolderid() {
        return folderid;
    }

    public void setFolderid(String folderid) {
        this.folderid = folderid;
    }

    public int getUsersessionid() {
        return usersessionid;
    }

    public void setUsersessionid(int usersessionid) {
        this.usersessionid = usersessionid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}