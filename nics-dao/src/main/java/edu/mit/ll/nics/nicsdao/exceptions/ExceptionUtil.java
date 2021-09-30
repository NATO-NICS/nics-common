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
package edu.mit.ll.nics.nicsdao.exceptions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

public class ExceptionUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionUtil.class);


    /**
     * Reads the DataAccessException to glean more user-friendly, sanitized messaging to return to the client. Intent is
     * to generalize for known constraint/duplicate issues, and not return large SQL message that gives away schema
     * information to the client. New Exception is always created with the original to not lose information.
     *
     * <p>This method also LOGs the original error, using the <i>preface</i> parameter as the message</p>
     *
     * @param exception the original exception caught
     *
     * @return A DataAccessException with a sanitized/friendly message for known issues
     */
    public static DataAccessException getFriendlySanitizedException(String preface, DataAccessException exception) {
        // TODO: Not great logging here, since wherever this ends up in the client will log usually log it as well
        LOG.error(preface, exception);

        // TODO: get sqlerrorcode translator maybe to return a nicer error for us OR
        //  make utility method that reads the detail message part, and based on keywords can
        //  send an appropriate response
        Throwable cause = exception.getCause();
        String message = cause.getMessage();

        // TODO: If there's a key mentioned, get it so we know which constraint failure
        //  to respond with... something like "key()"... use regex to pull it out
        String key = null;
        final Pattern pattern = Pattern.compile(".*\\((\\w+)\\)=.*");
        final Matcher matcher = pattern.matcher(message);
        if(matcher.find()) {
            key = matcher.group(1);
            // TODO: need to see if it matches more than one, and get the key, not the value, both are put in parens
        }

        // TODO: parse out Detail
        /*String[] parts = message.split("Detail");
        String detail = parts[1];*/

        if(message.contains("duplicate key")) {
            if(key != null && message.contains("("+key+")")) {
                return new DuplicateKeyException("A Symbology with that " + key + " already exists", exception);
            } else {
                return new DuplicateKeyException("A duplicate Symbology already exists", exception);
            }
        } else if(message.contains("foreign key constraint")) {
            if(key != null && message.contains("("+key+")")) {
                return new DataIntegrityViolationException("The specified " + key + " was not found",
                        exception);
            } else {
                return new DataIntegrityViolationException("Constraint violation");
            }
        } else {
            return new DataIntegrityViolationException("There was a Data Access issue", exception);
        }
    }
}
