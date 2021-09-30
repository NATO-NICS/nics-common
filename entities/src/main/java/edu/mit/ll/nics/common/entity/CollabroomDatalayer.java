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

import edu.mit.ll.nics.common.entity.datalayer.Datalayer;

/**
 * CollabroomDatalayer
 */
public class CollabroomDatalayer {

    private int collabroomid;
    private String datalayerid;
    private int collabroomDatalayerId;
    private boolean enablemobile;
    private double collabroomOpacity;
    private String hazard;

    private CollabRoom collabroom;
    private Datalayer datalayer;


    public CollabroomDatalayer() {
    }

    public CollabroomDatalayer(int collabRoomId, String datalayerId, int collabRoomDatalayerId) {
        this.collabroomid = collabRoomId;
        this.datalayerid = datalayerId;
        this.collabroomDatalayerId = collabRoomDatalayerId;
    }

    public int getCollabroomDatalayerId() {
        return this.collabroomDatalayerId;
    }

    public void setCollabroomDatalayerId(int collabRoomDatalayerId) {
        this.collabroomDatalayerId = collabRoomDatalayerId;
    }

    public int getCollabroomid() {
        return collabroomid;
    }

    public void setCollabroomid(int collabroomid) {
        this.collabroomid = collabroomid;
    }

    public String getDatalayerid() {
        return datalayerid;
    }

    public void setDatalayerid(String datalayerid) {
        this.datalayerid = datalayerid;
    }

    public double getCollabroomOpacity() {
        return this.collabroomOpacity;
    }

    public void setCollabroomOpacity(double collabroomOpacity) {
        this.collabroomOpacity = collabroomOpacity;
    }

    public boolean getEnablemobile() {
        return this.enablemobile;
    }

    public void setEnablemobile(boolean enable) {
        this.enablemobile = enable;
    }

    public CollabRoom getCollabroom() {
        return collabroom;
    }

    public void setCollabroom(CollabRoom collabroom) {
        this.collabroom = collabroom;
    }

    public Datalayer getDatalayer() {
        return datalayer;
    }

    public void setDatalayer(Datalayer datalayer) {
        this.datalayer = datalayer;
    }

    public String getHazard() { return this.hazard; }

    public void setHazard(String hazard) { this.hazard = hazard; }
}