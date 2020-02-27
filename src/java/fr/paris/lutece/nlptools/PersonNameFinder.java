/*
 * Copyright (c) 2002-2020, Mairie de Paris
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

/**
 * PersonNameFinder
 */
public class PersonNameFinder implements Finder
{

    private static final String TOKEN_DEFAULT_MODEL = "/fr/paris/lutece/nlptools/models/en-token.bin";
    private static final String NAME_FINDER_DEFAULT_MODEL = "/fr/paris/lutece/nlptools/models/en-ner-person.bin";

    private static String _strTokenModel = TOKEN_DEFAULT_MODEL;
    private static TokenizerME _tokenizer;

    private static String _strNameFinderModel = NAME_FINDER_DEFAULT_MODEL;
    private static NameFinderME _nameFinder;

    private static boolean _bInit;
    
    private String _strReplacement;

    public PersonNameFinder()
    {
        _strReplacement = " ";
    }
    
    public PersonNameFinder( String strReplacement )
    {
        _strReplacement = strReplacement;
    }

    /**
     * @return the Model
     */
    public static String getNameModel()
    {
        return _strNameFinderModel;
    }

    /**
     * @param strModel the Model to set
     */
    public static void setNameModel( String strModel )
    {
        _strNameFinderModel = strModel;
    }

    /**
     * @return the Model
     */
    public static String getTokenModel()
    {
        return _strTokenModel;
    }

    /**
     * @param strModel the Model to set
     */
    public static void setTokenModel( String strModel )
    {
        _strTokenModel = strModel;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<String> findOccurrences ( String strInputText ) throws FinderException
    {
        if( !_bInit )
        {
            init();
        }
        
        List<String> listNames = new ArrayList<>();
        String[] sentence = _tokenizer.tokenize( strInputText );
        Span nameSpans[] = _nameFinder.find( sentence );
        for( Span span : nameSpans )
        {
            StringBuilder sbName = new StringBuilder();
            for( int i = span.getStart(); i < span.getEnd(); i++ )
            {
                if( i > span.getStart() )
                {
                    sbName.append( " " );
                }
                sbName.append( sentence[i] );
            }
            listNames.add( sbName.toString() );
        }
        
        return listNames;
        
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
        String strOutputText = strInputText;
        List<String> listNames = findOccurrences( strInputText );
        if( ! listNames.isEmpty() )
        {
            for( String strName : listNames )
            {
                strOutputText = strOutputText.replaceAll( strName, strReplacement );
            }
        }
        return strOutputText;
    }
    
    
    /**
     * Initialize the finder by loading models
     * @throws FinderException 
     */
    private void init() throws FinderException
    {
        try(
                InputStream isTokenModel = PersonNameFinder.class.getResourceAsStream(_strTokenModel );
                InputStream isNameFinderModel = PersonNameFinder.class.getResourceAsStream( _strNameFinderModel ) )
        {
            TokenizerModel tm = new TokenizerModel( isTokenModel );
            _tokenizer = new TokenizerME( tm );
            TokenNameFinderModel model = new TokenNameFinderModel( isNameFinderModel );
            _nameFinder = new NameFinderME( model );
            _bInit = true;
        }
        catch( IOException ex )
        {
            throw new FinderException( "Error loading model : " + ex.getMessage() , ex );
        }

    }
}