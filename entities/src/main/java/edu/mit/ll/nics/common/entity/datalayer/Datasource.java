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

import java.util.HashSet;
import java.util.Set;


/**
 * Datasource
 */
public class Datasource {

    private String datasourceid;
    private Datasourcetype datasourcetype;
    private Integer datasourcetypeid;
    private String internalurl;
    private String externalurl;
    private String displayname;
    private String username;
    private String password;
    private boolean secure = false;
    private Set<Document> documents = new HashSet<>(0);
    private Set<Datalayersource> datalayersources = new HashSet<>(
            0);

    public Datasource() {
    }

    public Datasource(String datasourceid, Datasourcetype datasourcetype,
                      String internalurl, String externalurl, Set<Document> documents,
                      Set<Datalayersource> datalayersources, String displayname) {

        this.datasourceid = datasourceid;
        this.datasourcetype = datasourcetype;
        this.internalurl = internalurl;
        this.externalurl = externalurl;
        this.documents = documents;
        this.displayname = displayname;
        this.datalayersources = datalayersources;
    }

    public String getDatasourceid() {
        return this.datasourceid;
    }

    public void setDatasourceid(String datasourceid) {
        this.datasourceid = datasourceid;
    }

    public Datasourcetype getDatasourcetype() {
        return this.datasourcetype;
    }

    public void setDatasourcetype(Datasourcetype datasourcetype) {
        this.datasourcetype = datasourcetype;
    }

    public String getInternalurl() {
        return this.internalurl;
    }

    public void setInternalurl(String internalurl) {
        this.internalurl = internalurl;
    }

    public String getExternalurl() {
        return this.externalurl;
    }

    public void setExternalurl(String externalurl) {
        this.externalurl = externalurl;
    }

    public String getDisplayname() {
        return this.displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public Set<Document> getDocuments() {
        return this.documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    public Set<Datalayersource> getDatalayersources() {
        return this.datalayersources;
    }

    public void setDatalayersources(Set<Datalayersource> datalayersources) {
        this.datalayersources = datalayersources;
    }

    public Integer getDatasourcetypeid() {
        return datasourcetypeid;
    }

    public void setDatasourcetypeid(Integer datasourcetypeid) {
        this.datasourcetypeid = datasourcetypeid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean getSecure() {
        return this.secure;
    }

    public void setSecure(boolean isSecure) {
        this.secure = isSecure;
    }
}