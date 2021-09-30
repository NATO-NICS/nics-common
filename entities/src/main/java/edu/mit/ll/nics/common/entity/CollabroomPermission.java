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
 * Collabroompermission
 */
public class CollabroomPermission {

    private int collabRoomId;
    private int userId;
    private int systemRoleId;
    private int collabroomPermissionId;

    private CollabRoom collabroom;
    private User user;
    private SystemRole systemRole;

    public CollabroomPermission() {
    }

    public CollabroomPermission(int collabRoomId, int userid, int systemRoleId, int collabRoomPermissionId) {
        this.collabRoomId = collabRoomId;
        this.userId = userid;
        this.systemRoleId = systemRoleId;
        this.collabroomPermissionId = collabRoomPermissionId;
    }

    public int getCollabroomPermissionId() {
        return this.collabroomPermissionId;
    }

    public void setCollabroomPermissionId(int collabRoomPermissionId) {
        this.collabroomPermissionId = collabRoomPermissionId;
    }

    public void setCollabRoomId(int collabroomid) {
        this.collabRoomId = collabroomid;
    }

    public int getCollabRoomId() {
        return this.collabRoomId;
    }

    public void setUserId(int userid) {
        this.userId = userid;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setSystemRoleId(int systemRoleId) {
        this.systemRoleId = systemRoleId;
    }

    public int getSystemRoleId() {
        return this.systemRoleId;
    }

    public CollabRoom getCollabroom() {
        return this.collabroom;
    }

    public User getUser() {
        return this.user;
    }

    public SystemRole getSystemRole() {
        return this.systemRole;
    }

    public void setCollabroom(CollabRoom room) {
        this.collabroom = room;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setSystemRole(SystemRole role) {
        this.systemRole = role;
    }
}