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

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.mit.ll.nics.common.entity.datalayer.Document;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.json.JSONObject;


public class Feature {
    private static final long serialVersionUID = 1L;

    private long featureId;
    private String version;
    private Usersession usersession;
    private int usersessionId;

    @JsonProperty("type")
    public String type;
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
    private Double labelSize;
    private String labelText;
    private String username;
    private String nickname;
    private String topic;
    private String time;
    private String ip;
    private long seqtime;
    private long seqnum;
    private Date lastupdate;

    private String geometry;
    private Double pointRadius;
    private Set<UserFeature> userfeatures;
    private Set<CollabroomFeature> collabroomfeatures;
    private String featureattributes;
    private Set<Document> documents = new HashSet<>();

    public Feature() {
        this.setLastupdate(Calendar.getInstance().getTime());
    }

    public Feature(long featureId, Usersession usersession, String type,
                   String user, String nickname, String topic, String time, String ip,
                   long seqtime, long seqnum, Date lastupdate) {

        this.featureId = featureId;
        this.usersession = usersession;
        this.type = type;
        this.username = user;
        this.nickname = nickname;
        this.topic = topic;
        this.time = time;
        this.ip = ip;
        this.seqtime = seqtime;
        this.seqnum = seqnum;
        this.lastupdate = lastupdate;
    }

    public Feature(long featureId, Usersession usersession, String type,
                   String strokeColor, Double strokeWidth, String fillColor,
                   String dashStyle, Double opacity, Double rotation,
                   String graphic, Double graphicHeight, Double graphicWidth,
                   Boolean hasGraphic, Double labelsize, String labelText,
                   String user, String nickname, String topic, String time, String ip,
                   long seqtime, long seqnum, Date lastupdate, String geometry) {

        this.featureId = featureId;
        this.usersession = usersession;
        this.type = type;
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
        this.fillColor = fillColor;
        this.dashStyle = dashStyle;
        this.opacity = opacity;
        this.rotation = rotation;
        this.graphic = graphic;
        this.graphicHeight = graphicHeight;
        this.graphicWidth = graphicWidth;
        this.hasGraphic = hasGraphic;
        this.labelSize = labelsize;
        this.labelText = labelText;
        this.username = user;
        this.nickname = nickname;
        this.topic = topic;
        this.time = time;
        this.ip = ip;
        this.seqtime = seqtime;
        this.seqnum = seqnum;
        this.lastupdate = lastupdate;
        this.geometry = geometry;
    }

    public long getFeatureId() {
        return this.featureId;
    }

    public void setFeatureId(long featureId) {
        this.featureId = featureId;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Usersession getUsersession() {
        return this.usersession;
    }

    public void setUsersession(Usersession usersession) {
        this.usersession = usersession;
    }

    public int getUsersessionId() {
        return this.usersessionId;
    }

    public void setUsersessionId(int usersessionId) {
        this.usersessionId = usersessionId;
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

    public Double getLabelSize() {
        return this.labelSize;
    }

    public void setLabelsize(Double labelSize) {
        this.labelSize = labelSize;
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

    public long getSeqtime() {
        return this.seqtime;
    }

    public void setSeqtime(long seqtime) {
        this.seqtime = seqtime;
    }

    public long getSeqnum() {
        return this.seqnum;
    }

    public void setSeqnum(long seqnum) {
        this.seqnum = seqnum;
    }

    public Date getLastupdate() {
        return this.lastupdate;
    }

    public void setLastupdate(Date lastupdate) {
        this.lastupdate = lastupdate;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public String getGeometry() {
        return this.geometry;
    }

    public Set<UserFeature> getUserfeatures() {
        return this.userfeatures;
    }

    public void setUserfeatures(Set<UserFeature> userfeatures) {
        this.userfeatures = userfeatures;
    }

    public Set<CollabroomFeature> getCollabroomfeatures() {
        return this.collabroomfeatures;
    }

    public void setCollabroomfeatures(Set<CollabroomFeature> collabroomfeatures) {
        this.collabroomfeatures = collabroomfeatures;
    }

    public boolean isGesture() {
        return gesture;
    }

    public void setGesture(boolean gesture) {
        this.gesture = gesture;
    }

    public String buildEventname() {
        StringBuffer events = new StringBuffer();
        Set<CollabroomFeature> cFeatures = this.getCollabroomfeatures();
        if(cFeatures != null) {
            for(Iterator<CollabroomFeature> itr = cFeatures.iterator(); itr.hasNext(); ) {
                if(events.length() > 0) {
                    events.append(",");
                }
                events.append(itr.next().getCollabroom().getIncident().getIncidentname());
            }
        }
        return events.toString();
    }

    public String getAttributes() {
        return featureattributes;
    }

    public void setAttributes(String featureattributes) {
        this.featureattributes = featureattributes;
    }

    public Set<Document> getDocuments() {
        return this.documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
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
            json.put("labelSize", this.labelSize);
            json.put("labelText", this.labelText);
            json.put("pointRadius", this.pointRadius);
            json.put("type", this.type);
            json.put("user", this.username);
            json.put("created", this.time);
            json.put("featureattributes", this.featureattributes);
            json.put("eventname", this.buildEventname());
            json.put("lastupdate", this.lastupdate);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}
