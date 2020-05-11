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
import java.text.MessageFormat;
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
public class PersonNameFinder extends AbstractFinder
{

    private static final String TOKEN_DEFAULT_MODEL = "/fr/paris/lutece/nlptools/models/{0}-token.bin";
    private static final String NAME_FINDER_DEFAULT_MODEL = "/fr/paris/lutece/nlptools/models/{0}-ner-person.bin";
    private static final int MODULO = 1000;

    private static String[] _ignoredNames =
    {
        "Les", "Merci", "R", "Il", "Elle", "Dans", "Pour", "De", "Bien", "Monsieur", "Votre", "Cordialement", "Et", "Ils", "Nous", "Vous", "Sur", "Mes", "Je", "Ainsi", "Ville", "J'ai" , "En", "Alors"
    };

    private static String _strTokenModel = TOKEN_DEFAULT_MODEL;
    private static TokenizerME _tokenizer;

    private String _strNameFinderModel = NAME_FINDER_DEFAULT_MODEL;
    private NameFinderME _nameFinder;

    private boolean _bInit;

    /**
     * Constructor
     */
    public PersonNameFinder()
    {
        super();
    }

    /**
     * Constructor
     *
     * @param strReplacement The replacement
     */
    public PersonNameFinder(String strReplacement)
    {
        super(strReplacement);
    }

    /**
     * Constructor
     *
     * @param strReplacement The replacement
     */
    public PersonNameFinder(String strReplacement, String strLanguage)
    {
        super(strReplacement, strLanguage);
    }

    /**
     * @return the Model
     */
    public String getNameModel()
    {
        return _strNameFinderModel;
    }

    /**
     * @param strModel the Model to set
     */
    public void setNameModel(String strModel)
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
    public static void setTokenModel(String strModel)
    {
        _strTokenModel = strModel;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<String> findOccurrences(String strInput) throws FinderException
    {
        if (!_bInit)
        {
            init();
        }
        else
        {
            _nameFinder.clearAdaptiveData();
        }

        String strCleanedInput = clean(strInput);

        List<String> listNames = new ArrayList<>();
        String[] sentences = strCleanedInput.split("\"");
        int nTotal = sentences.length;
        System.out.println("NameFinder - number of text bloc to process: " + nTotal );
        int nCount = 0;
        int nPercent = 0;
        for (String strSentece : sentences)
        {
            String[] tokens = _tokenizer.tokenize(strSentece);
            Span nameSpans[] = _nameFinder.find(tokens);
            for (Span span : nameSpans)
            {
                StringBuilder sbName = new StringBuilder();
                for (int i = span.getStart(); i < span.getEnd(); i++)
                {
                    if (i > span.getStart())
                    {
                        sbName.append(" ");
                    }
                    sbName.append(tokens[i]);
                }
                String strEntity = sbName.toString();
                if (isValidPersonName(strEntity))
                {
                    listNames.add(strEntity);
                    addEntity(strEntity);
                }
            }
            nCount++;
            int nNewPercent = 100 * nCount / nTotal;
            if ( nNewPercent != nPercent )
            {
                nPercent = nNewPercent;
                System.out.println("NameFinder - number of text bloc processed: " + nCount + " - (" + nPercent + "%)" );
            }
        }
        return listNames;

    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String replaceOccurrences(String strInputText) throws FinderException
    {
        return replaceOccurrences(strInputText, getReplacement());
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String replaceOccurrences(String strInput, String strReplacement) throws FinderException
    {
        if (!_bInit)
        {
            init();
        }
        else
        {
            _nameFinder.clearAdaptiveData();
        }

        String strCleanedInput = clean(strInput);
        String[] sentences = strCleanedInput.split("\"");
        int nTotal = sentences.length;
        System.out.println("NameFinder - number of text bloc to process: " + nTotal );
        StringBuilder sbOutput = new StringBuilder();
        int nCount = 0;
        int nPercent = 0;
        for (String strSentece : sentences)
        {
            sbOutput.append('"');
            String[] tokens = _tokenizer.tokenize(strSentece);
            String[] output = new String[tokens.length];
            Span nameSpans[] = _nameFinder.find(tokens);
            int i = 0;
            int j = 0;
            while (true)
            {

                for (Span span : nameSpans)
                {
                    if (i == span.getStart())
                    {
                        i = span.getEnd();

                        StringBuilder sbName = new StringBuilder();
                        for (int k = span.getStart(); k < span.getEnd(); k++)
                        {
                            if (k > span.getStart())
                            {
                                sbName.append(" ");
                            }
                            sbName.append(tokens[k]);
                        }
                        String strEntity = sbName.toString();
                        if (isValidPersonName(strEntity))
                        {
                            output[j++] = strReplacement;
                        }
                        else
                        {
                            output[j++] = strEntity;
                        }
                    }
                }

                if (i < tokens.length && j < output.length)
                {
                    output[j++] = tokens[i++];
                }
                else
                {
                    break;
                }
            }

            for (String strWord : output)
            {
                if (strWord != null)
                {
                    sbOutput.append(strWord).append(" ");
                }
            }
            nCount++;
            int nNewPercent = 100 * nCount / nTotal;
            if ( nNewPercent != nPercent )
            {
                nPercent = nNewPercent;
                System.out.println("NameFinder - number of text bloc processed: " + nCount + " - (" + nPercent + "%)" );
            }
        }
        sbOutput.append('"');

        System.out.println("NameFinder - number of text bloc processed: " + nCount);
        
        String strOutput = sbOutput.toString();

        strOutput = strOutput.replaceAll( "# # # # # # # # " , "\n" );
        strOutput = strOutput.replaceAll( "# # # # # # # #" , "" );
        strOutput = strOutput.replaceAll( "# # # # # # ##" , "" );
        strOutput = strOutput.replaceAll( "## # # # # ##" , "" );
        strOutput = strOutput.replaceAll( "## # # # # # #" , "" );
        strOutput = strOutput.replaceAll( "#\n# # # # # ##" , "" );
//        strOutput = strOutput.replaceAll( " # # # # ##" , "" );
        
        return strOutput;
    }

    /**
     * Initialize the finder by loading models
     *
     * @throws FinderException
     */
    private void init() throws FinderException
    {
        String strTokenModel = MessageFormat.format(_strTokenModel, getLanguage());
        String strNameFinderModel = MessageFormat.format(_strNameFinderModel, getLanguage());
        try (InputStream isTokenModel = PersonNameFinder.class.getResourceAsStream(strTokenModel);
                InputStream isNameFinderModel = PersonNameFinder.class.getResourceAsStream(strNameFinderModel))
        {
            TokenizerModel tm = new TokenizerModel(isTokenModel);
            _tokenizer = new TokenizerME(tm);
            TokenNameFinderModel model = new TokenNameFinderModel(isNameFinderModel);
            _nameFinder = new NameFinderME(model);
            _bInit = true;
        }
        catch (IOException ex)
        {
            throw new FinderException("Error loading model : " + ex.getMessage(), ex);
        }

    }

    private String clean(String strInput)
    {
        String strClean = strInput.replaceAll("_x000D_", "");
        strClean = strClean.replaceAll( "\n", "########" );
        
        return strClean;
    }

    private boolean isValidPersonName(String strInput)
    {
        for (String strIgnore : _ignoredNames)
        {
            if (strInput.equals(strIgnore))
            {
                return false;
            }
        }
        return true;

    }
}
