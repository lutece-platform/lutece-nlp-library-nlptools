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
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * EmailFinder
 */
public class EmailFinder implements Finder
{
    private static final Pattern MAIL_REGEX = Pattern.compile("[-_.0-9A-Za-z]+@[-_0-9A-Za-z]+[-_.0-9A-Za-z]+");

    private String _strReplacement;

    /**
     * Constructor
     */
    public EmailFinder()
    {
        _strReplacement = " ";
    }
    
    /**
     * Constructor
     * @param strReplacement Replacement string 
     */
    public EmailFinder( String strReplacement )
    {
        _strReplacement = strReplacement;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public List<String> findOccurrences( String strInputText ) throws FinderException
    {
        List<String> listOccurrences = new ArrayList<>();
        Matcher matcher = MAIL_REGEX.matcher( strInputText );
        while( matcher.find() )
        {
            listOccurrences.add( matcher.group() );
        }
       return listOccurrences;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String replaceOccurrences( String strInputText ) throws FinderException
    {
        return replaceOccurrences( strInputText, _strReplacement );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String replaceOccurrences( String strInputText, String strReplacement ) throws FinderException
    {
        return MAIL_REGEX.matcher( strInputText ).replaceAll( strReplacement );
    }

}
