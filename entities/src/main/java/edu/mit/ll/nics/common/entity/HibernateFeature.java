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

import java.util.Calendar;
import java.util.Date;
import javax.persistence.Transient;
import org.json.JSONObject;
import org.locationtech.jts.geom.Geometry;

/**
 * HibernateFeature
 */
public class HibernateFeature {

    private String featureid;
    private String version;
    private String type;
    private String strokeColor;
    private Double strokeWidth;
    private String fillColor;
    private String dashStyle;
    private Double opacity;
    private Double rotation;
    private boolean gesture = false;
    private String graphic;
    private Double graphicHeight;
    private Double graphicWidth;
    private Boolean hasGraphic;
    private Double labelsize;
    private String labelText;
    private String username;
    private String nickname;
    private String topic;
    private String time;
    private String ip;
    private Integer usersessionid;
    private Date lastupdate;
    private Geometry geometry;
    private Double pointRadius;
    private String featureattributes;

    public HibernateFeature() {
        this.setLastupdate(Calendar.getInstance().getTime());
    }

    public String getFeatureid() {
        return this.featureid;
    }

    public void setFeatureid(String featureid) {
        this.featureid = featureid;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStrokeColor() {
        return this.strokeColor;
    }

    public void setStrokeColor(String strokeColor) {
        this.strokeColor = strokeColor;
    }

    public Double getStrokeWidth() {
        return this.strokeWidth;
    }

    public void setStrokeWidth(Double strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public String getFillColor() {
        return this.fillColor;
    }

    public void setFillColor(String fillColor) {
        this.fillColor = fillColor;
    }

    public String getDashStyle() {
        return this.dashStyle;
    }

    public void setDashStyle(String dashStyle) {
        this.dashStyle = dashStyle;
    }

    public Double getPointRadius() {
        return this.pointRadius;
    }

    public void setPointRadius(Double pointRadius) {
        this.pointRadius = pointRadius;
    }

    public Double getOpacity() {
        return this.opacity;
    }

    public void setOpacity(Double opacity) {
        this.opacity = opacity;
    }

    public Double getRotation() {
        return this.rotation;
    }

    public void setRotation(Double rotation) {
        this.rotation = rotation;
    }

    public String getGraphic() {
        return this.graphic;
    }

    public void setGraphic(String graphic) {
        this.graphic = graphic;
    }

    public Double getGraphicHeight() {
        return this.graphicHeight;
    }

    public void setGraphicHeight(Double graphicHeight) {
        this.graphicHeight = graphicHeight;
    }

    public Double getGraphicWidth() {
        return this.graphicWidth;
    }

    public void setGraphicWidth(Double graphicWidth) {
        this.graphicWidth = graphicWidth;
    }

    public Boolean getHasGraphic() {
        return this.hasGraphic;
    }

    public void setHasGraphic(Boolean hasGraphic) {
        this.hasGraphic = hasGraphic;
    }

    public Double getLabelsize() {
        return this.labelsize;
    }

    public void setLabelsize(Double labelsize) {
        this.labelsize = labelsize;
    }

    public String getLabelText() {
        return this.labelText;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String user) {
        this.username = user;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTopic() {
        return this.topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getLastupdate() {
        return this.lastupdate;
    }

    public void setLastupdate(Date lastupdate) {
        this.lastupdate = lastupdate;
    }

    public Geometry getTheGeom() {
        return this.geometry;
    }

    public void setTheGeom(Geometry theGeom) {
        this.geometry = theGeom;
        if(this.geometry != null) {
            this.geometry.setSRID(3857);
        }
    }

    public Integer getUsersessionid() {
        return this.usersessionid;
    }

    public void setUsersessionid(Integer id) {
        this.usersessionid = id;
    }

    @Transient
    public boolean isGesture() {
        return gesture;
    }

    public void setGesture(boolean gesture) {
        this.gesture = gesture;
    }

    public String getFeatureattributes() {
        return featureattributes;
    }

    public void setFeatureattributes(String featureattributes) {
        this.featureattributes = EntityEncoder.encodeJSONObject(featureattributes);
    }

    public JSONObject buildAttributes() {
        JSONObject json = new JSONObject();
        try {
            json.put("strokeColor", this.strokeColor);
            json.put("strokeWidth", this.strokeWidth);
            json.put("fillColor", this.fillColor);
            json.put("dashStyle", this.dashStyle);
            json.put("opacity", this.opacity);
            json.put("rotation", this.rotation);
            json.put("gesture", this.gesture);
            json.put("graphic", this.graphic);
            json.put("graphicHeight", this.graphicHeight);
            json.put("graphicWidth", this.graphicWidth);
            json.put("hasGraphic", this.hasGraphic);
            json.put("labelsize", this.labelsize);
            json.put("labelText", this.labelText);
            json.put("pointRadius", this.pointRadius);
            json.put("type", this.type);
            json.put("user", this.username);
            json.put("created", this.time);
            json.put("featureattributes", this.featureattributes);
            json.put("lastupdate", this.lastupdate);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}