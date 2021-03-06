/*
 * Copyright (c) 2011.
 * This file is part of "Freud".
 *
 * Freud is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Freud is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Freud.  If not, see <http://www.gnu.org/licenses/>.
 * @author Amir Langer  langera_at_gmail_dot_com
 */

package org.langera.examples.javasource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.langera.freud.core.FreudAnalyser;
import org.langera.freud.core.iterator.resource.ResourceIterators;
import org.langera.freud.core.listener.AnalysisListenerStub;
import org.langera.freud.optional.javasource.JavaSourceJdom;

import static org.langera.freud.optional.javasource.JavaSourceTestMatchers.codeBlockIn;

public class AnalyseCodeBlockSizeLimitTest
{
    private AnalysisListenerStub listener;

    @Test
    public void shouldAnalyseCodeBlockSizeLimitWhenItsOk() throws Exception
    {
        FreudAnalyser analysis = JavaSourceExamples.codeBlockSizeIsLimitedTo30Lines(
                ResourceIterators.selfResourceIterator(JavaSourceJdom.PARSER,
                        "package org.langera.examples;\n" +
                                " \n" +
                                "public class SimpleClass \n" +
                                "{ \n" +
                                " \n" +
                                "  public String toString()\n" +
                                "  {\n" +
                                "       return \"\" + getClass();\n" +
                                "  }\n" +
                                "}"));

        analysis.analyse(listener);

        Assert.assertEquals(1, listener.getTotalObjectsAnalysed());
        listener.assertPassed(codeBlockIn("SimpleClass"));
    }

    @Test
    public void shouldAnalyseCodeBlockSizeLimitWhenIReturnTypeIsVoid() throws Exception
    {
        FreudAnalyser analysis = JavaSourceExamples.codeBlockSizeIsLimitedTo30Lines(
                ResourceIterators.selfResourceIterator(JavaSourceJdom.PARSER,
                "package org.langera.examples;\n" +
                        " \n" +
                        "public class SimpleClass \n" +
                        "{ \n" +
                        " \n" +
                        "  public void doStuff()\n" +
                        "  {\n" +
                        "       toString();\n" +
                        "  }\n" +
                        "}"));

        analysis.analyse(listener);

        Assert.assertEquals(1, listener.getTotalObjectsAnalysed());
        listener.assertPassed(codeBlockIn("SimpleClass"));
    }

    @Test
    public void shouldAnalyseCodeBlockSizeLimitWhenItsTooBig() throws Exception
    {
        FreudAnalyser analysis = JavaSourceExamples.codeBlockSizeIsLimitedTo30Lines(
                ResourceIterators.selfResourceIterator(JavaSourceJdom.PARSER,
                "package org.langera.examples;\n" +
                        " \n" +
                        "public class SimpleClass \n" +
                        "{ \n" +
                        " \n" +
                        "  public String toString()\n" +
                        "  {\n" +
                        "       int a1 = 1;\n" +
                        "       int a2 = 1;\n" +
                        "       int a3 = 1;\n" +
                        "       int a4 = 1;\n" +
                        "       int a5 = 1;\n" +
                        "       int a6 = 1;\n" +
                        "       int a7 = 1;\n" +
                        "       int a8 = 1;\n" +
                        "       int a9 = 1;\n" +
                        "       int a10 = 1;\n" +
                        "       int a11 = 1;\n" +
                        "       int a12 = 1;\n" +
                        "       int a13 = 1;\n" +
                        "       int a14 = 1;\n" +
                        "       int a15 = 1;\n" +
                        "       int a16 = 1;\n" +
                        "       int a17 = 1;\n" +
                        "       int a18 = 1;\n" +
                        "       int a19 = 1;\n" +
                        "       int a20 = 1;\n" +
                        "       int a21 = 1;\n" +
                        "       int a22 = 1;\n" +
                        "       int a23 = 1;\n" +
                        "       int a24 = 1;\n" +
                        "       int a25 = 1;\n" +
                        "       int a26 = 1;\n" +
                        "       int a27 = 1;\n" +
                        "       int a28 = 1;\n" +
                        "       int a29 = 1;\n" +
                        "       int a30 = 1;\n" +
                        "       return \"\" + getClass();" +
                        "  }\n" +
                        "}"));

        analysis.analyse(listener);

        Assert.assertEquals(1, listener.getTotalObjectsAnalysed());
        listener.assertFailed(codeBlockIn("SimpleClass"));
    }

    @Test
    public void shouldAnalyseCodeBlockSizeLimitWhenItsBigButOnlyBecauseOfCommentedBlocks() throws Exception
    {
        FreudAnalyser analysis = JavaSourceExamples.codeBlockSizeIsLimitedTo30Lines(
                ResourceIterators.selfResourceIterator(JavaSourceJdom.PARSER,
                "package org.langera.examples;\n" +
                        " \n" +
                        "public class SimpleClass1 \n" +
                        "{ \n" +
                        " \n" +
                        "  public String toString()\n" +
                        "  {\n" +
                        " //      int a1 = 1;\n" +
                        " //      int a2 = 1;\n" +
                        " //      int a3 = 1;\n" +
                        " //      int a4 = 1;\n" +
                        " //      int a5 = 1;\n" +
                        " //      int a6 = 1;\n" +
                        " //      int a7 = 1;\n" +
                        " //      int a8 = 1;\n" +
                        " //      int a9 = 1;\n" +
                        " //      int a10 = 1;\n" +
                        "       int a11 = 1;\n" +
                        "       int a12 = 1;\n" +
                        "       int a13 = 1;\n" +
                        "       int a14 = 1;\n" +
                        "       int a15 = 1;\n" +
                        "       int a16 = 1;\n" +
                        "       int a17 = 1;\n" +
                        "       int a18 = 1;\n" +
                        "       int a19 = 1;\n" +
                        "       int a20 = 1;\n" +
                        "       int a21 = 1;\n" +
                        "       int a22 = 1;\n" +
                        "       int a23 = 1;\n" +
                        "       int a24 = 1;\n" +
                        "       int a25 = 1;\n" +
                        "       int a26 = 1;\n" +
                        "       int a27 = 1;\n" +
                        "       int a28 = 1;\n" +
                        "       int a29 = 1;\n" +
                        "       int a30 = 1;\n" +
                        "       return \"\" + getClass();" +
                        "  }\n" +
                        "}",
                "package org.langera.examples;\n" +
                        " \n" +
                        "public class SimpleClass2 \n" +
                        "{ \n" +
                        " \n" +
                        "  public String toString()\n" +
                        "  {\n" +
                        " /*      int a1 = 1;\n" +
                        "       int a2 = 1;\n" +
                        "       int a3 = 1;\n" +
                        "       int a4 = 1;\n" +
                        "       int a5 = 1;\n" +
                        "       int a6 = 1;\n" +
                        "       int a7 = 1;\n" +
                        "       int a8 = 1;\n" +
                        "       int a9 = 1;\n" +
                        " */      int a10 = 1;\n" +
                        "       int a11 = 1;\n" +
                        "       int a12 = 1;\n" +
                        "       int a13 = 1;\n" +
                        "       int a14 = 1;\n" +
                        "       int a15 = 1;\n" +
                        "       int a16 = 1;\n" +
                        "       int a17 = 1;\n" +
                        "       int a18 = 1;\n" +
                        "       int a19 = 1;\n" +
                        "       int a20 = 1;\n" +
                        "       int a21 = 1;\n" +
                        "       int a22 = 1;\n" +
                        "       int a23 = 1;\n" +
                        "       int a24 = 1;\n" +
                        "       int a25 = 1;\n" +
                        "       int a26 = 1;\n" +
                        "       int a27 = 1;\n" +
                        "       int a28 = 1;\n" +
                        "       int a29 = 1;\n" +
                        "       int a30 = 1;\n" +
                        "       return \"\" + getClass();" +
                        "  }\n" +
                        "}"));

        analysis.analyse(listener);

        Assert.assertEquals(2, listener.getTotalObjectsAnalysed());
        listener.assertPassed(codeBlockIn("SimpleClass1"));
        listener.assertPassed(codeBlockIn("SimpleClass2"));
    }

    @Before
    public void setUp() throws Exception
    {
        listener = new AnalysisListenerStub();

    }
}
