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


/**
 * Datalayersource
 */
public class Datalayersource {

    private String datalayersourceid;
    private Datasource datasource;
    private String datasourceid;
    private int refreshrate;
    private String stylepath;
    private String styleicon;
    private String imageformat;
    private String nativeprojection;
    private String layername;
    private String tilegridset;
    private String attributes;
    private Integer tilesize;
    private Double opacity;
    private Date created;
    private Usersession usersession;
    private int usersessionid;

    public Datalayersource() {
    }

    public Datalayersource(String datalayersourceid,
                           Datasource datasource, int refreshrate, Date created) {

        this.datalayersourceid = datalayersourceid;
        this.datasource = datasource;
        this.refreshrate = refreshrate;
        this.created = created;
    }

    public Datalayersource(String datalayersourceid,
                           Datasource datasource, int refreshrate, String stylepath,
                           String imageformat, String nativeprojection, String layername,
                           String tilegridset, Integer tilesize, Date created, Double opacity) {

        this.datalayersourceid = datalayersourceid;
        this.datasource = datasource;
        this.refreshrate = refreshrate;
        this.stylepath = stylepath;
        this.imageformat = imageformat;
        this.nativeprojection = nativeprojection;
        this.layername = layername;
        this.tilegridset = tilegridset;
        this.tilesize = tilesize;
        this.created = created;
        this.opacity = opacity;
    }

    public String getDatalayersourceid() {
        return this.datalayersourceid;
    }

    public void setDatalayersourceid(String datalayersourceid) {
        this.datalayersourceid = datalayersourceid;
    }

    public Datasource getDatasource() {
        return this.datasource;
    }

    public void setDatasource(Datasource datasource) {
        this.datasource = datasource;
    }

    public int getRefreshrate() {
        return this.refreshrate;
    }

    public void setRefreshrate(int refreshrate) {
        this.refreshrate = refreshrate;
    }

    public String getStylepath() {
        return this.stylepath;
    }

    public void setStylepath(String stylepath) {
        this.stylepath = stylepath;
    }

    public String getStyleicon() {
        return this.styleicon;
    }

    public void setStyleicon(String styleicon) {
        this.styleicon = styleicon;
    }

    public String getImageformat() {
        return this.imageformat;
    }

    public void setImageformat(String imageformat) {
        this.imageformat = imageformat;
    }

    public String getNativeprojection() {
        return this.nativeprojection;
    }

    public void setNativeprojection(String nativeprojection) {
        this.nativeprojection = nativeprojection;
    }

    public String getLayername() {
        return this.layername;
    }

    public void setLayername(String layername) {
        this.layername = layername;
    }

    public String getTilegridset() {
        return this.tilegridset;
    }

    public void setTilegridset(String tilegridset) {
        this.tilegridset = tilegridset;
    }

    public Integer getTilesize() {
        return this.tilesize;
    }

    public void setTilesize(Integer tilesize) {
        this.tilesize = tilesize;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getDatasourceid() {
        return datasourceid;
    }

    public void setDatasourceid(String datasourceid) {
        this.datasourceid = datasourceid;
    }

    public Double getOpacity() {
        return this.opacity;
    }

    public void setOpacity(Double opacity) {
        this.opacity = opacity;
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

    public String getAttributes() {
        return this.attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }
}