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
 * UserInfo
 */
public class UserInfo {

    private int userId;
    private User user;
    private String rank;
    private String primaryMobilePhone;
    private String primaryHomePhone;
    private String primaryEmailAddr;
    private String homeBaseName;
    private String homeBaseStreet;
    private String homeBaseCity;
    private String homeBaseState;
    private String homeBaseZip;
    private String agency;
    private Integer approxWeight;
    private String remarks;
    private long creationDateTime;


    public UserInfo() {
        this.approxWeight = 0;
    }

    public UserInfo(User user, long creationDateTime) {
        this();

        this.user = user;
        this.creationDateTime = creationDateTime;
    }

    public UserInfo(User user, String rank, String primaryMobilePhone,
                    String primaryHomePhone, String primaryEmailAddr,
                    String homeBaseName, String homeBaseStreet, String homeBaseCity,
                    String homeBaseState, String homeBaseZip, String agency,
                    Integer approxWeight, String remarks, long creationDateTime) {

        this.user = user;
        this.rank = rank;
        this.primaryMobilePhone = primaryMobilePhone;
        this.primaryHomePhone = primaryHomePhone;
        this.primaryEmailAddr = primaryEmailAddr;
        this.homeBaseName = homeBaseName;
        this.homeBaseStreet = homeBaseStreet;
        this.homeBaseCity = homeBaseCity;
        this.homeBaseState = homeBaseState;
        this.homeBaseZip = homeBaseZip;
        this.agency = agency;
        this.approxWeight = approxWeight;
        this.remarks = remarks;
        this.creationDateTime = creationDateTime;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRank() {
        return this.rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getPrimaryMobilePhone() {
        return this.primaryMobilePhone;
    }

    public void setPrimaryMobilePhone(String primaryMobilePhone) {
        this.primaryMobilePhone = primaryMobilePhone;
    }

    public String getPrimaryHomePhone() {
        return this.primaryHomePhone;
    }

    public void setPrimaryHomePhone(String primaryHomePhone) {
        this.primaryHomePhone = primaryHomePhone;
    }

    public String getPrimaryEmailAddr() {
        return this.primaryEmailAddr;
    }

    public void setPrimaryEmailAddr(String primaryEmailAddr) {
        this.primaryEmailAddr = primaryEmailAddr;
    }

    public String getHomeBaseName() {
        return this.homeBaseName;
    }

    public void setHomeBaseName(String homeBaseName) {
        this.homeBaseName = homeBaseName;
    }

    public String getHomeBaseStreet() {
        return this.homeBaseStreet;
    }

    public void setHomeBaseStreet(String homeBaseStreet) {
        this.homeBaseStreet = homeBaseStreet;
    }

    public String getHomeBaseCity() {
        return this.homeBaseCity;
    }

    public void setHomeBaseCity(String homeBaseCity) {
        this.homeBaseCity = homeBaseCity;
    }

    public String getHomeBaseState() {
        return this.homeBaseState;
    }

    public void setHomeBaseState(String homeBaseState) {
        this.homeBaseState = homeBaseState;
    }

    public String getHomeBaseZip() {
        return this.homeBaseZip;
    }

    public void setHomeBaseZip(String homeBaseZip) {
        this.homeBaseZip = homeBaseZip;
    }

    public String getAgency() {
        return this.agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public Integer getApproxWeight() {
        return this.approxWeight;
    }

    public void setApproxWeight(Integer approxWeight) {
        this.approxWeight = approxWeight;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public long getCreationDateTime() {
        return this.creationDateTime;
    }

    public void setCreationDateTime(long creationDateTime) {
        this.creationDateTime = creationDateTime;
    }
}
