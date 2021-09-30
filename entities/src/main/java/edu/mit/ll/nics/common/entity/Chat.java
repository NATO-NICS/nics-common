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


/**
 * Chat
 */
public class Chat {

    private long chatid;
    private UserOrg userorg;
    private int userorgid;
    private CollabRoom collabroom;
    private int collabroomid;
    private Date created;
    private long seqnum;
    private String message;

    public Chat() {
    }

    public Chat(long chatid, UserOrg userorg, CollabRoom collabroom, Date created, long seqnum, String message) {
        this.chatid = chatid;
        this.userorg = userorg;
        this.collabroom = collabroom;
        this.created = created;
        this.seqnum = seqnum;
        this.message = message;
    }

    public long getChatid() {
        return this.chatid;
    }

    public void setChatid(long chatid) {
        this.chatid = chatid;
    }

    public UserOrg getUserorg() {
        return this.userorg;
    }

    public void setUserorg(UserOrg userorg) {
        this.userorg = userorg;
    }

    public int getUserorgid() {
        return this.userorgid;
    }

    public void setUserorgid(int userorgid) {
        this.userorgid = userorgid;
    }

    public CollabRoom getCollabroom() {
        return this.collabroom;
    }

    public void setCollabroom(CollabRoom collabroom) {
        this.collabroom = collabroom;
    }

    public int getCollabroomid() {
        return this.collabroomid;
    }

    public void setCollabroomid(int collabroomid) {
        this.collabroomid = collabroomid;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
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
        this.message = message; //EntityEncoder.encodeForHTML(message);
    }
}