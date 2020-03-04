/*
 * Copyright (c) 2002-2019, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */

package fr.paris.lutece.nlptools;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract Finder
 */
public abstract class AbstractFinder implements Finder
{
    private String _strReplacement;
    private List<String> _listFoundEntities = new ArrayList<>( );

    /**
     * Constructor
     */
    public AbstractFinder( )
    {
        _strReplacement = " ";
    }

    /**
     * Constructor
     * 
     * @param strReplacement
     *            Replacement string
     */
    public AbstractFinder( String strReplacement )
    {
        _strReplacement = strReplacement;
    }

    /**
     * @return the replacement
     */
    public String getReplacement( )
    {
        return _strReplacement;
    }

    /**
     * @param strReplacement
     *            the replacement to set
     */
    public void setReplacement( String strReplacement )
    {
        _strReplacement = strReplacement;
    }

    /**
     * Add a found entity
     * 
     * @param strEntity
     *            The entity
     */
    public void addEntity( String strEntity )
    {
        _listFoundEntities.add( strEntity );
    }

    /**
     * Get all found entities
     * 
     * @return The list
     */
    @Override
    public List<String> getFoundEntities( )
    {
        return new ArrayList( _listFoundEntities );
    }

}
