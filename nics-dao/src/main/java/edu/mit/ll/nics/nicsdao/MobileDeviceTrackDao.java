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

import edu.mit.ll.nics.common.constants.SADisplayConstants;
import edu.mit.ll.nics.common.entity.MobileDeviceTrack;
import org.springframework.dao.DataAccessException;


public interface MobileDeviceTrackDao extends BaseDAO {

    /**
     * Checks to see if a matching track already exists, and inserts a new one if it doesn't, and updates the existing
     * one if it does.
     *
     * @param mobileDeviceTrack the track to persist
     * @return true if it was successfully persisted, false otherwise
     *
     * @throws DataAccessException if there was an issue with querying/inserting/updating
     */
    boolean insertOrUpdate(MobileDeviceTrack mobileDeviceTrack) throws DataAccessException;

    /**
     * Delete's the track with the specified values
     *
     * @param deviceId
     * @param username
     * @param workspaceId
     * @return true if successfully deleted, false if not found
     *
     * @throws DataAccessException when there's an issue querying for the track
     */
    boolean delete(String deviceId, String username, Integer workspaceId) throws DataAccessException;

    /**
     * Get {@link MobileDeviceTrack} by its ID
     *
     * @param mobileDeviceTrackId the {@link SADisplayConstants#MDT_ID}
     * @return a {@link MobileDeviceTrack} if found, null otherwise
     *
     * @throws DataAccessException if there's an issue querying for the track
     */
    MobileDeviceTrack getTrackById(long mobileDeviceTrackId) throws DataAccessException;

    /**
     * Gets a MobileDeviceTrack with the specified fields
     *
     * @param deviceId
     * @param username
     * @param workspaceId
     * @return a MobileDeviceTrack if found, null otherwise
     *
     * @throws DataAccessException if there's an issue querying for the track
     */
    MobileDeviceTrack getMobileDeviceTrack(String deviceId, String username, Integer workspaceId)
            throws DataAccessException;
}