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

import java.util.HashSet;
import java.util.Set;
import org.locationtech.jts.geom.Geometry;


/**
 * Location
 */
public class Location implements java.io.Serializable {
    private static final long serialVersionUID = -3319346490605055680L;

    private long id;
    private UserInfo userInfo;
    private int userid;
    private String deviceId;
    private Geometry location;
    private Double course;
    private Double speed;
    private Double accuracy;
    private long time;
    private Set<Image> images = new HashSet<>(0);

    public Location() {
    }

    public Location(long id, UserInfo userInfo, String deviceId, Geometry location, long time) {
        this.id = id;
        this.userInfo = userInfo;
        this.deviceId = deviceId;
        this.location = location;
        this.time = time;
    }

    public Location(long id, UserInfo userInfo, String deviceId, Geometry location,
                    Double course, Double speed, Double accuracy, long time, Set<Image> images) {

        this.id = id;
        this.userInfo = userInfo;
        this.deviceId = deviceId;
        this.location = location;
        this.course = course;
        this.speed = speed;
        this.accuracy = accuracy;
        this.time = time;
        this.images = images;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserInfo getUserInfo() {
        return this.userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        if(this.userInfo != null) {
            this.userid = this.userInfo.getUserId();
        }
    }

    public int getUserid() {
        return this.userid;
    }

    public void setUserid(int id) {
        this.userid = id;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Geometry getLocation() {
        return this.location;
    }

    public void setLocation(Geometry location) {
        this.location = location;
    }

    public Double getCourse() {
        return this.course;
    }

    public void setCourse(Double course) {
        this.course = course;
    }

    public Double getSpeed() {
        return this.speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getAccuracy() {
        return this.accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Set<Image> getImages() {
        return this.images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }
}
