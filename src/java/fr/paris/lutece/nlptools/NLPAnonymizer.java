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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * NLPAnonymizer
 */
public class NLPAnonymizer
{
    private static final String PREFIX_OUTPUT_FILE = "anonymized-";
    private static final String PREFIX_LOG_FILE = "log-";

    public static void main( String [ ] args ) throws IOException
    {
        if ( args.length < 1 )
        {
            System.out.println( "Please give a file path as argument " );
            System.exit( 0 );
        }

        List<Finder> listFinders = new ArrayList<>( );
        EmailFinder emailFinder = new EmailFinder( "#Email#" );
        listFinders.add( emailFinder );

        PhoneNumberFinder phoneFinder = new PhoneNumberFinder( "#PhoneNumber#" );
        listFinders.add( phoneFinder );

        PersonNameFinder nameFinder = new PersonNameFinder( "#PersonName#", "en" );
        listFinders.add( nameFinder );

        String strInputFile = args [0];
        String strInput = FileUtils.readFileContent( strInputFile );

        StringBuilder sbLogs = new StringBuilder( );

        for ( Finder finder : listFinders )
        {
            try
            {
                finder.findOccurrences( strInput );
                List<String> listEntities = finder.getFoundEntities( );
                log( sbLogs, "- " + listEntities.size( ) + " entities found by " + finder.getClass( ).getName( ) );

                for ( String strEntity : listEntities )
                {
                    log( sbLogs, "'" + strEntity + "'" );
                }
            }
            catch( FinderException ex )
            {
                log( sbLogs, ex.getMessage( ) );
            }
        }

        String strOutput = strInput;
        for ( Finder finder : listFinders )
        {
            try
            {
                strOutput = finder.replaceOccurrences( strOutput );
            }
            catch( FinderException ex )
            {
                log( sbLogs, ex.getMessage( ) );
            }
        }

        String strOutputFile = getOutputFile( strInputFile, PREFIX_OUTPUT_FILE );
        FileUtils.writeFile( strOutputFile, strOutput );
        String strLogFile = getOutputFile( strInputFile, PREFIX_LOG_FILE );
        FileUtils.writeFile( strLogFile, sbLogs.toString( ) );

        System.exit( 0 );
    }

    private static String getOutputFile( String strFilePath, String strPrefix )
    {
        File file = new File( strFilePath );
        String strPath = file.getPath( ).substring( 0, strFilePath.lastIndexOf( file.getName( ) ) );
        return strPath + strPrefix + file.getName( );

    }

    private static void log( StringBuilder sbLogs, String strLog )
    {
        sbLogs.append( strLog ).append( '\n' );
        System.out.println( strLog );
    }
}
