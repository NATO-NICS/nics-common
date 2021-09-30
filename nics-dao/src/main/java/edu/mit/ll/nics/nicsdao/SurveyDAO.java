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
package edu.mit.ll.nics.nicsdao;


import edu.mit.ll.nics.common.entity.Survey;
import edu.mit.ll.nics.common.entity.SurveyHistory;
import edu.mit.ll.nics.common.entity.SurveyResult;
import java.util.List;
import org.springframework.dao.DataAccessException;


public interface SurveyDAO {

    /**
     * Creates a survey
     *
     * @param survey the Survey to insert
     *
     * @return the surveyid of the survey created if successful,
     *  -1 for general errors, -2 for title already existing
     */
    int createSurvey(Survey survey);

    /**
     * Retrieve a survey by id
     *
     * @param surveyId the surveyid
     *
     * @return Survey if found, null otherwise
     */
    Survey getById(final int surveyId);

    Survey getByFormTypeName(final String formTypeName);

    /**
     * Get all Surveys
     *
     * @return a list of Surveys if found, an empty list otherwise.
     * // TODO:itft make it throw on error
     */
    List<Survey> getSurveys();

    List<Survey> getSurveys(boolean metadata);

    int removeSurvey(final int surveyId);

    int updateSurvey(Survey survey);

    SurveyResult getSurveyResult(int resultId);

    List<SurveyResult> getSurveyResults();

    List<SurveyResult> getSurveyResultsForSurvey(int surveyId);

    int createSurveyResult(SurveyResult newResult);

    int updateSurveyResult(SurveyResult surveyResult) throws DataAccessException; // TODO: shouldn't be necessary?

    int removeSurveyResult(int resultId);

    /**
     * Creates a SurveyHistory entry. Shouldn't need to be used, as this will happen
     * whenever a survey is updated.
     *
     * TODO: consider removal
     *
     * @param history the SurveyHistory to persist
     *
     * @return the ID of the SurveyHistory entry if successful
     */
    int createSurveyHistory(SurveyHistory history);

    /**
     * Fetches the specified SurveyHistory
     *
     * @param surveyHistoryId the ID of the SurveyHistory to be returned
     *
     * @return the SurveyHistory entity if found, null otherwise
     */
    SurveyHistory getSurveyHistoryById(long surveyHistoryId);

    /**
     * Fetches the latest archived SurveyHistory entry for the specified Survey
     * @param surveyId the ID of the Survey to fetch the latest history entry for
     *
     * @return a SurveyHistory entity if found, null otherwise
     */
    SurveyHistory getLatestSurveyHistoryForSurvey(int surveyId);

    /**
     * Get SurveyHistory entities for the given Survey
     *
     * @param surveyId
     *
     * @return
     */
    List<SurveyHistory> getSurveyHistory(int surveyId);

    /**
     * Delete the SurveyHistory entity specified
     *
     * @param surveyHistoryId the ID of the SurveyHistory to delete
     *
     * @return the number of rows deleted if successful, -1 otherwise
     */
    int removeSurveyHistory(int surveyHistoryId);

    /**
     * Delete all SurveyHistory entries for the Survey specified
     *
     * @param surveyId the ID of the Survey to delete all histories for
     *
     * @return the number of rows deleted if successful, -1 otherwise
     */
    int removeSurveyHistories(int surveyId);
}