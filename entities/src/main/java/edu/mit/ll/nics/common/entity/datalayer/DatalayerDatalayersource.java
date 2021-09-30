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
 * DatalayerDatalayersource
 */
public class DatalayerDatalayersource {

    private String datalayerDatalayersourceid;
    private Datalayersource datalayersource;
    private String datalayersourceid;
    private Datalayer datalayer;
    private String datalayerid;

    public DatalayerDatalayersource() {
    }

    public DatalayerDatalayersource(String datalayerDatalayersourceid, Datalayersource datalayersource,
                                    Datalayer datalayer) {

        this.datalayerDatalayersourceid = datalayerDatalayersourceid;
        this.datalayersource = datalayersource;
        this.datalayer = datalayer;
    }

    public String getDatalayerDatalayersourceid() {
        return this.datalayerDatalayersourceid;
    }

    public void setDatalayerDatalayersourceid(String datalayerDatalayersourceid) {
        this.datalayerDatalayersourceid = datalayerDatalayersourceid;
    }

    public Datalayersource getDatalayersource() {
        return this.datalayersource;
    }

    public void setDatalayersource(Datalayersource datalayersource) {
        this.datalayersource = datalayersource;
    }

    public Datalayer getDatalayer() {
        return this.datalayer;
    }

    public void setDatalayer(Datalayer datalayer) {
        this.datalayer = datalayer;
    }

    public String getDatalayerid() {
        return datalayerid;
    }

    public void setDatalayerid(String datalayerid) {
        this.datalayerid = datalayerid;
    }

    public String getDatalayersourceid() {
        return datalayersourceid;
    }

    public void setDatalayersourceid(String datalayersourceid) {
        this.datalayersourceid = datalayersourceid;
    }
}