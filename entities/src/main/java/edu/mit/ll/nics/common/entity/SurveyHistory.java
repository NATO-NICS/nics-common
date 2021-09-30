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


/**
 * SurveyHistory entity
 */
public class SurveyHistory
{
    private long surveyHistoryId;

    private int surveyid;

    private String title;

    private String survey;

    private Timestamp originaldate;

    private Timestamp archiveddate;


    public SurveyHistory() {
    }

    public SurveyHistory(String title, String survey) {
        this.title = title;
        this.survey = survey;
    }

    public long getSurveyHistoryId() {
        return surveyHistoryId;
    }

    public void setSurveyHistoryId(long surveyHistoryId) {
        this.surveyHistoryId = surveyHistoryId;
    }

    public int getSurveyid() {
        return surveyid;
    }

    public void setSurveyid(int surveyid) {
        this.surveyid = surveyid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSurvey() {
        return survey;
    }

    public void setSurvey(String survey) {
        this.survey = survey;
    }

    public Timestamp getOriginaldate() {
        return originaldate;
    }

    public void setOriginaldate(Timestamp originaldate) {
        this.originaldate = originaldate;
    }

    public Timestamp getArchiveddate() {
        return archiveddate;
    }

    public void setArchiveddate(Timestamp archiveddate) {
        this.archiveddate = archiveddate;
    }

    @Override
    public String toString() {
        return "SurveyHistory{" +
                "surveyhistoryid=" + surveyHistoryId +
                "surveyid=" + surveyid +
                ", title='" + title + '\'' +
                ", survey='" + ((survey != null && survey.length() > 50) ? survey.toString().substring(0,50) + "...(truncated)" : survey ) + '\'' +
                ", originaldate=" + originaldate +
                ", archiveddate=" + archiveddate +
                '}';
    }
}
