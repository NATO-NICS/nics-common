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


public class Contact {

    private int contactid;
    private User user;
    private String value;
    private boolean enabled;
    private Date created;
    private int userId;
    private int contacttypeid;
    private boolean enableLogin;
    private ContactType contactType;

    private static int EMAIL_CONTACT_TYPE_ID = 0;
    private static int RADIO_CONTACT_TYPE_ID = 4;
    private static String PHONE_REG_EXP = "[0-9]*[0-9]+$";
    private static String RADIO_REG_EXP = "^[a-zA-Z0-9 -]*$";

    public Contact() {
    }

    public Contact(int contactid, ContactType contacttype, User user, boolean enabled, Date created) {
        this.contactid = contactid;
        this.user = user;
        this.enabled = enabled;
        this.created = created;
    }

    public Contact(int userid, String value, int contacttypeid) {
        this.userId = userid;
        this.value = value;
        this.contacttypeid = contacttypeid;
    }

    public int getContactid() {
        return this.contactid;
    }

    public void setContactid(int contactid) {
        this.contactid = contactid;
    }

    public int getContacttypeid() {
        return this.contacttypeid;
    }

    public void setContacttypeid(int contacttypeid) {
        this.contacttypeid = contacttypeid;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userid) {
        this.userId = userid;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnableLogin() {
        return this.enableLogin;
    }

    public void setEnableLogin(boolean enableLogin) {
        this.enableLogin = enableLogin;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    public ContactType getContactType() {
        return this.contactType;
    }

    /**
     * Validate the value of the contact based on the contacttypeid
     *
     * @param value
     * @return the value if it is a known contacttype and it validates, empty String otherwise
     */
    private String validateValue(String value) {
        if(this.contacttypeid == EMAIL_CONTACT_TYPE_ID &&
                EntityEncoder.validateEmailAddress(value)) {
            return value;
        } else if(this.contacttypeid == RADIO_CONTACT_TYPE_ID &&
                value.matches(RADIO_REG_EXP)) {
            return value;
        } else if(value.matches(PHONE_REG_EXP)) {
            return value;
        }
        return "";
    }
}