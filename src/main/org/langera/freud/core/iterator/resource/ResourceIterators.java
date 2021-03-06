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

package org.langera.freud.core.iterator.resource;

import org.langera.freud.core.iterator.AnalysedObjectIterator;
import org.langera.freud.core.iterator.IteratorWrapperAnalysedObjectIterator;

import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Iterator;

/**
 * This file is part of "Freud".
 * <p/>
 * Freud is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * Freud is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with Freud.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @author Amir Langer  langera_at_gmail_dot_com
 */

public final class ResourceIterators
{
    private ResourceIterators()
    {
        // static utility
    }

    public static <T> AnalysedObjectIterator<T> filesByPathResourceIterator(
            ResourceParser<T> parser, FilenameFilter filenameFilter,
            boolean recursive, String... filepaths)
    {
        return new ResourceDirectoryIterator<T>(parser, true, filenameFilter, recursive, filepaths);
    }

    public static <T> AnalysedObjectIterator<T> fileResourceIterator(ResourceParser<T> parser, String... filepaths)
    {
        return new ResourceByIdentifierIterator<T>(parser, false, FileResource.getInstance(), filepaths);
    }

    public static <T> AnalysedObjectIterator<T> selfResourceIterator(ResourceParser<T> parser, String... contents)
    {
        return new ResourceByIdentifierIterator<T>(parser, false, SelfResource.getInstance(), contents);
    }

    @SuppressWarnings("unchecked")
    public static <T> AnalysedObjectIterator<T> selfResourceIterator(T... contents)
    {
        return new IteratorWrapperAnalysedObjectIterator<T>(Arrays.asList(contents), (Class<T>) contents[0].getClass(), false);
    }

    public static <T> AnalysedObjectIterator<T> analysedObjectIterator(Iterator<T> iterator, Class<T> type)
    {
        return new IteratorWrapperAnalysedObjectIterator<T>(iterator, type, false);
    }

    public static <T> AnalysedObjectIterator<T> classpathResourceIterator(ResourceParser<T> parser, String... names)
    {
        return new ResourceByIdentifierIterator<T>(parser, false, ClasspathResource.getInstance(), names);
    }
}
