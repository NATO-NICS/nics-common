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

import java.sql.Timestamp;
import org.locationtech.jts.geom.Geometry;


/**
 * MobileDeviceTrack entity
 */
public class MobileDeviceTrack implements java.io.Serializable {

    private static final long serialVersionUID = -3319346490605055680L;

    private long mobileDeviceTrackId;
    private String deviceId;
    private String username;
    private String name;
    private Geometry location;
    private Double course;
    private Double speed;
    private Double altitude;
    private Double accuracy;
    private Timestamp timestamp;
    private String description;
    private String extendeddata;
    private Integer workspaceId;
    private Double longitude;
    private Double latitude;

    /**
     * Default constructor
     */
    public MobileDeviceTrack() {
    }

    /**
     * Required Constructor
     *
     * @param deviceId
     * @param username
     * @param latitude
     * @param longitude
     * @param timestamp
     */
    public MobileDeviceTrack(String deviceId, String username, Double latitude, Double longitude,
                             Timestamp timestamp) {

        this.deviceId = deviceId;
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    /**
     * Full constructor
     *
     * @param deviceId
     * @param username
     * @param name
     * @param course
     * @param speed
     * @param altitude
     * @param accuracy
     * @param timestamp
     * @param description
     * @param extendeddata
     * @param workspaceId
     * @param longitude
     * @param latitude
     */
    public MobileDeviceTrack(String deviceId, String username, String name, Double course, Double speed,
                             Double altitude, Double accuracy, Timestamp timestamp, String description,
                             String extendeddata, Integer workspaceId, Double longitude, Double latitude) {

        this.deviceId = deviceId;
        this.username = username;
        this.name = name;
        this.course = course;
        this.speed = speed;
        this.altitude = altitude;
        this.accuracy = accuracy;
        this.timestamp = timestamp;
        this.description = description;
        this.extendeddata = extendeddata;
        this.workspaceId = workspaceId;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public long getMobileDeviceTrackId() {
        return this.mobileDeviceTrackId;
    }

    public void setMobileDeviceTrackId(long mobileDeviceTrackId) {
        this.mobileDeviceTrackId = mobileDeviceTrackId;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Double getAccuracy() {
        return this.accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public Timestamp getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExtendeddata() {
        return extendeddata;
    }

    public void setExtendeddata(String extendeddata) {
        this.extendeddata = extendeddata;
    }

    public Integer getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Integer workspaceId) {
        this.workspaceId = workspaceId;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}