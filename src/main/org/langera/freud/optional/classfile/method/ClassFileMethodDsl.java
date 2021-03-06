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

package org.langera.freud.optional.classfile.method;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.langera.freud.core.matcher.FreudMatcher;
import org.langera.freud.core.matcher.RegexMatcherAdapter;
import org.langera.freud.core.matcher.StringMatcherBuilder;
import org.langera.freud.optional.classfile.method.instruction.AbstractInstructionVisitor;
import org.langera.freud.optional.classfile.method.instruction.CastOperandStack;
import org.langera.freud.optional.classfile.method.instruction.Instruction;
import org.langera.freud.optional.classfile.method.instruction.Opcode;
import org.langera.freud.optional.classfile.method.instruction.OperandStack;

import java.util.Arrays;

import static org.langera.freud.optional.classfile.method.instruction.AbstractInstructionVisitor.typeEncoding;

public final class ClassFileMethodDsl
{

    private ClassFileMethodDsl()
    {
        // static utility
    }

    public static StringMatcherBuilder<ClassFileMethod> methodName()
    {
        return new StringMatcherBuilder<ClassFileMethod>(new RegexMatcherAdapter<ClassFileMethod>()
        {
            @Override
            public String getStringToMatch(final ClassFileMethod toBeAnalysed)
            {
                return toBeAnalysed.getName();
            }

            @Override
            public String matcherDisplayName()
            {
                return "MethodName";
            }
        });
    }

    private static final String[] EMPTY_ARGS = new String[0];

    public static FreudMatcher<ClassFileMethod> hasMethodInvocation
            (final Class expectedOwner, final String expectedMethodName, final Class... expectedParamsDeclared)
    {
        return new FreudMatcher<ClassFileMethod>()
        {
            private String expectedOwnerName;
            private String[] expectedParamNames;

            {
                expectedOwnerName = typeEncoding(expectedOwner);
                expectedParamNames = (expectedParamsDeclared.length == 0) ? EMPTY_ARGS : new String[expectedParamsDeclared.length];
                for (int i = 0, size = expectedParamsDeclared.length; i < size; i++)
                {
                    expectedParamNames[i] = typeEncoding(expectedParamsDeclared[i]);

                }
            }

            @Override
            protected boolean matchesSafely(final ClassFileMethod item)
            {
                final boolean[] found = new boolean[]{false};
                found[0] = false;
                item.findInstruction(new AbstractInstructionVisitor()
                {
                    @Override
                    public void methodInvocation(final Instruction instruction, final String owner, final String methodName, final String... args)
                    {
                        if (!found[0] && expectedOwnerName.equals(owner) &&
                                expectedMethodName.equals(methodName) &&
                                Arrays.equals(expectedParamNames, args))
                        {
                            found[0] = true;
                        }

                    }
                });
                return found[0];
            }

            @Override
            public void describeTo(final Description description)
            {
                description.appendText("hasMethodInvocation(" + expectedOwner.getName() + ", " + expectedMethodName + ", " + Arrays.toString(expectedParamNames) + ")");
            }
        };
    }

    public static FreudMatcher<ClassFileMethod> methodInvokedWithParams
                                        (final Class expectedOwner,
                                         final String expectedMethodName,
                                         final Matcher<OperandStack>... expectedParamsPassed)
    {
        return new FreudMatcher<ClassFileMethod>()
        {
            private String expectedOwnerName;

            {
                expectedOwnerName = typeEncoding(expectedOwner);
            }

            @Override
            protected boolean matchesSafely(final ClassFileMethod item)
            {
                final boolean[] found = new boolean[1];
                found[0] = false;
                item.findInstruction(new AbstractInstructionVisitor()
                {
                    @Override
                    public void methodInvocation(final Instruction instruction, final String owner, final String methodName, final String... args)
                    {
                        if (!found[0] && expectedOwnerName.equals(owner) &&
                                expectedMethodName.equals(methodName))
                        {
                            Instruction prevInstruction = item.getInstruction(instruction.getInstructionIndex() - 1);
                            OperandStack operandStack = prevInstruction.getOperandStack();
                            found[0] = true;
                            for (int i = expectedParamsPassed.length - 1; i >= 0; i--)
                            {
                                Matcher<OperandStack> matcher = expectedParamsPassed[i];
                                if (!matcher.matches(operandStack))
                                {
                                    found[0] = false;
                                    break;
                                }
                                operandStack = operandStack.next();
                            }
                        }
                    }
                });
                return found[0];
            }

            @Override
            public void describeTo(final Description description)
            {
                description.appendText("methodInvokedWithParams(" + expectedOwner.getName() + ", " + expectedMethodName + ")");
            }
        };
    }

    public static FreudMatcher<ClassFileMethod> containsInstructions(final Opcode... opcodes)
    {
        return new FreudMatcher<ClassFileMethod>()
        {
            private Instruction found = null;

            @Override
            protected boolean matchesSafely(final ClassFileMethod item)
            {
                item.findInstruction(new AbstractInstructionVisitor()
                {
                    @Override
                    public void noArgInstruction(final Instruction instruction)
                    {
                        for (int i = 0; i < opcodes.length; i++)
                        {
                            Opcode opcode = opcodes[i];
                            if (instruction.getOpcode() == opcode)
                            {
                                found = instruction;
                                break;
                            }
                        }
                    }
                });
                return found != null;
            }

            @Override
            public void describeTo(final Description description)
            {
                description.appendText("containsInstructions(");
                for (int i = 0; i < opcodes.length; i++)
                {
                    Opcode opcode = opcodes[i];
                    description.appendText(opcode.name());
                    description.appendText(", ");

                }
                description.appendText(") found");
            }
        };
    }

    public static FreudMatcher<OperandStack> a(final Class<?> aClass)
    {
        return new FreudMatcher<OperandStack>()
        {
            private final String expectedType = typeEncoding(aClass);

            @Override
            protected boolean matchesSafely(final OperandStack item)
            {
                return expectedType.equals(item.getOperandType());
            }

            @Override
            public void describeTo(final Description description)
            {
                description.appendText("a(" + aClass + ")");
            }
        };
    }

    public static FreudMatcher<OperandStack> aConstantOf(final Class<?> aClass)
    {
        return new FreudMatcher<OperandStack>()
        {
            private final String expectedType = typeEncoding(aClass);

            @Override
            protected boolean matchesSafely(final OperandStack item)
            {
                return expectedType.equals(item.getOperandType()) &&
                        item.getGeneratingOpcode().isConstant();
            }

            @Override
            public void describeTo(final Description description)
            {
                description.appendText("aConstantOf(" + aClass + ")");
            }
        };
    }

    public static FreudMatcher<OperandStack> castOf(final Class<?> aClass)
    {
        return new FreudMatcher<OperandStack>()
        {
            private final String expectedType = typeEncoding(aClass);

            @Override
            protected boolean matchesSafely(final OperandStack item)
            {
                if (item instanceof CastOperandStack)
                {
                    return expectedType.equals(((CastOperandStack) item).getFromType());
                }
                return false;
            }

            @Override
            public void describeTo(final Description description)
            {
                description.appendText("a(" + aClass + ")");
            }
        };
    }
}
