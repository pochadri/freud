package org.langera.freud.optional.classobject;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.langera.freud.core.matcher.FreudMatcher;
import org.langera.freud.core.matcher.RegexMatcherAdapter;
import org.langera.freud.core.matcher.RegexMatcherBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public final class ClassObjectDsl
{
    private ClassObjectDsl()
    {
        // static utility
    }


    public static RegexMatcherBuilder<Class> className()
    {
        return new RegexMatcherBuilder<Class>(new RegexMatcherAdapter<Class>()
        {
            @Override
            public String getStringToMatch(final Class toBeAnalysed)
            {
                return toBeAnalysed.getName();
            }

            @Override
            public String matcherDisplayName()
            {
                return "ClassObjectName";
            }
        });

    }

    public static RegexMatcherBuilder<Class> classSimpleName()
    {
        return new RegexMatcherBuilder<Class>(new RegexMatcherAdapter<Class>()
        {
            @Override
            public String getStringToMatch(final Class toBeAnalysed)
            {
                return toBeAnalysed.getSimpleName();
            }

            @Override
            public String matcherDisplayName()
            {
                return "ClassObjectSimpleName";
            }
        });

    }

    public static FreudMatcher<Class> array()
    {
        return new FreudMatcher<Class>()
        {
            @Override
            protected boolean matchesSafely(final Class item)
            {
                return item.isArray();
            }

            @Override
            public void describeTo(final Description description)
            {
                description.appendText("array");
            }
        };
    }


    public static FreudMatcher<Class> anEnum()
    {
        return new FreudMatcher<Class>()
        {
            @Override
            protected boolean matchesSafely(final Class item)
            {
                return item.isEnum();
            }

            @Override
            public void describeTo(final Description description)
            {
                description.appendText("enum");
            }
        };
    }

    public static FreudMatcher<Class> anInterface()
    {
        return new FreudMatcher<Class>()
        {
            @Override
            protected boolean matchesSafely(final Class item)
            {
                return item.isInterface();
            }

            @Override
            public void describeTo(final Description description)
            {
                description.appendText("interface");
            }
        };
    }

    public static FreudMatcher<Class> anonymous()
    {
        return new FreudMatcher<Class>()
        {
            @Override
            protected boolean matchesSafely(final Class item)
            {
                return item.isAnonymousClass();
            }

            @Override
            public void describeTo(final Description description)
            {
                description.appendText("anonymous");
            }
        };
    }

    public static FreudMatcher<Class> localClass()
    {
        return new FreudMatcher<Class>()
        {
            @Override
            protected boolean matchesSafely(final Class item)
            {
                return item.isLocalClass();
            }

            @Override
            public void describeTo(final Description description)
            {
                description.appendText("localClass");
            }
        };
    }

    public static FreudMatcher<Class> memberClass()
    {
        return new FreudMatcher<Class>()
        {
            @Override
            protected boolean matchesSafely(final Class item)
            {
                return item.isMemberClass();
            }

            @Override
            public void describeTo(final Description description)
            {
                description.appendText("memberClass");
            }
        };
    }

    public static FreudMatcher<Class> primitive()
    {
        return new FreudMatcher<Class>()
        {
            @Override
            protected boolean matchesSafely(final Class item)
            {
                return item.isPrimitive();
            }

            @Override
            public void describeTo(final Description description)
            {
                description.appendText("primitive");
            }
        };
    }

    public static FreudMatcher<Class> synthetic()
    {
        return new FreudMatcher<Class>()
        {
            @Override
            protected boolean matchesSafely(final Class item)
            {
                return item.isSynthetic();
            }

            @Override
            public void describeTo(final Description description)
            {
                description.appendText("synthetic");
            }
        };
    }

    public static FreudMatcher<Class> abstractClass()
    {
        return byModifierMatcher(Modifier.ABSTRACT);
    }

    public static FreudMatcher<Class> finalClass()
    {
        return byModifierMatcher(Modifier.FINAL);
    }

    public static FreudMatcher<Class> privateClass()
    {
        return byModifierMatcher(Modifier.PRIVATE);
    }

    public static FreudMatcher<Class> protectedClass()
    {
        return byModifierMatcher(Modifier.PROTECTED);
    }

    public static FreudMatcher<Class> publicClass()
    {
        return byModifierMatcher(Modifier.PUBLIC);
    }

    public static FreudMatcher<Class> staticClass()
    {
        return byModifierMatcher(Modifier.STATIC);
    }

    public static FreudMatcher<Class> subTypeOf(final Class superType)
    {
        return new FreudMatcher<Class>()
        {
            @Override
            protected boolean matchesSafely(final Class item)
            {
                return superType.isAssignableFrom(item);
            }

            @Override
            public void describeTo(final Description description)
            {
                description.appendText("subTypeOf(" + superType.getName() + ")");
            }
        };
    }

    public static FreudMatcher<Class> hasDeclaredMethod(final String methodName, final Class... parameterTypes)
    {
        return new FreudMatcher<Class>()
        {
            @Override
            protected boolean matchesSafely(final Class item)
            {
                try
                {
                    item.getDeclaredMethod(methodName, parameterTypes);
                }
                catch (NoSuchMethodException e)
                {
                    return false;
                }
                return true;
            }

            @Override
            public void describeTo(final Description description)
            {
                description.appendText("hasDeclaredMethod(" + methodName + "(" + Arrays.toString(parameterTypes) + ")" + ")");
            }
        };
    }

    public static FreudMatcher<Class> hasDeclaredFieldOfType(final Class fieldType)
    {
        return new FreudMatcher<Class>()
        {
            @Override
            protected boolean matchesSafely(final Class item)
            {
                for (Field field : item.getDeclaredFields())
                {
                    if (fieldType.isAssignableFrom(field.getType()))
                    {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void describeTo(final Description description)
            {
                description.appendText("hasDeclaredFieldOfType(" + fieldType.getName() + ")");
            }
        };
    }

    public static FreudMatcher<Class> classAnnotation(final Class<? extends Annotation> annotationType)
    {
        return classAnnotation(annotationType, null);
    }

    public static FreudMatcher<Class> classAnnotation(final Class<? extends Annotation> annotationType, final Object value)
    {
        return classAnnotation(annotationType, Matchers.equalTo(value));
    }

    public static FreudMatcher<Class> classAnnotation(final Class<? extends Annotation> annotationType, final Matcher valueMatcher)
    {
        return new AnnotationFreudMatcher<Class>(valueMatcher, annotationType);
    }

    private static FreudMatcher<Class> byModifierMatcher(final int modifierMask)
    {
        return new FreudMatcher<Class>()
        {
            @Override
            protected boolean matchesSafely(final Class item)
            {
                return (item.getModifiers() & modifierMask) != 0;
            }

            @Override
            public void describeTo(final Description description)
            {
                description.appendText("anonymous");
            }
        };
    }
}
