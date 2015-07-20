package net.kr9ly.entitysupports.model;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
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
public class HashCodeSupportClassBuilder {

    private ProcessingEnvironment processingEnv;

    private TypeSpec.Builder classBuilder;

    private MethodSpec.Builder hashCodeMethodBuilder;

    public HashCodeSupportClassBuilder(ProcessingEnvironment processingEnv, Element hashCodeElement) {
        this.processingEnv = processingEnv;

        classBuilder = TypeSpec.classBuilder(toHashCodeName(hashCodeElement))
                .addAnnotation(SpecHelper.getGeneratedAnnotation())
                .addModifiers(Modifier.PUBLIC);

        hashCodeMethodBuilder = MethodSpec.methodBuilder("hashCode")
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                .addParameter(TypeName.get(hashCodeElement.asType()), "o")
                .addStatement("int hashCode = 0")
                .returns(int.class);
    }

    public void addField(VariableElement fieldElement) {
        TypeMirror fieldType = fieldElement.asType();
        Name fieldName = fieldElement.getSimpleName();

        if (fieldType.getKind() == TypeKind.LONG) {
            hashCodeMethodBuilder.addStatement("hashCode = hashCode * 31 + (int) (o.$L ^ (o.$L >>> 32))", fieldName, fieldName);
        } else if (fieldType.getKind() == TypeKind.BOOLEAN) {
            hashCodeMethodBuilder.addStatement("hashCode = hashCode * 31 + (o.$L ? 1 : 0)", fieldName);
        } else if (fieldType.getKind() == TypeKind.INT) {
            hashCodeMethodBuilder.addStatement("hashCode = hashCode * 31 + o.$L", fieldName);
        } else if (fieldType.getKind().isPrimitive()) {
            hashCodeMethodBuilder.addStatement("hashCode = hashCode * 31 + (int) o.$L", fieldName);
        } else if (fieldType.getKind() == TypeKind.ARRAY) {
            hashCodeMethodBuilder.addStatement("hashCode = hashCode * 31 + (o.$L != null ? $T.hashCode(o.$L) : 0)", fieldName, Arrays.class, fieldName);
        } else {
            hashCodeMethodBuilder.addStatement("hashCode = hashCode * 31 + (o.$L != null ? o.$L.hashCode() : 0)", fieldName, fieldName);
        }
    }

    public void addHashCodeMethod() {
        classBuilder.addMethod(
                hashCodeMethodBuilder
                        .addStatement("return hashCode")
                        .build()
        );
    }

    public TypeSpec build() {
        return classBuilder.build();
    }

    private String toHashCodeName(Element builderElement) {
        return builderElement.getSimpleName() + "HashCode";
    }
}
