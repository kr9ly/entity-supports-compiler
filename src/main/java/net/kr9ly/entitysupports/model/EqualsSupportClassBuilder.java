package net.kr9ly.entitysupports.model;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
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
public class EqualsSupportClassBuilder {

    private ProcessingEnvironment processingEnv;

    private TypeSpec.Builder classBuilder;

    private MethodSpec.Builder equalsMethodBuilder;

    private CodeBlock.Builder equalsMethodCodeBuilder;

    public EqualsSupportClassBuilder(ProcessingEnvironment processingEnv, Element equalsElement) {
        this.processingEnv = processingEnv;
        classBuilder = TypeSpec.classBuilder(toEqualsName(equalsElement))
                .addAnnotation(SpecHelper.getGeneratedAnnotation())
                .addModifiers(Modifier.PUBLIC);

        equalsMethodBuilder = MethodSpec.methodBuilder("equals")
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                .addParameter(Object.class, "o1")
                .addParameter(Object.class, "o2")
                .returns(boolean.class);

        equalsMethodCodeBuilder = CodeBlock.builder()
                .addStatement("if (o1 == o2) return true")
                .addStatement("if (o1 == null || o2 == null || o1.getClass() != o2.getClass()) return false")
                .addStatement("$T left = ($T) o1", equalsElement.asType(), equalsElement.asType())
                .addStatement("$T right = ($T) o2", equalsElement.asType(), equalsElement.asType());
    }

    public void addField(VariableElement fieldElement) {
        TypeMirror fieldType = fieldElement.asType();
        Name fieldName = fieldElement.getSimpleName();

        if (fieldType.getKind().isPrimitive() || SpecHelper.isEnumType(processingEnv, fieldType)) {
            equalsMethodCodeBuilder.addStatement("if (left.$L != right.$L) return false", fieldName, fieldName);
        } else if (fieldType.getKind() == TypeKind.ARRAY) {
            equalsMethodCodeBuilder.addStatement("if (!$T.equals(left.$L, right.$L)) return false", Arrays.class, fieldName, fieldName);
        } else {
            equalsMethodCodeBuilder.addStatement("if (left.$L != null ? !left.$L.equals(right.$L) : right.$L != null) return false", fieldName, fieldName, fieldName, fieldName);
        }
    }

    public void addEqualsMethod() {
        classBuilder.addMethod(
                equalsMethodBuilder
                        .addCode(equalsMethodCodeBuilder
                                .addStatement("return true")
                                .build())
                        .build()
        );
    }

    public TypeSpec build() {
        return classBuilder.build();
    }

    private String toEqualsName(Element builderElement) {
        return builderElement.getSimpleName() + "Equals";
    }
}
