package org.langera.freud.optional.text.textline;

import org.langera.freud.core.FreudBuilderException;
import org.langera.freud.core.FreudConfig;
import org.langera.freud.core.iterator.AnalysedObjectIterator;
import org.langera.freud.core.iterator.SubTypeAnalysedObjectIterator;
import org.langera.freud.core.iterator.SubTypeIteratorBuilder;
import org.langera.freud.optional.text.Text;

public final class TextLineFreudConfig implements FreudConfig<TextLine>
{

    @Override
    public Class<?> supports()
    {
        return Text.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public AnalysedObjectIterator<TextLine> iteratorAdapter(final AnalysedObjectIterator<?> superTypeIterator) throws FreudBuilderException
    {
        return new SubTypeAnalysedObjectIterator<Text, TextLine>((AnalysedObjectIterator<Text>) superTypeIterator,
                new SubTypeIteratorBuilder<Text, TextLine>()
                {
                    @Override
                    public Iterable<TextLine> buildIterable(final Text superTypeItem)
                    {
                        return superTypeItem.getTextLines();
                    }
                }, TextLine.class);
    }
}
