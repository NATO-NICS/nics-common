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

import edu.mit.ll.nics.common.entity.MediaStream;
import java.util.List;

/**
 * Interface for the MediaStream DAO
 */
public interface MediaStreamDAO extends BaseDAO {
    /**
     * Responsible for querying {@link MediaStream} from the specified workspace
     *
     * @param workspaceId the workspaceid to get MediaStreams from
     * @return a list of {@link MediaStream} objects if found, empty list otherwise
     */
    List<MediaStream> readMediaStreamsByWorkspaceId(int workspaceId) throws Exception;

    /**
     * Responsible for querying {@link MediaStream} from the specified workspace, and matching on the name and/or url.
     * If both name and url are provided, both must match, if just one is provided, will only match one. All matches
     * must be exact.
     *
     * @param workspaceId the workspaceid to get MediaStreams from
     * @param name        the name of the stream to find
     * @param url         the url of the stream to find
     * @return a list of {@link MediaStream} objects if found, empty list otherwise
     */
    List<MediaStream> findMediaStreamsByWorkspaceId(int workspaceId, String name, String url) throws Exception;

    /**
     * Responsible for inserting a MediaStream into the database. msid field on MediaStream is ignored. If the
     * MediaStream is successfully created, it's returned with the actual msid given.
     *
     * @param stream      the stream entity to create
     * @param workspaceId the workspace the stream belongs to
     * @return a {@link MediaStream} entity representing the data inserted if successful, null otherwise
     */
    MediaStream createMediaStream(MediaStream stream, int workspaceId) throws Exception;

    /**
     * Responsible for modifying an existing MediaStream entry
     *
     * @param mediaStream the MediaStreaming entity to update
     * @return the updated {@link MediaStream} entity if successful, null otherwise
     */
    MediaStream updateMediaStream(MediaStream mediaStream) throws Exception;

    /**
     * Responsible for deleting a single entry in the MediaStream table
     *
     * @param streamId the ID of the MediaStream to delete
     * @return true if successful, false otherwise
     */
    boolean deleteMediaStream(long streamId) throws Exception;

    /**
     * Responsible for checking if a stream with the specified id exists
     *
     * @param streamId ID of stream to check for
     * @return true if it exists, false otherwise
     *
     * @throws Exception if there's a problem performing the check
     */
    boolean exists(long streamId) throws Exception;
}
