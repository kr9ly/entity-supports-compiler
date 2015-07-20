package net.kr9ly.entitysupports.model;

import com.squareup.javapoet.*;
import net.kr9ly.entitysupports.SpecHelper;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

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
public class BuilderSupportClassBuilder {

    private ProcessingEnvironment processingEnv;

    private ClassName className;

    private TypeSpec.Builder classBuilder;

    private MethodSpec.Builder buildMethodBuilder;

    private CodeBlock.Builder buildMethodCodeBuilder;

    public BuilderSupportClassBuilder(ProcessingEnvironment processingEnv, Element builderElement) {
        this.processingEnv = processingEnv;
        TypeName entityType = TypeName.get(builderElement.asType());
        className = ClassName.bestGuess(SpecHelper.getPackageName(processingEnv, builderElement) + "." + toSupportName(builderElement));
        classBuilder = TypeSpec.classBuilder(className.simpleName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(SpecHelper.getGeneratedAnnotation());
        buildMethodBuilder = MethodSpec.methodBuilder("build")
                .returns(entityType)
                .addModifiers(Modifier.PUBLIC);
        buildMethodCodeBuilder = CodeBlock.builder()
                .addStatement("$T instance = new $T()", entityType, entityType);
        classBuilder.addMethod(
                MethodSpec.methodBuilder("newBuilder")
                        .returns(className)
                        .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                        .addStatement("return new $T()", className)
                        .build()
        );
    }

    public void addField(VariableElement fieldElement) {
        String fieldName = fieldElement.getSimpleName().toString();
        classBuilder.addField(TypeName.get(fieldElement.asType()), fieldName, Modifier.PRIVATE);
        classBuilder.addMethod(
                MethodSpec.methodBuilder(toSetterName(fieldElement))
                        .addModifiers(Modifier.PUBLIC)
                        .returns(className)
                        .addParameter(TypeName.get(fieldElement.asType()), fieldName)
                        .addStatement("this.$L = $L", fieldName, fieldName)
                        .addStatement("return this")
                        .build()
        );
        buildMethodCodeBuilder.addStatement("instance.$L = $L", fieldName, fieldName);
    }

    public void addBuildMethod() {
        classBuilder.addMethod(
                buildMethodBuilder.addCode(
                        buildMethodCodeBuilder.addStatement("return instance")
                                .build()
                ).build()
        );
    }

    public TypeSpec build() {
        return classBuilder.build();
    }

    private String toSupportName(Element builderElement) {
        return builderElement.getSimpleName() + "Builder";
    }

    private String toSetterName(Element fieldElement) {
        return fieldElement.getSimpleName().toString();
    }
}
