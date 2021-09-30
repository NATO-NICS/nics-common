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
import java.util.HashSet;
import java.util.Set;


/**
 * Org
 */
public class Org {

    private int orgId;
    private String name;
    private String county;
    private String state;
    private String timezone;
    private String prefix;
    private String distribution;
    private double defaultlatitude;
    private double defaultlongitude;
    private String defaultlanguage;
    private Integer parentorgid;
    private int countryId;
    private Date created;
    private boolean restrictincidents;
    private boolean createincidentrequiresadmin;
    private Set<UserOrg> userorgs = new HashSet<UserOrg>(0);
    private Set<OrgOrgType> orgTypes = new HashSet<OrgOrgType>(0);

    public Org() {
    }

    public Org(int orgId, String name, String prefix, double defaultlatitude,
               double defaultlongitude, Date created, String defaultLanguage) {
        this.orgId = orgId;
        this.name = name;
        this.prefix = prefix;
        this.defaultlatitude = defaultlatitude;
        this.defaultlongitude = defaultlongitude;
        this.created = created;
        this.defaultlanguage = defaultLanguage;
    }

    public Org(int orgId, String name, String county, String state,
               String timezone, String prefix, String distribution,
               double defaultlatitude, double defaultlongitude,
               Integer parentorgid, int countryId, Date created, Set userorgs) {
        this.orgId = orgId;
        this.name = name;
        this.countryId = countryId;
        this.state = state;
        this.timezone = timezone;
        this.prefix = prefix;
        this.distribution = distribution;
        this.defaultlatitude = defaultlatitude;
        this.defaultlongitude = defaultlongitude;
        this.parentorgid = parentorgid;
        this.county = county;
        this.created = created;
        this.userorgs = userorgs;
    }

    public int getOrgId() {
        return this.orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCounty() {
        return this.county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTimezone() {
        return this.timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getDistribution() {
        return this.distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    public double getDefaultlatitude() {
        return this.defaultlatitude;
    }

    public void setDefaultlatitude(double defaultlatitude) {
        this.defaultlatitude = defaultlatitude;
    }

    public double getDefaultlongitude() {
        return this.defaultlongitude;
    }

    public void setDefaultlongitude(double defaultlongitude) {
        this.defaultlongitude = defaultlongitude;
    }

    public Integer getParentorgid() {
        return this.parentorgid;
    }

    public void setParentorgid(Integer parentorgid) {
        this.parentorgid = parentorgid;
    }

    public int getCountryId() {
        return this.countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getDefaultlanguage() {
        return this.defaultlanguage;
    }

    public void setDefaultlanguage(String defaultLanguage) {
        this.defaultlanguage = defaultLanguage;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Set<UserOrg> getUserorgs() {
        return this.userorgs;
    }

    public void setUserorgs(Set<UserOrg> userorgs) {
        this.userorgs = userorgs;
    }

    public Set<OrgOrgType> getOrgTypes() {
        return this.orgTypes;
    }

    public void setOrgTypes(Set<OrgOrgType> orgTypes) {
        this.orgTypes = orgTypes;
    }

    public boolean getRestrictincidents() {
        return restrictincidents;
    }

    public void setRestrictincidents(boolean restrictincidents) {
        this.restrictincidents = restrictincidents;
    }

    public boolean getCreateincidentrequiresadmin() {
        return createincidentrequiresadmin;
    }

    public void setCreateincidentrequiresadmin(boolean createincidentrequiresadmin) {
        this.createincidentrequiresadmin = createincidentrequiresadmin;
    }
}
