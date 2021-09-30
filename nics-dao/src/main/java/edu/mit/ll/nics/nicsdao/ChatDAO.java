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

import edu.mit.ll.nics.common.entity.Chat;
import edu.mit.ll.nics.nicsdao.query.QueryConstraint.OrderBy;
import edu.mit.ll.nics.nicsdao.query.QueryConstraint.ResultSetPage;
import edu.mit.ll.nics.nicsdao.query.QueryConstraint.UTCRange;
import java.util.List;

/**
 * ChatDAO Interface for interacting with {@link Chat} entities
 */
public interface ChatDAO extends BaseDAO {

    /**
     * Responsible for retrieving {@link Chat} messages
     *
     * @param collabroomId the id of the collabroom the messages are associated with
     * @param dateRange    date range of messages to return
     * @param orderBy      desired ordering of results when queried
     * @param pageRange    page range
     * @return list of {@link Chat} entities if found
     */
    List<Chat> getChatMessages(int collabroomId, UTCRange dateRange, OrderBy orderBy, ResultSetPage pageRange);

    /**
     * Responsible for retrieving a {@link Chat} entity with the specified id.
     *
     * @param chatId the id of the entity being requested
     * @return a {@link Chat} entity if found, TODO: null if not found, or exception thrown?
     */
    Chat getChatMessage(long chatId);

    /**
     * Responsible for persisting a {@link Chat} entity.
     *
     * @param chat entity to persist
     * @return that id of the chat entity
     *
     * @throws Exception when there was an error persisting the entity
     */
    long addChat(Chat chat) throws Exception;
}
