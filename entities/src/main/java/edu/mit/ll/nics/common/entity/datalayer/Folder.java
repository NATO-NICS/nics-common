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

import edu.mit.ll.nics.common.entity.OrgFolder;
import java.util.HashSet;
import java.util.Set;


/**
 * Folder
 */
public class Folder {

    private String folderid;
    private String foldername;
    private String parentfolderid;
    private Folder parentfolder;
    private int workspaceid;
    private int index;
    private Set<Document> documents = new HashSet<>(0);
    private Set<Folder> children = new HashSet<>(0);
    private Set<OrgFolder> orgfolders = new HashSet<>(0);
    private Set<Datalayerfolder> datalayerfolders = new HashSet<>(0);

    public Folder() {
    }

    public Folder(String folderid, String foldername, String parentfolderid) {
        this.folderid = folderid;
        this.foldername = foldername;
        this.parentfolderid = parentfolderid;
    }

    public Folder(String folderid, String foldername, String parentfolderid, Set<Document> documents, int index) {
        this.folderid = folderid;
        this.foldername = foldername;
        this.parentfolderid = parentfolderid;
        this.documents = documents;
        this.index = index;
    }

    public String getFolderid() {
        return this.folderid;
    }

    public void setFolderid(String folderid) {
        this.folderid = folderid;
    }

    public String getFoldername() {
        return this.foldername;
    }

    public void setFoldername(String foldername) {
        this.foldername = foldername;
    }

    public Folder getParentfolder() {
        return this.parentfolder;
    }

    public void setParentfolder(Folder parentfolder) {
        this.parentfolder = parentfolder;
    }

    public String getParentfolderid() {
        return this.parentfolderid;
    }

    public void setParentfolderid(String parentfolderid) {
        this.parentfolderid = parentfolderid;
    }

    public int getWorkspaceid() {
        return this.workspaceid;
    }

    public void setWorkspaceid(int id) {
        this.workspaceid = id;
    }

    public Set<Folder> getChildren() {
        return this.children;
    }

    public void setChildren(Set<Folder> children) {
        this.children = children;
    }

    public Set<Document> getDocuments() {
        return this.documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Set<OrgFolder> getOrgfolders() {
        return this.orgfolders;
    }

    public void setOrgfolders(Set<OrgFolder> orgfolders) {
        this.orgfolders = orgfolders;
    }

    public Set<Datalayerfolder> getDatalayerfolders() {
        return this.datalayerfolders;
    }

    public void setDatalayerfolders(Set<Datalayerfolder> datalayerfolders) {
        this.datalayerfolders = datalayerfolders;
    }
}