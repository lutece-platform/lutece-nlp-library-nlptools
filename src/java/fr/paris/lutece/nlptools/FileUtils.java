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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * FileUtils
 */
public class FileUtils
{
    /**
     * read the file content
     * 
     * @param strInputFile
     *            The file path
     * @return The content
     * @throws IOException
     *             if an error occurs
     */

    public static String readFileContent( String strInputFile ) throws IOException
    {

        try( InputStream is = new FileInputStream( strInputFile ) ;
                InputStreamReader isr = new InputStreamReader( is ) ;
                BufferedReader in = new BufferedReader( isr ) ; )
        {
            Writer writer = new StringWriter( );
            char [ ] buffer = new char [ 1024];
            int n;
            while ( ( n = in.read( buffer ) ) != -1 )
            {
                writer.write( buffer, 0, n );
            }

            return writer.toString( );
        }

    }

    /**
     * Write file
     * 
     * @param strOutputFile
     *            The file path
     * @param strContent
     *            The content
     * @throws IOException
     *             if an error occurs
     */
    public static void writeFile( String strOutputFile, String strContent ) throws IOException
    {
        FileWriter fstream = new FileWriter( strOutputFile );
        try( BufferedWriter out = new BufferedWriter( fstream ) )
        {
            out.write( strContent );
        }

    }
}
