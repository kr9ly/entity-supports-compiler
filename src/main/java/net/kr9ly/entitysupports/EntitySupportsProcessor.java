package net.kr9ly.entitysupports;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import net.kr9ly.entitysupports.model.BuilderSupportClassBuilder;
import net.kr9ly.entitysupports.model.EqualsSupportClassBuilder;
import net.kr9ly.entitysupports.model.HashCodeSupportClassBuilder;
import net.kr9ly.entitysupports.model.ToStringSupportClassBuilder;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Set;

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
@AutoService(Processor.class)
@SupportedAnnotationTypes({"net.kr9ly.entitysupports.*"})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class EntitySupportsProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processBuilderSupports(roundEnv);
        processEqualsSupports(roundEnv);
        processHashCodeSupports(roundEnv);
        processToStringSupports(roundEnv);
        return false;
    }

    private void processBuilderSupports(RoundEnvironment roundEnv) {
        Set<? extends Element> builderElements = roundEnv.getElementsAnnotatedWith(BuilderSupport.class);
        for (Element builderElement : builderElements) {
            BuilderSupportClassBuilder supportClassBuilder = new BuilderSupportClassBuilder(processingEnv, builderElement);
            for (Element member : builderElement.getEnclosedElements()) {
                if (member.getKind() == ElementKind.FIELD) {
                    supportClassBuilder.addField((VariableElement) member);
                }
            }

            supportClassBuilder.addBuildMethod();
            JavaFile javaFile = JavaFile.builder(SpecHelper.getPackageName(processingEnv, builderElement), supportClassBuilder.build())
                    .build();

            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "fail to write component file.", builderElement);
            }
        }
    }

    private void processEqualsSupports(RoundEnvironment roundEnv) {
        Set<? extends Element> equalsElements = roundEnv.getElementsAnnotatedWith(EqualsSupport.class);
        for (Element equalsElement : equalsElements) {
            EqualsSupportClassBuilder supportClassBuilder = new EqualsSupportClassBuilder(processingEnv, equalsElement);
            for (Element member : equalsElement.getEnclosedElements()) {
                if (member.getKind() == ElementKind.FIELD) {
                    supportClassBuilder.addField((VariableElement) member);
                }
            }

            supportClassBuilder.addEqualsMethod();
            JavaFile javaFile = JavaFile.builder(SpecHelper.getPackageName(processingEnv, equalsElement), supportClassBuilder.build())
                    .build();

            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "fail to write component file.", equalsElement);
            }
        }
    }

    private void processHashCodeSupports(RoundEnvironment roundEnv) {
        Set<? extends Element> hashCodeElements = roundEnv.getElementsAnnotatedWith(HashCodeSupport.class);
        for (Element hashCodeElement : hashCodeElements) {
            HashCodeSupportClassBuilder supportClassBuilder = new HashCodeSupportClassBuilder(processingEnv, hashCodeElement);
            for (Element member : hashCodeElement.getEnclosedElements()) {
                if (member.getKind() == ElementKind.FIELD) {
                    supportClassBuilder.addField((VariableElement) member);
                }
            }

            supportClassBuilder.addHashCodeMethod();
            JavaFile javaFile = JavaFile.builder(SpecHelper.getPackageName(processingEnv, hashCodeElement), supportClassBuilder.build())
                    .build();

            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "fail to write component file.", hashCodeElement);
            }
        }
    }

    private void processToStringSupports(RoundEnvironment roundEnv) {
        Set<? extends Element> toStringElements = roundEnv.getElementsAnnotatedWith(ToStringSupport.class);
        for (Element toStringElement : toStringElements) {
            ToStringSupportClassBuilder supportClassBuilder = new ToStringSupportClassBuilder(processingEnv, toStringElement);
            for (Element member : toStringElement.getEnclosedElements()) {
                if (member.getKind() == ElementKind.FIELD) {
                    supportClassBuilder.addField((VariableElement) member);
                }
            }

            supportClassBuilder.addToStringMethod();
            JavaFile javaFile = JavaFile.builder(SpecHelper.getPackageName(processingEnv, toStringElement), supportClassBuilder.build())
                    .build();

            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "fail to write component file.", toStringElement);
            }
        }
    }
}
