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

import edu.mit.ll.nics.common.entity.CollabroomFeature;
import edu.mit.ll.nics.common.entity.Feature;
import edu.mit.ll.nics.common.entity.FeatureComment;
import edu.mit.ll.nics.common.entity.UserFeature;
import edu.mit.ll.nics.nicsdao.query.QueryConstraint.UTCRange;
import java.util.List;
import org.json.JSONObject;


public interface FeatureDAO extends BaseDAO {
    public List<Feature> getFeatureState(int collabroomid, UTCRange dateRange, int geoType);

    public List<Feature> getUserFeatureState(int userid);

    public Feature getFeature(long featureId);

    public long addFeature(JSONObject feature, List<String> fields, int geoType) throws Exception;

    public void addCollabroomFeature(CollabroomFeature collabroomFeature) throws Exception;

    public void addUserFeature(UserFeature userFeature) throws Exception;

    public int setCollabroomFeatureDeleted(long featureId, boolean deleted) throws Exception;

    public int setUserFeatureDeleted(long featureId, boolean deleted) throws Exception;

    public int deleteUserFeature(long featureId) throws Exception;

    public void updateFeature(long featureId, JSONObject properties) throws Exception;

    public void updateFeature(long featureId, JSONObject properties, int srsType) throws Exception;

    public List<CollabroomFeature> getCollabroomFeatures(long featureId);

    public List<Long> deleteSharedFeatures(int userId, int collabRoomId);

    public List<Long> shareFeatures(int userId, int collabRoomId);

    public List<Long> markSharedFeaturesDeleted(int userId, int collabRoomId);

    public List<Long> getDeletedFeatures(int collabroomId, UTCRange dateRange);

    public List<Long> copyFeatures(int userId, int collabRoomId);

    public List<Feature> getFeatures(List<Long> featureIds);

    public List<FeatureComment> getFeatureComments(long featureid);

    public int addFeatureComment(FeatureComment featureComment);

    public int deleteFeatureComment(long featureCommentId);

    public int updateFeatureComment(FeatureComment featureComment);
}
