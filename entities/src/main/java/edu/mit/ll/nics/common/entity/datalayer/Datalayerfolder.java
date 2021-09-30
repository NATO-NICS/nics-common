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
package edu.mit.ll.nics.common.entity.datalayer;


/**
 * Datalayerfolder
 */
public class Datalayerfolder {

    private int datalayerfolderid;
    private String datalayerid;
    private String folderid;
    private int index;

    private Datalayer datalayer;
    private Folder folder;

    public Datalayerfolder() {
    }

    public Datalayerfolder(int datalayerfolderid,
                           String datalayerid, String folderid, int index) {

        this.datalayerfolderid = datalayerfolderid;
        this.datalayerid = datalayerid;
        this.folderid = folderid;
        this.index = index;
    }

    public int getDatalayerfolderid() {
        return this.datalayerfolderid;
    }

    public void setDatalayerfolderid(int datalayerfolderid) {
        this.datalayerfolderid = datalayerfolderid;
    }

    public Datalayer getDatalayer() {
        return this.datalayer;
    }

    public void setDatalayer(Datalayer datalayer) {
        this.datalayer = datalayer;
    }

    public Folder getFolder() {
        return this.folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getDatalayerid() {
        return datalayerid;
    }

    public void setDatalayerid(String datalayerid) {
        this.datalayerid = datalayerid;
    }

    public String getFolderid() {
        return this.folderid;
    }

    public void setFolderid(String folderid) {
        this.folderid = folderid;
    }
}