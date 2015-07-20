package net.kr9ly.entitysupports.model;

import com.squareup.javapoet.*;
import net.kr9ly.entitysupports.SpecHelper;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.Arrays;

/**
 * Copyright 2015 kr9ly
 * <br />
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <br />
 * http://www.apache.org/licenses/LICENSE-2.0
 * <br />
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class ToStringSupportClassBuilder {

    private ProcessingEnvironment processingEnv;

    private TypeSpec.Builder classBuilder;

    private MethodSpec.Builder toStringMethodBuilder;

    private CodeBlock.Builder toStringMethodCodeBuilder;

    private boolean isFirstField = true;

    public ToStringSupportClassBuilder(ProcessingEnvironment processingEnv, Element toStringElement) {
        this.processingEnv = processingEnv;

        classBuilder = TypeSpec.classBuilder(toToStringName(toStringElement))
                .addAnnotation(SpecHelper.getGeneratedAnnotation())
                .addModifiers(Modifier.PUBLIC);

        toStringMethodBuilder = MethodSpec.methodBuilder("toString")
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                .addParameter(TypeName.get(toStringElement.asType()), "o")
                .returns(String.class);

        toStringMethodCodeBuilder = CodeBlock.builder()
                .add("return \"$T{\" +\n", toStringElement.asType())
                .indent().indent();
    }

    public void addField(VariableElement fieldElement) {
        TypeMirror fieldType = fieldElement.asType();
        Name fieldName = fieldElement.getSimpleName();

        if (SpecHelper.isString(processingEnv, fieldType)) {
            toStringMethodCodeBuilder.add("\"$L$L='\" + o.$L + '\\'' +\n", isFirstField ? ", " : "", fieldName, fieldName);
        } else if (fieldType.getKind() == TypeKind.ARRAY) {
            toStringMethodCodeBuilder.add("\"$L$L=\" + $T.toString(o.$L) +\n", isFirstField ? ", " : "", fieldName, Arrays.class, fieldName);
        } else {
            toStringMethodCodeBuilder.add("\"$L$L=\" + o.$L +\n", isFirstField ? ", " : "", fieldName, fieldName);
        }
    }

    public void addToStringMethod() {
        classBuilder.addMethod(
                toStringMethodBuilder.addCode(
                        toStringMethodCodeBuilder.addStatement("'}'")
                                .unindent().unindent()
                                .build()
                ).build()
        );
    }

    public TypeSpec build() {
        return classBuilder.build();
    }

    private String toToStringName(Element builderElement) {
        return builderElement.getSimpleName() + "ToString";
    }
}
